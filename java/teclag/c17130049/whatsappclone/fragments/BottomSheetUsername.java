package teclag.c17130049.whatsappclone.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import teclag.c17130049.whatsappclone.R;
import teclag.c17130049.whatsappclone.activities.ProfileActivity;
import teclag.c17130049.whatsappclone.providers.AuthProvider;
import teclag.c17130049.whatsappclone.providers.ImageProvider;
import teclag.c17130049.whatsappclone.providers.UsersProvider;

public class BottomSheetUsername extends BottomSheetDialogFragment {
    Button mButtonSave;
    Button mButtonCancel;
    EditText mEditTextUsername;

    ImageProvider mImageProvider;
    AuthProvider mAuthProvider;
    UsersProvider mUsersProvider;

    String username;

    public static BottomSheetUsername newInstance(String username) {
        BottomSheetUsername bottomSheetSelectImage = new BottomSheetUsername();
        Bundle args = new Bundle();
        args.putString("username", username);
        bottomSheetSelectImage.setArguments(args);
        return bottomSheetSelectImage;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        username = getArguments().getString("username");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.botton_sheet_username, container, false);
        mButtonSave = view.findViewById(R.id.btnSave);
        mButtonCancel = view.findViewById(R.id.btnCancel);
        mEditTextUsername = view.findViewById(R.id.editTextUsername);
        mEditTextUsername.setText(username);

        mImageProvider = new ImageProvider();
        mUsersProvider = new UsersProvider();
        mAuthProvider = new AuthProvider();
        mEditTextUsername.setText(username);

        mButtonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateUsername();
            }
        });

        mButtonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });




        return view;
    }

    private void updateUsername() {
        String username = mEditTextUsername.getText().toString();
        if (!username.equals("")) {
            mUsersProvider.updateUsername(mAuthProvider.getid(), username).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    dismiss();
                    Toast.makeText(getContext(), "El nombre de usuario se ha actualizo", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
