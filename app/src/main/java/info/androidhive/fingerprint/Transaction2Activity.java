package info.androidhive.fingerprint;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class Transaction2Activity extends AppCompatActivity {
    private EditText editTextAmount;
    private TextView textViewUsername;
    public static final int REQUEST_QRCODE = 1;
    public static final String USERNAME = "Username from QR Code";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        editTextAmount = (EditText) findViewById(R.id.editTextAmount);
        textViewUsername = (TextView) findViewById(R.id.textViewUser);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    public void scanQR(View v){
        Intent intent = new Intent(this, Transaction1Activity.class);
        startActivityForResult(intent, REQUEST_QRCODE);
    }
    public void fingerPrints(View v){
        Intent intent = new Intent(this, FingerprintActivity.class);
        startActivityForResult(intent, REQUEST_QRCODE);
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
