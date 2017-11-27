package info.androidhive.fingerprint;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {
    private EditText editTextUs, editTextPassword;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextUs = (EditText)findViewById(R.id.editTextUs);
        editTextPassword = (EditText)findViewById(R.id.editTextPassword);
    }

    public void login(View v){
        intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }

    public void register(View v){
        intent = new Intent(this, Register1Activity.class);
        startActivity(intent);
    }



    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}
