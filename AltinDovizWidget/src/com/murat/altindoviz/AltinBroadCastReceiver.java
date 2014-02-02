package com.murat.altindoviz;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.RemoteViews;

public class AltinBroadCastReceiver extends AppWidgetProvider {

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {

		RemoteViews remoteViews = init(context);
		
		callService(context);
		
		ComponentName thisWidget = new ComponentName(context,
				AltinBroadCastReceiver.class);

		appWidgetManager.updateAppWidget(thisWidget, remoteViews);
	}

	private RemoteViews init(Context context) {
		
		Intent intent = new Intent(context, AltinBroadCastReceiver.class);
		intent.setAction("click");
		RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
				R.layout.main);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0,
				intent, 0);
		remoteViews.setOnClickPendingIntent(R.id.buttonRefresh, pendingIntent);
		
		return remoteViews;
	}

	@Override
	public void onReceive(Context context, Intent intent) {

		if (intent.getAction().equals("click")) {

			callService(context);
		}
		else if(intent.getAction().equals(AppWidgetManager.ACTION_APPWIDGET_DELETED))
		{
			SharedPreferences preferences = PreferenceManager
					.getDefaultSharedPreferences(context);
			SharedPreferences.Editor editor = preferences.edit();
			editor.putString("dolarAlis", "0");
			editor.putString("euroAlis", "0");
			editor.putString("altinAlis", "0");
			editor.putString("dolarSatis", "0");
			editor.putString("euroSatis", "0");
			editor.putString("altinSatis", "0");
	        editor.commit();
			return;
		}
		else if(intent.getAction().equals(AppWidgetManager.ACTION_APPWIDGET_DISABLED))
		{
			Log.d("altin", "appwidget disabled");
			return;
		}
		else if(intent.getAction().equals(AppWidgetManager.ACTION_APPWIDGET_UPDATE))
		{
			RemoteViews remoteViews = init(context);
			
			callService(context);
			
			ComponentName thisWidget = new ComponentName(context,
					AltinBroadCastReceiver.class);

			AppWidgetManager.getInstance(context).updateAppWidget(thisWidget, remoteViews);
		}

	}

	
	private void callService(Context context) {
		ComponentName thisWidget = new ComponentName(context,
				AltinBroadCastReceiver.class);
		int[] allWidgetIds = AppWidgetManager.getInstance(context)
				.getAppWidgetIds(thisWidget);

		// Build the intent to call the service
		Intent intentService = new Intent(context.getApplicationContext(),
				UpdateWidgetService.class);
		intentService.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS,
				allWidgetIds);

		// Update the widgets via the service
		context.startService(intentService);
	}
}
