package com.project.DigitalBank.services;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.FileOutputStream;

@Service
public class RegistrationDocumentService {

    @Value(value = "${image.path:./img}")
    private String imagePath;

    public String saveDocument(String id, String image) {
        //Decode the String which is encoded by using Base64 class
        byte[] imageByte = Base64.decodeBase64(image);

        //Create the file name where the image will be save
        String document = String.format("%s%s%s", imagePath, id, ".jpg");

        //Save the image
        try {
            new FileOutputStream(document).write(imageByte);
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Error while processing image ", e);
        }

        return document;
    }
}
