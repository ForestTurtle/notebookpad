package com.games.jefferson.collectingnumbers;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

/**
 * The notebook objects
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
    private Button button;

    public Notebook(String t, int i, int img, View v){
        this(t,i,img,0, v);
    }

    public Notebook(String t, int i, int img, int r, View v){
        title = t;
        index = i;
        image = img;
        recent = r;
        createNotebookButton(v);
    }



    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        button.setText(title);
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

    public Button getButton(){
        return button;
    }

    /**
     * Helper method to create the notebook button on the screen
     */
    private void createNotebookButton(View view){

        //creating a new notebook button
        Button temp = new Button(view.getContext());
        temp.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT
                , ViewGroup.LayoutParams.WRAP_CONTENT));
        temp.setText(title);
        temp.setId(index); //the notebook id

        temp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //sendMessage(v);
                goToNotebook(v);
            }
        });
        button = temp;
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
