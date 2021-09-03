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

        for ( int i = 0; i < notes.length; i++ )
        {
            notes[i].setDuration( notes[i].getDuration() * ratio );
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

    /**
     * Lower the octave by one. For example, a C note in octave 4 would become a C note in octave 3.
     * If any of the notes are at octave 1, then the entire method should do nothing.
     *
     * @return true if method lowered the octave. False if otherwise.
     */
    public boolean octaveDown() {

        //If there are no octaves that are 1, then lower the octave.
        if ( this.containsSpecialOctave() )
            {
                return false;
            }

        for ( int i = 0; i < notes.length; i++ )
            {
                if ( notes[i].getPitch() != Pitch.R )
                    {
                        notes[i].setOctave( notes[i].getOctave() - 1 );
                    }
            }

        return true;
    }

    public boolean octaveUp() {

        if ( this.containsSpecialOctave() )
            {
                return false;
            }

        for ( int i = 0; i < notes.length; i++ )
            {
                if ( notes[i].getPitch() != Pitch.R )
                    {
                        notes[i].setOctave( notes[i].getOctave() + 1 );
                    }
            }
        return true;
    }

    /**
     * Check the melody notes for any octaves that are special cases (1 or 10)
     *
     * @return true if melody has at least one note with a special case octave
     */
    private boolean containsSpecialOctave()
    {
        /* TODO: Maybe delete this method and put it in the other octave methods?
         * Error occurs when increasing octave to maximum limit of 10 but then this method outputs
         * false when trying to decrease from an octave of 10.
         */
        int number_of_special_octaves = 0;

        for ( int i = 0; i < notes.length; i++ )
            {
                if ( notes[i].getOctave() == 1 || notes[i].getOctave() == 10 )
                    {
                        number_of_special_octaves++;
                    }
            }

        if ( number_of_special_octaves > 1 )
            {
                return true;
            }

        return false;
    }

    /**
     * Play the melody
     */
    public void play() {

        int note_i = 0; // Index of the current note.
        boolean toRepeat = false;       // Is the current section to be repeated?
        boolean secondPass = false;     // Is it the second pass of a repeated section?
        Note currNote;

        int repeatSectionStart = 0;  // Starting index of the repeated section.
        int repeatSectionEnd = 0;

        // Play the notes as long as there are notes left to play
        while ( note_i < notes.length ){
            currNote = notes[note_i];
            currNote.play();

            /* If the current note is a repeat note, need to consider special cases:
             *
             * 1. If the note is a repeated note and it is the second pass of the section:
             *          > If the index is at the ending note of the section => second pass = false
             *          > Increment the note index.
             *
             * 2. If the note is a repeated note and it is the first pass of the section (ie NOT a second pass then need to consider the following):
             *
             *          > If the melody is currently  not repeating, then it is the start of the section => record the index.
             *          > If the current section has to repeated, then it is the end of the section => record the ending index.
             *                  - Return to the start of the section.
             *                  - Currently the secondPass of the melody section.
             */
            if ( currNote.isRepeat() && !secondPass){

                if ( toRepeat ) {
                    repeatSectionEnd = note_i;
                    note_i = repeatSectionStart;
                    secondPass = true;
                }
                else{
                    repeatSectionStart = note_i;
                    note_i++;
                }

                toRepeat = !toRepeat;
            }
            else{

                if ( note_i == repeatSectionEnd ){
                    secondPass = false;
                }

                note_i++;
            }
        }
    }

    /**
     * Plays the melody in reverse
     */
    public void reverse() {

        Note tmp_note;

        for ( int i = 0; i < ((notes.length + 1) / 2); i++ )
        {
            // Get the corresponding element from the end of the array and store in a temporary variable
            tmp_note = notes[notes.length - 1 - i];

            // Assign the current index of notes to the corresponding element at the end.
            notes[notes.length - 1 - i] = notes[i];

            notes[i] = tmp_note;
        }

        this.play();
    }

    /**
     * Return a string representation of the melody
     *
     * @return  a String of the melody
     */
    public String toString() {
        String melodyString = "\n================================================================================\n"; 
        melodyString += "TITLE: " + this.getTitle() + "\nARTIST: " + this.getArtist() + "\n# Notes: " + numNotes + "\n\n";

        for ( int i = 0; i < notes.length; i++ ){
            melodyString += notes[i].toString();
            melodyString += "\n";
        }

        melodyString += "================================================================================\n"; 

        return melodyString;
    }
}
