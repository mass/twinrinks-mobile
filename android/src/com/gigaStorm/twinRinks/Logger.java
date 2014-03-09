package com.gigaStorm.twinRinks;
import android.util.Log;

public class Logger 
{
	private static	int loggingLevel = Log.ERROR;
	
	public static void enableLogging(int lvl)
	{
		loggingLevel = lvl;
	}
	
	public static void d(String tag, String msg) 
	{
		if (loggingLevel >= Log.DEBUG) 
		{
			Log.d(tag, msg);
		}
	}

	public static void i(String tag, String msg) 
	{
		if (loggingLevel <= Log.INFO)
		{
			Log.i(tag, msg);
		}
	}

	public static void e(String tag, String msg) 
	{
		if (loggingLevel <= Log.ERROR) 
		{
			Log.e(tag, msg);
		}
	}

	public static void v(String tag, String msg) 
	{
		if (loggingLevel <= Log.VERBOSE) 
		{
			Log.v(tag, msg);
		}
	}

	public static void w(String tag, String msg) 
	{
		if (loggingLevel <= Log.WARN)
		{
			Log.w(tag, msg);
		}
	}
}
