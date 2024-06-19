package com.example.absensireact.helper;

import com.example.absensireact.exception.NotFoundException;
import com.example.absensireact.model.Cuti;
import com.example.absensireact.model.User;
import com.example.absensireact.repository.CutiRepository;

import com.example.absensireact.repository.UserRepository;
import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Optional;

@Component
public class CutiPDF {

    private final CutiRepository cutiRepository;

    @Autowired
    private UserRepository userRepository;

    public CutiPDF(CutiRepository cutiRepository) {
        this.cutiRepository = cutiRepository;
    }

    public void generatePDF(Long id, ByteArrayOutputStream baos) throws IOException {
        Cuti cuti = cutiRepository.findById(id).orElseThrow(() -> new NotFoundException("Id cuti tidak ditemukan"));
        User user = userRepository.findById(cuti.getUser().getId())
                .orElseThrow(() -> new NotFoundException("user tidak ditemukan"));

        String htmlContent = "<html><body>" +
                "<h1 style='text-align: center;'>Permohonan Cuti</h1>" +
                "<h2 style='text-align: center;'>SMK BINA NUSANTARA SEMARANG</h2>" +
                "<p style='text-align: center;'>Jl. Kemantren Raya No.5, RT.02/RW.04, Wonosari, Kec. Ngaliyan, Kota Semarang, Jawa Tengah 50186</p>" +
                "<hr/>" +
                "<p>Yth.Bpk Ibu Guru SMK Bina Nusantara Semarang</p>" +
                "<p>di tempat</p>" +
                "<p>Dengan hormat,</p>" +
                "<p>Yang bertanda tangan di bawah ini:</p>" +
                "<p>Nama: " + cuti.getUser().getUsername() + "</p>" +
                "<p>Jabatan: " + user.getJabatan().getNamaJabatan() + "</p>" +
                "<p>Tanggal Pengambilan Cuti: " + cuti.getAwalCuti() + "</p>" +
                "<p>Tanggal Kembali Kerja: " + cuti.getMasukKerja() + "</p>" +
                "<p>Keperluan: " + cuti.getKeperluan() + "</p>" +
                "<p>Bermaksud mengajukan cuti tahunan dari <strong>" + cuti.getAwalCuti() + " hingga </strong>" + cuti.getAkhirCuti() + ", saya akan mulai bekerja kembali pada <strong>" + cuti.getMasukKerja() + "</strong>.</p>" +
                "<p>Demikian permohonan cuti ini saya ajukan. Terima kasih atas perhatian Bapak/Ibu.</p>" +
                "<p>Tanggal: " + cuti.getAwalCuti() + "</p>" +
                "<br/><br/><br/>" +
                "<div style='display: flex; justify-content: space-between;'>" +
                "<div>" + cuti.getUser().getUsername() + "</div>" +
                "<div>Petinggi</div>" +
                "</div>" +
                "</body></html>";


        ConverterProperties properties = new ConverterProperties();
        HtmlConverter.convertToPdf(htmlContent, baos, properties);
    }
}
