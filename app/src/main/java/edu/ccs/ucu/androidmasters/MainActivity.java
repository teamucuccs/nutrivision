package edu.ccs.ucu.androidmasters;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.afollestad.materialcamera.MaterialCamera;
import com.github.clans.fab.FloatingActionButton;

import java.io.File;
import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {

    private final static int CAMERA_RQ = 6969;
    private final static int RESULT_LOAD_IMAGE = 1;

    FloatingActionButton fabCam, fabBrowse;
    //This is a sample comment na gawa ni Aljohn. Tutorial ng Git

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fabCam = (FloatingActionButton) findViewById(R.id.menu_camera);
        fabBrowse = (FloatingActionButton) findViewById(R.id.menu_browse);


        fabCam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cameraShot();
            }
        });

        fabBrowse.setOnClickListener(new View.OnClickListener() {
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
                .defaultToFrontFacing(true);

        materialCamera.stillShot();
        materialCamera.start(CAMERA_RQ);

    }

    public void browseGal() {
        Intent i = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, RESULT_LOAD_IMAGE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Received recording or error from MaterialCamera
        if (requestCode == CAMERA_RQ) {
            if (resultCode == RESULT_OK) {
                final File file = new File(data.getData().getPath());
                Uri imageUri = Uri.fromFile(file);
                ImageView image = (ImageView) findViewById(R.id.scanImage);
                image.setImageURI(imageUri);
            } else if (data != null) {
                Exception e = (Exception) data.getSerializableExtra(MaterialCamera.ERROR_EXTRA);
                if (e != null) {
                    e.printStackTrace();
                    Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }

        }
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            ImageView image = (ImageView) findViewById(R.id.scanImage);
            image.setImageBitmap(BitmapFactory.decodeFile(picturePath));

        }

    }

}
