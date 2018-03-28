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
import android.widget.Button;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class view_seating extends ActionBarActivity {

    int machine_selection;
    SQLiteDatabase db;
    int kick_right;
    int [] array_of_id;
    public final static String VIEW_KICK_RIGHT = "VIEW_KICK_RIGHT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seating);
        Intent original_intent = getIntent();
        kick_right = Integer.parseInt(original_intent.getStringExtra(VIEW_KICK_RIGHT));
        db=openOrCreateDatabase("RegistrationDB", Context.MODE_PRIVATE, null);
        array_of_id = new int [] {R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4, R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9, R.id.btn10, R.id.btn11, R.id.btn12};
        for (int i=0;i<12;i++){
            Cursor c = db.rawQuery("SELECT * FROM id_machine WHERE machine='SCL_" + String.format("%02d", i+1) +            "'", null);
            if (c.getCount()!=0){
                Button temp = (Button) findViewById(array_of_id[i]);
                temp.setBackgroundResource(R.drawable.occupied_w);
            }
            else {Button temp = (Button) findViewById(array_of_id[i]);
                temp.setBackgroundResource(R.drawable.empty_w);}
        }
    }

    public void seat (View v){
        String btn_id_string = v.getResources().getResourceName(v.getId());
        machine_selection=Integer.parseInt(btn_id_string.replace(getApplicationContext().getPackageName() + ":id/btn", ""));
        view_status();
    }

    public void view_status (){
        String occupied_status="";
        String user="";
        Cursor c = db.rawQuery("SELECT * FROM id_machine WHERE machine='SCL_" + String.format("%02d", machine_selection) +            "'", null);
        if (c.getCount()!=0){
            occupied_status="Machine is occupied";
            c.moveToFirst();
            user=c.getString(0);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Status of machine SCL_" + String.format("%02d", machine_selection));
            builder.setMessage("Occupied Status: " + "\t" + "\t" + occupied_status + "\n" +
                    "Occupied by user: " + "\t" + user);
            if (kick_right==0){
                builder.setPositiveButton("Back", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
            }
            else {
                builder.setPositiveButton("Kick", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Date date = new Date();
                        db.execSQL("DELETE FROM id_machine WHERE machine=" + "'" + "SCL_" + String.format("%02d", machine_selection) + "'");
                        db.execSQL("UPDATE student_log SET leave_datetime=" + "'" + dateFormat.format(date) + "'" + "WHERE machine=" + "'" + "SCL_" + String.format("%02d", machine_selection) + "'" + "AND leave_datetime=" + "' '");
                        show_kick_confirm();
                        for (int i=0;i<12;i++){
                            Cursor c = db.rawQuery("SELECT * FROM id_machine WHERE machine='SCL_" + String.format("%02d", i+1) +            "'", null);
                            if (c.getCount()!=0){
                                Button temp = (Button) findViewById(array_of_id[i]);
                                temp.setBackgroundResource(R.drawable.occupied_w);
                            }
                            else {Button temp = (Button) findViewById(array_of_id[i]);
                                temp.setBackgroundResource(R.drawable.empty_w);}
                        }
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
            }
            AlertDialog dialog = builder.create();
            dialog.setIcon(R.drawable.ic_dialog_alert);
            dialog.show();
        }
        else {
            occupied_status="Machine is unoccupied";
            user="N/A";
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Status of machine SCL_" + String.format("%02d", machine_selection));
            builder.setMessage("Occupied Status: " + "\t" + "\t" + occupied_status + "\n" +
                    "Occupied by user: " + "\t" + user);
                builder.setPositiveButton("Back", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    public void show_kick_confirm (){
        Toast.makeText(this, "User removed from computer lab", Toast.LENGTH_LONG).show();
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_view_seating, menu);
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
