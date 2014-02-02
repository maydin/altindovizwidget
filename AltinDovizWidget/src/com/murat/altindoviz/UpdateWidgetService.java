package com.murat.altindoviz;

import java.util.Random;

import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.widget.RemoteViews;

import com.murat.altindoviz.webservice.DataService;
import com.murat.altindoviz.webservice.IWsdl2CodeEvents;

public class UpdateWidgetService extends Service implements IWsdl2CodeEvents {

	public Intent intent;
	public Context context;
	int[] allWidgetIds;

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		allWidgetIds = intent
				.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS);
		context = this.getApplicationContext();

		if (isOnline()) {

			DataService service = new DataService(this);
			try {
				service.GetGoldAndCurrencyAsync();
			} catch (Exception e) {
				updateWidget("Yenile");
			}
		}

		stopSelf();
		return super.onStartCommand(intent, flags, startId);
	}

	public boolean isOnline() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnected()) {
			return true;
		}
		return false;
	}

	public void updateValues(int[] allWidgetIds, Context context, Kurlar altin) {

		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = preferences.edit();
		
		AppWidgetManager appWidgetManager = AppWidgetManager
				.getInstance(context);

		for (int widgetId : allWidgetIds) {

			RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
					R.layout.main);

			if (altin != null && altin.getList().size() > 0) {

				String[] values = altin.getValues();
//				String[] values = new String[7];
//				Random r = new Random();
//				int num = r.nextInt(100);
//				values[0] = String.valueOf(num);
//				values[1] = String.valueOf(num+1);
//				num = r.nextInt(100);
//				values[2] = String.valueOf(num);
//				values[3] = String.valueOf(num+1);
//				num = r.nextInt(100);
//				values[4] = String.valueOf(num);
//				values[5] = String.valueOf(num+1);
				
				
				float previousAltinAlis = Float.parseFloat(preferences.getString("altinAlis", "0"));
				float previousDolarAlis = Float.parseFloat(preferences.getString("dolarAlis", "0"));
				float previousEuroAlis = Float.parseFloat(preferences.getString("euroAlis", "0"));
				float previousAltinSatis = Float.parseFloat(preferences.getString("altinSatis", "0"));
				float previousDolarSatis = Float.parseFloat(preferences.getString("dolarSatis", "0"));
				float previousEuroSatis = Float.parseFloat(preferences.getString("euroSatis", "0"));
				float currentAltinAlis = Float.parseFloat(values[4]);
				float currentDolarAlis = Float.parseFloat(values[0]);
				float currentEuroAlis = Float.parseFloat(values[2]);
				float currentAltinSatis = Float.parseFloat(values[5]);
				float currentDolarSatis = Float.parseFloat(values[1]);
				float currentEuroSatis = Float.parseFloat(values[3]);
				
				if(previousAltinAlis > 0)
				{
					int drawableId;
					if(previousAltinAlis > currentAltinAlis || previousAltinSatis > currentAltinSatis)
						drawableId = R.drawable.ic_downarrow;
					else if(previousAltinAlis < currentAltinAlis || previousAltinSatis < currentAltinSatis)
						drawableId = R.drawable.ic_uparrow;
					else
						drawableId =  R.drawable.ic_equal;
					
					remoteViews.setImageViewResource(R.id.altinArrow,drawableId);
				}
				
				if(previousDolarAlis > 0)
				{
					int drawableId;
					if(previousDolarAlis > currentDolarAlis || previousDolarSatis > currentDolarSatis)
						drawableId = R.drawable.ic_downarrow;
					else if(previousDolarAlis < currentDolarAlis ||  previousDolarSatis < currentDolarSatis)
						drawableId = R.drawable.ic_uparrow;
					else
						drawableId =  R.drawable.ic_equal;
					
					remoteViews.setImageViewResource(R.id.dolarArrow,drawableId);
				}
				
				if(previousEuroAlis > 0)
				{
					int drawableId;
					if(previousEuroAlis > currentEuroAlis || previousEuroSatis > currentEuroSatis)
						drawableId = R.drawable.ic_downarrow;
					else if(previousEuroAlis < currentEuroAlis || previousEuroSatis < currentEuroSatis)
						drawableId = R.drawable.ic_uparrow;
					else
						drawableId =  R.drawable.ic_equal;
					
					remoteViews.setImageViewResource(R.id.euroArrow,drawableId);
				}
				
				
				editor.putString("dolarAlis", values[0]);
				editor.putString("euroAlis", values[2]);
				editor.putString("altinAlis", values[4]);
				editor.putString("dolarSatis", values[1]);
				editor.putString("euroSatis", values[3]);
				editor.putString("altinSatis", values[5]);
		        editor.commit();
		        
				remoteViews.setTextViewText(R.id.textViewDolarAlis, values[0]);
				remoteViews.setTextViewText(R.id.textViewDolarSatis, values[1]);

				remoteViews.setTextViewText(R.id.textViewEuroAlis, values[2]);
				remoteViews.setTextViewText(R.id.textViewEuroSatis, values[3]);

				remoteViews.setTextViewText(R.id.textViewAltinAlis, values[4]);
				remoteViews.setTextViewText(R.id.textViewAltinSatis, values[5]);

				remoteViews.setTextViewText(R.id.textViewUpdateTime, values[6]);
			}

			appWidgetManager.updateAppWidget(widgetId, remoteViews);
		}
	}

	public void updateWidget(String text) {
		AppWidgetManager appWidgetManager = AppWidgetManager
				.getInstance(context);

		for (int widgetId : allWidgetIds) {

			RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
					R.layout.main);
			remoteViews.setTextViewText(R.id.buttonRefresh, text);
			appWidgetManager.updateAppWidget(widgetId, remoteViews);
		}
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void Wsdl2CodeStartedRequest() {
		updateWidget("Yükleniyor");

	}

	@Override
	public void Wsdl2CodeFinished(String methodName, Object Data) {
		// TODO Auto-generated method stub

	}

	@Override
	public void Wsdl2CodeFinishedWithException(Exception ex) {
		updateWidget("Yenile");

	}

	@Override
	public void Wsdl2CodeEndedRequest() {
		// TODO Auto-generated method stub

	}

	@Override
	public void Wsdl2CodeAppFinished(String methodName, Kurlar data) {

		if (data != null) {
			updateValues(allWidgetIds, context, data);
		}

		updateWidget("Yenile");

	}

}
