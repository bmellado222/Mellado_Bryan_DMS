/********************************************
 * Name: 	  Bryan Mellado	  	         	*
 * Course: 	  CEN 3024C	     	         	*
 * Purpose:	  MCMS       			     	*
 * Date:	  6 /8 /2024			     	*
 ********************************************
 * Class Function:
 * Artist object class, its function to merely keep track of the artist's name
 * As well as, for displaying methods.
 * As it currently stands, there is no reason for the artist name to be its own class;
 * this is an attribute that the Song class should just have, but I feel it is important to explain my original intention for this class.
 *
 * Picture this: some songs out there, or perhaps entire albums of songs, are created by multiple artists, not just one.
 * Effectively, every song could have more than one artist attributed to it.
 * The reason I made this artist class was because I intended to make it so that when artists were added from text files,
 * the program would parse every artist if the artist attribute contained double quotes and create multiple commas for each artist, but the hard part of this came down to validation
 * Also, this was one of the last things I tried tackling. Unfortunately, there wasn't enough time to implement this so, a lot of it got left over.
 *
 */

public class Artist {
    //Fields
    private String artistName;


    /*
    Name: Artist
    Explanation: full arg constructor
    Arguments: String artistName
    Return Values: Not even void
     */
    public Artist(String artistName) {
        this.artistName = artistName;
    }

    public String getArtistName() {
        return artistName;
    }


    /*
  Name: setArtistName
  Explanation: set value for setArtistName
  Arguments: String artistName
  Return Values: void
   */
    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }


    /*
    Name: toString
    Explanation: toString()
    Arguments: none
    Return Values: String
     */
    public String toString() {
        return artistName;
    }



}//end class Artist
