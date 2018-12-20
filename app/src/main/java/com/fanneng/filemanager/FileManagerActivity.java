package com.fanneng.filemanager;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.fanneng.filemanager.utils.FileUtils;
import com.fanneng.library.FileInfoReport;
import com.fanneng.library.save.imp.FileInfoWriter;
import com.fanneng.library.util.FileUtil;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class FileManagerActivity extends AppCompatActivity {


    public static final String TAG = "FileManagerActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_manager);
        FileInfoReport.getInstance().upload(this);
        setUpListener();
    }

    private void setUpListener() {
        Button button1 = (Button) findViewById(R.id.button1);
        Button button2 = (Button) findViewById(R.id.button2);
        Button button3 = (Button) findViewById(R.id.button3);
        Button button4 = (Button) findViewById(R.id.button4);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //FileInfoReport.getInstance().upload(getApplicationContext());
                Intent intent = new Intent(FileManagerActivity.this,MainSqliteActivity.class);

                startActivity(intent);

            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileInfoWriter.writeFileInfo(TAG, "文件文本信息！！！！");
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileUtil.deleteDir(new File(FileInfoReport.getInstance().getROOT()));
            }
        });
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });
    }

    private final static int FILE_SELECT_CODE =0x00001;
    private String  filename ="";
    private String  filepath ="";
    /** 调用文件选择软件来选择文件 **/
    private void showFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        try {
            startActivityForResult(Intent.createChooser(intent, "请选择一个要上传的文件"),
                    FILE_SELECT_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            // Potentially direct the user to the Market with a Dialog
            Toast.makeText(this, "请安装文件管理器", Toast.LENGTH_SHORT)
                    .show();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case FILE_SELECT_CODE:
                if (resultCode == RESULT_OK) {
                    // Get the Uri of the selected file
                    Uri uri = data.getData();
                    Log.d(TAG, "File Uri: " + uri.toString());

                    // Get the path
                    String path = null;
                    try {
                        path = FileUtils.getPath(this, uri);

                        filename = path.substring(path.lastIndexOf("/") + 1);
                        Log.i(TAG, "onActivityResult: path=="+path);
                        //textView1.setText(path);
                    } catch (URISyntaxException e) {
                        Log.e("TAG", e.toString());
                        //e.printStackTrace();
                        path = "";
                    }
                    filepath = path;
                    Log.d(TAG, "上传的文件路径File Path: " + path);
                    // Get the file instance
                    // File file = new File(path);
                    // Initiate the upload


                    uploadFiles();
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    private void uploadFiles(){
        Log.i(TAG, "onActivityResult: 开始调用上传文件方法...");
        //上传文件：
        File file = new File(filepath);
        RequestBody fileBody = RequestBody.create(MediaType.parse("application/octet-stream"), file);
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addPart(Headers.of(
                        "Content-Disposition",
                        "form-data; name=\"username\""),
                        RequestBody.create(null, "HGR"))
                .addPart(Headers.of(
                        "Content-Disposition",
                        "form-data; name=\"mFile\"; filename=\"" + filename + "\""), fileBody)
                .build();


        Request request = new Request.Builder()
                .url("http://10.4.94.166:8080/mTestServer/servlet/UploadHandleServlet")
                .post(requestBody)
                .build();

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
//                .addInterceptor(new LoggerInterceptor("TAG"))
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                .readTimeout(10000L, TimeUnit.MILLISECONDS)
                //其他配置
                .build();

        Log.i(TAG, "onActivityResult: 调用上传方法 call.equals");
        //new call
        Call call = okHttpClient.newCall(request);
        //请求加入调度
        call.equals(new Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                Log.e(TAG, "failure upload!");
            }

            @Override
            public void onResponse(okhttp3.Call call, Response response) throws IOException {
                Log.i(TAG, "success upload!");
            }
        });
    }




}

