package com.ihm.wassup;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ihm.wassup.sqlite.Event;
import com.ihm.wassup.sqlite.Favorite;
import com.ihm.wassup.sqlite.Profil;
import com.ihm.wassup.sqlite.WassupDB;

public class MainActivity extends ActionBarActivity {

	private WassupDB wassupBDD;
	private TextView content;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		content = (TextView) findViewById(R.id.liste);
		
		
		/**
		 * Mise en place de la base de données et remplissage si elle est vide (en utilisant les fichiers
		 * de données (assets/evenement.txt et assets/profil.txt)
		 */
		wassupBDD = new WassupDB(this);
		wassupBDD.open();
		
		/**
		 * TESTS d'affichage et d'utilisation de requetes
		 */
	    ArrayList<HashMap<String,String>> profilsList = wassupBDD.getSQLiteBase().getProfilsList();
	    ArrayList<HashMap<String,String>> evenementsList = wassupBDD.getSQLiteBase().getEventsList();
	    ArrayList<HashMap<String,String>> datesList = wassupBDD.getSQLiteBase().getDatesList();
		
	    String content1="PROFILS";
	    for(int p=0;p<profilsList.size();p++)
	    	content1 += "\n"+profilsList.get(p).get(wassupBDD.getSQLiteBase().USERNAME)+" "+profilsList.get(p).get(wassupBDD.getSQLiteBase().PASSWORD)+" "+profilsList.get(p).get(wassupBDD.getSQLiteBase().EMAIL);	   	  
	    content1+="\n\nEVENEMENTS";
	    for(int p=0;p<evenementsList.size();p++)
	    	content1 += "\n"+evenementsList.get(p).get(wassupBDD.getSQLiteBase().CATEGORY)+" "+evenementsList.get(p).get(wassupBDD.getSQLiteBase().TYPE1)+" "+evenementsList.get(p).get(wassupBDD.getSQLiteBase().TITLE)+" "+evenementsList.get(p).get(wassupBDD.getSQLiteBase().DESCRIPTION)+" "+evenementsList.get(p).get(wassupBDD.getSQLiteBase().PRICE);
	    content1+="\n\nDATES";
	    if(datesList.size()>0){
	    	for(int p=0;p<datesList.size();p++)
	    		content1 += "\n"+datesList.get(p).get(wassupBDD.getSQLiteBase().IDEVENT)+" "+datesList.get(p).get(wassupBDD.getSQLiteBase().DATE);
	    }
	    
	    content1+="\n\nTESTS DE REQUETES PARTICULIERES";
	    
	    
	    //afficher un événement grâce à son titre
	    Map<String,String> couples = new HashMap<String,String>();
	    couples.put(wassupBDD.getSQLiteBase().TITLE,"'David Guetta'");
		Event evenement = wassupBDD.getSQLiteBase().getEvent(couples);
		if(evenement!=null)
			content1+="\n\n"+evenement.toString();

		
		//afficher un profil particulier
		couples.clear();
		couples.put(wassupBDD.getSQLiteBase().USERNAME,"'Alice'");
		Profil profil = wassupBDD.getSQLiteBase().getProfil(couples);
		if(profil!=null)
			content1+="\n\n"+profil.toString();
	    
		//afficher les dates de représentation d'un événement grâce à son titre
		String titre = "marathon";
		int idEvenement = 1;
		content1+="\n\nDates de l'événement "+titre;
		String sql = "select d."+wassupBDD.getSQLiteBase().DATE+" from "+wassupBDD.getSQLiteBase().TABLE_EVENT+" e inner join "+wassupBDD.getSQLiteBase().TABLE_DATE+" d on e."+wassupBDD.getSQLiteBase().COL_ID+"=d."+wassupBDD.getSQLiteBase().IDEVENT+" where e."+wassupBDD.getSQLiteBase().TITLE+" = '"+titre+"'";
		Cursor c = wassupBDD.getSQLiteBase().getDb().rawQuery(sql, null);
		if(c.moveToFirst()){
			do{
				content1+="\n"+c.getString(0);
			} while (c.moveToNext());
		}
		
		//Alice ajoute l'événement de David Guetta dans ses favoris
		content1+="\nAlice ajoute l'événement "+titre+" dans ses favoris";
		int idProfil = 1;
		Favorite favoris = new Favorite(idEvenement, idProfil);
		wassupBDD.getSQLiteBase().insertFavoris(favoris);
		
		//Affichage des événements favoris d'Alice
		content1+="\n\nAffichage des favoris d'Alice";
		sql = "select distinct e."+wassupBDD.getSQLiteBase().TITLE+" from "+wassupBDD.getSQLiteBase().TABLE_EVENT+" e inner join "+wassupBDD.getSQLiteBase().TABLE_FAVORITE+" f on e."+wassupBDD.getSQLiteBase().COL_ID+"=f."+wassupBDD.getSQLiteBase().IDEVENT+" inner join "+wassupBDD.getSQLiteBase().TABLE_PROFIL+" p on p."+wassupBDD.getSQLiteBase().COL_ID+"=f."+wassupBDD.getSQLiteBase().IDPROFIL+" where p."+wassupBDD.getSQLiteBase().COL_ID+" = "+idProfil;
		c = wassupBDD.getSQLiteBase().getDb().rawQuery(sql, null);
		if(c.moveToFirst()){
			do{
				content1+="\n"+c.getString(0);
			} while (c.moveToNext());
		}
		
		
	    //affichage des résultats des requetes dans l'appli
	    content.setText(content1);

	    
	    
	    /**
	     * FERMETURE DE LA BASE
	     */
	    wassupBDD.close();
		
		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
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
