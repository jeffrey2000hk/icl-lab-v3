package ds.com.icl_lab_v3;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


public class thankyou extends ActionBarActivity {

    public final static String MACHINE_ID = "MACHINE_ID";
    public final static String BORROW_RETURN = "BORROW_RETURN";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.thankyou);
        Intent original_intent = getIntent();
        TextView tv_machine_id = (TextView) findViewById(R.id.thankyou_machineID);
        tv_machine_id.setText(original_intent.getStringExtra(MACHINE_ID));
        if (original_intent.getStringExtra(BORROW_RETURN).equals("return")){
            TextView borrow_return_tv = (TextView) findViewById(R.id.textView2);
            borrow_return_tv.setText("You have returned machine(s)");
        }
    }

    public void thankyou_back (View v){
        if (create_session.lab_state==null||create_session.lab_state.length()==0){
            Intent intent = new Intent(this, MainMenu.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        else {
            Intent intent = new Intent(this, MainMenu.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            Intent intent2 = new Intent(this, welcome.class);
            startActivity(intent2);
        }
    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_thankyou, menu);
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
