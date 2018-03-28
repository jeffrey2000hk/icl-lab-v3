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
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;


public class id_input extends ActionBarActivity {

    SQLiteDatabase db;

    public final static String STUDENT_TEACHER_POS = "STUDENT_TEACHER_POS";
    public final static String INPUT_ID_INTENTEXTRA = "INPUT_ID";
    public final static String VIEW_KICK_RIGHT = "VIEW_KICK_RIGHT";

    String input_id;
    EditText edittext_input_id;
    TextView tv_pos;
    Button btn_toggle_pos;
    int student_teacher_value;
    ArrayList<String> list_of_ds = new ArrayList<String>();

    ArrayList<String> list_of_ds_name = new ArrayList<String>();

    ArrayList<String> list_of_duty = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        list_of_ds.clear();
        list_of_ds_name.clear();
        list_of_duty.clear();
        Intent original_intent = getIntent();
        student_teacher_value=Integer.parseInt(original_intent.getStringExtra(MainMenu.STUDENT_TEACHER_POS));

        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_input_id);

        db=openOrCreateDatabase("RegistrationDB", Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS id_machine(input_id VARCHAR(8),machine VARCHAR(8));");
        db.execSQL("CREATE TABLE IF NOT EXISTS student_log(input_id VARCHAR(8),machine VARCHAR(8),arrive_datetime DATETIME, leave_datetime DATETIME);");

        Cursor c=db.rawQuery("SELECT * FROM ds_info", null);
        while (c.moveToNext()){
            list_of_ds.add(c.getString(0));
            list_of_ds_name.add(c.getString(1));
            list_of_duty.add(c.getString(2));
        }

        btn_toggle_pos=(Button) findViewById(R.id.btn_toggle_pos);
        tv_pos = (TextView) findViewById(R.id.tv_student_teacher_pos);
        edittext_input_id = (EditText) findViewById(R.id.edittext_input_id);
        if (student_teacher_value==0){tv_pos.setText("Student");    btn_toggle_pos.setText("Change to Teacher!");}
        else {tv_pos.setText("Teacher");    btn_toggle_pos.setText("Change to Student!");}
        refresh();
    }

    public void refresh(){
        String dsnameview="";
        for (int i=0; i<list_of_ds.size(); i++){
            Cursor c4 = db.rawQuery("SELECT * FROM duty_list WHERE input_id=" + "'" + list_of_ds.get(i) + "'", null);

            if (c4.getCount()==0){
            }
            else {
                dsnameview+=list_of_ds_name.get(i);
                c4.moveToFirst();
                if (!c4.getString(1).equals("0")){
                    dsnameview+=" subs \n\t"+list_of_ds_name.get(list_of_ds.indexOf(c4.getString(1)));
                }
                dsnameview+="\n";
            }
        }
        TextView nameview = (TextView) findViewById(R.id.textView4);
        if (dsnameview.length()!=0){
            nameview.setText(dsnameview);
        }
        else {
            nameview.setText("None!");
        }





        TextView remain_desktop_tv = (TextView) findViewById(R.id.desktop_no);
        TextView remain_surface_tv = (TextView) findViewById(R.id.surface_no);

        Cursor c = db.rawQuery("SELECT COUNT(*) FROM id_machine WHERE machine LIKE 'SCL_%';", null);
        c.moveToFirst();
        remain_desktop_tv.setText(Integer.toString(12-Integer.parseInt(c.getString(0))));

        Cursor c2 = db.rawQuery("SELECT COUNT(*) FROM id_machine WHERE machine LIKE 'T002%';", null);
        c2.moveToFirst();
        Cursor c3 = db.rawQuery("SELECT COUNT(*) FROM id_machine_teacher WHERE machine LIKE 'T002%';", null);
        c3.moveToFirst();
        remain_surface_tv.setText(Integer.toString(40-Integer.parseInt(c2.getString(0))-Integer.parseInt(c3.getString(0))));
    }


    public void scan_barcode (View v) {
        Intent intent = new Intent("com.google.zxing.client.android.SCAN");
        intent.setPackage("com.google.zxing.client.android");
        intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
        startActivityForResult(intent, 0);

        InputMethodManager imm = (InputMethodManager)getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(edittext_input_id.getWindowToken(), 0);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {

                edittext_input_id.setText(intent.getStringExtra("SCAN_RESULT"));
                // Handle successful scan
            } else if (resultCode == RESULT_CANCELED) {
                // Handle cancel
            }
        }
    }

    public void toggle_pos (View v){
        if (student_teacher_value==1){student_teacher_value=0;  tv_pos.setText("Student");  btn_toggle_pos.setText("Change to Teacher!");}
        else {student_teacher_value=1;  tv_pos.setText("Teacher");  btn_toggle_pos.setText("Change to Student!");}
    }

    public void click_input_id (View v){
        input_id=edittext_input_id.getText().toString();

        if (input_id.length()==0){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Missing ID!");
            builder.setMessage("Please enter your ID!");

            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

                }
            });
            AlertDialog dialog = builder.create();
            dialog.setIcon(R.drawable.ic_dialog_alert);
            dialog.show();
        }
        else {

            if (student_teacher_value==0){


                if (input_id.length()!=8){
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Invalid ID!");
                    builder.setMessage("Please a valid student ID!");

                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.setIcon(R.drawable.ic_dialog_alert);
                    dialog.show();
                }
                else if (create_session.lab_state==null||create_session.lab_state.length()==0){
                    Toast.makeText(this, "Lab is closed", Toast.LENGTH_LONG).show();
                }
                /*else if (list_of_ds.contains(input_id)){
                    if (list_of_duty.get(list_of_ds.indexOf(input_id)).equals(create_session.lab_state)){
                        if (test_induty()){
                            AlertDialog.Builder builder = new AlertDialog.Builder(this);
                            builder.setMessage("You have successfully left duty!");
                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {}
                            });
                            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            Date date = new Date();
                            edittext_input_id.setText("");
                            db.execSQL("DELETE FROM duty_list WHERE input_id=" + "'" + input_id + "'");
                            db.execSQL("UPDATE duty_log SET leave_datetime=" + "'" + dateFormat.format(date) + "'" + "WHERE input_id=" + "'" + input_id + "'" + "AND leave_datetime=" + "' '");

                            AlertDialog dialog = builder.create();
                            dialog.show();
                            refresh();
                        }
                        else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(this);
                            builder.setMessage("You have successfully taken duty!");
                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {    }
                            });
                            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            Date date = new Date();
                            edittext_input_id.setText("");
                            db.execSQL("INSERT INTO duty_list VALUES(" + "'" + input_id + "'" + ");");
                            db.execSQL("INSERT INTO duty_log VALUES(" + "'" + input_id + "'" + "," + "'" + dateFormat.format(date) + "'" + "," + "'" + " " + "'" + ");");

                            AlertDialog dialog = builder.create();
                            dialog.show();
                            refresh();
                        }
                    }
                    else{
                        if (test_inlab()){
                            Toast.makeText(this, "Bye!", Toast.LENGTH_LONG).show();
                            edittext_input_id.setText("");
                            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            Date date = new Date();
                            db.execSQL("DELETE FROM id_machine WHERE input_id=" + "'" + input_id + "'");
                            db.execSQL("UPDATE student_log SET leave_datetime=" + "'" + dateFormat.format(date) + "'" + "WHERE input_id=" + "'" + input_id + "'" + "AND leave_datetime=" + "' '");
                            refresh();
                        }
                        else {
                            Intent intent = new Intent(this, surface_desktop.class);
                            intent.putExtra(STUDENT_TEACHER_POS, "0");
                            intent.putExtra(INPUT_ID_INTENTEXTRA, input_id);
                            startActivity(intent);}
                    }
                }*/
                else if (test_inlab()){
                    Toast.makeText(this, "Bye!", Toast.LENGTH_LONG).show();
                    edittext_input_id.setText("");
                    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date date = new Date();
                    db.execSQL("DELETE FROM id_machine WHERE input_id=" + "'" + input_id + "'");
                    db.execSQL("UPDATE student_log SET leave_datetime=" + "'" + dateFormat.format(date) + "'" + "WHERE input_id=" + "'" + input_id + "'" + "AND leave_datetime=" + "' '");
                    refresh();
                }
                else {
                    Intent intent = new Intent(this, surface_desktop.class);
                    intent.putExtra(STUDENT_TEACHER_POS, "0");
                    intent.putExtra(INPUT_ID_INTENTEXTRA, input_id);
                    startActivity(intent);}


            }


            else if (student_teacher_value==1){
                if (input_id.length()!=4){
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Invalid ID!");
                    builder.setMessage("Please a valid teacher ID!");

                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.setIcon(R.drawable.ic_dialog_alert);
                    dialog.show();
                }


                else if (test_inlab()){
                    Intent intent = new Intent(this, teacher_borrow_return.class);
                    intent.putExtra(STUDENT_TEACHER_POS, "1");
                    intent.putExtra(INPUT_ID_INTENTEXTRA, input_id);
                    startActivity(intent);
                }
                else {
                    Intent intent = new Intent(this, teacher_borrow_surface.class);
                    intent.putExtra(STUDENT_TEACHER_POS, "1");
                    intent.putExtra(INPUT_ID_INTENTEXTRA, input_id);
                    startActivity(intent);}
            }

        }


    }

    public boolean test_inlab (){

        Cursor c = db.rawQuery("SELECT * FROM id_machine WHERE input_id=" + "'" + input_id + "'", null);

        if (c.getCount()==0){
            Cursor c2 = db.rawQuery("SELECT * FROM id_machine_teacher WHERE input_id=" + "'" + input_id + "'", null);

            if (c2.getCount()==0){
                return false;
            }
            else {return true;}
        }
        else {
            return true;
        }
    }

    public boolean test_induty (){

        Cursor c = db.rawQuery("SELECT * FROM duty_list WHERE input_id=" + "'" + input_id + "'", null);

        if (c.getCount()==0){
                return false;
        }
        else {
            return true;
        }
    }

    public void view_desktop_user (View v){
        Intent intent = new Intent(this, view_seating.class);
        intent.putExtra(VIEW_KICK_RIGHT, "0");
        startActivity(intent);
    }

    public void view_surface (View v){
        Intent intent = new Intent(this, view_surface.class);
        intent.putExtra(VIEW_KICK_RIGHT, "0");
        startActivity(intent);
    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_student_input, menu);
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
