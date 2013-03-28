package com.murat.altindoviz;

import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.IBinder;
import android.widget.RemoteViews;

public class UpdateWidgetService extends Service {

	public Intent intent;
	public Context context;
	int[] allWidgetIds;

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		allWidgetIds = intent
				.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS);
		context = this.getApplicationContext();
		
		if (isOnline()) {
			DownloadAsync download = new DownloadAsync();
			download.execute("http://xml.altinkaynak.com.tr/altinkaynak.xml");
		}

		stopSelf();
		return super.onStartCommand(intent, flags, startId);
	}

	private class DownloadAsync extends AsyncTask<String, Void, AltinKaynak> {

		@Override
		protected void onPreExecute() {
			updateWidget("Yükleniyor");
		}

		@Override
		protected AltinKaynak doInBackground(String... params) {

			AltinKaynak altin = null;
			try {
				altin = DownloadManager.getAltinKaynak(params[0]);
			} catch (Exception e) {

				e.printStackTrace();
				altin = null;
			}
			return altin;
		}

		@Override
		protected void onPostExecute(AltinKaynak result) {

			if (result != null) {
				updateValues(allWidgetIds, context, result);
			}

			updateWidget("Yenile");
		}
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
			AltinKaynak altin) {

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

}
