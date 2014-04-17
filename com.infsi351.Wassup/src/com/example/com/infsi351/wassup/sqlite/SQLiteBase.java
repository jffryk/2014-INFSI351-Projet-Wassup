package com.example.com.infsi351.wassup.sqlite;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
 
public class SQLiteBase extends SQLiteOpenHelper {
 
	public static final String TABLE_PROFIL = "profils";
	public static final String TABLE_EVENT = "events";
	public static final String TABLE_FAVORITE = "favorites";
	public static final String TABLE_DATE = "dates";

	public static final String COL_ID = "id";
	public static final String USERNAME = "username";
	public static final String PASSWORD = "password";
	public static final String EMAIL = "email";

	public static final String CATEGORY = "category";
	public static final String TYPE1 = "type1";
	public static final String TYPE2 = "type2";
	public static final String TYPE3 = "type3";
	public static final String TITLE = "title";
	public static final String DESCRIPTION = "description";
	public static final String PRICE = "price";
	public static final String LOCATION = "location";
	
	public static final String IDEVENT = "id_event";
	public static final String IDPROFIL = "id_profil";
	
	public static final String DATE = "date";

	
	private static final String TABLE_PROFILS = "CREATE TABLE " + TABLE_PROFIL + " ("
			+ COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + USERNAME + " TEXT NOT NULL, "
			+ PASSWORD + " TEXT NOT NULL,"
			+ EMAIL + " TEXT NOT NULL);";
	private static final String TABLE_EVENTS = "CREATE TABLE " + TABLE_EVENT + " ("
			+ COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ CATEGORY + " TEXT NOT NULL, "
			+ TYPE1 + " TEXT NOT NULL,"
			+ TYPE2 + " TEXT NOT NULL,"
			+ TYPE3 + " TEXT NOT NULL,"
			+ LOCATION + " TEXT NOT NULL,"
			+ TITLE + " TEXT NOT NULL," + DESCRIPTION + " TEXT NOT NULL, " + PRICE + " TEXT NOT NULL );";
	private static final String TABLE_FAVORITES = "CREATE TABLE " + TABLE_FAVORITE + " ("
			+ COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ IDPROFIL + " INTEGER,"
			+ IDEVENT + " INTEGER," +
			"FOREIGN KEY ("+ IDPROFIL +") REFERENCES "+TABLE_PROFIL+" ("+ COL_ID + "), FOREIGN KEY ("+ IDEVENT +") REFERENCES "+TABLE_EVENT+" ("+ COL_ID + "));";

	private static final String TABLE_DATES = "CREATE TABLE " + TABLE_DATE + " ("
			+ COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ IDEVENT + " INTEGER,"
			+ DATE + " TEXT NOT NULL,"+
			"FOREIGN KEY ("+ IDEVENT +") REFERENCES "+TABLE_EVENT+" ("+ COL_ID + "));";
 
	private SQLiteDatabase db = this.getWritableDatabase();
	
	public SQLiteDatabase getDb() {
		return db;
	}

	public void setDb(SQLiteDatabase db) {
		this.db = db;
	}

	public SQLiteBase(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		//on créé la table à partir de la requête écrite dans la variable CREATE_BDD
		db.execSQL(TABLE_PROFILS);
		db.execSQL(TABLE_EVENTS);
		db.execSQL(TABLE_FAVORITES);
		db.execSQL(TABLE_DATES);
	}
 
