package teclag.c17130049.whatsappclone.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;
import teclag.c17130049.whatsappclone.R;
import teclag.c17130049.whatsappclone.adapters.ChatsAdapter;
import teclag.c17130049.whatsappclone.adapters.MessagesAdapter;
import teclag.c17130049.whatsappclone.models.Chat;
import teclag.c17130049.whatsappclone.models.Message;
import teclag.c17130049.whatsappclone.models.User;
import teclag.c17130049.whatsappclone.providers.AuthProvider;
import teclag.c17130049.whatsappclone.providers.ChatsProvider;
import teclag.c17130049.whatsappclone.providers.MessagesProvider;
import teclag.c17130049.whatsappclone.providers.UsersProvider;

public class ChatActivity extends AppCompatActivity {

    String mExtraIdUser;
    String mExtraIdChat;


    UsersProvider mUsersProvider ;
    AuthProvider mAuthProvider;
    ImageView mimageViewBack;
    TextView mTextViewUsername;
    CircleImageView mCircleImageUser;

    ChatsProvider mChatsProvider;

    EditText mEditTextMessage;
    ImageView mImageViewSend;

    MessagesProvider mMessagesProvider;

    MessagesAdapter mAdapter;
    RecyclerView  mRecyclerViewMessage;

    LinearLayoutManager mLinearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_chat);

        mExtraIdUser = getIntent().getStringExtra("idUser");
        mExtraIdChat = getIntent().getStringExtra("idChat");

        mAuthProvider = new AuthProvider();
        mUsersProvider = new UsersProvider();
        mChatsProvider = new ChatsProvider();
        mMessagesProvider = new MessagesProvider();

        mEditTextMessage = findViewById(R.id.editTextMessage);
        mImageViewSend = findViewById(R.id.imageViewSend);
        mRecyclerViewMessage = findViewById(R.id.recylcerViewMessages);

        mLinearLayoutManager = new LinearLayoutManager(ChatActivity.this);
        mRecyclerViewMessage.setLayoutManager(mLinearLayoutManager);


        showChatToolbar(R.layout.chat_toolbar);
        getUserInfo();


                checkifExistChat();



        mImageViewSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            createMessage();
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        Query query = mMessagesProvider.getMessageByChat(mExtraIdChat);
        FirestoreRecyclerOptions<Message> options = new FirestoreRecyclerOptions.Builder<Message>()
                .setQuery(query,Message.class)
                .build();

        mAdapter = new MessagesAdapter(options,ChatActivity.this);
        mRecyclerViewMessage.setAdapter(mAdapter);
        mAdapter.startListening();

    }

    @Override
    protected void onStop() {
        super.onStop();
        mAdapter.stopListening();
        }

    private void createMessage() {
        String textMessage = mEditTextMessage.getText().toString();
        if (!textMessage.equals("")){
            Message message = new Message();
            message.setIdChat(mExtraIdChat);
            message.setIdSender(mAuthProvider.getid());
            message.setIdReceiver(mExtraIdUser);
            message.setMessage(textMessage);
            message.setStatus("Enviado");
            message.setTimestamp(new  Date().getTime());
            mMessagesProvider.create(message).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    mEditTextMessage.setText("");
                    Toast.makeText(ChatActivity.this,"El mensaje se envio ",Toast.LENGTH_SHORT).show();
                }
            });
        }
        else{
            Toast.makeText(ChatActivity.this,"Ingresa el mensaje ",Toast.LENGTH_SHORT).show();

        }
    }

    private void checkifExistChat() {
        mChatsProvider.getChatByUser1AndUser2(mExtraIdUser,mAuthProvider.getid()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (queryDocumentSnapshots != null){
                    if (queryDocumentSnapshots.size() == 0){
                        createChat();
                    }
                    else{
                        mExtraIdChat = queryDocumentSnapshots.getDocuments().get(0).getId();
                        Toast.makeText(ChatActivity.this,"El chat entre estos dos usuarios ya existe",Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    private void createChat() {
        Chat chat = new Chat();
        chat.setId(mAuthProvider.getid() + mExtraIdUser);
        chat.setTimestamp(new Date().getTime());

        ArrayList<String> ids= new ArrayList<>();
        ids.add(mAuthProvider.getid());
        ids.add(mExtraIdUser);

        chat.setIds(ids);

        mExtraIdChat = chat.getId();


        mChatsProvider.create(chat).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(ChatActivity.this,"el chat se Creo Correctamente ",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getUserInfo() {
        mUsersProvider.getUserInfo(mExtraIdUser).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                if (documentSnapshot != null){
                    if (documentSnapshot.exists()){
                        User user = documentSnapshot.toObject(User.class);
                        mTextViewUsername.setText(user.getUsername());
                        if (user.getImage() != null){
                            if (!user.getImage().equals("")){
                                Picasso.with(ChatActivity.this).load(user.getImage()).into(mCircleImageUser);
                            }
                        }
                    }
                }
            }
        });

    }

    private void  showChatToolbar(int resource){
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(resource , null);
        actionBar.setCustomView(view);

        mimageViewBack = view.findViewById(R.id.ImageViewBack);
        mTextViewUsername = view.findViewById(R.id.textViewUsername);
        mCircleImageUser = view.findViewById(R.id.CircleImageUser);

        mimageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }


}