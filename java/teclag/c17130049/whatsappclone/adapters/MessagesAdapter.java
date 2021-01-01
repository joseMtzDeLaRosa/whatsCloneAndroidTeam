package teclag.c17130049.whatsappclone.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import teclag.c17130049.whatsappclone.R;
import teclag.c17130049.whatsappclone.activities.ChatActivity;
import teclag.c17130049.whatsappclone.models.Chat;
import teclag.c17130049.whatsappclone.models.Message;
import teclag.c17130049.whatsappclone.models.User;
import teclag.c17130049.whatsappclone.providers.AuthProvider;
import teclag.c17130049.whatsappclone.providers.UsersProvider;
import teclag.c17130049.whatsappclone.utils.RelativeTime;

public class MessagesAdapter extends FirestoreRecyclerAdapter <Message, MessagesAdapter.ViewHolder>{

    Context context;
    AuthProvider authProvider;
    UsersProvider mUserProviders;
    User user ;
    ListenerRegistration listener;

    public MessagesAdapter(FirestoreRecyclerOptions options, Context context) {
        super(options);
        this.context = context;
        authProvider = new AuthProvider();
        mUserProviders = new UsersProvider() ;
        user = new User();

    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull  final Message message) {

        holder.textViewMessage.setText(message.getMessage());
        holder.textViewDate.setText(RelativeTime.timeFormatAMPM(message.getTimestamp(),context));

    }



    public ListenerRegistration getListener (){
        return listener;
    }




    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_message, parent, false);
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView textViewMessage;
        TextView textViewDate;
        ImageView ImageViewCheck;
        View myView;


        public ViewHolder(View view) {
            super(view);
            myView = view;
            textViewMessage = view.findViewById(R.id.textViewMessage);
            textViewDate = view.findViewById(R.id.textViewDate);
            ImageViewCheck = view.findViewById(R.id.imageViewCheck);
            ImageViewCheck = view.findViewById(R.id.imageViewCheck);



        }
    }

}
