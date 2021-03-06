package info.androidhive.fingerprint;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import info.androidhive.fingerprint.Transaction.Transaction1Activity;

public class AboutActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        getVersion();
    }

    public void getVersion(){

        String androidOS = Build.VERSION.RELEASE;


        TextView tvVersion,textViewTOS, textViewFAQ, textViewWeblink;
        textViewTOS = (TextView) findViewById(R.id.textViewTOS);
        textViewFAQ = (TextView) findViewById(R.id.textViewFAQ);
        textViewWeblink = (TextView) findViewById(R.id.textViewWebLink);

        textViewTOS.setMovementMethod(LinkMovementMethod.getInstance());
        textViewFAQ.setMovementMethod(LinkMovementMethod.getInstance());
        textViewWeblink.setMovementMethod(LinkMovementMethod.getInstance());

        tvVersion = (TextView) findViewById(R.id.tvVersion);
        tvVersion.setText(androidOS);

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
        getMenuInflater().inflate(R.menu.about, menu);
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
        } else if (id == R.id.nav_Topup){
            intent = new Intent(this, TopupActivity.class);
            startActivity(intent);
        }else if (id == R.id.nav_About) {

        } else if (id == R.id.nav_Logout) {
            intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
