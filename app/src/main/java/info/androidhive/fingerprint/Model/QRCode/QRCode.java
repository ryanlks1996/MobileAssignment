package info.androidhive.fingerprint.Model.QRCode;

/**
 * Created by Family on 27/12/2017.
 */

public class QRCode {
    private String QRID, QRImage, CustomerID;

    public QRCode(){

    }

    public QRCode(String QRID, String QRImage, String customerID) {
        this.QRID = QRID;
        this.QRImage = QRImage;
        CustomerID = customerID;
    }

    public String getQRID() {
        return QRID;
    }

    public void setQRID(String QRID) {
        this.QRID = QRID;
    }

    public String getQRImage() {
        return QRImage;
    }

    public void setQRImage(String QRImage) {
        this.QRImage = QRImage;
    }

    public String getCustomerID() {
        return CustomerID;
    }

    public void setCustomerID(String customerID) {
        CustomerID = customerID;
    }

    @Override
    public String toString() {
        return "QRCode{" +
                "QRID='" + QRID + '\'' +
                ", QRImage='" + QRImage + '\'' +
                ", CustomerID='" + CustomerID + '\'' +
                '}';
    }
}
