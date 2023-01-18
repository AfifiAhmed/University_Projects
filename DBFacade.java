package dbadapter;

import java.sql.Connection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

import interfaces.IMovieDatabase;
import interfaces.IUserDatabase;

/**
 * Class which acts as the connector between application and database. Creates Java objects from SQL returns. Exceptions thrown 
 * in this class will be caught with a 500 error page.
 * 
 * @author f.fischer
 *
 */
public class DBFacade implements IMovieDatabase, IUserDatabase {
	private static DBFacade instance;

	
	/**
	 * Constructor which loads the corresponding driver for the chosen database type
	 */
	private DBFacade() {
		try {
			Class.forName("com." + Configuration.getType() + ".jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Singleton pattern
	 * 
	 * @return
	 */
	public static DBFacade getInstance() {
		if (instance == null) {
			instance = new DBFacade();
		}
		return instance;
	}

	public static void setInstance(DBFacade dbfacade) {
		instance = dbfacade;
	}

	/**
	 * Function that returns all movies from the movie database.
	 * 
	 * @param title
	 * @param director
	 * @param mainActors
	 * @param publishDate
	 * @return ArrayList of all movie objects.
	 * @throws SQLException 
	 */
	//public ArrayList<MovieDatabase> getMovieList(String title, String director, String mainActors, Timestamp publishDate) {
	public ArrayList<MovieDatabase> getMovieList() {
		ArrayList<MovieDatabase> mo = new ArrayList<MovieDatabase>();

		// Declare the necessary SQL queries.
		String queryLIST = "SELECT id,title,director,mainActors,publishDate,AVG(ratingValue),ratingComment FROM MovieDatabase GROUP BY title ORDER BY avg(ratingValue) DESC";

		// Query all movies.
		try (Connection connection = DriverManager.getConnection(
						"jdbc:" + Configuration.getType() + "://" + Configuration.getServer() + ":"
								+ Configuration.getPort() + "/" + Configuration.getDatabase(),
						Configuration.getUser(), Configuration.getPassword())) {
			try (PreparedStatement ps = connection.prepareStatement(queryLIST)) {
			
			//MovieListSet
			try (ResultSet mls = ps.executeQuery()) {
				int i = 0;
				System.out.printf("executed queryList\n");
				
				//ArrayList<MovieDatabase> mo = new ArrayList<MovieDatabase>();
				while (mls.next()) {
					i++;
					mo.add(new MovieDatabase(mls.getInt(1), mls.getString(2), mls.getString(3), mls.getString(4), mls.getTimestamp(5), mls.getInt(6), mls.getString(7)));
				}
				System.out.printf("detected " + i + " rows in MovieDatabase.\n");
			}	
			catch (Exception e) {
				e.printStackTrace();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		} catch (SQLException e) {
			e.printStackTrace();
		}	
		return mo;
	}	

	/**
	 * Inserts a new user into the user database.
	 * 
	 * @param userName
	 * @param age
	 * @param emailAddress
	 * @return 
	 */
	public UserDatabase addNewUser(String userName, int age, String emailAddress) {
		UserDatabase newUser = null;
		// Declare SQL query to insert user.
		String insertU = "INSERT INTO UserDatabase (userName, age, emailAddress) VALUES (?,?,?)";

		// Insert user into user database.
		try (Connection connection = DriverManager.getConnection(
						"jdbc:" + Configuration.getType() + "://" + Configuration.getServer() + ":"
								+ Configuration.getPort() + "/" + Configuration.getDatabase(),
						Configuration.getUser(), Configuration.getPassword())) {

			try (PreparedStatement ps = connection.prepareStatement(insertU)) {
				ps.setString(1, userName);
				ps.setInt(2, age);
				ps.setString(3, emailAddress);
				ps.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
				throw new NumberFormatException();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return newUser;
	}

	/**
	 * Inserts a new movie in the database.
	 * 
	 * @param title
	 * @param director
	 * @param mainActors
	 * @param publishDateSQL
	 * @param ratingValue
	 * @param ratingComment
	 * @return 
	 */
	public boolean joinMovieToDB(String title, String director, String mainActors, Timestamp publishDate, int ratingValue, String ratingComment) {
		boolean joined = false;
		// Declare SQL query to insert movie.
		String insertM = "INSERT INTO MovieDatabase (title, director, mainActors, publishDate, ratingValue, ratingComment) VALUES (?,?,?,?,?,?)";
		// Insert movie into movie database.
		try (Connection connection = DriverManager.getConnection(
						"jdbc:" + Configuration.getType() + "://" + Configuration.getServer() + ":"
								+ Configuration.getPort() + "/" + Configuration.getDatabase(),
						Configuration.getUser(), Configuration.getPassword())) {

			try (PreparedStatement ps = connection.prepareStatement(insertM)) {
				ps.setString(1, title);
				ps.setString(2, director);
				ps.setString(3, mainActors);
				ps.setTimestamp(4, publishDate);
				ps.setInt(5, ratingValue);
				ps.setString(6, ratingComment);
				ps.executeUpdate();
				joined = true;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return joined;
	}

	/**
	 * Adds a rating to a selected movie.
	 * 
	 * @param ratingValueSQL
	 * @param ratingComment
	 * @param userName
	 * @param title
	 */
	public boolean forwardRating(double ratingValueSQL, String ratingCommentSQL, int idSQL, String titleSQL) {
		boolean rating = false;
		
		// Declare SQL query to insert offer.
		//String insertR = "INSERT INTO MovieDatabase (ratingValue,ratingComment,userName,title) VALUES (?,?,?,?)";
		String updateR = "INSERT INTO MovieDatabase (title, director, mainActors, publishDate, ratingValue, ratingComment) VALUES (?,?,?,?,?,?)";
		// Insert rating into movie database.
		try (Connection connection = DriverManager.getConnection(
						"jdbc:" + Configuration.getType() + "://" + Configuration.getServer() + ":"
								+ Configuration.getPort() + "/" + Configuration.getDatabase(),
						Configuration.getUser(), Configuration.getPassword())) {
			try (PreparedStatement ps = connection.prepareStatement(updateR)) {
				DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
				Date date = dateFormat.parse("1/1/2020");
				long time = date.getTime();
				Timestamp publishDateSQL = new Timestamp(time);
				ps.setString(1, titleSQL);
				ps.setString(2, "");
				ps.setString(3, "");
				ps.setTimestamp(4, publishDateSQL);
				//doesnt matter until ratingValue column in M table gets changed from int to double
				ps.setDouble(5, ratingValueSQL);
				ps.setString(6, ratingCommentSQL);
				ps.executeUpdate();
				rating = true;
				//System.out.printf("addRating");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rating;
	}

	/**
	 * Checks if user with given userName exists.
	 * 
	 * @param userName
	 * @return
	 */
	public boolean checkUserDatabaseByName(String userName) {
		//boolean unique = true;
		// Declare necessary SQL query.		
		String queryREG = "SELECT * FROM UserDatabase WHERE userName=?";
		// query data.
		try (Connection connection = DriverManager.getConnection(
						"jdbc:" + Configuration.getType() + "://" + Configuration.getServer() + ":"
								+ Configuration.getPort() + "/" + Configuration.getDatabase(),
						Configuration.getUser(), Configuration.getPassword())) {
			try (PreparedStatement psSelect = connection.prepareStatement(queryREG)) {
				psSelect.setString(1, userName);
				try (ResultSet rs = psSelect.executeQuery()) {
					//returns true if query finds a row 
					// true = username exists already = not unique
					return rs.next();	
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Checks if movie with given title exists.
	 * 
	 * @param title
	 * @return
	 */
	public boolean checkMovieDatabaseByTitle(String title) {
		// Declare necessary SQL query.
		String queryNEWM = "SELECT * FROM MovieDatabase WHERE title=?";
		// query data.
		try (Connection connection = DriverManager.getConnection(
						"jdbc:" + Configuration.getType() + "://" + Configuration.getServer() + ":"
								+ Configuration.getPort() + "/" + Configuration.getDatabase(),
						Configuration.getUser(), Configuration.getPassword())) {
			try (PreparedStatement psSelect = connection.prepareStatement(queryNEWM)) {
				psSelect.setString(1, title);
				try (ResultSet rs = psSelect.executeQuery()) {
					return rs.next();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	

}
