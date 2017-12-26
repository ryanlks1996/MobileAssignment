package info.androidhive.fingerprint.Register;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.text.Text;

import info.androidhive.fingerprint.LoginActivity;
import info.androidhive.fingerprint.R;

public class Register2Activity extends AppCompatActivity {
    TextView textViewUsername, textViewPassword, textViewGender, textViewEmail;
    protected static final int REQUEST_DETAILS = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register2);

        textViewUsername = (TextView)findViewById(R.id.textViewUsername);
        textViewEmail = (TextView)findViewById(R.id.textViewEmail);
        textViewGender = (TextView)findViewById(R.id.textViewGender);
        textViewPassword = (TextView)findViewById(R.id.textViewPassword);

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_DETAILS) {
            String username, gender, email, password;

            username = data.getStringExtra(Register1Activity.USERNAME);
            gender = data.getStringExtra(Register1Activity.GENDER);
            email = data.getStringExtra(Register1Activity.EMAIL);
            password = data.getStringExtra(Register1Activity.PASSWORD);

            textViewUsername.setText(username);
            textViewGender.setText(gender);
            textViewEmail.setText(email);
            textViewPassword.setText(password);
        }
    }

    public void edit(View v){
        Intent intent = new Intent(this, Register1Activity.class);
        startActivityForResult(intent,REQUEST_DETAILS);
    }

    public void next(View v){
        if(textViewUsername.getText()==""||
                textViewGender.getText()==""||
                textViewPassword.getText()==""||
                textViewEmail.getText()=="") {
            Toast toast = new Toast(getApplicationContext());
            toast.makeText(getApplicationContext(),"Please check if the details are valid.",Toast.LENGTH_SHORT);
        }else{
            //insert into database


            //display successful message (toast)
            Toast toast = new Toast(this);
            toast.makeText(this, "Successfully created account.", Toast.LENGTH_LONG).show();

            //return back to login screen
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
    }
}
