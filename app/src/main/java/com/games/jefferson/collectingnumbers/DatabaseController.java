package com.games.jefferson.collectingnumbers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 *
 *
 * Created by jeff on 7/28/2015.
 */
public class DatabaseController {

    private DatabaseHelper dbOpenHelper;
    private SQLiteDatabase pagesDb;

    //CREATE TABLE PagesTable (NotebookId int, PageNo int, Bookmark bit, PageText nvarchar(255));
    public final static String PAGES_TABLE_NAME="PagesTable"; // name of table

    //the notebook table and columns
    public final static String NOTEBOOK_TABLE_NAME="NotebookTable"; // name of table
    public final static String NOTEBOOK_TITLE = "NotebookTitle";
    public final static String NOTEBOOK_ID = "NotebookId";
    public final static String NOTEBOOK_IMG = "NotebookImg";
    public final static String RECENT_PAGE = "RecentPage";

    public DatabaseController(Context context){
        dbOpenHelper = new DatabaseHelper(context);
        pagesDb = dbOpenHelper.getWritableDatabase();
    }

    /**
     *
     * @param notebookId
     * @param pageNo
     * @param bookmark
     * @param pageText
     * @return
     */
    public long addPage(int notebookId, int pageNo, boolean bookmark,String pageText){
        return 0;
    }
//----------------------------------line dividing tables-------------------------------------------
    /**
     *  adds a notebooks to the database
     * @param notebookTitle
     * @param notebookId
     * @param notebookImg
     * @param recentPage
     * @return
     */
    public long addNotebook(String notebookTitle, int notebookId, int notebookImg, int recentPage){
        ContentValues values = new ContentValues();
        values.put(NOTEBOOK_TITLE, notebookTitle);
        values.put(NOTEBOOK_ID, notebookId);
        values.put(NOTEBOOK_IMG, notebookImg);
        values.put(RECENT_PAGE, recentPage);
        return pagesDb.insert(NOTEBOOK_TABLE_NAME, null, values);
    }

    /**
     * gets all the notebooks in the databse
     * @return
     */
    public Cursor getNotebooks(){
        //table
        //columns
        //where clause
        //extra args
        //group by
        //having
        //orderby
        //limit
        Cursor mCursor = pagesDb.query(NOTEBOOK_TABLE_NAME, null, null, null, null, null, NOTEBOOK_ID, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }


    public void close(){
        pagesDb.close();
    }


}
