package info.androidhive.fingerprint;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

public class Transaction2Activity extends AppCompatActivity {
    EditText editTextAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction2);

        editTextAmount = (EditText)findViewById(R.id.editTextAmount);
    }
}
