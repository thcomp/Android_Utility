package jp.co.thcomp.util;

import java.util.Calendar;

import android.os.Environment;

public class Constant {
	public static interface LOG_TYPE{
		public static final int LOG_TYPE_ERROR = 0;
		public static final int LOG_TYPE_WARNING = 1;
		public static final int LOG_TYPE_DEBUG = 2;
		public static final int LOG_TYPE_INFO = 3;
		public static final int LOG_TYPE_VERBOSE = 4;
		public static final int LOG_TYPE_BITMAP = 5;
		public static final int LOG_TYPE_STACKTRACE = 6;
	};

	public static interface LOG_SWITCH{
		public static final int LOG_SWITCH_ERROR = 1;
		public static final int LOG_SWITCH_WARNING = 2;
		public static final int LOG_SWITCH_DEBUG = 4;
		public static final int LOG_SWITCH_INFO = 8;
		public static final int LOG_SWITCH_VERBOSE = 16;
		public static final int LOG_SWITCH_BITMAP = 32;
		public static final int LOG_SWITCH_STACKTRACE = 64;
	};

	public static interface ACTIVITY_STATUS{
		public static final int ACT_STATUS_INIT = 0;
		public static final int ACT_STATUS_CREATED = 1;
		public static final int ACT_STATUS_STARTED = 2;
		public static final int ACT_STATUS_RESUMED = 3;
		public static final int ACT_STATUS_RESTARTED = 4;
		public static final int ACT_STATUS_PAUSED = 5;
		public static final int ACT_STATUS_STOPPED = 6;
		public static final int ACT_STATUS_DESTROYED = 7;
		public static final int ACT_STATUS_MAX = 8;
	};

	public static interface UNIT{
		public static final int YEAR = Calendar.YEAR;
		public static final int MONTH = Calendar.MONTH;
		public static final int DAY = Calendar.DAY_OF_MONTH;
		public static final int HOUR = Calendar.HOUR_OF_DAY;
		public static final int MINUTE = Calendar.MINUTE;
		public static final int SECOND = Calendar.SECOND;
		public static final int MILLISECOND = Calendar.MILLISECOND;
	};

	public static interface WEEK_DAY{
		public static final int SUNDAY = 0;
		public static final int MONDAY = 1;
		public static final int TUESDAY = 2;
		public static final int WEDNESDAY = 3;
		public static final int THURSDAY = 4;
		public static final int FRIDAY = 5;
		public static final int SATURDAY = 6;
		public static final int MAX_WEEK_DAY = 7;
	};

	public static interface MEDIA_TYPE{
		public static final int MEDIA_TYPE_IMAGE = 0;
		public static final int MEDIA_TYPE_VIDEO = 1;
		public static final int MEDIA_TYPE_MUSIC = 2;
		public static final int MEDIA_TYPE_MAX = 3;
	};

	public static interface MEDIA_LOCATION_TYPE{
		public static final int MEDIA_LOC_LOCAL = 0;
		public static final int MEDIA_LOC_REMOTE = 1;
		public static final int MEDIA_LOC_UNKNOWN = 2;
	};

	public static String APP_VERSION = "1.0";
	public static int CONN_TIMEOUT = 5000;
	public static int SO_TIMEOUT = 5000;
	public static String USER_AGENT = "thcomp-calendar/" + APP_VERSION;
	public static final int MICRO_KIND_SIZE = 96;
	public static final int MICRO_KIND_WIDTH = 96;
	public static final int MICRO_KIND_HEIGHT = 96;
	public static final int MINI_KIND_WIDTH = 512;
	public static final int MINI_KIND_HEIGHT = 384;
	public static final int BITMAP_POOL_SIZE_PER_TYPE = 5;
	public static final int WEEK_DAY_NUM = 7;

	public static final int INT_SIZE = (int)(Float.SIZE / Byte.SIZE);
	public static final int FLOAT_SIZE = (int)(Float.SIZE / Byte.SIZE);

	public static final String CommonFolder = Environment.getExternalStorageDirectory().getPath() + "/Android/data/jp.co.thcomp.common";
	public static final String EventLogcatFile = CommonFolder + "/event_logcat.txt";

	public static final String[][] MEDIA_TYPE_CHECK_STRINGS = new String[MEDIA_TYPE.MEDIA_TYPE_MAX][];
	static{
		MEDIA_TYPE_CHECK_STRINGS[MEDIA_TYPE.MEDIA_TYPE_IMAGE] = new String[]{
				"image/",
		};
		MEDIA_TYPE_CHECK_STRINGS[MEDIA_TYPE.MEDIA_TYPE_VIDEO] = new String[]{
				"video/",
		};
		MEDIA_TYPE_CHECK_STRINGS[MEDIA_TYPE.MEDIA_TYPE_MUSIC] = new String[]{
				"audio/",
		};
	}
}
