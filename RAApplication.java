package application;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import dbadapter.DBFacade;
import dbadapter.MovieDatabase;
import interfaces.UCmds;

/**
 * This class contains the RAApplication which acts as the interface between all components.
 * 
 * @author f.fischer
 *
 */
public class RAApplication implements UCmds {

	
	private static RAApplication instance;

	/**
	 * Singleton pattern
	 * 
	 * @return
	 */
	public static RAApplication getInstance() {
		if (instance == null) {
			instance = new RAApplication();
		}
		return instance;
	}

	/**
	 * Calls DBFacade method to register a new user and forward the input to the user database.
	 * 
	 * @param userName
	 * @param age
	 * @param emailAddress
	 * @return
	 */
	public boolean addNewUser(String userName, String age, String emailAddress) {

		//UserDatabase showFailRegister = null;
		// inv:  WebsiteUser.allInstances() -> isUnique(userName)
		//assert uniqueName(userName) : "Username already taken";
		if(uniqueName(userName)) {
			System.out.printf("userName not unique\n");
			return false;
		} else {
			System.out.printf("userName unique\n");	
			// Parse inputs to correct datatypes for SQL
			try {
				String userNameSQL = userName;
				int ageSQL = Integer.parseInt(age);
				String emailAddressSQL = emailAddress;
				DBFacade.getInstance().addNewUser(userNameSQL, ageSQL, emailAddressSQL);
				return true;
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}	
	}

	/**
	 * Calls DBFacade method to add a new movie and forward the input to the movie database.
	 * 
	 * @param title
	 * @param director
	 * @param mainActors
	 * @param publishDate
	 */
	public boolean joinMovieToDB(String title, String director, String mainActors, String publishDate, String ratingValue, String ratingComment) {
		
		// inv: Movie.allInstances().movieInfo -> isUnique(title)
		//assert uniqueTitle(title) : "Movie already exists ";

		if (uniqueTitle(title)) {
			System.out.printf("title not unique\n");
			return false;
		} else {
			// Parse inputs to correct datatypes for SQL
			try {
				System.out.printf("title unique\n");
				DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
	
				String titleSQL = title;
				String directorSQL = director;
				String mainActorsSQL = mainActors;
	
				Date date = dateFormat.parse(publishDate);
				long time = date.getTime();
				Timestamp publishDateSQL = new Timestamp(time);
				int ratingValueSQL = Integer.parseInt(ratingValue);
				String ratingCommentSQL = ratingComment;
				DBFacade.getInstance().joinMovieToDB(titleSQL, directorSQL, mainActorsSQL, publishDateSQL, ratingValueSQL, ratingCommentSQL);
				return true;
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}
	}

	/**
	 * Calls DBFacade method to show all movies in the list out of the movie database.
	 * 
	 * @param title
	 * @param director
	 * @param mainActors
	 * @param publishDate
	 * @return
	 */
	//public ArrayList<MovieDatabase> getMovieList(String title, String director, String mainActors, String publishDate) {
	public ArrayList<MovieDatabase> getMovieList() {
		ArrayList<MovieDatabase> result = null;
		// Parse string attributes to correct datatype
		try {
			/*
			DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
			String titleSQL = title;
			String directorSQL = director;
			String mainActorsSQL = mainActors;
			Date date = dateFormat.parse(publishDate);
			long time = date.getTime();
			Timestamp publishDateSQL = new Timestamp(time);
			result = DBFacade.getInstance().getMovieList(titleSQL, directorSQL, mainActorsSQL, publishDateSQL);
			*/
			result = DBFacade.getInstance().getMovieList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * Calls DBFacade method to add a rating to a selected movie and forwards it to the movie database.
	 * 
	 * @param ratingValue
	 * @param ratingComment
	 * @param userName
	 * @param title
	 */
	public boolean forwardRating(String ratingValue, String ratingComment, String mid, String title) {
		
		boolean givenAlreadyNoexistWrong = false;
		//System.out.printf("forwardRating\n");
		
		// Parse inputs to correct datatypes
		try {
			double ratingValueSQL = Double.parseDouble(ratingValue);
			//if (ratingValueSQL > 0 || ratingValueSQL < 10) {
			//	ratingValueSQL = 0;
			//}
			String ratingCommentSQL = ratingComment;
			int idSQL = Integer.parseInt(mid);
			String titleSQL = title;
			givenAlreadyNoexistWrong= DBFacade.getInstance().forwardRating(ratingValueSQL, ratingCommentSQL, idSQL, titleSQL);
		} catch (Exception e) {
			e.printStackTrace();
		}	
		
		return givenAlreadyNoexistWrong;
	}

	/**
	 * Checks precondition if title of movie is unique.
	 * 
	 * @param title
	 * @return
	 */
	private boolean uniqueTitle(String title) {
		return DBFacade.getInstance().checkMovieDatabaseByTitle(title);
	}

	/**
	 * Checks precondition if userName is unique.
	 * 
	 * @param userName
	 * @return
	 */
	private boolean uniqueName(String userName) {
		return DBFacade.getInstance().checkUserDatabaseByName(userName);
	}
	
	
}
