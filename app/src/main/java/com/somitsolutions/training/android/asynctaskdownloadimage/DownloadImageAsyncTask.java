package com.somitsolutions.training.android.asynctaskdownloadimage;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.InputStream;

public class DownloadImageAsyncTask extends AsyncTask<String, Integer, Bitmap> {
	
	private CallBack cb;
	Context context;
	DownloadImageAsyncTask( CallBack cb){
	
		this.cb = cb;
	}
	
	protected Bitmap doInBackground(String... params){
			//android.os.Debug.waitForDebugger();
			publishProgress();
			return downloadBitmap(params[0]);
	}

	protected void onProgressUpdate(Integer... progress){

		cb.onProgress();

	}

	protected void onPostExecute(Bitmap result){

		cb.onResult(result);

	}
	
	protected void onCancelled(){
		cb.onCancel();
	}
	
	
	private Bitmap downloadBitmap(String url) { 
		
		final DefaultHttpClient client = new DefaultHttpClient(); 
		
		final HttpGet getRequest = new HttpGet(url); 
		
		try { 
			HttpResponse response = client.execute(getRequest); 
			final int statusCode = response.getStatusLine().getStatusCode(); 
			if (statusCode != HttpStatus.SC_OK) {
				return null; 
				} 
			
			final HttpEntity entity = response.getEntity();
			
			if (entity != null) { 
				InputStream inputStream = null;
				try {
					inputStream = entity.getContent();
					Options optionSample = new BitmapFactory.Options();
					optionSample.inSampleSize = 4; // Or 8 for smaller image
					Bitmap bitmap = BitmapFactory.decodeStream(inputStream,null,optionSample);
					return bitmap;
					
				} finally { 
					if (inputStream != null) { 
						inputStream.close(); 
						} 
					//entity.consumeContent(); 
					} 
				} 
			} catch (Exception e) {
				getRequest.abort();
				Log.e("AsynctaskImageDownload", e.toString());
				} 
		return null;
	}

}
