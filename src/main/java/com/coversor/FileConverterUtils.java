package com.coversor;

import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;

import java.io.File;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class FileConverterUtils {
    private String someFile;
    private ArrayList<String> listFilePath = new ArrayList<>();

    /* CONSTRUCTOR */
    public FileConverterUtils(Object obj) throws IOException {

        if (obj instanceof String) {
            setInputFile(obj);
        } else {
            setInputFileList(obj);
        }

    }

    /* GETTERS */
    public String getFilePNG() throws IOException {
        String extensionFile = checkFileType(someFile);
        saveAsPNG(extensionFile);

        return "Arquivo convertido em PNG com sucesso!";
    }

    public String getFileJPEG() throws IOException {
        String extensionFile = checkFileType(someFile);
        System.out.println(extensionFile);
        saveAsJPEG(extensionFile);

        return "Arquivo convertido em JPEG com sucesso!";
    }

    public String getFilePDF() throws IOException {
        saveAsPDF();

        return "Arquivo convertido em PDF com sucesso!";
    }

    /* SETTERS */
    public void setInputFile(Object filePath) throws IOException {
        this.someFile = (String) filePath;
    }

    @SuppressWarnings("unchecked")
    public void setInputFileList(Object fileList) throws IOException {
        if (fileList instanceof ArrayList<?>) {
            ArrayList<?> tempList = (ArrayList<?>) fileList;
            boolean allStrings = true;
            for (Object item : tempList) {
                if (!(item instanceof String)) {
                    allStrings = false;
                    break;
                }
            }
            if (allStrings) {
                this.listFilePath = (ArrayList<String>) tempList;
            } else {
                throw new IllegalArgumentException("O ArrayList deve conter apenas Strings.");
            }
        } else {
            throw new IllegalArgumentException("O argumento deve ser um ArrayList.");
        }
    }

    /* METHODS */
    public void saveAsPNG(String extension) throws IOException {
        File convertSomeFile = new File(someFile);

        // create dir to save
        String dirPath = "output/" + convertSomeFile.getName();
        File directory = new File(dirPath);
        directory.mkdir();

        if (extension.equals("pdf")) {
            PDDocument document = PDDocument.load(convertSomeFile);
            PDFRenderer renderer = new PDFRenderer(document);

            // save
            for (int page = 0; page < document.getNumberOfPages(); ++page) {
                BufferedImage image = renderer.renderImageWithDPI(page, 300, ImageType.RGB);
                if (image == null) {
                    System.out.println("A leitura da imagem falhou. Verifique o arquivo de entrada.");
                    return;
                }
                File output = new File(dirPath + "/" + (page + 1) + "." + "png".toLowerCase());
                if (ImageIO.write(image, "PNG", output)) {
                    System.out.println("Imagem salva com sucesso em: " + output.getAbsolutePath());
                } else {
                    System.out.println("Falha ao salvar a imagem. Formato não suportado.");
                }
            }

            document.close();
        } else {
            BufferedImage image = ImageIO.read(convertSomeFile);
            if (image == null) {
                System.out.println("A leitura da imagem falhou. Verifique o arquivo de entrada.");
                return;
            }

            File output = new File(dirPath + "/1.png");
            if (ImageIO.write(image, "PNG", output)) {
                System.out.println("Imagem salva com sucesso em: " + output.getAbsolutePath());
            } else {
                System.out.println("Falha ao salvar a imagem. Formato não suportado.");
            }
        }
    }

    public void saveAsJPEG(String extension) throws IOException {
        File convertSomeFile = new File(someFile);

        // create dir to save
        String dirPath = "output/" + convertSomeFile.getName();
        File directory = new File(dirPath);
        directory.mkdir();

        if (extension.equals("pdf")) {
            PDDocument document = PDDocument.load(convertSomeFile);
            PDFRenderer renderer = new PDFRenderer(document);

            // save
            for (int page = 0; page < document.getNumberOfPages(); ++page) {
                BufferedImage image = renderer.renderImageWithDPI(page, 300, ImageType.RGB);
                File output = new File(dirPath + "/" + (page + 1) + "." + "jpeg".toLowerCase());
                if (ImageIO.write(image, "JPEG", output)) {
                    System.out.println("Imagem salva com sucesso em: " + output.getAbsolutePath());
                } else {
                    System.out.println("Falha ao salvar a imagem. Formato não suportado.");
                }
            }

            document.close();
        } else {
            BufferedImage image = ImageIO.read(convertSomeFile);
            if (image == null) {
                System.out.println("A leitura da imagem falhou. Verifique o arquivo de entrada.");
                return;
            }

            File output = new File(dirPath + "/1.jpeg");
            if (ImageIO.write(image, "JPEG", output)) {
                System.out.println("Imagem salva com sucesso em: " + output.getAbsolutePath());
            } else {
                System.out.println("Falha ao salvar a imagem. Formato não suportado.");
            }
        }
    }

    public void saveAsPDF() throws IOException {
        File convertSomeFile = new File(listFilePath.get(0));

        // create dir to save
        String dirPath = "output/(PDFjoin) " + convertSomeFile.getName();
        File directory = new File(dirPath);
        directory.mkdir();

        try (PDDocument document = new PDDocument()) {
            for (String imagePath : listFilePath) {
                BufferedImage image = ImageIO.read(new File(imagePath));
                if (image != null) {
                    PDPage page = new PDPage(new PDRectangle(image.getWidth(), image.getHeight()));
                    document.addPage(page);

                    PDImageXObject pdImage = LosslessFactory.createFromImage(document, image);
                    try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                        contentStream.drawImage(pdImage, 0, 0);
                    }
                }
            }

            document.save(dirPath + "/join.pdf");
        }
    }

    public static String checkFileType(String filePath) throws IOException {
        return FileTypeChecker.getFileType(filePath);
    }
}
