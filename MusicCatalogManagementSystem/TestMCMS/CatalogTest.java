import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.PrintStream;
import static org.junit.jupiter.api.Assertions.*;

class CatalogTest {
    private Catalog catalog;



    @BeforeEach
    public void initialization() {
        catalog = new Catalog();
        Song song1 = new Song(1,"Tusk","Tusk","Fleetwood Mac","Pop-Rock",3.95f);
        Song song2 = new Song(2, "Pyramid Song", "Amnesiac", "Radiohead", "Indie Rock", 4.66f);
        catalog.addSong(song1, song1.getIdentification());
        catalog.addSong(song2, song2.getIdentification());
    }


    @Test
    public void addSongCorrect() {
        Song song1 = new Song(1,"Tusk","Tusk","Fleetwood Mac","Pop-Rock",3.95f);
        catalog.addSong(song1, song1.getIdentification());

        assertTrue(catalog.getSongs().contains(song1));
        assertTrue(catalog.getUsedIds().contains(song1.getIdentification()));

    }


    @org.junit.jupiter.api.Test
    public void addSongError() {
        String song1 = "7,New Tusk, New Tusk, New Fleetwood Mac, New Pop-Rock,3.95";
        String song2 = "2, Duplicate Tusk, Duplicate Tusk, Duplicate Fleetwood Mac, Duplicate Pop-Rock, 3.95";
        String[] parts1 = song1.split(",");
        String[] parts2 = song2.split(",");

        for (int i = 0; i < 6; i++) {
            parts1[i] = parts1[i].trim();
        }

        int song1Id = Integer.parseInt(parts1[0]);
        String song1Title = parts1[1];
        String song1Album = parts1[2];
        String song1Artist = parts1[3];
        String song1Genre = parts1[4];
        float song1Score = Float.parseFloat(parts1[5]);

        if (catalog.checkNewId(song1Id)) {
            Song newSong1 = new Song(song1Id, song1Title, song1Album, song1Artist, song1Genre, song1Score);
            catalog.addSong(newSong1, song1Id);
        } else{
            System.out.println("An Error has occurred! Song with ID " + song1Id + " already exists in the catalog.");
        }

        for (int i = 0; i < 6; i++) {
            parts2[i] = parts2[i].trim();
        }

        int song2Id = Integer.parseInt(parts2[0]);
        String song2Title = parts2[1];
        String song2Album = parts2[2];
        String song2Artist = parts2[3];
        String song2Genre = parts2[4];
        float song2Score = Float.parseFloat(parts2[5]);

        if (catalog.checkNewId(song2Id)) {
            Song newSong2 = new Song(song2Id, song2Title, song2Album, song2Artist, song2Genre, song2Score);
            catalog.addSong(newSong2, song2Id);
        } else {
        System.out.println("An Error has occurred! Song with ID " + song2Id + " already exists in the catalog.");
    }

        List<Song> songs = catalog.getSongs();


        assertTrue(songs.stream().anyMatch(song -> song.getTitle().equals("New Tusk")));
        assertFalse(songs.stream().anyMatch(song -> song.getTitle().equals("Duplicate Tusk")));


        Assertions.assertEquals(3, songs.size());
    }


    @org.junit.jupiter.api.Test
    void removeSongIdPositive() {

        String userInput = "ID\n1\n";
        InputStream in = new ByteArrayInputStream(userInput.getBytes());
        System.setIn(in);

        catalog.removeSong();

        List<Song> songs = catalog.getSongs();
        Assertions.assertEquals(1, songs.size());

    }


    @org.junit.jupiter.api.Test
    void removeSongIdNegative() {


        String userInput = "ID\n5\n";
        InputStream in = new ByteArrayInputStream(userInput.getBytes());
        System.setIn(in);

        catalog.removeSong();


        List<Song> songs = catalog.getSongs();
        Assertions.assertEquals(2, songs.size());

    }


    @org.junit.jupiter.api.Test
    void removeSongTitlePositive() {

        String userInput = "TITLE\nTusk\n";
        InputStream in = new ByteArrayInputStream(userInput.getBytes());
        System.setIn(in);

        catalog.removeSong();

        List<Song> songs = catalog.getSongs();
        Assertions.assertEquals(1, songs.size());
    }

