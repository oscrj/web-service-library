package com.example.webservicelibrary.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/library-api/v1/files")
public class FileController {

    private static final String currentWorkingDirectory = System.getProperty("user.dir");
    private static final String uploadDirectory = currentWorkingDirectory + "/src/main/resources/static/uploads/images";
    final List<String> supportedFileExtensions = List.of(".png,.jpeg,.jpg,.gif".split(","));

    @PostConstruct
    public void createFolder() {
        File dirPath = new File(uploadDirectory);
        if (!dirPath.exists()) {
            dirPath.mkdir();
        }
    }

    @Secured({"ROLE_ADMIN","ROLE_LIBRARIAN"})
    @PostMapping
    public ResponseEntity<String> uploadImage(@RequestParam MultipartFile file) {
        var filename = file.getOriginalFilename();
        var fileExtension = filename.substring(filename.lastIndexOf("."));
        if (!supportedFileExtensions.contains(fileExtension.toLowerCase())) {
            log.error(String.format("Unsupported file extension"));
            return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).build();
        }
        var targetLocation = new File(uploadDirectory + File.separator + filename);
        try {
            file.transferTo(targetLocation);
        }catch (IOException ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getLocalizedMessage());
        }
        return ResponseEntity.created(URI.create("/files/" + filename)).build();
    }

}
