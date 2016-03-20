package jp.co.thcomp.util;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import jp.co.thcomp.util.Constant.LOG_TYPE;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;

public class LogUtil {
	private static boolean LOGOUTPUT_E = true;
	private static boolean LOGOUTPUT_W = false;
	private static boolean LOGOUTPUT_D = false;
	private static boolean LOGOUTPUT_I = false;
	private static boolean LOGOUTPUT_V = false;
	private static boolean LOGOUTPUT_BMP = false;
	private static boolean LOGOUTPUT_STACKTRACE = false;
	private static HashMap<String, FunctionLog> mFunctionLogMap = new HashMap<String, FunctionLog>();
	private static final BulkFunctionLog mBulkFuncLog = new BulkFunctionLog("");
	private static final String BLANK_LOG = "";

	private LogUtil(){
		// forbid create instance
	}

	public static void logoutput(int logswitch){
		if((logswitch & Constant.LOG_SWITCH.LOG_SWITCH_ERROR) != 0){
			LOGOUTPUT_E = true;
		}else{
			LOGOUTPUT_E = false;
		}
		if((logswitch & Constant.LOG_SWITCH.LOG_SWITCH_WARNING) != 0){
			LOGOUTPUT_W = true;
		}else{
			LOGOUTPUT_W = false;
		}
		if((logswitch & Constant.LOG_SWITCH.LOG_SWITCH_DEBUG) != 0){
			LOGOUTPUT_D = true;
		}else{
			LOGOUTPUT_D = false;
		}
		if((logswitch & Constant.LOG_SWITCH.LOG_SWITCH_INFO) != 0){
			LOGOUTPUT_I = true;
		}else{
			LOGOUTPUT_I = false;
		}
		if((logswitch & Constant.LOG_SWITCH.LOG_SWITCH_VERBOSE) != 0){
			LOGOUTPUT_V = true;
		}else{
			LOGOUTPUT_V = false;
		}
		if((logswitch & Constant.LOG_SWITCH.LOG_SWITCH_BITMAP) != 0){
			LOGOUTPUT_BMP = true;
		}else{
			LOGOUTPUT_BMP = false;
		}
		if((logswitch & Constant.LOG_SWITCH.LOG_SWITCH_STACKTRACE) != 0){
			LOGOUTPUT_STACKTRACE = true;
		}else{
			LOGOUTPUT_STACKTRACE = false;
		}
	}

	public static boolean isOutput(int logType){
		switch(logType){
		case LOG_TYPE.LOG_TYPE_ERROR:
			return LOGOUTPUT_E;
		case LOG_TYPE.LOG_TYPE_WARNING:
			return LOGOUTPUT_W;
		case LOG_TYPE.LOG_TYPE_DEBUG:
			return LOGOUTPUT_D;
		case LOG_TYPE.LOG_TYPE_INFO:
			return LOGOUTPUT_I;
		case LOG_TYPE.LOG_TYPE_VERBOSE:
			return LOGOUTPUT_V;
		case LOG_TYPE.LOG_TYPE_BITMAP:
			return LOGOUTPUT_BMP;
		case LOG_TYPE.LOG_TYPE_STACKTRACE:
			return LOGOUTPUT_STACKTRACE;
		default:
			return false;
		}
	}
	
	public static void registFunctionLog(String... tags){
		for(int iCount = 0; iCount < tags.length; iCount++){
			mFunctionLogMap.put(tags[iCount], new FunctionLog(tags[iCount]));
		}
	}

	public static FunctionLog getFunctionLogInstance(String tag){
		FunctionLog ret = null;

		ret = mFunctionLogMap.get(tag);
		if(null == ret){
			ret = mBulkFuncLog;
		}

		return ret;
	}

	public static void e(String tag, String content){
		if(LOGOUTPUT_E){
			if(content == null){
				content = BLANK_LOG;
			}
			Log.e(tag, content);
		}
	}

	public static void e(String tag, InputStream input, int bufferSize){
		if(LOGOUTPUT_E){
			byte buffer[] = new byte[bufferSize];
			try {
				int readSize = input.read(buffer);
				String content = new String(buffer, 0, readSize);
				Log.e(tag, content);
			} catch (IOException e1) {
			}
		}
	}

	public static void w(String tag, String content){
		if(LOGOUTPUT_W){
			if(content == null){
				content = BLANK_LOG;
			}
			Log.w(tag, content);
		}
	}

