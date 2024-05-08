package com.example.absensireact.impl;

import com.example.absensireact.exception.NotFoundException;
import com.example.absensireact.model.Absensi;
import com.example.absensireact.model.User;
import com.example.absensireact.repository.AbsensiRepository;
import com.example.absensireact.repository.UserRepository;
import com.example.absensireact.service.AbsensiService;
import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;


@Service
public class AbsensiImpl implements AbsensiService {
    static final String DOWNLOAD_URL = "https://firebasestorage.googleapis.com/v0/b/absensireact.appspot.com/o/%s?alt=media";

    private final AbsensiRepository absensiRepository;

    private final UserRepository userRepository;


    public AbsensiImpl(AbsensiRepository absensiRepository, UserRepository userRepository) {
        this.absensiRepository = absensiRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<Absensi> getAllAbsensi(){
        return  absensiRepository.findAll();
    }



    public Absensi PostAbsensi(Long userId, MultipartFile image , String keteranganTerlambat) throws IOException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User dengan ID " + userId + " tidak ditemukan."));

        Date masuk = new Date();
        Date tanggalHariIni = truncateTime(new Date()); // Assuming truncateTime truncates time portion
        String keterangan = (getHourOfDay(masuk) < 7) ? "Tidak Terlambat" : "Terlambat";

        Absensi absensi = new Absensi();
        absensi.setUser(user);
        absensi.setTanggalAbsen(tanggalHariIni);
        absensi.setJamMasuk(String.valueOf(masuk));
        absensi.setJamPulang("-");
        absensi.setKeteranganTerlambat(keteranganTerlambat);
        absensi.setStatusAbsen(keterangan);

        String fotoUrl = uploadFile(image, "foto_masuk_" + userId);
        absensi.setFotoMasuk(fotoUrl);

        return absensiRepository.save(absensi);
    }

    @Override
    public Absensi PulangLebihAwal(Long id, Absensi absensi){
        Optional<Absensi> absensivalid = absensiRepository.findById(id);
        if (!absensivalid.isPresent()) {
            Date pulangAwal = new Date();

            absensi.setKeteranganPulangAwal(absensi.getKeteranganPulangAwal());
            absensi.setJamPulang(String.valueOf(pulangAwal));
            return  absensiRepository.save(absensi);
        }
        throw new NotFoundException("Absensi tidak ditemukan");

    }
    public Absensi PutPulang(Long id, MultipartFile image) throws IOException, NotFoundException {
        Absensi absensi = absensiRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Absensi tidak ditemukan"));

        Date pulang = new Date();
        int jamMasuk = getHourOfDay(absensi.getTanggalAbsen());

        if (jamMasuk > 14) {
            absensi.setJamPulang(String.valueOf(pulang));
            String fotoUrl = uploadFile(image, "foto_pulang_" + absensi.getUser().getId());
            absensi.setFotoPulang(fotoUrl);
            return absensiRepository.save(absensi);
        } else {
            throw new NotActiveException("Belum Waktunya Pulang");
        }
    }

    private Date truncateTime(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    private int getHourOfDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    private String uploadFile(MultipartFile multipartFile, String fileName) throws IOException {
        String timestamp = String.valueOf(System.currentTimeMillis());
        String folderPath = "fotoAbsen/";
        String fullPath = folderPath + timestamp + "_" + fileName;
        BlobId blobId = BlobId.of("absensireact.appspot.com", fullPath);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("media").build();
        Credentials credentials = GoogleCredentials.fromStream(new FileInputStream("./src/main/resources/FirebaseConfig.json"));
        Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
        storage.create(blobInfo, multipartFile.getBytes());
        return String.format(DOWNLOAD_URL, URLEncoder.encode(fullPath, StandardCharsets.UTF_8));
    }

    @Override
    public Optional<Absensi> getAbsensiById(Long id) {
        return absensiRepository.findById(id);
    }

    @Override
    public Absensi updateAbsensi(Long id, Absensi absensi) {
        return absensiRepository.findById(id)
                .map(existingAbsensi -> {
                    existingAbsensi.setTanggalAbsen(absensi.getTanggalAbsen());
                    existingAbsensi.setJamMasuk(absensi.getJamMasuk());
                    existingAbsensi.setJamPulang(absensi.getJamPulang());
                    existingAbsensi.setLokasiMasuk(absensi.getLokasiMasuk());
                    existingAbsensi.setLokasiPulang(absensi.getLokasiPulang());
                    existingAbsensi.setKeteranganTerlambat(absensi.getKeteranganTerlambat());
                    existingAbsensi.setFotoMasuk(absensi.getFotoMasuk());
                    existingAbsensi.setFotoPulang(absensi.getFotoPulang());
                    existingAbsensi.setStatus(absensi.getStatus());
                    existingAbsensi.setStatusAbsen(absensi.getStatusAbsen());
                    existingAbsensi.setKeteranganIzin(absensi.getKeteranganIzin());
                    existingAbsensi.setKeteranganPulang(absensi.getKeteranganPulang());
                    existingAbsensi.setKeteranganPulangAwal(absensi.getKeteranganPulangAwal());
                    return absensiRepository.save(existingAbsensi);
                })
                .orElseThrow(() -> new NotFoundException("Absensi not found with id: " + id));
    }

    @Override
    public void deleteAbsensi(Long id) {
        if (absensiRepository.existsById(id)) {
            absensiRepository.deleteById(id);
        } else {
            throw new NotFoundException("Absensi not found with id: " + id);
        }
    }

    @Override
    public List<Absensi> getAbsensiByUserId(Long userId) {
        return absensiRepository.findByUser_Id(userId);
    }
}
