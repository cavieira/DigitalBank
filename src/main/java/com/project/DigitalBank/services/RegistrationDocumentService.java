package com.project.DigitalBank.services;

import com.project.DigitalBank.exceptions.RegistrationDocumentSaveFailed;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.validation.ValidationException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;

@Service
public class RegistrationDocumentService {

    @Value(value = "${image.path:./img}")
    private String imagePath;

    @Value(value = "${image.pathStatic:./img}")
    private String imagePathStatic;

    public String saveDocument(String id, String image) {
        try {
            //Decode the String which is encoded by using Base64 class
            byte[] imageByte = Base64.getDecoder().decode(image);

            //Create the file name where the image will be save
            String document = String.format("%s%s%s", imagePath, id, ".jpg");
            String documentResources = String.format("%s%s%s", imagePathStatic, id, ".jpg");

            //Save the image
            new FileOutputStream(document).write(imageByte);

            return documentResources;
        } catch (IllegalArgumentException e) {
            throw new ValidationException("Imagem não pode ser decodificada usando Base64 decode.");
        } catch (SecurityException | IOException e) {
            throw new RegistrationDocumentSaveFailed("Erro ao salvar foto do CPF.");
        }
    }
}
