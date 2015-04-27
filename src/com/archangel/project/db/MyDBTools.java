package com.archangel.project.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDBTools {
	
	private DatabaseHelper myDBHelper;
	private SQLiteDatabase myDB;
	private static MyDBTools dbTools = null;
	
	private static String myDBName = "database.db";
	private static int myDBVersion = 1;
	
	private static String TableNames[];
	private static String FieldNames[][];
	private static String FieldTypes[][];
	
	private final Context myContext;    
	
	private static class DatabaseHelper extends SQLiteOpenHelper {
		public DatabaseHelper(Context context) {
			super(context, myDBName, null, myDBVersion);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			if (TableNames == null) {
				return;
			}
			for (int i = 0; i < TableNames.length; i++) {
				String query = "CREATE TABLE " + TableNames[i] + " (";
				for (int j = 0; j < FieldNames[i].length; j++) {
					query += FieldNames[i][j] + " " + FieldTypes[i][j] + ",";
				}
				query = query.substring(0, query.length() - 1);
				query += ")";
				db.execSQL(query);
			}
			
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			for (int i = 0; i < TableNames[i].length(); i++){
	  			String query = "DROP TABLE IF EXISTS " + TableNames[i];
	  			db.execSQL(query);
	  	    }
	  		onCreate(db);
		}
	}
	
	
	private MyDBTools(Context context) {
		this.myContext = context;
	}
	
	public static MyDBTools getInstance(Context context) {
		if (dbTools == null) {
			dbTools = new MyDBTools(context);
			TableNames = MyDBInfo.getTableNames();
			FieldNames = MyDBInfo.getFieldNames();
			FieldTypes = MyDBInfo.getFieldTypes();
		}
		return dbTools;
	}
	
	public MyDBTools open() {
		myDBHelper = new DatabaseHelper(myContext);
		myDB = myDBHelper.getWritableDatabase();
		return this;
	}
	
	public void close() {
		myDBHelper.close();
	}
	
	public void execSQL(String query) throws SQLException {
		myDB.execSQL(query);
	}
	
	public Cursor rawQuery(String query,String[] selectionArgs) {
		return myDB.rawQuery(query, selectionArgs);
	}
	
	public Cursor select(String table, String[] columns, String selection, 
			String[] selectionArgs, String groupBy, 
			String having, String orderBy) {
		return myDB.query(table, columns, selection, selectionArgs, groupBy, having, orderBy);
	}
	
	public long insert(String table, String fields[], String values[]) {
		ContentValues cv = new ContentValues();
		for (int i = 0; i < fields.length; i++) {
			cv.put(fields[i], values[i]);
		}
		return myDB.insert(table, null, cv);
	}
	
	public int delete(String table, String where, String[] whereValue) {
		return myDB.delete(table, where, whereValue);
	}
	
	public int update(String table, String updateFields[],
			String updateValues[], String where, String[] whereValue) {
		ContentValues cv = new ContentValues();
		for (int i = 0; i < updateFields.length; i++) {
			cv.put(updateFields[i], updateValues[i]);
		}
		return myDB.update(table, cv, where, whereValue);
	}


	
}