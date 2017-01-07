package local.nicolas.letsfan;

import android.app.Application;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by dell on 2017/1/4.
 */

public class Data extends Application {
    private User currentUser;
    private FirebaseAuth mFirebaseAuth;
    private static final int RC_SIGN_IN = 9001;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference userRef;

    public User getUser()
    {
        return this.currentUser;
    }
    public void setUser(User u)
    {
        this.currentUser=u;
    }

    public FirebaseAuth getmFirebaseAuth()
    {
        return this.mFirebaseAuth;
    }
    public void setmFirebaseAuth(FirebaseAuth f)
    {
        this.mFirebaseAuth=f;
    }

    public FirebaseDatabase getmFirebaseDatabase()
    {
        return this.mFirebaseDatabase;
    }
    public void setmFirebaseDatabase(FirebaseDatabase b)
    {
        this.mFirebaseDatabase=b;
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
    }
}
