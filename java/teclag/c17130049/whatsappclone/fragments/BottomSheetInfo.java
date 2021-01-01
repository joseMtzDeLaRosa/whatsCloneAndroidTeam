package teclag.c17130049.whatsappclone.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import teclag.c17130049.whatsappclone.R;
import teclag.c17130049.whatsappclone.providers.AuthProvider;
import teclag.c17130049.whatsappclone.providers.ImageProvider;
import teclag.c17130049.whatsappclone.providers.UsersProvider;

public class BottomSheetInfo extends BottomSheetDialogFragment {
    Button mButtonSave;
    Button mButtonCancel;
    EditText mEditTextInfo;

    ImageProvider mImageProvider;
    AuthProvider mAuthProvider;
    UsersProvider mUsersProvider;

    String info;

    public static BottomSheetInfo newInstance(String info) {
        BottomSheetInfo bottomSheetSelectImage = new BottomSheetInfo();
        Bundle args = new Bundle();
        args.putString("info", info);
        bottomSheetSelectImage.setArguments(args);
        return bottomSheetSelectImage;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        info = getArguments().getString("info");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.botton_sheet_info, container, false);
        mButtonSave = view.findViewById(R.id.btnSave);
        mButtonCancel = view.findViewById(R.id.btnCancel);
        mEditTextInfo = view.findViewById(R.id.editTextInfo);
        mEditTextInfo.setText(info);

        mImageProvider = new ImageProvider();
        mUsersProvider = new UsersProvider();
        mAuthProvider = new AuthProvider();
        mEditTextInfo.setText(info);

        mButtonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateInfo();
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

    private void updateInfo() {
        String info = mEditTextInfo.getText().toString();
        if (!info.equals("")) {
            mUsersProvider.updateInfo(mAuthProvider.getid(), info).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    dismiss();
                    Toast.makeText(getContext(), "tu estado se actualizo", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
