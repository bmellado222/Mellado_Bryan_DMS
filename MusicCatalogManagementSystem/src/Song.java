/**
 * Song --- Song object class as far as function goes this merely houses every aspect of a song. A song has a title, album, artist, id, genre, and score.
 *
 * @author Bryan Mellado A.
 * @version update-javadoc
 * @since	  7 / 17 /2024
 */
public class Song {
    //Fields
    private int identification;
    private String title;
    private String album;
    private Artist artist;
    private String genre;
    private float songScore;

    /**
     * Full arg constructor of Song.
     *
     * @param identification ID of the song.
     * @param title          Title of the song.
     * @param album          Album from which the song originates.
     * @param artistName     Name of the artist of the song.
     * @param genre          Genre of the song.
     * @param songScore      User's Personal Score of the song.
     */
    public Song(int identification, String title, String album, String artistName, String genre, float songScore) {
        this.identification = identification;
        this.title = title;
        this.album = album;
        this.artist = new Artist(artistName);
        this.genre = genre;
        this.songScore = songScore;
    }


    /**
     * Gets the ID int value of the song
     *
     * @return int ID of the song.
     */
    public int getIdentification() {
        return identification;
    }

    /**
     * Sets int value for ID of the song.
     *
     * @param identification New ID to set.
     */
    public void setIdentification(int identification) {
        this.identification = identification;
    }


    /**
     * Gets the title String value of the song.
     *
     * @return String The Title of the song.
     */
    public String getTitle() {
        return title;
    }


    /**
     * Sets String value for title of the song.
     *
     * @param title New title to set.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets the album String value of the song.
     *
     * @return String Album from which the song originates.
     */
    public String getAlbum() {
        return album;
    }

    /**
     * Sets String value for album of the song.
     *
     * @param album New album to set.
     */
    public void setAlbum(String album) {
        this.album = album;
    }


    /**
     * Gets the artist Artist value of the song.
     *
     * @return artist Artist that created the song.
     */
    public Artist getArtist() {
        return artist;
    }

    /**
     * Sets Artist value for artist of the song.
     *
     * @param artist New artist to set.
     */
    public void setArtist(Artist artist) {
        this.artist = artist;
    }


    /**
     * Gets the song score float value of the song.
     *
     * @return float User's Personal Score of the song.
     */
    public float getSongScore() {
        return songScore;
    }


    /**
     * Sets float value for score of the song.
     *
     * @param songScore New song score to set.
     */
    public void setSongScore(float songScore) {
        this.songScore = songScore;
    }



    /**
     * Gets the genre String value of the song.
     *
     * @return String Genre of the song.
     */
    public String getGenre() {
        return genre;
    }


    /**
     * Sets String value for genre of the song.
     *
     * @param genre New genre to set.
     */
    public void setGenre(String genre) {
        this.genre = genre;
    }


    /**
     * Returns a string of the song object in a particular format.
     *
     * @return String This is to display the song object in the following format: "Song: [ID=%s, Title=%s, Album=%s, Artist=%s, Genre=%s, Score=%.2f]"
     */
    public String toString() {

        return String.format("Song: [ID=%s, Title=%s, Album=%s, Artist=%s, Genre=%s, Score=%.2f]",
                identification, title, album, artist, genre, songScore);
    }//end method toString


}//end class Song