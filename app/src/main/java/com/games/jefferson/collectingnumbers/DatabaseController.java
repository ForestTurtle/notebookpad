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
    private SQLiteDatabase writableDb;

    //the pages table and columns
    public final static String PAGES_TABLE_NAME="PagesTable"; // name of table
    public final static String PAGES_ID = "NotebookId";
    public final static String PAGE_NUMBER = "PageNo";
    public final static String PAGE_BOOKMARK = "Bookmark";
    public final static String PAGE_TEXT = "PageText";

    //the notebook table and columns
    public final static String NOTEBOOK_TABLE_NAME="NotebookTable"; // name of table
    public final static String NOTEBOOK_TITLE = "NotebookTitle";
    public final static String NOTEBOOK_ID = "NotebookId";
    public final static String NOTEBOOK_IMG = "NotebookImg";
    public final static String RECENT_PAGE = "RecentPage";

    public DatabaseController(Context context){
        dbOpenHelper = new DatabaseHelper(context);
        writableDb = dbOpenHelper.getWritableDatabase();
    }

    /**
     *
     * @param notebookId
     * @param pageNo
     * @param bookmark
     * @param pageText
     * @return
     */
    public long savePage(int notebookId, int pageNo, boolean bookmark, String pageText){
        ContentValues values = new ContentValues();
        values.put(PAGES_ID, notebookId);
        values.put(PAGE_NUMBER, pageNo);
        values.put(PAGE_BOOKMARK, bookmark);
        values.put(PAGE_TEXT, pageText);
        return writableDb.insert(PAGES_TABLE_NAME, null, values);
    }

    public int updatePage(int notebookId, int pageNo, boolean bookmark, String pageText){
        ContentValues values = new ContentValues();
        values.put(PAGES_ID, notebookId);
        values.put(PAGE_NUMBER, pageNo);
        values.put(PAGE_BOOKMARK, bookmark);
        values.put(PAGE_TEXT, pageText);
        return writableDb.update(PAGES_TABLE_NAME, values, PAGES_ID+" = "+notebookId+
                " AND "+PAGE_NUMBER+" = "+pageNo, null);
    }

    public Cursor getPage(int notebookId, int pageNo){
        //table
        //columns
        //where clause
        //extra args
        //group by
        //having
        //orderby
        //limit
        Cursor mCursor = writableDb.query(PAGES_TABLE_NAME, null, PAGES_ID+" = "+notebookId+
                " AND "+PAGE_NUMBER+" = "+pageNo, null, null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
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
        return writableDb.insert(NOTEBOOK_TABLE_NAME, null, values);
    }

    public int updateRecent(int notebookId, int recentPage){
        ContentValues values = new ContentValues();
        values.put(RECENT_PAGE, recentPage);
        return writableDb.update(NOTEBOOK_TABLE_NAME, values, NOTEBOOK_ID + " = " + notebookId, null);
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
        Cursor mCursor = writableDb.query(NOTEBOOK_TABLE_NAME, null, null, null, null, null, NOTEBOOK_ID, null);
//        if (mCursor != null) {
//            mCursor.moveToFirst();
//        }
        return mCursor;
    }

    /**
     * deletes everything
     */
    public void reset(){
        writableDb.delete(NOTEBOOK_TABLE_NAME,null,null);
        writableDb.delete(PAGES_TABLE_NAME,null,null);
    }

    public void close(){
        writableDb.close();
    }


}
