package com.murat.altindoviz;

import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.widget.RemoteViews;

import com.murat.altindoviz.webservice.DataService;
import com.murat.altindoviz.webservice.IWsdl2CodeEvents;

public class UpdateWidgetService extends Service implements IWsdl2CodeEvents{

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

	public void updateValues(int[] allWidgetIds, Context context,
			Kurlar altin) {

		AppWidgetManager appWidgetManager = AppWidgetManager
				.getInstance(context);

		for (int widgetId : allWidgetIds) {

			RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
					R.layout.main);

			if (altin != null && altin.getList().size() > 0) {

				String[] values = altin.getValues();
			
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
	public void Wsdl2CodeFinished(String methodName, Kurlar data) {
		
		if (data != null) {
			updateValues(allWidgetIds, context, data);
		}

		updateWidget("Yenile");
		
	}

}
