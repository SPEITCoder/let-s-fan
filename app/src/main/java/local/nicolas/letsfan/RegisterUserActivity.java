package local.nicolas.letsfan;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.SeekBar;

import com.google.firebase.auth.FirebaseAuth;

import local.nicolas.letsfan.auth.ui.email.field_validators.RequiredFieldValidator;

/**
 * Created by soshy on 07/01/2017.
 */

public class RegisterUserActivity extends AppCompatActivity {
    private EditText firstName;
    private EditText lastName;
    private EditText nickName;
    private CheckBox isInfoPublic;
    private SeekBar tasteVariation;
    private SeekBar tasteSour;
    private SeekBar tasteSweet;
    private SeekBar tasteBitter;
    private SeekBar tasteSpice;
    private SeekBar tasteSalt;
    private FloatingActionButton fabFinish;
    private RequiredFieldValidator mFirstNameValidator;
    private RequiredFieldValidator mLastNameValidator;
    private RequiredFieldValidator mNickNameValidator;

    private final String TAG = "RegisterUserActivity:";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // UI binding
        setContentView(R.layout.activity_register_user);

        firstName = (EditText) findViewById(R.id.editText_firstName);
        lastName = (EditText) findViewById(R.id.editText_lastName);
        nickName = (EditText) findViewById(R.id.editText_nickName);
        isInfoPublic = (CheckBox) findViewById(R.id.checkBox_isInfoPublic);
        tasteVariation = (SeekBar) findViewById(R.id.seekBar_tasteVariation);
        tasteSour = (SeekBar) findViewById(R.id.seekBar_tasteSour);
        tasteSweet = (SeekBar) findViewById(R.id.seekBar_tasteSweet);
        tasteBitter = (SeekBar) findViewById(R.id.seekBar_tasteBitter);
        tasteSpice = (SeekBar) findViewById(R.id.seekBar_tasteSpice);
        tasteSalt = (SeekBar) findViewById(R.id.seekBar_tasteSalty);
        fabFinish = (FloatingActionButton) findViewById(R.id.fab_createUser);

        mFirstNameValidator = new RequiredFieldValidator((TextInputLayout) findViewById(R.id.firstName_layout));
        mLastNameValidator = new RequiredFieldValidator((TextInputLayout) findViewById(R.id.lastName_layout));
        mNickNameValidator = new RequiredFieldValidator((TextInputLayout) findViewById(R.id.nickName_layout));


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_UserRg);
        toolbar.setTitle("Finish registering...");

        String displayName = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        nickName.setText(displayName);
        
        fabFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nickNameString = nickName.getText().toString();
                String firstNameString = firstName.getText().toString();
                String lastNameString = lastName.getText().toString();

                if (mNickNameValidator.validate(nickNameString) && mFirstNameValidator.validate(firstNameString) && mLastNameValidator.validate(lastNameString)) {
                    User currentUser = new User(nickNameString , firstNameString, lastNameString, FirebaseAuth.getInstance().getCurrentUser().getEmail(), ((double) 1 + tasteVariation.getProgress()), ((double) 1 + tasteSour.getProgress()), ((double) 1 + tasteSweet.getProgress()), ((double) 1 + tasteBitter.getProgress()), ((double) 1 + tasteSpice.getProgress()), ((double) 1 + tasteSalt.getProgress()), isInfoPublic.isChecked());
                    currentUser.createUserInDatabase(FirebaseAuth.getInstance().getCurrentUser().getUid());
                    Log.d(TAG, "fabOnClick: create user in database request sent.");
                    Intent mIntent = new Intent();
                    mIntent.putExtra("currentUser", currentUser);
                    setResult(RESULT_OK, mIntent);
                    finish();
                }
            }
        });
    }

    @Override
    protected void onStop() {
        setResult(RESULT_CANCELED);
        super.onStop();
    }

}
