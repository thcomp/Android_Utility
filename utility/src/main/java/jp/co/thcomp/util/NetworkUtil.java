package jp.co.thcomp.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;

import java.lang.reflect.Method;

public class NetworkUtil {
	private static final String TAG = "NetworkUtil";
	private static Boolean SUPPORT_WIMAX_STATE = null;
	private static Boolean SUPPORT_WIFI_STATE = null;
	private static Boolean SUPPORT_MOBILE_STATE = null;
	private static Boolean SUPPORT_TETHERING_STATE = null;

	public static boolean isAvailableNetwork(Context context){
		ConnectivityManager connManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connManager.getActiveNetworkInfo();
		return networkInfo == null ? false : networkInfo.isAvailable();
	}

	public static boolean isConnectedNetwork(Context context){
		ConnectivityManager connManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connManager.getActiveNetworkInfo();
		return networkInfo == null ? false : networkInfo.isConnected();
	}

	public static boolean isSupportMobileNetwork(Context context){
		if(SUPPORT_MOBILE_STATE == null){
			SUPPORT_MOBILE_STATE = isNetworkSupported(context, ConnectivityManager.TYPE_MOBILE);
		}

		return SUPPORT_MOBILE_STATE;
	}

	public static boolean isSupportWiMax(Context context){
		if(SUPPORT_WIMAX_STATE == null){
			SUPPORT_WIMAX_STATE = isNetworkSupported(context, ConnectivityManager.TYPE_WIMAX);
		}

		return SUPPORT_WIMAX_STATE;
	}

	public static boolean isSupportWiFi(Context context){
		if(SUPPORT_WIFI_STATE == null){
			SUPPORT_WIFI_STATE = isNetworkSupported(context, ConnectivityManager.TYPE_WIFI);
		}

		return SUPPORT_WIFI_STATE;
	}

	public static boolean isSupportTethering(Context context){
		if(SUPPORT_TETHERING_STATE == null){
			return true;
		}

		return SUPPORT_WIFI_STATE;
	}

	public static void enableMobileNetwork(Context context, boolean enable){
		if(isSupportMobileNetwork(context)){
			enableNetworkStatus(context, ConnectivityManager.TYPE_MOBILE, enable);
		}
	}

	public static void enableWiMax(Context context, boolean enable){
		if(isSupportWiMax(context)){
			enableNetworkStatus(context, ConnectivityManager.TYPE_WIMAX, enable);
		}
	}

	public static void enableWiFi(Context context, boolean enable){
		if(isSupportWiFi(context)){
			enableNetworkStatus(context, ConnectivityManager.TYPE_WIFI, enable);
		}
	}

	public static boolean enableTethering(Context context, boolean enable){
		boolean ret = false;

		if(isSupportTethering(context)) {
			WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);

			try {
				Object methodRet = wifiManager.getClass().getMethod("setWifiApEnabled", new Class[]{WifiConfiguration.class, boolean.class}).invoke(wifiManager, null, enable);
				if(methodRet instanceof Boolean){
					ret = (Boolean)methodRet;
				}
			} catch (Exception ex) {
				SUPPORT_TETHERING_STATE = false;
			}
		}

		return ret;
	}

	public static boolean isEnableMobileNetwork(Context context){
		boolean ret = false;
		if(isSupportMobileNetwork(context)){
			ret = isEnableNetworkStatus(context, ConnectivityManager.TYPE_MOBILE);
		}
		return ret;
	}

	public static boolean isEnableWiMax(Context context){
		boolean ret = false;
		if(isSupportWiMax(context)){
			ret = isEnableNetworkStatus(context, ConnectivityManager.TYPE_WIMAX);
		}
		return ret;
	}

	public static boolean isEnableWiFi(Context context){
		boolean ret = false;
		if(isSupportWiFi(context)){
			ret = isEnableNetworkStatus(context, ConnectivityManager.TYPE_WIFI);
		}
		return ret;
	}

	public static boolean isEnableTethering(Context context){
		boolean ret = false;
		if(isSupportTethering(context)){
			WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);

			try {
				Object methodRet = wifiManager.getClass().getMethod("isWifiApEnabled").invoke(wifiManager);
				if(methodRet instanceof Boolean){
					ret = (Boolean)methodRet;
				}
			} catch (Exception ex) {
				SUPPORT_TETHERING_STATE = false;
			}
		}
		return ret;
	}

	private static boolean isNetworkSupported(Context context, int networkType){
		boolean ret = true;
		ConnectivityManager connManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

		switch(networkType){
		case ConnectivityManager.TYPE_MOBILE:
		case ConnectivityManager.TYPE_WIFI:
		case ConnectivityManager.TYPE_WIMAX:
			try {
				ret = (Boolean)connManager.getClass().getMethod("isNetworkSupported", new Class[]{Integer.TYPE}).invoke(connManager, Integer.valueOf(networkType));
			} catch (Exception e) {
				LogUtil.e(TAG, e.getLocalizedMessage());
			}
			break;
		default:
			ret = false;
			break;
		}

		return ret;
	}

	private static void enableNetworkStatus(Context context, int networkType, boolean enable){
		switch(networkType){
		case ConnectivityManager.TYPE_MOBILE:
			try {
				ConnectivityManager connManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
				connManager.getClass().getMethod("setMobileDataEnabled", new Class[]{Boolean.TYPE}).invoke(connManager, Boolean.valueOf(enable));
			} catch (Exception e) {
				LogUtil.e(TAG, e.getLocalizedMessage());
			}
			break;
		case ConnectivityManager.TYPE_WIFI:
			WifiManager wifiManager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
			wifiManager.setWifiEnabled(enable);
			break;
		case ConnectivityManager.TYPE_WIMAX:
			try {
				Object manager = context.getSystemService("wimax");
				if(manager != null){
					manager.getClass().getMethod("setWimaxEnabled", new Class[]{Boolean.TYPE}).invoke(manager, Boolean.valueOf(enable));
				}
			} catch (Exception e) {
				LogUtil.e(TAG, e.getLocalizedMessage());
			}
			break;
		default:
			break;
		}
	}

	private static boolean isEnableNetworkStatus(Context context, int networkType){
		boolean ret = false;

		switch(networkType){
		case ConnectivityManager.TYPE_MOBILE:
			try {
				ConnectivityManager connManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
				ret = (Boolean) connManager.getClass().getMethod("getMobileDataEnabled", new Class[]{}).invoke(connManager);
			} catch (Exception e) {
				LogUtil.e(TAG, e.getLocalizedMessage());
			}
			break;
		case ConnectivityManager.TYPE_WIFI:
			WifiManager wifiManager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
			ret = wifiManager.isWifiEnabled();
			break;
		case ConnectivityManager.TYPE_WIMAX:
			try {
				Object manager = context.getSystemService("wimax");
				if(manager != null){
					manager.getClass().getMethod("getWimaxEnabled", new Class[]{}).invoke(manager);
				}
			} catch (Exception e) {
				LogUtil.e(TAG, e.getLocalizedMessage());
			}
			break;
		default:
			ret = false;
			break;
		}

		return ret;
	}
}
