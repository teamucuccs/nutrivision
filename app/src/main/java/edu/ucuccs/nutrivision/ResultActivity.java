/**
 * Created by Byron Jan on 8/30/2016.
 */

package edu.ucuccs.nutrivision;


import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.clarifai.api.ClarifaiClient;
import com.clarifai.api.RecognitionRequest;
import com.clarifai.api.RecognitionResult;
import com.clarifai.api.Tag;
import com.clarifai.api.exception.ClarifaiException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ResultActivity extends AppCompatActivity {


    private Toolbar mToolbar;
    private TextView lblResultTags;
    private FloatingActionButton fabCapturePhoto;
    private ImageView imgResult;
    private static final int CODE_PICK = 1;
    private static final String TAG = ResultActivity.class.getSimpleName();
    private final ClarifaiClient client = new ClarifaiClient(Credentials.CLIENT_ID, Credentials.CLIENT_SECRET);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        mToolbar = (Toolbar) findViewById(R.id.main_toolbar);
        imgResult = (ImageView) findViewById(R.id.imgResult);
        lblResultTags = (TextView) findViewById(R.id.lblResultTags);
        fabCapturePhoto = (FloatingActionButton) findViewById(R.id.fabCapturePhoto);
        fabCapturePhoto.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                final Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, CODE_PICK);
            }
        });

        setUpToolbar();
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Uri photoURI = Uri.parse(getIntent().getExtras().getString("image"));
        submitPhotoToClarifai(photoURI);
    }

    void setUpToolbar(){
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    void submitPhotoToClarifai(Uri uri){
        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (bitmap != null) {
            imgResult.setImageBitmap(bitmap);
            lblResultTags.setText("Recognizing...");
            fabCapturePhoto.setEnabled(false);

            // Run recognition on a background thread since it makes a network call.
            new AsyncTask<Bitmap, Void, RecognitionResult>() {
                @Override protected RecognitionResult doInBackground(Bitmap... bitmaps) {
                    return recognizeBitmap(bitmaps[0]);
                }
                @Override protected void onPostExecute(RecognitionResult result) {
                    updateUIForResult(result);
                }
            }.execute(bitmap);
        } else {
            lblResultTags.setText("Unable to load selected image.");
        }
    }


    @Override protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == CODE_PICK && resultCode == RESULT_OK) {
            submitPhotoToClarifai(intent.getData());
        }
    }

    private RecognitionResult recognizeBitmap(Bitmap bitmap) {
        try {
            // Scale down the image. This step is optional. However, sending large images over the
            // network is slow and  does not significantly improve recognition performance.
            Bitmap scaled = Bitmap.createScaledBitmap(bitmap, 320,
                    320 * bitmap.getHeight() / bitmap.getWidth(), true);

            // Compress the image as a JPEG.
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            scaled.compress(Bitmap.CompressFormat.JPEG, 90, out);
            byte[] jpeg = out.toByteArray();

            // Send the JPEG to Clarifai and return the result.
            return client.recognize(new RecognitionRequest(jpeg)).get(0);
        } catch (ClarifaiException e) {
            Log.e(TAG, "Clarifai error", e);
            return null;
        }
    }

    /** Updates the UI by displaying tags for the given result. */
    private void updateUIForResult(RecognitionResult result) {
        if (result != null) {
            if (result.getStatusCode() == RecognitionResult.StatusCode.OK) {
                // Display the list of tags in the UI.
                StringBuilder b = new StringBuilder();
                for (Tag tag : result.getTags()) {
                    b.append(b.length() > 0 ? ", " : "").append(tag.getName());
                }
                lblResultTags.setText("Tags:\n" + b);
            } else {
                Log.e(TAG, "Clarifai: " + result.getStatusMessage());
                lblResultTags.setText("Sorry, there was an error recognizing your image.");
            }
        } else {
            lblResultTags.setText("Sorry, there was an error recognizing your image.");
        }
        fabCapturePhoto.setEnabled(true);
    }
}
