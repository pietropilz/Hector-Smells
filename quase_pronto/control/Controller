package control;

import model.*;
import view.*;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.midi.Track;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;

public class Controller {
    private MusicPlayer player;
    private UserInterface screen;
    private final TextConverter reader = new TextConverter();
    private boolean isPlaying = false;
    private boolean midiFileLoaded = false;

    public void setPlayer(MusicPlayer player) {
        this.player = player;
    }

    public void setScreen(UserInterface screen) {
        this.screen = screen;
    }

    public Controller() {}

    public void buttonStartPause() {
        String text = screen.getEditText().getText();
        if (text.isEmpty()){
            if (!midiFileLoaded) {
                screen.showMessageDialog("Digite algo para tocar!");
                //JOptionPane.showMessageDialog(this, "Digite algo para tocar!");
                return;
            }
        }
        else {
            midiFileLoaded = false;
        }
        if(player != null){
            if(isPlaying){
                if (!player.isRunning()) {
                    startMusic(text);
                }
                else {
                    pauseMusic();
                }
            }
            else{
                playMusic();
            }
        }
    }

    public void buttonRestart() {
        String text = screen.getEditText().getText();
        if (text.isEmpty()) {
            if (!midiFileLoaded) {
                screen.showMessageDialog("Digite algo para tocar!");
                //OptionPane.showMessageDialog(this, "Digite algo para tocar!");
                return;
            }
        }
        else {
            midiFileLoaded = false;
        }

        if (player != null) {
            startMusic(text);
        }
    }

    public void buttonSaveFileTxt() throws IOException {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Salvar Arquivo");
        fileChooser.setFileFilter(new FileNameExtensionFilter("Arquivo de texto (*.txt)", "txt"));

        int userSelection = fileChooser.showSaveDialog(screen);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();

            if (!fileToSave.getName().toLowerCase().endsWith(".txt")) {
                fileToSave = new File(fileToSave.getAbsolutePath() + ".txt");
            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileToSave))) {
                writer.write(screen.getEditText().getText());
                screen.showMessageDialog("Arquivo salvo com sucesso!");
                //JOptionPane.showMessageDialog(this, "Arquivo salvo com sucesso!");
            } catch (IOException ex) {
                screen.showMessageDialog("Erro ao salvar o arquivo: " + ex.getMessage());
                //JOptionPane.showMessageDialog(this, "Erro ao salvar o arquivo: " + ex.getMessage());
            }
        }
    }

    public void buttonChooseFileTxt() {
        JFileChooser filechoose = new JFileChooser();
        if (filechoose.showOpenDialog(screen) == JFileChooser.APPROVE_OPTION){
            File file = filechoose.getSelectedFile();
            loadFile(file);
        }
    }

    public void buttonSaveFileMidi() throws IOException {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Salvar Arquivo");
        fileChooser.setFileFilter(new FileNameExtensionFilter("Arquivo MIDI (*.mid)", "mid"));

        int userSelection = fileChooser.showSaveDialog(screen);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();

            if (!fileToSave.getName().toLowerCase().endsWith(".mid")) {
                fileToSave = new File(fileToSave.getAbsolutePath() + ".mid");
            }

            try {
                MidiSystem.write(player.getSequence(), 1, fileToSave);
                screen.showMessageDialog("Arquivo salvo com sucesso!");
                //JOptionPane.showMessageDialog(this, "Arquivo salvo com sucesso!");
            } catch (IOException ex) {
                screen.showMessageDialog("Erro ao salvar o arquivo: " + ex.getMessage());
                //JOptionPane.showMessageDialog(this, "Erro ao salvar o arquivo: " + ex.getMessage());
            }
        }
    }

    public void buttonChooseFileMidi() throws InvalidMidiDataException, IOException {
        JFileChooser filechoose = new JFileChooser();
        if (filechoose.showOpenDialog(screen) == JFileChooser.APPROVE_OPTION){
            File file = filechoose.getSelectedFile();
            Sequence sequence = MidiSystem.getSequence(file);
            player.setSequence(sequence);
            screen.getEditText().setText("");
            midiFileLoaded = true;
        }
    }

    private void startMusic(String text) {
        new Thread(() -> {
            player.stop();

            for (Track t : player.getSequence().getTracks()) {
                player.deleteTrack(t);
            }

            readText(text);
            playMusic();
        }).start();
    }

    private void readText(String text){
        try {
            Instrument currentInstrument = screen.getCurrentInstrument();
            int currentOctave = screen.getCurrentOctave();
            int currentVolume = screen.getCurrentVolume();
            int currentBPM = screen.getCurrentBPM();

            TrackManager tm = player.createTrack(currentInstrument);
            tm.setOctave(currentOctave);
            tm.setCurrentBPM(currentBPM);
            tm.setVolume(currentVolume);

            reader.readString(tm, text);

        } catch (Exception e) {
            screen.showMessageDialog("Erro ao ler o texto.");
        }
    }

    private void loadFile(File file){
        try {
            String content = Files.readString(file.toPath());
            screen.getEditText().setText(content);
        } catch (Exception e) {
            screen.showMessageDialog("Erro ao carregar o arquivo: " + file);
        }
    }

    private void playMusic() {
        player.play();
        isPlaying = true;
    }

    private void pauseMusic() {
        player.pause();
        isPlaying = false;
    }
}
