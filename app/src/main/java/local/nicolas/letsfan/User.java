package local.nicolas.letsfan;

import java.util.ArrayList;

/**
 * Created by soshy on 20/12/2016.
 */

public class User {
    private String first_name;
    private String last_name;
    private String email;
    private Double taste_variation;
    private Double taste_sour;
    private Double taste_sweet;
    private Double taste_bitter;
    private Double taste_spice;
    private Double taste_salty;

    public User(String _first_name, String _last_name, String _email, Double _taste_variation, Double _taste_sour, Double _taste_sweet, Double _taste_bitter, Double _taste_spice, Double _taste_salty) {
        first_name = _first_name;
        last_name = _last_name;
        email = _email;
        taste_variation = _taste_variation;
        taste_sour = _taste_sour;
        taste_sweet = _taste_sweet;
        taste_bitter = _taste_bitter;
        taste_spice = _taste_spice;
        taste_salty = _taste_salty;
    }

}
