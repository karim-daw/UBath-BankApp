package se2.groupb.server.customer;

//Subset of Domain for data transfer between layers
public final class CustomerDTO {

    private String username;
    private String password;

    // Constructor
    public CustomerDTO(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

}
