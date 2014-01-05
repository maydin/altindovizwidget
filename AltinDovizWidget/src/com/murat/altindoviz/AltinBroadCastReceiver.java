package com.murat.altindoviz;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
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
		else//probably a resize because of the reload of launcher
		{
			RemoteViews remoteViews = init(context);
			
			callService(context);
			
			ComponentName thisWidget = new ComponentName(context,
					AltinBroadCastReceiver.class);

			AppWidgetManager.getInstance(context).updateAppWidget(thisWidget, remoteViews);
		}

		super.onReceive(context, intent);
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
