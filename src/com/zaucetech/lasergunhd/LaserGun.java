package com.zaucetech.lasergunhd;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class LaserGun extends Activity {
    /** Called when the activity is first created. */
	SoundPool sp;
	int ionbeam = 0;
	int deathray = 0;
	int lasercannon = 0;
	int pew = 0;
	int startup1 = 0;
	int startup2 = 0;
	int longpress = 0;
	int levelupsound = 0;
	int finalsound = 0;
	int soundchoice;
	Vibrator vib;
	int duration;
	String color;
	boolean vibratemode;
	String level;
	int corecount;
	int prevcorecount=0;
	int redcorecount;
	int bluecorecount;
	int greencorecount;
	TextView bluetext;
	TextView greentext;
	TextView redtext;
	TextView leveltext;
	Button b;
	Button s;
	Dialog levelup;
	Dialog prereq;
	boolean shown1;
	boolean shown2;
	boolean shown3;
	boolean shown4;
	boolean shown5;
	boolean shown6;
	boolean started = false;
	long startTime;
	int cpm;
	TextView cpmview;
	boolean invisible;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
//      call the apprater function        
      AppRater.app_launched(this);        
//      AppRater.showRateDialog(this, null);     // use this for testing
      	
//      timer    
      	cpmview = (TextView) findViewById(R.id.textView6);
      	cpmview.setText(String.valueOf(cpm)+" cpm");
      	corecount=prevcorecount=0;
      	new Timer().scheduleAtFixedRate(new TimerTask() {      		
            public void run() {            	
                cpm = (corecount - prevcorecount)*60;                
                prevcorecount = corecount;
            }
        }, 0, 1000);
      	
      
//      load stuff
      	
      	
      	levelup = new Dialog(this);
      	levelup.setContentView(R.layout.custom_dialog);
      
            
        final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        
        sp = new SoundPool(8, AudioManager.STREAM_MUSIC, 0);
        ionbeam = sp.load(this, R.raw.ionbeam, 1);
        deathray = sp.load(this, R.raw.deathray, 1);
        lasercannon = sp.load(this, R.raw.lasercannon, 1);
        pew = sp.load(this, R.raw.pew, 1);
        startup1 = sp.load(this, R.raw.longpressfiring, 1);
        startup2 = sp.load(this, R.raw.startup2, 1);
        longpress = sp.load(this, R.raw.longpress, 1);
        levelupsound = sp.load(this, R.raw.levelup, 1);
        finalsound = sp.load(this, R.raw.finals, 1);
        
        
        
       
        soundchoice = settings.getInt("sval", ionbeam);
        duration = settings.getInt("dval", 20);
        color = settings.getString("lasercolor", "3");
        shown1 = settings.getBoolean("shown1", false);
        shown2 = settings.getBoolean("shown2", false);
        shown3 = settings.getBoolean("shown3", false);
        shown4 = settings.getBoolean("shown4", false);
        shown5 = settings.getBoolean("shown5", false);
        shown6 = settings.getBoolean("shown6", false);
        
        
        
        int orientation = getResources().getConfiguration().orientation;
        
        b = (Button) findViewById(R.id.button1);
        if(orientation == android.content.res.Configuration.ORIENTATION_PORTRAIT){        	
    		if (soundchoice== lasercannon){
    			b.setBackgroundResource(R.drawable.redselector);
    		}else
    		if (soundchoice== deathray){
    			b.setBackgroundResource(R.drawable.greenselector);
    		}else
    		if (soundchoice== ionbeam){
    			b.setBackgroundResource(R.drawable.selector);
    		}
        }else
        	if(orientation == android.content.res.Configuration.ORIENTATION_LANDSCAPE){            	
        		if (soundchoice== lasercannon){
        			b.setBackgroundResource(R.drawable.redcanselector);
        		}else
        		if (soundchoice== deathray){
        			b.setBackgroundResource(R.drawable.greencanselector);
        		}else
        		if (soundchoice== ionbeam){
        			b.setBackgroundResource(R.drawable.canselector);
        		}
            }
        corecount = settings.getInt("cores", 0); 
        redcorecount = settings.getInt("redcores", 0);
        bluecorecount = settings.getInt("bluecores", 0);
        greencorecount = settings.getInt("greencores", 0);
        level = settings.getString("level", "Learner");
        bluetext = (TextView) findViewById(R.id.textView4);
        bluetext.setTextColor(Color.BLUE);
        bluetext.setText(String.valueOf(bluecorecount));
        greentext = (TextView) findViewById(R.id.textView2);
        greentext.setTextColor(Color.GREEN);
        greentext.setText(String.valueOf(greencorecount));
        redtext = (TextView) findViewById(R.id.textView3);
        redtext.setTextColor(Color.RED);
        redtext.setText(String.valueOf(redcorecount));
        leveltext = (TextView) findViewById(R.id.textView1);
        
        
        leveltext.setText(level + " : " + String.valueOf(corecount));
        
        vib = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        
        
        
     // Get the AudioManager
        AudioManager audioManager = 
        (AudioManager)this.getSystemService(Context.AUDIO_SERVICE);
        // Set the volume of played media to maximum.
        audioManager.setStreamVolume (
        AudioManager.STREAM_MUSIC,
        audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC),
        0);
        
        
