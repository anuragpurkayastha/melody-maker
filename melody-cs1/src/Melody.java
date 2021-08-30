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
     * Also take a copy of the _notes_ array for processing.
     */
    private Note[] notes, copyOfNotes;
    private String artist;
    private String title;
    private int numNotes;
    private Note[] allNotes;
    
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
            copyOfNotes = new Note[numNotes];

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
                    newNoteCopy = new Note(duration, repeat);
                    notes[i] = newNote;
                    copyOfNotes[i] = newNoteCopy;
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
                    newNoteCopy = new Note(duration, pit, octave, acc, repeat);
                    notes[i] = newNote;
                    copyOfNotes[i] = newNoteCopy;
                }
            }
            
            for ( int i = 0; i < notes.length; i++ ){
                System.out.println(notes[i].toString());
            }

            allNotes = this.getAllNotes();
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

        System.out.println("Changing the tempo\n");

        System.out.println("Notes before tempo change:");

        for ( int i = 0; i < allNotes.length; i++){
            System.out.println(allNotes[i].toString() + " (Duration: " + allNotes[i].getDuration() + ")");
        }
        
        System.out.println();

        for ( int i = 0; i < allNotes.length; i++ ){
            System.out.print(allNotes[i].toString() + " (Duration: " + allNotes[i].getDuration() + ")\t=>\t");
            allNotes[i].setDuration(allNotes[i].getDuration() * ratio);
            System.out.println( allNotes[i].toString() + "\n");
        }

        System.out.println("\nNotes after tempo change:");
        for ( int i = 0; i < allNotes.length; i++){
            System.out.println(allNotes[i].toString());
        }
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
        

        for ( int i = 0; i < allNotes.length; i++ ){
            totalDuration += allNotes[i].getDuration();
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
        for ( int i = 0; i < allNotes.length; i++ ){
            allNotes[i].play();
        }
    }

    /**
     * Plays the melody in reverse
     */
    public void reverse() {
        for ( int i = allNotes.length - 1; i >= 0; i-- ){
           allNotes[i].play();
        } 
    }

    public String toString() {
        
        // TODO: Fix bug where the 'repeat' field is being altered by the getAllNotes() method.
        String melodyString = "\nTITLE: " + this.getTitle() + "\nARTIST: " + this.getArtist() + "\n# Notes: " + numNotes + "\n\n";
        
        for ( int i = 0; i < notes.length; i++ ){
            melodyString += notes[i].toString();
            melodyString += "\n";
        }
        return melodyString;
    }

    /**
     * Return an array containing every single note that is to be played.
     * This can be used by other methods.
     *
     * @return  a Note array containing all of the Note objects.
     */
    private Note[] getAllNotes(){

        ArrayList<Note> noteList = new ArrayList<Note>();
        
        // Loop through each note contained in the _notes_ array
        int note_i = 0;  // Index of the current note.
        Note currNote;  // Current note.
        int rep_sec_start = 0;  // If the note is part of a repeated section => index of the starting note

        // If the note is part of a repeated section, is this note the end of the repeated section? False if no, True if yes.
        boolean isEndOfRepeat = true;

        while ( note_i < copyOfNotes.length ){
            currNote = copyOfNotes[note_i];

            // Add the current note to the array list
            noteList.add(currNote);
            
            // If the current note is a repeat, toggle the flag if the note is the end of the repeat
            if ( currNote.isRepeat() ){
                isEndOfRepeat = !isEndOfRepeat; 

                if ( !isEndOfRepeat ){
                    rep_sec_start = note_i;
                }
            }

            // If the current note is FALSE (ie. the start of the repeated section), then record the index of the current note as the _START_ of the section. Otherwise, record it as the _END_.
            if ( isEndOfRepeat && currNote.isRepeat() ){
                note_i = rep_sec_start;
            }
            else{
                note_i++;
            }

            currNote.setRepeat(false);
        }

        Note[] noteListArray = new Note[noteList.size()];
        noteListArray = noteList.toArray(noteListArray);
        

        return noteListArray;
    }
}
