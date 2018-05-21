package com.tattleup.app.tattleup;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

public class SplashScreensActivity extends AppCompatActivity {


	private TextView mLogo;
	private TextView welcomeText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_NO_TITLE); //Removing ActionBar
		getSupportActionBar().hide();
		setContentView(R.layout.activity_splash_screen);

		mLogo = (TextView) findViewById(R.id.logo);
		welcomeText = (TextView) findViewById(R.id.welcome_text);
//
//		String category = SPLASH_SCREEN_OPTION_1;
//		Bundle extras = getIntent().getExtras();
//		if (extras != null && extras.containsKey(SPLASH_SCREEN_OPTION)) {
//			category = extras.getString(SPLASH_SCREEN_OPTION, SPLASH_SCREEN_OPTION_1);
//		}
		setAnimation();
	}
	
	/** Animation depends on category.
	 * */
	private void setAnimation() {
//			mKenBurns.setImageResource(R.drawable.gradient_background_media);
			animation1();
//		} else if (category.equals(SPLASH_SCREEN_OPTION_2)) {
//			mLogo.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.main_color_500));
//			mKenBurns.setImageResource(R.drawable.background_shop);
//			animation2();
//		} else if (category.equals(SPLASH_SCREEN_OPTION_3)) {
//			mKenBurns.setImageResource(R.drawable.splash_screen_option_three);
//			animation2();
//			animation3();
//		}
	}

	private void animation1() {
		ObjectAnimator scaleXAnimation = ObjectAnimator.ofFloat(mLogo, "scaleX", 5.0F, 1.0F);
		scaleXAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
		scaleXAnimation.setDuration(300);
		ObjectAnimator scaleYAnimation = ObjectAnimator.ofFloat(mLogo, "scaleY", 5.0F, 1.0F);
		scaleYAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
		scaleYAnimation.setDuration(300);
		ObjectAnimator alphaAnimation = ObjectAnimator.ofFloat(mLogo, "alpha", 0.0F, 1.0F);
		alphaAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
		alphaAnimation.setDuration(300);
		ObjectAnimator alphaAnimation1 = ObjectAnimator.ofFloat(welcomeText, "alpha", 0.0F, 1.0F);
		alphaAnimation1.setStartDelay(300);
		alphaAnimation1.setDuration(300);
		AnimatorSet animatorSet = new AnimatorSet();
		animatorSet.play(scaleXAnimation).with(scaleYAnimation).with(alphaAnimation).with(alphaAnimation1);
		animatorSet.setStartDelay(300);
		animatorSet.start();

		Thread background = new Thread() {
			public void run() {

				try {
					// Thread will sleep for 5 seconds
					sleep(3*1000);

					// After 5 seconds redirect to another intent
					Intent i=new Intent(getBaseContext(),RequestPermissionActivity.class);
					startActivity(i);

					//Remove activity
					finish();

				} catch (Exception e) {

				}
			}
		};

		// start thread
		background.start();

	}
	
	private void animation2() {
		mLogo.setAlpha(1.0F);
		Animation anim = AnimationUtils.loadAnimation(this, R.anim.translate_top_to_center);
		mLogo.startAnimation(anim);
	}
	
	private void animation3() {
		ObjectAnimator alphaAnimation = ObjectAnimator.ofFloat(welcomeText, "alpha", 0.0F, 1.0F);
		alphaAnimation.setStartDelay(1700);
		alphaAnimation.setDuration(500);
		alphaAnimation.start();
	}
}
