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
    private int identification;
    private String title;
    private String album;
    private Artist artist;
    private String genre;
    private float songScore;

    public Song(int identification, String title, String album, String artistName, String genre, float songScore) {
        this.identification = identification;
        this.title = title;
        this.album = album;
        this.artist = new Artist(artistName);
        this.genre = genre;
        this.songScore = songScore;
    }


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

        return String.format("Song: [ID=%s, Title=%s, Album=%s, Artist=%s, Genre=%s, Score=%.2f]",
                identification, title, album, artist, genre, songScore);
    }
//end method toString



}//end class Song