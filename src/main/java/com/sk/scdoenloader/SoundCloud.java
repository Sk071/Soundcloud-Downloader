package com.sk.scdoenloader;

import android.app.Activity;
import android.app.ProgressDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Build;
import android.os.AsyncTask;
import android.util.Log;
import android.view.MenuInflater;
import android.view.Menu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import java.util.List;
import java.util.ArrayList;
import android.net.Uri;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.content.res.AssetManager;
import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import android.content.ContentValues;
import android.content.ContentUris;
import java.io.FileOutputStream;
import java.io.File;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;


public class SoundCloud extends ActionBarActivity
{
	TextView tv;
	ListView lv;
	List<String> temp;
	private ProgressDialog dialog;
	public static String SCURL;
	ArrayAdapter adapter;
	CustomAdapter myadapter;
	Context ctx;
	String trackname;
	String streamurl;
	String downloadurl;
	DownloadFileFromURL dlfile;
	boolean isDownloadManagerAvailible;
	String myurl;
	DownloadManager manager = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_result);
		
		Toolbar tb;

		tb = (Toolbar) findViewById(R.id.tb);

		setSupportActionBar(tb);
		getSupportActionBar().setDisplayShowTitleEnabled(false);
		
		
		tv = (TextView) findViewById(R.id.resulttext);
		temp = new ArrayList<String>();

		lv = (ListView) findViewById(R.id.listview);

		myadapter = new CustomAdapter(SoundCloud.this, android.R.layout.simple_list_item_1, temp);
		lv.setAdapter(myadapter);	

	    dialog = new ProgressDialog(this);

		Bundle extras = getIntent().getExtras();
		trackname = extras.getString("trackname");
		streamurl = extras.getString("streamurl");
		downloadurl = streamurl + "?client_id=c85f6828ae5eaf5981937ead09ef1b45";
		temp.add(trackname);
		myadapter.notifyDataSetChanged();

		tv.setText("Your Search Results");		
	}

	public static boolean isDownloadManagerAvailable(Context context)
	{

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD)
		{
			return true;
		}
		return false;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// TODO: Implement this method
		MenuInflater mi = getMenuInflater();
		mi.inflate(R.menu.menu_options, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		// TODO: Implement this method
		switch (item.getItemId())
		{
			case R.id.dl:
				//setAlbumart();
				dlfile = new DownloadFileFromURL();
				Log.d("downloadurl", downloadurl);
				dlfile.execute(downloadurl);
				return true;		
		}
		return true;
	}

	public class DownloadFileFromURL extends AsyncTask<String, Integer, String>
	{
		@Override
		protected String doInBackground(String... f_url)
		{
			myurl = f_url[0];
			Log.e("doinbackground link" , myurl);

			isDownloadManagerAvailible =	isDownloadManagerAvailable(ctx);

			if (isDownloadManagerAvailible)
			{

				DownloadManager.Request request = new DownloadManager.Request(Uri.parse(myurl));
				request.setDescription("Downloading...");
				request.setTitle(trackname);

				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
				{
					request.allowScanningByMediaScanner();
					request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
				}
				request.setDestinationInExternalPublicDir("/SoundCloud", trackname + ".mp3");

				manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
				manager.enqueue(request);

			}

			runOnUiThread(new Runnable(){

					@Override
					public void run()
					{
						//update ui here
						Toast.makeText(SoundCloud.this, "Downloading...", Toast.LENGTH_SHORT).show();
					}
				});

			return "something";
		}
		
		@Override
		protected void onPostExecute(String file_url)
		{
			}
	}
	
	private void setAlbumart()
	{
		// TODO: Implement this method
		Uri path = Uri.parse("file:///android_asset/raw/sample/Flyer.jpg");

		String newPath = path.toString();
		File f = new File(newPath);
		if(f.exists())
		{
			Toast.makeText(SoundCloud.this,"file exists: "+f.getAbsolutePath(),Toast.LENGTH_LONG).show();
		}
		else if(!f.exists())
		{
			Toast.makeText(SoundCloud.this,"file does not exist",Toast.LENGTH_LONG).show();
		}
		Toast.makeText(SoundCloud.this,newPath,Toast.LENGTH_LONG).show();
		
		String albumid = "38";

		long l = Long.parseLong(albumid);
		Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");    
		int deleted = getContentResolver().delete(ContentUris.withAppendedId(sArtworkUri, l), null, null);
		Log.d("deleted:",""+deleted);

		ContentValues values = new ContentValues();
		values.put("album_id", albumid);
		values.put("_data", f.getAbsolutePath());
		Uri new_uri = getContentResolver().insert(sArtworkUri, values);
		if(new_uri == null)
		{
			Toast.makeText(SoundCloud.this,"failed to update albumart",Toast.LENGTH_LONG).show();
		}
		else
		{
			Toast.makeText(SoundCloud.this,"Albumart Updated",Toast.LENGTH_LONG).show();
		}
	}
	

	@Override
	protected void onPause()
	{
		// TODO: Implement this method
		super.onPause();	
	}
}

// https://m.soundcloud.com/krewella/bitch-of-the-year
