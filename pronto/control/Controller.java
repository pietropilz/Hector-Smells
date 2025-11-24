package control;

import model.*;
import view.*;

import javax.sound.midi.InvalidMidiDataException;
import javax.swing.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;

public class Controller {
    private final MusicPlayer player = new MusicPlayer();
    private final UserInterface screen = new UserInterface(this);
    private final FileManager fileManager = new FileManager(this);
    private final MusicManager musicManager = new MusicManager(player, this);

    public Controller() throws Exception {}

    public void buttonStartPause() throws InvalidMidiDataException {
        String text = screen.getEditText().getText();
        musicManager.startPause(text);
    }

    public void buttonRestart() throws InvalidMidiDataException {
        String text = screen.getEditText().getText();
        musicManager.restart(text);
    }

    private void saveFile(String extension) {
        String text = screen.getEditText().getText();
        if (text.isEmpty()) {
            screen.showMessageDialog("Digite algo para salvar!");
            return;
        }

        musicManager.readText(text);

        JFileChooser fileChooser = fileManager.chooseFile(extension);
        int userSelection = fileChooser.showSaveDialog(screen);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            fileManager.writeFile(fileChooser, extension, text, player.getSequence());
        }
    }

    public void buttonSaveFileTxt() throws IOException {

        saveFile("txt");
    }

    public void buttonChooseFileTxt() {
        JFileChooser filechoose = new JFileChooser();
        if (filechoose.showOpenDialog(screen) == JFileChooser.APPROVE_OPTION){
            File file = filechoose.getSelectedFile();
            loadFile(file);
        }
    }

    public void buttonSaveFileMidi() throws IOException {
        saveFile("mid");
    }

    public StringBuilder buttonDocumentation() throws IOException {
        return fileManager.buttonDocumentation();
    }

    private void loadFile(File file){
        try {
            String content = Files.readString(file.toPath());
            screen.getEditText().setText(content);
        } catch (Exception e) {
            screen.showMessageDialog("Erro ao carregar o arquivo: " + file);
        }
    }

    public TrackManager updateParameters() {
        Instrument currentInstrument = screen.getCurrentInstrument();
        int currentOctave = screen.getCurrentOctave();
        int currentVolume = screen.getCurrentVolume();
        int currentBPM = screen.getCurrentBPM();

        TrackManager trackManager = player.createTrack(currentInstrument);
        trackManager.setOctave(currentOctave);
        trackManager.setCurrentBPM(currentBPM);
        trackManager.setVolume(currentVolume);

        return trackManager;
    }

    public void showMessageDialog(String string) {
        screen.showMessageDialog(string);
    }
}
