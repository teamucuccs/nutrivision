/**
 * Author: UCU Knight Coders on 9/13/2016.
 * Website: http://facebook.com/teamucuccs
 * E-mail: teamucuccs@gmail.com
 */

package edu.ucuccs.nutrivision;

class Credentials {

    interface CLARIFAI {

        String CLIENT_ID = "HyvKQpTZGPqeqOBT-iiumKO0wBToi2dUXsLY-_-y";
        String CLIENT_SECRET = "KWWtZYOKP_uW3aq7SGZ7Tfjj2WjlXqPzKg8b1OJI";
    }

    interface NUTRIONIX {
        String APP_ID = "F5fbBfo8MZtjMeeNWn_Fc9pC-u97usHofgiNz5tp";
        String API_KEY = "070502cd54fa6bfb1790e382e10c7731";
    }

    interface NUTRIVISION{
        String RESTAURANTS_URL = "http://ucu.edu.ph/nutrivision-api/v1/restaurants";
    }

    String returnURL(String keyword){
        return "https://api.nutritionix.com/v1_1/search/" + keyword + "?fields=item_name%2Citem_id%2Cbrand_name" +
                "%2Cnf_serving_size_qty%2Cnf_serving_size_unit%2Cnf_calories_from_fat%2Cnf_saturated_fat%2Cnf_trans_fatty_acid" +
                "%2Cnf_cholesterol%2Cnf_sodium%2Cnf_total_carbohydrate%2Cnf_calories%2Cnf_total_fat" +
                "%2Cnf_dietary_fiber%2Cnf_sugars%2Cnf_protein%2Cnf_vitamin_a_dv%2Cnf_vitamin_c_dv" +
                "%2Cnf_calcium_dv%2Cnf_iron_dv&appId=" + NUTRIONIX.APP_ID + "&appKey=" + NUTRIONIX.API_KEY;
    }
}