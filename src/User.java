public class User {


    private String username;
    private String password;
    private String status;

    public User( String username, String password, String status){

        this.username = username;
        this.password = password;
        this.status = status;

    }



    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "User{" +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
