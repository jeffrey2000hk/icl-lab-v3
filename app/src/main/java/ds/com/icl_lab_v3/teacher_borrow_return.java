package ds.com.icl_lab_v3;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class teacher_borrow_return extends ActionBarActivity {

    public final static String STUDENT_TEACHER_POS = "STUDENT_TEACHER_POS";
    public final static String INPUT_ID_INTENTEXTRA = "INPUT_ID";
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent original_intent = getIntent();
        setContentView(R.layout.teacher_borrow_return);
        db=openOrCreateDatabase("RegistrationDB", Context.MODE_PRIVATE, null);
        String borrowed="";
        for (int i=0;i<40;i++) {
            Cursor c = db.rawQuery("SELECT * FROM id_machine_teacher WHERE machine=" + "'" + "T002" + String.format("%02d", i) + "' AND input_id=" + "'" + original_intent.getStringExtra(INPUT_ID_INTENTEXTRA) + "'" + ";", null);
            if (c.getCount() != 0) {
                borrowed += ("T002" + String.format("%02d", i)) + ", ";
            }
        }
        TextView borrowed_tv = (TextView) findViewById(R.id.textView5);
        borrowed_tv.setText(borrowed.substring(0, borrowed.length()-2));
    }

    public void teacher_choose_borrow (View v){

        Intent original_intent = getIntent();

        Intent intent = new Intent(this, teacher_borrow_surface.class);
        intent.putExtra(STUDENT_TEACHER_POS, "1");
        intent.putExtra(INPUT_ID_INTENTEXTRA, original_intent.getStringExtra(INPUT_ID_INTENTEXTRA));
        startActivity(intent);
    }

    public void teacher_choose_return (View v){
        Intent original_intent = getIntent();
        Intent intent = new Intent(this, teacher_return_surface.class);
        intent.putExtra(STUDENT_TEACHER_POS, "1");
        intent.putExtra(INPUT_ID_INTENTEXTRA, original_intent.getStringExtra(INPUT_ID_INTENTEXTRA));
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_teacher_borrow_return, menu);
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