//        
        final MediaPlayer mp = MediaPlayer.create(this, R.raw.lazor);
        b.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
//				play sound and vibrate
				sp.autoPause();
				vibratemode = settings.getBoolean("vibrate", true);				
				if (vibratemode==false){
					sp.play(soundchoice, 1, 1, 0, 0, 1);
				}else {	
					sp.play(soundchoice, 1, 1, 0, 0, 1);				
					vib.vibrate(duration);					
				}
				
//				count the cores shot
				if (soundchoice== lasercannon){
	    			redcorecount = redcorecount + 3;
	    			redtext.setText(String.valueOf(redcorecount));
	    		}else
	    		if (soundchoice == deathray){	    			
	    			greencorecount = greencorecount + 2;
	    			greentext.setText(String.valueOf(greencorecount));
	    		}else
	    		if (soundchoice == ionbeam){
	    			bluecorecount++;
	    			bluetext.setText(String.valueOf(bluecorecount));    			
	    		}
				corecount = bluecorecount + redcorecount + greencorecount;
				if (corecount >= 10 && !shown1){
					level = "Space Cadet";					
					levelup.setTitle("Level Up!");					
					TextView textlvl = (TextView) levelup.findViewById(R.id.text);
					TextView text = (TextView) levelup.findViewById(R.id.TextView01);
			      	text.setText("Your Laser Skill Has Advanced To: ");	
			      	textlvl.setText("Space Cadet! - You've Unlocked Death Ray!");
//			      	sp.play(finalsound, 1, 1, 0, 0, 1);
	        		levelup.show();	
	        		
	        		shown1 = true;
				}
				if (corecount >= 100 && !shown2){
					level = "Laser Specialist";					
					levelup.setTitle("Level Up!");					
					TextView textlvl = (TextView) levelup.findViewById(R.id.text);
					TextView text = (TextView) levelup.findViewById(R.id.TextView01);
			      	text.setText("Your Laser Skill Has Advanced To: ");	
			      	textlvl.setText("Laser Specialist! - You've Unlocked Impluse Cannon!");
//			      	sp.play(finalsound, 1, 1, 0, 0, 1);
	        		levelup.show();	
	        		
	        		shown2 = true;
				}								
				if (corecount >= 250 && !shown5){
					level = "Galactic Destroyer";					
					levelup.setTitle("Level Up!");					
					TextView textlvl = (TextView) levelup.findViewById(R.id.text);
					TextView text = (TextView) levelup.findViewById(R.id.TextView01);
			      	text.setText("Your Laser Skill Has Advanced To: ");	
			      	textlvl.setText("Galactic Destroyer!");
//			      	sp.play(finalsound, 1, 1, 0, 0, 1);
	        		levelup.show();
	        		
	        		shown5 = true;
				}
				if (corecount >= 400 && !shown6){
					level = "Laser Master";					
					levelup.setTitle("Level Up!");					
					TextView textlvl = (TextView) levelup.findViewById(R.id.text);
					TextView text = (TextView) levelup.findViewById(R.id.TextView01);
			      	text.setText("Your Laser Skill Has Advanced To: ");	
			      	textlvl.setText("Laser Master!                                  You have mastered Laser Gun HD!  Your Enemies Shall Fall Before You!"); 
			      	levelup.show();
			      	sp.play(finalsound, 1, 1, 0, 0, 1);
	        		
	        		shown6 = true;
				}
				if (corecount >= 10000 && !shown3){
					level = "Laser God";					
					levelup.setTitle("WOW!");					
					TextView textlvl = (TextView) levelup.findViewById(R.id.text);
					TextView text = (TextView) levelup.findViewById(R.id.TextView01);
			      	text.setText("Your Laser Skill Has Advanced To: ");	
			      	textlvl.setText("Laser God! All Of Teh Lazors Are Belong To You!");
	        		levelup.show();	        		
	        		mp.start();
	        		
	        		shown3 = true;
				}
				
				leveltext.setText(level + " : " + String.valueOf(corecount));	
				cpmview.setText(String.valueOf(cpm)+" cpm");
                
				
				
			}
		});
        b.setOnLongClickListener(new OnLongClickListener() {
			
			public boolean onLongClick(View v) {
				// TODO Auto-generated method stub	
								
				sp.autoResume();				
				sp.play(longpress, 1, 1, 0, -1, 1);	
				
				return false;
			}
		});
        
        
    }



