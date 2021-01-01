package teclag.c17130049.whatsappclone.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;

import kotlin.contracts.Returns;
import teclag.c17130049.whatsappclone.R;
import teclag.c17130049.whatsappclone.adapters.ChatsAdapter;
import teclag.c17130049.whatsappclone.adapters.ContactsAdapter;
import teclag.c17130049.whatsappclone.models.Chat;
import teclag.c17130049.whatsappclone.models.User;
import teclag.c17130049.whatsappclone.providers.AuthProvider;
import teclag.c17130049.whatsappclone.providers.ChatsProvider;
import teclag.c17130049.whatsappclone.providers.UsersProvider;


public class ChatsFragment extends Fragment {

    View mView;
    RecyclerView mRecyclerViewChats;

    ChatsAdapter mAdapter;


    UsersProvider mUsersProvider;
    AuthProvider mAuthProvider;
    ChatsProvider mChatsProvider;
    public ChatsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView= inflater.inflate(R.layout.fragment_chats, container, false);
        mRecyclerViewChats = mView.findViewById(R.id.recyclerViewChats);
        mUsersProvider = new UsersProvider();
        mAuthProvider = new AuthProvider();
        mChatsProvider = new ChatsProvider();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());

        mRecyclerViewChats.setLayoutManager(linearLayoutManager);

        return mView;
    }

    @Override
    public void onStart() {
        super.onStart();
        Query query = mChatsProvider.getUserChats(mAuthProvider.getid());
        FirestoreRecyclerOptions<Chat> options = new FirestoreRecyclerOptions.Builder<Chat>().setQuery(query,Chat.class).build();

        mAdapter = new ChatsAdapter(options,getContext());
        mRecyclerViewChats.setAdapter(mAdapter);
        mAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        mAdapter.stopListening();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mAdapter.getListener()!= null){
            mAdapter.getListener().remove();
        }
    }
}
