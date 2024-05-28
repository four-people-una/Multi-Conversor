// Classe BackEnd.java
package com.coversor;

import java.io.IOException;
import java.util.ArrayList;

public class BackEnd {
    private FrontEnd frontEnd;

    ArrayList<String> filePathList = new ArrayList<>();
    String checkBoxSelection = "";

    public BackEnd() {

    }

    public void handleFileConversion() throws IOException {
        filePathList = frontEnd.getSelectedFilePath();
        checkBoxSelection = frontEnd.getCheckBoxSelection();

        if (!checkBoxSelection.isEmpty()) {
            if (!checkBoxSelection.equals("PDF")) {
                for (String filePath : filePathList) {
                    if (checkBoxSelection.equals("PNG")) {
                        try {
                            FileConverterUtils convert = new FileConverterUtils(filePath);
                            String mensagem = convert.getFilePNG();
                            frontEnd.setMessage(mensagem);
                        } catch (IOException j) {
                            j.printStackTrace();
                        }
                    } else if (checkBoxSelection.equals("JPEG")) {
                        try {
                            FileConverterUtils convert = new FileConverterUtils(filePath);

                            String mensagem = convert.getFileJPEG();
                            frontEnd.setMessage(mensagem);

                        } catch (IOException j) {
                            j.printStackTrace();
                        }
                    }
                }
            } else {
                FileConverterUtils convert = new FileConverterUtils(filePathList);
                String mensagem = convert.getFilePDF();
                frontEnd.setMessage(mensagem);
            }
            
        } else {
            String messagem = "Nenhuma checkBox selecionada!";
            frontEnd.setMessage(messagem);
        }
    }

    // SETTERS
    public void setFrontEnd(FrontEnd frontEnd) {
        this.frontEnd = frontEnd;
    }
}