package teclag.c17130049.whatsappclone.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.google.android.material.tabs.TabLayout;
import com.mancj.materialsearchbar.MaterialSearchBar;

import teclag.c17130049.whatsappclone.R;
import teclag.c17130049.whatsappclone.adapters.ViewPagerAdapter;
import teclag.c17130049.whatsappclone.fragments.ChatsFragment;
import teclag.c17130049.whatsappclone.fragments.ContactsFragment;
import teclag.c17130049.whatsappclone.fragments.PhotoFragment;
import teclag.c17130049.whatsappclone.fragments.StatusFragment;
import teclag.c17130049.whatsappclone.providers.AuthProvider;


public class HomeActivity extends AppCompatActivity implements MaterialSearchBar.OnSearchActionListener{


    AuthProvider mAuthProvider;
    MaterialSearchBar mSearchBar;

    TabLayout mTabLayout;
    ViewPager mViewPager;

    ChatsFragment mChatsFragments;
    ContactsFragment mContactFragment;
    StatusFragment mStatusFragment;
    PhotoFragment mPhotoFragment;

    int mTabSelected = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);



        mSearchBar = findViewById(R.id.searchBar);
        mTabLayout = findViewById(R.id.tablayout);
        mViewPager = findViewById(R.id.viewPager);


        mViewPager.setOffscreenPageLimit(3);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        mChatsFragments = new ChatsFragment();
        mContactFragment = new ContactsFragment();
        mStatusFragment = new StatusFragment();
        mPhotoFragment = new PhotoFragment();

        adapter.addFragment(mPhotoFragment,"");
        adapter.addFragment(mChatsFragments,"chats");
        adapter.addFragment(mStatusFragment,"estados");
        adapter.addFragment(mContactFragment,"contactos");


        mViewPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mViewPager.setCurrentItem(mTabSelected);


        setupTabIcon();

        mSearchBar.setOnSearchActionListener(this);
        mSearchBar.inflateMenu(R.menu.main_menu);
        mSearchBar.getMenu().setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.itemSignOut){
                signOut();
            }
            else if (item.getItemId() == R.id.itemProfile){
                goToProfile();
            }
            return true;
        });


        mAuthProvider = new AuthProvider();
    }

    private void goToProfile() {
        Intent intent = new Intent(HomeActivity.this,ProfileActivity.class);
        startActivity(intent);
    }

    private void setupTabIcon() {

        mTabLayout.getTabAt(0).setIcon(R.drawable.ic_camera);
        LinearLayout linearLayout= ((LinearLayout) ((LinearLayout) mTabLayout.getChildAt(0)).getChildAt(0));
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams)linearLayout.getLayoutParams();
        layoutParams.weight=0.5f;
        linearLayout.setLayoutParams(layoutParams);

    }

    private void signOut(){
        mAuthProvider.signOut();
        Intent intent = new Intent(HomeActivity.this,MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void onSearchStateChanged(boolean enabled) {

    }

    @Override
    public void onSearchConfirmed(CharSequence text) {

    }

    @Override
    public void onButtonClicked(int buttonCode) {

    }
}