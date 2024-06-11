package com.example.absensireact.exel;

import com.example.absensireact.model.Absensi;
import com.example.absensireact.repository.AbsensiRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class ExcelAbsensiMingguan {

    @Autowired
    private AbsensiRepository absensiRepository;

    public void excelAbsensiMingguan(Date tanggalAwal, Date tanggalAkhir, HttpServletResponse response) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Absensi-mingguan");
        int rowNum = 0; // Deklarasi dan inisialisasi variabel rowNum

        // Cell styles
        CellStyle styleHeader = workbook.createCellStyle();
        styleHeader.setAlignment(HorizontalAlignment.CENTER);
        styleHeader.setVerticalAlignment(VerticalAlignment.CENTER);
        styleHeader.setBorderTop(BorderStyle.THIN);
        styleHeader.setBorderRight(BorderStyle.THIN);
        styleHeader.setBorderBottom(BorderStyle.THIN);
        styleHeader.setBorderLeft(BorderStyle.THIN);

        Font fontWhite = workbook.createFont();
        fontWhite.setColor(IndexedColors.WHITE.getIndex()); // Set font color to white

        CellStyle styleNumber = workbook.createCellStyle();
        styleNumber.setAlignment(HorizontalAlignment.RIGHT);
        styleNumber.setVerticalAlignment(VerticalAlignment.CENTER);
        styleNumber.setBorderTop(BorderStyle.THIN);
        styleNumber.setBorderRight(BorderStyle.THIN);
        styleNumber.setBorderBottom(BorderStyle.THIN);
        styleNumber.setBorderLeft(BorderStyle.THIN);
        styleNumber.setFont(fontWhite); // Set font to white

        // Conditional formatting colors
        CellStyle styleColor1 = workbook.createCellStyle();
        styleColor1.cloneStyleFrom(styleNumber);
        styleColor1.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
        styleColor1.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        CellStyle styleColor2 = workbook.createCellStyle();
        styleColor2.cloneStyleFrom(styleNumber);
        styleColor2.setFillForegroundColor(IndexedColors.BLUE.getIndex());
        styleColor2.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        CellStyle styleColor3 = workbook.createCellStyle();
        styleColor3.cloneStyleFrom(styleNumber);
        styleColor3.setFillForegroundColor(IndexedColors.RED.getIndex());
        styleColor3.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        CellStyle styleColor4 = workbook.createCellStyle();
        styleColor4.cloneStyleFrom(styleNumber);
        styleColor4.setFillForegroundColor(IndexedColors.GREEN.getIndex());
        styleColor4.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        // Header style with red background and white text
        CellStyle styleHeaderRed = workbook.createCellStyle();
        styleHeaderRed.cloneStyleFrom(styleHeader);
        styleHeaderRed.setFillForegroundColor(IndexedColors.RED.getIndex());
        styleHeaderRed.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        styleHeaderRed.setFont(fontWhite); // Set font color to white

        List<Absensi> absensiList = absensiRepository.findByMingguan(tanggalAwal, tanggalAkhir);

        Map<String, List<Absensi>> userAbsensiMap = new HashMap<>();
        for (Absensi absensi : absensiList) {
            userAbsensiMap.computeIfAbsent(absensi.getUser().getUsername(), k -> new ArrayList<>()).add(absensi);
        }

        for (Map.Entry<String, List<Absensi>> entry : userAbsensiMap.entrySet()) {
            String userName = entry.getKey();
            List<Absensi> userAbsensi = entry.getValue();
            String position = userAbsensi.get(0).getUser().getJabatan().getNamaJabatan();

            // Name and Position row
            Row nameRow = sheet.createRow(rowNum++);
            Cell nameCell = nameRow.createCell(0);
            nameCell.setCellValue("Nama :  " + userName);
            nameCell.setCellStyle(styleHeader);

            Row positionRow = sheet.createRow(rowNum++);
            Cell positionCell = positionRow.createCell(0);
            positionCell.setCellValue("Jabatan :   " + position);
            positionCell.setCellStyle(styleHeader);

            // Header row
            Row headerRow = sheet.createRow(rowNum++);
            String[] headers = {"No", "Tanggal Absen", "Jam Masuk", "Jam Pulang", "Keterangan"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
//                if (rowNum == 3) { // Apply red background and white text only for the first header row
//                    cell.setCellStyle(styleHeaderRed);
//                } else {
//                    cell.setCellStyle(styleHeader);
//                }
            }

            // Data rows
            int userRowNum = 1;
            for (Absensi absensi : userAbsensi) {
                Row row = sheet.createRow(rowNum++);
                Cell cell0 = row.createCell(0);
                cell0.setCellValue(userRowNum++);
                cell0.setCellStyle(styleNumber);

                Cell cell1 = row.createCell(1);
                cell1.setCellValue(new SimpleDateFormat("yyyy-MM-dd").format(absensi.getTanggalAbsen()));
                cell1.setCellStyle(styleNumber);

                Cell cell2 = row.createCell(2);
                cell2.setCellValue(absensi.getJamMasuk());
                cell2.setCellStyle(styleNumber);

                Cell cell3 = row.createCell(3);
                cell3.setCellValue(absensi.getJamPulang());
                cell3.setCellStyle(styleNumber);

                Cell cell4 = row.createCell(4);
                cell4.setCellValue(absensi.getStatusAbsen());
                cell4.setCellStyle(styleNumber);

                CellStyle styleColor = null;
                switch (absensi.getStatusAbsen()) {
                    case "Lebih Awal":
                        styleColor = styleColor4;
                        break;
                    case "Terlambat":
                        styleColor = styleColor1;
                        break;
                    case "Izin":
                        styleColor = styleColor2;
                        break;
                }
                if (styleColor != null) {
                    for (int i = 0; i < headers.length; i++) {
                        row.getCell(i).setCellStyle(styleColor);
                    }
                }
            }
            rowNum++;
        }

        // Adjust column width
        for (int i = 0; i < 5; i++) {
            sheet.autoSizeColumn(i);
        }

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=AbsensiBulanan.xlsx");
        workbook.write(response.getOutputStream());
        workbook.close();
    }

    public void excelAbsensiHarian(Date tanggal, HttpServletResponse response) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Absensi-harian");
        int rowNum = 0; // Deklarasi dan inisialisasi variabel rowNum

        // Cell styles
        CellStyle styleHeader = workbook.createCellStyle();
        styleHeader.setAlignment(HorizontalAlignment.CENTER);
        styleHeader.setVerticalAlignment(VerticalAlignment.CENTER);
        styleHeader.setBorderTop(BorderStyle.THIN);
        styleHeader.setBorderRight(BorderStyle.THIN);
        styleHeader.setBorderBottom(BorderStyle.THIN);
        styleHeader.setBorderLeft(BorderStyle.THIN);

        Font fontWhite = workbook.createFont();
        fontWhite.setColor(IndexedColors.WHITE.getIndex()); // Set font color to white

        CellStyle styleNumber = workbook.createCellStyle();
        styleNumber.setAlignment(HorizontalAlignment.RIGHT);
        styleNumber.setVerticalAlignment(VerticalAlignment.CENTER);
        styleNumber.setBorderTop(BorderStyle.THIN);
        styleNumber.setBorderRight(BorderStyle.THIN);
        styleNumber.setBorderBottom(BorderStyle.THIN);
        styleNumber.setBorderLeft(BorderStyle.THIN);
        styleNumber.setFont(fontWhite); // Set font to white

        // Conditional formatting colors
        CellStyle styleColor1 = workbook.createCellStyle();
        styleColor1.cloneStyleFrom(styleNumber);
        styleColor1.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
        styleColor1.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        CellStyle styleColor2 = workbook.createCellStyle();
        styleColor2.cloneStyleFrom(styleNumber);
        styleColor2.setFillForegroundColor(IndexedColors.BLUE.getIndex());
        styleColor2.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        CellStyle styleColor3 = workbook.createCellStyle();
        styleColor3.cloneStyleFrom(styleNumber);
        styleColor3.setFillForegroundColor(IndexedColors.RED.getIndex());
        styleColor3.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        CellStyle styleColor4 = workbook.createCellStyle();
        styleColor4.cloneStyleFrom(styleNumber);
        styleColor4.setFillForegroundColor(IndexedColors.GREEN.getIndex());
        styleColor4.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        // Header style with red background and white text
        CellStyle styleHeaderRed = workbook.createCellStyle();
        styleHeaderRed.cloneStyleFrom(styleHeader);
        styleHeaderRed.setFillForegroundColor(IndexedColors.RED.getIndex());
        styleHeaderRed.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        styleHeaderRed.setFont(fontWhite); // Set font color to white

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(tanggal);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH) + 1; // Calendar.MONTH is zero-based
        int year = calendar.get(Calendar.YEAR);

        List<Absensi> absensiList = absensiRepository.findByTanggalAbsen(day, month, year);

        Map<String, List<Absensi>> userAbsensiMap = new HashMap<>();
        for (Absensi absensi : absensiList) {
            userAbsensiMap.computeIfAbsent(absensi.getUser().getUsername(), k -> new ArrayList<>()).add(absensi);
        }

        for (Map.Entry<String, List<Absensi>> entry : userAbsensiMap.entrySet()) {
            String userName = entry.getKey();
            List<Absensi> userAbsensi = entry.getValue();
            String position = userAbsensi.get(0).getUser().getJabatan().getNamaJabatan();

            // Name and Position row
            Row nameRow = sheet.createRow(rowNum++);
            Cell nameCell = nameRow.createCell(0);
            nameCell.setCellValue("Nama :  " + userName);
            nameCell.setCellStyle(styleHeader);

            Row positionRow = sheet.createRow(rowNum++);
            Cell positionCell = positionRow.createCell(0);
            positionCell.setCellValue("Jabatan :   " + position);
            positionCell.setCellStyle(styleHeader);

            // Header row
            Row headerRow = sheet.createRow(rowNum++);
            String[] headers = {"No", "Tanggal Absen", "Jam Masuk", "Jam Pulang", "Keterangan"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
//                if (rowNum == 3) { // Apply red background and white text only for the first header row
//                    cell.setCellStyle(styleHeaderRed);
//                } else {
//                    cell.setCellStyle(styleHeader);
//                }
            }

            // Data rows
            int userRowNum = 1;
            for (Absensi absensi : userAbsensi) {
                Row row = sheet.createRow(rowNum++);
                Cell cell0 = row.createCell(0);
                cell0.setCellValue(userRowNum++);
                cell0.setCellStyle(styleNumber);

                Cell cell1 = row.createCell(1);
                cell1.setCellValue(new SimpleDateFormat("yyyy-MM-dd").format(absensi.getTanggalAbsen()));
                cell1.setCellStyle(styleNumber);

                Cell cell2 = row.createCell(2);
                cell2.setCellValue(absensi.getJamMasuk());
                cell2.setCellStyle(styleNumber);

                Cell cell3 = row.createCell(3);
                cell3.setCellValue(absensi.getJamPulang());
                cell3.setCellStyle(styleNumber);

                Cell cell4 = row.createCell(4);
                cell4.setCellValue(absensi.getStatusAbsen());
                cell4.setCellStyle(styleNumber);

                CellStyle styleColor = null;
                switch (absensi.getStatusAbsen()) {
                    case "Lebih Awal":
                        styleColor = styleColor4;
                        break;
                    case "Terlambat":
                        styleColor = styleColor1;
                        break;
                    case "Izin":
                        styleColor = styleColor2;
                        break;
                }
                if (styleColor != null) {
                    for (int i = 0; i < headers.length; i++) {
                        row.getCell(i).setCellStyle(styleColor);
                    }
                }
            }
            rowNum++;
        }

        // Adjust column width
        for (int i = 0; i < 5; i++) {
            sheet.autoSizeColumn(i);
        }

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=AbsensiHarian.xlsx");
        workbook.write(response.getOutputStream());
        workbook.close();
    }

    }
