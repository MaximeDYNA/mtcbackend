package com.catis.controller;

import org.apache.commons.io.FilenameUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.CompletableFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping(value = "/images/")
public class ProductImageController {
    private final ResourceLoader resourceLoader;
    Logger logger = LoggerFactory.getLogger(ProductImageController.class);

    public ProductImageController(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Async("taskExecutorDefault")
    @GetMapping("/{imageName}")
    public CompletableFuture<ResponseEntity<byte[]>> getImage(@PathVariable String imageName) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Resource resource = resourceLoader.getResource("classpath:static/images/" + imageName);
                if (resource.exists() && resource.isReadable()) {
                    try (InputStream inputStream = resource.getInputStream()) {
                        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                        byte[] buffer = new byte[1024];
                        int length;
                        while ((length = inputStream.read(buffer)) != -1) {
                            outputStream.write(buffer, 0, length);
                        }
                        byte[] imageBytes = outputStream.toByteArray();

                        String contentType = determineContentType(imageName);
                        return ResponseEntity.ok()
                                .contentType(MediaType.parseMediaType(contentType))
                                .body(imageBytes);
                    }
                } else {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Image not found");
                }
            } catch (IOException e) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error retrieving image", e);
            }
        });
    }

    private String determineContentType(String imageName) {
        // Get the file extension
        String extension = FilenameUtils.getExtension(imageName.toLowerCase());

        // Determine the content type based on the file extension
        switch (extension) {
            case "jpg":
            case "jpeg":
                return "image/jpeg";
            case "png":
                return "image/png";
            case "gif":
                return "image/gif";
            case "bmp":
                return "image/bmp";
            // Add more cases for other image formats as needed
            default:
                // If the file extension is not recognized, assume it's a binary file
                return "application/octet-stream";
        }
    }
}
// import org.apache.commons.io.FilenameUtils;
// import org.springframework.core.io.Resource;
// import org.springframework.core.io.ResourceLoader;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.MediaType;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.PathVariable;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RestController;
// import org.springframework.web.server.ResponseStatusException;

// import java.io.ByteArrayOutputStream;
// import java.io.IOException;
// import java.io.InputStream;

// @RestController
// @RequestMapping(value = "/images/")
// public class ProductImageController {
//     private final ResourceLoader resourceLoader;

//     public ProductImageController(ResourceLoader resourceLoader) {
//         this.resourceLoader = resourceLoader;
//     }
    
//     @GetMapping("/{imageName}")
//     public ResponseEntity<byte[]> getImage(@PathVariable String imageName) {
//         try {
//             Resource resource = resourceLoader.getResource("classpath:static/images/" + imageName);
//             if (resource.exists() && resource.isReadable()) {
//                 try (InputStream inputStream = resource.getInputStream()) {
//                     ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//                     byte[] buffer = new byte[1024];
//                     int length;
//                     while ((length = inputStream.read(buffer)) != -1) {
//                         outputStream.write(buffer, 0, length);
//                     }
//                     byte[] imageBytes = outputStream.toByteArray();

//                     String contentType = determineContentType(imageName);
//                     return ResponseEntity.ok()
//                             .contentType(MediaType.parseMediaType(contentType))
//                             .body(imageBytes);
//                 }
//             } else {
//                 throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Image not found");
//             }
//         } catch (IOException e) {
//             throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error retrieving image", e);
//         }
//     }

//     private String determineContentType(String imageName) {
//         // Get the file extension
//         String extension = FilenameUtils.getExtension(imageName.toLowerCase());

//         // Determine the content type based on the file extension
//         switch (extension) {
//             case "jpg":
//             case "jpeg":
//                 return "image/jpeg";
//             case "png":
//                 return "image/png";
//             case "gif":
//                 return "image/gif";
//             case "bmp":
//                 return "image/bmp";
//             // Add more cases for other image formats as needed
//             default:
//                 // If the file extension is not recognized, assume it's a binary file
//                 return "application/octet-stream";
//         }
//     }
// }


// import java.io.IOException;

// import org.apache.commons.io.FilenameUtils;
// import org.springframework.core.io.Resource;
// import org.springframework.core.io.ResourceLoader;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.PathVariable;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RestController;
// import org.springframework.web.server.ResponseStatusException;
// import org.springframework.http.MediaType;
// import java.io.InputStream;

// @RestController
// @RequestMapping(value = "/images/")
// public class ProductImageController {
//      private final ResourceLoader resourceLoader;

//     public ProductImageController(ResourceLoader resourceLoader) {
//         this.resourceLoader = resourceLoader;
//     }
//    @GetMapping("/{imageName}")
//     public ResponseEntity<byte[]> getImage(@PathVariable String imageName) {
//         try {
//             Resource resource = resourceLoader.getResource("classpath:static/images/" + imageName);
//             if (resource.exists() && resource.isReadable()) {
//                 try (InputStream inputStream = resource.getInputStream()) {
//                     byte[] imageBytes = inputStream.readAllBytes();
//                     String contentType = determineContentType(imageName);
//                     return ResponseEntity.ok()
//                             .contentType(MediaType.parseMediaType(contentType))
//                             .body(imageBytes);
//                 }
//             } else {
//                 throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Image not found");
//             }
//         } catch (IOException e) {
//             throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error retrieving image", e);
//         }
//     }

//     private String determineContentType(String imageName) {
//     // Get the file extension
//     String extension = FilenameUtils.getExtension(imageName.toLowerCase());

//     // Determine the content type based on the file extension
//     switch (extension) {
//         case "jpg":
//         case "jpeg":
//             return "image/jpeg";
//         case "png":
//             return "image/png";
//         case "gif":
//             return "image/gif";
//         case "bmp":
//             return "image/bmp";
//         // Add more cases for other image formats as needed
//         default:
//             // If the file extension is not recognized, assume it's a binary file
//             return "application/octet-stream";
//     }
// }


// }
