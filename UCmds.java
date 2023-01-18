package interfaces;

import java.util.ArrayList;
import dbadapter.MovieDatabase;
import dbadapter.UserDatabase;

/**
 * Interface that provides all methods to interact with a user.
 * 
 * @author f.fischer
 *
 */
public interface UCmds {
	//public ArrayList<MovieDatabase> getMovieList(String title, String director, String mainActors, String publishDate);
	public ArrayList<MovieDatabase> getMovieList();

	public boolean addNewUser(String userName, String age, String emailAddress);

	public boolean joinMovieToDB(String title, String director, String mainActors, String publishDate, String ratingValue, String ratingComment);

	public boolean forwardRating(String ratingValue, String ratingComment, String id, String title);
}
