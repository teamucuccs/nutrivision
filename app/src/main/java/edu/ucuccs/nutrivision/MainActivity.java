package edu.ucuccs.nutrivision;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.afollestad.materialcamera.MaterialCamera;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private final static int CAMERA_RQ = 6969;
    private final static int RESULT_LOAD_IMAGE = 1;

    FloatingActionButton mFabCam, mFabBrowse;
    FloatingActionMenu fabMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFabCam = (FloatingActionButton) findViewById(R.id.menu_camera);
        mFabBrowse = (FloatingActionButton) findViewById(R.id.menu_browse);
        fabMenu = (FloatingActionMenu)findViewById(R.id.fab_menu);


        mFabCam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cameraShot();
                fabMenu.close(true);
            }
        });

        mFabBrowse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                browseGal();
                fabMenu.close(true);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_RQ) {
            if (resultCode == RESULT_OK) {
                File file = new File(data.getData().getPath());
                Intent i = new Intent(this, SecondActivity.class);
                i.putExtra("image", file);
                startActivity(i);

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

            File file = new File(picturePath);

            Intent i = new Intent(this, SecondActivity.class);
            i.putExtra("image", file);
            startActivity(i);

        }

    }
}
