package com.restapi.fileservice.dto;

import com.restapi.fileservice.entity.FileEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileReponseDTO {
    private String fileName;
    private String path;
    private long size;

    public static FileReponseDTO toDTO(FileEntity entity){
        FileReponseDTO file = new FileReponseDTO();
        file.setFileName(entity.getOriginalName());
        file.setPath(entity.getFilePath());
        file.setSize(entity.getSize());
        return  file;
    }


}
