package com.games.jefferson.collectingnumbers;

import android.content.Intent;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.TextView;
import android.widget.ViewFlipper;


public class Notebook extends AppCompatActivity {

    ViewFlipper notebookFlipper;
    TextView textView;
    float startX;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notebook);

        notebookFlipper = (ViewFlipper)findViewById(R.id.notebook_flipper);

        Intent intent = getIntent();
        int notebook = intent.getIntExtra(MainActivity.NOTEBOOK_NUMBER, 0);
        //load notebook here
        textView = (TextView)findViewById(R.id.notebooknum);
        textView.setTextSize(40);
        textView.setText("This is notebook Number: " + notebook);
        //-----
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_notebook, menu);
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

    public  boolean onTouchEvent(MotionEvent e){
        int action = MotionEventCompat.getActionMasked(e);

        switch(action) {
            case (MotionEvent.ACTION_DOWN) :
                //textView.setText("Action was DOWN");
                startX = e.getX();
                return true;
            case (MotionEvent.ACTION_MOVE) :
                //textView.setText("Action was MOVE");
                return true;
            case (MotionEvent.ACTION_UP) : {
                //textView.setText("Action was UP");

                float endX = e.getX();

                if (startX < endX) {//swiped right
                    if (notebookFlipper.getDisplayedChild() == 0) {
                        return true;
                    }
                    notebookFlipper.setInAnimation(this, R.anim.in_from_left);
                    notebookFlipper.setOutAnimation(this, R.anim.out_to_right);
                    notebookFlipper.showPrevious();
                }
                if (startX > endX) {//swiped left
                    if (notebookFlipper.getDisplayedChild() == 2) {
                        return true;
                    }
                    notebookFlipper.setInAnimation(this, R.anim.in_from_right);
                    notebookFlipper.setOutAnimation(this, R.anim.out_to_left);
                    notebookFlipper.showNext();
                }
                return true;
            }
            case (MotionEvent.ACTION_CANCEL) :
               // textView.setText("Action was CANCEL");
                return true;
            case (MotionEvent.ACTION_OUTSIDE) :
                textView.setText("Movement occurred outside bounds " +
                        "of current screen element");
                return true;
            default :
                return super.onTouchEvent(e);
        }
    }
}
