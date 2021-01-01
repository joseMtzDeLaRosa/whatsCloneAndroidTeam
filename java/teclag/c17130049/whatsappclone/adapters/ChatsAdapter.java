package teclag.c17130049.whatsappclone.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import teclag.c17130049.whatsappclone.models.User;
import teclag.c17130049.whatsappclone.providers.AuthProvider;
import teclag.c17130049.whatsappclone.providers.UsersProvider;

public class ChatsAdapter extends FirestoreRecyclerAdapter <Chat, ChatsAdapter.ViewHolder>{

    Context context;
    AuthProvider authProvider;
    UsersProvider mUserProviders;
    User user ;
    ListenerRegistration listener;

    public ChatsAdapter(FirestoreRecyclerOptions options, Context context) {
        super(options);
        this.context = context;
        authProvider = new AuthProvider();
        mUserProviders = new UsersProvider() ;
        user = new User();
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull  final Chat chat) {

         String idUser = "";


        for (int i = 0 ; i< chat.getIds().size(); i++){

            if (!authProvider.getid().equals(chat.getIds().get(i))){
                idUser = chat.getIds().get(i);
                break;
            }
        }
        
        getuUserInfo(holder,idUser);


       clickMyView(holder , chat.getId(), idUser);

    }

    private void clickMyView(ViewHolder holder,final String idChat, final String idUser) {
        holder.myView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToChatActivity(idChat , idUser);
            }
        });
    }

    private void getuUserInfo( final ViewHolder holder, String idUser) {

        listener =   mUserProviders.getUserInfo(idUser).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {

                if (documentSnapshot != null) {
                    if (documentSnapshot.exists()) {
                        user = documentSnapshot.toObject(User.class);
                        holder.textViewUsername.setText(user.getUsername());

                        if (user.getImage() != null){

                            if (!user.getImage().equals("")) {

                                Picasso.with(context).load(user.getImage()).into(holder.circleImageUser);
                            }

                            else {
                                holder.circleImageUser.setImageResource(R.drawable.ic_person);
                            }
                        }
                            else {
                                holder.circleImageUser.setImageResource(R.drawable.ic_person);
                            }
                        }
                    }
                }

            });


    }

    public ListenerRegistration getListener (){
        return listener;
    }


    private void goToChatActivity(String idChat, String idUser) {

        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra("idUser",idUser);
        intent.putExtra("idChat",idChat);
        context.startActivity(intent);

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_chats, parent, false);
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView textViewUsername;
        TextView textViewLastMessage;
        TextView textViewTimestamp;
        CircleImageView circleImageUser;
        ImageView ImageViewCheck;
        View myView;


        public ViewHolder(View view) {
            super(view);

            myView = view;
            textViewUsername = view.findViewById(R.id.textViewUsername);
            textViewLastMessage = view.findViewById(R.id.textViewLastMessage);
            circleImageUser = view.findViewById(R.id.circleImageUser);
            textViewTimestamp = view.findViewById(R.id.textViewTimestamp);

            ImageViewCheck = view.findViewById(R.id.imageViewCheck);



        }
    }

}
