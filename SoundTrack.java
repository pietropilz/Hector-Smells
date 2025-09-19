/*
 * Para compilar e salvar no pacote music:
 * javac -d . music\SoundTrack.java
 *
 * Para importar:
 * import music.*
 */

package music;

import javax.sound.midi.*;

public class SoundTrack extends Music {
	private Note[] allNotes;
	private Track track;
	private int channel;

	public SoundTrack(Track track, int channel, Note[] allNotes) {
		super(allNotes);
		this.track = track;
		this.channel = channel;
	}

	public Track getTrack() {
		return track;
	}

	public void setChannel(int channel) {
		this.channel = channel;
	}

	public int getChannel() {
		return channel;
	}

	public boolean createTrack(int tick) {
		try {
			for (Note note : this.getAllNotes()) {
				int instrument = note.getInstrument();
				int volume = note.getVolume();
				int fullNote = note.getFullNote();
				int duration = note.getDuration();
				int channel = this.getChannel();

		        ShortMessage sm = new ShortMessage();
		        sm.setMessage(ShortMessage.PROGRAM_CHANGE, channel, instrument, 0);
		        track.add(new MidiEvent(sm, tick));

				ShortMessage on = new ShortMessage();
	            on.setMessage(ShortMessage.NOTE_ON, channel, fullNote, volume);
	            this.track.add(new MidiEvent(on, tick));

	            // Note off
	            ShortMessage off = new ShortMessage();
	            off.setMessage(ShortMessage.NOTE_OFF, channel, fullNote, volume);
	            this.track.add(new MidiEvent(off, tick + duration));

	            tick += duration;
			}
			return true;
		}
		catch (InvalidMidiDataException e) {
			return false;
		}
			
	}
}
