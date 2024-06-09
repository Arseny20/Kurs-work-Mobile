package com.thatgame.langcards;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class dbConnect extends SQLiteOpenHelper {

    private static String dbName = "usersOfLangCards";
    private static String dbTable = "users";
    private static int dbVersion = 1;
    private static String username = "USERNAME";
    private static String password = "PASSWORD";
    private static String clicked = "CLICKED";

    public dbConnect(@Nullable Context context) {
        super(context, dbName, null, dbVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "create table " + dbTable + "( " + username + " TEXT primary key, " + password + " TEXT)";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + dbTable);
        onCreate(db);
    }

    public void addUser(String usname, String uspassword) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(username, usname);
        values.put(password, uspassword);
        db.insert(dbTable, null, values);
    }

    public Boolean checkUsername(String usname) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + dbTable + " WHERE " + username + " = ?", new String[]{usname});
        if (cursor.getCount() == 0) {
            return true;
        } else {
            return false;
        }
    }

    public Boolean checkUser(String usname, String uspassword) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + dbTable + " WHERE " + username + " = ?" + " AND " + password + "= ?", new String[]{usname, uspassword});
        return cursor.getCount() > 0;
    }

    public void updatePassword(String usname, String uspassword) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + dbTable + " SET " + password + " = " + uspassword + " WHERE " + username + " = " + usname;
        db.execSQL(query);
    }

    public void createNewTableDict(String table) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "create table if not exists " + table + "( word TEXT primary key, translation TEXT )";
        db.execSQL(query);
    }

    public void updateDict(String table, String word, String translation) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("word", word);
        values.put("translation", translation);
        db.insert(table, null, values);
    }

    public ArrayList<String> getWords(String table) {
        String query = "SELECT * FROM " + table;
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        ArrayList<String> list = new ArrayList<>();

        while (cursor.moveToNext()) {
            list.add(cursor.getString(0));
        }

        cursor.close();
        return list;
    }

    public ArrayList<String> getTranslations(String table) {
        String query = "SELECT * FROM " + table;
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        ArrayList<String> list = new ArrayList<>();

        while (cursor.moveToNext()) {
            list.add(cursor.getString(1));
        }

        cursor.close();
        return list;
    }

    public ArrayList<String> getLangs(String login) {
        String query = "SELECT name FROM sqlite_master WHERE type='table'";
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        ArrayList<String> list = new ArrayList<>();

        while (cursor.moveToNext()) {
            String[] log = cursor.getString(0).split("_");
            if (((log.length > 1) &&(log[1].equals(login)))) {
                list.add(log[0]);
           }
        }

        cursor.close();
        return list;
    }

    public void cleanUnregDict() {
        String query = "SELECT name FROM sqlite_master WHERE type='table'";
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        while (cursor.moveToNext()) {
            String[] log = cursor.getString(0).split("_");
            if ((log.length > 1) && (log[1].equals("unreg"))) {
                db.execSQL("DROP TABLE " + cursor.getString(0));
            }
        }
        createNewTableDict("Английский_unreg");
        cursor.close();
    }

    public void deleteWord(String table, String word) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "delete from " + table + " where word = '" + word + "'";
        db.execSQL(query);
    }
}
