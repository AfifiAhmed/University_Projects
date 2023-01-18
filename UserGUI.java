package servlets;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import application.RAApplication;
import dbadapter.MovieDatabase;

/**
 * Class responsible for the GUI of the user
 * 
 * @author f.fischer
 *
 */
public class UserGUI extends HttpServlet {

	
	private static final long serialVersionUID = 1L;
	/**
	 * doGet is responsible for search forms
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) {
		//System.out.printf("--- doGet ---\n");
		// Set navtype
		request.setAttribute("navtype", "registeredUser");
		
		// Catch error if there is no page contained in the request
		String action = (request.getParameter("action") == null) ? "" : request.getParameter("action");

		// Case: Request movieList form
		if (action.equals("giveRating")) {
			//System.out.printf("give Rating\n");
			// Set request attributes
			request.setAttribute("pagetitle", "Rate Movie");
			request.setAttribute("mid", request.getParameter("mid"));
			request.setAttribute("mtitle", request.getParameter("mtitle"));

			// Dispatch request to template engine
			try {
				request.getRequestDispatcher("/templates/showRateMovieForm.ftl").forward(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (action.equals("addMovieToDB")) {
			// Set request attributes
			request.setAttribute("pagetitle", "Rate Movie");

			// Dispatch request to template engine
			try {
				request.getRequestDispatcher("/templates/showAddMovieForm.ftl").forward(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}	
		} else {
			request.setAttribute("pagetitle", "Register");
			try {
				request.getRequestDispatcher("/templates/defaultWebpageU.ftl").forward(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * doPost manages handling of submitted forms.
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) {
		//System.out.printf("--- doPost --\n");
		// Set attribute for navigation types
		request.setAttribute("navtype", "registeredUser");

		// Generate and show results of a search
		if (request.getParameter("action").equals("browseMovieList")) {
			request.setAttribute("pagetitle", "Browse result");
			List<MovieDatabase> movieList = null;

			//System.out.printf("browsing movie list\n");
			// Call application to request the results
			try {
				movieList = RAApplication.getInstance().getMovieList();
				
				//user already registered, dont show Register Option again
				request.setAttribute("navtype", "registeredUser");
				// Dispatch results to template engine
				request.setAttribute("movieList", movieList);
				request.getRequestDispatcher("/templates/movieListRepresentation.ftl").forward(request, response);
			} catch (Exception e1) {
				try {
					request.setAttribute("errormessage", "Database error: please contact the administator");
					request.getRequestDispatcher("/templates/error.ftl").forward(request, response);
				} catch (Exception e) {
					request.setAttribute("errormessage", "System error: please contact the administrator");
					e.printStackTrace();
				}
				e1.printStackTrace();
			}
		// Insert rating into movie database
		} else if (request.getParameter("action").equals("rateMovie")) {
			String ratingValueString = request.getParameter("ratingValue");
			int ratingValueInt = Integer.parseInt(ratingValueString);
			
			if(ratingValueInt < 0 || ratingValueInt > 10) {		
				request.setAttribute("navtype",  "goBack");
				request.setAttribute("pagetitle", "Rating failed");
				request.setAttribute("errormessage", "Rating failed. Please enter a rating between 0 and 10: 1= very bad movie, 10= excellent movie");
				try {
					request.getRequestDispatcher("/templates/error.ftl").forward(request, response);
				} catch (ServletException | IOException e) {
					e.printStackTrace();
				}
			} else {	
				//System.out.printf("rateMovie\n");
				//String ratingValue, String ratingComment, String userName, String title
				if (RAApplication.getInstance().forwardRating(request.getParameter("ratingValue"),
						request.getParameter("ratingComment"), request.getParameter("mid"),
						request.getParameter("mtitle")) != false) {
					
					String mtitle = request.getParameter("mtitle");
					String ratingValue = request.getParameter("ratingValue");
	
					//System.out.printf("forwardRating != false\n");
					System.out.printf("+ rated movie " + mtitle + " with " + ratingValue + "/10\n");
					
					// Set request attributes
					request.setAttribute("pagetitle", "Rating Successful");
					request.setAttribute("message", "You've successfully rated the movie: " + mtitle + " with " + ratingValue + "/10 Points!");
	
					// Dispatch to template engine
					try {
						request.getRequestDispatcher("/templates/ratingRepresentation.ftl").forward(request, response);
					} catch (ServletException | IOException e) {
						e.printStackTrace();
					}
					// Catch rating error and print an error on the gui
				} else {
					request.setAttribute("pagetitle", "Rating failed");
					request.setAttribute("errormessage", "Rating failed.");
					try {
						request.getRequestDispatcher("/templates/error.ftl").forward(request, response);
					} catch (ServletException | IOException e) {
						e.printStackTrace();
					}
				}
			}
		//Add a movie into movie database
		} else if (request.getParameter("action").equals("addingMovie")) {
			//String title, String director, String mainActors, String publishDate, String ratingValue, String ratingComment
				// Append parameter of request
				String title = (String) request.getParameter("title");
				String director = (String) request.getParameter("director");
				String mainActors = (String) request.getParameter("mainActors");
				String publishDate = (String) request.getParameter("publishDate");
				String ratingComment = (String) request.getParameter("ratingComment");
				
				if(request.getParameter("ratingValue").isEmpty()) {
					String ratingValueZ = "0";
					//System.out.println("ratingValue is empty");
					//Movies without any rating are rated as zero
					// Call application to add new movie
					if (new RAApplication().joinMovieToDB(title, director, mainActors, publishDate, ratingValueZ, ratingComment) != false) {
						System.out.printf("+ successfully added movie " + title + " published on: " +  publishDate + "\n");
		
						// Dispatch message to template engine
						try {
							//user already registered, dont show Register Option again
							request.setAttribute("navtype", "registeredUser");
							request.setAttribute("pagetitle", "Added Movie");
							request.setAttribute("message", "You've successfully added a movie.");
							request.getRequestDispatcher("/templates/addedMovieRepresentation.ftl").forward(request, response);
		
						} catch (ServletException | IOException e) {
							e.printStackTrace();
						}
					} else {
						System.out.printf("- movietitle " + title + " already exists in db\n");
						try {
							//user already registered, dont show Register Option again
							request.setAttribute("navtype", "goBack");
							request.setAttribute("pagetitle", "Error");
							request.setAttribute("errormessage", "This movie is already in the database.");
							request.getRequestDispatcher("/templates/error.ftl").forward(request, response);
	
						} catch (ServletException | IOException e) {
							e.printStackTrace();
						}
					}
				} else {
					String ratingValue = (String) request.getParameter("ratingValue");
					int ratingValueInt = Integer.parseInt(ratingValue);
					
					if(ratingValueInt < 0 || ratingValueInt > 10) {	
						request.setAttribute("navtype",  "goBack");
						request.setAttribute("pagetitle", "Adding movie failed");
						request.setAttribute("errormessage", "Adding movie failed. Please enter a rating value between 0 and 10. No rating translates to zero.");
						try {
							request.getRequestDispatcher("/templates/error.ftl").forward(request, response);
						} catch (ServletException | IOException e) {
							e.printStackTrace();
						}
					} else {
						// Call application to add new movie
						if (new RAApplication().joinMovieToDB(title, director, mainActors, publishDate, ratingValue, ratingComment) != false) {
							System.out.printf("+ successfully added movie " + title + " published on: " +  publishDate + "\n");
			
							// Dispatch message to template engine
							try {
								//user already registered, dont show Register Option again
								request.setAttribute("navtype", "registeredUser");
								request.setAttribute("pagetitle", "Added Movie");
								request.setAttribute("message", "You've successfully added a movie.");
								request.getRequestDispatcher("/templates/addedMovieRepresentation.ftl").forward(request, response);
			
							} catch (ServletException | IOException e) {
								e.printStackTrace();
							}
						} else {
							System.out.printf("- movietitle " + title + " already exists in db\n");
							try {
								//user already registered, dont show Register Option again
								request.setAttribute("navtype", "goBack");
								request.setAttribute("pagetitle", "Error");
								request.setAttribute("errormessage", "This movie is already in the database.");
								request.getRequestDispatcher("/templates/error.ftl").forward(request, response);
		
							} catch (ServletException | IOException e) {
								e.printStackTrace();
							}
						}
					}
				}
		//last case: add user to the database		
		} else 		// Check wether the call is register or not
			if (request.getParameter("action").equals("register")) {

				// Append parameter of request
				String userName = (String) request.getParameter("userName");
				String age = (String) request.getParameter("age");
				String emailAddress = (String) request.getParameter("emailAddress");
				
				int ageInt = Integer.parseInt(age);
				
				if (ageInt < 18) {
					System.out.printf("- age check failed: (" + ageInt + ")\n");
					// Dispatch message to template engine
					try {
						//user not registering, re-set navtype to default
						request.setAttribute("navtype", "goBack");
						request.setAttribute("pagetitle", "Error");
						request.setAttribute("errormessage", "You are not above the age of 18!");
						request.getRequestDispatcher("/templates/error.ftl").forward(request, response);

					} catch (ServletException | IOException e) {
						e.printStackTrace();
					}
				
				} else if (ageInt > 130) {
					System.out.printf("- age check failed: (" + ageInt + ")\n");
					// Dispatch message to template engine
					try {
						//user not registering, re-set navtype to default
						request.setAttribute("navtype", "goBack");
						request.setAttribute("pagetitle", "Error");
						request.setAttribute("errormessage", ageInt + "? That's probably a new world record. Try again...");
						request.getRequestDispatcher("/templates/error.ftl").forward(request, response);

					} catch (ServletException | IOException e) {
						e.printStackTrace();
					}
				
				} else {
				// Call application to add new user
				//new RAApplication().addNewUser(userName, age, emailAddress);
				
				if (new RAApplication().addNewUser(userName, age, emailAddress) != false) {
					System.out.printf("+ user " + userName + " registered\n");
				
				// Dispatch message to template engine
					try {
						//user already registered, dont show Register Option again
						request.setAttribute("navtype", "registeredUser");
						request.setAttribute("pagetitle", "Register");
						request.setAttribute("message1", "You've successfully registered.");
						request.setAttribute("message2", "You can now choose to browse the movie list or add a new one to it.");
						request.getRequestDispatcher("/templates/registerRepresentation.ftl").forward(request, response);
	
					} catch (ServletException | IOException e) {
						e.printStackTrace();
					}
				} else {
					System.out.printf("- username " + userName + " already exists in db\n");
					try {
						//user already registered, dont show Register Option again
						request.setAttribute("navtype", "goBack");
						request.setAttribute("pagetitle", "Error");
						request.setAttribute("errormessage", "Your chosen username already exists.");
						request.getRequestDispatcher("/templates/error.ftl").forward(request, response);

					} catch (ServletException | IOException e) {
						e.printStackTrace();
					}
				}
				}
				// Call doGet if request is not equal to above actions
			} else
				doGet(request, response);
	}
	
	
}