/********************************************
 * Name: 	  Bryan Mellado	  	         	*
 * Course: 	  CEN 3024C	     	         	*
 * Purpose:	  MCMS       			     	*
 * Date:	  6 / 8 /2024			     	*
 ********************************************
 * Class Function:
 * Song object class as far as function goes this merely houses every aspect of a song.
 * A song has a title, album, artist, id, genre, and score.
 * This is also used for display methods.
 *
 */
class Song {
    //Fields
    private String title;
    private String album;
    private Artist artist;
    private int identification;
    private String genre;
    private float songScore;


    /*
       Name: getIdentification
       Explanation: get value for Identification
       Arguments: none
       Return Values: int
        */
    public int getIdentification() {
        return identification;
    }


    /*
  Name: setIdentification
  Explanation: set value for Identification
  Arguments: int newId
  Return Values: void
   */
    public void setIdentification(int newId) {
        this.identification = newId;
    }


    /*
       Name: getTitle
       Explanation: get value for Title
       Arguments: none
       Return Values: String
        */
    public String getTitle() {
        return title;
    }


    /*
  Name: setTitle
  Explanation: set value for Title
  Arguments: String title
  Return Values: void
   */
    public void setTitle(String title) {
        this.title = title;
    }

    /*
       Name: getAlbum
       Explanation: get value for Album
       Arguments: none
       Return Values: String
        */
    public String getAlbum() {
        return album;
    }

    /*
  Name: setAlbum
  Explanation: set value for Album
  Arguments: String album
  Return Values: void
   */
    public void setAlbum(String album) {
        this.album = album;
    }


    /*
    Name: getArtist
    Explanation: get value for Artist
    Arguments: none
    Return Values: Artist
     */
    public Artist getArtist() {
        return artist;
    }

    /*
   Name: setArtist
   Explanation: set value for Artist
   Arguments: Artist artist
   Return Values: void
    */
    public void setArtist(Artist artist) {
        this.artist = artist;
    }


    /*
    Name: getSongScore
    Explanation: get value for SongScore
    Arguments: none
    Return Values: float
     */
    public float getSongScore() {
        return songScore;
    }


    /*
    Name: setSongScore
    Explanation: set value for Score
    Arguments: float songScore
    Return Values: void
     */
    public void setSongScore(float songScore) {
        this.songScore = songScore;
    }



    /*
    Name: getGenre
    Explanation: get value for genre
    Arguments: none
    Return Values: String
     */
    public String getGenre() {
        return genre;
    }



    /*
    Name: setGenre
    Explanation: set value for genre
    Arguments: String genre
    Return Values: void
     */
    public void setGenre(String genre) {
        this.genre = genre;
    }



    /*
    Name: toString
    Explanation: toString()
    Arguments: none
    Return Values: String
     */

    public String toString() {
        String idFormat = "ID: %-11s";
        String titleFormat = "Title: %-55s"; // This is supposed to be accounting for 75 characters total but, I made it smaller for demonstration purposes, normally this would be 76
        String albumFormat = "Album: %-55s"; // This is supposed to be accounting for 75 characters total but, I made it smaller for demonstration purposes, normally this would be 76
        String artistFormat = "Artist: %-44s"; // This is supposed to be accounting for 50 characters total but, I made it smaller for demonstration purposes, normally this would be 51
        String genreFormat = "Genre: %-44s"; // This is supposed to be accounting for 50 characters total but, I made it smaller for demonstration purposes, normally this would be 51
        String scoreFormat = "Score: %6.2f";

        String result = String.format(idFormat + " | " + titleFormat + " | " + albumFormat + " | " +
                        artistFormat + " | " + genreFormat + " | " + scoreFormat,
                identification, title, album, artist.toString(), genre, songScore);
        return result;
    }//end method toString



}//end class Song