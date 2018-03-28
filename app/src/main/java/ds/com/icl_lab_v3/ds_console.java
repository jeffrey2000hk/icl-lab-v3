package ds.com.icl_lab_v3;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;


public class ds_console extends ActionBarActivity {

    SQLiteDatabase db;
    public final static String VIEW_KICK_RIGHT = "VIEW_KICK_RIGHT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db=openOrCreateDatabase("RegistrationDB", Context.MODE_PRIVATE, null);
        setContentView(R.layout.admin);
        refresh_view();
    }

    public void btn_start (View v){
        Intent intent = new Intent(this, create_session.class);
        startActivity(intent);
    }

    public void btn_export_log (View v) throws IOException {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Export machine logs?");
        builder.setMessage("Old logs will be overwritten!");

        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                export_log();
                show_message("Logs exported to /machine_log.csv");
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        AlertDialog dialog = builder.create();
        dialog.setIcon(R.drawable.ic_dialog_alert);
        dialog.show();
    }

    public void export_log(){
        db.execSQL("CREATE TABLE IF NOT EXISTS id_machine(input_id VARCHAR(8),machine VARCHAR(8));");
        db.execSQL("CREATE TABLE IF NOT EXISTS id_machine_teacher(input_id VARCHAR(8),machine VARCHAR(8));");
        db.execSQL("CREATE TABLE IF NOT EXISTS student_log(input_id VARCHAR(8),machine VARCHAR(8),arrive_datetime DATETIME, leave_datetime DATETIME);");
        Cursor c=db.rawQuery("SELECT * FROM student_log", null);
        CSVWriter writer;

            try {


                writer = new CSVWriter(new PrintWriter(new BufferedWriter(new FileWriter(Environment.getExternalStorageDirectory() + "/machine_log.csv"))));
                while(c.moveToNext()) {
                    String[] temp = {c.getString(0), c.getString(1), c.getString(2), c.getString(3)};
                    writer.writeNext(temp);
                }
                writer.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    public void btn_export_log_ds (View v) throws IOException {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Export DS logs?");
        builder.setMessage("Old logs will be overwritten!");

        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                export_log_ds();
                show_message("Logs exported to /ds_log.csv");
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        AlertDialog dialog = builder.create();
        dialog.setIcon(R.drawable.ic_dialog_alert);
        dialog.show();
    }

    public void export_log_ds(){
        db.execSQL("CREATE TABLE IF NOT EXISTS id_machine(input_id VARCHAR(8),machine VARCHAR(8));");
        db.execSQL("CREATE TABLE IF NOT EXISTS id_machine_teacher(input_id VARCHAR(8),machine VARCHAR(8));");
        db.execSQL("CREATE TABLE IF NOT EXISTS student_log(input_id VARCHAR(8),machine VARCHAR(8),arrive_datetime DATETIME, leave_datetime DATETIME);");
        db.execSQL("CREATE TABLE IF NOT EXISTS duty_list(input_id VARCHAR(8),sub_id VARCHAR(8));");
        db.execSQL("CREATE TABLE IF NOT EXISTS duty_log(input_id VARCHAR(8),sub_id VARCHAR(8),arrive_datetime DATETIME, leave_datetime DATETIME);");
        Cursor c=db.rawQuery("SELECT * FROM duty_log", null);
        CSVWriter writer;

        try {


            writer = new CSVWriter(new FileWriter(Environment.getExternalStorageDirectory() + "/ds_log.csv"));
            while(c.moveToNext()) {
                String[] temp = {c.getString(0), c.getString(1), c.getString(2), c.getString(3)};
                writer.writeNext(temp);
            }
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void admin_view_desktop(View v){
        db.execSQL("CREATE TABLE IF NOT EXISTS id_machine(input_id VARCHAR(8),machine VARCHAR(8));");
        db.execSQL("CREATE TABLE IF NOT EXISTS student_log(input_id VARCHAR(8),machine VARCHAR(8),arrive_datetime DATETIME, leave_datetime DATETIME);");
        Intent intent = new Intent(this, view_seating.class);
        intent.putExtra(VIEW_KICK_RIGHT, "1");
        startActivity(intent);
    }

    public void btn_end (View v){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Close the computer lab?");
        builder.setMessage("You cannot undo this action!");

        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date();
                db.execSQL("DROP TABLE IF EXISTS id_machine;");
                db.execSQL("CREATE TABLE IF NOT EXISTS id_machine(input_id VARCHAR(8),machine VARCHAR(8));");
                db.execSQL("UPDATE student_log SET leave_datetime=" + "'" + dateFormat.format(date) + "'" + "WHERE leave_datetime=" + "' '");

                db.execSQL("DROP TABLE IF EXISTS duty_list;");
                db.execSQL("CREATE TABLE IF NOT EXISTS duty_list(input_id VARCHAR(8),sub_id VARCHAR(8));");
                db.execSQL("UPDATE duty_log SET leave_datetime=" + "'" + dateFormat.format(date) + "'" + "WHERE leave_datetime=" + "' '");
                Cursor c = db.rawQuery("SELECT * FROM setting WHERE setting_name='lab_state'", null);
                if (c.getCount()==0){db.execSQL("INSERT INTO setting VALUES('lab_state', '');");}
                else {db.execSQL("UPDATE setting SET value='' WHERE setting_name='lab_state' ");}
                create_session.lab_state="";
                refresh_view();
                show_message("Computer lab successfully cleared");
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        AlertDialog dialog = builder.create();
        dialog.setIcon(R.drawable.ic_dialog_alert);
        dialog.show();
    }


    public void show_message (String message){
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    public void admin_view_surface (View v){
        Intent intent = new Intent(this, view_surface.class);
        intent.putExtra(VIEW_KICK_RIGHT, "1");
        startActivity(intent);
    }

    public void btn_config(View v){
        Intent intent = new Intent(this, ds_console_password_heads.class);
        startActivity(intent);
    }
//int k=0;
    public void btn_view_duty(View v){
//        if (k==0) {
            Cursor c = db.rawQuery("SELECT * FROM ds_info;", null);

            LinearLayout dynamic_container = (LinearLayout) findViewById(R.id.ll_show_ds);
        dynamic_container.removeAllViews();
            for (c.moveToLast(); !c.isBeforeFirst(); c.moveToPrevious()) {

                LinearLayout tr1 = new LinearLayout(this);
                dynamic_container.addView(tr1, 0);
//            dynamic_container.addView(tr1);
                TextView tv = new TextView(this);
                tv.setText(c.getString(0));
                tv.setTextSize(12);
                tv.setVisibility(View.VISIBLE);
                tv.setPadding(0, 0, 30, 0);
                tv.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.FILL_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT, 310f));
                tr1.addView(tv);

                TextView tv1 = new TextView(this);
                tv1.setText(c.getString(1));
                tv1.setTextSize(12);
                tv1.setVisibility(View.VISIBLE);
                tv1.setPadding(0, 0, 30, 0);

                tv1.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.FILL_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT, 200f));
                tr1.addView(tv1);

                TextView tv2 = new TextView(this);
                tv2.setText(c.getString(2));
                tv2.setTextSize(12);
                tv2.setVisibility(View.VISIBLE);
                tv2.setPadding(0, 0, 30, 0);
                tv2.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.FILL_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT, 350f));
                tr1.addView(tv2);

            }

            show_message("Duty configuration displayed");
