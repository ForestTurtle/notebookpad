package com.games.jefferson.collectingnumbers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by jeff on 7/26/2015.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String NOTEBOOK_DB = "NotebookDatabase";
    private static final int DB_VERSION = 2;
    private static final String CREATE_PAGES_TABLE = "CREATE TABLE PagesTable (NotebookId int, " +
            "PageNo int, Bookmark bit, PageText nvarchar(255)); ";
    private static final String CREATE_NOTEBOOK_TABLE = "CREATE TABLE NotebookTable " +
            "(NotebookTitle nvarchar(10), NotebookId int, NotebookImg int, RecentPage int); ";

    // SQLiteDatabase.CursorFactory factory,
    public DatabaseHelper(Context context) {
        super(context, NOTEBOOK_DB, null, DB_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //create table
        db.execSQL(CREATE_PAGES_TABLE);
        db.execSQL(CREATE_NOTEBOOK_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        /*
        Log.w(MyDatabaseHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        database.execSQL("DROP TABLE IF EXISTS MyEmployees");
        onCreate(database);
        */
        //do not upgrade
    }
}
