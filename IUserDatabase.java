package interfaces;

import dbadapter.UserDatabase;

/**
 * Interface for DBFacade to provide all necessary database functions.
 * 
 * @author f.fischer
 *
 */
public interface IUserDatabase {

	public UserDatabase addNewUser(String userName, int age, String emailAddress);

}