//  options
  @Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.options, menu);
	    return true;
	}
  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
	  int orientation = getResources().getConfiguration().orientation;
      switch (item.getItemId()) {
      case R.id.ionbeam:   
    	 
    	  soundchoice = ionbeam;
    	  duration = 20;
    	  color = "3";
    	  
    	  if(orientation == android.content.res.Configuration.ORIENTATION_PORTRAIT){
    	  b.setBackgroundResource(R.drawable.selector);
    	  }else
    		  if(orientation == android.content.res.Configuration.ORIENTATION_LANDSCAPE){
    			  b.setBackgroundResource(R.drawable.canselector);
    		  }
    	  
    	  
    	  	
    	  break;          
      case R.id.deathray:
    	  if (corecount >9){
    	  soundchoice = deathray;
    	  duration = 50;
    	  color = "2";
    	  
    	  if(orientation == android.content.res.Configuration.ORIENTATION_PORTRAIT){
    	  b.setBackgroundResource(R.drawable.greenselector);
    	  }else
    		  if(orientation == android.content.res.Configuration.ORIENTATION_LANDSCAPE){
    			  b.setBackgroundResource(R.drawable.greencanselector);
    		  }
    	  }
    		  else{    		 
    	    	  	levelup.setTitle("Required Level: ");
    	    	  	TextView text = (TextView) levelup.findViewById(R.id.text);
					TextView textlvl = (TextView) levelup.findViewById(R.id.TextView01);
			      	text.setText("");	
			      	textlvl.setText("Space Cadet - Requires 10 Cores Fired");
	        		levelup.show();	
    	    	  }
    	  break;
      case R.id.cannon:
    	  if (corecount >99){
    	  soundchoice = lasercannon;
    	  duration = 80;
    	  color = "1";
    	  
    	  if(orientation == android.content.res.Configuration.ORIENTATION_PORTRAIT){
    	  b.setBackgroundResource(R.drawable.redselector);
    	  }else
    		  if(orientation == android.content.res.Configuration.ORIENTATION_LANDSCAPE){
    			  b.setBackgroundResource(R.drawable.redcanselector);
    		  }
      }
    		  else{    		 
  	    	  	levelup.setTitle("Required Level: ");
  	    	  	TextView text = (TextView) levelup.findViewById(R.id.text);
					TextView textlvl = (TextView) levelup.findViewById(R.id.TextView01);
			      	text.setText("");	
			      	textlvl.setText("Laser Specialist - Requires 100 Cores Fired");
	        		levelup.show();	
  	    	  }
    	  break;
      case R.id.rate:
    	  startActivity(new Intent(LaserGun.this, Prefs.class));    	  
    	  break;
      case R.id.rating:  
    	  startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.zaucetech.lasergun")));
    	  break;
      case R.id.share:
    	  Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
          emailIntent.setType("text/plain");
          emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Laser Gun HD!");
          emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "I Just Fired " + corecount +" Cores as a " + level + " on Laser Gun HD! https://market.android.com/details?id=com.zaucetech.lasergun");
          startActivity(emailIntent);
          break;
      case R.id.reset:
    	  bluecorecount = 0;
    	  redcorecount = 0;
    	  greencorecount = 0;
    	  corecount = 0;
    	  leveltext.setText(level + " : " + String.valueOf(corecount));
    	  redtext.setText(String.valueOf(redcorecount));
    	  bluetext.setText(String.valueOf(bluecorecount));
    	  greentext.setText(String.valueOf(greencorecount));
    	  level = "Learner";
    	  break;
      case R.id.toggle: 
    	  if(!invisible){
    	  bluetext.setVisibility(4);
    	  redtext.setVisibility(4);
    	  greentext.setVisibility(4);
    	  leveltext.setVisibility(4);
    	  cpmview.setVisibility(4);
    	  TextView counttext = (TextView) findViewById(R.id.textView5);
    	  counttext.setVisibility(4);
    	  invisible = true;
    	  item.setTitle("Show Score");
    	  }else {
    		  bluetext.setVisibility(1);
        	  redtext.setVisibility(1);
        	  greentext.setVisibility(1);
        	  leveltext.setVisibility(1);
        	  cpmview.setVisibility(1);
        	  TextView counttext = (TextView) findViewById(R.id.textView5);
        	  counttext.setVisibility(1);
        	  invisible = false;
        	  item.setTitle("Hide Score");
    	  }
    	  
    	  break;  
      default:
          return super.onOptionsItemSelected(item);
      }
	return false;
  }
