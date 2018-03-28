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

import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class teacher_borrow_surface extends ActionBarActivity {

    boolean [] checkbox_ticked = new boolean[40];
    boolean [] checkbox_current = new boolean[40];
    List<String> list_checked = new ArrayList<String>();
    String output_message = "";
    SQLiteDatabase db;
    public final static String MACHINE_ID = "MACHINE_ID";
    public final static String BORROW_RETURN = "BORROW_RETURN";
    int [] array_of_id = new int [50];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.surface_selection);

        R.id tempr = new R.id();
        for (int i=0;i<40;i++){
            try {
                Field f1 = tempr.getClass().getField("CheckBox" + String.format("%02d", i+1));
                array_of_id[i] = (Integer) f1.get(tempr);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        db=openOrCreateDatabase("RegistrationDB", Context.MODE_PRIVATE, null);

        for (int i=0;i<40;i++) {
            Cursor c = db.rawQuery("SELECT * FROM id_machine WHERE machine=" + "'" + "T002" + String.format("%02d", i) + "'", null);
            if (c.getCount() != 0) {
                checkbox_current[i] = true;
            }
        }

        for (int i=0;i<40;i++) {
            Cursor c = db.rawQuery("SELECT * FROM id_machine_teacher WHERE machine=" + "'" + "T002" + String.format("%02d", i) + "'", null);
            if (c.getCount() != 0) {
                checkbox_current[i] = true;
            }
        }

        for (int i=0;i<40;i++) {
            if (checkbox_current[i]) {
                CheckBox temp = (CheckBox) findViewById(array_of_id[i]);
                temp.setEnabled(false);
            }
        }
    }

    public void confirm_selection (View v){
        output_message = "";
        list_checked.clear();
        int count_machines = 0;
        for (int i=0; i<40;i++){
            CheckBox temp = (CheckBox) findViewById(array_of_id[i]);
            checkbox_ticked[i] = temp.isChecked();
            if (temp.isChecked()){
                count_machines++;
                list_checked.add("T002" + String.format("%02d", i));
            }
        }

        for (int i=0; i<list_checked.size(); i++){
            output_message = output_message + list_checked.get(i) + ", ";
        }
        twobutton_dialog("Please check the following information:", "You have borrowed " + output_message + "a total of " + Integer.toString(count_machines) + " surface." , "OK", "Cancel");
    }

    public void twobutton_dialog(String title, String message, String ok, String cancel){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(ok, new DialogInterface.OnClickListener() {
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
        output_message="";
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        Intent original_intent = getIntent();
        for (int i=0; i<list_checked.size(); i++){
            output_message = output_message + list_checked.get(i) + ", ";
            db.execSQL("INSERT INTO id_machine_teacher VALUES(" + "'" + original_intent.getStringExtra(id_input.INPUT_ID_INTENTEXTRA) + "'" + ',' + "'" + list_checked.get(i) + "'" +                                      ");");
            db.execSQL("INSERT INTO student_log VALUES(" + "'" + original_intent.getStringExtra(id_input.INPUT_ID_INTENTEXTRA) + "'" + "," + "'" + list_checked.get(i) + "'" + "," + "'" + dateFormat.format(date) + "'" + "," + "'" + " " + "'" + ");");
        }
        Intent intent = new Intent(this, thankyou.class);
        intent.putExtra(MACHINE_ID, output_message.substring(0, output_message.length() - 2));
        intent.putExtra(BORROW_RETURN, "borrow");
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_teacher_borrow_surface, menu);
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
}

//        CheckBox checkbox01 = (CheckBox) findViewById(R.id.CheckBox01);
//        CheckBox checkbox02 = (CheckBox) findViewById(R.id.CheckBox02);
//        CheckBox checkbox03 = (CheckBox) findViewById(R.id.CheckBox03);
//        CheckBox checkbox04 = (CheckBox) findViewById(R.id.CheckBox04);
//        CheckBox checkbox05 = (CheckBox) findViewById(R.id.CheckBox05);
//        CheckBox checkbox06 = (CheckBox) findViewById(R.id.CheckBox06);
//        CheckBox checkbox07 = (CheckBox) findViewById(R.id.CheckBox07);
//        CheckBox checkbox08 = (CheckBox) findViewById(R.id.CheckBox08);
//        CheckBox checkbox09 = (CheckBox) findViewById(R.id.CheckBox09);
//        CheckBox checkbox10 = (CheckBox) findViewById(R.id.CheckBox10);
//        CheckBox checkbox11 = (CheckBox) findViewById(R.id.CheckBox11);
//        CheckBox checkbox12 = (CheckBox) findViewById(R.id.CheckBox12);
//        CheckBox checkbox13 = (CheckBox) findViewById(R.id.CheckBox13);
//        CheckBox checkbox14 = (CheckBox) findViewById(R.id.CheckBox14);
//        CheckBox checkbox15 = (CheckBox) findViewById(R.id.CheckBox15);
//        CheckBox checkbox16 = (CheckBox) findViewById(R.id.CheckBox16);
//        CheckBox checkbox17 = (CheckBox) findViewById(R.id.CheckBox17);
//        CheckBox checkbox18 = (CheckBox) findViewById(R.id.CheckBox18);
//        CheckBox checkbox19 = (CheckBox) findViewById(R.id.CheckBox19);
//        CheckBox checkbox20 = (CheckBox) findViewById(R.id.CheckBox20);
//        CheckBox checkbox21 = (CheckBox) findViewById(R.id.CheckBox21);
//        CheckBox checkbox22 = (CheckBox) findViewById(R.id.CheckBox22);
//        CheckBox checkbox23 = (CheckBox) findViewById(R.id.CheckBox23);
//        CheckBox checkbox24 = (CheckBox) findViewById(R.id.CheckBox24);
//        CheckBox checkbox25 = (CheckBox) findViewById(R.id.CheckBox25);
//        CheckBox checkbox26 = (CheckBox) findViewById(R.id.CheckBox26);
//        CheckBox checkbox27 = (CheckBox) findViewById(R.id.CheckBox27);
//        CheckBox checkbox28 = (CheckBox) findViewById(R.id.CheckBox28);
//        CheckBox checkbox29 = (CheckBox) findViewById(R.id.CheckBox29);
//        CheckBox checkbox30 = (CheckBox) findViewById(R.id.CheckBox30);
//        CheckBox checkbox31 = (CheckBox) findViewById(R.id.CheckBox31);
//        CheckBox checkbox32 = (CheckBox) findViewById(R.id.CheckBox32);
//        CheckBox checkbox33 = (CheckBox) findViewById(R.id.CheckBox33);
//        CheckBox checkbox34 = (CheckBox) findViewById(R.id.CheckBox34);
//        CheckBox checkbox35 = (CheckBox) findViewById(R.id.CheckBox35);
//        CheckBox checkbox36 = (CheckBox) findViewById(R.id.CheckBox36);
//        CheckBox checkbox37 = (CheckBox) findViewById(R.id.CheckBox37);
//        CheckBox checkbox38 = (CheckBox) findViewById(R.id.CheckBox38);
//        CheckBox checkbox39 = (CheckBox) findViewById(R.id.CheckBox39);
//        CheckBox checkbox40 = (CheckBox) findViewById(R.id.CheckBox40);
//
//        CheckBox [] array_of_checkbox_temp = {checkbox01, checkbox02, checkbox03, checkbox04, checkbox05, checkbox06, checkbox07, checkbox08, checkbox09, checkbox10,
//                                              checkbox11, checkbox12, checkbox13, checkbox14, checkbox15, checkbox16, checkbox17, checkbox18, checkbox19, checkbox20,
//                                              checkbox21, checkbox22, checkbox23, checkbox24, checkbox25, checkbox26, checkbox27, checkbox28, checkbox29, checkbox30,
//                                              checkbox31, checkbox32, checkbox33, checkbox34, checkbox35, checkbox36, checkbox37, checkbox38, checkbox39, checkbox40, };
