package info.androidhive.fingerprint.Model.Transaction;

/**
 * Created by Kong Yih Hern on 27/12/2017.
 */

public class Transaction {
    private String transactionID,transactionDate, type, customerID;
    private int amount, charges;

    public Transaction() {

    }

    public Transaction(String transactionID, String transactionDate, String type, String customerID, int amount, int charges) {
        this.transactionID = transactionID;
        this.transactionDate = transactionDate;
        this.type = type;
        this.customerID = customerID;
        this.amount = amount;
        this.charges = charges;
    }

    @Override
    public String toString() {
        return "Transaction{" +
        "transactionID='" + transactionID + '\'' +
                ", transactionDate='" + transactionDate + '\'' +
                ", type='" + type + '\'' +
                ", customerID='" + customerID + '\'' +
                ", amount=" + amount +
                ", charges=" + charges +
                '}';
    }

    public String getTransactionID() {

        return transactionID;
    }

    public void setTransactionID(String transactionID) {
        this.transactionID = transactionID;
    }

    public String getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getCharges() {
        return charges;
    }

    public void setCharges(int charges) {
        this.charges = charges;
    }
}