//   end options
  @Override
protected void onStop() {
	// TODO Auto-generated method stub
	super.onStop();
	SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
	SharedPreferences.Editor editor = settings.edit();
	editor.putInt("sval", soundchoice);
	editor.putInt("dval", duration);
	editor.putString("lasercolor", color);
	editor.putString("level", level);
	editor.putInt("cores", corecount);
	editor.putInt("bluecores", bluecorecount);
	editor.putInt("redcores", redcorecount);
	editor.putInt("greencores", greencorecount);
	editor.putBoolean("shown1", shown1);
	editor.putBoolean("shown2", shown2);
	editor.putBoolean("shown3", shown3);
	editor.putBoolean("shown4", shown4);
	editor.putBoolean("shown5", shown5);
	editor.putBoolean("shown6", shown6);
	editor.commit();
	
}
  @Override
protected void onPause() {
	// TODO Auto-generated method stub
	super.onPause();
	SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
	SharedPreferences.Editor editor = settings.edit();
	editor.putInt("sval", soundchoice);
	editor.putInt("dval", duration);
	editor.putString("lasercolor", color);
	editor.putString("level", level);	
	editor.putInt("cores", corecount);
	editor.putInt("bluecores", bluecorecount);
	editor.putInt("redcores", redcorecount);
	editor.putInt("greencores", greencorecount);
	editor.putBoolean("shown1", shown1);
	editor.putBoolean("shown2", shown2);
	editor.putBoolean("shown3", shown3);
	editor.putBoolean("shown4", shown4);
	editor.putBoolean("shown5", shown5);
	editor.putBoolean("shown6", shown6);
	editor.commit();
}
  
//THIS IS THE APP RATER SNIPPET I FOUND ONLINE TO GET PPL TO RATE IT   
public static class AppRater {
    private final static String APP_TITLE = "Laser Gun";
    private final static String APP_PNAME = "com.zaucetech.lasergun";
    
    private final static int DAYS_UNTIL_PROMPT = 3;
    private final static int LAUNCHES_UNTIL_PROMPT = 7;
    
    public static void app_launched(Context mContext) {
        SharedPreferences prefs = mContext.getSharedPreferences("apprater", 0);
        if (prefs.getBoolean("dontshowagain", false)) { return ; }
        
        SharedPreferences.Editor editor = prefs.edit();
        
        // Increment launch counter
        long launch_count = prefs.getLong("launch_count", 0) + 1;
        editor.putLong("launch_count", launch_count);

        // Get date of first launch
        Long date_firstLaunch = prefs.getLong("date_firstlaunch", 0);
        if (date_firstLaunch == 0) {
            date_firstLaunch = System.currentTimeMillis();
            editor.putLong("date_firstlaunch", date_firstLaunch);
        }
        
        // Wait at least n days before opening
        if (launch_count >= LAUNCHES_UNTIL_PROMPT) {
            if (System.currentTimeMillis() >= date_firstLaunch + 
                    (DAYS_UNTIL_PROMPT * 24 * 60 * 60 * 1000)) {
                showRateDialog(mContext, editor);
            }
        }
        
        editor.commit();
    }      
   
    
    public static void showRateDialog(final Context mContext, final SharedPreferences.Editor editor) {
        final Dialog dialog = new Dialog(mContext);
        dialog.setTitle("Rate " + APP_TITLE);

        LinearLayout ll = new LinearLayout(mContext);
        ll.setOrientation(LinearLayout.VERTICAL);
        
        TextView tv = new TextView(mContext);
        tv.setText("If you enjoy using " + APP_TITLE + ", please take a moment to rate it. Thanks for your support!");
        tv.setWidth(240);
        tv.setPadding(4, 0, 4, 10);
        ll.addView(tv);
        
        Button b1 = new Button(mContext);
        b1.setText("Rate " + APP_TITLE);
        b1.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + APP_PNAME)));
                dialog.dismiss();
            }
        });        
        ll.addView(b1);

        Button b2 = new Button(mContext);
        b2.setText("Remind me later");
        b2.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        ll.addView(b2);

        Button b3 = new Button(mContext);
        b3.setText("No, thanks");
        b3.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (editor != null) {
                    editor.putBoolean("dontshowagain", true);
                    editor.commit();
                }
                dialog.dismiss();
            }
        });
        ll.addView(b3);

        dialog.setContentView(ll);        
        dialog.show();        
    }
}
}




