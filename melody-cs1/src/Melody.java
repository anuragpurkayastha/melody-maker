import java.util.*;
import java.io.*;
import melody.audio.*;

public class Melody {

    private Note[] notes;       // Notes contained in the file listed in order. Does not account for repetitions.
    private String artist;
    private String title;
    private int numNotes;
    private Note[] notesInOrder;

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

            notesInOrder = this.melodyNotesToPlay();
        }
        catch(FileNotFoundException err){
            System.out.println("Error! File not found");
        }
    }

    public void changeTempo(double ratio) {
        // TODO: write this method

    }

    public String getArtist() {
        return artist;
    }

    public String getTitle() {
        return title;
    }

    public double getTotalDuration() {
        /*
        * Get the total duration of the song
        *
        * Loop through each note in the _notes_ array and sum the durations of each note.
        */

        double totalDuration = 0.0;
        

        for ( int i = 0; i < notesInOrder.length; i++ ){
            totalDuration += notesInOrder[i].getDuration();
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
        for ( int i = 0; i < notesInOrder.length; i++ ){
            notesInOrder[i].play();
        }
    }

    public void reverse() {
        // TODO: write this method
    }

    public String toString() {
        // TODO: Fix bug where the 'repeat' field is being altered by the melodyNotesToPlay() method.
        String notesPlayedString = "\n";

        for ( int i = 0; i < notes.length; i++ ){
            notesPlayedString += notes[i].toString();
            notesPlayedString += "\n";
        }
        return notesPlayedString;
    }

    private Note[] melodyNotesToPlay(){
        /*
         * Parse the melody file to create a note array with all of the notes to play in order.
         *
         * This takes into account the repeated sections.
         */

        // Take a copy of the notes list so we don't modify the original
        Note[] copyOfNotes = notes.clone();

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
