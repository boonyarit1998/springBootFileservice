package com.restapi.fileservice.Service;

import com.restapi.fileservice.Entity.FileEntity;
import com.restapi.fileservice.Repository.FileRepository;
import com.restapi.fileservice.exception.FileStorageException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileService {
    private final FileRepository fileRepository;
    private final Path root = Paths.get("uploads");

    public FileEntity upload(MultipartFile file) throws IOException {
        validateFile(file);
        String storedName = UUID.randomUUID()+"_"+ file.getOriginalFilename();
        Path path = root.resolve(storedName);

        Files.copy(file.getInputStream(),path, StandardCopyOption.REPLACE_EXISTING);

        FileEntity metadata = new FileEntity();
        metadata.setOriginalName(file.getOriginalFilename());
        metadata.setStoredName(storedName);
        metadata.setContentType(file.getContentType());
        metadata.setSize(file.getSize());
        metadata.setFilePath(path.toString());

        return fileRepository.save(metadata);
    }

    private void validateFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new FileStorageException("File is empty");
        }
        if (file.getSize() > 5_000_000) { // จำกัด 5MB
            throw new FileStorageException("File size exceeds 5MB limit");
        }
        String contentType = file.getContentType();
        if (contentType == null ||
                !(contentType.equals("image/png") || contentType.equals("image/jpeg") || contentType.equals("application/pdf"))) {
            throw new FileStorageException("Only PNG, JPEG, and PDF files are allowed");
        }
    }

    public List<FileEntity> getAllFile(){
        return fileRepository.findAll();
    }

    public FileEntity getFileByid(long id) throws  IOException{
        return fileRepository.findById(id).orElseThrow(() -> new FileStorageException("File not found"));
    }

    public void deteleFile(Long id) throws IOException{
        FileEntity file = fileRepository.findById(id).orElseThrow(() -> new FileStorageException("File not found"));
        Files.deleteIfExists(Paths.get(file.getFilePath()));
        fileRepository.deleteById(id);
    }
}
