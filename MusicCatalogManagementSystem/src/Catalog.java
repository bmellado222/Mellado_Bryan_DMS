/********************************************
 * Name: 	  Bryan Mellado	  	         	*
 * Course: 	  CEN 3024C	     	         	*
 * Purpose:	  MCMS       			     	*
 * Date:	  6 / 8 /2024			     	*
 ********************************************
 * Class Function:
 * This class houses every function when accounting for multiple songs, it accounts for duplicate IDs.
 * Removing songs based on either ID or title,
 * Updating song scores that are housed in the catalog using ID,
 * Displaying songs in descending order based on each individual song score,
 * there is also an additional action in that you can filter your catalog by either author or album name,
 * and of course Adding Songs.
 * Additionally, there are many methods for display which were all created for a specific purpose behind them.
 */
import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.regex.Pattern;

class Catalog {
    //Fields
    private ArrayList<Song> songs;
    private Set<Integer> usedIds;


    /*
    Name: Catalog()
    Explanation:
    Constructor initializes songs and usedIds
    Arguments: None
    Return Values: Not even Void
     */
    public Catalog() {
        songs = new ArrayList<>();
        usedIds = new HashSet<>();
    }

    public ArrayList<Song> getSongs() {
        return songs;
    }
    public Set<Integer> getUsedIds() {
        return usedIds;
    }


    /*
    Name: filterByArtist
    Explanation:
    Scanner created, program prompts user for how they would like songs to be displayed,
    intakes user input via filterChoice which is trimmed to remove spaces, and set to lowercase to account for case-sensitivity.
    if its 'artist', the program prompts further for artist name then calls and passes it on to the filterByArist method.
    if its 'album', the program prompts further for album name then calls and passes it on to the filterByAlbum method.
    if its 'all', the program calls displayUnfiltered method.
    otherwise, the user didn't input any of these choices and will be returned to menu.
    Arguments: String artistName
    Return Values: void
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


    /*
    Name: filterByArtist
    Explanation:
    Passed a string called artistName which the user inputted from the askForDisplay method.
    Initializes an arrayList, filteredSongs which the runs a for loop looking at every song's album name in lowercase matching the user's input
    adding every song it finds to the filteredSongs array and then calling the displayRankings method and passing its,
    filtered songs from the arrayList, its filter type, and the name of the input that the user inputted during askForDisplay.
    Arguments: String artistName
    Return Values: void
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


    /*
    Name: filterByAlbum
    Explanation:
    Passed a string called albumName which the user inputted from the askForDisplay method.
    Initializes an arrayList, filteredSongs which the runs a for loop looking at every song's album name in lowercase matching the user's input
    adding every song it finds to the filteredSongs array and then calling the displayRankings method and passing its,
    filtered songs from the arrayList, its filter type, and the name of the input that the user inputted during askForDisplay.
    Arguments: String albumName
    Return Values: void
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


    /*
    Name: displayUnfiltered
    Explanation:
    Immediately moves on by calling the displayRankings giving it the songs arrayList, the filter type all, and no filter name.
    This one probably could've just been in the askForDisplay method but making a method for isn't bad either. Ultimately, harmless.
    Arguments: none
    Return Values: void
     */
    public void displayUnfiltered() {
        displayRankings(songs, "all", "");
    }//end method displayUnfiltered


    /*
    Name: displayRankings
    Explanation:
    First, it checks if the songs arrayList is empty, one retrospective thought, checking filtering by type all isn't really required because
    it's already checked at the menu but, so be it, a bit of bloat but ultimately harmless.
    Anyway if the songs arrayList isn't empty, then it will sort the songs by songScore in descending order.
    Then it keep try of int rank and float previous score for two reasons, one to display rank but, also to display rank properly
    as some songs may share the same rank, also previous score is set to max just so that there is no way that the user could possibly input a larger float score.
    It will also only update rank if the current song's score is lower.
    Also, rank formatted as 000000 just so that the UI looks alright.
    Arguments: ArrayList<Song> songs, String filterType, String filterName
    Return Values: void
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


    /*
    Name: removeSong
    Explanation:
    We have a collection of songs, an iterator is needed to retrieve the next song in the catalog to
    check if the passed user input variable songId matches any of the IDs of a song object in the arrayList of songs.
    Creates the 'found' variable, 'found' of the boolean data type to keep track of whether the users inputted id was actually able to locate the song.
    If it isn't successful, the user will be told so, if the iterator successfully matches the songId with a Books Identification
    it is removed, the 'found' variable is changed to true, as well as the deleted song's id is removed from the hashset of usedIds.

    On top of all that, before any of this actually runs, it will start by first asking the user which attribute they would like to use in order to remove a song from the catalog.
    If the user inputs something valid (id or title), then one of two things will happen from here:
    Either it will do what I have already stated about the song's ID or it will check through every song object, looking at every song title in lowercase.
    If it finds nothing, then that's it; if it finds something, 'found' is switched to true, then increase a count, which is just used to count how many songs contain that title.
    The program will then display all songs with that title if there are multiple of them. If found is switched to true from here, it will check the count;
    if count is greater than 1, the program will ask the user to further specify the song's ID, which follows pretty much exactly the same way that the removal of the song ID worked.
    Otherwise, if the count is just one, then the program will immediately remove that song from the catalog,
    following the exact same steps as song ID removal but instead display the title that was removed rather than ID.


    Arguments: none
    Return Values: Void
     */
    public void removeSong() {

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
                                    usedIds.remove(songId);
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
                                                usedIds.remove(songId);
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
                                        usedIds.remove(song.getIdentification());
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


    /*
    Name: updateUserScore
    Explanation:
    creates a boolean called 'found' set to false, initializes a scanner, iterates through songs looking for user input songID
    if it finds it 'found' is switched to true, and the program asks the user for what the song's new score should be,
    creating a new float called newScore which is then validated to make sure it's a float variable and that the range is between 0-5.
    All goes well, it will tell the user the score has been updated and the menu & catalog will come back showing the update.
    If it can't find the songId in songs then, error and back to the menu.
    Arguments: int songId
    Return Values: void

     */
    public void updateUserScore(int songId) {
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


    /*
    Name: addSong
    Explanation:
    Sets the id variable for a song object.
    Adds that book object to the songs arrayList.
    Adds id variable to a hashset that keeps track of every new ID and tells the user that the implementation of this song was successful and includes the ID of that song.
    Arguments: Object song and wrapper class Integer
    Return Values: Void
     */
    public void addSong(Song song, Integer newID) {
        song.setIdentification(newID);
        songs.add(song);
        usedIds.add(newID);
    }//end method addSong


    /*
    Name: checkNewId
    Explanation:
    Checks the hashset usedIds for any duplicate IDs
    Arguments: int
    Return Values: boolean
     */
    public boolean checkNewId(int id) {
        return !usedIds.contains(id);
    }



}//end class Catalog
