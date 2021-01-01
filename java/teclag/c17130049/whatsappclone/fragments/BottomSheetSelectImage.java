package teclag.c17130049.whatsappclone.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import teclag.c17130049.whatsappclone.R;
import teclag.c17130049.whatsappclone.activities.ProfileActivity;
import teclag.c17130049.whatsappclone.providers.AuthProvider;
import teclag.c17130049.whatsappclone.providers.ImageProvider;
import teclag.c17130049.whatsappclone.providers.UsersProvider;

public class BottomSheetSelectImage extends BottomSheetDialogFragment {

   LinearLayout mlinearLayoutDeleteImage;
   LinearLayout mlinearLayoutSelectImage;
   ImageProvider mImageProvider; //borar imagen
   AuthProvider mAuthProvider; //tener id
    UsersProvider mUserProvider;

    String image;

    public static BottomSheetSelectImage newInstances( String url){
        BottomSheetSelectImage bottomSelectImage = new BottomSheetSelectImage();
        Bundle args = new Bundle();
        args.putString("image",url);

        bottomSelectImage.setArguments(args);

        return bottomSelectImage;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        image = getArguments().getString("image");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.botton_sheet_select_image ,container,false);
        mlinearLayoutDeleteImage = view.findViewById(R.id.linearLayoutDeleteImage);
        mlinearLayoutSelectImage = view.findViewById(R.id.linearLayoutSelectImage);

        mImageProvider = new ImageProvider();
        mAuthProvider = new AuthProvider();
        mUserProvider = new UsersProvider();


        mlinearLayoutDeleteImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteImage();
            }
        });


        mlinearLayoutSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateImage();
            }
        });


        return view;

    }

    private void updateImage() {

        ((ProfileActivity)getActivity()).startPix();
    }

    private void deleteImage() {
        mImageProvider.delete(image).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    mUserProvider.UpdateImage(mAuthProvider.getid(),null).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task2) {
                            if (task2.isSuccessful()){
                                //setImageDefault();
                                Toast.makeText(getContext(),"la imagen se elimino correctamente",Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(getContext(),"No se pudo eliminar la imagen ",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                else{
                    Toast.makeText(getContext(),"nose pudo eliminiar la imagen",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setImageDefault() {
        ((ProfileActivity)getActivity()).setImageDefault();
    }



}
