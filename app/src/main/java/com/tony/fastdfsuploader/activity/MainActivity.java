package com.tony.fastdfsuploader.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.tony.fastdfsuploader.R;
import com.tony.fastdfsuploader.jni.FastDFSUploader;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";

    static {

        try {
            System.loadLibrary("fastdfsuploader");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static final int SELECT_VIDEO = 1000;

    private EditText et_filepath;
    private Button bt_load;
    private Button bt_upload;

    private ProgressBar pb_upload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et_filepath = (EditText) findViewById(R.id.et_filepath);
        bt_load = (Button) findViewById(R.id.bt_load);
        bt_upload = (Button) findViewById(R.id.bt_upload);

        pb_upload = (ProgressBar) findViewById(R.id.pb_upload);

        pb_upload.setProgress(0);

        bt_load.setOnClickListener(this);
        bt_upload.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.bt_load:
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("video/*");
                startActivityForResult(photoPickerIntent, SELECT_VIDEO);
                break;
            case R.id.bt_upload:
                String path = et_filepath.getText().toString().trim();
                int result = upload(path);

                Log.i(TAG, "result: " + result);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK && requestCode == SELECT_VIDEO){
            Uri videoUri = data.getData();

            String path = getAbsoluteFilePath(videoUri);
            et_filepath.setText(path);
        }
    }

    protected String getAbsoluteFilePath(Uri uri) {
        // can post image
        String [] proj={MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery( uri,
                proj,                 // Which columns to return
                null,       // WHERE clause; which rows to return (all rows)
                null,       // WHERE clause selection arguments (none)
                null);                 // Order-by clause (ascending by name)

        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();

        return cursor.getString(column_index);
    }


    /**
     * 回调上传进度, c中调用
     * @param progress 0.0 ~ 1.0
     */
    public void setUploadProgress(float progress) {

        Log.i(TAG, "upload progress: " + progress);

        pb_upload.setProgress((int)(progress * 100));
    }

    /**
     * 执行上传
     * @param path 传入的文件路径
     * @return
     */
    public native int upload(String path);

}
