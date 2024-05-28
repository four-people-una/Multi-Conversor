// Classe FrontEnd.java
package com.coversor;

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

public class FrontEnd {
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

    private BackEnd backEnd;

    public FrontEnd() {
        checkBoxSelect = new String[] { "" };

        frame = new JFrame("MultiConversor");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        initComponents();

        addListeners();

        frame.pack();
        frame.setVisible(true);
    }

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
        // open Files
        openButton.addActionListener(new ActionListener() {
            @Override

            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();

                fileChooser.setMultiSelectionEnabled(true);

                FileNameExtensionFilter filter = new FileNameExtensionFilter(
                        "PNG, JPEG, and PDF files", "png", "jpeg", "jpg", "pdf");

                fileChooser.setFileFilter(filter);

                boolean validSelection = false;
                while (!validSelection) {
                    // Mostra o diálogo de escolha de arquivo
                    int returnValue = fileChooser.showOpenDialog(frame);

                    if (returnValue == JFileChooser.APPROVE_OPTION) {
                        // Pega os arquivos selecionados
                        File[] selectedFiles = fileChooser.getSelectedFiles();
                        if (selectedFiles.length > 0) {
                            // Verifica se todas as extensões são iguais
                            String firstExtension = getFileExtension(selectedFiles[0]);
                            boolean allSameExtension = true;

                            for (File file : selectedFiles) {
                                if (!firstExtension.equals(getFileExtension(file))) {
                                    allSameExtension = false;
                                    break;
                                }
                            }

                            if (allSameExtension) {
                                validSelection = true;
                                checkBoxPDF.setVisible(true);
                                checkBoxJPEG.setVisible(true);
                                checkBoxPNG.setVisible(true);

                                StringBuilder fileNames = new StringBuilder("Arquivos selecionados: ");
                                for (int i = 0; i < selectedFiles.length; i++) {
                                    File file = selectedFiles[i];
                                    fileNames.append(file.getName()).append(", ");
                                    filePathList.add(file.getAbsolutePath());
                                }
                                selectedFileLabel.setText(fileNames.toString().trim());

                                formatFile.setText("Formato do arquivo: " + firstExtension);

                                if (firstExtension.equals("pdf")) {
                                    checkBoxPDF.setVisible(false);
                                } else if (firstExtension.equals("jpg") || firstExtension.equals("jpeg")) {
                                    checkBoxJPEG.setVisible(false);
                                } else if (firstExtension.equals("png")) {
                                    checkBoxPNG.setVisible(false);
                                }

                                panelInfoFile.setVisible(true);
                                panelContentChecksBoxs.setVisible(true);

                                frame.pack();
                            } else {
                                JOptionPane.showMessageDialog(frame,
                                        "Por favor, selecione arquivos com a mesma extensão.",
                                        "Extensões Diferentes", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    } else {
                        break;
                    }
                }
            }

        });

        // event checkBox
        ActionListener actionListener = new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                JCheckBox checkBox = (JCheckBox) event.getSource();
                Container container = checkBox.getParent();

                // disable all and active only select
                for (Component component : container.getComponents()) {
                    if (component instanceof JCheckBox) {
                        JCheckBox otherCheckBox = (JCheckBox) component;
                        if (otherCheckBox != checkBox) {
                            otherCheckBox.setEnabled(false);
                        } else {
                            checkBoxSelect[0] = checkBox.getText();
                            checkBox.setEnabled(true);
                        }
                    }
                }

                if (!checkBox.isSelected()) {
                    for (Component component : container.getComponents()) {
                        if (component instanceof JCheckBox) {
                            component.setEnabled(true);
                        }
                    }

                    checkBoxSelect[0] = "";
                }
            }
        };

        checkBoxJPEG.addActionListener(actionListener);
        checkBoxPDF.addActionListener(actionListener);
        checkBoxPNG.addActionListener(actionListener);

        // finish
        finishButton.addActionListener(new ActionListener() {
            @Override

            public void actionPerformed(ActionEvent e) {
                try {
                    backEnd.handleFileConversion();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
    }

    // SETTERS
    public void setBackEnd(BackEnd backEnd) {
        this.backEnd = backEnd;
    }

    public void setMessage(String messagem) {
        finishButton.setText(messagem);
        finishButton.setEnabled(false);

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                finishButton.setText("Iniciar a conversão");
                finishButton.setEnabled(true);
            }
        }, 2000);
    }

    // GETTERS
    public ArrayList<String> getSelectedFilePath() {
        return filePathList;
    }

    public String getCheckBoxSelection() {
        return checkBoxSelect[0];
    }

    /* METHODS */
    private static String getFileExtension(File file) {
        String name = file.getName();
        int lastIndexOf = name.lastIndexOf('.');
        if (lastIndexOf == -1) {
            return ""; // Arquivo sem extensão
        }
        return name.substring(lastIndexOf + 1).toLowerCase();
    }
}
