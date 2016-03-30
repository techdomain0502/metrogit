package com.delhi.metro.sasha.db;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import com.delhi.metro.sasha.R;

public class DbUtils {
	private  HashMap<Integer,String> map = new HashMap<Integer,String>();
	private int fare[][]= new int[133][];  
	private Context context;
	public DbUtils(Context c) {
      context = c;
	}
	
	public void load() {
		try {
		read_farefile();
		}
		catch(SQLiteException e) {
			e.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private  void read_farefile() {
		for(int i=0;i<fare.length;i++) {
			fare[i] = new int[133];
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
        
        
      //Ready to insert to Database;
        initMap();
	}

    private void initMap() {
        //Red Line
    	map.put(0, "Dilshad Garden");
    	map.put(1, "Jhilmil");
    	map.put(2, "Mansrovar Park");
    	map.put(3, "Shahdara");
    	map.put(4, "Welcome");
    	map.put(5, "Seelampur");
    	map.put(6, "Shastri Park");
    	map.put(7, "Kashmere Gate");
    	map.put(8, "Tis Hazari");
    	map.put(9, "Pul Bangash");
    	map.put(10,"Pratap Nagar");
    	map.put(11,"Shastri Nagar");

    
       // Green Line -5
     	map.put(12, "Mundka");
    	map.put(13, "Rajdhani Park");
    	map.put(14, "Nangloi Rly. Station");
    	map.put(15, "Nangloi");
    	map.put(16, "Surajmal Stadium");
    	map.put(17, "Udyog Nagar");
    	map.put(18, "Peeragarhi");
    	map.put(19, "Paschim Vihar(West)");
    	map.put(20, "Paschim Vihar(East)");
    	map.put(21, "Madipur");
    	map.put(22,"Shivaji Park");
    	map.put(23,"Punjabi Bagh");
    	map.put(24, "Ashok Park Main");
    	map.put(25,"Satguru Ram Singh Marg");

    
         // Red Line 1
    	map.put(26, "Inder Lok");
    	map.put(27, "Kanhaiya Nagar");
    	map.put(28, "Keshav Puram");
    	map.put(29, "Netaji Subash Palace");
    	map.put(30, "Kohat Enclave");
    	map.put(31, "Pitam Pura");
    	map.put(32, "Rohini East");
    	map.put(33,"Rohini West");
    	map.put(34,"Rithala");
    	

    	//Yellow Line -2
    	map.put(35, "Jahangirpuri");
    	map.put(36, "Adarsh Nagar");
    	map.put(37, "Azadpur");
    	map.put(38, "Model Town");
    	map.put(39, "GTB Nagar");
    	map.put(40, "Vishwavidyalaya");
    	map.put(41, "Vidhan Sabha");
    	map.put(42,"Civil Lines");
    	map.put(43,"Chandni Chowk");
    	map.put(44, "Chawri Bazar");
    	map.put(45, "New Delhi");
    	map.put(46, "Rajiv Chowk");
    	map.put(47, "Patel Chowk");
    	map.put(48, "Central Secretariat");
    	map.put(49, "Udyog Bhawan");
    	map.put(50, "Race Course");
    	map.put(51,"JorBagh");
    	map.put(52,"INA");
    	map.put(53, "AIIMS");
    	map.put(54, "Green Park");
    	map.put(55, "Hauz Khas");
    	map.put(56, "Malviya Nagar");
    	map.put(57, "Saket");
    	map.put(58, "Qutab Minar");
    	map.put(59, "Chattarpur");
    	map.put(60,"Sultanpur");
    	map.put(61,"Ghitorni");
    	map.put(62, "Arjangarh");
    	map.put(63, "Gurudronacharya");
    	map.put(64, "Sikandarpur");
    	map.put(65,"M G Road");
    	map.put(66,"IFFCO Chowk");
    	map.put(67, "Huda City Centre");
    
    
     	//Blue Line -4
    	map.put(68, "Vaishali");
    	map.put(69, "Kaushambi");
    	map.put(70, "Anand Vihar");
    	map.put(71, "Karkarduma");
    	map.put(72, "Preet Vihar");
    	map.put(73, "Nirman Vihar");
    	map.put(74, "Laxmi Nagar");
    	map.put(75,"Noida City Centre");
    	map.put(76,"Golf Course");
    	map.put(77, "Botanical Garden");
    	map.put(78, "Noida Sec18");
    	map.put(79, "Noida Sec16");
    	map.put(80, "Noida Sec15");
    	map.put(81, "New Ashok Nagar");
    	map.put(82, "Mayur Vihar Ext");
    	map.put(83, "Mayur Vihar1");
    	map.put(84,"Akshardham");
    	map.put(85,"Yamuna Bank");
    	map.put(86,"Indraprastha");
    	map.put(87, "Pragati Maidan");
    	map.put(88, "Mandi House");
    	map.put(89, "Barakhamba");
    	map.put(90, "R K Ashram Marg");
    	map.put(91, "Jhandewalan");
    	map.put(92, "Karol Bagh");
    	map.put(93, "Rajendra Place");
    	map.put(94,"Patel Nagar");
    	map.put(95,"Shadipur");
    	map.put(96, "Kirti Nagar");
    	map.put(97, "Moti Nagar");
    	map.put(98, "Ramesh Nagar");
    	map.put(99,"Rajouri Garden");
    	map.put(100,"Tagore Garden");
    	map.put(101, "Subash Nagar");
    	map.put(102, "Tilak Nagar");
    	map.put(103, "Janak Puri East");
    	map.put(104, "Janak Puri West");
    	map.put(105, "Uttam Nagar East");
    	map.put(106, "Uttam Nagar West");
    	map.put(107,"Nawada");
    	map.put(108,"Dwarka Mor");
    	map.put(109, "Dwarka");
    	map.put(110, "Dwarka Sec14");
    	map.put(111, "Dwarka Sec13");
    	map.put(112,"Dwarka Sec12");
    	map.put(113,"Dwarka Sec11");
    	map.put(114, "Dwarka Sec10");
    	map.put(115, "Dwarka Sec-9");
    	map.put(116, "Dwarka Sec-8");
    	map.put(117,"Dwarka Sec-21");
       
    	//violet line  -6 
    	map.put(118, "Khan Market");
    	map.put(119, "JLN Stadium");
    	map.put(120, "Jangpura");
    	map.put(121, "Lajpat Nagar");
    	map.put(122, "Moolchand");
    	map.put(123,"Kailash Colony");
    	map.put(124,"Nehru Place");
    	map.put(125, "Kalkaji Mandir");
    	map.put(126, "Govindpuri");
    	map.put(127, "Okhla");
    	map.put(128,"Jasola");
    	map.put(129,"Sarita Vihar");
    	map.put(130, "Mohan Estate");
    	map.put(131, "Tuglakabad");
    	map.put(132, "Badarpur");

    	DbHelper dbHelper = new DbHelper(context);
    	SQLiteDatabase db = null;
    	try{
    	db = dbHelper.getWritableDatabase();
    	   for(int ii=0;ii<133;ii++) {
           	for(int j=0;j<133;j++) {
           		db.execSQL("insert into "+DbConstants.Fare.TABLE_NAME+"('stop_from','stop_to','fare_id') values('"+map.get(ii)+"'"+","+"'"+map.get(j)+"'"+","+fare[ii][j]+")");
           	}
           }
    	}catch(SQLiteException e) {
    		e.printStackTrace();
    	}finally {
    		db.close();
    	}
    	   
    	   

    
    }

}
