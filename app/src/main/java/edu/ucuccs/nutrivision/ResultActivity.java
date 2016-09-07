/**
 * Created by Byron Jan on 8/30/2016.
 */

package edu.ucuccs.nutrivision;


import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import java.io.File;

public class ResultActivity extends AppCompatActivity {


    private Toolbar mToolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        mToolbar = (Toolbar) findViewById(R.id.main_toolbar);

        setUpToolbar();
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        File pictureFile = (File) getIntent().getExtras().get("image");
        Uri imageUri = Uri.fromFile(pictureFile);
        ImageView imageView = (ImageView) findViewById(R.id.imgResult);
        imageView.setImageURI(imageUri);
    }

    void setUpToolbar(){
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
