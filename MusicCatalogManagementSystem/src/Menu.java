import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import javax.swing.*;

/**
 * Menu --- Heart of the program, it creates a menu for the user, where the user may interact with the program to do this programs intended functions along with proper validation.
 * This menu implements JOptionPane and a JFrame for its GUI.
 * This class also houses the method for actually connecting to the mySQL database.
 *
 * @author Bryan Mellado A.
 * @version update-javadoc
 * @since	  7 / 17 /2024
 */
public class Menu {
    //Fields
    private ArrayList<Song> songs;
    private Catalog catalog;
    private JFrame preventUserExit;
    private boolean establishedConnection = false;
    private Connection connection;


    /**
     * Constructor initializes songs arrayList and catalog, it then runs the method for adding books, then finally boots to menu.
     * I also made a JFrame because I hadn't realized that the user can technically just leave the program by just clicking off the JOptionPane screen
     * which evades the Modality of JOptionPane and the only way to prevent that, while continuing to use JOptionPane, was to add this rather simplistic JFrame.
     */
    public Menu() {
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


    /**
     * Displays a Menu system using JOptionPane, JLabel is used to center some text to make the GUI look slightly better.
     * You pick from a number of options seen in the String[] using a classic switch case setup, each action however,
     * will be prevented from functioning if you have NOT connected to a mySQL database yet, aside from the first action which lets you connect to a database.
     * Outside of that it also accounts for every user action while interacting with the menu, you can exit the program from
     * only one of three ways, when the user click X or cancel on the menu or when JDBC is attempted to load, and it can't find the appropriate drivers.
     * Otherwise, the user will be sent back to the menu after every action.
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
                        //List of common issues a user may face when attempting to connect to a mySQL database.
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


    /**
     * Attempts to access a mySQL database, by first starting with loading up JDBC drivers, then prompting the user for critical information to attempt a database connection.
     * After information is gather from JOptionPane input dialogs, a connection is attempted, if successful, every other action of the MCMS menu becomes usable
     * and every song in the mySQL database is loaded on to an arrayList, which is important for practically every other action the user takes outside of this method.
     * Otherwise, the user will be told of the connection failure and returned to menu.
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

            // Use passed arrayList to obtain fetched songs and add to main ArrayList & Catalog
            for (Song song : fetchedSongs) {
                songs.add(song);
                catalog.addSong(song);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Failed to connect to database: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Create a new Song object & Add to ArrayList from the connected database by starting a query.
     * The reason we are still using objects and ArrayLists despite having a sql database is because I can still use all my validation and preferences for display.
     * Effectively this makes it so that very little of my code needs to change while still achieving all the back-end requirements set out by 3rd phase of the project.
     *
     * @return songs The arrayList of songs that were obtained and created from the database.
     */
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

                Song newSong = new Song(songId, title, album, artistName, genre, songScore);
                songs.add(newSong);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //pass the arrayList
        return songs;
    }

    /**
     * Try to load JDBC Driver, otherwise exit the whole program because there is no way that you can use this program as intended without it.
     */
    public void initialize() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null, "MySQL JDBC Driver not found. Exiting program.", "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }

}//end class Menu