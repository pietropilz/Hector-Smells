/*
 * Para compilar e salvar no pacote music:
 * javac Note.java
 * javac -d . Note.java
 *
 * Para importar:
 * import music.*
 */

package music;

public class Note extends Sound {
	private int instrument;
	private int octave;
	private int volume;
	private int semitone;
	private int duration;

	public Note(int instrument, int octave, int volume, int semitone, int duration) {
		super(instrument, octave, volume);
		this.semitone = semitone;
		this.duration = duration;
	}

	public void setSemitone(int semitone) {
		this.semitone = semitone;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public int getSemitone() {
		return semitone;
	}

	public int getDuration() {
		return duration;
	}

	public int OctaveSemitone() {
		return 12 * (this.getOctave() + 1) + this.getSemitone();
	}
}