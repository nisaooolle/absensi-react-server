package com.example.absensireact.service;

import com.example.absensireact.model.Absensi;
import com.example.absensireact.model.User;
import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

public interface  AbsensiService {
    static final String DOWNLOAD_URL = "https://firebasestorage.googleapis.com/v0/b/lowongan-a0c4a.appspot.com/o/%s?alt=media";


    List<Absensi> getAllAbsensi();



    Optional<Absensi> getAbsensiById(Long id);

    Absensi updateAbsensi(Long id, Absensi absensi);


    void deleteAbsensi(Long id);

    List<Absensi> getAbsensiByUserId(Long userId);



    Absensi PostAbsensi(Long userId, MultipartFile image) throws IOException;

    Absensi PutPulang( Long userId, MultipartFile image) throws IOException;

    String uploadFoto(MultipartFile image) throws IOException;


}
