package com.coversor;

import java.io.IOException;

public class FileTypeChecker {
    public static String getFileType(String filePath) throws IOException {
        // Obtém a extensão do arquivo
        String extension = getFileExtension(filePath);

        return extension;
    }

    // Método para obter a extensão do arquivo
    private static String getFileExtension(String filePath) {
        String extension = "";
        int lastDotIndex = filePath.lastIndexOf('.');
        if (lastDotIndex > 0 && lastDotIndex < filePath.length() - 1) {
            extension = filePath.substring(lastDotIndex + 1);
        }
        return extension.toLowerCase();
    }
}
