package com.company;

import java.io.*;
import java.util.AbstractMap;
import java.util.Map;


public class IOUtils {

    public static Map.Entry<FileInputStream, Long> getFileInputStream(String inputPath) throws FileNotFoundException, com.encryptor.ReadException {
        File sourceFile = new File(inputPath);
        if (!sourceFile.exists()) {
            throw new FileNotFoundException("input file doesn't exists");
        }
        if(!sourceFile.canRead()) {
            throw new com.encryptor.ReadException("can't read input file");
        }
        return new AbstractMap.SimpleEntry<>(new FileInputStream(sourceFile), sourceFile.length());
    }

    public static FileOutputStream getFileOutputStream(String outputPath) throws FileNotFoundException, com.encryptor.WriteException {
        File outputFile = new File(outputPath);
        try {
            outputFile.createNewFile();
        } catch (IOException e) {
            throw new com.encryptor.WriteException("can't write to this directory");
        }

        return new FileOutputStream(outputFile);
    }


}
