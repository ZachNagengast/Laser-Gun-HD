package com.zaucetech.lasergunhd;

import android.app.Activity;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RemoteViews;

public class Configure extends Activity {

	private Configure context;
	private int widgetID;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);		
		
		
		setContentView(R.layout.configure);
		setResult(RESULT_CANCELED);
		context=this;
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		
		Bundle extras = getIntent().getExtras();
		if(extras!=null);{
			widgetID = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, 
									AppWidgetManager.INVALID_APPWIDGET_ID);
		}
		final AppWidgetManager widgetManager = AppWidgetManager.getInstance(context);
		final RemoteViews remoteViews = new RemoteViews("com.zaucetech.lasergun", R.layout.widgetmain);
		Button ion = (Button) findViewById(R.id.button3);
		Button death = (Button) findViewById(R.id.button1);
		Button impulse = (Button) findViewById(R.id.button2);
		ion.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				String songchoice = "ion";
//				remoteViews.setTextViewText(R.id.song, songchoice);
				SharedPreferences.Editor prefs = context.getSharedPreferences("MYPREFS", MODE_WORLD_READABLE).edit();
			    prefs.putString("svalue", songchoice);
			    prefs.commit();
				widgetManager.updateAppWidget(widgetID, remoteViews); 
				
				Intent resultValue = new Intent();
				resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetID);
				setResult(RESULT_OK, resultValue);
				finish();
			}
		});
		
		death.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				
				
			}
		});
		
		impulse.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				
				
			}
		});
		
	}
}
