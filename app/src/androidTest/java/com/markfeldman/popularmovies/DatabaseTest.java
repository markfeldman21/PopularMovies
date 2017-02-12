package com.markfeldman.popularmovies;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.markfeldman.popularmovies.database.MovieContract;
import com.markfeldman.popularmovies.database.MovieDatabase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.Field;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

@RunWith(AndroidJUnit4.class)
public class DatabaseTest {
    private final Context mContext = InstrumentationRegistry.getTargetContext();
    private final Class movieDataBase = MovieDatabase.MovieDatabaseHelper.class;

    /**
     * Because we annotate this method with the @Before annotation, this method will be called
     * before every single method with an @Test annotation. We want to start each test clean, so we
     * delete the database to do so.
     */
    @Before
    public void setUp() {

        deleteTheDatabase();
    }


    @Test
    public void create_database_test() throws Exception{
        SQLiteOpenHelper dbHelper = (SQLiteOpenHelper) movieDataBase.getConstructor(Context.class).newInstance(mContext);
        SQLiteDatabase database = dbHelper.getWritableDatabase();

             /* We think the database is open, let's verify that here */
        String databaseIsNotOpen = "The database should be open and isn't";
        assertEquals(databaseIsNotOpen,
                true,
                database.isOpen());
        /* This Cursor will contain the names of each table in our database */
        Cursor tableNameCursor = database.rawQuery(
                "SELECT name FROM sqlite_master WHERE type='table' AND name='" +
                        MovieContract.MovieDataContract.TABLE_NAME + "'",
                null);
          /*
         * If tableNameCursor.moveToFirst returns false from this query, it means the database
         * wasn't created properly. In actuality, it means that your database contains no tables.
         */
        String errorInCreateDatabase = "Database not created properly";
        assertTrue(errorInCreateDatabase,tableNameCursor.moveToFirst());

                /* If this fails, it means that your database doesn't contain the expected table(s) */
        assertEquals("Error: Your database was created without the expected tables.",
                MovieContract.MovieDataContract.TABLE_NAME, tableNameCursor.getString(0));

        /* Always close a cursor when you are done with it */
        tableNameCursor.close();



    }




    void deleteTheDatabase(){
        try {
            /* Use reflection to get the database name from the db helper class */
            Field f = movieDataBase.getDeclaredField("DATABASE_NAME");
            f.setAccessible(true);
            mContext.deleteDatabase((String)f.get(null));
        }catch (NoSuchFieldException ex){
            fail("Make sure you have a member called DATABASE_NAME in the MovieDatabase Class");
        }catch (Exception ex){
            fail(ex.getMessage());
        }

    }

}

