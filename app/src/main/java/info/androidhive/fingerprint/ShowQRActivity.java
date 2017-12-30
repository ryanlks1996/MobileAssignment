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
import android.widget.ImageView;
import android.widget.ListView;
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
import java.util.List;
import java.util.Map;

import info.androidhive.fingerprint.Model.Customer.Customer;
import info.androidhive.fingerprint.Model.QRCode.QRAdapter;
import info.androidhive.fingerprint.Model.QRCode.QRCode;
import info.androidhive.fingerprint.Model.Transaction.Transaction;
import info.androidhive.fingerprint.Transaction.Transaction1Activity;
import info.androidhive.fingerprint.Transaction.TransactionAdapter;

public class ShowQRActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    public static final String MY_PREFS_NAME = "MyPrefsFile";
    ListView listViewQRImg;
    List<QRCode> qrImageList;
    RequestQueue queue;
    String customerIDSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_qr);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        qrImageList= new ArrayList<>();
        listViewQRImg = (ListView) findViewById(R.id.listViewQRImg);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if (isConnected()) {
            downloadQRImage(getApplicationContext(), getString(R.string.select_qr_url));
        }else{
            Toast.makeText(getApplicationContext(), "No network", Toast.LENGTH_LONG).show();
        }


    }

    private boolean isConnected() {
        ConnectivityManager cm =
                (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();

    }

    private void downloadQRImage(Context context, String url) {
        // Instantiate the RequestQueue
        queue = Volley.newRequestQueue(context);


        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        customerIDSession =prefs.getString("customerID", "No value"); //No value is default value

        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(
                url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            qrImageList.clear();

                            for (int i = 0; i < response.length(); i++) {
                                JSONObject qrResponse = (JSONObject) response.get(i);
                                String qrID = qrResponse.getString("QRID");
                                String qrImage = qrResponse.getString("QRImage");
                                String customerID = qrResponse.getString("CustomerID");
                                QRCode qrCode = new QRCode(qrID, qrImage, customerID);

                               if(customerIDSession.equals(qrCode.getCustomerID()))
                                    qrImageList.add(qrCode);

                            }
                            loadQRImage();

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

    private void loadQRImage() {
        final QRAdapter adapter = new QRAdapter(this, R.layout.content_show_qr, qrImageList);
        listViewQRImg.setAdapter(adapter);

       // Toast.makeText(getApplicationContext(), "Count :" + qrImageList.size(), Toast.LENGTH_LONG).show();
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
        getMenuInflater().inflate(R.menu.show_qr, menu);
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
            intent = new Intent(this, Transaction1Activity.class);
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

    public void returns(View v) {
        super.onBackPressed();
    }
}
