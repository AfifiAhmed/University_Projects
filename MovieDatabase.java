package dbadapter;

import java.sql.Timestamp;
import java.util.ArrayList;

/**
 * Class representing the movie database
 * 
 * @author f.fischer
 *
 */
public class MovieDatabase {

	private int id;
	private String title; 
	private String director; 
	private String mainActors;
	private Timestamp publishDate;
	private int ratingValue;
	private String ratingComment;
	private ArrayList<MovieDatabase> movies;

	public MovieDatabase(int id, String title, String director, String mainActors, Timestamp publishDate, int ratingValue, String ratingComment) {
		this.id = id;
		this.title = title;
		this.director = director;
		this.mainActors = mainActors;
		this.publishDate = publishDate;
		this.ratingValue = ratingValue;
		this.ratingComment = ratingComment;
	}

	/* //TODO?:
	public String toString() {
		return "HolidayOffer " + id + " startTime: " + startTime + " endTime: " + endTime + " capacity: " + capacity
				+ " fee: " + fee;
	}
	*/

	public int getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public String getDirector() {
		return director;
	}

	public String getMainActors() {
		return mainActors;
	}

	public Timestamp getPublishDate() {
		return publishDate;
	}

	public int getRatingValue() {
		return ratingValue;
	}

	public String getRatingComment() {
		return ratingComment;
	}

	public ArrayList<MovieDatabase> getMovies() {
		return movies;
	}

}
