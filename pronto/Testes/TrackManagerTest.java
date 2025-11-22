package Testes;

import model.Instrument;
import model.TrackManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.sound.midi.Sequence;
import javax.sound.midi.Track;

import static org.junit.jupiter.api.Assertions.*;

class TrackManagerTest {

    private TrackManager trackManager;

    @BeforeEach
    void setUp() throws Exception {
        // Configuração mínima para criar um TrackManager funcional
        Sequence sequence = new Sequence(Sequence.PPQ, 4);
        Track track = sequence.createTrack();
        trackManager = new TrackManager(track, Instrument.PIANO);
    }

    @Test
    void testInitialState() {
        assertEquals(TrackManager.DEFAULT_VOLUME, trackManager.getVolume());
        assertEquals(TrackManager.DEFAULT_OCTAVE, trackManager.getOctave());
        assertEquals(TrackManager.DEFAULT_BPM, trackManager.getCurrentBPM());
        assertEquals(Instrument.PIANO, trackManager.getInstrument());
    }

    @Test
    void testSetVolume() {
        trackManager.setVolume(50);
        assertEquals(50, trackManager.getVolume());
    }

    @Test
    void testSetOctave() {
        trackManager.setOctave(5);
        assertEquals(5, trackManager.getOctave());
    }

    @Test
    void testChangeInstrument() {
        trackManager.changeInstrument(Instrument.GUITARRA);
        assertEquals(Instrument.GUITARRA, trackManager.getInstrument());
    }

    @Test
    void testGetFullToneCalculation() {
        // Teste de cálculo de tom: 12 * (octave + 1) + semitone
        // Octave padrão é -1. Então 12 * 0 + 0 (Dó) = 0
        trackManager.setOctave(-1);
        assertEquals(0, trackManager.getFullTone(0));

        // Octave 0. Então 12 * 1 + 0 = 12
        trackManager.setOctave(0);
        assertEquals(12, trackManager.getFullTone(0));
    }
}