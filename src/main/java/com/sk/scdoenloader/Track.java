package com.sk.scdoenloader;

import com.google.gson.annotations.*;

public class Track
{
	@SerializedName("title")
	private String mTitle;

	@SerializedName("stream_url")
	private String mStreamUrl;

	public String getTitle()
	{
		return mTitle;
	}

	public String getStreamUrl()
	{
		return mStreamUrl;
	}
}
