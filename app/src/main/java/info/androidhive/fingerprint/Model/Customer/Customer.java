package info.androidhive.fingerprint.Model.Customer;

/**
 * Created by Kong Yih Hern on 27/12/2017.
 */

public class Customer {
    private String customerID, name, gender, email, username, password;
    private int accountBalance;

    public Customer(){

    }

    public Customer(String customerID, String name, String gender, String email, String username, String password, int accountBalance) {
        this.customerID = customerID;
        this.name = name;
        this.gender = gender;
        this.email = email;
        this.username = username;
        this.password = password;
        this.accountBalance = accountBalance;
    }

    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(int accountBalance) {
        this.accountBalance = accountBalance;
    }

    @Override
    public String toString() {
        return "Customer details :- /n"+
                "CustomerID : "+customerID+"/n"+
                "Name : "+name+"/n"+
                "Account Balance : "+accountBalance+"/n"+
                "Gender : "+gender+"/n"+
                "Email : "+email+"/n"+
                "Username : "+username+"/n"+
                "Password : "+password+"/n";
    }
}
