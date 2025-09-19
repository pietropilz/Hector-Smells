/*
 * Para compilar e salvar no pacote music:
 * javac Sound.java
 * javac -d . Sound.java
 *
 * Para importar:
 * import music.*
 */

package music;

public class Sound {
	private int instrument;
	private int octave;
	private int volume;

	public Sound(int instrument, int octave, int volume) {
		this.instrument = instrument;
		this.octave = octave;
		this.volume = volume;
	}

	public void setInstrument(int instrument) {
		this.instrument = instrument;
	}

	public void setOctave(int octave) {
		this.octave = octave;
	}

	public void setVolume(int volume) {
		this.volume = volume;
	}

	public int getInstrument() {
		return instrument;
	}

	public int getOctave() {
		return octave;
	}

	public int getVolume() {
		return volume;
	}
}
