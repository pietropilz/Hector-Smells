package model;

import javax.sound.midi.*;

public class MusicPlayer {
    private final Sequencer playerController;
    private Sequence sequence;

    public MusicPlayer() throws Exception {
        playerController = MidiSystem.getSequencer();
        playerController.open();
        sequence = new Sequence(Sequence.PPQ, 4);
    }

    public Sequence getSequence() {
        return this.sequence;
    }

    public void setSequence(Sequence sequence) {this.sequence = sequence;}
    //public Sequencer getPlayerController() {return this.playerController;}

    public TrackManager createTrack(Instrument currentInstrument) {
        Track track = this.sequence.createTrack();
        return new TrackManager(track,currentInstrument);

    }

    public void deleteTrack(Track track) {
        this.sequence.deleteTrack(track);
    }

    public void play() {
        try {
            playerController.setSequence(sequence);
            playerController.start();
        } catch (Exception _) {}
    }

    public void pause() {
        if (playerController.isRunning()) {
            playerController.stop();
        }
    }

    public void stop() {
        playerController.stop();
        playerController.setTickPosition(0);
    }

    public boolean isRunning() {
        return this.playerController.isRunning();
    }

    /*
    public void restart() {
        playerController.setTickPosition(TrackManager.TIME_BEGIN);
        playerController.start();
    }
    */

    /*
    public void close() {
        playerController.close();
    }
    */
}
