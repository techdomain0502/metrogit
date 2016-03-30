package com.delhi.metro.sasha.gui;

import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.delhi.metro.sasha.R;
import com.delhi.metro.sasha.tutorial.TutorialScreen;


public class WelcomeFragment extends Fragment implements OnClickListener {
	private Animation fadeIn = null;
//	private Animation fadeOut = null;
	private Animation translateUp = null;
	private Animation stretch = null;
	private Animation fadeOut = null;
	private LinearLayout mLogoLayout = null;
	private ViewGroup mStretchLayout = null;
	private LinearLayout mWelcomeTextLayout = null;
	private RelativeLayout mBottomRelLayout = null;
	private TextView mSvoiceTitle = null;
	private Button next;
	View content;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		content = inflater.inflate(R.layout.welcome_fragment_layout, null, false);
		init();
		return content;
	}
	private void init() {
		next = (Button)content.findViewById(R.id.next_button_layout);
		next.setOnClickListener(this);
		mSvoiceTitle = (TextView) getActivity().findViewById(R.id.svoice_title_welcomepage);
		mLogoLayout = (LinearLayout) content.findViewById(R.id.logo_layout);
		mBottomRelLayout =  (RelativeLayout) content.findViewById(R.id.bottom_layout);
		
		mWelcomeTextLayout = (LinearLayout) content.findViewById(R.id.welcome_text_layout);
		
		fadeIn = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in);
//		fadeIn.setAnimationListener(mFadeInListner);
		fadeIn.setStartOffset(500);
		fadeIn.setDuration(2000);
//		fadeOut = AnimationUtils.loadAnimation(this, R.anim.fade_out);
		stretch = AnimationUtils.loadAnimation(getActivity(), R.anim.stretch_anim);
		stretch.setAnimationListener(mStretchListener);
		fadeOut = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_out);
		fadeOut.setAnimationListener(mFadeOutListner);
		fadeOut.setDuration(900);
		mSvoiceTitle.startAnimation(fadeIn);
		mSvoiceTitle.setVisibility(View.VISIBLE);
		mLogoLayout.startAnimation(fadeIn);
		mLogoLayout.setVisibility(View.VISIBLE);
		
		
		
		translateUp = AnimationUtils.loadAnimation(getActivity(), R.anim.y_translation);
		translateUp.setStartOffset(800);
		mBottomRelLayout.startAnimation(translateUp);
		mBottomRelLayout.setVisibility(View.VISIBLE);
	}
	
	
private AnimationListener mStretchListener = new AnimationListener() {
		
		@Override
		public void onAnimationStart(Animation animation) {
			mWelcomeTextLayout.startAnimation(fadeOut);
			mLogoLayout.startAnimation(fadeOut);
			mSvoiceTitle.startAnimation(fadeOut);
		}
		
		@Override
		public void onAnimationRepeat(Animation animation) {

		}
		
		@Override
		public void onAnimationEnd(Animation animation) {
	    	getActivity().finish();
    	 Intent i = new Intent();
		 i.setComponent(new ComponentName(getActivity(),TutorialScreen.class));
		 startActivity(i);
		}
	};
	
	
	
	
	private AnimationListener mFadeOutListner = new AnimationListener() {
		@Override
		public void onAnimationStart(Animation animation) {
		}
		
		@Override
		public void onAnimationRepeat(Animation animation) {
		}
		
		@Override
		public void onAnimationEnd(Animation animation) {
			mWelcomeTextLayout.setAlpha(0f);
			mWelcomeTextLayout.setVisibility(View.GONE);
			mLogoLayout.setVisibility(View.GONE);
			mSvoiceTitle.setVisibility(View.GONE);
		}
	};
	@Override
	public void onClick(View v1) {
		View v = this.getActivity().findViewById(R.id.stretch_layout_1);
		v.setVisibility(View.VISIBLE);
		v.startAnimation(stretch);
	}
}
