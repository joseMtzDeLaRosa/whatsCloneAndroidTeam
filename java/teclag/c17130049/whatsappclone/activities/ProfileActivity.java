package teclag.c17130049.whatsappclone.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fxn.pix.Options;
import com.fxn.pix.Pix;
import com.fxn.utility.PermUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import teclag.c17130049.whatsappclone.R;
import teclag.c17130049.whatsappclone.fragments.BottomSheetInfo;
import teclag.c17130049.whatsappclone.fragments.BottomSheetSelectImage;
import teclag.c17130049.whatsappclone.fragments.BottomSheetUsername;
import teclag.c17130049.whatsappclone.models.User;
import teclag.c17130049.whatsappclone.providers.AuthProvider;
import teclag.c17130049.whatsappclone.providers.ImageProvider;
import teclag.c17130049.whatsappclone.providers.UsersProvider;
import teclag.c17130049.whatsappclone.utils.MyToolbar;

public class ProfileActivity extends AppCompatActivity {

    FloatingActionButton mFabSelectImage;
    BottomSheetSelectImage mButtonSheetSelectImage;
    BottomSheetInfo mButtonBottomSheetInfo;
    ImageProvider mImageProvider;

    BottomSheetUsername mBottonSheetUsername ;

    UsersProvider mUsersProvider;
    AuthProvider mAuthProvider;

    TextView mTextViewUsername;
    TextView mTextViewPhone;
    TextView mTextViewInfo;
    CircleImageView mCircleImageProfile;

    Options mOptions;
    ArrayList<String> mReturnValues = new ArrayList<>();
    File mImageFile;

    ImageView mImageViewEditUsername;
    ImageView mImageViewEditInfo;

    User mUser;

   ListenerRegistration mListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        MyToolbar.show(this,"perfil",true);

        mUsersProvider = new UsersProvider();
        mAuthProvider = new AuthProvider();


        mTextViewUsername = findViewById(R.id.textViewUsername);
        mTextViewPhone = findViewById(R.id.textViewPhone);
        mTextViewInfo = findViewById(R.id.textViewInfo);
        mCircleImageProfile = findViewById(R.id.circleImageProfile);
        mImageViewEditUsername = findViewById(R.id.imageViewEditUsername);
        mImageViewEditInfo = findViewById(R.id.imageViewEditInfo);

        mOptions = Options.init()
                .setRequestCode(100)                                           //Request code for activity results
                .setCount(1)                                                   //Number of images to restict selection count
                .setFrontfacing(false)                                         //Front Facing camera on start
                .setPreSelectedUrls(mReturnValues)                               //Pre selected Image Urls
                .setExcludeVideos(true)                                       //Option to exclude videos
                .setVideoDurationLimitinSeconds(0)                            //Duration for video recording
                .setScreenOrientation(Options.SCREEN_ORIENTATION_PORTRAIT)     //Orientaion
                .setPath("/pix/images");                                       //Custom Path For media Storage


        mFabSelectImage = findViewById(R.id.fabSelectImage);
        mFabSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBottomSheetSelectImage();
            }
        });

        mImageViewEditUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBottomSheetSelectUsername();
            }
        });
        mImageViewEditInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openBottomSheetEditInfo();
            }
        });
        getUserInfo();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mListener != null) {
            mListener.remove();
        }
    }

    private void getUserInfo() {
 mListener  =     mUsersProvider.getUserInfo(mAuthProvider.getid()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                if (documentSnapshot != null) {
                    if (documentSnapshot.exists()) {
                        mUser = documentSnapshot.toObject(User.class);
                        mTextViewUsername.setText(mUser.getUsername());
                        mTextViewPhone.setText(mUser.getPhone());
                        mTextViewInfo.setText(mUser.getInfo());

                        if (mUser.getImage() != null) {
                            if (!mUser.getImage().equals("")) {
                                Picasso.with(ProfileActivity.this).load(mUser.getImage()).into(mCircleImageProfile);
                            }
                            else{
                                setImageDefault();
                            }
                        }
                        else{
                            setImageDefault();
                        }
                    }
                }


            }
        });

    }

    private void openBottomSheetSelectUsername() {
        if (mUser != null) {
            mBottonSheetUsername = BottomSheetUsername.newInstance(mUser.getUsername());
            mBottonSheetUsername.show(getSupportFragmentManager(), mBottonSheetUsername.getTag());
        }
        else{
            Toast.makeText(this,"La informacion no se pudo cargar",Toast.LENGTH_SHORT).show();
        }
    }

    private void openBottomSheetEditInfo() {
        if (mUser != null) {
            mButtonBottomSheetInfo = BottomSheetInfo.newInstance(mUser.getInfo());
            mButtonBottomSheetInfo.show(getSupportFragmentManager(), mButtonBottomSheetInfo.getTag());
        }
        else{
            Toast.makeText(this,"La informacion no se pudo cargar",Toast.LENGTH_SHORT).show();
        }
    }



    private void openBottomSheetSelectImage() {
        if (mUser != null) {
            mButtonSheetSelectImage = BottomSheetSelectImage.newInstances(mUser.getImage());
            mButtonSheetSelectImage.show(getSupportFragmentManager(), mButtonSheetSelectImage.getTag());
        }
        else{
            Toast.makeText(this,"La informacion no se pudo cargar",Toast.LENGTH_SHORT).show();
        }
    }

    public void setImageDefault(){
        mCircleImageProfile.setImageResource(R.drawable.ic_person_white);
    }


    public void startPix() {
        Pix.start(ProfileActivity.this, mOptions);
    }

    //servia para obtener la imagen que del el usuario
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == 100) {
            mReturnValues = data.getStringArrayListExtra(Pix.IMAGE_RESULTS);
            mImageFile = new File(mReturnValues.get(0));
            mCircleImageProfile.setImageBitmap(BitmapFactory.decodeFile(mImageFile.getAbsolutePath()));
            saveImage();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PermUtil.REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Pix.start(ProfileActivity.this, mOptions);
                } else {
                    Toast.makeText(ProfileActivity.this, "Por favor concede los permisos para acceder a la camara", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

    private void saveImage() {
        mImageProvider = new ImageProvider();
        mImageProvider.save(ProfileActivity.this, mImageFile).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                mImageProvider.getDownloadUri().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String url = uri.toString();
                       mUsersProvider.UpdateImage(mAuthProvider.getid(),url).addOnSuccessListener(new OnSuccessListener<Void>() {
                           @Override
                           public void onSuccess(Void aVoid) {
                               Toast.makeText(ProfileActivity.this,"La Imagen se actualizo correctamente",Toast.LENGTH_SHORT).show();
                           }
                       });
                    }
                });
            }
            else {
                Toast.makeText(ProfileActivity.this, "No se pudo almacenar la imagen", Toast.LENGTH_SHORT).show();
            }
        });
    }

}