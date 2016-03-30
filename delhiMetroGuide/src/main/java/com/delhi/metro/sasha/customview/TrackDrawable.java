package com.delhi.metro.sasha.customview;

import com.delhi.metro.sasha.R;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class TrackDrawable extends View {
	
	private Paint dividerPaint;
	private Paint trackPaint;
	public TrackDrawable(Context context, AttributeSet attrs) {
		super(context, attrs);
		initPaint();
	}
	
	
	private void initPaint() {
		trackPaint = new Paint();
		trackPaint.setAntiAlias(true);
		trackPaint.setColor(getResources().getColor(R.color.blue));
		trackPaint.setStrokeWidth(6);
		
		dividerPaint = new Paint();
		dividerPaint.setAntiAlias(true);
		dividerPaint.setColor(getResources().getColor(R.color.blue));
		dividerPaint.setStrokeWidth(2);
		
	}

/* Utility Method to be invoked when trying to set
 * the color from outside
 */
	
  public void setColor(int color) {
	  dividerPaint.setColor(color);
	  trackPaint.setColor(color);
	  this.invalidate();
  }
	
	

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
	
      int width = getWidth();
      int height = getHeight();
      int division = getHeight()/6;
      //canvas.drawRect(width/2-10, 0, width/2+10,height, paint);
      canvas.drawLine(width/2-10,0,width/2-10,height, trackPaint);
      canvas.drawLine(width/2+10,0,width/2+10,height, trackPaint);
      
      canvas.drawLine(width/2-20,division,width/2+20, division, dividerPaint);
      canvas.drawLine(width/2-20,2*division,width/2+20, 2*division, dividerPaint);
      canvas.drawLine(width/2-20,3*division,width/2+20, 3*division, dividerPaint);
      canvas.drawLine(width/2-20,4*division,width/2+20, 4*division, dividerPaint);
      canvas.drawLine(width/2-20,5*division,width/2+20, 5*division, dividerPaint);
	}
	
	
	
}
