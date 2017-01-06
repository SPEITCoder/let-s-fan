package local.nicolas.letsfan;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by soshy on 20/12/2016.
 */

public class User {
    private String firstName;
    private String lastName;
    private String nickName;
    private String email;
    private Double tasteVariation;
    private TasteVector tasteVector;
    private Boolean isInfoPublic;

    static DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users");
    static DatabaseReference restRef = FirebaseDatabase.getInstance().getReference("restos");
    static DatabaseReference inviRef =  FirebaseDatabase.getInstance().getReference("invitations");

    public User(String _nick_name, String _first_name, String _last_name, String _email, Double _taste_variation, Double _taste_sour, Double _taste_sweet, Double _taste_bitter, Double _taste_spice, Double _taste_salty, Boolean _is_public) {
        nickName = _nick_name;
        firstName = _first_name;
        lastName = _last_name;
        email = _email;
        tasteVariation = _taste_variation;
        isInfoPublic = _is_public;
        tasteVector = new TasteVector(_taste_sour, _taste_sweet, _taste_bitter, _taste_spice, _taste_salty);
    }

    public String Stringtest()
    {
        return "String test: successful!";
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
        //db.getReference("users").child(uid).setValue(this);
    }

    public void createInvitation (FirebaseDatabase db, String uid, String _startTime, String _endTime, String _date, String _restaurant) {

        // invitation
        DatabaseReference currentRef = inviRef.push();
//        currentRef.setValue(new Invitation(_date, uid, this.tasteVariation, _restaurant, _startTime, _endTime));
        String pushID = currentRef.getKey();
        inviRef.child("pushID").child("date").setValue(_date);
        inviRef.child("pushID").child("organizer").setValue(uid);
        inviRef.child("pushID").child("organizerNickName").setValue(userRef.child(uid).child("nickName").getKey());
        inviRef.child("pushID").child("restaurant").setValue(_restaurant);
        inviRef.child("pushID").child("restaurantName").setValue(restRef.child(_restaurant).child("name").getKey());
        inviRef.child("pushID").child("tasteVariation").setValue(3);
        inviRef.child("pushID").child("startTime").setValue(_startTime);
        inviRef.child("pushID").child("endTime").setValue(_endTime);

        // index on invitationAttendees
        DatabaseReference invAttendeeRef = db.getReference("invitationAttendees");
        invAttendeeRef.child(pushID).child(uid).child("tasteVector").setValue(tasteVector);
        invAttendeeRef.child(pushID).child(uid).child("startTime").setValue(_startTime);
        invAttendeeRef.child(pushID).child(uid).child("endTime").setValue(_endTime);

        // index on userInEvents
        DatabaseReference userInEventsRef = db.getReference("userInEvents");
        userInEventsRef.child(uid).child(pushID).child("date").setValue(_date);
        userInEventsRef.child(uid).child(pushID).child("organizerName").setValue(nickName);
    }

    public void getUser(String uid)
    {

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
