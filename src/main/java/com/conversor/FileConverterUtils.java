package com.conversor;

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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class FileConverterUtils {
    private String someFile;
    private ArrayList<String> listFilePath = new ArrayList<>();

    /* CONSTRUCTOR */
    public FileConverterUtils(Object obj) throws IOException {
        // Verifica se o objeto de entrada é uma String ou uma lista de Strings
        if (obj instanceof String) {
            setInputFile(obj); // Configura o arquivo de entrada
        } else {
            setInputFileList(obj); // Configura a lista de arquivos de entrada
        }
    }

    /* GETTERS */
    public String getFilePNG() throws IOException {
        // Obtém a extensão do arquivo de entrada
        String extensionFile = getFileExtension(someFile);
        // Converte o arquivo para PNG
        saveAsPNG(extensionFile);

        return "Arquivo convertido em PNG com sucesso!";
    }

    public String getFileJPEG() throws IOException {
        // Obtém a extensão do arquivo de entrada
        String extensionFile = getFileExtension(someFile);
        // Converte o arquivo para JPEG
        saveAsJPEG(extensionFile);

        return "Arquivo convertido em JPEG com sucesso!";
    }

    public String getFilePDF() throws IOException {
        // Converte o arquivo para PDF
        saveAsPDF();

        return "Arquivo convertido em PDF com sucesso!";
    }

    /* SETTERS */
    public void setInputFile(Object filePath) throws IOException {
        this.someFile = (String) filePath;
    }

    @SuppressWarnings("unchecked")
    public void setInputFileList(Object fileList) throws IOException {
        // Verifica se o objeto fileList é uma instância de ArrayList
        if (fileList instanceof ArrayList<?>) {
            ArrayList<?> tempList = (ArrayList<?>) fileList;
            boolean allStrings = true;
            // Verifica se todos os elementos da lista são do tipo String
            for (Object item : tempList) {
                if (!(item instanceof String)) {
                    allStrings = false;
                    break;
                }
            }
            // Se todos os elementos são Strings, atribui a lista de caminhos de arquivo
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
        // Cria um objeto File para o arquivo a ser convertido
        File convertSomeFile = new File(someFile);

        // Cria o diretório para salvar as imagens convertidas
        String dirPath = createDirs(convertSomeFile);

        if (extension.equals("pdf")) { // Se a extensão for PDF
            // Carrega o documento PDF
            PDDocument document = PDDocument.load(convertSomeFile);
            PDFRenderer renderer = new PDFRenderer(document);

            // Salva cada página do PDF como uma imagem PNG
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

            // Fecha o documento PDF
            document.close();
        } else { // Se a extensão não for PDF
            BufferedImage image = ImageIO.read(convertSomeFile);
            if (image == null) {
                System.out.println("A leitura da imagem falhou. Verifique o arquivo de entrada.");
                return;
            }

            // Salva a imagem como PNG
            File output = new File(dirPath + "/1.png");
            if (ImageIO.write(image, "PNG", output)) {
                System.out.println("Imagem salva com sucesso em: " + output.getAbsolutePath());
            } else {
                System.out.println("Falha ao salvar a imagem. Formato não suportado.");
            }
        }
    }

    public void saveAsJPEG(String extension) throws IOException {
        // Cria um objeto File para o arquivo a ser convertido
        File convertSomeFile = new File(someFile);

        // Cria o diretório para salvar as imagens convertidas
        String dirPath = createDirs(convertSomeFile);

        if (extension.equals("pdf")) { // Se a extensão for PDF
            // Carrega o documento PDF
            PDDocument document = PDDocument.load(convertSomeFile);
            PDFRenderer renderer = new PDFRenderer(document);

            // Salva cada página do PDF como uma imagem JPEG
            for (int page = 0; page < document.getNumberOfPages(); ++page) {
                BufferedImage image = renderer.renderImageWithDPI(page, 300, ImageType.RGB);
                File output = new File(dirPath + "/" + (page + 1) + "." + "jpeg".toLowerCase());
                if (ImageIO.write(image, "JPEG", output)) {
                    System.out.println("Imagem salva com sucesso em: " + output.getAbsolutePath());
                } else {
                    System.out.println("Falha ao salvar a imagem. Formato não suportado.");
                }
            }

            // Fecha o documento PDF
            document.close();
        } else { // Se a extensão não for PDF
            BufferedImage image = ImageIO.read(convertSomeFile);
            if (image == null) {
                System.out.println("A leitura da imagem falhou. Verifique o arquivo de entrada.");
                return;
            }

            // Salva a imagem como JPEG
            File output = new File(dirPath + "/1.jpeg");
            if (ImageIO.write(image, "JPEG", output)) {
                System.out.println("Imagem salva com sucesso em: " + output.getAbsolutePath());
            } else {
                System.out.println("Falha ao salvar a imagem. Formato não suportado.");
            }
        }
    }

    public void saveAsPDF() throws IOException {
        // Cria um objeto File para o primeiro arquivo da lista de caminhos de arquivo
        File convertSomeFile = new File(listFilePath.get(0));

        // Cria o diretório para salvar o PDF
        String dirPath = createDirs(convertSomeFile);

        try (PDDocument document = new PDDocument()) {
            // Para cada imagem na lista de caminhos de arquivo
            for (String imagePath : listFilePath) {
                // Lê a imagem como BufferedImage
                BufferedImage image = ImageIO.read(new File(imagePath));
                if (image != null) {
                    // Cria uma página PDF com as dimensões da imagem
                    PDPage page = new PDPage(new PDRectangle(image.getWidth(), image.getHeight()));
                    document.addPage(page);

                    // Converte a BufferedImage em PDImageXObject
                    PDImageXObject pdImage = LosslessFactory.createFromImage(document, image);
                    try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                        // Desenha a imagem na página PDF
                        contentStream.drawImage(pdImage, 0, 0);
                    }
                }
            }

            // Salva o documento PDF
            document.save(dirPath + "/join.pdf");
        }
    }

    public String createDirs(File file) throws IOException {
        // Obtém o diretório atual
        Path currentDirectory = Paths.get("").toAbsolutePath();

        // Cria o caminho para o novo diretório "output"
        Path outputDirectory = currentDirectory.resolve("output");

        // Cria o diretório "output" se ele não existir
        if (Files.notExists(outputDirectory)) {
            Files.createDirectory(outputDirectory);
        }

        // Cria o caminho para o subdiretório dentro de "output" com o nome do arquivo
        String dirPath = outputDirectory.resolve(file.getName()).toString();
        File directory = new File(dirPath);

        // Cria o subdiretório
        directory.mkdir();

        // Retorna o caminho do subdiretório criado
        return dirPath;
    }

    private static String getFileExtension(String file) {
        String extension = "";
        int lastDotIndex = file.lastIndexOf('.');
        if (lastDotIndex > 0 && lastDotIndex < file.length() - 1) {
            extension = file.substring(lastDotIndex + 1);
        }
        return extension.toLowerCase();
    }
}
