package com.sk.scdoenloader;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.Context;
import android.util.Log;
import android.database.Cursor;
import android.widget.Toast;

public class checkDownloadComplete extends BroadcastReceiver
{
    public static boolean isDownloadComplete= false;

	@Override
	public void onReceive(Context context, Intent intent)
	{
		// TODO: Implement this method
		Cursor cursor;
		String action = intent.getAction();
		if (action.equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
		{
			DownloadManager.Query query = new DownloadManager.Query();
			query.setFilterById(intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0));
			DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
			cursor = manager.query(query);
			if (cursor.moveToFirst())
			{
				if (cursor.getCount() > 0)
				{
					int status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
					if (status == DownloadManager.STATUS_SUCCESSFUL)
					{
						String file = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_FILENAME));
						Toast.makeText(context, "Download Complete", Toast.LENGTH_SHORT).show();
					}
					else
					{
						int message = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_REASON));
						Toast.makeText(context, "File Not Found On Server", Toast.LENGTH_LONG).show();
					}
				}
			}
		}
	}

	
	
}
