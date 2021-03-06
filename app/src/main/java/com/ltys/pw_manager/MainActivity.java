package com.ltys.pw_manager;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
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
    public static String sDir = null;
//    private String temp_sDir = null;
//    private String note_sDir = null;
    public static do_with_xml dwx = null;

    public static int lock_val = 1;

    private String SDCARD_DIR = "/mnt/sdcard/.pw_manager/";
//    private String SDCARD_DIR_TEMP = "/mnt/sdcard/.pw_manager/.temp/";
//    private String SDCARD_DIR_NOTE = "/mnt/sdcard/.pw_manager/.note/";
    private String NOSDCARD_DIR = "/data/data/.com.example.lenovo.pw_manager/";
//    private String NOSDCARD_DIR_TEMP = "/data/data/.com.ltys.pw_manager/.temp/";
//    private String NOSDCARD_DIR_NOTE = "/data/data/.com.ltys.pw_manager/.note/";

    private final String md_text = "liutengying";
    private Button sign_in_button = null;
    private File file = null;
    private File pass_file = null;
    private FileOutputStream fos = null;
    private OutputStreamWriter osw = null;
    private SimpleAdapter adapter = null;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        MainActivityPermissionsDispatcher.get_permissions_funcWithCheck(MainActivity.this);

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @NeedsPermission({Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.INTERNET})
    void get_permissions_func(){
        String status = Environment.getExternalStorageState();
        if (status.equals(Environment.MEDIA_MOUNTED)) {
            sDir = SDCARD_DIR;
//            temp_sDir = SDCARD_DIR_TEMP;
//            note_sDir = SDCARD_DIR_NOTE;
        } else {
            sDir = NOSDCARD_DIR;
//            temp_sDir = NOSDCARD_DIR_TEMP;
//            note_sDir = NOSDCARD_DIR_NOTE;
        }
//        sDir = new String("./");
        file = new File(sDir);
        if(!file.exists()){
            file.mkdirs();
        }
        pass_file = new File(sDir+".src/ltys.txt");
        if(!pass_file.exists()){
            file = new File(sDir+"pw_manager.xml");
            if(file.exists()){
                Toast.makeText(getApplicationContext(),"密码文件异常丢失,恢复初始密码,并删除数据",Toast.LENGTH_SHORT);
                file.delete();
            }
        }
        dwx = new do_with_xml(sDir+"pw_manager.xml");
        if(lock_val == 1){
            show_lock(0);
        }
        else{
            show_main();
        }
    }



    private void show_lock(int mode){
        Intent it = new Intent();
        it.setClassName(MainActivity.this,"com.ltys.pw_manager.signin_activity");
        it.putExtra("mode",mode);
        it.putExtra("path",sDir);
        startActivity(it);
        if(mode==0)
            finish();
    }

    public void change(){
        ListView liview = (ListView) findViewById(R.id.listview);
        adapter = new SimpleAdapter(getApplicationContext(),getData(),R.layout.item, new String[]{"name","icon"}, new int[]{R.id.name,R.id.icon});
        liview.setAdapter(adapter);
        liview.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> arg0,View arg1,int arg2,long arg3) {
                // TODO Auto-generated method stub
                String account,passwd,note,img;
                ListView liview = (ListView) arg0;
                HashMap<String, Object> map = (HashMap<String,Object>) liview.getItemAtPosition(arg2);
                String name = (String)map.get("name");
                Log.i("click",name);
                account = dwx.get_por_by_name(name,"account");
                passwd = dwx.get_por_by_name(name,"passwd");
                note = dwx.get_por_by_name(name,"note");
                img = dwx.get_por_by_name(name,"img");
                Intent it = new Intent();
                it.setClassName(MainActivity.this,"com.ltys.pw_manager.show_info");
                it.putExtra("name",name);
                it.putExtra("account",account==null?"":account);
                it.putExtra("passwd",passwd==null?"":passwd);
                it.putExtra("note",note==null?"":note);
                it.putExtra("img",img==null?"":img);
                startActivity(it);
            }
        });
    }

    private void show_main(){
        setContentView(R.layout.activity_main);
        change();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add:
                Intent it = new Intent();
                it.setClassName(getApplicationContext(),"com.ltys.pw_manager.add_item");
                startActivity(it);
                break;
            case R.id.exit:
                exit(0);
                break;
            case R.id.set_passwd:
                show_lock(1);
                break;
            default:
                break;
        }
        return false;
    }


    private static List<Map<String, Object>> getData() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        ArrayList<String> names = dwx.all_name();
        String img = null;
        for(String name:names){
            img = dwx.get_por_by_name(name,"img");
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("name", name);
            map.put("icon", R.mipmap.ic_launcher);
            list.add(map);
        }
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
        Log.i("pass",ToolFunction.encrypt("华中科技大学hub"));
        Log.i("pass",ToolFunction.decrypt(ToolFunction.encrypt("华中科技大学hub")));
        if(lock_val==0)
            change();
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
