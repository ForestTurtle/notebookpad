package com.games.jefferson.collectingnumbers;

import android.content.Intent;
import android.database.Cursor;
import android.support.v4.view.MotionEventCompat;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import java.util.TreeMap;

/**
 * The interfacer Class that displays an individual notebook
 */
public class NotebookView extends AppCompatActivity {

    ViewFlipper notebookFlipper;

    float startX;
    DatabaseController pagesDb;
    int notebookIndex;
    int currentPage;
    int currentIndex; //the current index of the page you're looking at, useful for loading pages
    /*to skip to a page quick, take its position in the sorted loadedpages array and
     subtract it with the current page's position in the loaded pages list. Then add the difference
     to the current view index to get the index of the new page.
     use floorentry or ceiling entry
      */
    int negativeCount; //a count of the negative indices b/c you can have nev indices
    TreeMap loadedPages; //sorted list of loaded pages to insert into, key: pageno, value: index

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notebook);

        //load the viewGroup
        notebookFlipper = (ViewFlipper)findViewById(R.id.notebook_flipper);
        //load the database
        pagesDb = new DatabaseController(getApplicationContext());
        //retrieve the extra information
        Intent intent = getIntent();
        notebookIndex = intent.getIntExtra(Notebook.NOTEBOOK_NUMBER, 0);
        //setTitle(intent.getStringExtra(Notebook.NOTEBOOK_TITLE));

        //initialize the loaded pages list
        loadedPages = new TreeMap();

        //get recent page, load it & 2 surrounding pages, add it to the loaded list.
        //
        int recentPage = intent.getIntExtra(Notebook.RECENT, 0);
        currentPage = recentPage;
        currentIndex = 0;
        negativeCount = 0;
        insertPage(notebookFlipper, recentPage, currentIndex);
        insertPage(notebookFlipper, recentPage + 1, currentIndex + 1);
        if(recentPage != 0){ //load previous into view, tooe
            insertPage(notebookFlipper, recentPage - 1, currentIndex - 1);
            notebookFlipper.setDisplayedChild(currentIndex + negativeCount);
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_notebook, menu);
        this.setTitle("notebook " + notebookIndex);
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

    /**
     * generates a properly formatted, empty notebook page
     * @return the EditText
     */
    private LinearLayout getEmptyPage(int pageNo){
        LinearLayout newLL = new LinearLayout(getApplicationContext());
        TextView pageNoText = new TextView(newLL.getContext());
        LinedEditText newEditText = new LinedEditText(newLL.getContext()) {
            @Override
            public boolean onTouchEvent(MotionEvent e) {
                //return super.onTouchEvent(event);
                int action = MotionEventCompat.getActionMasked(e);
                switch(action) {
                    case (MotionEvent.ACTION_DOWN) :
                        startX = e.getX();
                        break;
                    case (MotionEvent.ACTION_MOVE) :
                        break;
                    case (MotionEvent.ACTION_UP) : {
                        LinearLayout currLL = (LinearLayout) notebookFlipper.getCurrentView();
                        LinedEditText currPage = (LinedEditText) currLL.findViewById(0);
                        currPage.setText("loadedp" + loadedPages.keySet().toString() +
                                "\nindices" + loadedPages.values().toString()+"\n curr page: " +
                                currentPage+"\ncurrIndex: "+currentIndex+"\nnbflipper info: " +
                                notebookFlipper.getDisplayedChild());

                        float endX = e.getX();
                        if (startX < endX) {//swiped right
                            if (currentPage == 0) {
                                break;
                            }
                            prevPage();
                        } else if (startX > endX) {//swiped left
                            nextPage();
                        } else {
                            super.onTouchEvent(e);
                        }
                        break;
                    }
                    default :
                        return super.onTouchEvent(e);
                }
                return true;
            }
        };

        newLL.setOrientation(LinearLayout.VERTICAL);

        pageNoText.setText("PageNo: " + pageNo);
        pageNoText.setGravity(Gravity.BOTTOM);

        newEditText.setGravity(Gravity.TOP);
        newEditText.setId(0);
        newEditText.setMaxLines(24);

        newLL.addView(newEditText, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, 1));
        newLL.addView(pageNoText, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, 0));
        return  newLL;
    }

    /**
     * loads a pageInto the view from the db, checks if page exists and if it's loaded
     * @param view the view to load into
     * @param pageNo the page to load
     * @param index the index to load the page into the view
     */
    private void insertPage(ViewGroup view, int pageNo, int index){
        //searches the db for the page
        Cursor c = pagesDb.getPage(notebookIndex, pageNo);
        String text = "";
        if(c != null && c.getCount()>0) { //if found in db
            //retrieves the text from the page
             text = c.getString(3);
        } else { //does not exist in database
            //add new page to database
            pagesDb.savePage(notebookIndex, pageNo, false, "");
        }
        if(loadedPages.get(pageNo) == null) { //if not already loaded
            //places it into a new edit text
            LinearLayout page = getEmptyPage(pageNo);
            LinedEditText pageText = (LinedEditText)(page.findViewById(0));
            pageText.setText(text);
            if(index<0){
                negativeCount++;
            }
            //places it into the view, adjusting for the negatives
            view.addView(page, index+negativeCount);
            //places it into loaded pages
            loadedPages.put(pageNo, index);
        }

    }


    /**preconditions:  this and the next page is loaded
     * post conditions: the view is not on the next page. both pages surrounding it are not loaded
     */
    private void nextPage(){
        //update current page
        LinearLayout currLL = (LinearLayout)notebookFlipper.getCurrentView();
        LinedEditText currPage = (LinedEditText)currLL.findViewById(0);
        pagesDb.updatePage(notebookIndex, currentPage, false, currPage.getText().toString());
        //move view to the next page
        notebookFlipper.setInAnimation(this, R.anim.in_from_right);
        notebookFlipper.setOutAnimation(this, R.anim.out_to_left);
        notebookFlipper.setDisplayedChild(currentIndex + 1 + negativeCount);
        currentPage++;
        currentIndex++;
        //load the next page
        insertPage(notebookFlipper, currentPage+1, currentIndex+1);
    }

    private void prevPage(){
        //update current page
        LinearLayout currLL = (LinearLayout)notebookFlipper.getCurrentView();
        LinedEditText currPage = (LinedEditText)currLL.findViewById(0);
        pagesDb.updatePage(notebookIndex, currentPage, false, currPage.getText().toString());
        //move view to previous page
        notebookFlipper.setInAnimation(this, R.anim.in_from_left);
        notebookFlipper.setOutAnimation(this, R.anim.out_to_right);
        notebookFlipper.setDisplayedChild(currentIndex - 1 + negativeCount);
        currentIndex--;
        currentPage--;
        //load previous page
        if(currentPage>0) {
            insertPage(notebookFlipper, currentPage - 1, currentIndex - 1);
        }
    }

    protected void onPause(){
        super.onPause();
        LinearLayout currLL = (LinearLayout)notebookFlipper.getCurrentView();
        LinedEditText currPage = (LinedEditText)currLL.findViewById(0);
        pagesDb.updatePage(notebookIndex, currentPage, false, currPage.getText().toString());
        pagesDb.updateRecent(notebookIndex, currentPage);
    }

    protected void onDestroy(){
        super.onDestroy();
        pagesDb.close();
    }

}
