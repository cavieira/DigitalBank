package com.project.DigitalBank.utils;

import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

public class URIUtils {

    public static  <T> ResponseEntity<T> createURI(String path, String id, T body) {
        URI uri = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path(path)
                .buildAndExpand(id)
                .toUri();

        return ResponseEntity.created(uri).body(body);
    }
}