	public void fillTable(String filename,int type,Context context) throws IOException {
		AssetManager am = context.getAssets();
		InputStream is = am.open(filename);
		InputStreamReader inputreader = new InputStreamReader(is); 
        BufferedReader fr = new BufferedReader(inputreader);
		String currentLine;
		try {
			while ((currentLine = fr.readLine()) != null) {
				String[] values = currentLine.split(",");
				switch(type){
				case 1:
					if(values.length==3)
						insertProfil(new Profil(values[0],values[1],values[2]));
					break;
				case 2:
					int threeshold = 8;
					//Log.d("ME","Read event "+values[0]+" "+values[1]+" "+values[2]+" "+values[3]+" "+values[4]+" "+values[5]);
					if(values.length>=threeshold)
						insertEvenement(new Event(values[0],values[1],values[2],values[3],values[4],values[5],values[6],values[7]));
					if(values.length>threeshold){
						//get last inserted id in Evenement table
						int lastEventId = -1;
						String query = "SELECT "+COL_ID+" from "+TABLE_EVENT+" order by "+COL_ID+" DESC limit 1";
						Cursor c = db.rawQuery(query,null);
						if (c != null && c.moveToFirst())
							lastEventId = c.getInt(0);
						if(lastEventId!=-1)
							for(int i=threeshold;i<values.length;i++)
								insertDates(new Dates(lastEventId,values[i]));
					}
					break;
				case 3:
					if(values.length==2)
						insertDates(new Dates(Integer.valueOf(values[0]),values[1]));
					break;
				case 4:
					if(values.length==2)
						insertFavoris(new Favorite(Integer.valueOf(values[0]),Integer.valueOf(values[1])));
					break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (fr != null)fr.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		//db.execSQL("DROP TABLE " + TABLE_FAVORITE + ";");
		db.execSQL("DROP TABLE " + TABLE_PROFIL + ";");
		//db.execSQL("DROP TABLE " + TABLE_EVENT + ";");
		db.execSQL("DROP TABLE " + TABLE_DATE + ";");
		db.execSQL("DROP TABLE evenements;");
		db.execSQL("DROP TABLE favoris;");
		onCreate(db);
	}
	
		
	//operations on tables
	public long insertProfil(Profil profil){
		ContentValues values = new ContentValues();
		values.put(USERNAME, profil.getUsername());
		values.put(PASSWORD, profil.getPassword());
		values.put(EMAIL, profil.getEmail());
		return db.insert(TABLE_PROFIL, null, values);
	}
	
	public long insertEvenement(Event evenement){
		ContentValues values = new ContentValues();
		values.put(TITLE, evenement.getTitle());
		values.put(CATEGORY, evenement.getCategory());
		values.put(TYPE1, evenement.getType1());
		values.put(TYPE2, evenement.getType2());
		values.put(TYPE3, evenement.getType3());
		values.put(DESCRIPTION, evenement.getDescription());
		values.put(PRICE, evenement.getPrice());
		values.put(LOCATION, evenement.getLocation());
		return db.insert(TABLE_EVENT, null, values);
	}
	
	public long insertFavoris(Favorite favorite){
		ContentValues values = new ContentValues();
		values.put(IDEVENT, favorite.getIdEvent());
		values.put(IDPROFIL, favorite.getIdProfil());
		return db.insert(TABLE_FAVORITE, null, values);
	}
	
	public long insertDates(Dates date){
		ContentValues values = new ContentValues();
		values.put(IDEVENT, date.getIdEvent());
		values.put(DATE, date.getDate());
		return db.insert(TABLE_DATE, null, values);
	}

	public int updateProfil(int id, Profil profil){
		ContentValues values = new ContentValues();
		values.put(USERNAME, profil.getUsername());
		values.put(USERNAME, profil.getPassword());
		values.put(EMAIL, profil.getEmail());
		return db.update(TABLE_PROFIL, values, COL_ID + " = " +id, null);
	}
	
	public int updateEvent(int id, Event event){
		ContentValues values = new ContentValues();
		values.put(TITLE, event.getTitle());
		values.put(CATEGORY, event.getCategory());
		values.put(TYPE1, event.getType1());
		values.put(TYPE2, event.getType2());
		values.put(TYPE3, event.getType3());
		values.put(DESCRIPTION, event.getDescription());
		values.put(PRICE, event.getPrice());
		values.put(LOCATION, event.getLocation());
		return db.update(TABLE_EVENT, values, COL_ID + " = " +id, null);
	}
	
	public int updateFavorite(int id, Favorite favorite){
		ContentValues values = new ContentValues();
		values.put(IDEVENT, favorite.getIdEvent());
		values.put(IDPROFIL, favorite.getIdProfil());
		return db.update(TABLE_FAVORITE, values, COL_ID + " = " +id, null);
	}
	
	public int updateDate(int id, Dates date){
		ContentValues values = new ContentValues();
		values.put(IDEVENT, date.getIdEvent());
		values.put(DATE, date.getDate());
		return db.update(TABLE_DATE, values, COL_ID + " = " +id, null);
	}

	public int removeProfilWithID(int id){
		return db.delete(TABLE_PROFIL, COL_ID + " = " +id, null);
	}
	public int removeEventWithID(int id){
		return db.delete(TABLE_EVENT, COL_ID + " = " +id, null);
	}
	public int removeFavoriteWithID(int id){
		return db.delete(TABLE_FAVORITE, COL_ID + " = " +id, null);
	}
	public int removeDateWithID(int id){
		return db.delete(TABLE_DATE, COL_ID + " = " +id, null);
	}

	public Profil getProfil(Map<String,String> couples){
		String aggregation = makeAggregation(couples);
		Cursor c = db.query(TABLE_PROFIL, new String[] {COL_ID, USERNAME, PASSWORD, EMAIL}, aggregation, null, null, null, null);
		return cursorToProfil(c);
	}

	//convertir Cursor en Profil
	private Profil cursorToProfil(Cursor c){
		if (c.getCount() == 0)
			return null;
		c.moveToFirst();
		Profil profil = new Profil();
		profil.setId(c.getInt(0));
		profil.setUsername(c.getString(1));
		profil.setPassword(c.getString(2));
		profil.setEmail(c.getString(3));
		c.close();
		return profil;
	}
	
	public Event getEvent(Map<String,String> couples){
		String aggregation = makeAggregation(couples);
		Cursor c = db.query(TABLE_EVENT, new String[] {COL_ID, CATEGORY, TYPE1, TYPE2, TYPE3, TITLE, DESCRIPTION, PRICE, LOCATION}, aggregation, null, null, null, null);
		return cursorToEvent(c);
	}
	
	//convertir Cursor en Evenement
	private Event cursorToEvent(Cursor c){
		if (c.getCount() == 0)
			return null;
		c.moveToFirst();
		Event event = new Event();
		event.setId(c.getInt(0));
		event.setCategory(c.getString(1));
		event.setType1(c.getString(2));
		event.setType2(c.getString(3));
		event.setType3(c.getString(4));
		event.setTitle(c.getString(5));
		event.setDescription(c.getString(6));
		event.setPrice(c.getString(7));
		event.setLocation(c.getString(8));
		c.close();
		return event;
	}
	
	public Favorite getFavorite(Map<String,String> couples){
		String aggregation = makeAggregation(couples);
		Cursor c = db.query(TABLE_FAVORITE, new String[] {COL_ID, IDEVENT, IDPROFIL}, aggregation, null, null, null, null);
		return cursorToFavorite(c);
	}
	
	//convertir Cursor en Favoris
	private Favorite cursorToFavorite(Cursor c){
		if (c.getCount() == 0)
			return null;
		c.moveToFirst();
		Favorite favorite = new Favorite();
		favorite.setId(c.getInt(0));
		favorite.setIdEvent(c.getInt(1));
		favorite.setIdProfil(c.getInt(2));
		c.close();
		return favorite;
	}
	
	public Dates getDate(Map<String,String> couples) throws ParseException{
		String aggregation = makeAggregation(couples);
		Cursor c = db.query(TABLE_DATE, new String[] {COL_ID, IDEVENT, DATE}, aggregation, null, null, null, null);
		return cursorToDate(c);
	}
	
	//convertir Cursor en Profil
	private Dates cursorToDate(Cursor c){
		if (c.getCount() == 0)
			return null;
		c.moveToFirst();		
		Dates date = new Dates();
		date.setId(c.getInt(0));
		date.setIdEvent(c.getInt(1));
		date.setDate(c.getString(2));
		c.close();
		return date;
	}
 
	//make aggregation conditions on the request
	private String makeAggregation(Map<String, String> couples) {
		String aggregation = null;
		if(couples != null){
			int j=0;
			Set<String> keys = couples.keySet();
			Iterator<String> it = keys.iterator();
			while (it.hasNext()){
				String key = it.next();
				if(j==0)
					aggregation = key + " = " + couples.get(key);
				else
					aggregation +=","+key + " = " + couples.get(key);
				j++;
			}
		}
		return aggregation;
	}
	
	public ArrayList<HashMap<String, String>> getProfilsList(){
		Cursor cursor = db.rawQuery("select * from "+TABLE_PROFIL, null);
	    ArrayList<HashMap<String, String>> profilsList = new ArrayList<HashMap<String, String>>();
	    if (cursor.moveToFirst()) {
	        do {
	            HashMap<String, String> map = new HashMap<String, String>();
	            map.put(COL_ID,cursor.getInt(0)+"");
	            map.put(USERNAME,cursor.getString(1));
	            map.put(PASSWORD,cursor.getString(2));
	            map.put(EMAIL,cursor.getString(3));
	            profilsList.add(map);
	        } while (cursor.moveToNext());
	    }
	    return profilsList;
	}
	public ArrayList<HashMap<String, String>> getEventsList(){
		Cursor cursor = db.rawQuery("select * from "+TABLE_EVENT, null);
	    ArrayList<HashMap<String, String>> eventsList = new ArrayList<HashMap<String, String>>();
	    if (cursor.moveToFirst()) {
	        do {
	            HashMap<String, String> map = new HashMap<String, String>();
	            map.put(COL_ID,cursor.getInt(0)+"");
	            map.put(CATEGORY,cursor.getString(1));
	            map.put(TYPE1,cursor.getString(2));
	            map.put(TYPE2,cursor.getString(3));
	            map.put(TYPE3,cursor.getString(4));
	            map.put(TITLE,cursor.getString(5));
	            map.put(DESCRIPTION,cursor.getString(6));
	            map.put(PRICE,cursor.getString(7));
	            map.put(LOCATION,cursor.getString(8));
	            eventsList.add(map);
	        } while (cursor.moveToNext());
	    }
	    return eventsList;
	}
	public ArrayList<HashMap<String, String>> getDatesList(){
		Cursor cursor = db.rawQuery("select * from "+TABLE_DATE, null);
	    ArrayList<HashMap<String, String>> datesList = new ArrayList<HashMap<String, String>>();
	    if (cursor.moveToFirst()) {
	        do {
	            HashMap<String, String> map = new HashMap<String, String>();
	            map.put(COL_ID,cursor.getInt(0)+"");
	            map.put(IDEVENT,cursor.getString(1));
	            map.put(DATE,cursor.getString(2));
	            datesList.add(map);
	        } while (cursor.moveToNext());
	    }
	    return datesList;
	}
}