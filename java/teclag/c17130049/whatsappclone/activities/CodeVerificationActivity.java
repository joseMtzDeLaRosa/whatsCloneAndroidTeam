package teclag.c17130049.whatsappclone.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentSnapshot;

import teclag.c17130049.whatsappclone.R;
import teclag.c17130049.whatsappclone.models.User;
import teclag.c17130049.whatsappclone.providers.AuthProvider;
import teclag.c17130049.whatsappclone.providers.UsersProvider;

public class CodeVerificationActivity extends AppCompatActivity {

    Button mButtonCodeVerfication;
    EditText mEditTextCode;
    TextView mTextViewSMS;
    ProgressBar mProgressBar;

    String mExtraPhone;
    String mVerificationId;

    AuthProvider mAuthProvider;
    UsersProvider mUsersProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code_verification);

        mButtonCodeVerfication = findViewById(R.id.btnCodeVerification);
        mEditTextCode = findViewById(R.id.editTextCodeVerification);
        mTextViewSMS = findViewById(R.id.textViewSMS);
        mProgressBar = findViewById(R.id.progressBar);

        mAuthProvider = new AuthProvider();
        mUsersProvider = new UsersProvider();

        mExtraPhone = getIntent().getStringExtra("phone");

        mAuthProvider.sendCodeVerification(mExtraPhone, mCallbacks);

        mButtonCodeVerfication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String code = mEditTextCode.getText().toString();
                if (!code.equals("") && code.length() >= 6) {
                    signIn(code);
                }
                else {
                    Toast.makeText(CodeVerificationActivity.this, "Ingresa el codigo", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

            mProgressBar.setVisibility(View.GONE);
            mTextViewSMS.setVisibility(View.GONE);

            String code = phoneAuthCredential.getSmsCode();

            if (code != null) {
                mEditTextCode.setText(code);
                signIn(code);
            }

        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            mProgressBar.setVisibility(View.GONE);
            mTextViewSMS.setVisibility(View.GONE);

            Toast.makeText(CodeVerificationActivity.this, "Se produjo un error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(verificationId, forceResendingToken);
            Toast.makeText(CodeVerificationActivity.this, "El codigo se envio", Toast.LENGTH_SHORT).show();
            mVerificationId = verificationId;
        }
    };

    private void signIn(String code) {
        mAuthProvider.signInPhone(mVerificationId, code).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    final User user = new User();
                    user.setId(mAuthProvider.getid());
                    user.setPhone(mExtraPhone);

                    mUsersProvider.getUserInfo(mAuthProvider.getid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (!documentSnapshot.exists()) {
                                mUsersProvider.create(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        goToCompleteInfo();
                                    }
                                });
                            }
                            else if(documentSnapshot.contains("username") && documentSnapshot.contains("image")){
                                String username = documentSnapshot.getString("username");
                                String image = documentSnapshot.getString("image");

                                    if (username != null && image !=null) {
                                        if (!username.equals("") && !image.equals("")) {
                                            goToHomeActivity();
                                        }
                                        else{
                                            goToCompleteInfo();
                                        }
                                    }
                                    else{
                                        goToCompleteInfo();
                                    }
                            }
                            else{
                                goToCompleteInfo();
                            }
                        }
                    });

                }
                else {
                    Toast.makeText(CodeVerificationActivity.this, "No se pudo autenticar al usuario", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void goToHomeActivity() {
        Intent intent = new Intent(CodeVerificationActivity.this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void goToCompleteInfo() {
        Intent intent = new Intent(CodeVerificationActivity.this, CompleteInfoActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


}