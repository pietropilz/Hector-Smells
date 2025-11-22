package model;

import control.*;

import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.io.File;

public class FileManager {
    private final Controller controller;

    public FileManager(Controller controller){
        this.controller = controller;
    }

    private static final Map<String, String> EXTENSIONS;

    static {
        EXTENSIONS = new HashMap<>();
        EXTENSIONS.put("mid", "MIDI");
        EXTENSIONS.put("txt", "texto");
    }

    public JFileChooser chooseFile(String extension) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Salvar Arquivo");
        String extensionName = EXTENSIONS.get(extension);
        String description = "Arquivo " + extensionName + " (*." + extension + ")";
        fileChooser.setFileFilter(new FileNameExtensionFilter(description, extension));

        return fileChooser;
    }

    public void writeFile(JFileChooser fileChooser, String extension, String text, Sequence sequence) {
        File fileToSave = fileChooser.getSelectedFile();
        String newExtension = "." + extension;

        if (!fileToSave.getName().toLowerCase().endsWith(newExtension)) {
            fileToSave = new File(fileToSave.getAbsolutePath() + newExtension);
        }

        if (extension.equals("txt")) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileToSave))) {
                writer.write(text);
                controller.showMessageDialog("Arquivo salvo com sucesso!");
            } catch (IOException ex) {
                controller.showMessageDialog("Erro ao salvar o arquivo: " + ex.getMessage());
            }
        } else if (extension.equals("mid")) {
            try {
                MidiSystem.write(sequence, 1, fileToSave);
                controller.showMessageDialog("Arquivo salvo com sucesso!");
            } catch (IOException ex) {
                controller.showMessageDialog("Erro ao salvar o arquivo: " + ex.getMessage());
            }
        }

    }
}
