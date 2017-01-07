package local.nicolas.letsfan;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by dell on 2017/1/7.
 */

public class Invitation {
    private Long creationTime;
    private Long dateDay;
    private Long dateMonth;
    private Long dateYear;
    private Long endTimeHour;
    private Long endTimeMinute;
    private String organizer;
    private String organizerNickName;
    private String restaurant;
    private String restaurantName;
    private Long startTimeHour;
    private Long startTimeMinute;
    private Double tasteVariation;

    static DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users");
    static DatabaseReference restRef = FirebaseDatabase.getInstance().getReference("restos");

    private Invitation() {
    }

    public Invitation(Long _creationTime, Long _dateDay, Long _dateMonth, Long _dateYear, Long _endTimeHour, Long _endTimeMinute,
                      String _organizer, String _restaurant, Long _startTimeHour,
                      Long _startTimeMinute, Double _tasteVariation) {
        this.creationTime = _creationTime;
        this.dateDay = _dateDay;
        this.dateMonth = _dateMonth;
        this.dateYear = _dateYear;
        this.endTimeHour = _endTimeHour;
        this.endTimeMinute = _endTimeMinute;
        this.organizer = _organizer;
        this.organizerNickName = userRef.child(organizer).child("nickName").getKey();
        this.tasteVariation = _tasteVariation;
        this.restaurant = _restaurant;
        this.restaurantName = restRef.child(restaurant).child("name").getKey();
        this.startTimeHour = _startTimeHour;
        this.startTimeMinute = _startTimeMinute;

    }

    public Long getCreationTime() {
        return creationTime;
    }
    public Long getDateDay() {
        return dateDay;
    }
    public Long getDateMonth() {
        return dateMonth;
    }
    public Long getDateYear() {
        return dateYear;
    }
    public Long getEndTimeHour() {
        return endTimeHour;
    }
    public Long getEndTimeMinute() {
        return endTimeMinute;
    }
    public Long getStartTimeHour() {
        return startTimeHour;
    }
    public Long getStartTimeMinute() {
        return startTimeMinute;
    }
    public String getOrganizer() {return organizer;}
    public String getOrganizerNickName(){return organizerNickName;}
    public String getRestaurant(){return restaurant;}
    public String getRestaurantName(){return restaurantName;}
    public Double getTasteVariation(){return tasteVariation;}

}
