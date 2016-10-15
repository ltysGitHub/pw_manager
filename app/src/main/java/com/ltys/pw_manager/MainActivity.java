package com.ltys.pw_manager;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.RuntimePermissions;

import static java.lang.System.exit;

@RuntimePermissions
public class MainActivity extends Activity {
    private String sDir = null;
    private String temp_sDir = null;
    private String note_sDir = null;
    private do_with_xml dwx_pass = null;
    private do_with_xml dwx_content = null;
    public static int curse = 0;
    public static int width = 5;
    public static int lock_val = 1;
    //    private String SDCARD_DIR = "/mnt/sdcard/.wifi_trans/.pic/";
//    private String SDCARD_DIR_TEMP = "/mnt/sdcard/.wifi_trans/.temp/";
//    private String SDCARD_DIR_NOTE = "/mnt/sdcard/.wifi_trans/.note/";
//    private String NOSDCARD_DIR = "/data/data/.com.example.lenovo.wifi_trans/.pic/";
//    private String NOSDCARD_DIR_TEMP = "/data/data/.com.example.lenovo.wifi_trans/.temp/";
//    private String NOSDCARD_DIR_NOTE = "/data/data/.com.example.lenovo.wifi_trans/.note/";
    private final String md_text = "liutengying";
    private Button sign_in_button = null;
    private File file = null;
    private FileOutputStream fos = null;
    private OutputStreamWriter osw = null;
    public static int[] container = new int[width*width];
    public static int[] input_container = new int[width*width];
    public static int[] sec_input_container = new int[width*width];
    public static int[] negativeGestures = new int[width*width];
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        for(int i = 0; i < negativeGestures.length; i++) {
            negativeGestures[i] = -1;
        }
        input_container = negativeGestures.clone();
        sec_input_container = negativeGestures.clone();
//        String status = Environment.getExternalStorageState();
//        if (status.equals(Environment.MEDIA_MOUNTED)) {
//            sDir = SDCARD_DIR;
//            temp_sDir = SDCARD_DIR_TEMP;
//            note_sDir = SDCARD_DIR_NOTE;
//        } else {
//            sDir = NOSDCARD_DIR;
//            temp_sDir = NOSDCARD_DIR_TEMP;
//            note_sDir = NOSDCARD_DIR_NOTE;
//        }

//        sDir = new String("./");
//        file = new File(sDir + "pass.xml");
//        if (!file.exists()) {
//            try {
//                file.createNewFile();
//                fos = new FileOutputStream(file);
//                osw = new OutputStreamWriter(fos, "UTF-8");
//                osw.write("<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"no\"?><root></root>");
//            } catch (IOException e) {
//                e.printStackTrace();
//                exit(0);
//            }
//        }
        MainActivityPermissionsDispatcher.get_permissions_funcWithCheck(MainActivity.this);

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @NeedsPermission({Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.INTERNET})
    void get_permissions_func(){
        if(lock_val == 1){
            show_lock();
        }
        else{
            show_main();
        }
    }

    private void show_lock(){
        Intent it = new Intent();
        it.setClassName(MainActivity.this,"com.ltys.pw_manager.signin_activity");
        startActivity(it);
        finish();
    }

    private void show_main(){
        setContentView(R.layout.activity_main);
        ListView liview = (ListView) findViewById(R.id.listview);
        SimpleAdapter adapter = new SimpleAdapter(this,getData(),R.layout.item,
                new String[]{"name","icon"},
                new int[]{R.id.name,R.id.icon});
        liview.setAdapter(adapter);
    }

    private List<Map<String, Object>> getData() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("name", "G1");
        map.put("icon", R.mipmap.ic_launcher);
        list.add(map);

        map = new HashMap<String, Object>();
        map.put("name", "G2");
        map.put("icon", R.mipmap.ic_launcher);
        list.add(map);

        map = new HashMap<String, Object>();
        map.put("name", "G3");
        map.put("icon", R.mipmap.ic_launcher);
        list.add(map);

        return list;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        MainActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @OnPermissionDenied({Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.INTERNET})
    void no_permission_func() {
        Toast.makeText(this, "请设置读写内存卡和网络权限", Toast.LENGTH_LONG).show();
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Main Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}
