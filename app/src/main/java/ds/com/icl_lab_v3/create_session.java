package ds.com.icl_lab_v3;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class create_session extends ActionBarActivity {

    public static String lab_state;
    CheckBox clear_data;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_session);
        clear_data=(CheckBox) findViewById(R.id.new_session_checkbox);
        db=openOrCreateDatabase("RegistrationDB", Context.MODE_PRIVATE, null);
    }

    public void btnCreate (View v){
        Spinner spDay = (Spinner) findViewById(R.id.spDay);
        Spinner spTime = (Spinner) findViewById(R.id.spTime);

        String day = spDay.getSelectedItem().toString();
        String time = spTime.getSelectedItem().toString();
        final long day_id = spDay.getSelectedItemId();
        final long time_id = spTime.getSelectedItemId();

        if (day_id==0 || time_id==0){
            Toast.makeText(this, "Please select all fields", Toast.LENGTH_LONG).show();
        }
        else {

            final long temp_day_id = day_id;
            final long temp_time_id = time_id;

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Create new session?");
            builder.setMessage("Please check the following information:" + "\n" + "\n" + "Day: " + "\t" + day + "\n" + "Time: " + "\t" + time);

            builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    lab_state = Long.toString(temp_day_id) + Long.toString(temp_time_id);

                    Cursor c = db.rawQuery("SELECT * FROM setting WHERE setting_name='lab_state'", null);

                    if (c.getCount()==0){db.execSQL("INSERT INTO setting VALUES('lab_state', '" + lab_state + "');");}
                    else {db.execSQL("UPDATE setting SET value='" + lab_state + "' WHERE setting_name='lab_state' ");}

                    if (clear_data.isChecked()){
                        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Date date = new Date();
                        db.execSQL("DROP TABLE IF EXISTS id_machine;");
                        db.execSQL("CREATE TABLE IF NOT EXISTS id_machine(input_id VARCHAR(8),machine VARCHAR(8));");
                        db.execSQL("UPDATE student_log SET leave_datetime=" + "'" + dateFormat.format(date) + "'" + "WHERE leave_datetime=" + "' '");

                        db.execSQL("DROP TABLE IF EXISTS duty_list;");
                        db.execSQL("CREATE TABLE IF NOT EXISTS duty_list(input_id VARCHAR(8),sub_id VARCHAR(8));");
                        db.execSQL("UPDATE duty_log SET leave_datetime=" + "'" + dateFormat.format(date) + "'" + "WHERE leave_datetime=" + "' '");}
                    goto_welcome();
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
    }

    public void goto_welcome(){
        Intent intent = new Intent(this, welcome.class);
        startActivity(intent);
    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_create_session, menu);
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
