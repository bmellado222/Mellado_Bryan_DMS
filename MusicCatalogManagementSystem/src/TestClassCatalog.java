/********************************************
 * Name: 	  Bryan Mellado	  	         	*
 * Course: 	  CEN 3024C	     	         	*
 * Purpose:	  MCMS       			     	*
 * Date:	  6 / 8 /2024			     	*
 ********************************************
 * Class Function:
 * Creates object Menu which in turn creates the proper arrayLists and starts up the menu for the user to interact with.
 *
 * Project Function:
 * This is the Music Catalog Management System, its intended uses include:
 * adding songs to a catalog with plenty of validation, removing a song from the catalog based on either song ID or title, displaying all songs in the
 * catalog in descending order by their rank, updating the user score of a song in the catalog which in turn effects a song's rank, and filtering the display of
 * all songs in the catalog by either album or author name. However, this is not all there is to
 * it because along with the functionalities of the project come the required constraints of these
 * functionalities, which would include making sure that there are no duplicate IDs,
 * preventing personal scores from going above or below the range of 0.00–5.00,
 * preventing song title and album names from being larger than 75 characters
 * preventing song aritst and genre names from being larger than 50 character,
 * that each attribute of the song objects in the catalog possesses the correct data types, that the program correctly
 * displays and ranks each song on the list based on each song's individual score, and,
 * making sure that the program accepts only comma-delimited test files that possess only six attributes when adding songs. All of which the user interacts with
 * at some point via the menu
 */
public class TestClassCatalog {

    /*
    Name: main
    Explanation:
    creates the Menu object and fires up the Menu() method
    Arguments: None
    Return Values: Void
     */
    public static void main(String[] args) {
        Menu myMenu = new Menu();
    }// end method Main



}//end class TestClassCatalog
