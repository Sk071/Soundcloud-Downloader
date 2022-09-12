package com.sk.scdoenloader;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.gson.*;
import java.io.IOException;
import okhttp3.*;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;


public class MainActivity extends ActionBarActivity 
{
	String res;
	Button b;
	EditText et;
	public String search;
	ProgressDialog dialog;
	String trackname;
	String url;
	String streamUrl;
	String downloadlink;
	Track track;
	Toolbar tb;

	private boolean isNetworkAvailable()
	{
		ConnectivityManager connectivityManager 
			= (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

		tb = (Toolbar) findViewById(R.id.tb);

		setSupportActionBar(tb);
		getSupportActionBar().setDisplayShowTitleEnabled(false);

		b = (Button) findViewById(R.id.b);
		et = (EditText) findViewById(R.id.et);
		dialog = new ProgressDialog(this);

		if (isNetworkAvailable())
		{
			Toast.makeText(MainActivity.this, "Internet Is Connected", Toast.LENGTH_SHORT).show();
		}
		else
		{
			Toast.makeText(MainActivity.this, "Please Connect to internet First", Toast.LENGTH_LONG).show();
		}

		b.setOnClickListener(new View.OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					// TODO: Implement this method
					if (et.getText().toString().matches(""))
					{
						Toast.makeText(MainActivity.this, "Field Is Blank", Toast.LENGTH_SHORT).show();
					}
					else
					{
						OkHttpHandler myhandler = new OkHttpHandler();		
						search = et.getText().toString();
						Log.d("search", search);			
						dialog.setMessage("Searching...");
						dialog.show();
		                myhandler.execute(search);		
					}
				}
			});
    }

	@Override
	protected void onPause()
	{
		// TODO: Implement this method
		super.onPause();
	}

	public class OkHttpHandler extends AsyncTask<String,Void,String>
	{
		private void showMyError()
		{
			runOnUiThread(new Runnable(){
					@Override
					public void run()
					{
						// TODO: Implement this method
						Toast.makeText(MainActivity.this, "Invalid URL", Toast.LENGTH_SHORT).show();
					}
				});
		}

		private void showMyError(final String tAG)
		{
			runOnUiThread(new Runnable(){
					@Override
					public void run()
					{
						// TODO: Implement this method
						Toast.makeText(MainActivity.this, tAG, Toast.LENGTH_SHORT).show();
					}
				});
		}

		@Override
		protected String doInBackground(String[] p1)
		{
			// TODO: Implement this method
			if (isNetworkAvailable())
			{
				OkHttpClient client = new OkHttpClient();
				String searchedstring = p1[0];		

				String url = Config.API_URL + "/resolve.json?url=" + searchedstring + "&client_id=" + Config.CLIENT_ID;

				Request request = new Request.Builder().url(url).build();

				try
				{
					Response response = client.newCall(request).execute();
					res = response.body().string();	


					if (res == null)
					{
						Log.e("TAG", "NULLPOINTEREXCEPTION");
						showMyError("error");
					}

					if (res.contains("errors"))
					{
						Log.e("TAG", "Invalid URL");
						showMyError();	
					}

					if (res != null)
					{
						Log.d("callResponse", res);
					}

				}
				catch (IOException e)
				{
					String err = (e.getMessage() == null) ?"Failed to fetch": e.getMessage();
					showMyError();
					Log.e("error", err);
				}	

				if (res.contains("errors"))
				{
					Log.e("TAG", "result is null");
				}

				else
				{
					Gson gson = new Gson();
					track = gson.fromJson(res, Track.class);
					streamUrl = track.getStreamUrl();
					trackname = track.getTitle();
					myActivity(trackname, streamUrl);
				}
			}
			else
			{
				showMyError("Connect to the Internet first");
			}

			return trackname;
		}

		@Override
		protected void onPostExecute(String result)
		{
			// TODO: Implement this method
			if (dialog.isShowing())
			{
				dialog.dismiss();
			}
			super.onPostExecute(result);
			if (result != null)
			{
				Log.d("result from AsyncTask", result);
				Toast.makeText(MainActivity.this, result, Toast.LENGTH_SHORT).show();
			}
			if (res == null)
			{
				Log.e("TAG", "result is null");
			}
		}

		private void myActivity(String tname, String surl)
		{
			// TODO: Implement this method
			Log.d("tname", tname);
			Log.d("surl", surl);
			Intent i = new Intent(MainActivity.this, SoundCloud.class);
			Bundle extras = new Bundle();
			extras.putString("trackname", tname);
			extras.putString("streamurl", surl);
		    i.putExtras(extras);
			startActivity(i);
		}
	}
}
