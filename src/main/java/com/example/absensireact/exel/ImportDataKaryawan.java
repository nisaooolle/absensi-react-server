package com.example.absensireact.exel;

import com.example.absensireact.exception.BadRequestException;
import com.example.absensireact.exception.NotFoundException;
import com.example.absensireact.model.*;
import com.example.absensireact.repository.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Component
public class ImportDataKaryawan {

    @Autowired
    private UserRepository siswaRepository;

    @Autowired
    PasswordEncoder encoder;

    public void importKaryawan(MultipartFile file, Admin admin) throws IOException {
        Workbook workbook = new XSSFWorkbook(file.getInputStream());
        Sheet sheet = workbook.getSheetAt(0);

        List<User> userList = new ArrayList<>();

        // Mulai dari baris ke-4 (baris indeks 3) untuk melewati header dan catatan
        for (int i = 5; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row != null) {
                Cell firstCell = row.getCell(0);
                if (firstCell != null && "No".equalsIgnoreCase(getCellValue(firstCell))) {
                    continue;
                }

                User user = new User();

                Cell emailCell = row.getCell(1);
                Cell usernameCell = row.getCell(2);
                Cell passwordCell = row.getCell(3);
                Cell startKerjaCell = row.getCell(4);
                Cell statusKerjaCell = row.getCell(5);
                Cell roleCell = row.getCell(6);

                // Validasi bahwa kolom email, username, dan password tidak kosong
                if (emailCell != null && usernameCell != null && passwordCell != null) {
                    user.setEmail(getCellValue(emailCell));
                    user.setUsername(getCellValue(usernameCell));
                    user.setPassword(encoder.encode(getCellValue(passwordCell)));
                    user.setStartKerja(getCellValue(startKerjaCell));
                    user.setStatusKerja(getCellValue(statusKerjaCell));
                    user.setRole(getCellValue(roleCell));

                    user.setAdmin(admin); // Set admin berdasarkan admin yang di-passing
                    userList.add(user); // Tambahkan ke daftar user
                }
            }
        }

        // Simpan semua user ke dalam database
        siswaRepository.saveAll(userList);
        workbook.close();
    }

    private String getCellValue(Cell cell) {
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                return String.valueOf((int)cell.getNumericCellValue());
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            default:
                return "";
        }
    }
}