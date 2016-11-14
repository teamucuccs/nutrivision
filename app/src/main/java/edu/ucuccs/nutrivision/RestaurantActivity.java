package edu.ucuccs.nutrivision;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class RestaurantActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private ArrayList<String> mRestoID = new ArrayList<>();
    private ArrayList<String> mRestoName = new ArrayList<>();
    private ArrayList<String> mRestoLogo = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setUpToolbar();
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
            }
        });
    }
    void setUpToolbar(){
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    private void loadRestaurants() {
        /*pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading Restaurants...");
        pDialog.show();

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest obreq = new JsonObjectRequest(Request.Method.GET,  Credentials.NUTRIVISION.RESTAURANTS_URL, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                pDialog.hide();
                try {
                    JSONArray arrResto = response.getJSONArray("resto");
                    clearArray();
                    for (int i = 0; i < arrResto.length(); i++) {

                        JSONObject objFields = arrResto.getJSONObject(i);
                        mRestoID.add(objFields.getString("resto_id"));
                        mRestoName.add(objFields.getString("resto_name"));
                        mRestoLogo.add(objFields.getString("resto_logo"));

                    }
                    FoodsAdapter adapter;
                    if (recyFoods.getAdapter() == null) {
                        adapter = new FoodsAdapter(getApplicationContext(), feedListContent(mArrayID), recyFoods);
                        recyFoods.setAdapter(adapter);
                    } else {
                        adapter = ((FoodsAdapter) recyFoods.getAdapter());
                        adapter.resetData(feedListContent(mArrayID));
                    }
                }
                catch (JSONException e) {
                    e.printStackTrace();
                    nutriText.setVisibility(View.VISIBLE);
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.hide();
                        nutriText.setVisibility(View.VISIBLE);
                        Log.e("Volley", "Error");
                    }
                }
        );
        requestQueue.add(obreq);*/
    }
}
