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
    Name: displayAllSongs()
    Explanation:
    Uses a for loop that Prints out all the song objects in the songs arrayList.
    I wanted to give the display action a greater sense of identity because, as the assignment clearly states, after every action that the user takes,
    the catalog should always be displayed. Thus, my thought process behind creating a distinction for displaying the catalog after every action
    and the user choosing to display all songs in the catalog was to give a ranking system in the display all songs catalog, which is done based upon the score of each individual song.
    Arguments: None
    Return Values: Void
     */
    public void displayAllSongs() {
        System.out.println("All Songs stored in Catalog:");
        for (Song song : songs)
            System.out.println(song);

    }//end method displayAllSongs


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
        Scanner userInput = new Scanner(System.in);
        System.out.println("Do you want to filter songs by artist or album name? (artist/album)");
        String filterChoice = userInput.nextLine().trim().toLowerCase();

        if (filterChoice.equals("artist")) {
            System.out.println("Enter artist name:");
            String artistName = userInput.nextLine().trim();
            filterByArtist(artistName);
        } else if (filterChoice.equals("album")) {
            System.out.println("Enter album name:");
            String albumName = userInput.nextLine().trim();
            filterByAlbum(albumName);
        } else {
            System.out.println("Invalid choice. Please choose 'artist' or 'album'.");
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
                System.out.println("Ain't Nobody Here But Us Chickens. (Try adding a .txt file to the catalog!)");
            } else {
                System.out.println("No songs found for the specified " + filterType + " '" + filterName + "'.");
            }
        } else {
            Collections.sort(songs, (s1, s2) -> Float.compare(s2.getSongScore(), s1.getSongScore()));

            System.out.println("Songs in the catalog:");
            int rank = 0;
            float previousScore = Float.MAX_VALUE;

            for (Song song : songs) {
                if (song.getSongScore() < previousScore) {
                    // Update rank if the current song has a lower score
                    rank++;
                    previousScore = song.getSongScore();
                }
                System.out.println("Rank #" + String.format("%06d", rank) + " | " + song);
            }
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

        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the attribute you want to remove the song by (ID or Title): ");
        String attribute = scanner.nextLine().trim().toLowerCase();

        if (attribute.equals("id")) {
            System.out.print("Enter the ID of the song you want to remove: ");
            if (scanner.hasNextInt()) {
                int songId = scanner.nextInt();
                scanner.nextLine();

                Iterator<Song> iterator = songs.iterator();
                boolean found = false;

                while (iterator.hasNext()) {
                    Song song = iterator.next();
                    if (song.getIdentification() == songId) {
                        iterator.remove();
                        found = true;
                        usedIds.remove(songId);
                        System.out.println("Song " + songId + " has been removed.");
                        break;
                    }
                }

                if (!found) {
                    System.out.println("Song with ID " + songId + " is not found in the catalog.");
                }
            } else {
                System.out.println("Invalid input! Enter a valid integer ID.");
            }
        } else if (attribute.equals("title")) {
            System.out.print("Enter the title of the song you want to remove: ");
            String title = scanner.nextLine().trim();

            boolean found = false;
            int count = 0;

            for (Song song : songs) {
                if (song.getTitle().equalsIgnoreCase(title)) {
                    found = true;
                    count++;
                    System.out.println("The following song(s) in the catalog matches your criteria:");
                    System.out.println(count + ". ID: " + song.getIdentification() + ", Title: " + song.getTitle() + ", Artist: " + song.getArtist());
                }
            }

            if (found) {
                if (count > 1) {
                    System.out.print("Enter the ID of the song you want to remove: ");
                    if (scanner.hasNextInt()) {
                        int songId = scanner.nextInt();
                        scanner.nextLine();

                        Iterator<Song> iterator = songs.iterator();
                        boolean removed = false;

                        while (iterator.hasNext()) {
                            Song song = iterator.next();
                            if (song.getIdentification() == songId && song.getTitle().equalsIgnoreCase(title)) {
                                iterator.remove();
                                usedIds.remove(songId);
                                System.out.println("Song " + songId + " has been removed.");
                                removed = true;
                                break;
                            }
                        }

                        if (!removed) {
                            System.out.println("Song with ID " + songId + " and title " + title + " is not found in the catalog.");
                        }
                    } else {
                        System.out.println("Invalid input! Enter a valid integer for ID!");
                    }
                } else {
                    Iterator<Song> iterator = songs.iterator();
                    while (iterator.hasNext()) {
                        Song song = iterator.next();
                        if (song.getTitle().equalsIgnoreCase(title)) {
                            iterator.remove();
                            usedIds.remove(song.getIdentification());
                            System.out.println("Song '" + title + "' has been removed.");
                            break;
                        }
                    }
                }
            } else {
                System.out.println("Song with title '" + title + "' is not found in the catalog.");
            }
        } else {
            System.out.println("Invalid selection. Please enter 'ID' or 'Title'.");
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
        Scanner myScanner = new Scanner(System.in);

        while (iterator.hasNext()) {
            Song song = iterator.next();
            if (song.getIdentification() == songId) {
                found = true;
                System.out.println("Enter the new score for song " + songId + " (between 0.00 and 5.00):");
                float newScore;
                do {
                    try {
                        newScore = Float.parseFloat(myScanner.nextLine());
                        Pattern validScore = Pattern.compile("\\d(\\.\\d{1,2})?");

                        if (newScore < 0.00 || newScore > 5.00 || !validScore.matcher(String.valueOf(newScore)).matches()) {
                            System.out.println("Invalid score! Enter a value between 0.00 and 5.00:");
                        } else {
                            song.setSongScore(newScore);
                            System.out.println("Song " + songId + " has been updated with a new score of: " + newScore);
                            break;
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid format! Enter a valid float value:");
                    }
                } while (true);
            }
        }

        if (!found) {
            System.out.println("Song with ID " + songId + " is not found in the catalog.");
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
        System.out.println("Song added successfully with ID " + newID + ".");
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
