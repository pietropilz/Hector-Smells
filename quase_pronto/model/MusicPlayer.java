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

    public void setSequence(Sequence sequence) throws InvalidMidiDataException {
        this.sequence = sequence;
        playerController.setSequence(sequence);
    }

    public TrackManager createTrack(Instrument currentInstrument) {
        Track track = this.sequence.createTrack();
        return new TrackManager(track,currentInstrument);

    }

    public void deleteTracks() {
        for (Track track : sequence.getTracks()) {
            sequence.deleteTrack(track);
        }
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

    public void restart() throws InvalidMidiDataException {
        playerController.setSequence(sequence);
        playerController.setTickPosition(TrackManager.TIME_BEGIN);
        playerController.start();
    }
}