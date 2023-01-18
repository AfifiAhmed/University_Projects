package interfaces;

import java.util.ArrayList;
import java.sql.Timestamp;
import dbadapter.MovieDatabase;

/**
 * Interface for DBFacade to provide all necessary database functions.
 * 
 * @author f.fischer
 *
 */
public interface IMovieDatabase {

	//public ArrayList<MovieDatabase> getMovieList(String title, String director, String mainActors, Timestamp publishDate);
	public ArrayList<MovieDatabase> getMovieList();
	
	public boolean joinMovieToDB(String title, String director, String mainActors, Timestamp publishDate, int ratingValue, String ratingComment);

	//public void forwardRating(int ratingValue, String ratingComment, String userName, String title);

}
