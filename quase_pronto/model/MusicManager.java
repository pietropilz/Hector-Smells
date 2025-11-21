package model;

import control.*;

import javax.sound.midi.InvalidMidiDataException;
import javax.swing.*;

public class MusicManager {
    private final MusicPlayer player;
    private final Controller controller;
    private final TextConverter reader = new TextConverter();

    private boolean midiFileLoaded = false;
    private boolean isPlaying = false;

    public void setMidiFileLoaded(boolean midiFileLoaded) {
        this.midiFileLoaded = midiFileLoaded;
    }

    public MusicManager(MusicPlayer player, Controller controller){
        this.player = player;
        this.controller = controller;
    }

    public void startPause(String text) throws InvalidMidiDataException {
        if (text.isEmpty() && !midiFileLoaded) {
            controller.showMessageDialog("Digite algo para tocar!");
            return;
        }

        if (!isPlaying) {
            playMusic();
            return;
        }

        if (player.isRunning()) {
            pauseMusic();
            return;
        }

        if (!text.isEmpty()) {
            midiFileLoaded = false;

            startMusic(text);
            return;
        }

        if (midiFileLoaded) {
            try {
                player.restart();
                isPlaying = true;
            } catch (InvalidMidiDataException e) {
                controller.showMessageDialog("Erro ao reiniciar a sequência MIDI: " + e.getMessage());
            }
        }
    }

    public void restart(String text) throws InvalidMidiDataException {
        if (!text.isEmpty()) {
            midiFileLoaded = false;
            startMusic(text);
        }
        else if (midiFileLoaded) {
            player.restart();
            isPlaying = true;
        }
        else {
            controller.showMessageDialog("Digite algo para tocar!");
        }
    }

    private void startMusic(String text) {
        new Thread(() -> {
            player.stop();
            player.deleteTracks();

            readText(text);
            playMusic();
        }).start();
    }

    private void readText(String text){
        try {
            TrackManager tm = controller.updateParameters();
            reader.readString(tm, text);
        } catch (Exception e) {
            controller.showMessageDialog("Erro ao ler o texto.");
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
