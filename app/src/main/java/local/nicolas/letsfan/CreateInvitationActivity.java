package local.nicolas.letsfan;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

public class CreateInvitationActivity extends AppCompatActivity {


    private User currentUser;        ;
    private TimePicker startTime;
    private TimePicker endTime;
    private DatePicker eventDate;

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

        final Data app= (Data) getApplication();
        currentUser = app.getUser();
        startTime = (TimePicker) findViewById(R.id.startTimePicker);
        endTime = (TimePicker) findViewById(R.id.endTimePicker);
        eventDate = (DatePicker) findViewById(R.id.dateInvitationPicker);

        FloatingActionButton myfab = (FloatingActionButton) findViewById(R.id.fabFinishCreatingInvitation);
        myfab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentUser.createInvitation(app.getmFirebaseDatabase(), app.getmFirebaseAuth().getCurrentUser().getUid(),
                        startTime.getHour(), startTime.getMinute(), endTime.getHour(), endTime.getMinute(), eventDate.getYear(), eventDate.getMonth(), eventDate.getDayOfMonth(), "rid1");

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
        setResult(RESULT_OK);
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}
