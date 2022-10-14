package application.mealplanner;

public class FrameControllerClass {
    private String userEmail;
    private String userPassword;

    public FrameControllerClass(String email, String password) {
        this.userEmail = email;
        this.userPassword = password;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getUserPassword() {
        return userPassword;
    }
}
