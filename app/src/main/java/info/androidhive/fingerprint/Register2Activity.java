package info.androidhive.fingerprint;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class Register2Activity extends AppCompatActivity {
    private TextView textViewMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register2);

        textViewMessage = (TextView)findViewById(R.id.textViewMessage);
    }

    public void createAccount(View v){
        boolean valid = false;
        String errorMsg = "";
        //Check validation


        if(valid){
            //Create new data in database

        } else{
            //Prompt error message
            textViewMessage.setText(errorMsg);
        }
    }
}
