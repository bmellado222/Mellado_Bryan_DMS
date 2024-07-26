import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;
import java.util.regex.Pattern;


/**
 * Catalog ---
 * This class houses every function when accounting for multiple songs.
 * Adding songs from an established connected database.
 * Removing songs based on either ID or title,
 * Updating song scores that are housed in the catalog using ID,
 * Displaying songs in descending order based on each individual song score,
 * A custom action that you can use to filter your catalog by either author or album name.
 * All while presenting lasting changes to the mySQL database that houses all of these song objects when the appropriate action/function is taken.
 * -----------
 * @author Bryan Mellado A.
 * @version update-javadoc
 * @since 7 / 17 /2024
 */
public class Catalog {
    //Fields
    private ArrayList<Song> songs;


    /**
     * Constructor initializes songs arrayList
     */
    public Catalog() {
        songs = new ArrayList<>();
    }


    /**
     * Gets the list of songs in the arrayList
     *
     * @return songs ArrayList containing songs.
     */
    public ArrayList<Song> getSongs() {
        return songs;
    }


    /**
     * The user is prompted from a JOptionPane option dialog where the options can be clearly seen in the options String[].
     * The switch case is used to take account for any interactions the user may have at this point of the program including canceling via closing the window or cancel.
     */
    public void askForDisplay() {

        String[] options = {"Filter By Artist", "Filter By Album", "Cancel"};
        int select = JOptionPane.showOptionDialog(null, "How would you like to filter the catalog?", "Music Catalog Management System", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
        switch (select) {
            case 0: // Filter By Artist
                String artistName = JOptionPane.showInputDialog("Enter the name of the artist: ");
                if (artistName == null) {
                    JOptionPane.showMessageDialog(null, "Returning to Menu.");
                    break;
                }
                filterByArtist(artistName);
                break;
            case 1: //Filter By Album
                String albumName = JOptionPane.showInputDialog("Enter the name of the album: ");
                if (albumName == null) {
                    JOptionPane.showMessageDialog(null, "Returning to Menu.");
                    break;
                }
                filterByAlbum(albumName);
                break;
            case 2: //Cancel Function
                JOptionPane.showMessageDialog(null, "Returning to Menu.");
                break;
            default: //Close Window
                JOptionPane.showMessageDialog(null, "Returning to Menu.");
                break;



        }

    }//end method askForDisplay



    /**
     * Initializes an arrayList, filteredSongs which the runs a for loop looking at every song's album name in lowercase matching the user's input
     * adding every song it finds to the filteredSongs array and then calling the displayRankings method and passing its,
     * filtered songs from the arrayList, its filter type, and the name of the input that the user inputted during askForDisplay.
     *
     * @param artistName Passed a string called artistName which the user inputted from the askForDisplay method.
     */
    private void filterByArtist(String artistName) {
        ArrayList<Song> filteredSongs = new ArrayList<>();

        for (Song song : songs) {
            if (song.getArtist().getArtistName().toLowerCase().contains(artistName.toLowerCase())) {
                filteredSongs.add(song);
            }
        }

        displayRankings(filteredSongs, "artist", artistName);
    }//end method filterByArtist


    /**
     * Initializes an arrayList, filteredSongs which the runs a for loop looking at every song's album name in lowercase matching the user's input
     * adding every song it finds to the filteredSongs array and then calling the displayRankings method and passing its,
     * filtered songs from the arrayList, its filter type, and the name of the input that the user inputted during askForDisplay.
     *
     * @param albumName Passed a string called albumName which the user inputted from the askForDisplay method.
     */
    private void filterByAlbum(String albumName) {
        ArrayList<Song> filteredSongs = new ArrayList<>();

        for (Song song : songs) {
            if (song.getAlbum().toLowerCase().contains(albumName.toLowerCase())) {
                filteredSongs.add(song);
            }
        }

        displayRankings(filteredSongs, "album", albumName);
    }//end method filterByAlbum


    /**
     * Immediately moves on by calling the displayRankings giving it the songs arrayList, the filter type all, and no filter name.
     */
    public void displayUnfiltered() {
        displayRankings(songs, "all", "");
    }//end method displayUnfiltered


    /**
     * First, it checks if the songs arrayList is empty, if the songs arrayList isn't empty, then it will sort the songs by songScore in descending order.
     * Then it keep try of int rank and float previous score for two reasons, one to display rank but, also to display rank properly
     * as some songs may share the same rank, also previous score is set to max just so that there is no way that the user could possibly input a larger float score.
     * It will also only update rank if the current song's score is lower.
     * Also, rank formatted as 000000. (E.g. 000001, 000002, 000003,...)
     *
     * @param songs Passed songs arrayList which may or may not be altered based on filters.
     * @param filterType Passed filter type from either of the three options of askForDisplay.
     * @param filterName Passed user input from either artistName or albumName of askForDisplay.
     */
    private void displayRankings(ArrayList<Song> songs, String filterType, String filterName) {
        if (songs.isEmpty()) {
            if (filterType.equals("all")) {
                JOptionPane.showMessageDialog(null, "Ain't Nobody Here But Us Chickens. (Try adding a .txt file to the catalog!)", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "No songs found for the specified " + filterType + " '" + filterName + "'.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            Collections.sort(songs, (s1, s2) -> Float.compare(s2.getSongScore(), s1.getSongScore()));

            // Created StringBuilder to accumulate the contents of the Catalog and immediately starts an HTML table in order to customize the appearance of the Catalog
            StringBuilder catalogTable = new StringBuilder("<html><body><table style='width: 150%; border-collapse: collapse;'>");
            //Header Row
            catalogTable.append("<tr>");
            catalogTable.append("<th style='border: 1px solid black; padding: 10px; font-weight: 50; background-color: black; color: white; '>Rank</th>");
            catalogTable.append("<th style='border: 1px solid black; padding: 10px; font-weight: 50; background-color: black; color: white;'>ID</th>");
            catalogTable.append("<th style='border: 1px solid black; padding: 10px; font-weight: 50; background-color: black; color: white;'>Title</th>");
            catalogTable.append("<th style='border: 1px solid black; padding: 10px; font-weight: 50; background-color: black; color: white;'>Album</th>");
            catalogTable.append("<th style='border: 1px solid black; padding: 10px; font-weight: 50; background-color: black; color: white;'>Artist</th>");
            catalogTable.append("<th style='border: 1px solid black; padding: 10px; font-weight: 50; background-color: black; color: white;'>Genre</th>");
            catalogTable.append("<th style='border: 1px solid black; padding: 10px; font-weight: 50; background-color: black; color: white;'>Score</th>");
            catalogTable.append("</tr>");


            int rank = 0;
            float previousScore = Float.MAX_VALUE;

            // Limit the number of songs visible by chunks
            //This is ultimately unnecessary for the scope of the MCMS, but it's nice to have a bit of scalability in case you ever wanted to have hundreds of songs instead of just a mere 20.
            int displayAmount = 50;
            int chunkCount = (int) Math.ceil((double) songs.size() / displayAmount);

            for (int chunkIndex = 0; chunkIndex < chunkCount; chunkIndex++) {
                catalogTable.append("<tr>");
                for (int i = chunkIndex * displayAmount; i < Math.min((chunkIndex + 1) * displayAmount, songs.size()); i++) {
                    Song song = songs.get(i);
                    if (song.getSongScore() < previousScore) {
                        rank++;
                        previousScore = song.getSongScore();
                    }

                    // Since we're using a String Builder in order to display the content of the catalog, I decided that formatting it should also be here
                    // I considered writing this in my toString for Song but, I ultimately decided that because this method is intended for the user experience, that here is where it should be formatted for the user
                    // and the toString for Song would be a quick concise use of the object where needed for testing if required
                    catalogTable.append("<td style='border: 1px solid black; padding: 7px; background-color: white; color: black; text-align: left;'>" + String.format("%06d", rank) + "</td>");
                    catalogTable.append("<td style='border: 1px solid black; padding: 7px; background-color: white; color: black; text-align: center;'>" + song.getIdentification() + "</td>");
                    catalogTable.append("<td style='border: 1px solid black; padding: 7px; background-color: white; color: black; text-align: center;'>" + song.getTitle() + "</td>");
                    catalogTable.append("<td style='border: 1px solid black; padding: 7px; background-color: white; color: black; text-align: center;'>" + song.getAlbum() + "</td>");
                    catalogTable.append("<td style='border: 1px solid black; padding: 7px; background-color: white; color: black; text-align: center;'>" + song.getArtist() + "</td>");
                    catalogTable.append("<td style='border: 1px solid black; padding: 7px; background-color: white; color: black; text-align: center;'>" + song.getGenre() + "</td>");
                    catalogTable.append("<td style='border: 1px solid black; padding: 7px; background-color: white; color: black; text-align: right;'>" + String.format("%.2f", song.getSongScore()) + "</td>");
                    catalogTable.append("</tr>");
                }
            }

            catalogTable.append("</table></body></html>");

            // JLabel to display the content
            JLabel toDisplay = new JLabel(catalogTable.toString());

            // Put the content from the toDisplay into a JScrollPane scrollableContent
            JScrollPane scrollableContent = new JScrollPane(toDisplay);
            scrollableContent.setPreferredSize(new Dimension(1200, 700));

            // Display the scrollable content in a JOptionPane
            JOptionPane.showMessageDialog(null, scrollableContent, "Songs Catalog", JOptionPane.PLAIN_MESSAGE);

        }

    }//end method displayRankings


    /**
     * We have a collection of songs, an iterator is needed to retrieve the next song in the catalog to
     * check if the passed user input variable songId matches any of the IDs of a song object in the arrayList of songs.
     * 'found' of the boolean data type is used to keep track of whether the users inputted id was actually able to locate the song.
     * If it isn't successful, the user will be told so, if the iterator successfully matches the songId with a Songs Identification it is removed.
     * <p>
     * On top of all that, before any of this actually runs, it will start by first asking the user which attribute they would like to use in order to remove a song from the catalog.
     * The user chooses between ID, Title, X button and Cancel.
     * Either it will do what I have already stated about the song's ID, or it will check through every song object, looking at every song title in lowercase.
     * If it finds nothing, then it will tell the user such and return to menu.
     * if it finds something, 'found' is switched to true, then increase a count, which is just used to count how many songs contain that title.
     * The program will then display all songs with that title if there are multiple of them. If found is switched to true from here, it will check the count;
     * if count is greater than 1, the program will ask the user to further specify the song's ID, which follows pretty much exactly the same way that the removal of the song ID worked.
     * Otherwise, if the count is just one, then the program will immediately remove that song from the catalog,
     * following the exact same steps as song ID removal but instead display the title that was removed rather than ID.
     *
     * @param connection The connection to the MCMS Database to create lasting changes of removed songs.
     */
    public void removeSong(Connection connection) {

        Iterator<Song> iterator = songs.iterator();
        boolean found = false;

        String[] options = {"Remove By ID", "Remove By Title", "Cancel"};
        int select = JOptionPane.showOptionDialog(null, "How would you like to remove a song from the catalog?", "Music Catalog Management System", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

        switch (select) {
            case 0: // Remove By ID
                do {
                    String removeSong = JOptionPane.showInputDialog("Enter the ID of the song you want to remove: ");
                    if (removeSong == null) {
                        JOptionPane.showMessageDialog(null, "Returning to Menu.");
                        break;
                    } else {
                        try {
                            int songId = Integer.parseInt(removeSong);
                            while (iterator.hasNext()) {
                                Song song = iterator.next();
                                if (song.getIdentification() == songId) {
                                    iterator.remove();
                                    found = true;
                                    try {
                                        String deleteQuery = "DELETE FROM music_catalog.Songs WHERE songId = ?";
                                        PreparedStatement preassembled = connection.prepareStatement(deleteQuery);
                                        preassembled.setInt(1, songId);
                                        preassembled.executeUpdate();

                                        connection.commit();
                                    } catch (SQLException e) {
                                        // This is written here because Java would get upset otherwise but,
                                        // by the time the user validates their input to actually get here there is no need to actually have a try catch for this.
                                        e.printStackTrace();
                                    }
                                    JOptionPane.showMessageDialog(null, "Song " + songId + " has been removed.");
                                    break;
                                }
                            }
                            if (!found) {
                                JOptionPane.showMessageDialog(null, "Song with ID " + songId + " is not found in the catalog.", "Error", JOptionPane.ERROR_MESSAGE);
                            }
                            break;
                        } catch (NumberFormatException e) {
                            JOptionPane.showMessageDialog(null, "Error! Enter a valid ID.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                } while (true);
                break;
            case 1: // Remove By Title
                do {
                    String removeSong = JOptionPane.showInputDialog("Enter the Title of the song you want to remove: ");
                    if (removeSong == null) {
                        JOptionPane.showMessageDialog(null, "Returning to Menu.");
                        break;
                    } else {
                        StringBuilder foundSongs = new StringBuilder();
                        int count = 0;
                        for (Song song : songs) {
                            if (song.getTitle().equalsIgnoreCase(removeSong)) {
                                found = true;
                                count++;
                                foundSongs.append(count).append(", ID: ")
                                        .append(song.getIdentification())
                                        .append(", Title: ")
                                        .append(song.getTitle())
                                        .append(", Artist: ")
                                        .append(song.getArtist())
                                        .append("\n");
                            }
                        }

                        if (found) {
                            if (count > 1) {
                                do {
                                    String removeSongById = JOptionPane.showInputDialog("The following songs in the catalog matches your criteria:\n" + foundSongs.toString() + "Enter the ID of the song you want to remove: ");
                                    if (removeSongById == null) {
                                        JOptionPane.showMessageDialog(null, "Returning to Menu.");
                                        break;
                                    }
                                    try {
                                        int songId = Integer.parseInt(removeSongById);
                                        boolean removed = false;
                                        while (iterator.hasNext()) {
                                            Song song = iterator.next();
                                            if (song.getIdentification() == songId && song.getTitle().equalsIgnoreCase(removeSong)) {
                                                iterator.remove();
                                                try {
                                                    String deleteQuery = "DELETE FROM music_catalog.Songs WHERE songId = ?";
                                                    PreparedStatement preassembled = connection.prepareStatement(deleteQuery);
                                                    preassembled.setInt(1, songId);
                                                    preassembled.executeUpdate();

                                                    connection.commit();
                                                } catch (SQLException e) {
                                                    // This is written here because Java would get upset otherwise but,
                                                    // by the time the user validates their input to actually get here there is no need to actually have a try catch for this.
                                                    e.printStackTrace();
                                                }
                                                JOptionPane.showMessageDialog(null, "Song " + songId + " has been removed.");
                                                removed = true;
                                                break;
                                            }
                                        }
                                        if (!removed) {
                                            JOptionPane.showMessageDialog(null, "Song with ID " + songId + " and title " + removeSong + " is not found in the catalog.", "Error", JOptionPane.ERROR_MESSAGE);
                                        }
                                        break;
                                    } catch (NumberFormatException e) {
                                        JOptionPane.showMessageDialog(null, "Error! Enter a valid ID.", "Error", JOptionPane.ERROR_MESSAGE);
                                    }
                                } while (true);
                            } else {
                                while (iterator.hasNext()) {
                                    Song song = iterator.next();
                                    if (song.getTitle().equalsIgnoreCase(removeSong)) {
                                        iterator.remove();
                                        try {
                                            String deleteQuery = "DELETE FROM music_catalog.Songs WHERE songTitle = ?";
                                            PreparedStatement preassembled = connection.prepareStatement(deleteQuery);
                                            preassembled.setString(1, removeSong);
                                            preassembled.executeUpdate();

                                            connection.commit();
                                        } catch (SQLException e) {
                                            // This is written here because Java would get upset otherwise but,
                                            // by the time the user validates their input to actually get here there is no need to actually have a try catch for this.
                                            e.printStackTrace();
                                        }
                                        JOptionPane.showMessageDialog(null, "Song '" + removeSong + "' has been removed.");
                                        break;
                                    }
                                }
                            }
                            break;
                        } else {
                            JOptionPane.showMessageDialog(null, "Song with title '" + removeSong + "' is not found in the catalog.", "Error", JOptionPane.ERROR_MESSAGE);
                            break;
                        }
                    }
                } while (true);
                break;
            case 2: // Cancel Function
                JOptionPane.showMessageDialog(null, "Returning to Menu.");
                break;
            default: //Clicked X on Window
                JOptionPane.showMessageDialog(null, "Returning to Menu.");
                break;
        }
    }//end method removeSong


    /**
     * Iterates through songs looking for user input songID obtained from JOptionPane.
     * if it finds it 'found' is switched to true, and the program asks the user for what the song's new score should be,
     * creating a new float called newScore which is then validated to make sure it's a float variable and that the range is between 0-5.
     * Along with making sure that it only goes accepts scores out to the hundredths place.
     * All goes well, it will tell the user the score has been updated and these changes shall also be made to the mySQL database as well based on the connection to said database.
     * If it can't find the songId in songs then, the user will be told such via an error message and be sent back to the menu.
     *
     * @param songId     The ID of the song which will attempted to be found in the songs arrayList.
     * @param connection The connection to the MCMS Database to create lasting changes of songScore.
     */
    public void updateUserScore(int songId, Connection connection) {
        Iterator<Song> iterator = songs.iterator();
        boolean found = false;



        while (iterator.hasNext()) {
            Song song = iterator.next();
            if (song.getIdentification() == songId) {
                found = true;

                do {
                    String inputScore = JOptionPane.showInputDialog(null, "Enter the new score for song " + songId + " (between 0.00 and 5.00):");
                    if (inputScore == null) {
                        JOptionPane.showMessageDialog(null, "Returning to Menu.");
                        break;
                    }
                    try {
                        float newScore = Float.parseFloat(inputScore);
                        Pattern validScore = Pattern.compile("\\d(\\.\\d{1,2})?");

                        if (newScore < 0.00 || newScore > 5.00 || !validScore.matcher(String.valueOf(newScore)).matches()) {
                            JOptionPane.showMessageDialog(null, "Invalid score! Enter a value between 0.00 and 5.00!", "Error", JOptionPane.ERROR_MESSAGE);
                        } else {
                            song.setSongScore(newScore);
                            try {
                                String query = "UPDATE music_catalog.Songs SET songScore = ? WHERE songId = ?";
                                PreparedStatement preassembled = connection.prepareStatement(query);
                                preassembled.setFloat(1, newScore);
                                preassembled.setInt(2, songId);
                                preassembled.executeUpdate();

                                connection.commit();
                            } catch (SQLException e) {
                                e.printStackTrace();
                                // This is written here because Java would get upset otherwise but,
                                // by the time the user validates their input to actually get here there is no need to actually have a try catch for this.
                            }
                            JOptionPane.showMessageDialog(null, "Song " + songId + " has been updated with a new score of: " + newScore);
                            break;
                        }
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(null, "Invalid format! Enter a valid float value!", "Error", JOptionPane.ERROR_MESSAGE);

                    }
                } while (true);
            }
        }

        if (!found) {
            JOptionPane.showMessageDialog(null, "Song with ID " + songId + " is not found in the catalog.", "Error", JOptionPane.ERROR_MESSAGE);

        }

    }//end method updateUserScore


    /**
     * Adds song object to the songs arrayList.
     *
     * @param song The song object that will be added to the arrayList.
     */
    public void addSong(Song song) {
        songs.add(song);
    }//end method addSong


}//end class Catalog