//            k=1;

//        }
        }


    public void refresh_view () {
        TextView tv_state = (TextView) findViewById(R.id.state);

        if (create_session.lab_state != null) {
            if (create_session.lab_state.length() == 0) {
                tv_state.setText("Closed");
                tv_state.setTextColor(Color.parseColor("#FF0000"));
            } else {
                tv_state.setText("Open");
                tv_state.setTextColor(Color.parseColor("#00FF00"));
            }

            TextView occupied_desktop = (TextView) findViewById(R.id.occupied_desktop);
            TextView remain_desktop = (TextView) findViewById(R.id.remain_desktop);
            TextView occupied_surface = (TextView) findViewById(R.id.occupied_surface);
            TextView remain_surface = (TextView) findViewById(R.id.remain_surface);

            Cursor c = db.rawQuery("SELECT COUNT(*) FROM id_machine WHERE machine LIKE 'SCL_%';", null);
            if (c.getCount() != 0) {
                c.moveToFirst();
                occupied_desktop.setText(c.getString(0));
                remain_desktop.setText(Integer.toString(12 - Integer.parseInt(c.getString(0))));
            } else {
                remain_desktop.setText("12");
            }
            Cursor c2 = db.rawQuery("SELECT COUNT(*) FROM id_machine WHERE machine LIKE 'T002%';", null);
            c2.moveToFirst();
            Cursor c3 = db.rawQuery("SELECT COUNT(*) FROM id_machine_teacher WHERE machine LIKE 'T002%';", null);
            c3.moveToFirst();
            occupied_surface.setText(Integer.toString(Integer.parseInt(c2.getString(0)) + Integer.parseInt(c3.getString(0))));
            remain_surface.setText(Integer.toString(40 - Integer.parseInt(c2.getString(0)) - Integer.parseInt(c3.getString(0))));
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainMenu.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_ds_console, menu);
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
}
