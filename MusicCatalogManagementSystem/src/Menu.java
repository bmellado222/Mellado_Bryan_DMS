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
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import javax.swing.*;


public class Menu {
    //Fields
    private ArrayList<Song> songs;
    private Catalog catalog;
    private JFrame preventUserExit;
    private boolean establishedConnection = false;
    private Connection connection;





    /*
    Name: Menu
    Explanation:
    Constructor initializes songs arrayList and catalog, it then runs the method for adding books, then finally boots to menu.
    It also asks the user in a 'witty' way to please input a txt file to view a catalog.
    I also made a very quick JFrame because I hadn't realized that the user can technically just leave the program by just clicking off the JOptionPane screen
    which evades the Modality of JOptionPane and the only way to prevent that, while continuing to use JOptionPane, was to add this, less than ideal, JFrame.
    There is definitely a cleaner way to do this but, this option suffices.
    Arguments: None
    Return Values: Not even Void
     */
    public Menu() {
        /*
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String databaseUser = "root";
            String databasePassword = "FeetLeetNeet@1991";
            //String query = "select * from Songs";
            String databaseURL = "jdbc:mysql://localhost:3306/music_catalog";
            Connection connection = DriverManager.getConnection(databaseURL, databaseUser, databasePassword);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to connect to database. Exiting program.", "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
         */

        songs = new ArrayList<>();
        catalog = new Catalog();



        preventUserExit = new JFrame("Music Catalog Management System");
        preventUserExit.getContentPane().setBackground(Color.DARK_GRAY);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        preventUserExit.setSize(screenSize.width, screenSize.height);
        preventUserExit.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        preventUserExit.setLocationRelativeTo(null);
        preventUserExit.setVisible(true);


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
        int select;
        JLabel welcomeText = new JLabel("Welcome to my Music Catalog Management System! Please select an action from the menu below:");
        welcomeText.setHorizontalAlignment(SwingConstants.CENTER);

        do {
            String[] options = new String[]{
                        "Add New Songs",
                        "Remove a Song",
                        "Display all Songs",
                        "Update a Song's Score",
                        "Filter Songs",
                        "Exit"
                };

            select = JOptionPane.showOptionDialog(
                    preventUserExit,
                    welcomeText,
                    "Music Catalog Menu",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    options,
                    options[0]
            );

            switch (select) {
                case 0: // Connect to Database
                    if (establishedConnection) {
                        JOptionPane.showMessageDialog(preventUserExit, "You are already connected to a database!", "Info", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        String warningMessage = "Attention! Make sure you have done the following steps:\n" +
                                "1. Make sure you have imported the provided dump File to a MySQL Server. (typically through a MySQL Workbench or command line)\n" +
                                "2. Make sure you have access to the MySQL Server you have imported the database to. (You will need the URL, username, and password. Also make sure it's online)\n" +
                                "3. Make sure that this project still possesses the mysql-connector in its external libraries. (It should but, just in case it doesn't because of export mishaps)";

                        JOptionPane.showMessageDialog(null, warningMessage, "Warning", JOptionPane.WARNING_MESSAGE);
                        accessDatabase();
                    }
                    break;
                case 1: // Remove Song from Existing Catalog
                    if (establishedConnection) {
                        catalog.removeSong(connection);
                    } else {
                        JOptionPane.showMessageDialog(preventUserExit, "Please connect to a database first.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    break;
                case 2: // Display Catalog
                    if (establishedConnection) {
                        if (songs.isEmpty()) {
                            JOptionPane.showMessageDialog(preventUserExit, "Ain't Nobody Here But Us Chickens (Try adding a .txt file to the catalog!)", "Error", JOptionPane.ERROR_MESSAGE);
                        } else {
                            catalog.displayUnfiltered();
                        }
                    } else {
                        JOptionPane.showMessageDialog(preventUserExit, "Please connect to a database first.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    break;
                case 3: // Update a Song's Score
                    if (establishedConnection) {
                        try {
                            String songIdInput = JOptionPane.showInputDialog(preventUserExit, "Enter the ID of the song's score you would like to update:");
                            if (songIdInput != null) {
                                int songToUpdate = Integer.parseInt(songIdInput.trim());
                                catalog.updateUserScore(songToUpdate, connection);
                            } else {
                                JOptionPane.showMessageDialog(preventUserExit, "Returning to Menu.");
                            }
                        } catch (NumberFormatException e) {
                            JOptionPane.showMessageDialog(preventUserExit, "Please enter a valid integer for song ID.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(preventUserExit, "Please connect to a database first.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    break;
                case 4: // Filter Catalog
                    if (establishedConnection) {
                        if (songs.isEmpty()) {
                            JOptionPane.showMessageDialog(preventUserExit, "Ain't Nobody Here But Us Chickens (Try adding a .txt file to the catalog!)", "Error", JOptionPane.ERROR_MESSAGE);
                        } else {
                            catalog.askForDisplay();
                        }
                    } else {
                        JOptionPane.showMessageDialog(preventUserExit, "Please connect to a database first.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    break;
                case 5: // Exit Catalog
                    JOptionPane.showMessageDialog(preventUserExit, "Exiting Program...", "Exit Program", JOptionPane.WARNING_MESSAGE);
                    System.exit(0);
                    break;
                default: // Clicked X on the Window
                    JOptionPane.showMessageDialog(preventUserExit, "Exiting Program...", "Exit Program", JOptionPane.WARNING_MESSAGE);
                    System.exit(0);
                    break;
            }

        } while (select != 5);

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
    public void accessDatabase() {
        initialize();
        String jdbcURL = JOptionPane.showInputDialog("Enter the JDBC URL: (Ex. jdbc:mysql://[host]:[port]/[database_name] )");
        if (jdbcURL == null) {
            JOptionPane.showMessageDialog(null, "Returning to Menu.");
            return;
        }
        jdbcURL = jdbcURL.trim();

        String username = JOptionPane.showInputDialog("Enter your MySQL username: (Ex. root )");
        if (username == null) {
            JOptionPane.showMessageDialog(null, "Returning to Menu.");
            return;
        }
        username = username.trim();


        String password = JOptionPane.showInputDialog("Enter your MySQL password:");
        if (password == null) {
            JOptionPane.showMessageDialog(null, "Returning to Menu.");
            return;
        }
        password = password.trim();

        try {
            connection = DriverManager.getConnection(jdbcURL, username, password);
            JOptionPane.showMessageDialog(null, "Database connection successful.", "Success", JOptionPane.INFORMATION_MESSAGE);
            establishedConnection = true; //Very Important Boolean, Once Connection is successful every other Menu Option becomes usable.

            ArrayList<Song> fetchedSongs = fetchSongs();

            // Add fetched songs to your ArrayList & Catalog
            for (Song song : fetchedSongs) {
                songs.add(song);
                catalog.addSong(song);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Failed to connect to database: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private ArrayList<Song> fetchSongs() {
        ArrayList<Song> songs = new ArrayList<>();

        String query = "SELECT * FROM Songs";

        try (Statement statement = connection.createStatement();
             ResultSet result = statement.executeQuery(query)) {

            while (result.next()) {
                int songId = result.getInt("songId");
                String title = result.getString("songTitle");
                String album = result.getString("songAlbum");
                String artistName = result.getString("songArtist");
                String genre = result.getString("songGenre");
                float songScore = result.getFloat("songScore");

                // Create a new Song object & Add to ArrayList
                // The reason we are still using objects and ArrayLists despite having a sql database is because I can still use all my validation and preferences for display.
                // Effectively this makes it so that very little of my code needs to change while still achieving all the back-end requirements set out by 3rd phase of the project.
                Song newSong = new Song(songId, title, album, artistName, genre, songScore);
                songs.add(newSong);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return songs;
    }

    public void initialize() {
        try {
            // Load JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null, "MySQL JDBC Driver not found. Exiting program.", "Error", JOptionPane.ERROR_MESSAGE);
            // Handle the error, If you don't have JDBC driver the program can't  work.
            System.exit(1); // Exit application if driver not found
        }
    }

}//end class Menu