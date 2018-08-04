package plm.oop.com.plmac;

public class UserProfile {
    public String userName;
    public String userProgram;
    public String userNumber;
    public String userPassword;
    

    public UserProfile(){
    }

    public UserProfile(String userName, String userProgram, String userNumber, String userPassword) {
        this.userName = userName;
        this.userProgram = userProgram;
        this.userPassword = userPassword;
        this.userNumber = userNumber;

    }


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserProgram() { return userProgram; }

    public void setUserProgram(String userProgram) {
        this.userProgram = userProgram;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userName = userPassword;
    }
    public String getUserNumber() {
        return userNumber;
    }

    public void setUserNumber(String userNumber) {
        this.userName = userNumber;
    }

}


