package com.example.absensireact.helper;

import com.example.absensireact.model.Cuti;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Component
public class CutiPDF {

    public void generatePDF(Cuti cuti,  ByteArrayOutputStream baos) throws IOException {
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
                contentStream.beginText();
                contentStream.newLineAtOffset(50, 700);
                contentStream.showText("Detail Cuti");
                contentStream.endText();

                contentStream.setFont(PDType1Font.HELVETICA, 12);
                contentStream.beginText();
                contentStream.newLineAtOffset(50, 650);
                contentStream.showText("ID: " + cuti.getId());
                contentStream.newLine();
                contentStream.showText("Awal Cuti: " + cuti.getAwalCuti());
                contentStream.newLine();
                contentStream.showText("Akhir Cuti: " + cuti.getAkhirCuti());
                contentStream.newLine();
                contentStream.showText("Masuk Kerja: " + cuti.getMasukKerja());
                contentStream.newLine();
                contentStream.showText("Keperluan: " + cuti.getKeperluan());
                contentStream.newLine();
                contentStream.showText("Status: " + cuti.getStatus());
                contentStream.newLine();
                contentStream.showText("User ID: " + cuti.getUser().getId());
                contentStream.endText();
            }

            document.save(baos);
        }
    }

    public void downloadPDF(Cuti cuti, HttpServletResponse response) throws IOException {
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 16);
                float textWidth = PDType1Font.HELVETICA_BOLD.getStringWidth("Permohonan Cuti") / 1000f * 12; // Ukuran font 12

// Mendapatkan setengah lebar halaman
                float halfPageWidth = page.getMediaBox().getWidth() / 2;

// Hitung offset X untuk menempatkan teks di tengah horizontal
                float offsetX = halfPageWidth - (textWidth / 2);

// Teks Detail Cuti di tengah halaman
                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
                contentStream.newLineAtOffset(offsetX, 700);
                contentStream.showText("Permohonan Cuti");
                contentStream.endText();

                // Teks ID
                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA, 12);
                contentStream.newLineAtOffset(50, 650);
                contentStream.showText("ID: " + cuti.getId());
                contentStream.newLine();
                contentStream.endText();

                // Teks Awal Cuti
                contentStream.beginText();
                contentStream.newLineAtOffset(50, 630);
                contentStream.showText("Awal Cuti: " + cuti.getAwalCuti());
                contentStream.newLine();
                contentStream.endText();

                // Teks Akhir Cuti
                contentStream.beginText();
                contentStream.newLineAtOffset(50, 610);
                contentStream.showText("Akhir Cuti: " + cuti.getAkhirCuti());
                contentStream.newLine();
                contentStream.endText();

                // Teks Masuk Kerja
                contentStream.beginText();
                contentStream.newLineAtOffset(50, 590);
                contentStream.showText("Masuk Kerja: " + cuti.getMasukKerja());
                contentStream.newLine();
                contentStream.endText();

                // Teks Keperluan
                contentStream.beginText();
                contentStream.newLineAtOffset(50, 570);
                contentStream.showText("Keperluan: " + cuti.getKeperluan());
                contentStream.newLine();
                contentStream.endText();

                // Teks Status
                contentStream.beginText();
                contentStream.newLineAtOffset(50, 550);
                contentStream.showText("Status: " + cuti.getStatus());
                contentStream.newLine();
                contentStream.endText();

                // Teks User ID
                contentStream.beginText();
                contentStream.newLineAtOffset(50, 530); // Mengatur jarak vertikal antara baris
                contentStream.showText("User ID: " + cuti.getUser().getId());
                contentStream.newLine();
                contentStream.endText();
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            document.save(baos);
            document.close();

            // Set response headers
            response.setContentType("application/pdf");
            response.setHeader("Content-disposition", "attachment; filename=cuti.pdf");
            response.setContentLength(baos.size());

            // Write PDF content to response output stream
            response.getOutputStream().write(baos.toByteArray());
            response.getOutputStream().flush();
        }
    }
}
