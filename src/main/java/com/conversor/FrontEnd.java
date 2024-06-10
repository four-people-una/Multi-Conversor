// Classe FrontEnd.java
package com.conversor;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.ArrayList;

/**
 * Classe FrontEnd contendo a interface grafica do programa
 * @author Grupo four-people-una
 * @version 1.0
 */
public class FrontEnd {
    /**
     * Atributos para definição e preparação da interface grafica do programa
     */
    private JButton openButton = new JButton("Abrir arquivo");
    private JCheckBox checkBoxJPEG = new JCheckBox("JPEG");
    private JCheckBox checkBoxPDF = new JCheckBox("PDF");
    private JCheckBox checkBoxPNG = new JCheckBox("PNG");
    private JFrame frame;
    private JButton finishButton = new JButton("Iniciar a conversão");
    private JLabel formatAceppts = new JLabel("Formatos aceitos: PNG, JPEG, PDF");
    private JLabel labelConverte = new JLabel("Converter para qual tipo: ");
    private JLabel selectedFileLabel = new JLabel("Arquivo selecionado: ");
    private JLabel formatFile = new JLabel("Formato do arquivo: ");
    private JPanel panelButtons = new JPanel();
    private JPanel panelMain = new JPanel();
    private JPanel panelInfoFile = new JPanel();
    private JPanel panelChecksBoxs = new JPanel();
    private JPanel panelContentChecksBoxs = new JPanel();
    ArrayList<String> filePathList = new ArrayList<>();
    private String[] checkBoxSelect;

    /**
     * Atributo instancia de BackEnd
     */
    private BackEnd backEnd;

    /**
     * Método construtor da classe FrontEnd
     */
    public FrontEnd() {
        checkBoxSelect = new String[] { "" };  // Inicializa a seleção da checkbox como uma string vazia
    
        frame = new JFrame("MultiConversor");  // Cria uma nova janela com o título "MultiConversor"
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  // Define o comportamento padrão ao fechar a janela
    
        initComponents();  // Inicializa os componentes da interface gráfica
    
        addListeners();  // Adiciona os ouvintes de eventos aos componentes
    
        frame.pack();  // Ajusta o tamanho da janela automaticamente
        frame.setVisible(true);  // Torna a janela visível na tela
    }

    /**
     * Método para inicializar os componentes da interface grafica
     * @author Grupo four-people-una
     * @version 1.0
     */
    private void initComponents() {
        // Painel principal
        panelMain.setLayout(new BoxLayout(panelMain, BoxLayout.Y_AXIS));
        panelMain.setBorder(new EmptyBorder(18, 18, 18, 18));

        // Painel dos botões
        panelButtons.setLayout(new GridLayout(2, 1, 0, 5));
        openButton.setPreferredSize(new Dimension(390, 30));
        formatAceppts.setFont(new Font("Arial", Font.BOLD, 12));

        panelButtons.add(openButton);
        panelButtons.add(formatAceppts);

        panelInfoFile.setLayout(new GridLayout(2, 1, 0, 8));
        panelInfoFile.add(selectedFileLabel);
        panelInfoFile.add(formatFile);

        // Painel dos checkboxes
        panelContentChecksBoxs.setLayout(new GridLayout(3, 1, 0, 5));
        panelContentChecksBoxs.add(labelConverte);

        // Painel dos checkboxes específicos
        panelChecksBoxs.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        panelChecksBoxs.add(checkBoxJPEG);
        panelChecksBoxs.add(checkBoxPNG);
        panelChecksBoxs.add(checkBoxPDF);
        panelContentChecksBoxs.add(panelChecksBoxs);

        // Botão de finalizar
        finishButton.setPreferredSize(new Dimension(390, 30));
        panelContentChecksBoxs.add(finishButton);

        // Adiciona os painéis ao painel principal
        panelMain.add(panelButtons);
        panelMain.add(Box.createVerticalStrut(10));
        panelMain.add(panelInfoFile);
        panelMain.add(Box.createVerticalStrut(10));
        panelMain.add(panelContentChecksBoxs);

        panelInfoFile.setVisible(false);
        panelContentChecksBoxs.setVisible(false);

        // Adiciona o painel principal à janela
        frame.add(panelMain);
    }

