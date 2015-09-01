package com.games.jefferson.collectingnumbers;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * The notebook information holder
 * used to instantiate an object with the notebook information from the database
 * Created by jeff on 7/28/2015.
 */
public class Notebook {

    public final static String NOTEBOOK_NUMBER = "com.games.jefferson.collectingnumbers.NUMBER";
    public final static String RECENT = "com.games.jefferson.collectingnumbers.RECENT_PAGE";
    public final static String NOTEBOOK_TITLE = "com.games.jefferson.collectingnumbers.TITLE";
    private String title;
    private int index; //unique for this notebook
    private int recent;
    private int image; //the number for the inmage
    private LinearLayout minNotebook;

    public Notebook(String t, int i, int img, View v){
        this(t,i,img,0, v);
    }

    public Notebook(String t, int i, int img, int r, View v){
        title = t;
        index = i;
        image = img;
        recent = r;
        createNotebookView(v);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        TextView temp = (TextView)minNotebook.getChildAt(0);
        temp.setText(title);
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
        //button fix here
    }

    public int getRecent() {
        return recent;
    }

    public void setRecent(int recent) {
        this.recent = recent;
    }

    public int getIndex() {
        return index;
    }

    public LinearLayout getButton(){
        return minNotebook;
    }

    /**
     * Helper method to create the notebook button on the screen
     */
    private void createNotebookView(View view){
        //creating the layout to hold the notebook
        LinearLayout minNote = new LinearLayout(view.getContext());
        minNote.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT
                , ViewGroup.LayoutParams.WRAP_CONTENT));
        minNote.setId(index); //notebook id


        //creating the title of the notebook
        TextView title = new TextView(minNote.getContext());
        title.setText(this.title);
        minNote.addView(title);
        title.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT
                , ViewGroup.LayoutParams.WRAP_CONTENT, .2f));
        title.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                goToNotebook(v);
            }
        });


        //creating the edit button
        Button edit = new Button(view.getContext());
        edit.setText("edit");
        minNote.addView(edit);
        edit.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT
                , ViewGroup.LayoutParams.WRAP_CONTENT, .4f));
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //sendMessage(v);
                //goToNotebook(v);
            }
        });


        //creating delete button
        Button delete = new Button(view.getContext());
        delete.setText("del");
        minNote.addView(delete);
        delete.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT
                , ViewGroup.LayoutParams.WRAP_CONTENT, .4f));
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });


        minNotebook = minNote;
    }
    /**
     * changes the activity to the corresponding notebook
     * @param v
     */
    public void goToNotebook(View v){
        Intent intent = new Intent(v.getContext(), NotebookView.class);
        intent.putExtra(NOTEBOOK_NUMBER, index);
        intent.putExtra(RECENT, recent);
        intent.putExtra(NOTEBOOK_TITLE, title);
        v.getContext().startActivity(intent);
    }
}
