package model;

import javax.sound.midi.*;
import javax.sound.midi.MetaMessage;

public class TrackManager {
    public static final int TIME_BEGIN = 4;
    public static final int DEFAULT_VOLUME = 100;
    public static final int MAX_VOLUME = 127;
    public static final int DEFAULT_OCTAVE = -1;
    public static final int MAX_OCTAVE = 9;
    public static final float DEFAULT_BPM = 120.0f;
    public static final float INCREASE_BPM = 80.0f;
    public static final int DEFAULT_DURATION = 4;
    public static final int MAX_SEMITONE = 127;

    private int volume;
    private int octave;
    private Instrument instrument;

    private final Track track;
    private final int channel;
    private int tick;

    private float currentBPM;

    public int getVolume() {
        return this.volume;
    }

    public int getOctave() {
        return this.octave;
    }

    public Instrument getInstrument() {
        return this.instrument;
    }

    public float getCurrentBPM() {
        return currentBPM;
    }

    public void setOctave(int octave) {
        this.octave = octave;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }


    public void setCurrentBPM(float currentBPM) {
        this.currentBPM = currentBPM;
        addTempoChange(currentBPM);
    }

    public TrackManager(Track track, Instrument currentInstrument) {
        this.octave = DEFAULT_OCTAVE;
        this.instrument = currentInstrument;
        this.volume = DEFAULT_VOLUME;

        this.track = track;
        this.channel = 0;
        this.tick = TIME_BEGIN;

        this.currentBPM = DEFAULT_BPM;

        this.changeInstrument(this.instrument);
    }


    public void changeInstrument(Instrument instrument) {
        try {
            ShortMessage sm = new ShortMessage();
            sm.setMessage(ShortMessage.PROGRAM_CHANGE, this.channel, instrument.getInstrumentCode(), 0);
            track.add(new MidiEvent(sm, this.tick));
            this.instrument = instrument;

        } catch (InvalidMidiDataException _) {
        }
    }

    public int getFullTone(int semitone) {
        int fullTone = 12 * (this.octave + 1) + semitone;
        return Math.min(fullTone, MAX_SEMITONE);
    }


    public void addNote(Note note) {
        int semitone = note.getSemitone();
        int duration = DEFAULT_DURATION;
        int fullTone = getFullTone(semitone);

        try {
            ShortMessage on = new ShortMessage();
            on.setMessage(ShortMessage.NOTE_ON, this.channel, fullTone, this.volume);
            this.track.add(new MidiEvent(on, this.tick));

            ShortMessage off = new ShortMessage();
            off.setMessage(ShortMessage.NOTE_OFF, this.channel, fullTone, this.volume);
            this.track.add(new MidiEvent(off, this.tick + duration));

            this.tick += duration;
        } catch (InvalidMidiDataException _) {
        }
    }

    public void addTempoChange(float bpm) {
        if (bpm <= 0) return;

        long mpq = (long) (60000000.0 / bpm);

        byte[] data = new byte[3];
        data[0] = (byte) ((mpq >> 16) & 0xFF);
        data[1] = (byte) ((mpq >> 8) & 0xFF);
        data[2] = (byte) (mpq & 0xFF);

        try {
            MetaMessage tempoMessage = new MetaMessage();
            tempoMessage.setMessage(0x51, data, 3);

            this.track.add(new MidiEvent(tempoMessage, this.tick));

        } catch (InvalidMidiDataException _) {
        }
    }
}