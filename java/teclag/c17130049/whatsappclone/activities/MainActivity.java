package teclag.c17130049.whatsappclone.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hbb20.CountryCodePicker;

import teclag.c17130049.whatsappclone.R;
import teclag.c17130049.whatsappclone.providers.AuthProvider;

public class MainActivity extends AppCompatActivity {
    Button mButtonSendCode;
    EditText mEditTextPhone;
    CountryCodePicker mCountryCode;

    AuthProvider mAuthProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mButtonSendCode = findViewById(R.id.btnSendCode);
        mEditTextPhone = findViewById(R.id.editTextPhone);
        mCountryCode = findViewById(R.id.ccp);

        mAuthProvider = new AuthProvider();

        mButtonSendCode.setOnClickListener(view -> {
            //goToCodeVerificationActivity();
            getData();
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
    if (mAuthProvider.getSessionUser()!= null){
        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
    }

    private void getData() {
        String code = mCountryCode.getSelectedCountryCodeWithPlus();
        String phone = mEditTextPhone.getText().toString();

        if (phone.equals("")) {
            Toast.makeText(this, "debe insertar el telefono", Toast.LENGTH_SHORT).show();
        }
        else {
            goToCodeVerificationActivity(code + phone);
        }
    }

    private void goToCodeVerificationActivity(String phone) {

        Intent intent = new Intent(MainActivity.this, CodeVerificationActivity.class);
        intent.putExtra("phone", phone);
        startActivity(intent);

    }
}

