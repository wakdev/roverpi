package com.wakdev.roverpi;

// Import
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

/**
 * RpiStream (Async task)
 * @author wakdev.com
 * @version 1.0
 */
public class RpiStream extends AsyncTask<String, Void, Bitmap> {

	// Log tag
	private static final String LOG_TAG = "RoverPiLog";

	// vars
	private final WeakReference<ImageView> imageViewReference;
	private String streamURL;
	
	/**
	 * Constructor
	 * @param imageView Image view
	 */
	public RpiStream(ImageView imageView) {
		imageViewReference = new WeakReference<>(imageView);
	}

	/**
	 * Launch Stream
	 * @param url Stream URL
	 * @return Bitmap Stream image
	 */
	public Bitmap launchStream(String url) {
		Bitmap bitmap = null;
		InputStream inputStream = null;
		try {
			URL urlImage = new URL(url);
			HttpURLConnection connection = (HttpURLConnection) urlImage.openConnection();
			inputStream = connection.getInputStream();
			bitmap = BitmapFactory.decodeStream(inputStream);
		} catch (IOException e) {
			Log.e(LOG_TAG, "RpiStream : Stream Error");
			e.printStackTrace();
		}finally {
            if (inputStream != null) {
                try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}  
            }
        }
		return bitmap;
	}

	/**
	 * Execute Async Task
	 * @param bitmap image
	 */
	@Override
	protected void onPostExecute(Bitmap bitmap) {
		Log.v("Stream", "onPostExecute");
		if (imageViewReference != null) {
            ImageView imageView = imageViewReference.get();
            if (imageView != null) {
                imageView.setImageBitmap(bitmap);
                //New Task
                RpiStream task = new RpiStream(imageView);
                task.execute(streamURL);
            }
        }
	}
	
	/**
	 * In background
	 * @return Bitmap Image
	 */
	@Override
	protected Bitmap doInBackground(String... params) {
		streamURL = params[0];
		return launchStream(streamURL);
	}

}
