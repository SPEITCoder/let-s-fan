package local.nicolas.letsfan;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by soshy on 28/12/2016.
 */

public class Invitation {
    String date;
    String organizer;
    String organizerNickName;
    Double tasteVariation;
    String restaurant;
    String restaurantName;
    String startTime;
    String endTime;
    static DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users");
    static DatabaseReference restRef = FirebaseDatabase.getInstance().getReference("restos");
    public Invitation (String _date, String _organizer, Double _tasteVariation, String _restaurant, String _startTime, String _endTime) {
        date = _date;
        organizer = _organizer;
        organizerNickName = userRef.child(organizer).child("nickName").getKey();
        tasteVariation = _tasteVariation;
        restaurant = _restaurant;
        restaurantName = restRef.child(restaurant).child("name").getKey();
        startTime = _startTime;
        endTime = _endTime;
    }
}
