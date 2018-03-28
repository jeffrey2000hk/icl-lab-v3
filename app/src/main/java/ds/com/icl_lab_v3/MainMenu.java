package ds.com.icl_lab_v3;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * ICL Lab V3
 * The strongest computer lab registration system
 *
 * @author Jeffrey Lee
 *
 */


public class MainMenu extends ActionBarActivity {

    SQLiteDatabase db;

    public final static String STUDENT_TEACHER_POS = "STUDENT_TEACHER_POS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();

        db=openOrCreateDatabase("RegistrationDB", Context.MODE_PRIVATE, null);
//        db.execSQL("DROP TABLE id_machine;");
//        db.execSQL("DROP TABLE duty_list;");
//        db.execSQL("DROP TABLE id_machine_teacher");
        db.execSQL("CREATE TABLE IF NOT EXISTS id_machine(input_id VARCHAR(8),machine VARCHAR(8));");
        db.execSQL("CREATE TABLE IF NOT EXISTS id_machine_teacher(input_id VARCHAR(8),machine VARCHAR(8));");
        db.execSQL("CREATE TABLE IF NOT EXISTS duty_list(input_id VARCHAR(8),sub_id VARCHAR(8));");
        db.execSQL("CREATE TABLE IF NOT EXISTS student_log(input_id VARCHAR(8),machine VARCHAR(8),arrive_datetime DATETIME, leave_datetime DATETIME);");
//        db.execSQL("UPDATE student_log SET leave_datetime=" + "'" + dateFormat.format(date) + "'" + "WHERE leave_datetime=' '");

        db.execSQL("CREATE TABLE IF NOT EXISTS duty_log(input_id VARCHAR(8),sub_id VARCHAR(8),arrive_datetime DATETIME, leave_datetime DATETIME);");

//        db.execSQL("DROP TABLE setting;");
        db.execSQL("CREATE TABLE IF NOT EXISTS setting(setting_name VARCHAR(20),value VARCHAR(40));");


        db.execSQL("CREATE TABLE IF NOT EXISTS ds_info(input_id VARCHAR(8),ds_name VARCHAR(30),duty_time VARCHAR(2));");



        Cursor c = db.rawQuery("SELECT * FROM setting WHERE setting_name='lab_state'", null);
        if (c.getCount()!=0){c.moveToFirst();   create_session.lab_state=c.getString(1);}

        Cursor d = db.rawQuery("SELECT * FROM setting WHERE setting_name='ds_pw'", null);
        if (d.getCount()==0){db.execSQL("INSERT INTO setting VALUES('ds_pw','');");}

        Cursor e = db.rawQuery("SELECT * FROM setting WHERE setting_name='admin_pw'", null);
        if (e.getCount()==0){db.execSQL("INSERT INTO setting VALUES('admin_pw','');");}
    }

    @Override
    public void onBackPressed() {
        ActivityManager mngr = (ActivityManager) getSystemService( ACTIVITY_SERVICE );

        List<ActivityManager.RunningTaskInfo> taskList = mngr.getRunningTasks(10);

        if(taskList.get(0).numActivities == 1 &&
                taskList.get(0).topActivity.getClassName().equals(this.getClass().getName())) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Are you sure you want to quit?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            finish();
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    })
                    .setCancelable(true);
            AlertDialog alert = builder.create();
            alert.show();
        }
        }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main_menu, menu);
//        return true;
//    }

    @Override
         public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void btn_main_student (View V){

        if (create_session.lab_state==null||create_session.lab_state.length()==0){
            Toast.makeText(this, "Lab is closed", Toast.LENGTH_LONG).show();
        }
        else {
        Intent intent = new Intent(this, welcome.class);
        intent.putExtra(STUDENT_TEACHER_POS, "0");
        startActivity(intent);}
    }

    public void btn_main_ds_console (View V){
        Intent intent = new Intent(this, ds_console_password.class);
        startActivity(intent);
    }

    public void btn_main_ds_sub (View V){
        if (create_session.lab_state==null||create_session.lab_state.length()==0){
            Toast.makeText(this, "Lab is closed", Toast.LENGTH_LONG).show();
        }
        else {
        Intent intent = new Intent(this, ds_duty_tabs.class);
        startActivity(intent);}
    }

    public void btn_main_teacher_surface (View V){
        Intent intent = new Intent(this, id_input.class);
        intent.putExtra(STUDENT_TEACHER_POS, "1");
        startActivity(intent);
    }
    }

