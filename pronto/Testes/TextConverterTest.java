package Testes;

import model.Instrument;
import model.TextConverter;
import model.TrackManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.sound.midi.Sequence;
import javax.sound.midi.Track;

import static org.junit.jupiter.api.Assertions.*;

class TextConverterTest {

    private TextConverter converter;
    private TrackManager trackManager;

    @BeforeEach
    void setUp() throws Exception {
        converter = new TextConverter();
        Sequence sequence = new Sequence(Sequence.PPQ, 4);
        Track track = sequence.createTrack();
        trackManager = new TrackManager(track, Instrument.PIANO);
    }

    @Test
    void testDoubleVolumeCommand() {
        // O caractere ' ' (espaço) deve dobrar o volume
        trackManager.setVolume(20);
        converter.readString(trackManager, " ");
        assertEquals(40, trackManager.getVolume());
    }

    @Test
    void testVolumeCap() {
        // Testa se o volume não excede o MAX_VOLUME (127)
        trackManager.setVolume(100);
        converter.readString(trackManager, " "); // Dobra para 200 -> deve travar em 127
        assertEquals(TrackManager.MAX_VOLUME, trackManager.getVolume());
    }

    @Test
    void testOctaveIncreaseCommand() {
        // O comando "OIT+" é substituído por '你' internamente e aumenta a oitava
        int initialOctave = trackManager.getOctave();
        converter.readString(trackManager, "OIT+");
        assertEquals(initialOctave + 1, trackManager.getOctave());
    }

    @Test
    void testBPMIncreaseCommand() {
        // O comando "BPM+" aumenta o BPM em 80
        float initialBPM = trackManager.getCurrentBPM();
        converter.readString(trackManager, "BPM+");
        assertEquals(initialBPM + 80.0f, trackManager.getCurrentBPM());
    }

    @Test
    void testPauseCommand() {
        // ';' deve pausar (tocar nota com volume 0 e restaurar)
        trackManager.setVolume(50);
        converter.readString(trackManager, ";");
        // O volume deve ter retornado ao original após a execução
        assertEquals(50, trackManager.getVolume());
    }
}