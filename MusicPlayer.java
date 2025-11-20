package music;

import javax.sound.midi.*;

public class MusicPlayer {
    private Sequencer controller;
    private Sequence sequence;

    public MusicPlayer() throws Exception {
        controller = MidiSystem.getSequencer();
        controller.open();
        sequence = new Sequence(sequence.PPQ, 4);
    }

    public Sequence getSequence() {
        return this.sequence;
    }
    public Sequencer getController() {return this.controller;}

    public SoundTrack createTrack(Instrument currentInstrument, int volumeAtual,int Currentoctave, int CurrentBPM) {
        Track track = this.sequence.createTrack();
        return new SoundTrack(track,currentInstrument,volumeAtual, Currentoctave, CurrentBPM);

    }

    public boolean deleteTrack(Track track) {
        return this.sequence.deleteTrack(track);
    }

    public void play() {
        try {
            controller.setSequence(sequence);
            controller.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void pause() {
        if (controller.isRunning()) {
            controller.stop();
        }
    }

    public void stop() {
        controller.stop();
        controller.setTickPosition(0);
    }

    public void restart() {
        controller.setTickPosition(SoundTrack.TIME_BEGIN);
        controller.start();
    }

    public boolean isRunning() {
        return this.controller.isRunning();
    }

    public void close() {
        controller.close();
    }
}
