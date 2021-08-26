/*
 *	TEST CLASS FOR THE MELODY.JAVA PROGRAM
 *
 */
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.File;

public class TestMelody{
    
    File testFile = new File("../res/melodies/hotcrossbuns.txt");
    Melody testMelody = new Melody(testFile);

    @Test
    public void testGetTotalDuration(){
        assertEquals(8, testMelody.getTotalDuration(), 0);
    }

    @Test
    public void testGetArtist(){
        assertEquals("unknown", testMelody.getArtist());
    }

    @Test
    public void testGetTitle(){
        assertEquals("Hot Cross Buns", testMelody.getTitle());
    }
}
