package info.androidhive.fingerprint;

import android.content.Intent;
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
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.text.Text;

import info.androidhive.fingerprint.Transaction.Transaction1Activity;

public class TopupActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private SeekBar seekBarAmount;
    private TextView textViewAmount, textViewAccountBalance;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topup);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        seekBarAmount = (SeekBar)findViewById(R.id.seekBarAmount);
        textViewAccountBalance = (TextView)findViewById(R.id.textViewAccountBalance);
        textViewAmount = (TextView)findViewById(R.id.textViewAmount);
        seekBarControl();
    }

    public void seekBarControl(){
        textViewAmount.setText(
                seekBarAmount.getProgress() +" / "+seekBarAmount.getMax());
        seekBarAmount.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener(){
                    int progress_value;
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                        progress_value = progress;
                        textViewAmount.setText(
                                progress +" / "+seekBarAmount.getMax());
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        textViewAmount.setText(
                                progress_value +" / "+seekBarAmount.getMax());
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

        }else if (id == R.id.nav_About) {
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
