package info.androidhive.fingerprint;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.vision.text.Text;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import info.androidhive.fingerprint.Model.Customer.Customer;
import info.androidhive.fingerprint.Model.Transaction.Transaction;
import info.androidhive.fingerprint.Transaction.Transaction1Activity;

public class TopupActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private SeekBar seekBarAmount;
    private TextView textViewAmount,textViewTopupAccBlc;

    public static List<Customer> custList;
    public static final String MY_PREFS_NAME = "MyPrefsFile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topup);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        custList = new ArrayList<>();

        textViewTopupAccBlc = (TextView) findViewById(R.id.tvTopupAccBlc) ;
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //add top up action

                topupAction();
            }
        });

        getCust(getApplicationContext(), getResources().getString(R.string.select_customer_url));
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        seekBarAmount = (SeekBar) findViewById(R.id.seekBarAmount);

        textViewAmount = (TextView) findViewById(R.id.textViewAmount);
        seekBarControl();


    }
    private void topupAction(){
        if (isConnected()) {
            SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
            String customerID = prefs.getString("customerID", "No value");
            int amount = seekBarAmount.getProgress();
            int charges = 1;
            int accountBalance = 0;

            HomeActivity homeActivity = new HomeActivity();
            List<Customer> customerList = homeActivity.custList;

            for (int i = 0; i < customerList.size(); i++) {
                if (customerList.get(i).getCustomerID().equals(customerID)) {
                    accountBalance = customerList.get(i).getAccountBalance();
                    break;
                }
            }
            accountBalance += (amount - charges);
            Transaction topUp = new Transaction();
            String date = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
            topUp.setAmount(amount);
            topUp.setTransactionDate(date);
            topUp.setCharges(charges);
            topUp.setType("TopUp");
            topUp.setCustomerID(customerID);

            try {
                String url = getApplicationContext().getString(R.string.insert_transaction_url);
                makeServiceCallTransaction(this, url, topUp);

                url = getApplicationContext().getString(R.string.update_customer_url);
                makeServiceCallCustomer(this, url, customerID, accountBalance);


                //update customer list
                for (int i = 0; i < customerList.size(); i++) {
                    if (customerList.get(i).getCustomerID().equals(customerID)) {
                        customerList.get(i).setAccountBalance(accountBalance);
                    }
                }
                homeActivity.custList = customerList;

                Intent intent = new Intent(getApplicationContext(), TopupActivity.class);
                startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }


        } else {
            Toast.makeText(getApplicationContext(), "No network", Toast.LENGTH_LONG).show();
        }
    }

    private boolean isConnected() {
        ConnectivityManager cm =
                (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
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
                            Toast.makeText(getApplicationContext(), "Successfully topup. Amount has been deducted from credit card", Toast.LENGTH_LONG).show();
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
                          //  Toast.makeText(getApplicationContext(), "Successfully updated customer details.", Toast.LENGTH_LONG).show();
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

    public void getCust(Context context, String url) {

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

                            displayAccountBalance();

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
    private void displayAccountBalance() {
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        String customerID = prefs.getString("customerID", "No value");
        int accBalance = 0;
        for (int i = 0; i < custList.size(); i++) {
            if (customerID.equalsIgnoreCase(custList.get(i).getCustomerID()))
                accBalance = custList.get(i).getAccountBalance();
        }
        textViewTopupAccBlc.setText(getString(R.string.account_balance) +" "+ String.valueOf(accBalance));
    }


    public void seekBarControl() {
        textViewAmount.setText(
                seekBarAmount.getProgress() + " / " + seekBarAmount.getMax());
        seekBarAmount.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    int progress_value;

                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                        progress_value = progress;
                        textViewAmount.setText(
                                progress + " / " + seekBarAmount.getMax());
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        textViewAmount.setText(
                                progress_value + " / " + seekBarAmount.getMax());
                    }
                }
        );
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.topup, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_refreshTopup) {
            getCust(getApplicationContext(), getResources().getString(R.string.select_customer_url));

            Toast.makeText(getApplicationContext(), "Page Refreshed.", Toast.LENGTH_LONG).show();
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Intent intent;
        if (id == R.id.nav_Home) {
            intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_Transfer) {
            intent = new Intent(this, Transaction1Activity.class);
            startActivity(intent);
        } else if (id == R.id.nav_Topup) {

        } else if (id == R.id.nav_About) {
            intent = new Intent(this, AboutActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_Logout) {
            intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
