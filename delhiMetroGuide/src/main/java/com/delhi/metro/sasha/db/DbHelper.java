package com.delhi.metro.sasha.db;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.delhi.metro.sasha.R;
import com.delhi.metro.sasha.db.DbConstants.ColorCode;
import com.delhi.metro.sasha.db.DbConstants.Fare;
import com.delhi.metro.sasha.db.DbConstants.FareAttributes;
import com.delhi.metro.sasha.db.DbConstants.First_Last;
import com.delhi.metro.sasha.db.DbConstants.Line;
import com.delhi.metro.sasha.db.DbConstants.Stop_Sequence;
import com.delhi.metro.sasha.db.DbConstants.Stops;
import com.delhi.metro.sasha.route.Edge;
import com.delhi.metro.sasha.route.Vertex;
import com.delhi.metro.sasha.utils.LogUtils;
import com.delhi.metro.sasha.utils.Utils;


public class DbHelper extends SQLiteOpenHelper{
	private static String TAG = DbHelper.class.getSimpleName();
	private static final String SQL_CREATE_FAREATTRIBUTES_TABLE=
			"CREATE TABLE fareattributes (fare_id INTEGER NOT NULL,fare_value INTEGER NOT NULL,PRIMARY KEY(fare_id))";

    private static final String SQL_CREATE_FARE_TABLE=
			"CREATE TABLE fare (id INTEGER PRIMARY KEY AUTOINCREMENT,stop_from	TEXT NOT NULL,stop_to	TEXT NOT NULL,fare_id	INTEGER NOT NULL,FOREIGN KEY(fare_id) REFERENCES fareattributes ( fare_id ) ON DELETE RESTRICT DEFERRABLE INITIALLY DEFERRED)";

	private static final String SQL_CREATE_COLOR_TABLE =
			"CREATE TABLE color(color_id TEXT  NOT NULL,color TEXT NOT NULL,PRIMARY KEY (color_id));";
	
	private static final String SQL_CREATE_STOP_TABLE  =
			"CREATE TABLE stops(stop_name TEXT  NOT NULL,color_id TEXT NOT NULL,line_name TEXT NOT NULL,lat REAL, lon REAL,PRIMARY KEY (stop_name),FOREIGN KEY(color_id) REFERENCES color(color_id) ON DELETE RESTRICT DEFERRABLE INITIALLY DEFERRED,FOREIGN KEY(line_name) REFERENCES line(line_name) ON DELETE RESTRICT DEFERRABLE INITIALLY DEFERRED);";

	private static final String SQL_CREATE_STOP_SEQUENCE_TABLE  =
			"CREATE TABLE  stop_sequence (id INTEGER PRIMARY KEY AUTOINCREMENT,source TEXT NOT NULL,destination	TEXT NOT NULL,FOREIGN KEY(source) REFERENCES stops(stop_name) ON DELETE RESTRICT DEFERRABLE INITIALLY DEFERRED,FOREIGN KEY(destination) REFERENCES stops(stop_name) ON DELETE RESTRICT DEFERRABLE INITIALLY DEFERRED)";

	private static final String SQL_CREATE_LINE_TABLE=
			"CREATE TABLE line(id INTEGER ,line_name  TEXT PRIMARY KEY NOT NULL)";
	
	private static final String SQL__CREATE_FIRST_LAST_TRAIN_TABLE=
			"CREATE TABLE first_last_train(id INTEGER PRIMARY KEY AUTOINCREMENT,source TEXT NOT NULL,destination TEXT NOT NULL,first TEXT NOT NULL,last TEXT NOT NULL,FOREIGN KEY(source) REFERENCES stops(stop_name) ON DELETE RESTRICT DEFERRABLE INITIALLY DEFERRED,FOREIGN KEY(destination) REFERENCES stops(stop_name) ON DELETE RESTRICT DEFERRABLE INITIALLY DEFERRED)";
	
   private static final String ENABLE_FOREIGN_KEY="PRAGMA FOREIGN_KEYS=\"ON\"";

	private  HashMap<Integer,String> map = new HashMap<Integer,String>();
	private int STOP_SIZE = 149;
	private int fare[][]= new int[STOP_SIZE][];
	private Context context;

