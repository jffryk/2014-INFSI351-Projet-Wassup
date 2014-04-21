package com.ihm.wassup;

import java.io.FileNotFoundException;
import java.io.IOException;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.ihm.wassup.sqlite.WassupDB;

public class Connexion extends ActionBarActivity {
	
	private EditText username;
	private EditText password;
	private WassupDB wassupBDD;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.connexion);
	
		username = (EditText) findViewById(R.id.usernameEdit);
		password = (EditText) findViewById(R.id.passwordEdit);
		
		
	}

	public void connect(View v){
		if(isAllowed()){
			Log.d("WASSUP",this.username.getText().toString()+" is allowed");
			Intent intent = new Intent(Connexion.this, MainActivity.class);
			startActivity(intent);
		}
		else{
			Log.d("WASSUP","Incorrect username/password for WASSUP connexion with username "+this.username.getText().toString());
	      	Toast.makeText(this, "Le mot de passe et/ou le username est incorrect...", Toast.LENGTH_LONG).show();
			
		}
		
	}

	
	public boolean isAllowed(){
		//get database info
		String password = this.password.getText().toString();
		String username = this.username.getText().toString();
		
		//check if correct user and password
		wassupBDD = new WassupDB(this);
		wassupBDD.open();
		fillTables();

		
		String sql = "select "+wassupBDD.getSQLiteBase().USERNAME+","+wassupBDD.getSQLiteBase().PASSWORD+" from "+wassupBDD.getSQLiteBase().TABLE_PROFIL+" where "+wassupBDD.getSQLiteBase().USERNAME+" = '"+username+"' and "+wassupBDD.getSQLiteBase().PASSWORD+" = '"+password+"'";
		Cursor c = wassupBDD.getSQLiteBase().getDb().rawQuery(sql, null);
		if(c.moveToFirst() && c!=null){
			wassupBDD.close();
			return true;
		}
		wassupBDD.close();
		return false;
	}
	
	/**
	 * Remplissage de la base de données si elle est vide (profils et evenements ainsi que leurs dates)
	 */
	private void fillTables() {
		//remplissage de la table des profils
		Cursor c = wassupBDD.getBDD().rawQuery("select * from "+wassupBDD.getSQLiteBase().TABLE_PROFIL, null);
		if(!c.moveToFirst()){
			try {
				wassupBDD.getSQLiteBase().fillTable("profil.txt",1,this);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		//Remplissage de la table des événements
		c = wassupBDD.getBDD().rawQuery("select * from "+wassupBDD.getSQLiteBase().TABLE_EVENT, null);
		if(!c.moveToFirst()){
			try {
				wassupBDD.getSQLiteBase().fillTable("evenement.txt",2,this);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			return rootView;
		}
	}

}
