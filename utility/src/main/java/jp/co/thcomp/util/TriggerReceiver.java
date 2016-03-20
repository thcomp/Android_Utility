package jp.co.thcomp.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

public class TriggerReceiver extends BroadcastReceiver {
	private static final HashMap<String, ArrayList<OnTriggerListener>> sRegisteredFilter = new HashMap<String, ArrayList<OnTriggerListener>>();

	public static interface OnTriggerListener{
		public void onReceive(Context context, Intent intent);
	};

	private static TriggerReceiver sInstance;
	
	private TriggerReceiver(){
		super();
	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent != null ? intent.getAction() : null;
		ArrayList<OnTriggerListener> listeners;

		synchronized(sRegisteredFilter){
			if((action != null) && ((listeners = sRegisteredFilter.get(action)) != null)){
				for(OnTriggerListener listener : listeners){
					listener.onReceive(context, intent);
				}
			}
		}
	}

	public static void registerReceiver(Context context, String actionType, OnTriggerListener listener){
		ArrayList<OnTriggerListener> listeners;

		synchronized(sRegisteredFilter){
			if(sInstance == null){
				sInstance = new TriggerReceiver();
			}
	
			if((listeners = sRegisteredFilter.get(actionType)) == null){
				IntentFilter filter = new IntentFilter();
				filter.addAction(actionType);
				context.getApplicationContext().registerReceiver(sInstance, filter);
	
				listeners = new ArrayList<OnTriggerListener>();
				sRegisteredFilter.put(actionType, listeners);
			}
	
			if(!listeners.contains(listener)){
				listeners.add(listener);
			}
		}
	}

	public static synchronized void unregisterReceiver(Context context, String actionType, OnTriggerListener listener){
		synchronized(sRegisteredFilter){
			if(sInstance != null){
				ArrayList<OnTriggerListener> listeners;
				
				if((listeners = sRegisteredFilter.get(actionType)) != null){
					listeners.remove(listener);
					if(listeners.size() == 0){
						sRegisteredFilter.remove(listeners);
						if(sRegisteredFilter.size() == 0){
							context.getApplicationContext().unregisterReceiver(sInstance);
						}
					}
				}
			}
		}
	}

	public static synchronized void unregisterReceiver(Context context, OnTriggerListener listener){
		synchronized(sRegisteredFilter){
			if(sInstance != null){
				ArrayList<OnTriggerListener> listeners;
				Set<String> actionSet = sRegisteredFilter.keySet();
				String actionArray[] = actionSet.toArray(new String[0]);
				for(String action : actionArray){
					listeners = sRegisteredFilter.get(action);
					if(listeners != null){
						listeners.remove(listener);
						if(listeners.size() == 0){
							sRegisteredFilter.remove(action);
						}
					}
				}
	
				if(sRegisteredFilter.size() == 0){
					context.getApplicationContext().unregisterReceiver(sInstance);
				}
			}
		}
	}
}
