package com.zaucetech.lasergunhd;

import android.media.*;
import android.media.SoundPool.OnLoadCompleteListener;
import android.net.Uri;
import android.preference.PreferenceManager;

import android.app.PendingIntent;
 
import android.appwidget.AppWidgetManager;
 
import android.appwidget.AppWidgetProvider;
 
import android.content.Context;
import android.content.SharedPreferences;
 
import android.content.Intent;
 
import android.util.Log;
import android.widget.RemoteViews;

public class Widgetlaser extends AppWidgetProvider {
	public static String ACTION_WIDGET_CONFIGURE = "ConfigureWidget";
	 
    public static String ACTION_WIDGET_RECEIVER = "ActionReceiverWidget";

    private MediaPlayer mPlay = null;
    public int ionbeam;
    public SoundPool sp;
    public String songchoice;
    boolean loaded = false;
    
    

   

@Override

public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {	
	
	RemoteViews remoteViews = new RemoteViews("com.zaucetech.lasergunhd", R.layout.widgetmain);
	
	loaded = false;
	 
    Intent active = new Intent(context, Widgetlaser.class);

    active.setAction(ACTION_WIDGET_RECEIVER);

    PendingIntent actionPendingIntent = PendingIntent.getBroadcast(context, 0, active, 0);

    remoteViews.setOnClickPendingIntent(R.id.gunwidg, actionPendingIntent);
    
//    sp = new SoundPool(8, AudioManager.STREAM_MUSIC, 0);
//    ionbeam = sp.load(context, R.raw.ionbeam, 1);

    appWidgetManager.updateAppWidget(appWidgetIds, remoteViews);  
    

    }



@Override

public void onReceive(final Context context, Intent intent) {	
	
	sp = new SoundPool(8, AudioManager.STREAM_MUSIC, 0);	
	ionbeam = sp.load(context, R.raw.ionogg, 1);
	Log.d("sdf", "loaded");
	loaded = true;	
	
	final String action = intent.getAction();
	 if (AppWidgetManager.ACTION_APPWIDGET_DELETED.equals(action)) {        
		 
         //The widget is being deleted off the desktop
		 	 

         final int appWidgetId = intent.getExtras().getInt(

                         AppWidgetManager.EXTRA_APPWIDGET_ID,

                         AppWidgetManager.INVALID_APPWIDGET_ID);

         	if (appWidgetId != AppWidgetManager.INVALID_APPWIDGET_ID) {

                 this.onDeleted(context, new int[] { appWidgetId });
                 

         	}

	 	}else
	 
                // check, if our Action was called

            if (intent.getAction().equals(ACTION_WIDGET_RECEIVER)) {              		
            	       		

            	sp.setOnLoadCompleteListener(new OnLoadCompleteListener() {
            		
            		public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {            			
            			loaded = true;
            			sp.play(ionbeam, 1, 1, 0, 0, 1);  
            			Log.d("sdf", "played");	
            			try {
							Thread.sleep(220);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
            			sp.release();
            			
            			Log.d("sdf", "released");
            		}
            	});
            	
            	
            	

            } else {

            	          	

            }

           
	 super.onReceive(context, intent);
            

    }



}


