package teclag.c17130049.whatsappclone.utils;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import teclag.c17130049.whatsappclone.R;

public class MyToolbar {

    public  static  void show(AppCompatActivity activity , String title , boolean upButton){
        Toolbar toolbar = activity.findViewById(R.id.toolbar);
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setTitle(title);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(upButton);

    }

}
