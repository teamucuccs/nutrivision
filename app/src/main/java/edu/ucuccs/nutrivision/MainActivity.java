package edu.ucuccs.nutrivision;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.clarifai.api.ClarifaiClient;
import com.clarifai.api.RecognitionRequest;
import com.clarifai.api.RecognitionResult;
import com.clarifai.api.Tag;
import com.clarifai.api.exception.ClarifaiException;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import edu.ucuccs.nutrivision.custom.AdjustableLayout;

import static android.provider.MediaStore.Images.Media;

public class MainActivity extends AppCompatActivity {

    //Test

    private final ClarifaiClient client = new ClarifaiClient(Credentials.CLARIFAI.CLIENT_ID, Credentials.CLARIFAI.CLIENT_SECRET);
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int CODE_PICK = 1;
    private Intent data;

    private final List<String> tagsListInitial = new ArrayList<>();
    private FloatingActionButton mFabCam, mFabBrowse;
    private FloatingActionMenu fabMenu;
    private AdjustableLayout adjustableLayout;
    private TextView mLblResultTags;
    private ImageView imgResult;
    private Toolbar mToolbar;

    private LinearLayout mLinearEmpty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar        = (Toolbar) findViewById(R.id.toolbar);
        mFabCam         = (FloatingActionButton) findViewById(R.id.menu_camera);
        mFabBrowse      = (FloatingActionButton) findViewById(R.id.menu_browse);
        fabMenu         = (FloatingActionMenu)findViewById(R.id.fab_menu);
        imgResult       = (ImageView) findViewById(R.id.img_result);
        mLblResultTags  = (TextView) findViewById(R.id.lbl_result_tag);

        mLinearEmpty = (LinearLayout) findViewById(R.id.layout_empty_state);

        setUpToolbar();

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
                browseGallery();
                fabMenu.close(true);
            }
        });
    }

    void setUpToolbar(){
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void cameraShot() {

    }

    public void browseGallery() {
        final Intent intent = new Intent(Intent.ACTION_PICK, Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, CODE_PICK);
    }

    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CODE_PICK && resultCode == RESULT_OK) {
            this.data = data;
            mLinearEmpty.setVisibility(View.GONE);
            Log.d(TAG, "onActivityResult: ");

            final Bitmap bitmap = loadBitmapFromUri(data.getData());
            if (bitmap != null) {
                imgResult.setImageBitmap(bitmap);
                mLblResultTags.setText("Recognizing...");

                new AsyncTask<Bitmap, Void, RecognitionResult>() {
                    @Override protected RecognitionResult doInBackground(Bitmap... bitmaps) {
                        Log.d(TAG, "doInBackground: "  + bitmaps[0]);
                        return recognizeBitmap(bitmaps[0]);
                    }
                    @Override protected void onPostExecute(RecognitionResult result) {
                        Log.d(TAG, "onPostExecute: " + result);
                        updateUIForResult(result);
                    }
                }.execute(bitmap);
            } else {
                mLblResultTags.setText("Unable to load selected image.");
            }
        }
    }
    private Bitmap loadBitmapFromUri(Uri uri) {
        try {
            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(getContentResolver().openInputStream(uri), null, opts);
            int sampleSize = 1;
            while (opts.outWidth / (2 * sampleSize) >= imgResult.getWidth() &&
                    opts.outHeight / (2 * sampleSize) >= imgResult.getHeight()) {
                sampleSize *= 2;
            }

            opts = new BitmapFactory.Options();
            opts.inSampleSize = sampleSize;
            return BitmapFactory.decodeStream(getContentResolver().openInputStream(uri), null, opts);
        } catch (IOException e) {
            Log.e(TAG, "Error loading image: " + uri, e);
        }
        return null;
    }

    private RecognitionResult recognizeBitmap(Bitmap bitmap) {
        try {
            Bitmap scaled = Bitmap.createScaledBitmap(bitmap, 320,
                    320 * bitmap.getHeight() / bitmap.getWidth(), true);

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            scaled.compress(Bitmap.CompressFormat.JPEG, 90, out);
            byte[] jpeg = out.toByteArray();

            return client.recognize(new RecognitionRequest(jpeg)).get(0);
        } catch (ClarifaiException e) {
            return null;
        }
    }

    private void updateUIForResult(RecognitionResult result) {
        tagsListInitial.clear();

        if (result != null) {
            if (result.getStatusCode() == RecognitionResult.StatusCode.OK) {
                StringBuilder b = new StringBuilder();
                for (Tag tag : result.getTags()) {
                    tagsListInitial.add(tag.getName());
                    b.append(b.length() > 0 ? ", " : "").append(tag.getName());
                }
                mLblResultTags.setVisibility(View.GONE);
                addChipsViewFinal(tagsListInitial);
            } else {
                mLblResultTags.setText("Sorry, there was an error recognizing your image.");
            }
        } else {
            mLblResultTags.setText("Sorry, there was an error recognizing your image.");
        }
    }
    private void addChipsViewFinal(List<String> tagList) {
        adjustableLayout = (AdjustableLayout) findViewById(R.id.container);
        adjustableLayout.removeAllViews();
        for (int i = 0; i < tagList.size(); i++) {
            final View newView          = LayoutInflater.from(this).inflate(R.layout.layout_view_chip_text, null);
            LinearLayout linearChipTag  = (LinearLayout) newView.findViewById(R.id.linear_chip_tag);
            final TextView txtChipTag         = (TextView) newView.findViewById(R.id.txt_chip_content);

            linearChipTag.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String tempTags = txtChipTag.getText().toString();

                    File file = new File(data.getData().getPath());
                    Intent i = new Intent(getApplicationContext(), ResultActivity.class);
                    i.putExtra("str_tag", tempTags);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                }
            });
            txtChipTag.setText(tagList.get(i));
            adjustableLayout.addingMultipleView(newView);
        }
        adjustableLayout.invalidateView();
    }

}
