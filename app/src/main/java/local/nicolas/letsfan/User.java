package local.nicolas.letsfan;

import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.util.ArrayMap;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class User implements Serializable {
    private String firstName;
    private String lastName;
    private String nickName;
    private String email;
    private Double tasteVariation;
    private Map<String, Double> tasteVector;
    private Boolean isInfoPublic;


    public String getEmail() {return email;}
    public String getFirstName() {return firstName;}
    public String getLastName() {return lastName;}
    public String getNickName() {return nickName;}
    public Double getTasteVariation() {return tasteVariation;}
    public Map<String, Double> getTasteVector() { return tasteVector;}
    public Boolean getIsInfoPublic() {return isInfoPublic;}

    private static DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users");
    private static DatabaseReference restRef = FirebaseDatabase.getInstance().getReference("restos");
    private static DatabaseReference inviRef =  FirebaseDatabase.getInstance().getReference("invitations");

    private User () {}

    public User(String _nick_name, String _first_name, String _last_name, String _email, Double _taste_variation, Double _taste_sour, Double _taste_sweet, Double _taste_bitter, Double _taste_spice, Double _taste_salty, Boolean _is_public) {
        nickName = _nick_name;
        firstName = _first_name;
        lastName = _last_name;
        email = _email;
        tasteVariation = _taste_variation;
        isInfoPublic = _is_public;
        tasteVector = new HashMap<>();
        tasteVector.put("sour", _taste_sour);
        tasteVector.put("sweet", _taste_sweet);
        tasteVector.put("spice", _taste_spice);
        tasteVector.put("bitter", _taste_bitter);
        tasteVector.put("salty", _taste_salty);
    }

    public void createUserInDatabase (String uid) {
        userRef.child(uid).child("firstName").setValue(firstName);
        userRef.child(uid).child("lastName").setValue(lastName);
        userRef.child(uid).child("nickName").setValue(nickName);
        userRef.child(uid).child("email").setValue(email);
        userRef.child(uid).child("tasteVariation").setValue(tasteVariation);
        userRef.child(uid).child("isInfoPublic").setValue(isInfoPublic);
        userRef.child(uid).child("tasteVector").setValue(tasteVector);
    }

    public void joinInvitation (Invitation invitation) {
        String pushID = invitation.getId();
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        // index on invitationAttendees
        DatabaseReference invAttendeeRef = FirebaseDatabase.getInstance().getReference("invitationAttendees");
        invAttendeeRef.child(pushID).child(uid).child("tasteVector").setValue(tasteVector);
        // TODO get user free time
        invAttendeeRef.child(pushID).child(uid).child("startTimeHour").setValue(invitation.getStartTimeHour());
        invAttendeeRef.child(pushID).child(uid).child("startTimeMinute").setValue(invitation.getStartTimeMinute());
        invAttendeeRef.child(pushID).child(uid).child("endTimeHour").setValue(invitation.getEndTimeHour());
        invAttendeeRef.child(pushID).child(uid).child("endTimeMinute").setValue(invitation.getEndTimeMinute());
        invAttendeeRef.child(pushID).child(uid).child("nickName").setValue(nickName);

        // index on userInEvents
        DatabaseReference userInEventsRef = FirebaseDatabase.getInstance().getReference("userInEvents");
        userInEventsRef.child(uid).child(pushID).child("dateYear").setValue(invitation.getDateYear());
        userInEventsRef.child(uid).child(pushID).child("dateMonth").setValue(invitation.getDateMonth());
        userInEventsRef.child(uid).child(pushID).child("dateDay").setValue(invitation.getDateDay());
        userInEventsRef.child(uid).child(pushID).child("organizerName").setValue(invitation.getOrganizerNickName());
        userInEventsRef.child(uid).child(pushID).child("restaurantName").setValue(invitation.getRestaurantName());
        userInEventsRef.child(uid).child(pushID).child("creationTime").setValue(invitation.getCreationTime());
        userInEventsRef.child(uid).child(pushID).child("id").setValue(pushID);


    }

    public void createInvitation (FirebaseDatabase db, String uid, int startTimeHour, int startTimeMinute, int endTimeHour, int endTimeMinute, int dateYear, int dateMonth, int dateDay, String _restaurant, String _restaurantName) {

        // timestamp
        Long timeStamp = System.currentTimeMillis();

        // invitation
        DatabaseReference currentRef = inviRef.push();
        String pushID = currentRef.getKey();
        currentRef.child("dateYear").setValue(dateYear);
        currentRef.child("dateMonth").setValue(dateMonth);
        currentRef.child("dateDay").setValue(dateDay);
        currentRef.child("startTimeHour").setValue(startTimeHour);
        currentRef.child("startTimeMinute").setValue(startTimeMinute);
        currentRef.child("endTimeHour").setValue(endTimeHour);
        currentRef.child("endTimeMinute").setValue(endTimeMinute);
        currentRef.child("organizer").setValue(uid);
        currentRef.child("organizerNickName").setValue(nickName);
        currentRef.child("restaurant").setValue(_restaurant);
        currentRef.child("restaurantName").setValue(_restaurantName);
        currentRef.child("tasteVariation").setValue(3);
        currentRef.child("creationTime").setValue(timeStamp);
        currentRef.child("id").setValue(pushID);

        // index on invitationAttendees
        DatabaseReference invAttendeeRef = db.getReference("invitationAttendees");
        invAttendeeRef.child(pushID).child(uid).child("tasteVector").setValue(tasteVector);
        invAttendeeRef.child(pushID).child(uid).child("startTimeHour").setValue(startTimeHour);
        invAttendeeRef.child(pushID).child(uid).child("startTimeMinute").setValue(startTimeMinute);
        invAttendeeRef.child(pushID).child(uid).child("endTimeHour").setValue(endTimeHour);
        invAttendeeRef.child(pushID).child(uid).child("endTimeMinute").setValue(endTimeMinute);
        invAttendeeRef.child(pushID).child(uid).child("nickName").setValue(nickName);

        // index on userInEvents
        DatabaseReference userInEventsRef = db.getReference("userInEvents");
        userInEventsRef.child(uid).child(pushID).child("dateYear").setValue(dateYear);
        userInEventsRef.child(uid).child(pushID).child("dateMonth").setValue(dateMonth);
        userInEventsRef.child(uid).child(pushID).child("dateDay").setValue(dateDay);
        userInEventsRef.child(uid).child(pushID).child("organizerName").setValue(nickName);
        userInEventsRef.child(uid).child(pushID).child("restaurantName").setValue(_restaurantName);
        userInEventsRef.child(uid).child(pushID).child("creationTime").setValue(timeStamp);
        userInEventsRef.child(uid).child(pushID).child("id").setValue(pushID);
    }

}

