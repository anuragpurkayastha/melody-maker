import java.util.*;
import java.io.*;
import melody.audio.*;

/**
 * Melody is a class that represents a sequence of Note objects.
 *
 * @author      Anurag Purkayastha
 * @version     0.1
 */
public class Melody {
    
    /* Notes contained in the file.
     */
    private Note[] notes;
    private String artist;
    private String title;
    private int numNotes;
    
    /**
     * Melody constructor.
     *
     * @param   file    the file object containing the data of the melody.
     */
    public Melody(File file) {
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
            Note newNote, newNoteCopy;
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
            
            for ( int i = 0; i < notes.length; i++ ){
                System.out.println(notes[i].toString());
            }
        }
        catch(FileNotFoundException err){
            System.out.println("Error! File not found");
        }
    }
    
    /**
     * Increase the duration of each note.
     *
     * @param   ratio   the ratio by which to increase the duration
     */
    public void changeTempo(double ratio) {
        // TODO: Fix scaling of duration. It is off by a factor of 4.
    }

    /**
     * Return the artist of the melody.
     *
     * @return  the artist of the melody
     */
    public String getArtist() {
        return artist;
    }

    /**
     * Return the title of the melody.
     *
     * @return  the title of the melody
     */
    public String getTitle() {
        return title;
    }

    /**
     * Get the total duration of the song.
     *
     * @return   the total duration
     */ 
    public double getTotalDuration() {

        double totalDuration = 0.0;
        
        boolean isRepeating = false;

        Note currNote;

        // Loop through each of the notes in the _notes_ array
        for ( int i = 0; i < notes.length; i++ ){
            currNote = notes[i];
            totalDuration += currNote.getDuration();

            if ( currNote.isRepeat() ){

                if ( isRepeating ){
                    totalDuration += currNote.getDuration();
                }

                isRepeating = !isRepeating;
            }

            if ( isRepeating ){
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

    /**
     * Play the melody
     */
    public void play() {

        int note_i = 0; // Index of the current note.
        boolean isRepeating = false;  // Is the current section being repeated?
        Note currNote;

        int repeatSectionStart = 0;  // Starting index of the repeated section.
        int repeatSectionEnd = 0;   // Ending index of repeated section.

        while ( note_i < notes.length ){
            currNote = notes[note_i];

            currNote.play();

            if ( currNote.isRepeat() ){

                if ( isRepeating ) {
                    repeatSectionEnd = note_i;
                    note_i = repeatSectionStart;
                    continue;
                }
                else{
                    repeatSectionStart = note_i;
                }

                isRepeating = !isRepeating;
            }
            note_i++;
        }
    }

    /**
     * Plays the melody in reverse
     */
    public void reverse() {
        for ( int i = notes.length - 1; i >= 0; i-- ){
           notes[i].play();
        } 
    }

    /**
     * Return a string representation of the melody
     *
     * @return  a String of the melody
     */
    public String toString() {
        
        String melodyString = "\nTITLE: " + this.getTitle() + "\nARTIST: " + this.getArtist() + "\n# Notes: " + numNotes + "\n\n";
        
        for ( int i = 0; i < notes.length; i++ ){
            melodyString += notes[i].toString();
            melodyString += "\n";
        }
        return melodyString;
    }
}
