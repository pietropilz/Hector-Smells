package model;

import java.util.HashMap;
import java.util.Map;

public class TextConverter {

    @FunctionalInterface
    private interface SoundTrackAction {
        void execute(TrackManager sound, char character, char previousCharacter, int previousNote);
    }

    private static final Map<String, String> REPLACER;
    static {
        REPLACER = new HashMap<>();
        REPLACER.put("BPM+", "辣");
        REPLACER.put("OIT+", "你");
        REPLACER.put("OIT-", "好");
    }

    private static final Map<Character, SoundTrackAction> ACTION_MAP;
    static {
        ACTION_MAP = new HashMap<>();

        // Letras que geram nova nota
        for (char c : "ABCDEFGHabcdefgh".toCharArray()) {
            ACTION_MAP.put(c, TextConverter::handleNewNote);
        }

        // Ações únicas
        ACTION_MAP.put(' ', TextConverter::doubleVolume);
        ACTION_MAP.put('辣', TextConverter::increaseBPM);
        ACTION_MAP.put('你', TextConverter::increaseOctave);
        ACTION_MAP.put('好', TextConverter::decreaseOctave);
        ACTION_MAP.put('?', TextConverter::randomNote);
        ACTION_MAP.put('\n', TextConverter::newInstrument);
        ACTION_MAP.put(';', TextConverter::pause);

        for (char c : "12345678".toCharArray()) {
            ACTION_MAP.put(c, TextConverter::changeDuration);
        }

        // Letras que repetem nota
        for (char c : "OoIiUu".toCharArray()) {
            ACTION_MAP.put(c, TextConverter::repeatNote);
        }
    }

    public TextConverter(){}

    private String replaceContent(String content) {
        for (Map.Entry<String, String> entry : REPLACER.entrySet()) {
            content = content.replace(entry.getKey(), entry.getValue());
        }

        return content;
    }

    public void readString(TrackManager trackManager, String content) {
        char previousCharacter = '\0';
        int previousNote = Note.DO;

        content = replaceContent(content);

        for (int i = 0; i < content.length(); i++){
            char character = content.charAt(i);
            processCharacter(trackManager, character, previousCharacter, previousNote);
            previousCharacter = character;

            if (Note.isNote(character)) {
                previousNote = Note.charToNote(character);
            }
        }
    }

    private SoundTrackAction lastAction = null;
    private char lastCharacter;
    private char lastpreviousCharacther;
    int lastpreviousNote;

    public void processCharacter(TrackManager trackManager, char character, char previousCharacter, int previousNote) {
        SoundTrackAction action = ACTION_MAP.get(character);

        if (action != null) {
            action.execute(trackManager, character, previousCharacter, previousNote);
        }
        else if (lastAction != null) {
            lastAction.execute(trackManager, lastCharacter, lastpreviousCharacther, lastpreviousNote);
            return;
        }
        lastAction = action;
        lastCharacter = character;
        lastpreviousCharacther = previousCharacter;
        lastpreviousNote = previousNote;
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
        float bpm = trackManager.getCurrentBPM() + TrackManager.INCREASE_BPM;
        trackManager.setCurrentBPM(bpm);
    }
}
