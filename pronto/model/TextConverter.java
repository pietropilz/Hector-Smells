package model;

import java.util.HashMap;
import java.util.Map;

public class TextConverter {

    @FunctionalInterface
    private interface TrackManagerAction {
        void execute(TrackManager trackManager, char character, char previousCharacter, Note previousNote);
    }

    private static final Map<String, String> REPLACER;
    static {
        REPLACER = new HashMap<>();
        REPLACER.put("BPM+", "辣");
        REPLACER.put("OIT+", "你");
        REPLACER.put("OIT-", "好");
    }

    private static final Map<Character, TrackManagerAction> ACTION_MAP;
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
        Note previousNote = Note.DO;

        content = replaceContent(content);

        for (int i = 0; i < content.length(); i++){
            char character = content.charAt(i);
            processCharacter(trackManager, character, previousCharacter, previousNote);
            previousCharacter = character;

            if (Note.isNote(character)) {
                previousNote = Note.NOTE_MAP.get(character);
            }
        }
    }

    private TrackManagerAction lastAction = null;
    private char lastCharacter;
    private char lastpreviousCharacther;
    Note lastpreviousNote;

    public void processCharacter(TrackManager trackManager, char character, char previousCharacter, Note previousNote) {
        TrackManagerAction action = ACTION_MAP.get(character);

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

    public static void handleNewNote(TrackManager trackManager, char character, char previousCharacter, Note previousNote) {
        if(Note.isNote(character)) {
            Note newNote = Note.NOTE_MAP.get(character);
            trackManager.addNote(newNote);
        }
    }

    public static void pause(TrackManager trackManager, char character, char previousCharacter, Note previousNote) {
        int temp = trackManager.getVolume();
        trackManager.setVolume(0);
        trackManager.addNote(previousNote);
        trackManager.setVolume(temp);
    }

    public static void doubleVolume(TrackManager trackManager, char character, char previousCharacter, Note previousNote) {
        int doubleVolume = trackManager.getVolume() * 2;
        trackManager.setVolume(Math.min(doubleVolume, TrackManager.MAX_VOLUME));
    }

    public static void repeatNote(TrackManager trackManager, char character, char previousCharacter, Note previousNote) {
        if (Note.isNote(previousCharacter)) {
            trackManager.addNote(previousNote);
        } else {
            Instrument instrumentoAtual = trackManager.getInstrument();
            trackManager.changeInstrument(Instrument.TELEFONE);
            trackManager.addNote(Note.DO);
            trackManager.changeInstrument(instrumentoAtual);
        }
    }

    public static void newInstrument(TrackManager trackManager, char character, char previousCharacter, Note previousNote) {
        trackManager.changeInstrument(Instrument.random());
    }

    public static void increaseOctave(TrackManager trackManager, char character, char previousCharacter, Note previousNote) {
        int newOctave = trackManager.getOctave() + 1;
        trackManager.setOctave((newOctave > TrackManager.MAX_OCTAVE) ? TrackManager.DEFAULT_OCTAVE : newOctave);
    }

    public static void decreaseOctave(TrackManager trackManager, char character, char previousCharacter, Note previousNote) {
        int newOctave = trackManager.getOctave() - 1;
        trackManager.setOctave((newOctave < TrackManager.DEFAULT_OCTAVE) ? TrackManager.MAX_OCTAVE : newOctave);
    }

    public static void randomNote(TrackManager trackManager, char character, char previousCharacter, Note previousNote) {
        trackManager.addNote(Note.random());
    }

    public static void increaseBPM(TrackManager trackManager, char character, char previousCharacter, Note previousNote) {
        float bpm = trackManager.getCurrentBPM() + TrackManager.INCREASE_BPM;
        trackManager.setCurrentBPM(bpm);
    }
}
