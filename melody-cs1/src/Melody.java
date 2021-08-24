import java.util.*;
import java.io.*;
import melody.audio.*;

public class Melody {

    private Note[] notes;
    private String artist;
    private String title;
    private int numNotes;

    public Melody(File file) {
        /*
          Create a Melody object.

          Arguments: file - Text file with the representation of the melody.
         */
        try{
            Scanner fileReader = new Scanner(file);

            // Read the song title
            title = fileReader.nextLine();

            // Read the artist name
            artist = fileReader.nextLine();

            // Read the number of notes
            numNotes = Integer.parseInt(fileReader.nextLine());


            // There are _numNotes_ notes in the file.
            // Loop word by word and parse each note in the file.
            double duration;
            String pitch;
            int octave;
            String accidental;
            boolean repeat;
            Note newNote;
            notes = new Note[numNotes];

            for ( int i = 0; i < numNotes; i++ ){

                // Get the duration
                duration = Double.parseDouble(fileReader.next());

                // Get the pitch of the note
                pitch = fileReader.next();
                Pitch pit = Pitch.getValueOf(pitch);

                // If the pitch is "R" then the next term is the 'repeat'.
                // Create a new Note object and add it to the array of notes.
                if ( pitch.equals("R") ){
                    // Get the repeat
                    repeat = Boolean.parseBoolean(fileReader.next());

                    newNote = new Note(duration, repeat);

                    notes[i] = newNote;
                }
                else {
                    // Get the octave
                    octave = Integer.parseInt(fileReader.next());

                    // Get the accidental
                    accidental = fileReader.next();
                    Accidental acc = Accidental.getValueOf(accidental);

                    // Get the repeat
                    repeat = Boolean.parseBoolean(fileReader.next());

                    newNote = new Note(duration, pit, octave, acc, repeat);
                    notes[i] = newNote;
                }
            }
        }
        catch(FileNotFoundException err){
            System.out.println("Error! File not found");
        }
    }

    public void changeTempo(double ratio) {
        // TODO: write this method

    }

    public String getArtist() {
        // TODO: write this method
        return artist;
    }

	public String getTitle() {
        // TODO: write this method
        return title;
    }

    public double getTotalDuration() {
    /*
    * Get the total duration of the song
    *
    * Loop through each note in the _notes_ array and sum the durations of each note.
    */

    double totalDuration = 0.0;
    Note currNote = null;

    // Is the current note within the section that will be repeated?
    boolean inRepeatedSection = false;

    // Loop through the notes of the melody
    for ( int i = 0; i < notes.length; i++ ){
        currNote = notes[i];
        //System.out.println("Current note: " + currNote.toString());
        /*
          Within a repeated section, each note is played twice.
          If the current note is within a repeated section, then add twice the duration to the totalDuration.
        */

        // Check if the current note is the start of a repeated section. If it is, then toggle the inRepeatedSection flag (false -> true). If the current note is another repeated, toggle the flag to false (not in repeated section).
        if ( currNote.isRepeat() ) {

            if ( !inRepeatedSection ){
                inRepeatedSection = true;
            }
            else if ( inRepeatedSection ) {
                //System.out.println("Adding " + 2 * currNote.getDuration() + "s");
                totalDuration += 2 * currNote.getDuration();
                inRepeatedSection = false;
            }
        }

        if ( inRepeatedSection ){
            //System.out.println("Adding " + 2 * currNote.getDuration() + "s");
            totalDuration += 2 * currNote.getDuration();
        }
        else if ( !currNote.isRepeat() ){
            //System.out.println("Adding " + currNote.getDuration() + "s");
            totalDuration += currNote.getDuration();
        }
    }
    return totalDuration;
    }

    public boolean octaveDown() {
            // TODO: write this method
            return false;
    }

    public boolean octaveUp() {
            // TODO: write this method
            return false;
    }

    public void play() {
  /*
   * Play the melody.
   * Loop through the array in _notes_ array and play each note.
   */

  for ( int i = 0; i < notes.length; i++ ){
      notes[i].play();
  }
    }

    public void reverse() {
            // TODO: write this method
    }

    public String toString() {
            // TODO: write this method
            return "";
    }
}

