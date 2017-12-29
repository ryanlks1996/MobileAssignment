package info.androidhive.fingerprint.Transaction;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import info.androidhive.fingerprint.HomeActivity;
import info.androidhive.fingerprint.LoginActivity;
import info.androidhive.fingerprint.Model.Customer.Customer;
import info.androidhive.fingerprint.Model.Transaction.Transaction;
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
        double targetAccountBal = 0;
        double amount = prefs.getFloat("amount", 0); //0 is the default value.

        List<Customer> customerList = LoginActivity.custList;
        for(int i=0; i<customerList.size(); i++) {
            if(customerList.get(i).getCustomerID().equals(targetCustomerID)){
                targetAccountBal = customerList.get(i).getAccountBalance();
                break;
            }
        }

        //validation already performed on Transaction1Activity
        accountBalance -= amount;
        targetAccountBal += amount;

        Transaction transfer = new Transaction();
        Transaction receive = new Transaction();
        Date date = new Date();
        transfer.setAmount((int) amount);
        receive.setAmount((int) amount);
        transfer.setTransactionDate(String.valueOf(date));
        receive.setTransactionDate(String.valueOf(date));

        try {
            String url = getApplicationContext().getString(R.string.insert_transaction_url);
            makeServiceCall(this, url, transfer);
            makeServiceCall(this, url, receive);
            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void makeServiceCall(Context context, String url, final Transaction transaction) {
        //mPostCommentResponse.requestStarted();
        RequestQueue queue = Volley.newRequestQueue(context);

        //Send data
        try {
            StringRequest postRequest = new StringRequest(
                    Request.Method.POST,
                    url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Toast.makeText(getApplicationContext(), "Account Created.", Toast.LENGTH_LONG).show();
                            finish();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), "Error. " + error.toString(), Toast.LENGTH_LONG).show();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("CustomerID", transaction.getCustomerID());
                    params.put("Amount", String.valueOf(transaction.getAmount()));
                    params.put("TransactionDate", transaction.getTransactionDate());
                    params.put("Charges", String.valueOf(transaction.getCharges()));
                    params.put("Type", transaction.getType());
                    params.put("CustomerID", transaction.getCustomerID());

                    return params;
                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("Content-Type", "application/x-www-form-urlencoded");
                    return params;
                }
            };
            queue.add(postRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isConnected() {
        ConnectivityManager cm =
                (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }
}
