package local.nicolas.letsfan;

import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.DialogFragment;
import android.app.FragmentManager;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.icu.util.Calendar;
import android.icu.util.TimeZone;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CreateInvitationActivity extends AppCompatActivity {


    private User currentUser;
    private TextTime startTime;
    private TextTime endTime;
    private TextDate eventDate;
    private SeekBar tasteVariation;
    private Spinner restaurantSelector;

    final List<String> restos = new ArrayList<>();
    final List<String> restoID = new ArrayList<>();
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_invitation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_123);
        setSupportActionBar(toolbar);

        Intent mIntent = getIntent();

        currentUser = (User) mIntent.getSerializableExtra("currentUser");
        startTime = (TextTime) findViewById(R.id.start_time_text);
        endTime = (TextTime) findViewById(R.id.end_time_text);
        eventDate = (TextDate) findViewById(R.id.event_date_text);
        tasteVariation = (SeekBar) findViewById(R.id.seekBar_tasteAdv);
        restaurantSelector = (Spinner) findViewById(R.id.restaurant_slector);

        TimeZone mTimeZone = Calendar.getInstance().getTimeZone();
        Date mDate = Calendar.getInstance(mTimeZone).getTime();
        eventDate.setDate(mDate);
        startTime.setTime(mDate.getHours(), mDate.getMinutes());
        mDate.setTime(Calendar.getInstance().getTimeInMillis() + 3600000);
        endTime.setTime(mDate.getHours(), mDate.getMinutes());

        FirebaseDatabase.getInstance().getReference("restos").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Is better to use a List, because you don't know the size
                // of the iterator returned by dataSnapshot.getChildren() to
                // initialize the array
                restoID.clear();
                restos.clear();
                for (DataSnapshot restSnapshot: dataSnapshot.getChildren()) {
                    restos.add(restSnapshot.child("name").getValue(String.class));
                    restoID.add(restSnapshot.getKey());
                }

                restaurantSelector = (Spinner) findViewById(R.id.restaurant_slector);
                ArrayAdapter<String> restosAdapter = new ArrayAdapter<>(CreateInvitationActivity.this, android.R.layout.simple_spinner_item, restos);
                restosAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                restaurantSelector.setAdapter(restosAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        FloatingActionButton myfab = (FloatingActionButton) findViewById(R.id.fabFinishCreatingInvitation);
        myfab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (vadilateInput()) {
                    currentUser.createInvitation(FirebaseDatabase.getInstance(), FirebaseAuth.getInstance().getCurrentUser().getUid(),
                            startTime.getHour(), startTime.getMinute(), endTime.getHour(), endTime.getMinute(), eventDate.getYear(), eventDate.getMonth(), eventDate.getDayOfMonth(), restoID.get(restaurantSelector.getSelectedItemPosition()), restos.get(restaurantSelector.getSelectedItemPosition()));
                    setResult(RESULT_OK);
                    finish();
                }
            }
        });



        startTime.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    DialogFragment mTime = new TimePickerDialogFragment();
                    Bundle mBundle = new Bundle();
                    mBundle.putInt("hour", startTime.getHour());
                    mBundle.putInt("minute", startTime.getMinute());
                    mBundle.putInt("id", R.id.start_time_text);
                    mTime.setArguments(mBundle);
                    mTime.show(getSupportFragmentManager(), "timePicker");
                }
                return true;
            }
        });

        endTime.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    DialogFragment mTime = new TimePickerDialogFragment();
                    Bundle mBundle = new Bundle();
                    mBundle.putInt("hour", endTime.getHour());
                    mBundle.putInt("minute", endTime.getMinute());
                    mBundle.putInt("id", R.id.end_time_text);
                    mTime.setArguments(mBundle);
                    mTime.show(getSupportFragmentManager(), "timePicker");
                }
                return true;
            }
        });

        eventDate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    DialogFragment mDate = new DatePickerDialogFragment();
                    Bundle mBundle = new Bundle();
                    mBundle.putInt("year", eventDate.getYear());
                    mBundle.putInt("month", eventDate.getMonth());
                    mBundle.putInt("date", eventDate.getDayOfMonth());
                    mBundle.putInt("id", R.id.event_date_text);
                    mDate.setArguments(mBundle);
                    mDate.show(getSupportFragmentManager(), "DatePicker");
                }
                return true;
            }
        });


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("CreateInvitation Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        setResult(RESULT_CANCELED);
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

    public Boolean vadilateInput () {
        // Date check
        TimeZone mTimeZone = Calendar.getInstance().getTimeZone();
        Date mDate = Calendar.getInstance(mTimeZone).getTime();

        if (eventDate.getYear() < mDate.getYear() + 1900) {
            Snackbar.make(findViewById(R.id.content_create_invitation), "Year is invalid", Snackbar.LENGTH_LONG);
            return false;
        } else if (eventDate.getYear() == mDate.getYear() + 1900) {
            if (eventDate.getMonth() < mDate.getMonth() + 1) {
                Snackbar.make(findViewById(R.id.content_create_invitation), "Month is invalid", Snackbar.LENGTH_LONG);
                return false;
            } else if (eventDate.getMonth() == mDate.getMonth() + 1) {
                if (eventDate.getDayOfMonth() < mDate.getDate()) {
                    Snackbar.make(findViewById(R.id.content_create_invitation), "Date is invalid", Snackbar.LENGTH_LONG);
                    return false;
                } else if (eventDate.getDayOfMonth() == mDate.getDate()) {
                    if (startTime.getHour() < mDate.getHours()) {
                        Snackbar.make(findViewById(R.id.content_create_invitation), "Start hour is in the past.", Snackbar.LENGTH_LONG).show();
                        return false;
                    } else if (startTime.getHour() == mDate.getHours()) {
                        if (startTime.getMinute() < mDate.getMinutes()) {
                            Snackbar.make(findViewById(R.id.content_create_invitation), "Start minute is in the past.", Snackbar.LENGTH_LONG).show();
                            return false;
                        }
                    }
                }
            }
        }
        if (startTime.getHour() < endTime.getHour()) {
            return true;
        } else if (startTime.getHour() == endTime.getHour()) {
            if (startTime.getMinute() < endTime.getMinute()) {
                return true;
            } else {
                Snackbar.make(findViewById(R.id.content_create_invitation), "End before start.", Snackbar.LENGTH_LONG).show();
                return false;
            }
        } else {
            Snackbar.make(findViewById(R.id.content_create_invitation), "End before start.", Snackbar.LENGTH_LONG).show();
            return false;
        }

    }
}
