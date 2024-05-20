package com.example.absensireact.impl;

import com.example.absensireact.exception.InternalErrorException;
import com.example.absensireact.exception.NotFoundException;
import com.example.absensireact.model.Admin;
import com.example.absensireact.model.Organisasi;
import com.example.absensireact.repository.AdminRepository;
import com.example.absensireact.repository.OrganisasiRepository;
import com.example.absensireact.repository.UserRepository;
import com.example.absensireact.service.OrganisasiService;
import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

@Service
public class OrganisasiImpl implements OrganisasiService {
    static final String DOWNLOAD_URL = "https://firebasestorage.googleapis.com/v0/b/absensireact.appspot.com/o/%s?alt=media";

    private final OrganisasiRepository organisasiRepository;
    private final UserRepository userRepository;
    private final AdminRepository adminRepository;

    public OrganisasiImpl(OrganisasiRepository organisasiRepository, UserRepository userRepository, AdminRepository adminRepository) throws IOException {
        this.organisasiRepository = organisasiRepository;
        this.userRepository = userRepository;
        this.adminRepository = adminRepository;

    }
    @Override
    public List<Organisasi> getAllOrganisasi(){
        return organisasiRepository.findAll();
    }

    @Override
    public Optional<Organisasi>GetOrganisasiById(Long id){
        return organisasiRepository.findById(id);
    }



    @Override
    public Optional<Organisasi> GetByIdAdmin(Long idAdmin){
        return  organisasiRepository.findOrganisasiByIdAdmin(idAdmin);}
    @Override
    public Optional<Organisasi> GetAllBYId(Long id){
        return organisasiRepository.findById(id);
    }

    @Override
    public Organisasi TambahOrganisasi(Long idAdmin, Organisasi organisasi, MultipartFile image) throws IOException {
        Optional<Admin> adminOptional = Optional.ofNullable(adminRepository.findById(idAdmin).orElse(null));

        if (!adminOptional.isPresent()) {
            throw new NotFoundException("Id Admin tidak ditemukan");
        }
        Admin admin = adminOptional.get();

        Optional<Organisasi> existingOrganisasi = organisasiRepository.findOrganisasiByIdAdmin(idAdmin);
        if (existingOrganisasi.isPresent()) {
            throw new IllegalStateException("Admin sudah memiliki organisasi");
        }

        organisasi.setNamaOrganisasi(organisasi.getNamaOrganisasi());
        organisasi.setAlamat(organisasi.getAlamat());
        organisasi.setKecamatan(organisasi.getKecamatan());
        organisasi.setKabupaten(organisasi.getKabupaten());
        organisasi.setProvinsi(organisasi.getProvinsi());
        organisasi.setNomerTelepon(organisasi.getNomerTelepon());
        organisasi.setEmailOrganisasi(organisasi.getEmailOrganisasi());
        organisasi.setAdmin(admin);
        String file = uploadFoto(image, "_organisasi_" + idAdmin);
        organisasi.setFotoOrganisasi(file);

        return organisasiRepository.save(organisasi);
    }

    private String uploadFoto(MultipartFile multipartFile, String fileName) throws IOException {
        String timestamp = String.valueOf(System.currentTimeMillis());
        String folderPath = "organisasi/";
        String fullPath = folderPath + timestamp + "_" + fileName;
        BlobId blobId = BlobId.of("absensireact.appspot.com", fullPath);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("media").build();
        Credentials credentials = GoogleCredentials.fromStream(new FileInputStream("./src/main/resources/FirebaseConfig.json"));
        Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
        storage.create(blobInfo, multipartFile.getBytes());
        return String.format(DOWNLOAD_URL, URLEncoder.encode(fullPath, StandardCharsets.UTF_8));
    }



//    private String uploadFoto(MultipartFile multipartFile, String fileName) throws IOException {
//        String timestamp = String.valueOf(System.currentTimeMillis());
//        String folderPath = "organisasi/";
//        String fullPath = folderPath + timestamp + "_" + fileName;
//        BlobId blobId = BlobId.of("absensireact.appspot.com", fullPath);
//        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("media").build();
//        storage.create(blobInfo, multipartFile.getBytes());
//        return String.format(DOWNLOAD_URL, URLEncoder.encode(fullPath, StandardCharsets.UTF_8));
//    }

    private void deleteFoto(String fileName) throws IOException {
        BlobId blobId = BlobId.of("absensireact.appspot.com", fileName);
        Credentials credentials = GoogleCredentials.fromStream(new FileInputStream("./src/main/resources/FirebaseConfig.json"));
        Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
        storage.delete(blobId);
    }

    @Override
    public Organisasi UbahDataOrgannisasi(Long idAdmin, Organisasi organisasi, MultipartFile image) throws IOException {
        Admin admmin = adminRepository.findById(idAdmin).orElse(null);
        if (admmin == null) {
            throw new NotFoundException("Id admin tidak ditemukan");
        }
        organisasi.setNamaOrganisasi(organisasi.getNamaOrganisasi());
        organisasi.setAlamat(organisasi.getAlamat());
        organisasi.setKecamatan(organisasi.getKecamatan());
        organisasi.setKabupaten(organisasi.getKabupaten());
        organisasi.setProvinsi(organisasi.getProvinsi());
        organisasi.setNomerTelepon(organisasi.getNomerTelepon());
        organisasi.setEmailOrganisasi(organisasi.getEmailOrganisasi());
        organisasi.setAdmin(admmin);
        String imageUrl = uploadFoto(image, "_organisasi_" + idAdmin);
        organisasi.setFotoOrganisasi(imageUrl);

        return organisasiRepository.save(organisasi);
    }

    @Override
    public Organisasi EditByid(Long id, Organisasi organisasi, MultipartFile image) throws IOException{
        Organisasi organisasi1 = organisasiRepository.findById(id).orElse(null);
        if (organisasi1 == null) {
            throw new NotFoundException("Organisasi dengan id " + id + " tidak ditemukan");
        }
        Admin admin = organisasi.getAdmin();
        organisasi.setNamaOrganisasi(organisasi.getNamaOrganisasi());
        organisasi.setAlamat(organisasi.getAlamat());
        organisasi.setKecamatan(organisasi.getKecamatan());
        organisasi.setKabupaten(organisasi.getKabupaten());
        organisasi.setProvinsi(organisasi.getProvinsi());
        organisasi.setNomerTelepon(organisasi.getNomerTelepon());
        organisasi.setEmailOrganisasi(organisasi.getEmailOrganisasi());
        organisasi.setAdmin(admin);
        String imageUrl = uploadFoto(image, "organisasi" + id);
        organisasi.setFotoOrganisasi(imageUrl);

        return organisasiRepository.save(organisasi);
    }
    @Override
    public void deleteOrganisasi(Long id) throws IOException {
        Optional<Organisasi> organisasiOptional = organisasiRepository.findById(id);
        if (organisasiOptional.isPresent()) {
            Organisasi organisasi = organisasiOptional.get();
            String fotoUrl = organisasi.getFotoOrganisasi();
            String fileName = fotoUrl.substring(fotoUrl.indexOf("/o/") + 3, fotoUrl.indexOf("?alt=media"));
             deleteFoto(fileName);
             organisasiRepository.deleteById(id);
        } else {
            throw new NotFoundException("Organisasi not found with id: " + id);
        }
    }

}
