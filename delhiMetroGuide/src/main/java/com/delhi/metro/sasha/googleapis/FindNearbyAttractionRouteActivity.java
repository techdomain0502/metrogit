package com.delhi.metro.sasha.googleapis;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Locale;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.delhi.metro.sasha.R;
import com.delhi.metro.sasha.db.DbHelper;
import com.delhi.metro.sasha.utils.LogUtils;
import com.delhi.metro.sasha.utils.Utils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class FindNearbyAttractionRouteActivity extends FragmentActivity implements /*LocationListener,*/
GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener{
    private ImageButton findNearest;
	private Button findRoute;
	private boolean isGPSEnabled, isNetworkEnabled;
	protected LocationManager locationManager;
	private Location location;
	private double lat, lon;
    private EditText nearestEdit;
	private boolean addLatLon=false;
	private ProgressDialog pd;
	private boolean nearsetSet;
	RelativeLayout buttonContainer;
	ImageView search;
	Button tap;
	private static final int SHOW_GPS_DIALOG =0 ;
	private static final int SHOW_NETWORK_DIALOG =1;
	private GoogleApiClient mGoogleApiClient;
	private Location mCurrentLocation;
	   
	  private boolean isGooglePlayServicesAvailable() {
	        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
	        if (ConnectionResult.SUCCESS == status) {
	            return true;
	        } else if(status==1) {
				Toast.makeText(getApplicationContext(),"Google Play services missing",Toast.LENGTH_LONG).show();
				finish();
				return false;
			}
		  else if(status==2){
				Toast.makeText(getApplicationContext(),"Google Play services is not updated",Toast.LENGTH_LONG).show();
	            GooglePlayServicesUtil.getErrorDialog(status, this, 0).show();
	            return false;
	        }
		  else if(status ==9){
				Toast.makeText(getApplicationContext(),"Google Play services signature invalid",Toast.LENGTH_LONG).show();
				finish();
				return false;
			}
		  return false;
	    }
	  
	@Override
    protected void onCreate(Bundle arg0) {
    	super.onCreate(arg0);
       setContentView(R.layout.nearby_attraction);
       isGooglePlayServicesAvailable();

       
       mGoogleApiClient = new GoogleApiClient.Builder(this)
       .addApi(LocationServices.API)
       .addConnectionCallbacks(this)
       .addOnConnectionFailedListener(this)
       .build();

       
     findNearest = (ImageButton)findViewById(R.id.current_location_button);
     findRoute = (Button)findViewById(R.id.findRoute);
     nearestEdit = (EditText)findViewById(R.id.current_location_text);
     tap = (Button)findViewById(R.id.spinner1);
     search = (ImageView)findViewById(R.id.searchView1);
     
     search.setOnClickListener(new ViewOnClickListener());
     tap.setOnClickListener(new ViewOnClickListener());
     findRoute.setOnClickListener(new ViewOnClickListener());
     findNearest.setOnClickListener(new ViewOnClickListener());
    
		pd = new ProgressDialog(FindNearbyAttractionRouteActivity.this);
		pd.setMessage("Finding Nearest Metro...");
		pd.setCanceledOnTouchOutside(false);
		pd.setCancelable(false);
		
     }
	

    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }
	
    @Override
    protected void onResume() {
    	// TODO Auto-generated method stub
    	super.onResume();
    
    	if(addLatLon) {
       	 updateLatLon();
        }else {
           // findNearestMetro();
          //findNearestMetroFused();
        }
   	
    }
    
	private void findNearestMetroFused() {

		mCurrentLocation  = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
		if (false == nearsetSet &&   null != mCurrentLocation) {
             lat = mCurrentLocation.getLatitude();
             lon = mCurrentLocation.getLongitude();
     		pd.show();
    		new updateSourceMetroTask().execute();
		} /*else {
			nearestEdit.setText("Tap to search!!");
            Log.d("locationtest", "location is null ...............");
        }*/
	}
    
	private class updateSourceMetroTask extends AsyncTask<Void,Void,Void>{
        private String nearest;
		@Override
		protected Void doInBackground(Void... params) {
            DbHelper helper = new DbHelper(getApplicationContext());
            SQLiteDatabase db = helper.getReadableDatabase();
            Cursor c = db.rawQuery("select lat,lon from stops order by stop_name asc", null);
            double coord[][]= new double[c.getCount()][2];
            int row  =0;
            if(c.moveToFirst()) {
            	do {
            		coord[row][0] = c.getDouble(0);
            		coord[row][1] = c.getDouble(1);
            		row++;
            	}while(c.moveToNext());
            	
            }
            float[] results = new float[1];
            float[] results1 = new float[1];
            Location.distanceBetween(lat,lon, coord[0][0],coord[0][1],results);
            LogUtils.LOGD("locationtest", "firstmin source distance="+results[0]+" coords.length="+coord.length);
            float minDistance = results[0];
            int position=0;
            for(int index=1;index<coord.length;index++) {
            	Location.distanceBetween(lat,lon, coord[index][0],coord[index][1],results1);
            	
            	LogUtils.LOGD("locationtest", index+"th min source distance="+results1[0]+coord[index][0]+" "+coord[index][1]);
            	if(results1[0]<minDistance) {
            		minDistance = results1[0];
            		position = index;
            		LogUtils.LOGD("locationtest", "minDistance="+minDistance);
            	}
            }
            
            c= db.rawQuery("select stop_name from stops where lat=? and lon=?",new String[] {coord[position][0]+"",coord[position][1]+""});
            if(c.moveToFirst()) {
            	do {
            		nearest = c.getString(0);
            		LogUtils.LOGD("locationtest", "nearest="+nearest);
            	}while(c.moveToNext());
            }
			db.close();
            return null;
            
		}
		
		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			if(pd!=null && pd.isShowing()) {
			      pd.dismiss();
			      nearestEdit.setText(nearest);
				  nearsetSet = true;
			 LogUtils.LOGD("locationtest","onpostexecute ... nearest=" +nearest);
			}
		}
	}
	
	private class updateDestinationMetroTask extends AsyncTask<String,Void,Void>{
        private String source;
		private String destination;

		private double[] fallbackGeocoder(String address)  throws JSONException{
			StringBuilder stringBuilder = new StringBuilder();
			try {
			HttpGet httpGet = new HttpGet(
					"http://maps.google.com/maps/api/geocode/json?address="
							+ URLEncoder.encode(address, "UTF-8") + "&ka&sensor=false");
			HttpClient client = new DefaultHttpClient();
			HttpResponse response;



				response = client.execute(httpGet);
				HttpEntity entity = response.getEntity();
				InputStream stream = entity.getContent();
				int b;
				while ((b = stream.read()) != -1) {
					stringBuilder.append((char) b);
				}
			}catch (UnsupportedEncodingException e){

			} catch(ClientProtocolException e) {
			} catch (IOException e) {
			}

			JSONObject jsonObject = new JSONObject(stringBuilder.toString());

			double lng = ((JSONArray) jsonObject.get("results")).getJSONObject(0)
					.getJSONObject("geometry").getJSONObject("location")
					.getDouble("lng");

			double lat = ((JSONArray) jsonObject.get("results")).getJSONObject(0)
					.getJSONObject("geometry").getJSONObject("location")
					.getDouble("lat");
               return new double[]{lat,lng};
		}
        @Override
		protected Void doInBackground(String... params) {
            DbHelper helper = new DbHelper(getApplicationContext());
            SQLiteDatabase db = helper.getReadableDatabase();
			Geocoder geocoder = new Geocoder(FindNearbyAttractionRouteActivity.this, Locale.getDefault());
            double placeLat,placeLon;
            double coord[][];

            Cursor c = db.rawQuery("select lat,lon from stops order by stop_name asc",null);
            coord = new double[c.getCount()][2];
           if(c.moveToFirst()) {
        	   int i = 0;
        	   do {
                   	coord[i][0]= c.getDouble(0);
                   	coord[i][1]= c.getDouble(1);
                   	i++;
        	   }while(c.moveToNext());
           }

            String placeName = params[0];
            
					List addressList;
					try {
                		LogUtils.LOGD("locationtest", "placeName="+placeName);
						addressList = geocoder.getFromLocationName(
								placeName, 1);
						if (addressList != null && addressList.size() > 0) {
							Address address = (Address) addressList.get(0);
                            placeLat = address.getLatitude();
                            placeLon = address.getLongitude();
                            float[] results = new float[1];
                            Location.distanceBetween(placeLat,placeLon, coord[0][0],coord[0][1],results);
                            float minDistance = results[0];
                            int position=0;
                            for(int index=1;index<coord.length;index++) {
                            	Location.distanceBetween(placeLat, placeLon, coord[index][0],coord[index][1],results);
                            	if(results[0]<minDistance) {
                            		minDistance = results[0];
                            		position = index;
                            	}
                            }

                            c= db.rawQuery("select stop_name from stops where lat=? and lon=?",new String[] {coord[position][0]+"",coord[position][1]+""});
                            if(c.moveToFirst()) {
                            	do {
                            		destination = c.getString(0);
                            	}while(c.moveToNext());
                            }
                			db.close();
						} 
					}catch (IOException e) {
						e.printStackTrace();
						try {
							double arr[] = fallbackGeocoder(params[0]);
							placeLat = arr[0];
							placeLon = arr[1];
							float[] results = new float[1];
							Location.distanceBetween(placeLat,placeLon, coord[0][0],coord[0][1],results);
							float minDistance = results[0];
							int position=0;
							for(int index=1;index<coord.length;index++) {
								Location.distanceBetween(placeLat, placeLon, coord[index][0],coord[index][1],results);
								if(results[0]<minDistance) {
									minDistance = results[0];
									position = index;
								}
							}

							c= db.rawQuery("select stop_name from stops where lat=? and lon=?",new String[] {coord[position][0]+"",coord[position][1]+""});
							if(c.moveToFirst()) {
								do {
									destination = c.getString(0);
								}while(c.moveToNext());
							}
							db.close();
						}catch (JSONException e1){
                          e1.printStackTrace();;
						}
					}
					
			return null;
		}



		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
		    if(pd != null && pd.isShowing())
			    pd.dismiss();
		    source = nearestEdit.getText().toString();
		    Intent i = new Intent(getApplicationContext(),FollowRouteActivity.class);
		    i.putExtra("source",source);
		    i.putExtra("destination",destination);
			if(type == 2)
				i.putExtra("place",attraction);
			else if(tap.getText()!=null)
				i.putExtra("place",tap.getText().toString());
		    if(!Utils.isNullorWhiteSpace(source) && !Utils.isNullorWhiteSpace(destination))
		        startActivity(i);
		    else
		    	Toast.makeText(getApplicationContext(), "Sorry!! Unable to find route :(",Toast.LENGTH_LONG).show();
		}
	}
	
	
	private class updateLatLongTask extends AsyncTask<String[],Void ,Void>{

		@Override
		protected Void doInBackground(String[]... params) {
              DbHelper helper = new DbHelper(getApplicationContext());
              SQLiteDatabase db = helper.getWritableDatabase();
				Geocoder geocoder = new Geocoder(FindNearbyAttractionRouteActivity.this, Locale.getDefault());

              for(int i=0;i<params[0].length;i++) {
                  String locationAddress = params[0][i];
					List addressList;
					try {
						addressList = geocoder.getFromLocationName(
								locationAddress, 1);
						if (addressList != null && addressList.size() > 0) {
							Address address = (Address) addressList.get(0);
				           
					      ContentValues cv = new ContentValues();
					      cv.put("lat",address.getLatitude());
					      cv.put("lon", address.getLongitude());
					      int noofRows = db.update("stops", cv,"stop_name=?",new String[] {locationAddress});
					      LogUtils.LOGD("locationtest","rowsaffected="+noofRows+" "+ locationAddress+" "+address.getLatitude()+" "+address.getLongitude()+" "+noofRows);
						} 
					}catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					}
              
              
				
					db.close();
			
			return null;
		}
		
		
		
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
		DbHelper helper = new DbHelper(getApplicationContext());
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor c = db.rawQuery("select stop_name,lat,lon from stops order by stop_name asc", null);
		}
		
	}
	
    private void updateLatLon() {
    	String array[] = getResources().getStringArray(R.array.stoplist_en);
    	new updateLatLongTask().execute(array);
    }	
	
    
    private int type = 0;
	private String place = "",attraction="";
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	super.onActivityResult(requestCode,resultCode,data);
    

		if (data != null) {
			place = data.getStringExtra("place");
			attraction = data.getStringExtra("attraction");
			type = data.getIntExtra("type",0);
		}
		if (requestCode == 100 && resultCode == 102&& !Utils.isNullorWhiteSpace(place) && isGooglePlayServicesAvailable()) {
			tap.setText(place);
			pd.setMessage("And Here we Go!!!..");
			findRoute.setEnabled(true);
		}
    }
    
	
	
	
	
	private void findNearestMetro() {/*
	  try {
		  locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		  isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
		  isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		  if(!isGPSEnabled && !isNetworkEnabled)
			  showSettingsAlert("GPS Settings","GPS is not enabled. Do you want to enable?",SHOW_GPS_DIALOG);
		  else {

				if (isNetworkEnabled) {
					locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 2000, 1,FindNearbyAttractionRouteActivity.this);
					if (locationManager != null) {
						location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
						if (location != null) {
							lat = location.getLatitude();
							lon = location.getLongitude();
						}
					}
				}

				if (isGPSEnabled) {
					if (location == null) {
						locationManager.requestLocationUpdates(
								locationManager.GPS_PROVIDER, 2000,1, FindNearbyAttractionRouteActivity.this);
						if (locationManager != null) {

							location = locationManager
									.getLastKnownLocation(LocationManager.GPS_PROVIDER);
							if (location != null) {
								lat = location.getLatitude();
								lon = location.getLongitude();
							}

						}
					}
				}

		      pd.show();
		      LogUtils.LOGD("locationtest", "current location  lat="+lat+" "+"lon="+lon);
		      new  updateSourceMetroTask().execute();
		  }
	  }catch (Exception e) {
         e.printStackTrace();
	  }
	*/}
	
    
    
	private class ViewOnClickListener implements View.OnClickListener{

		@Override
		public void onClick(View v) {
          int id = v.getId();
          switch(id) {
          case R.id.current_location_button:
        	  //findNearestMetro();
			  locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
			  isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
			  if(!isGPSEnabled)
				  showSettingsAlert("GPS Settings","GPS is not enabled. Enable it to access fine location.",SHOW_GPS_DIALOG);
			  else
			    if(isGooglePlayServicesAvailable())
        	       findNearestMetroFused();
        	  break;
           case R.id.searchView1:
        	  Intent i = new Intent();
        	  i.setComponent(new ComponentName("com.delhi.metro.sasha", "com.delhi.metro.sasha.search.SearchPlace"));
        	  startActivityForResult(i,100);
        	  break;
          case R.id.spinner1:
        	  Intent ii = new Intent();
        	  ii.setComponent(new ComponentName("com.delhi.metro.sasha", "com.delhi.metro.sasha.search.SearchPlace"));
        	  startActivityForResult(ii,100);
        	  break;
          case R.id.findRoute:

			  if(nearsetSet == true) {
				  if (Utils.isWifiorData_connected(getApplicationContext())) {
					  pd.show();
					  new updateDestinationMetroTask().execute(tap.getText().toString());
				  } else
					  showSettingsAlert("Network Settings", "Need Working Internet Connection for this feature", SHOW_NETWORK_DIALOG);
			  }
			  else
			     Toast.makeText(getApplicationContext(),"Select current location first!! :)",Toast.LENGTH_LONG).show();
        	  break;
          }
		}

	}
	
    