	public static void w(String tag, InputStream input, int bufferSize){
		if(LOGOUTPUT_W){
			byte buffer[] = new byte[bufferSize];
			try {
				int readSize = input.read(buffer);
				String content = new String(buffer, 0, readSize);
				Log.e(tag, content);
			} catch (IOException e1) {
			}
		}
	}

	public static void d(String tag, String content){
		if(LOGOUTPUT_D){
			if(content == null){
				content = BLANK_LOG;
			}
			Log.d(tag, content);
		}
	}

	public static void d(String tag, InputStream input, int bufferSize){
		if(LOGOUTPUT_D){
			byte buffer[] = new byte[bufferSize];
			try {
				int readSize = input.read(buffer);
				String content = new String(buffer, 0, readSize);
				Log.e(tag, content);
			} catch (IOException e1) {
			}
		}
	}

	public static void i(String tag, String content){
		if(LOGOUTPUT_I){
			if(content == null){
				content = BLANK_LOG;
			}
			Log.i(tag, content);
		}
	}

	public static void i(String tag, InputStream input, int bufferSize){
		if(LOGOUTPUT_I){
			byte buffer[] = new byte[bufferSize];
			try {
				int readSize = input.read(buffer);
				String content = new String(buffer, 0, readSize);
				Log.e(tag, content);
			} catch (IOException e1) {
			}
		}
	}

	public static void v(String tag, String content){
		if(LOGOUTPUT_V){
			if(content == null){
				content = BLANK_LOG;
			}
			Log.v(tag, content);
		}
	}

	public static void v(String tag, InputStream input, int bufferSize){
		if(LOGOUTPUT_V){
			byte buffer[] = new byte[bufferSize];
			try {
				int readSize = input.read(buffer);
				String content = new String(buffer, 0, readSize);
				Log.e(tag, content);
			} catch (IOException e1) {
			}
		}
	}

	public static void dumpBitmap(String filepath, Bitmap bitmap){
		if(LOGOUTPUT_BMP){
			FileOutputStream output = null;
			try{
				output = new FileOutputStream(filepath);
				bitmap.compress(Bitmap.CompressFormat.PNG, 100, output);
			}catch(FileNotFoundException e){
				LogUtil.e("", e.getLocalizedMessage());
			}finally{
				if(output != null){
					try{
						output.close();
					}catch(IOException e){
						LogUtil.e("", e.getLocalizedMessage());
					}
				}
			}
		}
	}

	public static void dumpBitmap(String filepath, Bitmap bitmap, Rect dumpRect){
		if(LOGOUTPUT_BMP){
			Bitmap dumpBitmap = Bitmap.createBitmap(dumpRect.width(), dumpRect.height(), bitmap.getConfig());
			Canvas dumpCanvas = new Canvas(dumpBitmap);
			dumpCanvas.drawBitmap(bitmap, dumpRect, new Rect(0, 0, dumpRect.width(), dumpRect.height()), null);
			FileOutputStream output = null;
			try{
				output = new FileOutputStream(filepath);
				dumpBitmap.compress(Bitmap.CompressFormat.PNG, 100, output);
			}catch(FileNotFoundException e){
				LogUtil.e("", e.getLocalizedMessage());
			}finally{
				if(output != null){
					try{
						output.close();
					}catch(IOException e){
						LogUtil.e("", e.getLocalizedMessage());
					}
				}
				if(dumpBitmap != null){
					dumpBitmap.recycle();
				}
			}
		}
	}

	public static void printStackTrace(){
		if(LOGOUTPUT_STACKTRACE){
			try{
				throw new Throwable();
			}catch(Throwable th){
				th.printStackTrace();
			}
		}
	}

	private static class BulkFunctionLog extends FunctionLog{
		public BulkFunctionLog(String tag){
			super(tag);
		}

		public void d(String content){
			// no work
		}

		public void i(String content){
			// no work
		}

		public void v(String content){
			// no work
		}
	}
	
	public static class FunctionLog{
		private String TAG = null;

		private FunctionLog(String tag){
			// forbid create instance from others
			TAG = new String(tag);
		}

		public void d(String content){
			if(LOGOUTPUT_D){
				if(content == null){
					content = BLANK_LOG;
				}
				Log.d(TAG, content);
			}
		}

		public void i(String content){
			if(LOGOUTPUT_I){
				if(content == null){
					content = BLANK_LOG;
				}
				Log.i(TAG, content);
			}
		}

		public void v(String content){
			if(LOGOUTPUT_V){
				if(content == null){
					content = BLANK_LOG;
				}
				Log.v(TAG, content);
			}
		}
	}
}
