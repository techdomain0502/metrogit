package com.delhi.metro.sasha.fare;

import android.content.Context;

import com.delhi.metro.sasha.db.DbHelper;
import com.delhi.metro.sasha.utils.LogUtils;

public class CalculateFare {
	private Context mContext;
	private float fare = 0;

	public CalculateFare(Context context) {
		mContext = context;
	}

	public float getFare(float routeLength) {

		if(routeLength >0f && routeLength <=1.79f)
			fare = 8;
		else if(routeLength >1.79f && routeLength <=3.819f)
			fare = 10;
		else if(routeLength >3.819f && routeLength <=5.006f)
			fare = 12;
		else if(routeLength >5.006f && routeLength <=7.08f)
			fare = 15;
		else if(routeLength >7.08f && routeLength <=11.705f)
			fare = 16;
		else if(routeLength >11.705f && routeLength <=14.816f)
			fare = 18;
		else if(routeLength >14.816f && routeLength <=17.116f)
			fare = 19;
		else if(routeLength >17.116f && routeLength <=20.06f)
			fare = 21;
		else if(routeLength >20.06f && routeLength <=23.147f)
			fare = 22;
		else if(routeLength >23.147f && routeLength <=26.257f)
			fare = 23;
		else if(routeLength >26.257f && routeLength <=30.646f)
			fare = 25;
		else if(routeLength >30.646f && routeLength <=34.757f)
			fare = 27;
		else if(routeLength >34.757f && routeLength <=39.308f)
			fare = 28;
		else if(routeLength >39.308f && routeLength <=43.442f)
			fare = 29;
		else if(routeLength >43.442f && routeLength <=65.36f)
			fare = 30;
		LogUtils.LOGD("CalculateFare","routelength="+routeLength+"fare="+fare);
        return fare;
	}


}
