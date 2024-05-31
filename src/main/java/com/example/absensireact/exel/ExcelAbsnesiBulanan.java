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
public class ExcelAbsnesiBulanan {
    @Autowired
    private AbsensiRepository absensiRepository;

    public void excelAbsensiBulanan(int month, int year, HttpServletResponse response) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Absensi-Bulanan");

        // Cell styles
        CellStyle styleHeader = workbook.createCellStyle();
        styleHeader.setAlignment(HorizontalAlignment.CENTER);
        styleHeader.setVerticalAlignment(VerticalAlignment.CENTER);
        styleHeader.setBorderTop(BorderStyle.THIN);
        styleHeader.setBorderRight(BorderStyle.THIN);
        styleHeader.setBorderBottom(BorderStyle.THIN);
        styleHeader.setBorderLeft(BorderStyle.THIN);

        CellStyle styleNumber = workbook.createCellStyle();
        styleNumber.setAlignment(HorizontalAlignment.RIGHT);
        styleNumber.setVerticalAlignment(VerticalAlignment.CENTER);
        styleNumber.setBorderTop(BorderStyle.THIN);
        styleNumber.setBorderRight(BorderStyle.THIN);
        styleNumber.setBorderBottom(BorderStyle.THIN);
        styleNumber.setBorderLeft(BorderStyle.THIN);

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

        // Fetch data
        List<Absensi> absensiList = absensiRepository.findByMonthAndYear(month, year);

        // Group by user
        Map<String, List<Absensi>> userAbsensiMap = new HashMap<>();
        for (Absensi absensi : absensiList) {
            userAbsensiMap.computeIfAbsent(absensi.getUser().getUsername(), k -> new ArrayList<>()).add(absensi);
        }

        int rowNum = 0;

        // Title row
        Row titleRow = sheet.createRow(rowNum++);
        Cell titleCell = titleRow.createCell(0);
        titleCell.setCellValue("DATA ABSENSI BULAN : " + month + " - " + year);
        titleCell.setCellStyle(styleHeader);
        sheet.addMergedRegion(new CellRangeAddress(rowNum - 1, rowNum - 1, 0, 5)); // Merging cells for title

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
                cell.setCellStyle(styleHeader);
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

                switch (absensi.getStatusAbsen()) {
                    case "Lebih Awal":
                        row.setRowStyle(styleColor4);
                        break;
                    case "Terlambat":
                        row.setRowStyle(styleColor1);
                        break;
                    case "Izin":
                        row.setRowStyle(styleColor2);
                        break;
                }
            }
            // Add a blank row between each user's table for readability
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
}
