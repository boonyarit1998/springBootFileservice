package com.restapi.fileservice.controller;

import com.restapi.fileservice.dto.FileReponseDTO;
import com.restapi.fileservice.entity.FileEntity;
import com.restapi.fileservice.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/file")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @PostMapping("/upload")
    public ResponseEntity<FileEntity> uploadFile(@RequestParam("file")MultipartFile file) throws IOException {
        return ResponseEntity.ok().body(fileService.upload(file));
    }

    @GetMapping()
    public ResponseEntity<List<FileReponseDTO>> listFile(){
        return ResponseEntity.ok().body(fileService.getAllFile());
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<byte[]> dowloadFile(@PathVariable Long id) throws IOException{

        FileEntity file = fileService.getFileByid(id);
        Path path = Paths.get(file.getFilePath());
        byte[] data = Files.readAllBytes(path);

        String contentType = Files.probeContentType(path);
        return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION,"inline;filename=\""+file.getOriginalName()+ "\"")
                .body(data);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFile(@PathVariable Long id) throws IOException{
        fileService.deteleFile(id);
        return ResponseEntity.noContent().build();
    }
}
