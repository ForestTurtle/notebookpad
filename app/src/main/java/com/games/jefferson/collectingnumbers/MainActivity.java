package com.games.jefferson.collectingnumbers;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    public final static String EXTRA_MESSAGE = "com.games.jefferson.collectingnumbers.MESSAGE";
    public final static String NOTEBOOK_NUMBER = "com.games.jefferson.collectingnumbers.NUMBER";
    public int buttonCount1;
    public int buttonCount2;
    TextView c1;
    LinearLayout myNotebooks;
    ArrayList<Button> notebookButtons = new ArrayList<Button>();
    TextView noNotebooks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //user interface and class scope variables
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        buttonCount1 = 0; //the counter
        buttonCount2 = 0; //never used
        noNotebooks = (TextView)findViewById(R.id.nonotebooks);

        //button presses;
        c1 = (TextView)findViewById(R.id.count1);
        c1.setText("button presses: "+buttonCount1);

        myNotebooks = (LinearLayout)findViewById(R.id.notebooks);
        if(notebookButtons.size() > 0){
            noNotebooks.setVisibility(View.GONE);

            for (int i = 0; i < notebookButtons.size(); i++) {
                myNotebooks.addView(notebookButtons.get(i));
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.activity_main_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
       /* if (id == R.id.action_settings) {
            return true;
        }*/

        switch (id){
            case R.id.action_settings:
                sendMessage(this.getCurrentFocus());
                return true;
            case R.id.action_search:
                buttonCount1++;
                //creating a new notebook button
                Button temp = new Button(getApplicationContext());
                temp.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT
                        , ViewGroup.LayoutParams.WRAP_CONTENT));
                temp.setText("Notebook" + buttonCount1);
                temp.setId(buttonCount1);
                temp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //sendMessage(v);
                        goToNotebook(v, v.getId());
                    }
                });
                notebookButtons.add(temp);
                //add last element of the array
                myNotebooks.addView(notebookButtons.get(notebookButtons.size()-1));
                c1.setText("Notebooks Added: "+buttonCount1);

                if(notebookButtons.size() > 0) {
                    noNotebooks.setVisibility(View.GONE);
                }

                return true;
            default: return super.onOptionsItemSelected(item);
        }


    }

    public void sendMessage(View view){
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        EditText editText = (EditText)findViewById(R.id.edit_message);
        String message = editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }

    public void goToNotebook(View view, int notebookNum){
        Intent intent = new Intent(this, Notebook.class);
        intent.putExtra(NOTEBOOK_NUMBER, notebookNum);
        startActivity(intent);
    }
}