@Override
protected void onDestroy() {
	super.onDestroy();
	/*if(locationManager!=null)
         locationManager.removeUpdates(this);*/
}    
    
    public void showSettingsAlert(String title,String message,final int id){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(FindNearbyAttractionRouteActivity.this);
       final Context mContext = FindNearbyAttractionRouteActivity.this;
        // Setting Dialog Title"GPS is settings"
        alertDialog.setTitle(title);
  
        // Setting Dialog Message "GPS is not enabled. Do you want to go to settings menu?"
        alertDialog.setMessage(message);
        // On pressing Settings button
		String positiveButtonText = "GPS Settings";
		String negativeButtonText ="Cancel";
		if(id==SHOW_NETWORK_DIALOG){
			positiveButtonText = "Wifi Settings";
			negativeButtonText = "Mobile Data";
		}
        alertDialog.setPositiveButton(positiveButtonText, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
            	Intent intent = null;
            	if(id==SHOW_GPS_DIALOG) {
                   intent   = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            	}else if(id==SHOW_NETWORK_DIALOG) {
            		intent   = new Intent(Settings.ACTION_WIFI_SETTINGS);
            	}
            	if(intent!=null && intent.resolveActivity(getPackageManager())!=null)
            		startActivity(intent);
            	else {
            		Toast.makeText(mContext, "No matching application found to handle Network/GPS settings.", Toast.LENGTH_SHORT).show();
            	}
            	
            }
        });
  
        // on pressing cancel button
        alertDialog.setNegativeButton(negativeButtonText, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
				Intent intent = null;

				if(id==SHOW_GPS_DIALOG) {
					dialog.cancel();
				}else if(id==SHOW_NETWORK_DIALOG) {
					intent = new Intent(Settings.ACTION_SETTINGS);
					startActivity(intent);
				}

				if(intent!=null && intent.resolveActivity(getPackageManager())!=null)
					startActivity(intent);
				else {
					Toast.makeText(mContext, "No matching application found to handle Network settings.", Toast.LENGTH_SHORT).show();
				}

			}
        });
  
        // Showing Alert Message
        alertDialog.show();
    }




	/*@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		
	}




	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}
*/



/*	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}
*/



/*	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}
*/
	@Override
	public void onConnectionFailed(ConnectionResult arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onConnected(Bundle arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onConnectionSuspended(int arg0) {
		// TODO Auto-generated method stub
		
	}
 
}
