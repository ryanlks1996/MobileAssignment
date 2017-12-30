package info.androidhive.fingerprint.Register;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import info.androidhive.fingerprint.LoginActivity;
import info.androidhive.fingerprint.Model.Customer.Customer;
import info.androidhive.fingerprint.R;

public class Register1Activity extends AppCompatActivity {

    protected static final String NAME = "Register1Activity.name";
    protected static final String USERNAME = "Register1Activity.username";
    protected static final String GENDER = "Register1Activity.gender";
    protected static final String EMAIL = "Register1Activity.email";
    protected static final String PASSWORD = "Register1Activity.password";
    List<Customer> custList;
    int custNo;

    private Intent intent;
    private EditText editTextName, editTextUs, editTextPw, editTextCfPw, editTextEmail;
    private RadioButton radioButtonMale, radioButtonFemale;
    ConnectivityManager connMgr;
    NetworkInfo networkInfo;
    Boolean isConnected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register1);
        //Make connection
        connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = connMgr.getActiveNetworkInfo();
        isConnected = networkInfo != null && networkInfo.isConnectedOrConnecting();
        custList = new ArrayList<>();

        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextUs = (EditText) findViewById(R.id.editTextUs);
        editTextPw = (EditText) findViewById(R.id.editTextPassword);
        editTextCfPw = (EditText) findViewById(R.id.editTextCfPw);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        radioButtonMale = (RadioButton) findViewById(R.id.radioButtonMale);
        radioButtonFemale = (RadioButton) findViewById(R.id.radioButtonFemale);


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

    public void checkDetails(View v) {
        //Check validation
        boolean valid = false;
        String errorMsg = "";
        String name = "", username = "", gender = "", email = "", password = "", confirmPw = "";
        int dbLength = 10;//get database length

        name = editTextName.getText().toString();
        if (name.isEmpty() || name.equals("")) {
            errorMsg = "Please enter your name.";
        } else {
            username = editTextUs.getText().toString();
            boolean usernameRepeat = false;

            //check all usernames in database
            for (int i = 0; i < custList.size(); i++) {
                if (username.equals(custList.get(i).getUsername())) {
                    usernameRepeat = true;
                    break;
                }
            }
            if (username.isEmpty() || username.equals("")) {
                errorMsg = "Please enter username.";
            } else if (username.contains(" ")) {
                errorMsg = "Username shouldn't contains space(s)";
            } else if (username.length() < 8 || username.length() > 14) {
                errorMsg = "Username length must between 8-14";
            } else if (usernameRepeat) {
                errorMsg = "This username is already been used.";
            } else {
                if (!(radioButtonMale.isChecked() || radioButtonFemale.isChecked())) {
                    //Error, please choose gender
                    errorMsg = "Please choose your gender";
                } else {
                    email = editTextEmail.getText().toString();
                    boolean emailRepeat = false;

                    //check all emails in database
                    for (int i = 0; i < custList.size(); i++) {
                        if (email.equals(custList.get(i).getEmail())) {
                            emailRepeat = true;
                            break;
                        }
                    }
                    if (email.isEmpty() || email.equals("")) {
                        errorMsg = "Please enter email.";
                    } else if (email.length() > 25) {
                        errorMsg = "Invalid email length. (Too long)";
                    } else if (email.contains(" ")) {
                        errorMsg = "Email shouldn't contains space(s)";
                    } else if (emailRepeat) {
                        errorMsg = "This email is already registered.";
                    } else {
                        password = editTextPw.getText().toString();
                        confirmPw = editTextCfPw.getText().toString();
                        if (password.isEmpty() || username.equals("")) {
                            errorMsg = "Please enter password.";
                        } else if (password.contains(" ")) {
                            errorMsg = "Password shouldn't contains space(s)";
                        } else if (password.length() < 8 || password.length() > 14) {
                            errorMsg = "Password length must between 8-14";
                        } else if (!(password.equals(confirmPw))) {
                            errorMsg = "Confirm Password is not same with password.";
                        } else {
                            if (radioButtonMale.isChecked())
                                gender = "Male";
                            if (radioButtonFemale.isChecked())
                                gender = "Female";
                            valid = true;
                        }
                    }
                }
            }
        }
        if (valid) {

            String cID;
            String idPrefix = "C"; //set prefix of customerID
            //Continue the index from last customer
            cID = idPrefix + String.format("%4d", custList.size() + 1001);



            Customer cust = new Customer();
            cust.setCustomerID(cID);
            cust.setName(name);
            cust.setGender(gender);
            cust.setEmail(email);
            cust.setUsername(username);
            cust.setPassword(password);

            try {
                String url = getApplicationContext().getString(R.string.insert_customer_url);
                makeServiceCall(this, url, cust);
                intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        } else {
            //Display error message
            Toast toast = new Toast(this);
            toast.makeText(this, errorMsg, Toast.LENGTH_LONG).show();
        }
    }

    public void makeServiceCall(Context context, String url, final Customer cust) {
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


                    params.put("CustomerID", cust.getCustomerID());
                    params.put("Name", cust.getName());
                    params.put("AccountBalance", String.valueOf(cust.getAccountBalance()));
                    params.put("Gender", cust.getGender());
                    params.put("Email", cust.getEmail());
                    params.put("Username", cust.getUsername());
                    params.put("Password", cust.getPassword());


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




    public void reset(View v) {
        editTextName.setText("");
        editTextUs.setText("");
        editTextPw.setText("");
        editTextCfPw.setText("");
        editTextEmail.setText("");
        radioButtonMale.setChecked(false);
        radioButtonFemale.setChecked(false);
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
