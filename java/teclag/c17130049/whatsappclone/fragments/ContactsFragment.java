package teclag.c17130049.whatsappclone.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;

import teclag.c17130049.whatsappclone.R;
import teclag.c17130049.whatsappclone.adapters.ContactsAdapter;
import teclag.c17130049.whatsappclone.models.User;
import teclag.c17130049.whatsappclone.providers.AuthProvider;
import teclag.c17130049.whatsappclone.providers.UsersProvider;

/**
 * A simple {@link Fragment} subclass.
 * Use the  factory method to
 * create an instance of this fragment.
 */

public class ContactsFragment extends Fragment {

View mView;
RecyclerView mRecyclerViewContacts;

ContactsAdapter mAdapter;


UsersProvider mUsersProvider;
AuthProvider mAuthProvider;

    public ContactsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView= inflater.inflate(R.layout.fragment_contacs, container, false);
        mRecyclerViewContacts = mView.findViewById(R.id.recyclerViewContacts);
        mUsersProvider = new UsersProvider();
        mAuthProvider = new AuthProvider();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());

        mRecyclerViewContacts.setLayoutManager(linearLayoutManager);

        return mView;
    }

    @Override
    public void onStart() {
        super.onStart();
        Query query = mUsersProvider.getAllUsersByName();
        FirestoreRecyclerOptions<User> options = new FirestoreRecyclerOptions.Builder<User>().setQuery(query,User.class).build();

        mAdapter = new ContactsAdapter(options,getContext());
        mRecyclerViewContacts.setAdapter(mAdapter);
        mAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        mAdapter.stopListening();

    }
}