package Testes;

import model.Note;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class NoteTest {

    @Test
    void testGetSemitone() {
        assertEquals(0, Note.DO.getSemitone());
        assertEquals(9, Note.LA.getSemitone());
        assertEquals(11, Note.SI.getSemitone());
    }

    @Test
    void testIsNote() {
        assertTrue(Note.isNote('A'));
        assertTrue(Note.isNote('g'));
        assertFalse(Note.isNote('Z'));
        assertFalse(Note.isNote('1'));
        assertFalse(Note.isNote(' '));
    }

    @Test
    void testNoteMapping() {
        assertEquals(Note.LA, Note.NOTE_MAP.get('A'));
        assertEquals(Note.DO, Note.NOTE_MAP.get('C'));
        assertEquals(Note.SOL, Note.NOTE_MAP.get('G'));
        // Testa case insensitive definido no mapa
        assertEquals(Note.LA, Note.NOTE_MAP.get('a'));
    }
}
