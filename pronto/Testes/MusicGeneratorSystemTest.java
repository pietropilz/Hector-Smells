package Testes;

import model.Instrument;
import model.MusicPlayer;
import model.TextConverter;
import model.TrackManager;
import org.junit.jupiter.api.Test;

import javax.sound.midi.MidiEvent;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;

import static org.junit.jupiter.api.Assertions.*;

class MusicGeneratorSystemTest {

    @Test
    void testTextToMidiGenerationFlow() throws Exception {
        // 1. Configuração do Ambiente (Simula o Controller/MusicManager)
        MusicPlayer player = new MusicPlayer();
        TextConverter converter = new TextConverter();

        // Cria um TrackManager vinculado à Sequência do Player
        TrackManager trackManager = player.createTrack(Instrument.PIANO);

        // 2. Execução (Input do Usuário)
        // Texto: "A " -> Nota A (La) + Espaço (Dobra Volume)
        String inputText = "A ";
        converter.readString(trackManager, inputText);

        // 3. Verificação dos Resultados (Inspeção da Sequência MIDI gerada)
        Track track = player.getSequence().getTracks()[0];

        assertTrue(track.size() > 0, "A trilha MIDI deve conter eventos.");

        boolean noteOnFound = false;
        boolean noteOffFound = false;

        // Itera sobre os eventos MIDI gerados para garantir que notas foram criadas
        for (int i = 0; i < track.size(); i++) {
            MidiEvent event = track.get(i);
            if (event.getMessage() instanceof ShortMessage) {
                ShortMessage sm = (ShortMessage) event.getMessage();
                if (sm.getCommand() == ShortMessage.NOTE_ON) {
                    noteOnFound = true;
                } else if (sm.getCommand() == ShortMessage.NOTE_OFF) {
                    noteOffFound = true;
                }
            }
        }

        assertTrue(noteOnFound, "Deve haver pelo menos um evento NOTE_ON (tocar nota).");
        assertTrue(noteOffFound, "Deve haver pelo menos um evento NOTE_OFF (parar nota).");
    }

    @Test
    void testInstrumentChangeFlow() throws Exception {
        MusicPlayer player = new MusicPlayer();
        TextConverter converter = new TextConverter();
        TrackManager trackManager = player.createTrack(Instrument.PIANO);

        // '\n' (Nova Linha) deve trocar o instrumento aleatoriamente
        // Repetimos algumas vezes para garantir que o evento seja registrado
        converter.readString(trackManager, "\n");

        Track track = player.getSequence().getTracks()[0];
        boolean programChangeFound = false;

        for (int i = 0; i < track.size(); i++) {
            MidiEvent event = track.get(i);
            if (event.getMessage() instanceof ShortMessage) {
                ShortMessage sm = (ShortMessage) event.getMessage();
                // Comando 192 (0xC0) é PROGRAM_CHANGE (Troca de Instrumento)
                if (sm.getCommand() == ShortMessage.PROGRAM_CHANGE) {
                    programChangeFound = true;
                    break;
                }
            }
        }

        assertTrue(programChangeFound, "O sistema deve gerar um evento de troca de instrumento (Program Change) ao encontrar uma nova linha.");
    }
}