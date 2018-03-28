package ds.com.icl_lab_v3;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;



public class system_config extends ActionBarActivity {

    public final static int SELECT_FILE = 1;

    SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db=openOrCreateDatabase("RegistrationDB", Context.MODE_PRIVATE, null);
        setContentView(R.layout.config);
    }

    public static ArrayList<String> list_of_ds = new ArrayList<String>();

    public static ArrayList<String> list_of_ds_name = new ArrayList<String>();

    ArrayList<String> list_of_duty = new ArrayList<String>();


    public void btn_config(View v){
        update_ds_info();
    }

    public void btn_export(View v){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Export current duty configuration?");
        builder.setMessage("Old output file will be overwritten!");

        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                export_duty();
                show_message("Configuration exported to /curr_duty.csv");
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

    public void export_duty(){

        CSVWriter writer;

        try {
            writer = new CSVWriter(new FileWriter(Environment.getExternalStorageDirectory() + "/curr_duty.csv"));
            Cursor c = db.rawQuery("SELECT * FROM ds_info;", null);

            while(c.moveToNext()) {
                String[] temp = {c.getString(0), c.getString(1), c.getString(2)};
                writer.writeNext(temp);
            }
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void update_ds_info(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Import current duty configuration?");
        builder.setMessage("Old duty configuration will be overwritten!");

        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {


                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("text/csv");

                if (intent.resolveActivityInfo(getPackageManager(), 0) != null)
                {
                    startActivityForResult(Intent.createChooser(intent, "Select file picker"), SELECT_FILE);
                }
                else
                {
                    show_message("Please install a file manager!");
                }
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

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode==RESULT_OK){
            if (requestCode==SELECT_FILE){
                Uri uri = data.getData();
                File file = new File(uri.getPath());
                String path = file.getAbsolutePath();
                update_db(path);
            }
        }
    }

    public void update_db(String path){
        try {
            CSVReader reader = new CSVReader(new FileReader(path), ',' , '"' , 0);
            String[] nextLine;
            list_of_ds.clear();
            list_of_ds_name.clear();
            list_of_duty.clear();
            while ((nextLine = reader.readNext()) != null) {
                if (nextLine != null) {
                    //Verifying the read data here
                    System.out.println(Arrays.toString(nextLine));
                    list_of_ds.add(nextLine[0]);
                    list_of_ds_name.add(nextLine[1]);
                    list_of_duty.add(nextLine[2]);
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        db.execSQL("DROP TABLE IF EXISTS ds_info;");
        db.execSQL("CREATE TABLE IF NOT EXISTS ds_info(input_id VARCHAR(8),ds_name VARCHAR(30),duty_time VARCHAR(2));");

        for (int i=0;i<list_of_ds.size();i++){
            db.execSQL("INSERT INTO ds_info VALUES(" + "'" + list_of_ds.get(i) + "'" + "," + "'" + list_of_ds_name.get(i) + "'" + "," +"'" + list_of_duty.get(i) + "'" + ");");
        }

        refresh();
    }

    public void refresh(){
        LinearLayout dynamic_container=(LinearLayout) findViewById(R.id.display_list);
        int size=list_of_ds.size();
        for (int i=size-1;i>=0;i--){
            LinearLayout tr1 = new LinearLayout(this);
            dynamic_container.addView(tr1, 0);
//            dynamic_container.addView(tr1);
            TextView tv=new TextView(this);
            tv.setText(list_of_ds.get(i));
            tv.setTextSize(12);
            tv.setVisibility(View.VISIBLE);
            tv.setPadding(0,0,30,0);
            tv.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.FILL_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT, 310f));
            tr1.addView(tv);

            TextView tv1=new TextView(this);
            tv1.setText(list_of_ds_name.get(i));
            tv1.setTextSize(12);
            tv1.setVisibility(View.VISIBLE);
            tv1.setPadding(0,0,30,0);

            tv1.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.FILL_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT, 200f));
            tr1.addView(tv1);

            TextView tv2=new TextView(this);
            tv2.setText(list_of_duty.get(i));
            tv2.setTextSize(12);
            tv2.setVisibility(View.VISIBLE);
            tv2.setPadding(0,0,30,0);
            tv2.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.FILL_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT, 350f));
            tr1.addView(tv2);

        }

        show_message("Duty configuration updated");
    }


    public void clear_logs (View v){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Clear all existing machine logs?");
        builder.setMessage("You cannot undo this action!");
        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                db.execSQL("DROP TABLE IF EXISTS student_log;");
                db.execSQL("CREATE TABLE IF NOT EXISTS student_log(input_id VARCHAR(8),machine VARCHAR(8),arrive_datetime DATETIME, leave_datetime DATETIME);");
                show_message("All machine logs successfully cleared");
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

    public void clear_logs_ds (View v){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Clear all existing DS logs?");
        builder.setMessage("You cannot undo this action!");
        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                db.execSQL("DROP TABLE IF EXISTS duty_log;");
                db.execSQL("CREATE TABLE IF NOT EXISTS duty_log(input_id VARCHAR(8),sub_id VARCHAR(8),arrive_datetime DATETIME, leave_datetime DATETIME);");
                show_message("All DS logs successfully cleared");
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

    public void clear_teacher (View v){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Clear all teacher borrowed surface status?");
        builder.setMessage("You cannot undo this action!");
        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                db.execSQL("DROP TABLE IF EXISTS id_machine_teacher;");
                db.execSQL("CREATE TABLE IF NOT EXISTS id_machine_teacher(input_id VARCHAR(8),machine VARCHAR(8));");
//                refresh_view();
                show_message("Teacher borrowed surface successfully cleared");
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

    AlertDialog.Builder builder;
    AlertDialog dialog;

    public void change_ds_pw (View v){

        builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.change_pw_dialog, null));
        builder.setTitle("Change DS password");
//        builder.setCancelable(false);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog2, int id) {

                EditText et_old_pw=(EditText) dialog.findViewById(R.id.old_pw);
                EditText et_new_pw=(EditText) dialog.findViewById(R.id.new_pw);
                EditText et_confirm_new_pw=(EditText) dialog.findViewById(R.id.confirm_new_pw);

                String old_pw=et_old_pw.getText().toString();
                String new_pw=et_new_pw.getText().toString();
                String confirm_new_pw=et_confirm_new_pw.getText().toString();

                Cursor c=db.rawQuery("SELECT value FROM setting WHERE setting_name = 'ds_pw';",null);
                c.moveToFirst();
                String old_pw_query=c.getString(0);
                if (!old_pw_query.equals(old_pw)){
                    show_message("Old password does not match!");
                }
                else {
                    if (!new_pw.equals(confirm_new_pw)){
                        show_message("New password and Confirm new password do not match");
                    }
                    else {
                        db.execSQL("UPDATE setting SET value='" + new_pw + "' WHERE setting_name='ds_pw' ");
                        show_message("DS Password successfully changed");
                    }
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        dialog = builder.create();
        dialog.show();
    }

    public void change_admin_pw (View v){

            builder = new AlertDialog.Builder(this);
            LayoutInflater inflater = this.getLayoutInflater();
            builder.setView(inflater.inflate(R.layout.change_pw_dialog, null));
            builder.setTitle("Change Admin password");
//            builder.setCancelable(false);

            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog2, int id) {

                    EditText et_old_pw=(EditText) dialog.findViewById(R.id.old_pw);
                    EditText et_new_pw=(EditText) dialog.findViewById(R.id.new_pw);
                    EditText et_confirm_new_pw=(EditText) dialog.findViewById(R.id.confirm_new_pw);

                    String old_pw=et_old_pw.getText().toString();
                    String new_pw=et_new_pw.getText().toString();
                    String confirm_new_pw=et_confirm_new_pw.getText().toString();

                    Cursor c=db.rawQuery("SELECT value FROM setting WHERE setting_name = 'admin_pw';",null);
                    c.moveToFirst();
                    String old_pw_query=c.getString(0);
                    if (!old_pw_query.equals(old_pw)){
                        show_message("Old password does not match!");
                    }
                    else {
                        if (!new_pw.equals(confirm_new_pw)){
                            show_message("New password and Confirm new password do not match");
                        }
                        else {
                            db.execSQL("UPDATE setting SET value='" + new_pw + "' WHERE setting_name='admin_pw' ");
                            show_message("Admin Password successfully changed");
                        }
                    }
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                }
            });
            dialog = builder.create();
            dialog.show();
        }



    public void show_message (String message){
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }


}



