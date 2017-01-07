package local.nicolas.letsfan;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class User {
    private String firstName;
    private String lastName;
    private String nickName;
    private String email;
    private Double tasteVariation;
    private TasteVector tasteVector;
    private Boolean isInfoPublic;

    private static DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users");
    private static DatabaseReference restRef = FirebaseDatabase.getInstance().getReference("restos");
    private static DatabaseReference inviRef =  FirebaseDatabase.getInstance().getReference("invitations");

    public User(String _nick_name, String _first_name, String _last_name, String _email, Double _taste_variation, Double _taste_sour, Double _taste_sweet, Double _taste_bitter, Double _taste_spice, Double _taste_salty, Boolean _is_public) {
        nickName = _nick_name;
        firstName = _first_name;
        lastName = _last_name;
        email = _email;
        tasteVariation = _taste_variation;
        isInfoPublic = _is_public;
        tasteVector = new TasteVector(_taste_sour, _taste_sweet, _taste_bitter, _taste_spice, _taste_salty);
    }

    public void createUserInDatabase (String uid) {
        userRef.child(uid).child("firstName").setValue(firstName);
        userRef.child(uid).child("lastName").setValue(lastName);
        userRef.child(uid).child("nickName").setValue(nickName);
        userRef.child(uid).child("email").setValue(email);
        userRef.child(uid).child("tasteVariation").setValue(tasteVariation);
        userRef.child(uid).child("isInfoPublic").setValue(isInfoPublic);
        userRef.child(uid).child("tasteVector").child("sour").setValue(tasteVector.sour);
        userRef.child(uid).child("tasteVector").child("sweet").setValue(tasteVector.sweet);
        userRef.child(uid).child("tasteVector").child("bitter").setValue(tasteVector.bitter);
        userRef.child(uid).child("tasteVector").child("spice").setValue(tasteVector.spice);
        userRef.child(uid).child("tasteVector").child("salty").setValue(tasteVector.salty);
    }

    public void createInvitation (FirebaseDatabase db, String uid, int startTimeHour, int startTimeMinute, int endTimeHour, int endTimeMinute, int dateYear, int dateMonth, int dateDay, String _restaurant) {

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
        // TODO
        currentRef.child("restaurantName").setValue(restRef.child(_restaurant).child("name").toString());
        currentRef.child("tasteVariation").setValue(3);
        currentRef.child("creationTime").setValue(timeStamp);

        // index on invitationAttendees
        DatabaseReference invAttendeeRef = db.getReference("invitationAttendees");
        invAttendeeRef.child(pushID).child(uid).child("tasteVector").setValue(tasteVector);
        currentRef.child("startTimeHour").setValue(startTimeHour);
        currentRef.child("startTimeMinute").setValue(startTimeMinute);
        currentRef.child("endTimeHour").setValue(endTimeHour);
        currentRef.child("endTimeMinute").setValue(endTimeMinute);

        // index on userInEvents
        DatabaseReference userInEventsRef = db.getReference("userInEvents");
        currentRef.child("dateYear").setValue(dateYear);
        currentRef.child("dateMonth").setValue(dateMonth);
        currentRef.child("dateDay").setValue(dateDay);
        userInEventsRef.child(uid).child(pushID).child("organizerName").setValue(nickName);
        userInEventsRef.child(uid).child(pushID).child("creationTime").setValue(timeStamp);
    }

}

class TasteVector {
    protected Double sour;
    protected Double sweet;
    protected Double bitter;
    protected Double spice;
    protected Double salty;
    public TasteVector(Double _sour, Double _sweet, Double _bitter, Double _spice, Double _salty) {
        sour = _sour; sweet = _sweet; bitter = _bitter; spice = _spice; salty = _salty;
    }


}
