package info.androidhive.fingerprint.Transaction;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import info.androidhive.fingerprint.R;

public class Transaction3Activity extends AppCompatActivity {
    public static final String MY_PREFS_NAME = "MyPrefsFile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction3);

        if (!isConnected()) {
            Toast.makeText(getApplicationContext(), "No network", Toast.LENGTH_LONG).show();
        }

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        String customerID =prefs.getString("customerID", "No value");
        String targetCustomerID =prefs.getString("targetCustomerID", "No value"); //No value is default value
        double accountBalance = prefs.getFloat("accountBalance",0);
        double amount = prefs.getFloat("amount", 0); //0 is the default value.


    }

    private boolean isConnected() {
        ConnectivityManager cm =
                (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();

    }
}
