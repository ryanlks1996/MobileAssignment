package info.androidhive.fingerprint;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;

import static android.app.Activity.RESULT_OK;

public class RegisterFragment extends Fragment {
    public static final int REQUEST_ = 1;

    private Intent intent;
    private EditText editTextUs, editTextPw, editTextCfPw, editTextEmail;
    private RadioButton radioButtonMale, radioButtonFemale;

    public RegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_register, container, false);

        editTextUs = (EditText)v.findViewById(R.id.editTextUs);
        editTextPw = (EditText)v.findViewById(R.id.editTextPassword);
        editTextCfPw = (EditText)v.findViewById(R.id.editTextCfPw);
        editTextEmail = (EditText)v.findViewById(R.id.editTextEmail);
        radioButtonMale = (RadioButton)v.findViewById(R.id.radioButtonMale);
        radioButtonFemale = (RadioButton)v.findViewById(R.id.radioButtonFemale);
        return v;
    }

    public void next(View v){
        /*
        intent = new Intent(this, Register2Activity.class);
        startActivity(intent);
        */
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_ && resultCode == RESULT_OK){

        }
    }
}
