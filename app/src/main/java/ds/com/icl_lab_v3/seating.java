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
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class seating extends ActionBarActivity {

    int machine_selection;
    public final static String MACHINE_ID = "MACHINE_ID";
    public final static String BORROW_RETURN = "BORROW_RETURN";
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seating);
        db=openOrCreateDatabase("RegistrationDB", Context.MODE_PRIVATE, null);


        // Get int value of each id
        int [] array_of_id = new int [30];
        R.id tempr = new R.id();
        for (int i=0;i<12;i++){
            try {
                Field f1 = tempr.getClass().getField("btn" + Integer.toString(i+1));
                array_of_id[i] = (Integer) f1.get(tempr);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        //
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
        Cursor c = db.rawQuery("SELECT * FROM id_machine WHERE machine='SCL_" + String.format("%02d", machine_selection) +            "'", null);
        if (c.getCount()!=0){
            onebutton_dialog("This computer is taken!", "Please choose another computer!", "OK");
        }
        else {twobutton_dialog("Use this machine?" , "Use machine SCL_" + String.format("%02d", machine_selection) + "?", "Confirm", "Cancel");}
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_seating, menu);
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

    public void twobutton_dialog(String title, String message, String ok, String cancel){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(ok,
                new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog_ok();
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

    public void dialog_ok(){

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();

        Intent original_intent = getIntent();
        db.execSQL("INSERT INTO id_machine VALUES(" + "'" + original_intent.getStringExtra(id_input.INPUT_ID_INTENTEXTRA) + "'" + ',' + "'" + "SCL_" + String.format("%02d", machine_selection) + "'" +                                      ");");
        db.execSQL("INSERT INTO student_log VALUES(" + "'" + original_intent.getStringExtra(id_input.INPUT_ID_INTENTEXTRA) + "'" + "," + "'" + "SCL_" + String.format("%02d", machine_selection) + "'" + "," + "'" + dateFormat.format(date) + "'" + "," + "'" + " " + "'" + ");");

        Cursor c=db.rawQuery("SELECT * FROM student_log WHERE machine='"+"SCL_" + String.format("%02d", machine_selection) +"'" + ";", null);

        c.moveToFirst();
        Intent intent = new Intent(this, thankyou.class);
        intent.putExtra(MACHINE_ID, "SCL_" + String.format("%02d", machine_selection));
        intent.putExtra(BORROW_RETURN, "borrow");
        startActivity(intent);
    }
}

//    Button btn1;
//    Button btn2;
//    Button btn3;
//    Button btn4;
//    Button btn5;
//    Button btn6;
//    Button btn7;
//    Button btn8;
//    Button btn9;
//    Button btn10;
//    Button btn11;
//    Button btn12;

//        Button[] arrayofbuttons = new Button[] {btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9, btn10, btn11, btn12};
//        btn1 = (Button) findViewById(R.id.btn1);
//        btn2 = (Button) findViewById(R.id.btn2);
//        btn3 = (Button) findViewById(R.id.btn3);
//        btn4 = (Button) findViewById(R.id.btn4);
//        btn5 = (Button) findViewById(R.id.btn5);
//        btn6 = (Button) findViewById(R.id.btn6);
//        btn7 = (Button) findViewById(R.id.btn7);
//        btn8 = (Button) findViewById(R.id.btn8);
//        btn9 = (Button) findViewById(R.id.btn9);
//        btn10 = (Button) findViewById(R.id.btn10);
//        btn11 = (Button) findViewById(R.id.btn11);
//        btn12 = (Button) findViewById(R.id.btn12);
//        btn12 = (Button) findViewById(R.id.btn13);
//        btn12 = (Button) findViewById(R.id.btn14);
//        btn12 = (Button) findViewById(R.id.btn15);
//        btn12 = (Button) findViewById(R.id.btn16);
//        btn12 = (Button) findViewById(R.id.btn17);
//        btn12 = (Button) findViewById(R.id.btn18);
//        btn12 = (Button) findViewById(R.id.btn19);
//        btn12 = (Button) findViewById(R.id.btn20);
//        btn11 = (Button) findViewById(R.id.btn21);
//        btn12 = (Button) findViewById(R.id.btn22);
//        btn12 = (Button) findViewById(R.id.btn23);
//        btn12 = (Button) findViewById(R.id.btn24);
//        btn12 = (Button) findViewById(R.id.btn25);
//        btn12 = (Button) findViewById(R.id.btn26);
//        btn12 = (Button) findViewById(R.id.btn27);
//        btn12 = (Button) findViewById(R.id.btn28);
//        btn12 = (Button) findViewById(R.id.btn29);
//        btn12 = (Button) findViewById(R.id.btn30);