package application;

/**
 * This class create an object to store music DB
 * entries. It can store and return object properties.
 * @author Farrukh Khan
 *
 */
public class MusicMeta 
{
	//Create variables
	private String itemCode;
	private String title;
	private String artist;
	private String description;
	private String album;
	
	/**
	 * This method sets all properties.
	 * @param itm - item code
	 * @param titl - title of song
	 * @param arts - artist name
	 * @param des - description
	 * @param alb - album name
	 */
	public void setMusic(String itm, String titl, String arts, String des, String alb)
	{
		//Save to variables
		itemCode = itm;
		title = titl;
		artist = arts;
		description = des;
		album = alb;
	}
	
	public String getItemCode()
	{
		return itemCode;
	}
	
	/**
	 * This method returns the song title.
	 * @return
	 */
	public String getTitle()
	{
		//Return song title
		return title;
	}
	
	/**
	 * This method returns the artist.
	 * @return
	 */
	public String getArtist()
	{
		//Return artist name
		return artist;
	}
	
	/**
	 * This method returns the song year.
	 * @return
	 */
	public String getDescription()
	{
		//Return song release year
		return description;
	}
	
	/**
	 * This method returns the album title.
	 * @return
	 */
	public String getAlbum()
	{
		//Return song album title
		return album;
	}

}
