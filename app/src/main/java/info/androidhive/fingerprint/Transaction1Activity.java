package info.androidhive.fingerprint;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class Transaction1Activity extends AppCompatActivity {
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

        editTextAmount = (EditText) findViewById(R.id.editTextAmount);
        textViewUsername = (TextView) findViewById(R.id.textViewUser);
        textViewMessage = (TextView) findViewById(R.id.textViewMessage);
    }

    public void scanQR(View v){
        Intent intent = new Intent(this, Transaction2Activity.class);
        startActivityForResult(intent, REQUEST_QRCODE);
    }
    public void fingerPrints(View v){
        Intent intent = new Intent(this, FingerprintActivity.class);
        startActivity(intent);
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

        try {
           double amount = Double.parseDouble(String.valueOf(editTextAmount.getText()));
        }catch(Exception e){
           textViewMessage.setText(""+e);
        }
        //Process transfer

        //Add transfer history

    }
}
