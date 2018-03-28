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
import android.widget.EditText;


public class ds_console_password extends ActionBarActivity {

    EditText password_et;
    SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.password_screen);
        password_et=(EditText) findViewById(R.id.input_password);
        db=openOrCreateDatabase("RegistrationDB", Context.MODE_PRIVATE, null);
    }

    public void enter_password (View v){
        String enter_password = password_et.getText().toString();

        Cursor c=db.rawQuery("SELECT value FROM setting WHERE setting_name = 'ds_pw';",null);
        c.moveToFirst();
        String pw=c.getString(0);

        if (enter_password.equals(pw)){
            Intent intent = new Intent(this, ds_console.class);
            startActivity(intent);
        }
        else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Wrong password!");
            builder.setMessage("Please re-enter your password!");

            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    password_et.setText("");
                }
            });
            AlertDialog dialog = builder.create();
            dialog.setIcon(R.drawable.ic_dialog_alert);
            dialog.show();
        }
    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_ds_console_password, menu);
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
