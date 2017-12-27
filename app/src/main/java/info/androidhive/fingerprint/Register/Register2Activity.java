package info.androidhive.fingerprint.Register;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.vision.text.Text;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import info.androidhive.fingerprint.LoginActivity;
import info.androidhive.fingerprint.Model.Customer.Customer;
import info.androidhive.fingerprint.R;

public class Register2Activity extends AppCompatActivity {
    TextView textVIewName, textViewUsername, textViewPassword, textViewGender, textViewEmail;
    protected static final int REQUEST_DETAILS = 5;
    private String name, username, gender, email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register2);

        textVIewName = (TextView)findViewById(R.id.textViewName);
        textViewUsername = (TextView)findViewById(R.id.textViewUsername);
        textViewEmail = (TextView)findViewById(R.id.textViewEmail);
        textViewGender = (TextView)findViewById(R.id.textViewGender);
        textViewPassword = (TextView)findViewById(R.id.textViewPassword);

        if (!isConnected()) {
            Toast.makeText(getApplicationContext(), "No network", Toast.LENGTH_LONG).show();
        }
    }

    private boolean isConnected() {
        ConnectivityManager cm =
                (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_DETAILS) {
            name = data.getStringExtra(Register1Activity.NAME);
            username = data.getStringExtra(Register1Activity.USERNAME);
            gender = data.getStringExtra(Register1Activity.GENDER);
            email = data.getStringExtra(Register1Activity.EMAIL);
            password = data.getStringExtra(Register1Activity.PASSWORD);

            textVIewName.setText(name);
            textViewUsername.setText(username);
            textViewGender.setText(gender);
            textViewEmail.setText(email);
            textViewPassword.setText(password);
        }
    }

    public void edit(View v){
        Intent intent = new Intent(this, Register1Activity.class);
        startActivityForResult(intent,REQUEST_DETAILS);
    }

    public void next(View v){
        Customer customer = new Customer();
        customer.setCustomerID("C0003");//temporaily hardcode
        customer.setName(name);
        customer.setAccountBalance(0);
        customer.setGender(gender);
        customer.setEmail(email);
        customer.setUsername(username);
        customer.setPassword(password);

        if(textVIewName.getText()==""||
                textViewUsername.getText()==""||
                textViewGender.getText()==""||
                textViewPassword.getText()==""||
                textViewEmail.getText()=="") {
            Toast toast = new Toast(getApplicationContext());
            toast.makeText(getApplicationContext(),"Please check if the details are valid.",Toast.LENGTH_SHORT);
        }else{
            //insert into database
            try {
                makeServiceCall(this, getString(R.string.insert_customer_url), customer);
                //display successful message (toast)
                Toast toast = new Toast(this);
                toast.makeText(this, "Successfully created account.", Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
            //return back to login screen
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
    }

    public void makeServiceCall(Context context, String url, final Customer customer) {
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
                            JSONObject jsonObject;
                            try {
                                jsonObject = new JSONObject(response);
                                int success = jsonObject.getInt("success");
                                String message = jsonObject.getString("message");
                                if (success==0) {
                                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                                }else{
                                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                                    finish();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
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
                    params.put("CustomerID", customer.getCustomerID());;
                    params.put("Name", customer.getName());;
                    params.put("AccountBalance", customer.getAccountBalance());;
                    params.put("Gender", customer.getGender());;
                    params.put("Email", customer.getEmail());;
                    params.put("Username", customer.getUsername());;
                    params.put("Password", customer.getPassword());
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
}
