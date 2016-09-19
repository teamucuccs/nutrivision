package edu.ucuccs.nutrivision;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ResultActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private String mTagTitle;
    private ImageView imgResult;
    private static final String TAG = ResultActivity.class.getSimpleName();
    private ProgressDialog pDialog;
    private RecyclerView recyFoods;
    private String  tag_string_req = "string_req";

    private ArrayList<String> mArrayID= new ArrayList<>();
    private ArrayList<String> mArrayName = new ArrayList<>();
    private ArrayList<String> mArrayBrand = new ArrayList<>();
    private ArrayList<String> mArrayCalories = new ArrayList<>();
    private ArrayList<String> mArrayFat = new ArrayList<>();
    private ArrayList<String> mArrayQty = new ArrayList<>();
    private ArrayList<String> mArrayUnit = new ArrayList<>();
    String data="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        mToolbar        = (Toolbar) findViewById(R.id.result_toolbar);
        imgResult       = (ImageView) findViewById(R.id.img_result);
        recyFoods       = (RecyclerView) findViewById(R.id.recyFoods);

        recyFoods.setHasFixedSize(true);


        mTagTitle = (String) getIntent().getExtras().get("str_tag");

        setUpToolbar();
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
            }
        });
        loadNutriFacts();
    }
    void setUpToolbar(){
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(mTagTitle.substring(0,1).toUpperCase() + mTagTitle.substring(1).toLowerCase());
    }
    private void loadNutriFacts() {
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest obreq = new JsonObjectRequest(Request.Method.GET,  new Credentials().returnURL(mTagTitle),null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        pDialog.hide();
                        try {
                            JSONArray arrHits = response.getJSONArray("hits");
                            Log.d(TAG, "onResponse: " + arrHits.length());
                            Log.d(TAG, "response: " + response.toString());
                            clearArray();
                            for (int i = 0; i < arrHits.length(); i++) {

                                JSONObject objFields = arrHits.getJSONObject(i);
                                JSONObject test = objFields.getJSONObject("fields");
                                Log.d(TAG, "objFields: " + objFields.toString());

                                mArrayID.add(test.getString("item_id"));
                                mArrayName.add(test.getString("item_name"));
                                mArrayBrand.add(test.getString("brand_name"));
                                mArrayCalories.add(test.getString("nf_calories"));
                                mArrayFat.add(test.getString("nf_total_fat"));
                                mArrayQty.add(test.getString("nf_serving_size_qty"));
                                mArrayUnit.add(test.getString("nf_serving_size_unit"));

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
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.hide();
                        Log.e("Volley", "Error");
                    }
                }
        );
        requestQueue.add(obreq);
    }


    private void clearArray() {
        mArrayID.clear();
        mArrayName.clear();
        mArrayBrand.clear();
        mArrayCalories.clear();
        mArrayFat.clear();
        mArrayQty.clear();
        mArrayUnit.clear();
    }
    private class FooClass implements Serializable {
        String food_id;
        String food_name;
        String food_brand;
        String food_calories;
        String food_fat;
        String food_ty;
        String food_unit;
    }


    private List<FooClass> feedListContent(ArrayList mArrayID) {
        List<FooClass> result = new ArrayList<>();
        for (int i = 0; i < mArrayID.size(); i++) {
            FooClass ci = new FooClass();
            ci.food_id = mArrayID.get(i).toString();
            ci.food_name = mArrayName.get(i);
            ci.food_brand = mArrayBrand.get(i);
            ci.food_calories = mArrayCalories.get(i);
            ci.food_fat = mArrayFat.get(i);
            ci.food_ty = mArrayQty.get(i);
            ci.food_unit = mArrayUnit.get(i);
            result.add(ci);
        }
        return result;
    }
    private class FoodsAdapter extends RecyclerView.Adapter {
        private final Context applicationContext;
        private final List<FooClass> foodList;
        private final RecyclerView recyFoods;

        public FoodsAdapter(Context applicationContext, List<FooClass> foodList, RecyclerView recyFoods) {
            this.applicationContext = applicationContext;
            this.foodList = foodList;
            this.recyFoods = recyFoods;
        }
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View rowView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_nutri_facts, parent, false);
            return new SongListHolder(rowView);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder hold, int position) {
            final FooClass ci = foodList.get(position);
            SongListHolder holder = ((SongListHolder) hold);
            holder.lblFoodTitle.setText(ci.food_name);
            holder.lblCalories.setText(ci.food_calories);
            holder.lblFat.setText(ci.food_fat);
            holder.lblQuantity.setText(ci.food_ty);
            holder.lblServings.setText(ci.food_unit);
        }

        @Override
        public int getItemCount() {
            return foodList.size();
        }

        class SongListHolder extends RecyclerView.ViewHolder {
            final TextView lblFoodTitle, lblCalories, lblFat, lblQuantity, lblServings;

            public SongListHolder(View itemView) {
                super(itemView);
                lblFoodTitle = (TextView) itemView.findViewById(R.id.lblFoodTitle);
                lblCalories = (TextView) itemView.findViewById(R.id.lblCalories);
                lblFat = (TextView) itemView.findViewById(R.id.lblFat);
                lblQuantity = (TextView) itemView.findViewById(R.id.lblQuantity);
                lblServings = (TextView) itemView.findViewById(R.id.lblServings);
            }
        }

        public void resetData(List<FooClass> listSong) {
            this.foodList.clear();
            this.foodList.addAll(listSong);
            notifyDataSetChanged();
        }
    }
}
