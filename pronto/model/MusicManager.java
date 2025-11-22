package model;

import control.*;

import javax.swing.*;

public class MusicManager {
    private final MusicPlayer player;
    private final Controller controller;
    private final TextConverter reader = new TextConverter();

    private boolean isPlaying = false;

    public MusicManager(MusicPlayer player, Controller controller){
        this.player = player;
        this.controller = controller;
    }

    public void startPause(String text) {
        if (text.isEmpty()) {
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

        startMusic(text);

    }

    public void restart(String text) {
        if (!text.isEmpty()) {
            startMusic(text);
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

    public void readText(String text){
        try {
            TrackManager trackManager = controller.updateParameters();
            reader.readString(trackManager, text);
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
