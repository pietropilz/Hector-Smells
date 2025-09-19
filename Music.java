/*
 * Para compilar e salvar no pacote music:
 * javac -d . music\Music.java
 *
 * Para importar:
 * import music.*
 */

package music;

public class Music {
	private Note[] allNotes;

	public Music(Note[] allNotes) {
		this.allNotes = allNotes;
	}

	public void setNote(Note note, int position) {
		this.allNotes[position] = note;
	}

	public void setAllNotes(Note[] allNotes) {
		this.allNotes = allNotes;
	}

	public Note getNote(int position) {
		return allNotes[position];
	}

	public Note[] getAllNotes() {
		return allNotes;
	}
}
