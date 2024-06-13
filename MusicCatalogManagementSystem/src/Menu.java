/********************************************
 * Name: 	  Bryan Mellado	  	         	*
 * Course: 	  CEN 3024C	     	         	*
 * Purpose:	  MCMS       			     	*
 * Date:	  6 / 8 /2024			     	*
 ********************************************
 * Class Function:
 * Heart of the program, it creates a menu for the user, where the user may interact with the program to do this programs intended functions.
 * This class also houses the method for actually extracting the .txt file lines imported by the user.
 *
 */
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;


public class Menu {
    //Fields
    private Scanner input = new Scanner(System.in);
    private ArrayList<Song> songs;
    private Catalog catalog;


    /*
    Name: Menu
    Explanation:
    Constructor initializes songs arrayList and catalog, it then runs the method for adding books, then finally boots to menu.
    It also asks the user in a 'witty' way to please input a txt file to view a catalog.
    Arguments: None
    Return Values: Not even Void
     */
    public Menu() {
        songs = new ArrayList<>();
        catalog = new Catalog();
        System.out.println("Ain't Nobody Here But Us Chickens (Try adding a .txt file to the catalog!) ");
        createMenu();
    }//no arg Constructor


    /*
    Name: createMenu
    Explanation:
    This method first creates the variables myOptions of data type int
    from there, the user is presented with a menu that is fully functional and allows the user to
    select from options 1-5 to do any of the 4 listed actions.
    This menu will always run after every option until the menu is closed and accounts for bad inputs.
    Arguments: None
    Return Values: Void

     */
    public void createMenu() {
        int myOptions = 0;
        do {
            System.out.println(
                    "[1] Add New Songs to Catalog (from file)\n" +
                            "[2] Remove a Song\n" +
                            "[3] Display all Songs\n" +
                            "[4] Update a Song's Score\n" +
                            "[5] Exit\n" +
                            "Enter your selection:"
            );


            try {
                myOptions = input.nextInt();
                switch (myOptions) {
                    case 1: // Add Song to Existing Catalog
                        addSongsFromFile();
                        catalog.displayAllSongs();
                        break;
                    case 2: // Remove Song from Existing Catalog
                        catalog.removeSong();
                        catalog.displayAllSongs();
                        break;
                    case 3: // Display Catalog
                        if (songs.isEmpty()) {
                            System.out.println("Ain't Nobody Here But Us Chickens (Try adding a .txt file to the catalog!) ");
                            System.out.println();
                        } else
                            catalog.askForDisplay();
                        break;
                    case 4: // Remove Song from Existing Catalog
                        System.out.print("Enter the ID of the song's score you would like to update: ");
                        int songToUpdate = input.nextInt();
                        catalog.updateUserScore(songToUpdate);
                        catalog.displayAllSongs();
                        break;
                    case 5:
                        System.out.println("Exiting Program...");
                        System.exit(0);
                        break;
                    default:
                        System.out.println("Invalid option! Let's Try Again.");
                        break;
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input! Please enter a number.");
                input.nextLine();
            }

        } while (myOptions != 5);

    }//end method createMenu


    /*
    Name: addBooksFromFile()
    Explanation:
    This method starts by asking the user for the .txt file path they would like to import
    the program then proceeds to use BufferedReader and beings trying to extract
    each line of the text file and stops when a line contains no information.
    Trims whitespaces from each line
    While the lines are being read they are put in String data type line and broken down into
    pieces starting by first breaking down the line into six pieces which are in order:
    ID,TITLE,ALBUM,ARTIST,GENRE,SCORE | If there isn't exactly six commas an error will occur and the line is skipped.
    If any part of the line has ¦ the program will skip it.
    The id is then turned into an integer checking if the id already exists in the catalog.
    If the id is below 0, error, if the id is a number larger than 10, error, if it is already in the catalog error, if it can't be turned into an int, that's an error.
    Then it tries to create the song's score of type float, if it isn't 0.00-5.00, that's an error, if isn't a float value that's an error.
    Then checks the every string respective field and make sure that they are all less than a given maximum for instance song title is 75 and artist is 50. Otherwise, error.
    After all that, the object song is finally created, all attributes are added to that new song, artist is also created and added to that song.
    Finally, the program will say that the text file has been read and everything has been processed, valid songs have been added, invalid songs have been skipped.

    Edge Case:
    This code would likely behave unintended when ¦ is found inside of artist, title or album names.
    This could likely lead to my code breaking so, just to be safe, if this symbol is found anywhere during line reading, it will not be read any further.
    So this is all just to say that I guess this was an unforeseen limitation, come to light after thinking about potential problems that my code may face
    because of a rare symbol.

    I made it so that commas that are first encompassed in double quote can be properly read by the program, see in real life some songs have,
    commas in their title, album or artist name thus, comma de-limited formatting of text files made it impossible at first to properly account these cases,
    so I made an outstanding regex.


    Arguments: None
    Return Values: Void


     */
    private void addSongsFromFile() {
        System.out.print("Enter the path of the text file: ");
        input.nextLine();
        String filePath = input.nextLine();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.contains("¦")) {
                    System.out.println("An Error has occurred! Line contains invalid character '¦'. Skipping line: " + line);
                    continue;
                }

                line = line.trim();
                if (line.isEmpty()) continue;

                // The program will split on commas, excluding those within double quotes
                String[] parts = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
                if (parts.length == 6) {
                    for (int i = 0; i < 3; i++) {
                        parts[i] = parts[i].replace('¦', ',').trim();
                    }
                    int songId;
                    try {
                        songId = Integer.parseInt(parts[0].trim());
                        if (songId < 0 || songId > 9999999999L) {
                            System.out.println("An Error has occurred! Song ID must be between 1-10 Digits and can't be negative. Skipping line: " + line);
                            continue;
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("An Error has occurred! Invalid song ID format. Skipping line: " + line);
                        continue;
                    }

                    if (catalog.checkNewId(songId)) {
                        String title = removeQuotes(parts[1].trim());
                        String album = removeQuotes(parts[2].trim());
                        String artistName = removeQuotes(parts[3].trim());
                        String genre = removeQuotes(parts[4].trim());



                        float songScore;
                        try {
                            songScore = Float.parseFloat(parts[5].trim());
                            if (songScore < 0 || songScore > 5) {
                                System.out.println("An Error has occurred! Song score must be between 0.00 and 5.00. Skipping line: " + line);
                                continue;
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("An Error has occurred! Invalid song score number format. Skipping line: " + line);
                            continue;
                        }

                        if (title.length() <= 75 && album.length() <= 75 && artistName.length() <= 50 && genre.length() <= 50) {
                            Song newSong = new Song();
                            newSong.setTitle(title);
                            newSong.setAlbum(album);
                            Artist artist = new Artist(artistName);
                            newSong.setArtist(artist);
                            newSong.setGenre(genre);
                            newSong.setSongScore(songScore);

                            songs.add(newSong);
                            catalog.addSong(newSong, songId);
                        } else {
                            System.out.println("An Error has occurred! Title, album, ID, or artist exceeds maximum character limit. Skipping line: " + line);
                        }
                    } else {
                        System.out.println("An Error has occurred! Song with ID " + songId + " already exists in the catalog.");
                    }
                } else {
                    System.out.println("An Error has occurred! Invalid line format. Skipping line: " + line);
                }
            }
            System.out.println("All lines have been processed. All valid songs have been created and added to the catalog.");
        } catch (IOException e) {
            System.out.println("An Error has occurred! Error from file: " + e.getMessage());
        }
    }//end method addSongsFromFile


    /*
    Name: removeQuotes
    Explanation: Double quotes surrounding strings are removed, if applicable, then, returned.
    Also, the program does this one time on purpose because some song title, album, or artist names may have "" in them. For instance, the album: "Heroes" by David Bowie.
    Arguments: String str
    Return Values: String
     */
    private String removeQuotes(String str) {

        if (str.startsWith("\"") && str.endsWith("\""))
            return str.substring(1, str.length() - 1);
        return str;
    }//end method removeQuotes



}//end class Menu