package info.androidhive.fingerprint.Register;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import info.androidhive.fingerprint.R;

public class Register2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        String username, gender, email, password;

        username = data.getStringExtra("Register1Activity.username");
        gender = data.getStringExtra("Register1Activity.gender");
        email = data.getStringExtra("Register1Activity.email");
        password = data.getStringExtra("Register1Activity.password");
    }
}
