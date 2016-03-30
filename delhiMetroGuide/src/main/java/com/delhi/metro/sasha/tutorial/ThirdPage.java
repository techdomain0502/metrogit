package com.delhi.metro.sasha.tutorial;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.delhi.metro.sasha.R;

public class ThirdPage extends Fragment implements OnClickListener{
	 private Button nextButton,previousButton;
     private CustomButtonCallBacks callBackListener; 
	  
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
    		Bundle savedInstanceState) {
    	View v = inflater.inflate(R.layout.thirdpage, null, false);
    	nextButton = (Button)v.findViewById(R.id.finish);
    	previousButton = (Button)v.findViewById(R.id.previous);
    	nextButton.setOnClickListener(this);
    	previousButton.setOnClickListener(this);
    	return v;
    }

	 @Override
     public void onActivityCreated(Bundle savedInstanceState) {
     	// TODO Auto-generated method stub
     	super.onActivityCreated(savedInstanceState);
     	if(getActivity() instanceof TutorialScreen)
     		callBackListener = (CustomButtonCallBacks) getActivity();
     }
		@Override
		public void onClick(View v) {
          int id = v.getId();
         switch (id) {
			case R.id.finish:
				 SharedPreferences pref = getActivity().getSharedPreferences("metro",getActivity().MODE_PRIVATE);
				 SharedPreferences.Editor editor  = pref.edit();
				 editor.putBoolean("first_launch",false);
				 editor.commit();
				callBackListener.onNextClicked();
				break;
			case R.id.previous:
				callBackListener.onPreviousClicked();
			default:
				break;
			}
		}
      

}
