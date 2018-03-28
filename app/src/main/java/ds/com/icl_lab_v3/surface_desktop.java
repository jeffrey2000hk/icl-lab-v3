package ds.com.icl_lab_v3;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


public class surface_desktop extends ActionBarActivity {

    public final static String STUDENT_TEACHER_POS = "STUDENT_TEACHER_POS";
    public final static String INPUT_ID_INTENTEXTRA = "INPUT_ID";

    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.surface_desktop);

        db=openOrCreateDatabase("RegistrationDB", Context.MODE_PRIVATE, null);





        TextView remain_desktop_tv = (TextView) findViewById(R.id.desktop_surface_remaining_desktop);
        TextView remain_surface_tv = (TextView) findViewById(R.id.desktop_surface_remaining_surface);

        Cursor c = db.rawQuery("SELECT COUNT(*) FROM id_machine WHERE machine LIKE 'SCL_%';", null);
        c.moveToFirst();
        remain_desktop_tv.setText(Integer.toString(12-Integer.parseInt(c.getString(0)))+" left");

        Cursor c2 = db.rawQuery("SELECT COUNT(*) FROM id_machine WHERE machine LIKE 'T002%';", null);
        c2.moveToFirst();
        Cursor c3 = db.rawQuery("SELECT COUNT(*) FROM id_machine_teacher WHERE machine LIKE 'T002%';", null);
        c3.moveToFirst();
        remain_surface_tv.setText(Integer.toString(40-Integer.parseInt(c2.getString(0))-Integer.parseInt(c3.getString(0)))+" left");
    }

    public void goto_desktop (View v){
        Intent original_intent = getIntent();
        Intent intent = new Intent(this, seating.class);
        intent.putExtra(STUDENT_TEACHER_POS, original_intent.getStringExtra(id_input.STUDENT_TEACHER_POS));
        intent.putExtra(INPUT_ID_INTENTEXTRA, original_intent.getStringExtra(id_input.INPUT_ID_INTENTEXTRA));
        startActivity(intent);
    }

    public void goto_surface (View v){
        Intent original_intent = getIntent();
        Intent intent = new Intent(this, student_surface.class);
        intent.putExtra(STUDENT_TEACHER_POS, original_intent.getStringExtra(id_input.STUDENT_TEACHER_POS));
        intent.putExtra(INPUT_ID_INTENTEXTRA, original_intent.getStringExtra(id_input.INPUT_ID_INTENTEXTRA));
        startActivity(intent);
    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_surface_desktop, menu);
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
