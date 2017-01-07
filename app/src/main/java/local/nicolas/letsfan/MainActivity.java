package local.nicolas.letsfan;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import local.nicolas.letsfan.auth.AuthUI;
import local.nicolas.letsfan.auth.ui.ResultCodes;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    // class variables
    private static final String TAG = "MainActivity";

    // Google and Firebase
    private FirebaseAuth mFirebaseAuth;
    private static final int RC_SIGN_IN = 9001;
    private static final int RC_CREATE_INVITATION = 9002;
    private static final int RC_CREATE_USER = 9003;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference userRef;
    private DatabaseReference inviRef;

    private TextView mNavUserName;
    private TextView mNavUserMail;
    private ImageView mUserAvatar;
    private Button mNavSignInButton;
    private Toolbar toolbar;
    private FloatingActionButton fab;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private FirebaseRecyclerAdapter <Invitation,InvitationViewHolder> mFirebaseAdapter;

    private ActionBarDrawerToggle toggle;
    private View mRootView;
    private User currentUser;

    //viewholder
    public static class InvitationViewHolder extends RecyclerView.ViewHolder {
        public TextView organizerTextView;
        public TextView dateTextView;
        public TextView availableSlotTextView;
        public ImageView invitationImageView;

        public InvitationViewHolder(View v) {
            super(v);
            organizerTextView = (TextView) itemView.findViewById(R.id.invitation_organiser);
            dateTextView = (TextView) itemView.findViewById(R.id.invitation_date);
            availableSlotTextView = (TextView) itemView.findViewById(R.id.invitation_available_slot);
            //invitationImageView = (ImageView) itemView.findViewById(R.id.invitation_image);
        }
    }

    private ValueEventListener userListener = new ValueEventListener() {
        @Override
        public void onDataChange (DataSnapshot dataSnapshot) {
            if (dataSnapshot.hasChildren()) {
                currentUser = dataSnapshot.getValue(User.class);
            } else {
                // prompt to create new user
                Log.d(TAG, "userListener onDataChange: Create user in database.");
                startActivityForResult(new Intent(MainActivity.this, RegisterUserActivity.class), RC_CREATE_USER);
            }
        }
        @Override
        public void onCancelled (DatabaseError error) {
            Log.d(TAG, "userListener.onCancelled: error");
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Authentication
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        userRef = mFirebaseDatabase.getReference("users");
        inviRef = mFirebaseDatabase.getReference("invitations");

        // UI binding
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar_123);
        fab = (FloatingActionButton) findViewById(R.id.floatingActionButton2);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);

        mRootView = findViewById(R.id.content_invitation);
        mRecyclerView=(RecyclerView) findViewById(R.id.recyclerView);

        mLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);

        mFirebaseAdapter = new FirebaseRecyclerAdapter<Invitation, InvitationViewHolder>(
                Invitation.class,
                R.layout.invitation_card,
                InvitationViewHolder.class,
                inviRef) {
            @Override
            protected void populateViewHolder(InvitationViewHolder viewHolder, final Invitation invitation, final int position) {
                //mProgressBar.setVisibility(ProgressBar.INVISIBLE);
                String mDate=invitation.getDateMonth().toString()+"月"+invitation.getDateDay()+"日";
                viewHolder.organizerTextView.setText(invitation.getOrganizerNickName());
                viewHolder.dateTextView.setText(mDate);

                viewHolder.organizerTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view){
                        Log.w(TAG, "You clicked on "+position);
                        //mFirebaseAdapter.getRef(position).removeValue();
                        Invitation_Dialog invitation_dialog=new Invitation_Dialog();
                        Bundle mBundle = new Bundle();
                        mBundle.putString("pushId",invitation.getId());
                        invitation_dialog.setArguments(mBundle);
                        invitation_dialog.show(getFragmentManager(),"invitationDialog");
                    }
                });

            }
        };

        mFirebaseAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                int invitationCount = mFirebaseAdapter.getItemCount();
                int lastVisiblePosition = mLinearLayoutManager.findLastCompletelyVisibleItemPosition();
                // If the recycler view is initially being loaded or the user is at the bottom of the list, scroll
                // to the bottom of the list to show the newly added message.
                if (lastVisiblePosition == -1 ||
                        (positionStart >= (invitationCount - 1) && lastVisiblePosition == (positionStart - 1))) {
                    mRecyclerView.scrollToPosition(positionStart);
                }
            }
        });

        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setAdapter(mFirebaseAdapter);

        setSupportActionBar(toolbar);

        if (mFirebaseAuth.getCurrentUser() == null) {
            currentUser = null;
        } else {
            queryDatabaseForUser(mFirebaseAuth.getCurrentUser().getUid());
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mIntentInvitation = new Intent(MainActivity.this, CreateInvitationActivity.class);
                mIntentInvitation.putExtra("currentUser", currentUser);
                startActivityForResult(mIntentInvitation, RC_CREATE_INVITATION);
            }
        });

        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                updateNav();

                return;
            }
        };
        drawer.addDrawerListener(toggle);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @OnClick(R.id.sign_in_button)
    public void signIn(View view) {
        List<AuthUI.IdpConfig> providerList = new ArrayList();
        providerList.add(new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build());
        providerList.add(new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build());
        startActivityForResult(
                AuthUI.getInstance().createSignInIntentBuilder()
                        .setTheme(R.style.AppTheme)
                        .setLogo(R.drawable.ic_restaurant_menu_black_24dp)
                        .setProviders(providerList)
                        .setTosUrl("https://github.com/SPEITCoder/let-s-fan")
                        .setIsSmartLockEnabled(true)
                        .build(),
                RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Check which request we're responding to
        if (requestCode == RC_SIGN_IN) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                updateNav();
                Snackbar.make(mRootView, "Signed in successfully.", Snackbar.LENGTH_LONG).show();
                Query currentUserQuery = userRef.equalTo(mFirebaseAuth.getCurrentUser().getUid());
                currentUserQuery.addListenerForSingleValueEvent(userListener);
                return;
            } else if (resultCode == RESULT_CANCELED) {
                Snackbar.make(mRootView, "Signed in cancelled.", Snackbar.LENGTH_LONG).show();
                return;
            } else if (resultCode == ResultCodes.RESULT_NO_NETWORK) {
                Snackbar.make(mRootView, "Signed in failed for no internet.", Snackbar.LENGTH_LONG).show();
                return;
            } else {
                Snackbar.make(mRootView, "Unknown response.", Snackbar.LENGTH_LONG).show();
            }
        } else if (requestCode == RC_CREATE_INVITATION) {
            if (resultCode == RESULT_OK) {
                Snackbar.make(mRootView, "Invitation is added!" , Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            } else {
                Snackbar.make(mRootView, "Invitation creation failed!" , Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        } else if (requestCode == RC_CREATE_USER) {
            if (resultCode == RESULT_OK) {
                Snackbar.make(mRootView, "User is successfully registered!" , Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                currentUser = (User) data.getSerializableExtra("currentUser");
            } else {
                Snackbar.make(mRootView, "User registration failure!" , Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        toggle.syncState();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);


        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        navigationView.getMenu().clear();
        navigationView.inflateMenu(R.menu.activity_main_drawer);

//
//        if (mFirebaseAuth.getCurrentUser() == null) {
//            MenuItem temp = navigationView.getMenu().getItem(R.id.nav_initiate_invitation);
//            temp.setCheckable(false);
//            navigationView.getMenu().getItem(R.id.nav_manage_user_profile).setCheckable(false);
//            navigationView.getMenu().getItem(R.id.nav_section_sign_out).setCheckable(false);
//        } else {
//            navigationView.getMenu().getItem(R.id.nav_initiate_invitation).setCheckable(true);
//            navigationView.getMenu().getItem(R.id.nav_manage_user_profile).setCheckable(true);
//            navigationView.getMenu().getItem(R.id.nav_section_sign_out).setCheckable(true);
//        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.nav_initiate_invitation) {



        } else if (id == R.id.nav_open_invitation_list) {

        } else if (id == R.id.nav_manage_user_profile) {

        } else if (id == R.id.nav_settings) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_help) {

        } else if (id == R.id.nav_sign_out) {
            signOut();
            drawer.closeDrawer(GravityCompat.START);
            return true;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @OnClick(R.id.nav_sign_out)
    public void signOut() {
        AuthUI.getInstance().signOut(this).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    updateNav();
                    Snackbar.make(mRootView, "Sign out successful.", Snackbar.LENGTH_LONG).show();
                } else {
                    Snackbar.make(mRootView, "Sign out failed.", Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void updateNav() {
        mNavUserName = (TextView) findViewById(R.id.nav_head_user_name);
        mNavUserMail = (TextView) findViewById(R.id.nav_header_user_mail);
        mUserAvatar = (CircleImageView) findViewById(R.id.userAvatar);
        mNavSignInButton = (Button) findViewById(R.id.sign_in_button);

        if(currentUser != null && mFirebaseAuth.getCurrentUser() != null) {
            if (mFirebaseAuth.getCurrentUser().getPhotoUrl() != null) {
                Glide.with(this)
                        .load(mFirebaseAuth.getCurrentUser().getPhotoUrl())
                        .fitCenter()
                        .into(mUserAvatar);
            } else {
                // TODO, replace with random avatar

            }

            mNavUserMail.setText(currentUser.getEmail());
            mNavUserName.setText(currentUser.getNickName());

            mNavUserMail.setVisibility(View.VISIBLE);
            mNavSignInButton.setVisibility(View.GONE);
            mNavUserName.setVisibility(View.VISIBLE);
            mUserAvatar.setVisibility(View.VISIBLE);

            navigationView.getMenu().getItem(0).setVisible(true);
            navigationView.getMenu().getItem(1).setVisible(true);
            navigationView.getMenu().getItem(2).setVisible(true);

            navigationView.getMenu().getItem(6).setVisible(true);

        } else {
            mNavUserMail.setVisibility(View.GONE);
            mNavSignInButton.setVisibility(View.VISIBLE);
            mNavUserName.setVisibility(View.GONE);
            mUserAvatar.setVisibility(View.GONE);

            navigationView.getMenu().getItem(0).setVisible(false);
            navigationView.getMenu().getItem(1).setVisible(false);
            navigationView.getMenu().getItem(2).setVisible(false);
            navigationView.getMenu().getItem(6).setVisible(false);

            mNavSignInButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    signIn(v);
                    drawer.closeDrawer(GravityCompat.START);
                }
            });
        }
    }

//    public static class AlertDialogFragment extends DialogFragment{
//        public static AlertDialogFragment newInstance(){
//            AlertDialogFragment newInstance = new AlertDialogFragment();
//            return newInstance;
//        }
//        Context mContext = getApplicationContext();
//        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
//        View layout = inflater.inflate(R.layout.invitation_dialog,
//                (ViewGroup) findViewById(R.id.dialog));
//        @Override
//        public Dialog onCreateDialog(Bundle savedInstanceState){
//            AlertDialog.Builder alertDialog=new AlertDialog.Builder(getActivity());
//            alertDialog.setTitle("Alert Dialog");
//            alertDialog.setMessage("You have been alerted");
//
//
//            //LayoutInflater inflater = getLayoutInflater();
//
//            alertDialog.setView(layout);
//            alertDialog.setPositiveButton(android.R.string.ok,
//                    new DialogInterface.OnClickListener(){                        @Override
//                        public void onClick(DialogInterface dialog, int which){
//                            Toast.makeText(getActivity(),"Clicked OK!", Toast.LENGTH_SHORT).show();
//                            return;
//                        }
//                    });
//            return alertDialog.create();
//        }
//    }
//
//    void showDialogFragment(DialogFragment newFragment){
//        //FragmentManager fragmentManager = newFragment.getFragmentManager();
//        newFragment.show(getFragmentManager(),null);
//    }



    @Override
    public void onStop() {
        super.onStop();
    }

    private void queryDatabaseForUser (String uid) {

        userRef.child(uid).addListenerForSingleValueEvent(userListener);
    }




}
