package com.example.absensireact.exel;

import com.example.absensireact.exception.BadRequestException;
import com.example.absensireact.exception.NotFoundException;
import com.example.absensireact.model.Admin;
import com.example.absensireact.model.Organisasi;
import com.example.absensireact.model.Shift;
import com.example.absensireact.model.User;
import com.example.absensireact.repository.UserRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
@Component
public class ExcelDataKaryawan {

    @Autowired
    private UserRepository siswaRepository;


    public static ByteArrayInputStream karyawanToExcel(List<User> users) {
        try (XSSFWorkbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            XSSFSheet sheet = workbook.createSheet("Karyawan");

            // Buat gaya untuk header
            CellStyle headerCellStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerCellStyle.setFont(headerFont);
            headerCellStyle.setBorderBottom(BorderStyle.THIN);
            headerCellStyle.setBorderTop(BorderStyle.THIN);
            headerCellStyle.setBorderLeft(BorderStyle.THIN);
            headerCellStyle.setBorderRight(BorderStyle.THIN);
            headerCellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            // Buat Header Row
            String[] headers = {"No", "Email", "Username", "Password", "Start Kerja", "Status Kerja", "Role"};
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerCellStyle);
            }

            // Buat gaya untuk data
            CellStyle dataCellStyle = workbook.createCellStyle();
            dataCellStyle.setBorderBottom(BorderStyle.THIN);
            dataCellStyle.setBorderTop(BorderStyle.THIN);
            dataCellStyle.setBorderLeft(BorderStyle.THIN);
            dataCellStyle.setBorderRight(BorderStyle.THIN);

            // Isi Data Karyawan
            int rowIdx = 1;
            for (User user : users) {
                Row row = sheet.createRow(rowIdx++);

                Cell cell0 = row.createCell(0);
                cell0.setCellValue(user.getId());
                cell0.setCellStyle(dataCellStyle);

                Cell cell1 = row.createCell(1);
                cell1.setCellValue(user.getEmail());
                cell1.setCellStyle(dataCellStyle);

                Cell cell2 = row.createCell(2);
                cell2.setCellValue(user.getUsername());
                cell2.setCellStyle(dataCellStyle);

                Cell cell3 = row.createCell(3);
                cell3.setCellValue(user.getPassword());
                cell3.setCellStyle(dataCellStyle);

                Cell cell4 = row.createCell(4);
                cell4.setCellValue(user.getStartKerja());
                cell4.setCellStyle(dataCellStyle);

                Cell cell5 = row.createCell(5);
                cell5.setCellValue(user.getStatusKerja());
                cell5.setCellStyle(dataCellStyle);

                Cell cell6 = row.createCell(6);
                cell6.setCellValue(user.getRole());
                cell6.setCellStyle(dataCellStyle);
            }

            // Atur lebar kolom otomatis
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void templateExcelKaryawan(HttpServletResponse response) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Template Siswa");

        CellStyle styleTitle = workbook.createCellStyle();
        styleTitle.setAlignment(HorizontalAlignment.CENTER);
        styleTitle.setVerticalAlignment(VerticalAlignment.CENTER);
        Font titleFont = workbook.createFont();
        titleFont.setBold(true);
        styleTitle.setFont(titleFont);

        CellStyle styleHeader = workbook.createCellStyle();
        styleHeader.setAlignment(HorizontalAlignment.LEFT);
        styleHeader.setVerticalAlignment(VerticalAlignment.CENTER);
        styleHeader.setBorderTop(BorderStyle.THIN);
        styleHeader.setBorderRight(BorderStyle.THIN);
        styleHeader.setBorderBottom(BorderStyle.THIN);
        styleHeader.setBorderLeft(BorderStyle.THIN);
        styleHeader.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
        styleHeader.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        CellStyle styleNote = workbook.createCellStyle();
        styleNote.setAlignment(HorizontalAlignment.LEFT);
        styleNote.setVerticalAlignment(VerticalAlignment.CENTER);
        Font noteFont = workbook.createFont();
        noteFont.setItalic(true);
        styleNote.setFont(noteFont);

        int rowNum = 0;

        // Title row
        Row titleRow = sheet.createRow(rowNum++);
        Cell titleCell = titleRow.createCell(0);
        titleCell.setCellValue("DATA Karyawan");
        titleCell.setCellStyle(styleTitle);
        sheet.addMergedRegion(new CellRangeAddress(rowNum - 1, rowNum - 1, 0, 7)); // Adjusted for new "Kelas" column
        rowNum++;

        // Note row
        Row noteRow = sheet.createRow(rowNum++);
        Cell noteCell = noteRow.createCell(0);
        noteCell.setCellValue("Catatan: Jangan berikan spasi pada awal kata saat mengisi data.");
        noteCell.setCellStyle(styleNote);
        sheet.addMergedRegion(new CellRangeAddress(rowNum - 1, rowNum - 1, 0, 7)); // Adjusted for new "Kelas" column
        rowNum++;

        // Header row
        Row headerRow = sheet.createRow(rowNum++);
        String[] headers = {"No", "Email", "Username", "Password", "Start Kerja", "Status Kerja", "Role"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(styleHeader);
        }

        // Adjust column width
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=TemplateSiswa.xlsx");

        try (OutputStream outputStream = response.getOutputStream()) {
            workbook.write(outputStream);
        }

        workbook.close();
    }
}