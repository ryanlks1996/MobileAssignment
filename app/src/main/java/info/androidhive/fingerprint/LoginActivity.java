package info.androidhive.fingerprint;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import info.androidhive.fingerprint.Register.Register1Activity;
import info.androidhive.fingerprint.Register.Register2Activity;

public class LoginActivity extends AppCompatActivity {
    private EditText editTextUs, editTextPassword;
    private TextView textViewMessage;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextUs = (EditText)findViewById(R.id.editTextUs);
        editTextPassword = (EditText)findViewById(R.id.editTextPassword);
        textViewMessage = (TextView)findViewById(R.id.textViewMessage);
    }

    public void login(View v){
        boolean valid= true;//true for testing phase ONLY
        String username, password;
        username = String.valueOf(editTextUs.getText());
        password = String.valueOf(editTextPassword.getText());
        //Check record in database


        if(valid){
            intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
        }else{
            textViewMessage.setText("Invalid username or password.");
        }
    }

    public void register(View v){
        intent = new Intent(this, Register2Activity.class);
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
