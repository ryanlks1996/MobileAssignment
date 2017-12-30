package info.androidhive.fingerprint.Model.Transaction;

/**
 * Created by Family on 27/12/2017.
 */

public class Transaction {
    private String transactionID,transactionDate, customerID,type;
    private int amount, charges;

    public Transaction() {

    }

    public Transaction(String transactionID, String transactionDate, String customerID, String type, int amount, int charges) {
        this.transactionID = transactionID;
        this.transactionDate = transactionDate;
        this.customerID = customerID;
        this.amount = amount;
        this.charges = charges;
        this.type = type;
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

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setCharges(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "transactionID='" + transactionID + '\'' +
                ", transactionDate='" + transactionDate + '\'' +
                ", customerID='" + customerID + '\'' +
                ", amount=" + amount +
                ", charges=" + charges +
                ", type=" + type +
                '}';
    }
}
