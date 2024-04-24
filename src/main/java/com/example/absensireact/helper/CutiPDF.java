package com.example.absensireact.helper;

import com.example.absensireact.model.Cuti;
import com.example.absensireact.model.Karyawan;
import com.example.absensireact.repository.KaryawanRepository;
import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Component
public class CutiPDF {

    private final KaryawanRepository karyawanRepository;

    public CutiPDF(KaryawanRepository karyawanRepository) {
        this.karyawanRepository = karyawanRepository;
    }

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
        Karyawan karyawan = karyawanRepository.findJabatanByuserId(cuti.getUser().getId());
        // HTML content
        String htmlContent = "<html><body>" +
                "<h1  style=' text-align: center;' >Permohonan Cuti</h1>" +
                "<h1  style=' text-align: center;' >SMK BINA NUSANTARA SEMARANG</h1>\n" +
                "<p style=` text-align:center;`>Jl. Kemantren Raya No.5, RT.02/RW.04, Wonosari, Kec. Ngaliyan, Kota Semarang, Jawa Tengah </p>\n" +
                "<p style=` text-align:center;`>50186 </p>" +
                "<hr/>"+
                "<div style=`items-align: center;`>"+
                "<p style=` `>Yth. HRD PT. SMK Bina Nusantara Semarang  </p>" +
                "<p style=``>di tempat </p>\n" +
                "<p style=` ;`>Dengan hormat,</p>" +
                "<p style=``>yang bertanda tangan dibawah ini:</p>\n" +
                "<p style=' '>Nama: " + cuti.getUser().getUsername() + "</p>\n" +
                "<p style=''>Jabatan: " + karyawan.getJabatan() + "</p>\n" +
                "<p style=' '>Tanggal Pengambilan Cuti: " + cuti.getAwalCuti() + "</p>\n" +
                "<p style=' '>Tanggal Kembali Kerja: " + cuti.getMasukKerja() + "</p>\n" +
                "<p style=' '>Keperluan: " + cuti.getKeperluan() + "</p>\n" +
                "<p style=' '>Bermaksud mengajukan cuti tahunan dari <span style='font-weight: bold;'>" + cuti.getAwalCuti() + " hingga </span></p>\n" +
                "<p style=' '><span style='font-weight: bold;'>" + cuti.getAkhirCuti() + "</span> ,  saya akan mulai bekerja kembali pada <span> " + cuti.getMasukKerja() + "</spam> </p>\n" +
                "<p style=' '>Demikian permohonan cuti ini saya ajukan. Terimakasih atas perhatian Bapak/Ibu.</p>\n" +
                "<p style=' '>Tanggal " + cuti.getAwalCuti() + "</p>\n" +
                "<br/>\n"+
                "<br/>\n"+
                "<br/>\n"+
                "<div style='display: flex;'>"+
                "<div style='flex: 1;'>"+cuti.getUser().getUsername() +"</div>\n"+
                "<div style='flex: 1;'>Petinggi</div>\n"+
                "</div>\n" +
                "</div>"+

                "</body></html>";

        // Convert HTML to PDF
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ConverterProperties properties = new ConverterProperties();
        HtmlConverter.convertToPdf(htmlContent, baos, properties);

        // Set response headers
        response.setContentType("application/pdf");
        response.setHeader("Content-disposition", "attachment; filename=cuti.pdf");
        response.setContentLength(baos.size());

        // Write PDF content to response output stream
        response.getOutputStream().write(baos.toByteArray());
        response.getOutputStream().flush();
    }
}
