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
import com.google.firebase.database.DatabaseException;

import java.text.SimpleDateFormat;
import java.util.Calendar;
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
        int targetAccountBal = 0;
        int amount = prefs.getInt("amount", 0); //0 is the default value.
        int charges = 1; //1 dollar of service fee will be charged
        int accountBalance = 0;

        List<Customer> customerList = LoginActivity.custList;
        for(int i=0; i<customerList.size(); i++) {
            if(customerList.get(i).getCustomerID().equals(targetCustomerID)){
                targetAccountBal = customerList.get(i).getAccountBalance();
                break;
            }
        }

        for(int i=0;i<customerList.size();i++){
            if(customerList.get(i).getCustomerID().equals(customerID)){
                accountBalance = customerList.get(i).getAccountBalance();
                break;
            }
        }

        //validation already performed on Transaction1Activity
        accountBalance -= (amount+charges);
        targetAccountBal += amount;

        Transaction transfer = new Transaction();
        Transaction receive = new Transaction();
        String date = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());

        transfer.setAmount(amount);
        receive.setAmount(amount);
        transfer.setTransactionDate(date);
        receive.setTransactionDate(date);
        transfer.setCharges(charges);
        receive.setCharges(charges);
        transfer.setType("Transferred");
        receive.setType("Received");
        transfer.setCustomerID(customerID);
        receive.setCustomerID(targetCustomerID);

        try {
            String url = getApplicationContext().getString(R.string.insert_transaction_url);
            makeServiceCallTransaction(this, url, transfer);
            makeServiceCallTransaction(this, url, receive);
            url = getApplicationContext().getString(R.string.update_customer_url);
            makeServiceCallCustomer(this, url, customerID, accountBalance);
            makeServiceCallCustomer(this, url, targetCustomerID, targetAccountBal);

            //update customer list
            for(int i=0; i<customerList.size(); i++){
                if(customerList.get(i).getCustomerID().equals(customerID)){
                    customerList.get(i).setAccountBalance(accountBalance);
                }
                if(customerList.get(i).getCustomerID().equals(targetCustomerID)){
                    customerList.get(i).setAccountBalance(targetAccountBal);
                }
            }
            LoginActivity.custList = customerList;

            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void makeServiceCallTransaction(Context context, String url, final Transaction transaction) {
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
                            Toast.makeText(getApplicationContext(), "Successfully transferred.", Toast.LENGTH_LONG).show();
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

    public void makeServiceCallCustomer(Context context, String url, final String customerID, final int balance) {
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
                            Toast.makeText(getApplicationContext(), "Successfully updated customer details.", Toast.LENGTH_LONG).show();
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
                    params.put("AccountBalance", String.valueOf(balance));
                    params.put("CustomerID", customerID);

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