    private void addListeners() {
        // Abrir caixa de seletor de arquivos
        openButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser(); // Cria um seletor de arquivos

                fileChooser.setMultiSelectionEnabled(true); // Habilita a seleção de múltiplos arquivos

                // Define um filtro para mostrar apenas arquivos PNG, JPEG e PDF
                FileNameExtensionFilter filter = new FileNameExtensionFilter(
                        "PNG, JPEG, and PDF files", "png", "jpeg", "jpg", "pdf");

                fileChooser.setFileFilter(filter); // Aplica o filtro ao seletor de arquivos

                boolean validSelection = false;
                while (!validSelection) {
                    // Mostra o diálogo de escolha de arquivo
                    int returnValue = fileChooser.showOpenDialog(frame);

                    if (returnValue == JFileChooser.APPROVE_OPTION) {
                        // Obtém os arquivos selecionados
                        File[] selectedFiles = fileChooser.getSelectedFiles();

                        if (selectedFiles.length > 0) {
                            // Verifica se todas as extensões dos arquivos selecionados são iguais
                            String firstExtension = getFileExtension(selectedFiles[0]);
                            boolean allSameExtension = true;

                            for (File file : selectedFiles) {
                                if (!firstExtension.equals(getFileExtension(file))) {
                                    allSameExtension = false;
                                    break;
                                }
                            }

                            if (allSameExtension) {
                                validSelection = true; // Indica que uma seleção válida foi feita
                                // Torna visíveis as checkboxes para os formatos de arquivo
                                checkBoxPDF.setVisible(true);
                                checkBoxJPEG.setVisible(true);
                                checkBoxPNG.setVisible(true);

                                // Monta uma string com os nomes dos arquivos selecionados
                                StringBuilder fileNames = new StringBuilder("Arquivos selecionados: ");
                                for (int i = 0; i < selectedFiles.length; i++) {
                                    File file = selectedFiles[i];
                                    fileNames.append(file.getName()).append(", ");
                                    filePathList.add(file.getAbsolutePath()); // Adiciona o caminho do arquivo à lista
                                }
                                selectedFileLabel.setText(fileNames.toString().trim());

                                // Define o texto para mostrar o formato do arquivo
                                formatFile.setText("Formato do arquivo: " + firstExtension);

                                // Ajusta a visibilidade das checkboxes com base na extensão do arquivo
                                if (firstExtension.equals("pdf")) {
                                    checkBoxPDF.setVisible(false);
                                } else if (firstExtension.equals("jpg") || firstExtension.equals("jpeg")) {
                                    checkBoxJPEG.setVisible(false);
                                } else if (firstExtension.equals("png")) {
                                    checkBoxPNG.setVisible(false);
                                }

                                // Torna visíveis os painéis de informações e checkboxes
                                panelInfoFile.setVisible(true);
                                panelContentChecksBoxs.setVisible(true);

                                frame.pack(); // Ajusta o tamanho do frame
                            } else {
                                // Mostra uma mensagem de erro se os arquivos tiverem extensões diferentes
                                JOptionPane.showMessageDialog(frame,
                                        "Por favor, selecione arquivos com a mesma extensão.",
                                        "Extensões Diferentes", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    } else {
                        break; // Sai do loop se o diálogo for cancelado
                    }
                }
            }
        });

        // Desabilitar checkboxs
        ActionListener actionListener = new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                JCheckBox checkBox = (JCheckBox) event.getSource(); // Obtém a checkbox que disparou o evento
                Container container = checkBox.getParent(); // Obtém o contêiner pai da checkbox

                // Desabilita todas as checkboxes e habilita apenas a selecionada
                for (Component component : container.getComponents()) {
                    if (component instanceof JCheckBox) {
                        JCheckBox otherCheckBox = (JCheckBox) component;
                        if (otherCheckBox != checkBox) {
                            otherCheckBox.setEnabled(false); // Desabilita as outras checkboxes
                        } else {
                            checkBoxSelect[0] = checkBox.getText(); // Armazena o texto da checkbox selecionada
                            checkBox.setEnabled(true); // Habilita a checkbox selecionada
                        }
                    }
                }

                // Se a checkbox foi desmarcada, reabilita todas as checkboxes
                if (!checkBox.isSelected()) {
                    for (Component component : container.getComponents()) {
                        if (component instanceof JCheckBox) {
                            component.setEnabled(true); // Reabilita todas as checkboxes
                        }
                    }

                    checkBoxSelect[0] = ""; // Limpa a seleção armazenada
                }
            }
        };

        // Adicionar o evento "Desabilitar checkboxs"
        checkBoxJPEG.addActionListener(actionListener);
        checkBoxPDF.addActionListener(actionListener);
        checkBoxPNG.addActionListener(actionListener);

        // Adicionar o evento no botão de "Inciar conversão"
        finishButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    // Chama o método que lida com a conversão de arquivos no back-end
                    backEnd.handleFileConversion();
                } catch (IOException e1) {
                    // Imprime o stack trace no console se ocorrer uma IOException
                    e1.printStackTrace();
                }
            }
        });
    }

    /**
     * Método para instanciar internamente o BackEnd
     */
    public void setBackEnd(BackEnd backEnd) {
        this.backEnd = backEnd; // Define o objeto back-end
    }

    /**
     * Método SET para definir a mensagem do botão finishButton
     * @author Grupo four-people-una
     * @version 1.0
     */
    public void setMessage(String message) {
        finishButton.setText(message); // Define o texto do botão 'finishButton'
        finishButton.setEnabled(false); // Desabilita o botão 'finishButton'

        // Cria um timer para reativar o botão após 2 segundos
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                finishButton.setText("Iniciar a conversão"); // Redefine o texto do botão
                finishButton.setEnabled(true); // Reabilita o botão
            }
        }, 2000); // Define o atraso de 2000 milissegundos (2 segundos)
    }

    /**
     * Método GET para receber uma lista de paths dos arquivos
     * @return ArrayList<String> contendo uma lista de paths dos arquivis
     * @author Grupo four-people-una
     * @version 1.0
     */
    public ArrayList<String> getSelectedFilePath() {
        return filePathList; // Retorna a lista de caminhos dos arquivos selecionados
    }

    /**
     * Método GET para receber a seleção da checkbox
     * @return String contendo a seleção da checkbox
     * @author Grupo four-people-una
     * @version 1.0
     */
    public String getCheckBoxSelection() {
        return checkBoxSelect[0]; // Retorna a seleção da checkbox
    }

    /**
     * Método GET para retornar o tipo de arquivo com base na extensão do arquivo
     * @return String contendo a extensão do arquivo
     * @author Grupo four-people-una
     * @version 1.0
     */
    private static String getFileExtension(File file) {
        String name = file.getName(); // Obtém o nome do arquivo
        int lastIndexOf = name.lastIndexOf('.'); // Encontra o índice do último ponto no nome do arquivo
        if (lastIndexOf == -1) {
            return ""; // Retorna uma string vazia se o arquivo não tiver extensão
        }
        return name.substring(lastIndexOf + 1).toLowerCase(); // Retorna a extensão do arquivo em letras minúsculas
    }
}
