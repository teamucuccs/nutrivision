
/**
 * Created by Byron Jan on 8/30/2016.
 */

package edu.ucuccs.nutrivision;


import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import java.io.File;

public class SecondActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        File pictureFile = (File)getIntent().getExtras().get("image");
        Uri imageUri = Uri.fromFile(pictureFile);
        ImageView imageView = (ImageView)findViewById(R.id.scanImage);
        imageView.setImageURI(imageUri);
    }

}
