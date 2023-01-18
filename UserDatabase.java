package dbadapter;

/**
 * Class representing the user database
 * 
 * @author f.fischer
 *
 */
public class UserDatabase {

	int id;
	String userName;
	int age;
	String emailAddress;

	public UserDatabase(int id, String userName, int age, String emailAddress) {
		//super();
		this.id = id;
		this.userName = userName;
		this.age = age;
		this.emailAddress = emailAddress;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}


//TODO?:
	/**
	 * Checks if this booking overlaps with the given timespace.
	 * 
	 * @param arrivalTime
	 * @param departureTime
	 * @return
	 */
	
/*
	public boolean overlap(Timestamp arrivalTime, Timestamp departureTime) {
		//Check precondition
		assert this.arrivalTime.before(this.departureTime) :
			"pre not satisfiedd: this.arrivalTime < this.departureTime";
		assert arrivalTime.before(departureTime) :
			"pre not satisfiedd: arrivalTime < departureTime";
		// Case 1
		if ((arrivalTime.after(this.arrivalTime) || arrivalTime.equals(this.arrivalTime))
				&& (arrivalTime.before(this.departureTime) || arrivalTime.equals(this.departureTime))) {
			return true;
		}
		// Case 2
		if ((departureTime.after(this.arrivalTime) || departureTime.equals(this.arrivalTime))
				&& (departureTime.before(this.departureTime) || departureTime.equals(this.departureTime))) {
			return true;
		}
		// Case 3
		if (arrivalTime.before(this.arrivalTime) && departureTime.after(this.departureTime)) {
			return true;
		}
		return false;
	}
*/

}
