package info.androidhive.fingerprint;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;

public class Register1Activity extends AppCompatActivity {
    private Intent intent;
    private EditText editTextUs, editTextPw, editTextCfPw, editTextEmail;
    private RadioButton radioButtonMale, radioButtonFemale;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register1);

        editTextUs = (EditText)findViewById(R.id.editTextUs);
        editTextPw = (EditText)findViewById(R.id.editTextPassword);
        editTextCfPw = (EditText)findViewById(R.id.editTextCfPw);
        editTextEmail = (EditText)findViewById(R.id.editTextEmail);
        radioButtonMale = (RadioButton)findViewById(R.id.radioButtonMale);
        radioButtonFemale = (RadioButton)findViewById(R.id.radioButtonFemale);
    }

    public void next(View v){
        intent = new Intent(this, Register2Activity.class);
        startActivity(intent);
    }

    public void reset(View v){
        editTextUs.setText("");
        editTextPw.setText("");
        editTextCfPw.setText("");
        editTextEmail.setText("");
        radioButtonMale.setChecked(false);
        radioButtonFemale.setChecked(false);
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
