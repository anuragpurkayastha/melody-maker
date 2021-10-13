# Melody Maker

## Description
This program is a Java implementation of a graphical music player.

Through the interface, text files with melody information are able to be loaded to be played.

I found this program online at [Nifty Assignments](http://nifty.stanford.edu/2015/obourn-stepp-melody-maker/), which is a website that hosts university standard assignments from a number of universities around the world.

The only file that I typed code in is the `Melody.java` file. The rest of the code was provided as part of the assignment.

## Compilation
Compile the program using the following command
```
javac ./melody-cs1/src/*.java
```
## Run
Run the program by running the following command:

```
java ./melody-cs1/src/Main.java

```
## Usage
A screenshot of the interface is provided in the image below.

![Graphical interface](/melody-cs1/assets/img/Melody_player_gui.png)

To load a file, click on the **Load** button. This will open up a file chooser. Select the desired melody file and click **open**.

The playback buttons are described below:

**Load**: Load a text file containing the melody. Sample text files containing melodies can be found in the directory: `melody-cs1/src/sample-songs`.

**Play**: Play the melody.

**Reverse**: Play the melody in reverse.

**Change tempo**: Change the tempo to the value specified in the text box. For example, a tempo of 2.0 increases the duration of each note by a factor of 2.0. Similarly, a tempo of 0.5 changes the duration by half.

**Up**: Increases the octave of each note by 1. (Note: if there are any notes with an octave of 10, this button will do nothing.)

**Down**: Decreases the octave of each note by 1. (Note: if there are any notes with an octave of 1, this button will do nothing.)
