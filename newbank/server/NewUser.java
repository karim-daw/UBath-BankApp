// Use case requirements

// New user can request to register.
// The system will request user to provide a username and password.
// The system will check for any conflicts with existing usernames.
// If conflict, the system will prompt them to choose another username.
// If no conflict the system adds them to the data store and notifies them.

package newbank.server;

public class NewUser {

	private String accountName;
	private String password;
    public Array ExistingUserNames

	public NewUser(String UserName, String Password) {
		this.UserName = UserName;
		this.Password = Password;
	}

    public checkExistingUsers(String UserName, array ExistingUserNames) {
        this.UserName = username
        this.ExistingUserNames = ExistingUserNames
    }

	public String SuccessFail(String Response) {
        this.Response if not UserName in ExistingUserNames:
            return (Username + " created, registration successful!")
        else:
            return (Username + " already exists, registration failed!")
	}

}