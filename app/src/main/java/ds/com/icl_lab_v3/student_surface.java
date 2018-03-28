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
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class student_surface extends ActionBarActivity {

    SQLiteDatabase db;
    String surface_input;
    public final static String MACHINE_ID = "MACHINE_ID";
    public final static String BORROW_RETURN = "BORROW_RETURN";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_surface);
        db=openOrCreateDatabase("RegistrationDB", Context.MODE_PRIVATE, null);
    }

    public void input_student_surface (View v) {

        TextView student_surface_id = (TextView) findViewById(R.id.student_surface_id);
        surface_input = student_surface_id.getText().toString();
        if (surface_input.length()==0){
            onebutton_dialog("Missing ID!", "Please enter the ID of your Surface!", "OK");
        }
        else if (Integer.parseInt(surface_input) > 239 | Integer.parseInt(surface_input) <200){
            onebutton_dialog("Invalid ID!", "Please enter a valid Surface ID!", "OK");
            student_surface_id.setText("");
        }
        else if (check_borrowed()){
            onebutton_dialog("This computer is taken!", "Please choose another computer!", "OK");
        }
        else {
            twobutton_dialog("Use this machine?", "Use surface T00" + surface_input + "?", "Confirm", "Cancel");
        }
    }

    public void twobutton_dialog(String title, String message, String ok, String cancel){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date();
                Intent original_intent = getIntent();
                db.execSQL("INSERT INTO id_machine VALUES(" + "'" + original_intent.getStringExtra(id_input.INPUT_ID_INTENTEXTRA) + "'" + ',' + "'" + "T00" + surface_input + "'" +                                      ");");
                db.execSQL("INSERT INTO student_log VALUES(" + "'" + original_intent.getStringExtra(id_input.INPUT_ID_INTENTEXTRA) + "'" + "," + "'" + "T00" + surface_input + "'" + "," + "'" + dateFormat.format(date) + "'" + "," + "'" + " " + "'" + ");");
                launch_thankyou();
            }
        });
        builder.setNegativeButton(cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        AlertDialog dialog = builder.create();
        dialog.setIcon(R.drawable.ic_dialog_alert);
        dialog.show();
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

    public void launch_thankyou(){
        Intent intent = new Intent(this, thankyou.class);
        intent.putExtra(MACHINE_ID, "T00" + surface_input);
        intent.putExtra(BORROW_RETURN, "borrow");
        startActivity(intent);
    }

    public boolean check_borrowed(){
        Cursor c = db.rawQuery("SELECT * FROM id_machine WHERE machine='T00" + surface_input +            "'", null);
        if (c.getCount()!=0){
            return true;
        }
        else {Cursor c2 = db.rawQuery("SELECT * FROM id_machine_teacher WHERE machine='T00" + surface_input +            "'", null);
            if (c2.getCount()!=0){return true;}
            else {return false;}
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_student_surface, menu);
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
