package com.example.com.infsi351.wassup.sqlite;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class WassupDB {

	private static final int VERSION_DB = 1;
	private static final String NAME_DB = "wassup.db";

	private SQLiteDatabase db;

	private SQLiteBase sqlBase;

	public SQLiteBase getSQLiteBase() {
		return sqlBase;
	}

	public void setSQLiteBase(SQLiteBase maBaseSQLite) {
		this.sqlBase = maBaseSQLite;
	}

	public WassupDB(Context context){
		sqlBase = new SQLiteBase(context, NAME_DB, null, VERSION_DB);
	}

	public void open(){
		db = sqlBase.getWritableDatabase();
	}

	public void close(){
		db.close();
	}

	public SQLiteDatabase getBDD(){
		return db;
	}
}