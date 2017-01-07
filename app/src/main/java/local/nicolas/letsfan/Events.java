package local.nicolas.letsfan;

import java.util.LinkedHashMap;

/**
 * Created by soshy on 07/01/2017.
 */

public class Events {
    private Long creationTime;
    private Long dateDay;
    private Long dateMonth;
    private Long dateYear;
    private String organizerName;
    private String restaurantName;
    private String id;

    public Events() {}
    public Long getCreationTime() {return creationTime;}
    public Long getDateDay() {return dateDay;}
    public Long getDateMonth() {return dateMonth;}
    public Long getDateYear() {return dateYear;}
    public String getOrganizerName() {return organizerName;}
    public String getRestaurantName() {return restaurantName;}
    public String getId() {return id;}
}
