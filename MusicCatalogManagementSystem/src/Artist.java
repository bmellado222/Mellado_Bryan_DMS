/**
 * Artist --- Artist object class, its function to merely keep track of the artist's name and displaying methods.
 *
 * @author Bryan Mellado A.
 * @version update-javadoc
 * @since 7 / 17 /2024
 */
public class Artist {
    //Fields
    private String artistName;


    /**
     * Full arg constructor for Artist.
     *
     * @param artistName Name of Artist.
     */
    public Artist(String artistName) {
        this.artistName = artistName;
    }


    /**
     * Get the artistName String value for the Artist.
     *
     * @return String name of the Artist.
     */
    public String getArtistName() {
        return artistName;
    }


    /**
     * Set String value for artistName of Artist.
     *
     * @param artistName New name of Artist.
     */
    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }


    /**
     * Returns a string of the artist's name.
     *
     * @return String The name of the Artist.
     */
    public String toString() {
        return artistName;
    }


}//end class Artist