    @org.junit.jupiter.api.Test
    void removeSongTitleNegative() {

        String userInput = "TITLE\nSong 15\n";
        InputStream in = new ByteArrayInputStream(userInput.getBytes());
        System.setIn(in);

        catalog.removeSong();

        List<Song> songs = catalog.getSongs();
        Assertions.assertEquals(2, songs.size());
    }

    @org.junit.jupiter.api.Test
    void updateUserScoreCorrect() {
        Song songUpdate = new Song(99, "All Caps", "Madvillany", "MF DOOM", "Hip-Hop", 2f);
        catalog.addSong(songUpdate, songUpdate.getIdentification());

        String userInput = "5\n";
        InputStream in = new ByteArrayInputStream(userInput.getBytes());
        System.setIn(in);

        catalog.updateUserScore(99);

        assertEquals(5f, songUpdate.getSongScore(), 0.001);

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        catalog.displayUnfiltered();

        String consoleOutput = outContent.toString();
        assertTrue(consoleOutput.contains("Rank #000001 | ID: 99          | Title: All Caps"), "'All caps' should be the highest rank because of Updated Score!");



        System.setIn(System.in);
        System.setOut(System.out);
    }

    @org.junit.jupiter.api.Test
    void updateUserScoreError() {
        Song songUpdate = new Song(99, "All Caps", "Madvillany", "MF DOOM", "Hip-Hop", 2f);
        catalog.addSong(songUpdate, songUpdate.getIdentification());

        String userInput = "4.5\n";
        InputStream in = new ByteArrayInputStream(userInput.getBytes());
        System.setIn(in);

        catalog.updateUserScore(57);

        assertFalse(songUpdate.getSongScore() == 4.5f, "This should not be 4.5");

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        catalog.displayUnfiltered();

        String consoleOutput = outContent.toString();
        assertTrue(consoleOutput.contains("Rank #000001 | ID: 2           | Title: Pyramid Song"), "This should not be 'All Caps' because it was not updated.");

        System.setIn(System.in);
        System.setOut(System.out);
    }


    @org.junit.jupiter.api.Test
    void filterAlbumPositive() {

        String userInput = "Album\nTusk\n";
        InputStream in = new ByteArrayInputStream(userInput.getBytes());
        System.setIn(in);

        ByteArrayOutputStream captureContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(captureContent));

        catalog.askForDisplay();


        String consoleOutput = captureContent.toString();
        assertTrue(consoleOutput.contains("Rank #000001 | ID: 1           | Title: Tusk"), "This should be found.");

        System.setIn(System.in);
        System.setOut(System.out);

    }

    @org.junit.jupiter.api.Test
    void filterAlbumNegative() {

        String userInput = "Album\nPop-Rock\n";
        InputStream in = new ByteArrayInputStream(userInput.getBytes());
        System.setIn(in);

        ByteArrayOutputStream captureContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(captureContent));

        catalog.askForDisplay();

        String consoleOutput = captureContent.toString();
        assertTrue(consoleOutput.contains("No songs found for the specified album 'Pop-Rock'."), "No song should be found.");

        System.setIn(System.in);
        System.setOut(System.out);

    }



    @org.junit.jupiter.api.Test
    void filterArtistPositive() {

        String userInput = "artist\nRadiohead\n";
        InputStream in = new ByteArrayInputStream(userInput.getBytes());
        System.setIn(in);

        ByteArrayOutputStream captureContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(captureContent));

        catalog.askForDisplay();

        String consoleOutput = captureContent.toString();
        assertTrue(consoleOutput.contains("Rank #000001 | ID: 2           | Title: Pyramid Song"), "This should be found.");

        System.setIn(System.in);
        System.setOut(System.out);
    }

    @org.junit.jupiter.api.Test
    void filterArtistNegative() {

        String userInput = "artist\nIndie Rock\n";
        InputStream in = new ByteArrayInputStream(userInput.getBytes());
        System.setIn(in);

        ByteArrayOutputStream captureContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(captureContent));

        catalog.askForDisplay();


        String consoleOutput = captureContent.toString();
        assertTrue(consoleOutput.contains("No songs found for the specified artist 'Indie Rock'."), "No song should be found.");

        System.setIn(System.in);
        System.setOut(System.out);
    }

}