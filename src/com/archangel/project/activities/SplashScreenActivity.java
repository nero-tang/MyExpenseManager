package com.archangel.project.activities;

import com.archangel.project.R;
import com.archangel.project.data.CommonData;
import com.archangel.project.db.MyDBInfo;
import com.archangel.project.db.MyDBTools;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;

public class SplashScreenActivity extends Activity {
	private static int SPLASH_TIME_OUT = 1500;
	public static MyDBTools dbTools = null;
	public static Resources mResources = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		dbInitialize();
		CommonData.getInstance().load();
		
		
		new Handler().postDelayed(new Runnable() {
			 
            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */
 
            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                Intent i = new Intent(SplashScreenActivity.this, MainActivity.class);
                startActivity(i);
 
                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);
		
	}
	
	public void dbInitialize() {
		dbTools = MyDBTools.getInstance(this.getApplicationContext());
		mResources = this.getApplicationContext().getResources();
		
		dbTools.open();
		
		Cursor cursor = dbTools.select(MyDBInfo.getTableNames()[0], MyDBInfo.getFieldNames()[0], null, null, null, null, null);
		if (cursor.moveToNext()) {
			cursor.close();
			return;
		}
		
		String[] nameArr = mResources.getStringArray(R.array.TBL_EXPENSE_CATEGORY);
		String[] colorArr = mResources.getStringArray(R.array.EXPENSE_CATEGORY_COLOR);
		
		for (int i = 0; i < nameArr.length; i++) {
			dbTools.insert("TBL_EXPENDITURE_CATEGORY", new String[] { "NAME", "BUDGET", "COLOR" }, new String[]{ nameArr[i], "0", colorArr[i]});
		}
		
		cursor.close();
	}
	
}