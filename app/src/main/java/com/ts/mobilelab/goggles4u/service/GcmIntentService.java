package com.ts.mobilelab.goggles4u.service;

import org.json.JSONObject;

import com.ts.mobilelab.goggles4u.PrescriptionAddActivity;
import com.ts.mobilelab.goggles4u.R;
import com.ts.mobilelab.goggles4u.data.PreferenceData;


import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

public class GcmIntentService extends IntentService {

	Bundle extras;
	//NotificationManager mNotificationManager;
	String push_Response;
	JSONObject jsonObject;
	private String mRequestFor;
	PreferenceData mPreferenceData;

	public GcmIntentService() {
		super("");
		mPreferenceData = new PreferenceData();
		// TODO Auto-generated constructor stub
	}
	@Override
	protected void onHandleIntent(Intent intent) {
		
		Log.v("in service","onHandleIntent");
		extras = intent.getExtras();

		if (extras == null) {
			WakefulBroadcastReceiver.completeWakefulIntent(intent);
			return;
		}
		
		// get the push message
				push_Response = extras.getString("message");
				Log.v("gcm push_Response",""+push_Response);
				if (push_Response == null) {
					WakefulBroadcastReceiver.completeWakefulIntent(intent);
					return;
				}else{
					if(mPreferenceData.isLogincheck()){
						sendNotification(push_Response);
					}
					
				}
				
				
	}
	private void sendNotification(String notificationDetails) {
		// Create an explicit content Intent that starts the main Activity.
		Intent notificationIntent = new Intent(getApplicationContext(), PrescriptionAddActivity.class);
		notificationIntent.putExtra("msg","fromgcm");
		notificationIntent.putExtra("msg",push_Response);
		
		// Construct a task stack.
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);

		// Add the main Activity to the task stack as the parent.
		stackBuilder.addParentStack(PrescriptionAddActivity.class);

		// Push the content Intent onto the stack.
		stackBuilder.addNextIntent(notificationIntent);

		// Get a PendingIntent containing the entire back stack.
		PendingIntent notificationPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_ONE_SHOT);

		// Get a notification builder that's compatible with platform versions
		// >= 4
		NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
		Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
		builder.setSound(alarmSound);
		// Define the notification settings.
		builder.setSmallIcon(R.drawable.ic_addfriend_circle_red_72)
				// In a real app, you may want to use a library like Volley
				// to decode the Bitmap.
				.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_cloud_off_24dp)).setColor(Color.RED)
				.setSound(alarmSound).setContentTitle(notificationDetails)
				.setContentText(getString(R.string.actions))
				.setContentIntent(notificationPendingIntent);

		// Dismiss notification once the user touches it.
		builder.setAutoCancel(true);

		// Get an instance of the Notification manager
		NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

		// Issue the notification
		mNotificationManager.notify(0, builder.build());
	}

}
