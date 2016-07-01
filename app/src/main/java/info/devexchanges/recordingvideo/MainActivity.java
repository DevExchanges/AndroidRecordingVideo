package info.devexchanges.recordingvideo;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;
import java.text.SimpleDateFormat;

public class MainActivity extends AppCompatActivity {

    private Uri fileUri;
    private String timeStamp;
    public static final int MEDIA_TYPE_VIDEO = 2;
    private static final int CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE = 200;

    public TextView noticeText;
    private Button buttonRecording;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonRecording = (Button) findViewById(R.id.btn_record);
        noticeText = (TextView) findViewById(R.id.text_view);

        buttonRecording.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                // create new Intentwith with Standard Intent action that can be
                // sent to have the camera application capture an video and return it.
                Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

                // create a file to save the video
                fileUri = getOutputMediaFileUri(MEDIA_TYPE_VIDEO);

                // set the image file name
                intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

                // set the video image quality to high
                intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);

                // start the Video Capture Intent
                startActivityForResult(intent, CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE);
            }
        });
    }

    /**
     * Create a file Uri for saving an image or video
     */
    private Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    /**
     * Create a File for saving an image or video
     */
    @SuppressLint({"SetTextI18n", "SimpleDateFormat"})
    private File getOutputMediaFile(int type) {

        // Check that the SDCard is mounted
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "DevExVideo");

        // Create the storage directory("DevExVideo") if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                noticeText.setText("Failed to create directory DevExVideo");
                return null;
            }
        }

        // Create a media file name
        // For unique file name appending current timeStamp with file name
        java.util.Date date = new java.util.Date();
        timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(date.getTime());

        File mediaFile;
        if (type == MEDIA_TYPE_VIDEO) {
            // For unique video file name appending current timeStamp with file name
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + "VID_" + timeStamp + ".mp4");

        } else return null;

        return mediaFile;
    }


    @SuppressLint("SetTextI18n")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                noticeText.setText("Video file has been saved!");
            } else if (resultCode == RESULT_CANCELED) {
                noticeText.setText("User cancelled the video capture.");
            } else {
                noticeText.setText("Video capture failed.");
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        savedInstanceState.putString("text", noticeText.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if (savedInstanceState != null) {
            noticeText.setText(savedInstanceState.getString("text"));
        }
    }
}