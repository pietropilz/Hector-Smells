package model;

import java.util.HashMap;
import java.util.Map;

public enum Note {
    DO(0),
    RE(2),
    MI(4),
    FA(5),
    SOL(7),
    LA(9),
    SI(11),
    SIB(10);

    private final int semitone;

    Note(int semitone) {this.semitone = semitone;}

    public int getSemitone() {
        return semitone;
    }

    public static Note random() {
        Note[] notes = values();
        int index = java.util.concurrent.ThreadLocalRandom.current().nextInt(notes.length);
        return notes[index];
    }

    public static boolean isNote(char character) {
        return ('A' <= character && character<= 'H') ||
                ('a' <= character && character<= 'h');
    }

    public static final Map<Character, Note> NOTE_MAP;
    static {
        NOTE_MAP = new HashMap<>();
        NOTE_MAP.put('A', Note.LA);
        NOTE_MAP.put('B', Note.SI);
        NOTE_MAP.put('C', Note.DO);
        NOTE_MAP.put('D', Note.RE);
        NOTE_MAP.put('E', Note.MI);
        NOTE_MAP.put('F', Note.FA);
        NOTE_MAP.put('G', Note.SOL);
        NOTE_MAP.put('H', Note.SIB);
        NOTE_MAP.put('a', Note.LA);
        NOTE_MAP.put('b', Note.SI);
        NOTE_MAP.put('c', Note.DO);
        NOTE_MAP.put('d', Note.RE);
        NOTE_MAP.put('e', Note.MI);
        NOTE_MAP.put('f', Note.FA);
        NOTE_MAP.put('g', Note.SOL);
        NOTE_MAP.put('h', Note.SIB);
    }
}
