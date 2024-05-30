package com.example.absensireact.helper;
import com.example.absensireact.model.Absensi;
import com.example.absensireact.repository.AbsensiRepository;
import com.example.absensireact.service.AbsensiService;
import org.apache.poi.hpsf.Date;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
public class AbsensiExportService {

    @Autowired
    private AbsensiRepository absensiRepository;

    @Autowired
    private AbsensiService absensiService;

    public ByteArrayInputStream RekapPerkaryawan() throws IOException {
        List<Absensi> absensiList = absensiRepository.findAll();

        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Absensi");

            // Header row
            Row headerRow = sheet.createRow(0);
            String[] headers = {"ID", "Nama","Tanggal", "Jam Masuk","Foto Masuk" , "Jam Pulang", "Foto Pulang", "Keterangan"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
            }

            // Data rows
            int rowIdx = 1;
            for (Absensi absensi : absensiList) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(rowIdx + 1);
                row.createCell(1).setCellValue(absensi.getUser().getUsername());
                row.createCell(2).setCellValue(absensi.getTanggalAbsen().toString());
                row.createCell(3).setCellValue(absensi.getJamMasuk());
                row.createCell(4).setCellValue(absensi.getFotoMasuk());
                row.createCell(5).setCellValue(absensi.getJamPulang());
                row.createCell(6).setCellValue(absensi.getFotoPulang());
                row.createCell(7).setCellValue(absensi.getStatusAbsen());
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        }
    }

    public ByteArrayInputStream RekapPerkaryawanByUserId(Long userId) throws IOException {
        List<Absensi> absensiList = absensiRepository.findabsensiByUserId(userId);

        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Rekap Perkaryawan");

            // Header row
            Row headerRow = sheet.createRow(0);
            String[] headers = {"ID", "Nama","Tanggal", "Jam Masuk","Foto Masuk" , "Jam Pulang", "Foto Pulang","Jan Kerja" ,"Keterangan"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
            }

            // Data rows
            int rowIdx = 1;
            for (Absensi absensi : absensiList) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(rowIdx + 1);
                row.createCell(1).setCellValue(absensi.getUser().getUsername());
                row.createCell(2).setCellValue(absensi.getTanggalAbsen().toString());
                row.createCell(3).setCellValue(absensi.getJamMasuk());
                row.createCell(4).setCellValue(absensi.getFotoMasuk());
                row.createCell(5).setCellValue(absensi.getJamPulang());
                row.createCell(6).setCellValue(absensi.getFotoPulang());
                row.createCell(7).setCellValue(absensi.getUser().getShift().getNamaShift());
                row.createCell(8).setCellValue(absensi.getStatusAbsen());

            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        }
    }


}
