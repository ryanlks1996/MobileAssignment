package info.androidhive.fingerprint.Transaction;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import info.androidhive.fingerprint.AboutActivity;
import info.androidhive.fingerprint.FingerprintActivity;
import info.androidhive.fingerprint.HomeActivity;
import info.androidhive.fingerprint.LoginActivity;
import info.androidhive.fingerprint.Model.Customer.Customer;
import info.androidhive.fingerprint.R;
import info.androidhive.fingerprint.TopupActivity;

public class Transaction1Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private EditText editTextAmount;
    private TextView textViewUsername, textViewMessage;
    public static final int REQUEST_QRCODE = 1;
    public static final String USERNAME = "Username from QR Code";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction1);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        editTextAmount = (EditText) findViewById(R.id.editTextAmount);
        textViewUsername = (TextView) findViewById(R.id.textViewUser);
        textViewMessage = (TextView) findViewById(R.id.textViewMessage);

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

    public void scanQR(View v){
        Intent intent = new Intent(this, Transaction2Activity.class);
        startActivityForResult(intent, REQUEST_QRCODE);
    }
    public void fingerPrints(View v){
        //check if username is valid
        boolean validUsername = false; //false until found username in database
        boolean validAmount = true; //true until exception occurred
        String username  = (String) textViewUsername.getText();
        String targetCustomerID=""; //target customer to transfer

        List<Customer> customerList = LoginActivity.custList;
        for(int i=0;i<customerList.size();i++) {
            if(customerList.get(i).getUsername().equals(username)){
                targetCustomerID = customerList.get(i).getCustomerID();
                validUsername = true;
                break;
            }
        }

        String errorMsg = "";
        if(!validUsername){
            errorMsg += " Invalid Username.";
            textViewMessage.setText(errorMsg);
        }else {
            try {
                double amount = Double.parseDouble(String.valueOf(editTextAmount.getText()));
            } catch (Exception e) {
                validAmount = false;
                errorMsg += " Invalid Amount.";
                textViewMessage.setText(errorMsg);
            }
            if(validUsername && validAmount){
                //pass variables into pref
                SharedPreferences.Editor editor = getSharedPreferences(LoginActivity.MY_PREFS_NAME, MODE_PRIVATE).edit();
                editor.putString("customerID", targetCustomerID);
                editor.apply();

                //start activity
                Intent intent = new Intent(this, FingerprintActivity.class);
                startActivity(intent);
            }
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(android.support.v4.view.GravityCompat.START)) {
            drawer.closeDrawer(android.support.v4.view.GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.transaction1, menu);
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
            intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_Transfer) {

        } else if (id == R.id.nav_Topup){
            intent = new Intent(this, TopupActivity.class);
            startActivity(intent);
        }else if (id == R.id.nav_About) {
            intent = new Intent(this, AboutActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_Logout) {
            intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(android.support.v4.view.GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_QRCODE)
        {
            String username;
            username = data.getStringExtra(USERNAME);

            textViewUsername.setText(username);
        }
    }
}
