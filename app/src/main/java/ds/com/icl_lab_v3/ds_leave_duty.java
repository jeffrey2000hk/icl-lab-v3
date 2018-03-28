package ds.com.icl_lab_v3;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;


public class ds_leave_duty extends Fragment {

    SQLiteDatabase db;
    String input_id;

    public ds_leave_duty() {
        // Required empty public constructor
    }

    View rootView;

    public static ArrayList<String> list_of_ds = new ArrayList<String>();

    public static ArrayList<String> list_of_ds_name = new ArrayList<String>();

    public static String[] list_of_ds_combined;

    public static ArrayList<String> list_of_duty = new ArrayList<String>();

//    ArrayList<String> day_list = new ArrayList<String>();
    //    Spinner sp1;
//    Spinner sp2;
    AutoCompleteTextView textView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    //    private static final String[] COUNTRIES = list_of_ds_combined;
    public void back_to_main (View v){
        Intent intent = new Intent(getActivity(), MainMenu.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        list_of_ds.clear();
        list_of_ds_name.clear();
        list_of_duty.clear();
        // Inflate the layout for this fragment
        db=getActivity().openOrCreateDatabase("RegistrationDB", Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS id_machine(input_id VARCHAR(8),machine VARCHAR(8));");
        db.execSQL("CREATE TABLE IF NOT EXISTS student_log(input_id VARCHAR(8),machine VARCHAR(8),arrive_datetime DATETIME, leave_datetime DATETIME);");

        Cursor c=db.rawQuery("SELECT * FROM ds_info", null);
        while (c.moveToNext()){
            list_of_ds.add(c.getString(0));
            list_of_ds_name.add(c.getString(1));
            list_of_duty.add(c.getString(2));
        }


        rootView = inflater.inflate(R.layout.ds_duty_leave, container, false);


//        setContentView(R.layout.ds_duty);

        list_of_ds_combined=new String[list_of_ds.size()];
        for (int i=0;i< list_of_ds.size();i++){
            list_of_ds_combined[i]=list_of_ds.get(i)+" "+list_of_ds_name.get(i);
        }
//        sp1 = (Spinner) findViewById(R.id.spinner2);
//        sp2 = (Spinner) rootView.findViewById(R.id.day_time_ds);
        ArrayAdapter<String> dslist = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, list_of_ds_name);
////        sp1.setAdapter(dslist);
//        day_list.add("N/A");
//        for (int i=0; i<list_of_ds.size()-1; i++){
//            if (list_of_duty.get(i).equals(create_session.lab_state)){
//                day_list.add(list_of_ds_name.get(i));
//            }
//        }
//        ArrayAdapter<String> daylist = new ArrayAdapter<String>(getActivity(),
//                android.R.layout.simple_spinner_item, day_list);
//        sp2.setAdapter(daylist);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_dropdown_item_1line, list_of_ds_combined);
        textView = (AutoCompleteTextView)
                rootView.findViewById(R.id.editText3);
        textView.setThreshold(1);
        textView.setAdapter(adapter);



        Button main_btn = (Button) rootView.findViewById(R.id.from_viewsurface_to_input);
        main_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                back_to_main(v);
            }
        });

        Button duty_btn = (Button) rootView.findViewById(R.id.from_seating_to_input);
        duty_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v)
            {
                click_duty(v);
            }
        });


        return rootView;
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
    //list_of_duty.get(list_of_ds.indexOf(input_id)).equals(create_session.lab_state)
    public void click_duty (View v){
        input_id=textView.getText().toString();


        if (input_id.length()<8){
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("You have entered an invalid DS Student ID. Please check and try again.");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
        else {
            input_id = input_id.substring(0, 8);
            if (list_of_ds.indexOf(input_id) == -1) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("You have entered an invalid DS Student ID. Please check and try again.");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            } else if (!test_induty()) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("You are currently not in duty, please go to 'Take' tab to take duty.");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
                textView.setText("");
//                refresh();
            }

            else{
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("You have successfully left duty!");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date();
                textView.setText("");
                db.execSQL("DELETE FROM duty_list WHERE input_id=" + "'" + input_id + "'");
                db.execSQL("UPDATE duty_log SET leave_datetime=" + "'" + dateFormat.format(date) + "'" + "WHERE input_id=" + "'" + input_id + "'" + "AND leave_datetime=" + "' '");

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        }
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