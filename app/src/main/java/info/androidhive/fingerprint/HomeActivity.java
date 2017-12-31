package info.androidhive.fingerprint;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.appdatasearch.GetRecentContextCall;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import info.androidhive.fingerprint.Model.Customer.Customer;
import info.androidhive.fingerprint.Model.Transaction.Transaction;
import info.androidhive.fingerprint.Transaction.Transaction1Activity;
import info.androidhive.fingerprint.Transaction.TransactionAdapter;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final String MY_PREFS_NAME = "MyPrefsFile";
    ListView listViewHistory;
    List<Transaction> transactionList;
    RequestQueue queue;
    String customerIDSession;
    public static List<Customer> custList;
    TextView textViewBalance;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        transactionList = new ArrayList<>();
        listViewHistory = (ListView) findViewById(R.id.listViewHistory);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        textViewBalance = (TextView) findViewById(R.id.textViewBalance);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        custList = new ArrayList<>();
        if (isConnected()) {

            SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
            customerIDSession = prefs.getString("customerID", "No value"); //No value is default value

            getCust(getApplicationContext(), getResources().getString(R.string.select_customer_url));
            downloadTransaction(getApplicationContext(), getString(R.string.select_transaction_url));

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

    private void displayAccountBalance() {
        int accBalance = 0;
        for (int i = 0; i < custList.size(); i++) {
            if (customerIDSession.equalsIgnoreCase(custList.get(i).getCustomerID()))
                accBalance = custList.get(i).getAccountBalance();
        }
        textViewBalance.setText(textViewBalance.getText() +" "+ String.valueOf(accBalance));
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


    private void downloadTransaction(Context context, String url) {
        // Instantiate the RequestQueue
        queue = Volley.newRequestQueue(context);


        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(
                url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            transactionList.clear();

                            for (int i = 0; i < response.length(); i++) {
                                JSONObject transactionResponse = (JSONObject) response.get(i);
                                String transactionID = transactionResponse.getString("TransactionID");
                                int amount = Integer.parseInt(transactionResponse.getString("Amount"));
                                String transactionDate = transactionResponse.getString("TransactionDate");
                                int charges = Integer.parseInt(transactionResponse.getString("Charges"));
                                String type = transactionResponse.getString("Type");
                                String customerID = transactionResponse.getString("CustomerID");
                                Transaction transaction = new Transaction(transactionID, transactionDate, customerID, type, amount, charges);

                                if (customerIDSession.equals(transaction.getCustomerID()))
                                    transactionList.add(transaction);

                            }
                            loadTransaction();

                        } catch (Exception e) {
                            Toast.makeText(getApplicationContext(), "Error:" + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(getApplicationContext(), "Error" + volleyError.getMessage(), Toast.LENGTH_LONG).show();

                    }
                });


        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest);
    }

    private void loadTransaction() {
        final TransactionAdapter adapter = new TransactionAdapter(this, R.layout.content_home, transactionList);
        listViewHistory.setAdapter(adapter);
        //  Toast.makeText(getApplicationContext(), "Count :" + transactionList.size(), Toast.LENGTH_LONG).show();
    }

    public void Transfer(View v) {
        Intent intent = new Intent(this, Transaction1Activity.class);
        startActivity(intent);
    }

    public void showQR(View v) {
        Intent intent = new Intent(this, ShowQRActivity.class);
        startActivity(intent);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Toast toast = new Toast(getApplicationContext());
            toast.makeText(getApplicationContext(), "Successfully log out.", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Intent intent;

        if (id == R.id.nav_Home) {

        } else if (id == R.id.nav_Transfer) {
            intent = new Intent(this, Transaction1Activity.class);
            startActivity(intent);
        } else if (id == R.id.nav_Topup) {
            intent = new Intent(this, TopupActivity.class);
            startActivity(intent);
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
