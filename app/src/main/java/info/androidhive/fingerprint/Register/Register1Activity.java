package info.androidhive.fingerprint.Register;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import info.androidhive.fingerprint.R;

public class Register1Activity extends AppCompatActivity {
    protected static final String USERNAME = "Register1Activity.username";
    protected static final String GENDER = "Register1Activity.gender";
    protected static final String EMAIL = "Register1Activity.email";
    protected static final String PASSWORD = "Register1Activity.password";
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

    public void checkDetails(View v){
        //Check validation
        boolean valid=false;
        String errorMsg="";
        String username="",gender="",email="",password="",confirmPw="";
        int dbLength = 10;//get database length

        username = editTextUs.getText().toString();
        boolean usernameRepeat = false;
        /*
        //check all usernames in database
        for(int i=0;i<dbLength;i++){
            if(username.equals()){
                usernameRepeat=true;
                break;
            }
        }
        */
        if(username.isEmpty()||username.equals("")){
            errorMsg = "Please enter username.";
        }
        else if(username.contains(" ")){
            errorMsg = "Username shouldn't contains space(s)";
        }else if(username.length()<8 || username.length()>14){
            errorMsg = "Username length must between 8-14";
        }else if(usernameRepeat){
            errorMsg = "This username is already been used.";
        }else {
            if(!(radioButtonMale.isChecked() || radioButtonFemale.isChecked())){
                //Error, please choose gender
                errorMsg = "Please choose your gender";
            } else{
                email = editTextEmail.getText().toString();
                boolean emailRepeat = false;
                /*
                //check all emails in database
                for(int i=0;i<dbLength;i++){
                    if(email.equals()){
                        emailRepeat = true;
                        break;
                    }
                }
                */
                if(email.isEmpty() || email.equals("")){
                    errorMsg = "Please enter email.";
                }else if(email.length() > 25){
                    errorMsg = "Invalid email length. (Too long)";
                }else if(email.contains(" ")){
                    errorMsg = "Email shouldn't contains space(s)";
                }else if(emailRepeat){
                    errorMsg = "This email is already registered.";
                }else {
                    password = editTextPw.getText().toString();
                    confirmPw = editTextCfPw.getText().toString();
                    if(password.isEmpty()||username.equals("")){
                        errorMsg = "Please enter password.";
                    }
                    else if(password.contains(" ")){
                        errorMsg = "Password shouldn't contains space(s)";
                    }else if(password.length()<8 || password.length()>14){
                        errorMsg = "Password length must between 8-14";
                    }else if(!(password.equals(confirmPw))) {
                        errorMsg = "Confirm Password is not same with password.";
                    }else{
                        if(radioButtonMale.isChecked())
                            gender="Male";
                        if(radioButtonFemale.isChecked())
                            gender="Female";
                        valid = true;
                    }
                }
            }
        }
        if(valid) {
            intent = new Intent();
            intent.putExtra(USERNAME,username);
            intent.putExtra(GENDER,gender);
            intent.putExtra(EMAIL,email);
            intent.putExtra(PASSWORD,password);
            setResult(Register2Activity.REQUEST_DETAILS,intent);
            finish();
        }else{
            //Display error message
            Toast toast = new Toast(this);
            toast.makeText(this,errorMsg,Toast.LENGTH_LONG).show();
        }
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
