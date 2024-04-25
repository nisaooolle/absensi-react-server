package com.example.absensireact.impl;

import com.example.absensireact.exception.InternalErrorException;
import com.example.absensireact.exception.NotFoundException;
import com.example.absensireact.model.Admin;
import com.example.absensireact.model.Organisasi;
import com.example.absensireact.model.User;
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
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private OrganisasiRepository organisasiRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Override
    public Organisasi GetAllOrganisasi(){
        return (Organisasi) organisasiRepository.findAll();
    }

    @Override
    public Optional<Organisasi>GetOrganisasiById(Long id){
        return organisasiRepository.findById(id);
    }
    @Override
    public List<Organisasi> GetAllBYId(Long id){
        return organisasiRepository.GetallById(id);
    }

    @Override
    public Organisasi GetAllByIdAdmin(Long idAdmin){
        return (Organisasi) organisasiRepository.GetAllByIdAdmin(idAdmin);
    }
    @Override
    public Organisasi TambahOrganisasi(Long idAdmin, Organisasi organisasi, MultipartFile image) throws IOException {
        Optional<Admin> admin = Optional.ofNullable(adminRepository.findById(idAdmin).orElse(null));

        if (admin == null) {
            throw new NotFoundException("Id Admin tidak ditemukan");
        }
        Admin admin1 = admin.get();
        organisasi.setNamaOrganisasi(organisasi.getNamaOrganisasi());
        organisasi.setAlamat(organisasi.getAlamat());
        organisasi.setKecamatan(organisasi.getKecamatan());
        organisasi.setKabupaten(organisasi.getKabupaten());
        organisasi.setProvinsi(organisasi.getProvinsi());
        organisasi.setNomerTelepon(organisasi.getNomerTelepon());
        organisasi.setEmailOrganisasi(organisasi.getEmailOrganisasi());
        organisasi.setAdmin(admin1);
        String imageUrl = imageConverter(image);
        organisasi.setFotoOrganisasi(imageUrl);

        return organisasiRepository.save(organisasi);
    }


    private String imageConverter(MultipartFile multipartFile) {
        try {
            return uploadFoto(multipartFile);
        } catch (Exception e) {
            e.printStackTrace();
            throw new InternalErrorException("Error upload file");
        }
    }

    public String uploadFoto(MultipartFile image) throws IOException {
        String timestamp = String.valueOf(System.currentTimeMillis());
        String folderPath = "organisasi/";
        String fileName = folderPath + timestamp + "_" + image.getOriginalFilename();
        BlobId blobId = BlobId.of("absensireact.appspot.com", fileName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                .setContentType(image.getContentType()) // Set content type from image
                .build();
        Credentials credentials = GoogleCredentials.fromStream(new FileInputStream("./src/main/java/com.example.absensireact/firebase/FirebaseConfig.json"));
        Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
        storage.create(blobInfo, image.getBytes()); // Use image.getBytes() to get file bytes directly
        return String.format(DOWNLOAD_URL, URLEncoder.encode(fileName, StandardCharsets.UTF_8));
    }

    private File convertToFile(MultipartFile multipartFile, String fileName) throws IOException {
        File file = new File(fileName);
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(multipartFile.getBytes());
            fos.close();
        }
        return file;
    }
    private String getExtentions(String fileName) {
        return fileName.split("\\.")[0];
    }

    @Override
    public Organisasi UbahDataOrgannisasi(Long idAdmin, Organisasi organisasi, MultipartFile image){
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
        String imageUrl = imageConverter(image);
        organisasi.setFotoOrganisasi(imageUrl);

        return organisasiRepository.save(organisasi);
    }

    @Override
    public Organisasi EditByid(Long id, Organisasi organisasi, MultipartFile image) throws IOException{
        Organisasi organisasi1 = organisasiRepository.findById(id).orElse(null);
        if (organisasi1 == null) {
            throw new NotFoundException("Organisasi dengan id " + id + " tidak ditemukan");
        }
        Admin admin = organisasi.getAdmin().getId();
        organisasi.setNamaOrganisasi(organisasi.getNamaOrganisasi());
        organisasi.setAlamat(organisasi.getAlamat());
        organisasi.setKecamatan(organisasi.getKecamatan());
        organisasi.setKabupaten(organisasi.getKabupaten());
        organisasi.setProvinsi(organisasi.getProvinsi());
        organisasi.setNomerTelepon(organisasi.getNomerTelepon());
        organisasi.setEmailOrganisasi(organisasi.getEmailOrganisasi());
        organisasi.setAdmin(admin);
        String imageUrl = imageConverter(image);
        organisasi.setFotoOrganisasi(imageUrl);

        return organisasiRepository.save(organisasi);
    }
    @Override
    public void deleteKaryawan(Long id) {
        if (organisasiRepository.existsById(id)) {
            organisasiRepository.deleteById(id);
        } else {
            throw new NotFoundException("Organisasi not found with id: " + id);
        }
    }


}
