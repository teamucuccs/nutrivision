/**
 * Author: UCU Knight Coders on 9/13/2016.
 * Website: http://facebook.com/teamucuccs
 * E-mail: teamucuccs@gmail.com
 */

package edu.ucuccs.nutrivision;

class Credentials {

    public String  nutrionixURL;

    public interface CLARIFAI {
        String CLIENT_ID = "pB5Pk5fxJBp5ayc9ag193nedFRB3eWIRhLohRsbi";
        String CLIENT_SECRET = "j7eZYHcmI4CDo1MqazWWYKvcESm-NdXaL-woHjjI";
    }
    public interface NUTRIONIX {
        String APP_ID = "f8d8df72";
        String API_KEY = "d9aea37b804ffef0a9cdc1378638706c";
    }

    public String returnURL(String keyword){
        nutrionixURL = "https://api.nutritionix.com/v1_1/search/" + keyword + "?fields=item_name%2Citem_id%2Cbrand_name%2Cnf_calories%2Cnf_total_fat&appId=" + NUTRIONIX.APP_ID + "&appKey=" + NUTRIONIX.API_KEY;
        return nutrionixURL;
    }
}