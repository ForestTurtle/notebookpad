package com.games.jefferson.collectingnumbers;


import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;


import java.io.File;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * The activity the application starts on
 * Views include the list of notebooks
 */
public class MainActivity extends AppCompatActivity {

    public final static String EXTRA_MESSAGE = "com.games.jefferson.collectingnumbers.MESSAGE";

    public int notebookCount;
    TextView buttonCountText;
    LinearLayout myNotebooks;
    ArrayList<Notebook> notebookList = new ArrayList<>();
    TextView noNotebooks;
    PopupWindow createNotePopup;
    LinearLayout mainLayout;
    DatabaseController notebooksDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //user interface and class scope variables
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        noNotebooks = (TextView)findViewById(R.id.nonotebooks);
        mainLayout = (LinearLayout)findViewById(R.id.main_wall);

        //setting up the popup for creating notebooks
        createNotePopup = new PopupWindow(this);
        createNotePopup.setContentView(getLayoutInflater().inflate(R.layout.activity_add_notebook, null, false));
        createNotePopup.setFocusable(true);

        //the layout that holds the notebooks
        myNotebooks = (LinearLayout)findViewById(R.id.notebooks);
    }

    protected void onStart(){
        super.onStart();
        notebookList.clear();
        myNotebooks.removeAllViews();
        notebookCount = 0;
        //load the notebooks into the notebook list
        notebooksDb = new DatabaseController(getApplicationContext());
        Cursor notebookCursor = notebooksDb.getNotebooks();
        if(notebookCursor != null && notebookCursor.getCount()>0) {
            while (notebookCursor.moveToNext()){
                String title = notebookCursor.getString(0);
                int notebookIndex = notebookCursor.getInt(1);
                int imgId = notebookCursor.getInt(2);
                int recentPage = notebookCursor.getInt(3);
                if (notebookIndex > notebookCount){
                    notebookCount = notebookIndex;
                }
                Notebook newNotebook = new Notebook(title, notebookIndex, imgId, recentPage, mainLayout);
                notebookList.add(newNotebook);
                myNotebooks.addView(newNotebook.getView());
            }
            noNotebooks.setVisibility(View.GONE);
        }
        notebookCount++;
        //notebook counter text setup
        buttonCountText = (TextView)findViewById(R.id.count1);
        buttonCountText.setText("Notebooks Added: " + notebookCount);
    }

    /**
     * attempts to load the src file into the string. if the file doesn't exist, create it
     * @param src
     * @return the loaded string
     */
    private String loadText(File src){
        String loadedText = "";
        if(src.exists()){ //load the file
            try {
                Scanner fileScanner = new Scanner(src).useDelimiter("\\Z");
                if(fileScanner.hasNext())
                    loadedText += fileScanner.next();
                fileScanner.close();
            } catch (IOException e){
                AlertDialog.Builder creationAlert = new AlertDialog.Builder(getApplicationContext());
                creationAlert.setMessage("File unable to be created"+e.getMessage());
                creationAlert.show();
            }
        } else {
            //create the file
            try {
                src.createNewFile();
            } catch (IOException e) {
                AlertDialog.Builder creationAlert = new AlertDialog.Builder(getApplicationContext());
                creationAlert.setMessage("File unable to be created"+e.getMessage());
                creationAlert.show();
            }
        }
        return loadedText;
    }

    protected void onPause(){
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id){
            case R.id.action_settings:
                return true;
            case R.id.action_search:
                //notebookList.get(0).setTitle("test");
                notebooksDb.reset();
                return true;
            default: return super.onOptionsItemSelected(item);
        }

    }
    /**
     * helper method that opens the popup prompting the user to make a new notebook
     * @param view
     */
    public void createNewNotebook(View view){
        createNotePopup.showAtLocation(mainLayout, Gravity.CENTER, 0, 0);
        createNotePopup.update(mainLayout.getWidth() - 40, mainLayout.getHeight() - 40);

    }

    public void dismiss(View view){
        createNotePopup.dismiss();
    }

    /**
     * Called when the user enters the information to make a new notebook
     * puts the notebook on the screen and saves it to memory
     * @param view
     */
    public void addNotebook(View view){
        //retrieve the title
        String title = ((EditText)createNotePopup.getContentView().findViewById(R.id.notebook_title)).getText().toString();

        //retrieve the image id
        int imgId = 0; //to be implemented

        //create the notebook
        Notebook newNotebook = new Notebook(title, notebookCount, imgId, view);
        notebookCount++;

        //add last element of the array
        notebookList.add(newNotebook);
        myNotebooks.addView(newNotebook.getView());

        //save the new notebook to memory
        notebooksDb.addNotebook(title, newNotebook.getIndex(), imgId, newNotebook.getRecent());

        //refresh text
        buttonCountText.setText("Notebooks Added: " + notebookCount);

        if(notebookList.size() > 0) {
            noNotebooks.setVisibility(View.GONE);
        }

        AlertDialog.Builder creationAlert = new AlertDialog.Builder(view.getContext());
        creationAlert.setMessage("NotebookView created");
        creationAlert.show();


        //remove the new notebook prompt
        createNotePopup.dismiss();
    }

    /**
     * remove a notebook from existence
     * @param view
     * @param notebookId
     */
    public void deleteNotebook(View view, int notebookId){
        Notebook temp = notebookList.get(0);

        //retrieve notebook object
        for (Notebook n: notebookList){
            if(n.getIndex() == notebookId){
                temp = n;
                break;
            }
        }

        //remove from view
        myNotebooks.removeView(temp.getView());

        //remove from notebookList
        notebookList.remove(temp);

        //remove from database
        notebooksDb.deleteNotebook(notebookId);

        //show deletion
        AlertDialog.Builder creationAlert = new AlertDialog.Builder(view.getContext());
        creationAlert.setMessage("notebook deleted");
        creationAlert.show();
    }

    @Override
    protected void onStop(){
        super.onStop();
        notebooksDb.close();
    }
}
