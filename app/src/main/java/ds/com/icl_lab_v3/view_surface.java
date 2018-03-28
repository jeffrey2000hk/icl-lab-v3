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
import android.widget.EditText;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class view_surface extends ActionBarActivity {


    SQLiteDatabase db;
    public final static String VIEW_KICK_RIGHT = "VIEW_KICK_RIGHT";
    int kick_right;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_surface);
        db=openOrCreateDatabase("RegistrationDB", Context.MODE_PRIVATE, null);

        Intent original_intent = getIntent();
        kick_right = Integer.parseInt(original_intent.getStringExtra(VIEW_KICK_RIGHT));

        List<String> remaining = new ArrayList<String>();
        TextView teacher_surface_tv = (TextView) findViewById(R.id.teacher_surface_tv);
        TextView student_surface_tv = (TextView) findViewById(R.id.student_surface_tv);
        TextView remain_surface_tv = (TextView) findViewById(R.id.remain_surface_tv);

        String teacher_surface = "";
        String student_surface = "";
        String remain_surface = "";

        for (int i=0;i<40;i++) {
            remaining.add("T002" + String.format("%02d", i));
        }

        for (int i=0;i<40;i++) {

            Cursor c = db.rawQuery("SELECT * FROM id_machine_teacher WHERE machine=" + "'" + "T002" + String.format("%02d", i) + "';", null);

            if (c.getCount() != 0) {
                teacher_surface = teacher_surface + "T002" + String.format("%02d", i) + ", ";
                remaining.removeAll(Collections.singleton("T002" + String.format("%02d", i)));
            }
        }

        for (int i=0;i<40;i++) {

            Cursor c = db.rawQuery("SELECT * FROM id_machine WHERE machine=" + "'" + "T002" + String.format("%02d", i) + "';", null);

            if (c.getCount() != 0) {
                student_surface = student_surface + "T002" + String.format("%02d", i) + ", ";
                remaining.removeAll(Collections.singleton("T002" + String.format("%02d", i)));
            }
        }

        for (int i=0;i<remaining.size();i++){
            remain_surface = remain_surface + remaining.get(i) + ", ";
        }

        if (teacher_surface.length()!=0) {teacher_surface = teacher_surface.substring(0, teacher_surface.length() - 2);}
        if (student_surface.length()!=0) {student_surface = student_surface.substring(0, student_surface.length() - 2);}
        if (remain_surface.length()!=0) {remain_surface = remain_surface.substring(0, remain_surface.length() - 2);}

        teacher_surface_tv.setText(teacher_surface);
        student_surface_tv.setText(student_surface);
        remain_surface_tv.setText(remain_surface);

//        TableRow search_row = (TableRow) findViewById(R.id.row_searchsurface);
//        if (false){
//
//        }
//        else {
//            search_row.setVisibility(View.GONE);
//        }

    }


    public void surface_search (View v){
        EditText machine_input_et=(EditText) findViewById(R.id.editText2);
        final String machine_selection=machine_input_et.getText().toString();

        if (machine_selection.length()==0){
            onebutton_dialog("Missing ID!", "Please enter the ID of your Surface!", "OK");
        }
        else if (Integer.parseInt(machine_selection) > 239 | Integer.parseInt(machine_selection) <200){
            onebutton_dialog("Invalid ID!", "Please enter a valid Surface ID!", "OK");
            machine_input_et.setText("");
        }
        else {

            String occupied_status = "";
            String user = "";
            Cursor c = db.rawQuery("SELECT * FROM id_machine WHERE machine='T00" + machine_selection + "'", null);
            if (c.getCount() != 0) {
                occupied_status = "Machine is occupied";
                c.moveToFirst();
                user = c.getString(0);
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Status of machine T00" + machine_selection);
                builder.setMessage("Occupied Status: " + "\t" + "\t" + occupied_status + "\n" +
                        "Occupied by user: " + "\t" + user);
                if (kick_right == 0) {
                    builder.setPositiveButton("Back", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    });
                } else {
                    builder.setPositiveButton("Return machine", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            Date date = new Date();
                            db.execSQL("DELETE FROM id_machine WHERE machine=" + "'" + "T00" + machine_selection + "'");
                            db.execSQL("UPDATE student_log SET leave_datetime=" + "'" + dateFormat.format(date) + "'" + "WHERE machine=" + "'" + "T00" + machine_selection + "'" + "AND leave_datetime=" + "' '");
                            show_kick_confirm();





                            List<String> remaining = new ArrayList<String>();
                            TextView teacher_surface_tv = (TextView) findViewById(R.id.teacher_surface_tv);
                            TextView student_surface_tv = (TextView) findViewById(R.id.student_surface_tv);
                            TextView remain_surface_tv = (TextView) findViewById(R.id.remain_surface_tv);

                            String teacher_surface = "";
                            String student_surface = "";
                            String remain_surface = "";

                            for (int i=0;i<40;i++) {
                                remaining.add("T002" + String.format("%02d", i));
                            }

                            for (int i=0;i<40;i++) {

                                Cursor c = db.rawQuery("SELECT * FROM id_machine_teacher WHERE machine=" + "'" + "T002" + String.format("%02d", i) + "';", null);

                                if (c.getCount() != 0) {
                                    teacher_surface = teacher_surface + "T002" + String.format("%02d", i) + ", ";
                                    remaining.removeAll(Collections.singleton("T002" + String.format("%02d", i)));
                                }
                            }

                            for (int i=0;i<40;i++) {

                                Cursor c = db.rawQuery("SELECT * FROM id_machine WHERE machine=" + "'" + "T002" + String.format("%02d", i) + "';", null);

                                if (c.getCount() != 0) {
                                    student_surface = student_surface + "T002" + String.format("%02d", i) + ", ";
                                    remaining.removeAll(Collections.singleton("T002" + String.format("%02d", i)));
                                }
                            }

                            for (int i=0;i<remaining.size();i++){
                                remain_surface = remain_surface + remaining.get(i) + ", ";
                            }

                            if (teacher_surface.length()!=0) {teacher_surface = teacher_surface.substring(0, teacher_surface.length() - 2);}
                            if (student_surface.length()!=0) {student_surface = student_surface.substring(0, student_surface.length() - 2);}
                            if (remain_surface.length()!=0) {remain_surface = remain_surface.substring(0, remain_surface.length() - 2);}

                            teacher_surface_tv.setText(teacher_surface);
                            student_surface_tv.setText(student_surface);
                            remain_surface_tv.setText(remain_surface);

//                            TableRow search_row = (TableRow) findViewById(R.id.row_searchsurface);
//                            if (false){
//
//                            }
//                            else {
//                                search_row.setVisibility(View.GONE);
//                            }
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
            } else {
                occupied_status = "Machine is unoccupied";
                user = "N/A";
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Status of machine T00" + machine_selection);
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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view_surface, menu);
        return true;
    }

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

    public void onebutton_dialog(String title, String message, String ok){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

            }
        });
        AlertDialog dialog = builder.create();
        dialog.setIcon(R.drawable.ic_dialog_alert);
        dialog.show();
    }

    public void show_kick_confirm (){
        Toast.makeText(this, "User removed from computer lab", Toast.LENGTH_LONG).show();
    }
}
