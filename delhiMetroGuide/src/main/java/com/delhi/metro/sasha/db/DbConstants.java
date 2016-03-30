package com.delhi.metro.sasha.db;

import android.provider.BaseColumns;

public final class DbConstants {
       public DbConstants() {
   	}

   public static abstract class FareAttributes {
	   public static final String TABLE_NAME ="fareattributes";
	   public static final String COLUMN_NAME_FARE_ID="fare_id";
	   public static final String COLUMN_NAME_VALUE="fare_value";
      
   }
   
   public static abstract class Fare implements BaseColumns{
	   public static final String TABLE_NAME ="fare";
	   public static final String COLUMN_NAME_STOP_FROM="stop_from";
	   public static final String COLUMN_NAME_STOP_TO="stop_to";
	   public static final String COLUMN_NAME_FARE_ID="fare_id";
   }

   public static abstract class ColorCode{
	   public static final String TABLE_NAME ="color";
	   public static final String COLUMN_NAME_COLOR_ID="color_id";
	   public static final String COLUMN_NAME_COLOR="color";
   }
   
  public static abstract class Stops implements BaseColumns{
	  public static final String TABLE_NAME ="stops";
	  public static final String COLUMN_NAME_STOP="stop_name"; 
	  public static final String COLUMN_NAME_COLOR_ID="color_id";
  }
  
  public static abstract class Stop_Sequence{
	  public static final String TABLE_NAME="stop_sequence";
	  public static final String COLUMN_NAME_SOURCE="source";
	  public static final String COLUMN_NAME_DESTINATION="destination";
  }
  
  public static abstract class Line{
	  public static final String TABLE_NAME="line";
	  public static final String COLUMN_NAME_SOURCE="line_name";
  }

  public static abstract class First_Last{
	  public static final String TABLE_NAME="first_last_train";
	  public static final String COLUMN_NAME_SOURCE="source";
	  public static final String COLUMN_NAME_DESTINATION="destination";
	  public static final String COLUMN_NAME_FIRST="first_time";
	  public static final String COLUMN_NAME_LAST="last_time";
  }
  
   
}
