package edu.ucuccs.nutrivision;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.afollestad.materialcamera.MaterialCamera;
import com.github.clans.fab.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    private final static int CAMERA_RQ = 6969;
    private final static int RESULT_LOAD_IMAGE = 1;

    FloatingActionButton mFabCam, mFabBrowse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFabCam = (FloatingActionButton) findViewById(R.id.menu_camera);
        mFabBrowse = (FloatingActionButton) findViewById(R.id.menu_browse);


        mFabCam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cameraShot();
            }
        });

        mFabBrowse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                browseGal();
            }
        });
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void cameraShot() {

        MaterialCamera materialCamera = new MaterialCamera(this)
                .showPortraitWarning(true)
                .allowRetry(true)
                .defaultToFrontFacing(false);

        materialCamera.stillShot();
        materialCamera.start(CAMERA_RQ);

    }

    public void browseGal() {
        Intent i = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, RESULT_LOAD_IMAGE);
    }

}
