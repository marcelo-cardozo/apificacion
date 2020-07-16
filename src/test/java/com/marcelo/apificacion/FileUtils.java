package com.marcelo.apificacion;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.util.FileCopyUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;


public class FileUtils {
    public static String readTestResourceFile(String fileName) {
        try {
            File resource = null;
            resource = new ClassPathResource(fileName).getFile();
            return new String(Files.readAllBytes(resource.toPath()));
        } catch (IOException e) {
        }
        return null;
    }
}