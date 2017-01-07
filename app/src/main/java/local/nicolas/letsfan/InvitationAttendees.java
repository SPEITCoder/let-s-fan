package local.nicolas.letsfan;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by dell on 2017/1/7.
 */

public class InvitationAttendees {
    //private String attendeeId;
    private String nickName;
    private HashMap<String, Double> tasteVector;
    private Long startTimeHour;
    private Long startTimeMinute;
    private Long endTimeHour;
    private Long endTimeMinute;

    private InvitationAttendees(){}

    //public String getId{return attendeeId;}
    public String getNickName(){return nickName;}
    public HashMap<String, Double> getTasteVector() {return tasteVector;}
    public Long getStartTimeHour() {return startTimeHour;}
    public Long getStartTimeMinute() {return startTimeMinute;}
    public Long getEndTimeHour() {return  endTimeHour;}
    public Long getEndTimeMinute() {return endTimeMinute;}

}
