package model;

import javax.sound.midi.*;
// MUDANÇA: Importação necessária para eventos de tempo (MetaMessage)
import javax.sound.midi.MetaMessage;

public class TrackManager {
    public static final int TIME_BEGIN = 4;
    public static final int DEFAULT_VOLUME = 100;
    public static final int MAX_VOLUME = 127;
    public static final int DEFAULT_OCTAVE = -1;
    public static final int MAX_OCTAVE = 9;
    public static final float DEFAULT_BPM = 120.0f;
    public static final float INCREASE_BPM = 80.0f;

    private int volume;
    private int octave;
    private Instrument instrument;
    private int duration;

    private final Track track;
    private final int channel;
    private int tick;

    // MUDANÇA: Adicionado rastreador de BPM atual
    private float currentBPM;

    public int getVolume() {
        return this.volume;
    }
    public int getOctave() {
        return this.octave;
    }
    public Instrument getInstrument(){return this.instrument;}

    public void setOctave(int octave) {
        this.octave = octave;
    }
    public void setVolume(int volume) {
        this.volume = volume;
    }
    public void setDuration(int duration) {this.duration = duration;}

    public void setCurrentBPM(float currentBPM) {
        this.currentBPM = currentBPM;
        addTempoChange(currentBPM);
    }

    public TrackManager(Track track, Instrument currentInstrument) {
        this.octave = DEFAULT_OCTAVE;
        //this.volume = currentVolume;
        this.instrument = currentInstrument;
        this.volume = DEFAULT_VOLUME;
        this.duration = Note.DEFAULT_DURATION;

        this.track = track;
        this.channel = 0;
        this.tick = TIME_BEGIN;

        this.currentBPM = DEFAULT_BPM;
        //this.addTempoChange(this.currentBPM);

        this.changeInstrument(this.instrument);
    }


    public void changeInstrument(Instrument instrument) {
        try {
            ShortMessage sm = new ShortMessage();
            sm.setMessage(ShortMessage.PROGRAM_CHANGE, this.channel, instrument.getCodigo(), 0);
            track.add(new MidiEvent(sm, this.tick));
            this.instrument = instrument;

        }
        catch (InvalidMidiDataException _) {}
    }

    public int getFullTone(int semitone) {
        int fullTone = 12 * (this.octave + 1) + semitone;
        return Math.min(fullTone, Note.MAX_SEMITONE);
    }


    public void addNote(Note note) {
        int semitone = note.getSemitone();
        int duration = this.duration;
        int fullTone = getFullTone(semitone);

        try{
            ShortMessage on = new ShortMessage();
            on.setMessage(ShortMessage.NOTE_ON, this.channel, fullTone, this.volume);
            this.track.add(new MidiEvent(on, this.tick));

            ShortMessage off = new ShortMessage();
            off.setMessage(ShortMessage.NOTE_OFF, this.channel, fullTone, this.volume);
            this.track.add(new MidiEvent(off, this.tick + duration));

            this.tick += duration;
        }
        catch (InvalidMidiDataException _) {}
    }

    // MUDANÇA: Adicionado método para inserir eventos de tempo (BPM) na trilha
    public void addTempoChange(float bpm) {
        if (bpm <= 0) return;

        // Converte BPM para "microsegundos por semínima" (MPQ)
        long mpq = (long) (60000000.0 / bpm);

        // O MetaMessage de tempo armazena o MPQ como 3 bytes
        byte[] data = new byte[3];
        data[0] = (byte) ((mpq >> 16) & 0xFF);
        data[1] = (byte) ((mpq >> 8) & 0xFF);
        data[2] = (byte) (mpq & 0xFF);

        try {
            MetaMessage tempoMessage = new MetaMessage();
            // 0x51 é o código padrão MIDI para "Set Tempo"
            tempoMessage.setMessage(0x51, data, 3);

            // Adiciona o evento de tempo na track, no tick ATUAL
            this.track.add(new MidiEvent(tempoMessage, this.tick));

        } catch (InvalidMidiDataException _) {}
    }

    public static void handleNewNote(TrackManager sound, char character, char previousCharacter, int previousNote) {
        if(Note.isNote(character)) {
            int newNote = Note.charToNote(character);
            sound.addNote(new Note(newNote, Note.DEFAULT_DURATION));
        }
    }

    public static void pause(TrackManager sound, char character, char previousCharacter, int previousNote) {
        int temp = sound.getVolume();
        sound.setVolume(0);
        sound.addNote(new Note(previousNote, Note.DEFAULT_DURATION));
        sound.setVolume(temp);
    }

    public static void doubleVolume(TrackManager sound, char character, char previousCharacter, int previousNote) {
        int doubleVolume = sound.getVolume() * 2;
        sound.setVolume(Math.min(doubleVolume, TrackManager.MAX_VOLUME));
    }

    public static void repeatNote(TrackManager sound, char character, char previousCharacter, int previousNote) {
        if (Note.isNote(previousCharacter)) {
            sound.addNote(new Note(previousNote, Note.DEFAULT_DURATION));
        } else {
            Instrument instrumentoAtual = sound.getInstrument();
            sound.changeInstrument(Instrument.TELEFONE);
            sound.addNote(new Note(Note.DO, Note.DEFAULT_DURATION));
            sound.changeInstrument(instrumentoAtual);
        }
    }

    public static void newInstrument(TrackManager sound, char character, char previousCharacter, int previousNote) {
        sound.changeInstrument(Instrument.random());
    }

    public static void increaseOctave(TrackManager sound, char character, char previousCharacter, int previousNote) {
        int newOctave = sound.getOctave() + 1;
        sound.setOctave((newOctave > TrackManager.MAX_OCTAVE) ? TrackManager.DEFAULT_OCTAVE : newOctave);
    }

    public static void decreaseOctave(TrackManager sound, char character, char previousCharacter, int previousNote) {
        int newOctave = sound.getOctave() - 1;
        sound.setOctave((newOctave < TrackManager.DEFAULT_OCTAVE) ? TrackManager.MAX_OCTAVE : newOctave);
    }

    public static void randomNote(TrackManager sound, char character, char previousCharacter, int previousNote) {
        char randomChar = (char) ('A' + (int)(Math.random() * 8));
        sound.addNote(new Note(randomChar, Note.DEFAULT_DURATION));
    }

    public static void changeDuration(TrackManager trackManager, char character, char previousCharacter, int previousNote) {
        trackManager.setDuration((int) character - (int) '0');
    }

    public static void increaseBPM(TrackManager trackManager, char character, char previousCharacter, int previousNote) {
        trackManager.currentBPM += INCREASE_BPM;
        trackManager.addTempoChange(trackManager.currentBPM);
    }
}