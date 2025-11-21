package model;

import java.util.HashMap;
import java.util.Map;

public class TextConverter {

    @FunctionalInterface
    private interface SoundTrackAction {
        void execute(TrackManager sound, char character, char previousCharacter, int previousNote);
    }

    private static final Map<Character, SoundTrackAction> ACTION_MAP;
    private static final Map<String, String> REPLACER;

    static {
        REPLACER = new HashMap<>();
        REPLACER.put("BPM+", "辣");
        REPLACER.put("OIT+", "你");
        REPLACER.put("OIT-", "好");
    }

    static {
        ACTION_MAP = new HashMap<>();

        // Letras que geram nova nota
        for (char c : "ABCDEFGHabcdefgh".toCharArray()) {
            ACTION_MAP.put(c, TrackManager::handleNewNote);
        }

        // Ações únicas
        ACTION_MAP.put(' ', TrackManager::doubleVolume);
        ACTION_MAP.put('辣', TrackManager::increaseBPM);
        ACTION_MAP.put('你', TrackManager::increaseOctave);
        ACTION_MAP.put('好', TrackManager::decreaseOctave);
        ACTION_MAP.put('?', TrackManager::randomNote);
        ACTION_MAP.put('\n', TrackManager::newInstrument);
        ACTION_MAP.put(';', TrackManager::pause);

        for (char c : "12345678".toCharArray()) {
            ACTION_MAP.put(c, TrackManager::changeDuration);
        }

        // Letras que repetem nota
        for (char c : "OoIiUu".toCharArray()) {
            ACTION_MAP.put(c, TrackManager::repeatNote);
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
}