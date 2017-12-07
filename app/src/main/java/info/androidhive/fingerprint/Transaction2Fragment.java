package info.androidhive.fingerprint;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import static android.app.Activity.RESULT_OK;

public class Transaction2Fragment extends Fragment {
    private EditText editTextAmount;
    private TextView textViewUsername;
    public static final String USERNAME = "Username from QR Code";
    public static final int REQUEST_QRCODE = 1;
    public Transaction2Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_transaction2, container, false);
        editTextAmount = (EditText) v.findViewById(R.id.editTextAmount);
        textViewUsername = (TextView) v.findViewById(R.id.textViewUser);
        return v;
    }

    public void scanQR(View v){
        /*
        Intent intent = new Intent(this, Transaction1Activity.class);
        startActivityForResult(intent, REQUEST_QRCODE);
        */
    }
    public void fingerPrints(View v){
        /*
        Intent intent = new Intent(this, FingerprintActivity.class);
        startActivityForResult(intent, REQUEST_QRCODE);
        */
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_QRCODE)
        {
            String username;
            username = data.getStringExtra(USERNAME);

            textViewUsername.setText(username);
        }
    }
}
