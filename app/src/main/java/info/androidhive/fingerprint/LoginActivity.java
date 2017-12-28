package info.androidhive.fingerprint;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import info.androidhive.fingerprint.Model.Customer.Customer;
import info.androidhive.fingerprint.Register.Register1Activity;

public class LoginActivity extends AppCompatActivity {
    ConnectivityManager connMgr;
    NetworkInfo networkInfo;
    Boolean isConnected;

    private EditText editTextUs, editTextPassword;
    private TextView textViewMessage;
    private Intent intent;
    List<Customer> custList;
    int custNo;
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = connMgr.getActiveNetworkInfo();
        isConnected = networkInfo != null && networkInfo.isConnectedOrConnecting();
        custList = new ArrayList<>();

        editTextUs = (EditText) findViewById(R.id.editTextUs);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        textViewMessage = (TextView) findViewById(R.id.textViewMessage);




        readCustomer();
    }

    private void readCustomer() {
        try {
            // Check availability of network connection.
            if (isConnected) {

                getCust(getApplicationContext(), getResources().getString(R.string.get_customer_url));
            } else {
                Toast.makeText(getApplicationContext(), "Network is NOT available",
                        Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(),
                    "Error reading record:" + e.getMessage(),
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void getCust(Context context, String url) {
        //mPostCommentResponse.requestStarted();

        RequestQueue queue = Volley.newRequestQueue(context);

        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {


                        try {
                            //Clear list
                            custList.clear();

                            for (int i = 0; i < response.length(); i++) {
                                JSONObject recordResponse = (JSONObject) response.get(i);
                                String custID = recordResponse.getString("CustomerID");
                                String name = recordResponse.getString("Name");
                                int accountBalance = Integer.parseInt(recordResponse.getString("AccountBalance"));
                                String gender = recordResponse.getString("Gender");
                                String email = recordResponse.getString("Email");
                                String username = recordResponse.getString("Username");
                                String password = recordResponse.getString("Password");

                                Customer cust = new Customer(custID, name, gender, email, username, password, accountBalance);
                                custList.add(cust);

                            }


                        } catch (Exception e) {
                            Toast.makeText(getApplicationContext(), "Error 1:" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(getApplicationContext(), "Error 2:" + volleyError.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
        queue.add(jsonObjectRequest);

    }

    public void login(View v) {
        boolean valid = true;//true for testing phase ONLY

        String username, password;
        username = String.valueOf(editTextUs.getText());
        password = String.valueOf(editTextPassword.getText());
        //Check record in database
        for (int i = 0; i < custList.size(); i++) {
            if (custList.get(i).getUsername().equalsIgnoreCase(username) && custList.get(i).getPassword().equalsIgnoreCase(password)) {
                custNo =i;
                valid = true;

            }else
                valid=false;
        }

        if (valid) {
            Toast.makeText(getApplicationContext(), "Log In Successfully", Toast.LENGTH_SHORT).show();

            //putting ID and accountBalance into sharepreference
            SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
            editor.putString("customerID", custList.get(custNo).getCustomerID());
            editor.putInt("accountBalance", custList.get(custNo).getAccountBalance());
            editor.apply();

            intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(getApplicationContext(), "Invalid Username or Password!", Toast.LENGTH_SHORT).show();

        }
    }


    public void register(View v) {
        intent = new Intent(this, Register1Activity.class);
        startActivity(intent);
    }


    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}