	public DbHelper(Context context) {
		super(context,"metro.db",null,1);
		this.context = context;
	}
	

    
	@Override
	public void onConfigure(SQLiteDatabase db) {
		super.onConfigure(db);
		LogUtils.LOGD(TAG,"onConfigure called");
	  try {
	  db.execSQL(ENABLE_FOREIGN_KEY);
	  }catch (Exception e) {
		e.printStackTrace();
		LogUtils.LOGD(TAG,"Exception in onconfigure");
	}
	  
	}
	
	
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		//Create Tables Start
		LogUtils.LOGD(TAG, "onCreate called");
		try {
		db.execSQL(SQL_CREATE_LINE_TABLE);
		db.execSQL(SQL_CREATE_FAREATTRIBUTES_TABLE);
		db.execSQL(SQL_CREATE_FARE_TABLE);
	    db.execSQL(SQL_CREATE_COLOR_TABLE); 
	    db.execSQL(SQL_CREATE_STOP_TABLE); 
	    db.execSQL(SQL_CREATE_STOP_SEQUENCE_TABLE);
	    db.execSQL(SQL__CREATE_FIRST_LAST_TRAIN_TABLE);
	    LogUtils.LOGD(TAG,"oncreate finished successfully");
		}catch (SQLiteException e) {
			e.printStackTrace();
			LogUtils.LOGD(TAG,"Exception in oncreate");
		}
	    // Create Tables End
	    
	}


	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		LogUtils.LOGD(TAG,"onUpgrade called");
		 db.execSQL("DROP TABLE IF EXISTS " + Fare.TABLE_NAME);
		 db.execSQL("DROP TABLE IF EXISTS " + FareAttributes.TABLE_NAME);
		 db.execSQL("DROP TABLE IF EXISTS " + Stops.TABLE_NAME);
		 db.execSQL("DROP TABLE IF EXISTS " + ColorCode.TABLE_NAME);
		 db.execSQL("DROP TABLE IF EXISTS " + Stop_Sequence.TABLE_NAME);
		 db.execSQL("DROP TABLE IF EXISTS " + First_Last.TABLE_NAME);
		 db.execSQL("DROP TABLE IF EXISTS " + Line.TABLE_NAME);
		  
	        onCreate(db);
		
	}

    
	public void load_Data(SQLiteDatabase db) {
		try {
		load_colorattributes(db);	
		read_line(db);
		load_fareattributes(db);
	    read_stopfile(db);//inserting stops information with their color codes	
		read_farefile(db);
		read_stopsequence(db);
		read_first_last_train(db);
		LogUtils.LOGD(TAG,"data submitted successfully into database metro.db");
		}
		catch(SQLiteException e) {
			e.printStackTrace();
			LogUtils.LOGD(TAG,"error in reading data");
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			db.close();
		}
		
		
	}


	private void read_first_last_train(SQLiteDatabase db) {
		try{
			db.execSQL("insert into first_last_train('source','destination','first','last') values('Dilshad Garden','Rithala','05:30','23:00');");
			db.execSQL("insert into first_last_train('source','destination','first','last') values('Rithala','Dilshad Garden','05:45','23:31');");
			db.execSQL("insert into first_last_train('source','destination','first','last') values('Jahangirpuri','Huda City Centre','06:00','23:18');");
			db.execSQL("insert into first_last_train('source','destination','first','last') values('Huda City Centre','Jahangirpuri','05:45','23:30');");
			db.execSQL("insert into first_last_train('source','destination','first','last') values('Vishwavidyalaya','Huda City Centre','05:00','NA');");
			db.execSQL("insert into first_last_train('source','destination','first','last') values('Dwarka','Vaishali','04:42','NA');");
			db.execSQL("insert into first_last_train('source','destination','first','last') values('Dwarka Sec-21','Noida City Centre','06:00','23:30');");
			db.execSQL("insert into first_last_train('source','destination','first','last') values('Vaishali','Dwarka Sec-21','06:00','23:05');");
			db.execSQL("insert into first_last_train('source','destination','first','last') values('Noida City Centre','Dwarka Sec-21','06:00','23:05');");
			db.execSQL("insert into first_last_train('source','destination','first','last') values('Dwarka Sec-21','Vaishali','NA','22:22');");
			db.execSQL("insert into first_last_train('source','destination','first','last') values('Inderlok','Mundka','06:00','23:30');");
			db.execSQL("insert into first_last_train('source','destination','first','last') values('Kirti Nagar','Mundka','06:00','23:30');");
			db.execSQL("insert into first_last_train('source','destination','first','last') values('Mundka','Kirti Nagar','05:10','23:06');");
			db.execSQL("insert into first_last_train('source','destination','first','last') values('Mundka','Inderlok','05:25','23:00');");
			db.execSQL("insert into first_last_train('source','destination','first','last') values('Central Secretariat','Badarpur','06:00','23:30');");
			db.execSQL("insert into first_last_train('source','destination','first','last') values('Badarpur','Central Secretariat','06:00','23:30');");
			
			
		}catch(SQLiteException e){
			e.printStackTrace();
		}
	}



	private void read_line(SQLiteDatabase db) {
      try{
		 db.execSQL("insert into line('id','line_name') values('1','RiToDi');");
		 db.execSQL("insert into line('id','line_name') values('2','D21ToVa');");
		 db.execSQL("insert into line('id','line_name') values('3','D21ToNo');");
		 db.execSQL("insert into line('id','line_name') values('4','MuToSat');");
		 db.execSQL("insert into line('id','line_name') values('5','MuToIn');");
		 db.execSQL("insert into line('id','line_name') values('6','JaToHu');");
		 db.execSQL("insert into line('id','line_name') values('7','BaToJan');");
		 db.execSQL("insert into line('id','line_name') values('8','rM');");
		 db.execSQL("insert into line('id','line_name') values('9','NA');");
		}catch(SQLiteException e){
			e.printStackTrace();
		}
	}



	private void read_stopsequence(SQLiteDatabase db) {
		try {
			/************* RED LINE********************************************/
			db.execSQL("insert into stop_sequence('source','destination') values('Rithala','Rohini West');");
			db.execSQL("insert into stop_sequence('source','destination') values('Rohini West','Rithala');");
			
			db.execSQL("insert into stop_sequence('source','destination') values('Rohini East','Rohini West');");
			db.execSQL("insert into stop_sequence('source','destination') values('Rohini West','Rohini East');");
			
			db.execSQL("insert into stop_sequence('source','destination') values('Rohini East','Pitampura');");
			db.execSQL("insert into stop_sequence('source','destination') values('Pitampura','Rohini East');");
			
			db.execSQL("insert into stop_sequence('source','destination') values('Pitampura','Kohat Enclave');");
			db.execSQL("insert into stop_sequence('source','destination') values('Kohat Enclave','Pitampura');");
			
			db.execSQL("insert into stop_sequence('source','destination') values('Kohat Enclave','Netaji Subash Palace');");
			db.execSQL("insert into stop_sequence('source','destination') values('Netaji Subash Palace','Kohat Enclave');");
			
			db.execSQL("insert into stop_sequence('source','destination') values('Netaji Subash Palace','Keshav Puram');");
			db.execSQL("insert into stop_sequence('source','destination') values('Keshav Puram','Netaji Subash Palace');");
			
			db.execSQL("insert into stop_sequence('source','destination') values('Keshav Puram','Kanhaiya Nagar');");
			db.execSQL("insert into stop_sequence('source','destination') values('Kanhaiya Nagar','Keshav Puram');");
			
			db.execSQL("insert into stop_sequence('source','destination') values('Kanhaiya Nagar','Inderlok');");
			db.execSQL("insert into stop_sequence('source','destination') values('Inderlok','Kanhaiya Nagar');");
			
			db.execSQL("insert into stop_sequence('source','destination') values('Shastri Nagar','Inderlok');");
			db.execSQL("insert into stop_sequence('source','destination') values('Inderlok','Shastri Nagar');");
			
			db.execSQL("insert into stop_sequence('source','destination') values('Shastri Nagar','Pratap Nagar');");
			db.execSQL("insert into stop_sequence('source','destination') values('Pratap Nagar','Shastri Nagar');");
			
			db.execSQL("insert into stop_sequence('source','destination') values('Pulbangash','Pratap Nagar');");
			db.execSQL("insert into stop_sequence('source','destination') values('Pratap Nagar','Pulbangash');");
			
			db.execSQL("insert into stop_sequence('source','destination') values('Pulbangash','Tis Hazari');");
			db.execSQL("insert into stop_sequence('source','destination') values('Tis Hazari','Pulbangash');");
			
			db.execSQL("insert into stop_sequence('source','destination') values('Kashmere Gate','Tis Hazari');");
			db.execSQL("insert into stop_sequence('source','destination') values('Tis Hazari','Kashmere Gate');");
			
			db.execSQL("insert into stop_sequence('source','destination') values('Kashmere Gate','Shastri Park');");
			db.execSQL("insert into stop_sequence('source','destination') values('Shastri Park','Kashmere Gate');");
			
			db.execSQL("insert into stop_sequence('source','destination') values('Seelampur','Shastri Park');");
			db.execSQL("insert into stop_sequence('source','destination') values('Shastri Park','Seelampur');");
			
			db.execSQL("insert into stop_sequence('source','destination') values('Seelampur','Welcome');");
			db.execSQL("insert into stop_sequence('source','destination') values('Welcome','Seelampur');");
			
			db.execSQL("insert into stop_sequence('source','destination') values('Shahdara','Welcome');");
			db.execSQL("insert into stop_sequence('source','destination') values('Welcome','Shahdara');");
			
			db.execSQL("insert into stop_sequence('source','destination') values('Shahdara','Mansarovar Park');");
			db.execSQL("insert into stop_sequence('source','destination') values('Mansarovar Park','Shahdara');");
			
			db.execSQL("insert into stop_sequence('source','destination') values('Jhilmil','Mansarovar Park');");
			db.execSQL("insert into stop_sequence('source','destination') values('Mansarovar Park','Jhilmil');");
			
			db.execSQL("insert into stop_sequence('source','destination') values('Jhilmil','Dilshad Garden');");
			db.execSQL("insert into stop_sequence('source','destination') values('Dilshad Garden','Jhilmil');");
			
			/******************************** RED LINE **********************************/

			
			/************************ GREEN LINE****************************/
			db.execSQL("insert into stop_sequence('source','destination') values('Mundka','Rajdhani Park');");
			db.execSQL("insert into stop_sequence('source','destination') values('Rajdhani Park','Mundka');");
			
			db.execSQL("insert into stop_sequence('source','destination') values('Nangloi Rly Station','Rajdhani Park');");
			db.execSQL("insert into stop_sequence('source','destination') values('Rajdhani Park','Nangloi Rly Station');");
			
			db.execSQL("insert into stop_sequence('source','destination') values('Nangloi Rly Station','Nangloi');");
			db.execSQL("insert into stop_sequence('source','destination') values('Nangloi','Nangloi Rly Station');");
			
			db.execSQL("insert into stop_sequence('source','destination') values('Nangloi','Surajmal Stadium');");
			db.execSQL("insert into stop_sequence('source','destination') values('Surajmal Stadium','Nangloi');");
			
			db.execSQL("insert into stop_sequence('source','destination') values('Udyog Nagar','Surajmal Stadium');");
			db.execSQL("insert into stop_sequence('source','destination') values('Surajmal Stadium','Udyog Nagar');");
			

			
			db.execSQL("insert into stop_sequence('source','destination') values('Udyog Nagar','Peeragarhi');");
			db.execSQL("insert into stop_sequence('source','destination') values('Peeragarhi','Udyog Nagar');");
			
			db.execSQL("insert into stop_sequence('source','destination') values('Peeragarhi','Paschim Vihar(West)');");
			db.execSQL("insert into stop_sequence('source','destination') values('Paschim Vihar(West)','Peeragarhi');");
			
			db.execSQL("insert into stop_sequence('source','destination') values('Paschim Vihar(West)','Paschim Vihar(East)');");
			db.execSQL("insert into stop_sequence('source','destination') values('Paschim Vihar(East)','Paschim Vihar(West)');");


			db.execSQL("insert into stop_sequence('source','destination') values('Paschim Vihar(East)','Madipur');");
			db.execSQL("insert into stop_sequence('source','destination') values('Madipur','Paschim Vihar(East)');");
			
			db.execSQL("insert into stop_sequence('source','destination') values('Madipur','Shivaji Park');");
			db.execSQL("insert into stop_sequence('source','destination') values('Shivaji Park','Madipur');");
			
			db.execSQL("insert into stop_sequence('source','destination') values('Shivaji Park','Punjabi Bagh');");
			db.execSQL("insert into stop_sequence('source','destination') values('Punjabi Bagh','Shivaji Park');");

			

			db.execSQL("insert into stop_sequence('source','destination') values('Punjabi Bagh','Ashok Park Main');");
			db.execSQL("insert into stop_sequence('source','destination') values('Ashok Park Main','Punjabi Bagh');");
			
			db.execSQL("insert into stop_sequence('source','destination') values('Ashok Park Main','Satguru Ram Singh Marg');");
			db.execSQL("insert into stop_sequence('source','destination') values('Satguru Ram Singh Marg','Ashok Park Main');");
			
			db.execSQL("insert into stop_sequence('source','destination') values('Ashok Park Main','Inderlok');");
			db.execSQL("insert into stop_sequence('source','destination') values('Inderlok','Ashok Park Main');");



			db.execSQL("insert into stop_sequence('source','destination') values('Satguru Ram Singh Marg','Kirti Nagar');");
			db.execSQL("insert into stop_sequence('source','destination') values('Kirti Nagar','Satguru Ram Singh Marg');");
			
			/************* GREEN LINE********************************************/
			
			/************* YELLOW LINE********************************************/
			db.execSQL("insert into stop_sequence('source','destination') values('Jahangirpuri','Adarsh Nagar');");
			db.execSQL("insert into stop_sequence('source','destination') values('Adarsh Nagar','Jahangirpuri');");
			
			db.execSQL("insert into stop_sequence('source','destination') values('Adarsh Nagar','Azadpur');");
			db.execSQL("insert into stop_sequence('source','destination') values('Azadpur','Adarsh Nagar');");

			

			db.execSQL("insert into stop_sequence('source','destination') values('Azadpur','Model Town');");
			db.execSQL("insert into stop_sequence('source','destination') values('Model Town','Azadpur');");
			
			db.execSQL("insert into stop_sequence('source','destination') values('Model Town','GTB Nagar');");
			db.execSQL("insert into stop_sequence('source','destination') values('GTB Nagar','Model Town');");
			
			db.execSQL("insert into stop_sequence('source','destination') values('GTB Nagar','Vishwavidyalaya');");
			db.execSQL("insert into stop_sequence('source','destination') values('Vishwavidyalaya','GTB Nagar');");

			
			db.execSQL("insert into stop_sequence('source','destination') values('Vishwavidyalaya','Vidhan Sabha');");
			db.execSQL("insert into stop_sequence('source','destination') values('Vidhan Sabha','Vishwavidyalaya');");
			
			
			db.execSQL("insert into stop_sequence('source','destination') values('Vidhan Sabha','Civil Lines');");
			db.execSQL("insert into stop_sequence('source','destination') values('Civil Lines','Vidhan Sabha');");
			
			db.execSQL("insert into stop_sequence('source','destination') values('Civil Lines','Kashmere Gate');");
			db.execSQL("insert into stop_sequence('source','destination') values('Kashmere Gate','Civil Lines');");
			
			db.execSQL("insert into stop_sequence('source','destination') values('Kashmere Gate','Chandni Chowk');");
			db.execSQL("insert into stop_sequence('source','destination') values('Chandni Chowk','Kashmere Gate');");
			
			db.execSQL("insert into stop_sequence('source','destination') values('Chawri Bazar','Chandni Chowk');");
			db.execSQL("insert into stop_sequence('source','destination') values('Chandni Chowk','Chawri Bazar');");
			
			db.execSQL("insert into stop_sequence('source','destination') values('Chawri Bazar','New Delhi');");
			db.execSQL("insert into stop_sequence('source','destination') values('New Delhi','Chawri Bazar');");
			
			db.execSQL("insert into stop_sequence('source','destination') values('New Delhi','Rajiv Chowk');");
			db.execSQL("insert into stop_sequence('source','destination') values('Rajiv Chowk','New Delhi');");
			
			db.execSQL("insert into stop_sequence('source','destination') values('Rajiv Chowk','Patel Chowk');");
			db.execSQL("insert into stop_sequence('source','destination') values('Patel Chowk','Rajiv Chowk');");
			
			db.execSQL("insert into stop_sequence('source','destination') values('Central Secretariat','Patel Chowk');");
			db.execSQL("insert into stop_sequence('source','destination') values('Patel Chowk','Central Secretariat');");
			
			db.execSQL("insert into stop_sequence('source','destination') values('Udyog Bhawan','Central Secretariat');");
			db.execSQL("insert into stop_sequence('source','destination') values('Central Secretariat','Udyog Bhawan');");
			
			db.execSQL("insert into stop_sequence('source','destination') values('Udyog Bhawan','Race Course');");
			db.execSQL("insert into stop_sequence('source','destination') values('Race Course','Udyog Bhawan');");
			
			db.execSQL("insert into stop_sequence('source','destination') values('Race Course','Jorbagh');");
			db.execSQL("insert into stop_sequence('source','destination') values('Jorbagh','Race Course');");
			
			db.execSQL("insert into stop_sequence('source','destination') values('Jorbagh','INA');");
			db.execSQL("insert into stop_sequence('source','destination') values('INA','Jorbagh');");
			
			db.execSQL("insert into stop_sequence('source','destination') values('INA','AIIMS');");
			db.execSQL("insert into stop_sequence('source','destination') values('AIIMS','INA');");
			
			db.execSQL("insert into stop_sequence('source','destination') values('AIIMS','Green Park');");
			db.execSQL("insert into stop_sequence('source','destination') values('Green Park','AIIMS');");
			
			db.execSQL("insert into stop_sequence('source','destination') values('Green Park','Hauz Khas');");
			db.execSQL("insert into stop_sequence('source','destination') values('Hauz Khas','Green Park');");
			
			db.execSQL("insert into stop_sequence('source','destination') values('Hauz Khas','Malviya Nagar');");
			db.execSQL("insert into stop_sequence('source','destination') values('Malviya Nagar','Hauz Khas');");
			
			db.execSQL("insert into stop_sequence('source','destination') values('Malviya Nagar','Saket');");
			db.execSQL("insert into stop_sequence('source','destination') values('Saket','Malviya Nagar');");
			
			db.execSQL("insert into stop_sequence('source','destination') values('Saket','Qutab Minar');");
			db.execSQL("insert into stop_sequence('source','destination') values('Qutab Minar','Saket');");
			
			db.execSQL("insert into stop_sequence('source','destination') values('Qutab Minar','Chattarpur');");
			db.execSQL("insert into stop_sequence('source','destination') values('Chattarpur','Qutab Minar');");
			
			db.execSQL("insert into stop_sequence('source','destination') values('Chattarpur','Sultanpur');");
			db.execSQL("insert into stop_sequence('source','destination') values('Sultanpur','Chattarpur');");
			
			db.execSQL("insert into stop_sequence('source','destination') values('Sultanpur','Ghitorni');");
			db.execSQL("insert into stop_sequence('source','destination') values('Ghitorni','Sultanpur');");
			
			db.execSQL("insert into stop_sequence('source','destination') values('Ghitorni','Arjangarh');");
			db.execSQL("insert into stop_sequence('source','destination') values('Arjangarh','Ghitorni');");
			
			db.execSQL("insert into stop_sequence('source','destination') values('Arjangarh','Gurudronacharya');");
			db.execSQL("insert into stop_sequence('source','destination') values('Gurudronacharya','Arjangarh');");
			
			db.execSQL("insert into stop_sequence('source','destination') values('Gurudronacharya','Sikandarpur');");
			db.execSQL("insert into stop_sequence('source','destination') values('Sikandarpur','Gurudronacharya');");
			
			db.execSQL("insert into stop_sequence('source','destination') values('Sikandarpur','MG Road');");
			db.execSQL("insert into stop_sequence('source','destination') values('MG Road','Sikandarpur');");
			
			db.execSQL("insert into stop_sequence('source','destination') values('MG Road','IFFCO Chowk');");
			db.execSQL("insert into stop_sequence('source','destination') values('IFFCO Chowk','MG Road');");
			
			db.execSQL("insert into stop_sequence('source','destination') values('Huda City Centre','IFFCO Chowk');");
			db.execSQL("insert into stop_sequence('source','destination') values('IFFCO Chowk','Huda City Centre');");

			/************* YELLOW LINE********************************************/
			
			
			/************* VIOLET LINE********************************************/
			
			db.execSQL("insert into stop_sequence('source','destination') values('ITO','Mandi House');");
			db.execSQL("insert into stop_sequence('source','destination') values('Mandi House','ITO');");

			db.execSQL("insert into stop_sequence('source','destination') values('Central Secretariat','Khan Market');");
			db.execSQL("insert into stop_sequence('source','destination') values('Khan Market','Central Secretariat');");
			
			db.execSQL("insert into stop_sequence('source','destination') values('Khan Market','Jawaharlal Nehru Stadium');");
			db.execSQL("insert into stop_sequence('source','destination') values('Jawaharlal Nehru Stadium','Khan Market');");
			
			db.execSQL("insert into stop_sequence('source','destination') values('Jangpura','Jawaharlal Nehru Stadium');");
			db.execSQL("insert into stop_sequence('source','destination') values('Jawaharlal Nehru Stadium','Jangpura');");
			
			db.execSQL("insert into stop_sequence('source','destination') values('Jangpura','Lajpat Nagar');");
			db.execSQL("insert into stop_sequence('source','destination') values('Lajpat Nagar','Jangpura');");
			
			db.execSQL("insert into stop_sequence('source','destination') values('Lajpat Nagar','Moolchand');");
			db.execSQL("insert into stop_sequence('source','destination') values('Moolchand','Lajpat Nagar');");
			
			db.execSQL("insert into stop_sequence('source','destination') values('Moolchand','Kailash Colony');");
			db.execSQL("insert into stop_sequence('source','destination') values('Kailash Colony','Moolchand');");
			
			db.execSQL("insert into stop_sequence('source','destination') values('Kailash Colony','Nehru Place');");
			db.execSQL("insert into stop_sequence('source','destination') values('Nehru Place','Kailash Colony');");
			
			db.execSQL("insert into stop_sequence('source','destination') values('Nehru Place','Kalkaji Mandir');");
			db.execSQL("insert into stop_sequence('source','destination') values('Kalkaji Mandir','Nehru Place');");
			
			db.execSQL("insert into stop_sequence('source','destination') values('Kalkaji Mandir','Govindpuri');");
			db.execSQL("insert into stop_sequence('source','destination') values('Govindpuri','Kalkaji Mandir');");
			

			db.execSQL("insert into stop_sequence('source','destination') values('Govindpuri','Okhla');");
			db.execSQL("insert into stop_sequence('source','destination') values('Okhla','Govindpuri');");
			
			db.execSQL("insert into stop_sequence('source','destination') values('Okhla','Jasola');");
			db.execSQL("insert into stop_sequence('source','destination') values('Jasola','Okhla');");
			
			db.execSQL("insert into stop_sequence('source','destination') values('Jasola','Sarita Vihar');");
			db.execSQL("insert into stop_sequence('source','destination') values('Sarita Vihar','Jasola');");
			
			db.execSQL("insert into stop_sequence('source','destination') values('Sarita Vihar','Mohan Estate');");
			db.execSQL("insert into stop_sequence('source','destination') values('Mohan Estate','Sarita Vihar');");
			
			db.execSQL("insert into stop_sequence('source','destination') values('Mohan Estate','Tuglakabad');");
			db.execSQL("insert into stop_sequence('source','destination') values('Tuglakabad','Mohan Estate');");

			db.execSQL("insert into stop_sequence('source','destination') values('Badarpur','Tuglakabad');");
			db.execSQL("insert into stop_sequence('source','destination') values('Tuglakabad','Badarpur');");

			db.execSQL("insert into stop_sequence('source','destination') values('Badarpur','Sarai');");
			db.execSQL("insert into stop_sequence('source','destination') values('Sarai','Badarpur');");

			db.execSQL("insert into stop_sequence('source','destination') values('NHPC Chowk','Sarai');");
			db.execSQL("insert into stop_sequence('source','destination') values('Sarai','NHPC Chowk');");

			db.execSQL("insert into stop_sequence('source','destination') values('NHPC Chowk','Mewala Maharajpur');");
			db.execSQL("insert into stop_sequence('source','destination') values('Mewala Maharajpur','NHPC Chowk');");

			db.execSQL("insert into stop_sequence('source','destination') values('Sector-28','Mewala Maharajpur');");
			db.execSQL("insert into stop_sequence('source','destination') values('Mewala Maharajpur','Sector-28');");

			db.execSQL("insert into stop_sequence('source','destination') values('Sector-28','Badkal Mor');");
			db.execSQL("insert into stop_sequence('source','destination') values('Badkal Mor','Sector-28');");

			db.execSQL("insert into stop_sequence('source','destination') values('Old Faridabad','Badkal Mor');");
			db.execSQL("insert into stop_sequence('source','destination') values('Badkal Mor','Old Faridabad');");

			db.execSQL("insert into stop_sequence('source','destination') values('Old Faridabad','Neelam Chowk Ajronda');");
			db.execSQL("insert into stop_sequence('source','destination') values('Neelam Chowk Ajronda','Old Faridabad');");

			db.execSQL("insert into stop_sequence('source','destination') values('Neelam Chowk Ajronda','Bata Chowk');");
			db.execSQL("insert into stop_sequence('source','destination') values('Bata Chowk','Neelam Chowk Ajronda');");

			db.execSQL("insert into stop_sequence('source','destination') values('Escorts Mujesar','Bata Chowk');");
			db.execSQL("insert into stop_sequence('source','destination') values('Bata Chowk','Escorts Mujesar');");

			/************* VIOLET LINE********************************************/
			
			
			/************* BLUE LINE********************************************/
			db.execSQL("insert into stop_sequence('source','destination') values('Dwarka Sec-21','Dwarka Sec-8');");
			db.execSQL("insert into stop_sequence('source','destination') values('Dwarka Sec-8','Dwarka Sec-21');");
			
			db.execSQL("insert into stop_sequence('source','destination') values('Dwarka Sec-8','Dwarka Sec-9');");
			db.execSQL("insert into stop_sequence('source','destination') values('Dwarka Sec-9','Dwarka Sec-8');");
			
			db.execSQL("insert into stop_sequence('source','destination') values('Dwarka Sec-9','Dwarka Sec-10');");
			db.execSQL("insert into stop_sequence('source','destination') values('Dwarka Sec-10','Dwarka Sec-9');");
			
			db.execSQL("insert into stop_sequence('source','destination') values('Dwarka Sec-10','Dwarka Sec-11');");
			db.execSQL("insert into stop_sequence('source','destination') values('Dwarka Sec-11','Dwarka Sec-10');");
			
			db.execSQL("insert into stop_sequence('source','destination') values('Dwarka Sec-11','Dwarka Sec-12');");
			db.execSQL("insert into stop_sequence('source','destination') values('Dwarka Sec-12','Dwarka Sec-11');");
			
			db.execSQL("insert into stop_sequence('source','destination') values('Dwarka Sec-12','Dwarka Sec-13');");
			db.execSQL("insert into stop_sequence('source','destination') values('Dwarka Sec-13','Dwarka Sec-12');");
			
			db.execSQL("insert into stop_sequence('source','destination') values('Dwarka Sec-13','Dwarka Sec-14');");
			db.execSQL("insert into stop_sequence('source','destination') values('Dwarka Sec-14','Dwarka Sec-13');");
			
			db.execSQL("insert into stop_sequence('source','destination') values('Dwarka Sec-14','Dwarka');");
			db.execSQL("insert into stop_sequence('source','destination') values('Dwarka','Dwarka Sec-14');");
			
			db.execSQL("insert into stop_sequence('source','destination') values('Dwarka','Dwarka Mor');");
			db.execSQL("insert into stop_sequence('source','destination') values('Dwarka Mor','Dwarka');");
			

			db.execSQL("insert into stop_sequence('source','destination') values('Dwarka Mor','Nawada');");
			db.execSQL("insert into stop_sequence('source','destination') values('Nawada','Dwarka Mor');");
			
			db.execSQL("insert into stop_sequence('source','destination') values('Nawada','Uttam Nagar West');");
			db.execSQL("insert into stop_sequence('source','destination') values('Uttam Nagar West','Nawada');");
			
			db.execSQL("insert into stop_sequence('source','destination') values('Uttam Nagar West','Uttam Nagar East');");
			db.execSQL("insert into stop_sequence('source','destination') values('Uttam Nagar East','Uttam Nagar West');");
			
			db.execSQL("insert into stop_sequence('source','destination') values('Uttam Nagar East','Janak Puri West');");
			db.execSQL("insert into stop_sequence('source','destination') values('Janak Puri West','Uttam Nagar East');");
			
			db.execSQL("insert into stop_sequence('source','destination') values('Janak Puri West','Janak Puri East');");
			db.execSQL("insert into stop_sequence('source','destination') values('Janak Puri East','Janak Puri West');");

			db.execSQL("insert into stop_sequence('source','destination') values('Janak Puri East','Tilak Nagar');");
			db.execSQL("insert into stop_sequence('source','destination') values('Tilak Nagar','Janak Puri East');");


			
			db.execSQL("insert into stop_sequence('source','destination') values('Tilak Nagar','Subash Nagar');");
			db.execSQL("insert into stop_sequence('source','destination') values('Subash Nagar','Tilak Nagar');");
			
			db.execSQL("insert into stop_sequence('source','destination') values('Subash Nagar','Tagore Garden');");
			db.execSQL("insert into stop_sequence('source','destination') values('Tagore Garden','Subash Nagar');");
			
			db.execSQL("insert into stop_sequence('source','destination') values('Tagore Garden','Rajouri Garden');");
			db.execSQL("insert into stop_sequence('source','destination') values('Rajouri Garden','Tagore Garden');");
			
			db.execSQL("insert into stop_sequence('source','destination') values('Rajouri Garden','Ramesh Nagar');");
			db.execSQL("insert into stop_sequence('source','destination') values('Ramesh Nagar','Rajouri Garden');");
			
			db.execSQL("insert into stop_sequence('source','destination') values('Ramesh Nagar','Moti Nagar');");
			db.execSQL("insert into stop_sequence('source','destination') values('Moti Nagar','Ramesh Nagar');");

			db.execSQL("insert into stop_sequence('source','destination') values('Moti Nagar','Kirti Nagar');");
			db.execSQL("insert into stop_sequence('source','destination') values('Kirti Nagar','Moti Nagar');");


			
			db.execSQL("insert into stop_sequence('source','destination') values('Kirti Nagar','Shadipur');");
			db.execSQL("insert into stop_sequence('source','destination') values('Shadipur','Kirti Nagar');");
			
			db.execSQL("insert into stop_sequence('source','destination') values('Shadipur','Patel Nagar');");
			db.execSQL("insert into stop_sequence('source','destination') values('Patel Nagar','Shadipur');");
			
			db.execSQL("insert into stop_sequence('source','destination') values('Patel Nagar','Rajendra Place');");
			db.execSQL("insert into stop_sequence('source','destination') values('Rajendra Place','Patel Nagar');");
			
			db.execSQL("insert into stop_sequence('source','destination') values('Rajendra Place','Karol Bagh');");
			db.execSQL("insert into stop_sequence('source','destination') values('Karol Bagh','Rajendra Place');");
			
			db.execSQL("insert into stop_sequence('source','destination') values('Karol Bagh','Jhandewalan');");
			db.execSQL("insert into stop_sequence('source','destination') values('Jhandewalan','Karol Bagh');");

			db.execSQL("insert into stop_sequence('source','destination') values('Jhandewalan','R K Ashram Marg');");
			db.execSQL("insert into stop_sequence('source','destination') values('R K Ashram Marg','Jhandewalan');");


			
			db.execSQL("insert into stop_sequence('source','destination') values('R K Ashram Marg','Rajiv Chowk');");
			db.execSQL("insert into stop_sequence('source','destination') values('Rajiv Chowk','R K Ashram Marg');");
			
			db.execSQL("insert into stop_sequence('source','destination') values('Rajiv Chowk','Barakhamba');");
			db.execSQL("insert into stop_sequence('source','destination') values('Barakhamba','Rajiv Chowk');");
			
			db.execSQL("insert into stop_sequence('source','destination') values('Barakhamba','Mandi House');");
			db.execSQL("insert into stop_sequence('source','destination') values('Mandi House','Barakhamba');");
			
			db.execSQL("insert into stop_sequence('source','destination') values('Mandi House','Janpath');");
			db.execSQL("insert into stop_sequence('source','destination') values('Janpath','Mandi House');");
			
			db.execSQL("insert into stop_sequence('source','destination') values('Janpath','Central Secretariat');");
			db.execSQL("insert into stop_sequence('source','destination') values('Central Secretariat','Janpath');");
			
			db.execSQL("insert into stop_sequence('source','destination') values('Mandi House','Pragati Maidan');");
			db.execSQL("insert into stop_sequence('source','destination') values('Pragati Maidan','Mandi House');");
			
			db.execSQL("insert into stop_sequence('source','destination') values('Pragati Maidan','Indraprastha');");
			db.execSQL("insert into stop_sequence('source','destination') values('Indraprastha','Pragati Maidan');");

			db.execSQL("insert into stop_sequence('source','destination') values('Indraprastha','Yamuna Bank');");
			db.execSQL("insert into stop_sequence('source','destination') values('Yamuna Bank','Indraprastha');");




			db.execSQL("insert into stop_sequence('source','destination') values('Yamuna Bank','Akshardham');");
			db.execSQL("insert into stop_sequence('source','destination') values('Akshardham','Yamuna Bank');");
			
			db.execSQL("insert into stop_sequence('source','destination') values('Akshardham','Mayur Vihar 1');");
			db.execSQL("insert into stop_sequence('source','destination') values('Mayur Vihar 1','Akshardham');");
			
			db.execSQL("insert into stop_sequence('source','destination') values('Mayur Vihar 1','Mayur Vihar Ext');");
			db.execSQL("insert into stop_sequence('source','destination') values('Mayur Vihar Ext','Mayur Vihar 1');");
			
			db.execSQL("insert into stop_sequence('source','destination') values('Mayur Vihar Ext','New Ashok Nagar');");
			db.execSQL("insert into stop_sequence('source','destination') values('New Ashok Nagar','Mayur Vihar Ext');");
			
			db.execSQL("insert into stop_sequence('source','destination') values('New Ashok Nagar','Noida Sec-15');");
			db.execSQL("insert into stop_sequence('source','destination') values('Noida Sec-15','New Ashok Nagar');");

			db.execSQL("insert into stop_sequence('source','destination') values('Noida Sec-15','Noida Sec-16');");
			db.execSQL("insert into stop_sequence('source','destination') values('Noida Sec-16','Noida Sec-15');");

			


			db.execSQL("insert into stop_sequence('source','destination') values('Noida Sec-16','Noida Sec-18');");
			db.execSQL("insert into stop_sequence('source','destination') values('Noida Sec-18','Noida Sec-16');");
			
			db.execSQL("insert into stop_sequence('source','destination') values('Noida Sec-18','Botanical Garden');");
			db.execSQL("insert into stop_sequence('source','destination') values('Botanical Garden','Noida Sec-18');");
			
			db.execSQL("insert into stop_sequence('source','destination') values('Botanical Garden','Golf Course');");
			db.execSQL("insert into stop_sequence('source','destination') values('Golf Course','Botanical Garden');");
			
			db.execSQL("insert into stop_sequence('source','destination') values('Golf Course','Noida City Centre');");
			db.execSQL("insert into stop_sequence('source','destination') values('Noida City Centre','Golf Course');");
			
			db.execSQL("insert into stop_sequence('source','destination') values('Yamuna Bank','Laxmi Nagar');");
			db.execSQL("insert into stop_sequence('source','destination') values('Laxmi Nagar','Yamuna Bank');");

			db.execSQL("insert into stop_sequence('source','destination') values('Laxmi Nagar','Nirman Vihar');");
			db.execSQL("insert into stop_sequence('source','destination') values('Nirman Vihar','Laxmi Nagar');");

			
			
			
			db.execSQL("insert into stop_sequence('source','destination') values('Nirman Vihar','Preet Vihar');");
			db.execSQL("insert into stop_sequence('source','destination') values('Preet Vihar','Nirman Vihar');");
			
			db.execSQL("insert into stop_sequence('source','destination') values('Preet Vihar','Karkarduma');");
			db.execSQL("insert into stop_sequence('source','destination') values('Karkarduma','Preet Vihar');");
			
			db.execSQL("insert into stop_sequence('source','destination') values('Karkarduma','Anand Vihar');");
			db.execSQL("insert into stop_sequence('source','destination') values('Anand Vihar','Karkarduma');");
			
			db.execSQL("insert into stop_sequence('source','destination') values('Anand Vihar','Kaushambi');");
			db.execSQL("insert into stop_sequence('source','destination') values('Kaushambi','Anand Vihar');");
			
			db.execSQL("insert into stop_sequence('source','destination') values('Kaushambi','Vaishali');");
			db.execSQL("insert into stop_sequence('source','destination') values('Vaishali','Kaushambi');");


			/************* BLUE LINE********************************************/

		/************* rapid metro guragaon*****************/
			db.execSQL("insert into stop_sequence('source','destination') values('Phase-2','Vodafone Belvedere Towers');");

			db.execSQL("insert into stop_sequence('source','destination') values('Vodafone Belvedere Towers','Cyber City');");

			db.execSQL("insert into stop_sequence('source','destination') values('Cyber City','Moulsari Avenue');");

			db.execSQL("insert into stop_sequence('source','destination') values('Moulsari Avenue','Phase-3');");

			db.execSQL("insert into stop_sequence('source','destination') values('Phase-3','Phase-2');");

			db.execSQL("insert into stop_sequence('source','destination') values('Phase-2','Sikandarpur');");

			db.execSQL("insert into stop_sequence('source','destination') values('Sikandarpur','Phase-2');");

			/************* rapid metro guragaon*****************/
		}catch (SQLiteException e) {
			e.printStackTrace();
		}
	}



	private void load_colorattributes(SQLiteDatabase db) {
		try {
			db.execSQL("insert into  color values('V','V')");
			db.execSQL("insert into  color values('I','I')");
			db.execSQL("insert into  color values('B','B')");
			db.execSQL("insert into  color values('G','G')");
			db.execSQL("insert into  color values('Y','Y')");
			db.execSQL("insert into  color values('O','O')");
			db.execSQL("insert into  color values('R','R')");
			db.execSQL("insert into  color values('dB','dB')");//Rapid Metro Gurgaon
			db.execSQL("insert into  color values('X','X')");
		}catch(SQLiteException e) {
			e.printStackTrace();
		}
		
	}



	private void load_fareattributes(SQLiteDatabase db) {
		try {
			db.execSQL("insert into  fareattributes values(8,8)");
			db.execSQL("insert into  fareattributes values(10,10)");
			db.execSQL("insert into  fareattributes values(12,12)");
			db.execSQL("insert into  fareattributes values(15,15)");
			db.execSQL("insert into  fareattributes values(16,16)");
			db.execSQL("insert into  fareattributes values(18,18)");
			db.execSQL("insert into  fareattributes values(19,19)");
			db.execSQL("insert into  fareattributes values(20,20)");
			db.execSQL("insert into  fareattributes values(21,21)");
			db.execSQL("insert into  fareattributes values(22,22)");
			db.execSQL("insert into  fareattributes values(23,23)");
			db.execSQL("insert into  fareattributes values(25,25)");
			db.execSQL("insert into  fareattributes values(27,27)");
			db.execSQL("insert into  fareattributes values(28,28)");
			db.execSQL("insert into  fareattributes values(29,29)");
			db.execSQL("insert into  fareattributes values(30,30)");
			db.execSQL("insert into  fareattributes values(32,32)");
			db.execSQL("insert into  fareattributes values(35,35)");
			db.execSQL("insert into  fareattributes values(36,36)");
			db.execSQL("insert into  fareattributes values(38,38)");
			db.execSQL("insert into  fareattributes values(39,39)");
			db.execSQL("insert into  fareattributes values(41,41)");
			db.execSQL("insert into  fareattributes values(42,42)");
			db.execSQL("insert into  fareattributes values(43,43)");
			db.execSQL("insert into  fareattributes values(45,45)");
			db.execSQL("insert into  fareattributes values(47,47)");
			db.execSQL("insert into  fareattributes values(48,48)");
			db.execSQL("insert into  fareattributes values(49,49)");
			db.execSQL("insert into  fareattributes values(50,50)");

		}catch(SQLiteException e) {
			e.printStackTrace();
		}
	}



	public  void read_farefile(SQLiteDatabase db) {
		for(int i=0;i<STOP_SIZE;i++) {
			fare[i] = new int[STOP_SIZE];
		}
		int i =0 ;
		InputStream is = context.getResources().openRawResource(R.raw.fare);
        BufferedReader br = null;
        try {
			 
			String sCurrentLine;
 
			br = new BufferedReader(new InputStreamReader(is));
			
			while ((sCurrentLine = br.readLine()) != null) {
				
				String[] arr = sCurrentLine.split(",");
			    int size = arr.length;
			    for(int j=0;j<size;j++) {
			         fare[i][j] = Integer.valueOf(arr[j]);
			    }
			//    break;
			i++;
			}
 
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
        
    	try{int count =0 ;
    	   for(int ii=0;ii<STOP_SIZE;ii++) {
           	for(int j=ii;j<STOP_SIZE;j++) {
           		LogUtils.LOGD(TAG,++count+"");
           		 		db.execSQL("insert into  fare ('stop_from','stop_to','fare_id') values('"+map.get(ii)+"'"+","+"'"+map.get(j)+"'"+","+fare[ii][j]+")");
           	}
           	
           }
    	}catch(SQLiteException e) {
    		e.printStackTrace();
    	}finally {
    	//	db.close();
    	}
    }
    
	private void read_stopfile(SQLiteDatabase db){
		InputStream is = context.getResources().openRawResource(R.raw.stops);
        BufferedReader br = null;
        try {
			 
			String sCurrentLine;
 
			br = new BufferedReader(new InputStreamReader(is));
			int stop_index = 0;
			while ((sCurrentLine = br.readLine()) != null) {
				String[] arr = sCurrentLine.split(",");
				if(arr.length==2){
				    db.execSQL("insert into  stops ('stop_name','color_id','line_name') values('"+arr[0]+"'"+","+"'"+arr[1]+"'"+","+"'N"+"A'"+")");
				}
				else if(arr.length == 3){
					db.execSQL("insert into  stops ('stop_name','color_id','line_name') values('"+arr[0]+"'"+","+"'"+arr[1]+"'"+","+"'"+arr[2]+"'"+")");
				}
			    map.put(stop_index, arr[0]);
			stop_index++;
			}
 
		} catch (SQLiteException e) {
			e.printStackTrace();
		}catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}		

	
	}

	
	public ArrayList<String> getStopsList(
			HashMap<String, String> HiToEn,
			HashMap<String, String> EntoHi,
			/*HashMap<String, String> TaToEn,
			HashMap<String, String> EntoTa,
			HashMap<String, String> TeToEn,
			HashMap<String, String> EntoTe,
			*/HashMap<String, String> KnToEn,
			HashMap<String, String> EntoKn,
			//HashMap<String, String> MlToEn,
			//HashMap<String, String> EntoMl,
			HashMap<String, String> MrToEn,
			HashMap<String, String> EntoMr
			){
		SQLiteDatabase db = this.getReadableDatabase();
		ArrayList<String> list = new ArrayList<String>();
		String []stop_hi = context.getResources().getStringArray(R.array.stoplist_hi);
		//String []stop_ta = context.getResources().getStringArray(R.array.stoplist_ta);
		//String []stop_te = context.getResources().getStringArray(R.array.stoplist_te);
		String []stop_kn = context.getResources().getStringArray(R.array.stoplist_kn);
		//String []stop_ml = context.getResources().getStringArray(R.array.stoplist_ml);
		String []stop_mr = context.getResources().getStringArray(R.array.stoplist_mr);
		
		int index = 0;
		try {
		Cursor result = db.rawQuery("select stop_name from stops order by stop_name asc", null);
		if(result.moveToFirst()) {
			do {
				String stop_name = result.getString(0);
				list.add(stop_name);
				HiToEn.put(stop_hi[index], stop_name);
				EntoHi.put(stop_name, stop_hi[index]);
				LogUtils.LOGD("DbHelper",stop_hi[index]+" "+stop_name);
		/*		TaToEn.put(stop_ta[index], stop_name);
				EntoTa.put(stop_name, stop_ta[index]);
				TeToEn.put(stop_te[index], stop_name);
				EntoTe.put(stop_name, stop_te[index]);
		*/
				KnToEn.put(stop_kn[index], stop_name);
				EntoKn.put(stop_name, stop_kn[index]);
				
		/*		MlToEn.put(stop_ml[index], stop_name);
				EntoMl.put(stop_name, stop_ml[index]);
		*/
				MrToEn.put(stop_mr[index], stop_name);
				EntoMr.put(stop_name, stop_mr[index]);
				
				index++;
			}while(result.moveToNext());
		}
		}catch (SQLiteException e) {
			e.printStackTrace();
		}finally {
			db.close();
		}
		return  list!=null?list:null;
	}
	
	
	public HashMap<String, String> getStopsColorMap(ArrayList<Vertex> nodes) {
		SQLiteDatabase db = this.getReadableDatabase();
		HashMap<String, String> map = new HashMap<String,String>();
		try {
		Cursor result = db.rawQuery("select stop_name,color_id,lat,lon from stops", null);
		if(result.moveToFirst()) {
			do {
				String stop_name = result.getString(0);
				String colorcode_id = result.getString(1);
				String lat = result.getString(2);
				String lon = result.getString(3);
                LogUtils.LOGD("locationtest",stop_name+" " +lat + " " + lon);
				map.put(stop_name, colorcode_id);
				nodes.add(new Vertex(stop_name, stop_name));
			}while(result.moveToNext());
		}
		}catch (SQLiteException e) {
			e.printStackTrace();
		}finally {
			db.close();
		}
		return  map!=null?map:null;
	}
	

	public HashMap<String, String> getStopsLineMap() {
		SQLiteDatabase db = this.getReadableDatabase();
		HashMap<String, String> map = new HashMap<String,String>();
		try {
		Cursor result = db.rawQuery("select stop_name,line_name from stops", null);
		if(result.moveToFirst()) {
			do {
				String stop_name = result.getString(0);
				String line_name = result.getString(1);
				map.put(stop_name, line_name);
			}while(result.moveToNext());
		}
		}catch (SQLiteException e) {
			e.printStackTrace();
		}finally {
			db.close();
		}
		return  map!=null?map:null;
	}
	
	
	
  public ArrayList<Edge> getEdgeList(){
	  ArrayList<Edge> list = new ArrayList<Edge>();
	  SQLiteDatabase db = this.getReadableDatabase();
	  try {
			Cursor result = db.rawQuery("select id, source,destination,length from stop_sequence", null);
			if(result.moveToFirst()) {
				do {
					int id = result.getInt(0);
					String source = result.getString(1);
					String destination = result.getString(2);
					String length = result.getString(3);
					if(Utils.isNullorWhiteSpace(length))
						length = "-1";
					Edge edge = new Edge("",new Vertex(source, source),new Vertex(destination, destination), 1,length);
					list.add(edge);
				}while(result.moveToNext());
			}
			}catch (SQLiteException e) {
				e.printStackTrace();
			}finally {
				db.close();
			}
			return  list!=null?list:null;	
	  
	  
  }
	
  public int getFare(String source,String destination) {
	  int fare = 0;
	  SQLiteDatabase db = this.getReadableDatabase();
	  try {
			Cursor result = db.rawQuery("select fare_id from fare where stop_from=? and stop_to=?",new String[]{source,destination});
			if(result.moveToFirst()) { // A->B fare exists
				do {
                      fare =  result.getInt(0);
				}while(result.moveToNext());
			}else{  // if A->B not exists, B->A check
				result = db.rawQuery("select fare_id from fare where stop_from=? and stop_to=?",new String[]{destination,source});
				if(result.moveToFirst()) {
					do {
						fare =  result.getInt(0);
					}while(result.moveToNext());
				}
			}
			}catch (SQLiteException e) {

				e.printStackTrace();
			}finally {
				db.close();
			}
  
     return fare;
  }



  public ArrayList<line> getLastStopList(
		  ArrayList<String> engLine,
		  ArrayList<String> hiLine,
		 // ArrayList<String> taLine,
		 // ArrayList<String> teLine,
		  ArrayList<String> knLine,
		 // ArrayList<String> mlLine,
		  ArrayList<String> mrLine,
		  HashMap<String, String>enToHiMap,
		  //HashMap<String, String>enToTaMap,
		  //HashMap<String, String>enToTeMap,
		  HashMap<String, String>enToKnMap,
		  //HashMap<String, String>enToMlMap,
		  HashMap<String, String>enToMrMap
		  ) {
	SQLiteDatabase db = this.getReadableDatabase();
	ArrayList<line> list = new ArrayList<line>();
	try {
	Cursor result = db.rawQuery("select source,destination from first_last_train", null);
	if(result.moveToFirst()) {
		do {
			String source = result.getString(0);
			String destination = result.getString(1);
			list.add(new line(source, destination));
			engLine.add(source+" -> "+destination);
			hiLine.add(enToHiMap.get(source)+" -> "+enToHiMap.get(destination));
			//taLine.add(enToTaMap.get(source)+" -> "+enToTaMap.get(destination));
			//teLine.add(enToTeMap.get(source)+" -> "+enToTeMap.get(destination));
			knLine.add(enToKnMap.get(source)+" -> "+enToKnMap.get(destination));
			//mlLine.add(enToMlMap.get(source)+" -> "+enToMlMap.get(destination));
			mrLine.add(enToMrMap.get(source)+" -> "+enToMrMap.get(destination));
		}while(result.moveToNext());
	}
	}catch (SQLiteException e) {
		e.printStackTrace();
	}finally {
		db.close();
	}
	return  list!=null?list:null;

}

public class line{
 private 	String source;
 private	String destination;
 
 public line(String s,String d){
	 source =s;
	 destination = d;
 }
 
 public String getSource(){
	 return source;
 }
 
 public String getDestination(){
	 return destination;
 }
 
}

public String[] getFirstLastTime(String source,String destination) {
	SQLiteDatabase db = this.getReadableDatabase();
	String[] arr = new String[2];
	try {
	Cursor result = db.rawQuery("select first,last from first_last_train where source=? and destination=?",new String[]{source,destination});

	if(result.moveToFirst()) {
		do {
			String first = result.getString(0);
			String last = result.getString(1);
			arr[0]=first;
			arr[1]=last;
			
		}while(result.moveToNext());
	}
	}catch (SQLiteException e) {
		e.printStackTrace();
	}finally {
		db.close();
	}
	
	return  arr!=null?arr:null;

}
  
public void updateFare(String s, String d,float fare){
	SQLiteDatabase db = this.getWritableDatabase();
	ContentValues cv = new ContentValues();
	cv.put("fare_id",fare);
	db.update("fare",cv,"stop_from=? and stop_to=?",new String[]{s,d});
}

}
