package jp.co.thcomp.util;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

public class ViewUtil {
	public static int getAttributeResourceValue(AttributeSet attrs, String namespace, String attribute, int defValue){
		int ret = defValue;

		if(attrs != null){
			ret = attrs.getAttributeResourceValue(namespace, attribute, defValue);
			if(ret == defValue){
				int count = attrs.getAttributeCount();
				String temp = null;
				for(int i = 0; i < count; i++){
					temp = attrs.getAttributeName(i);
					if(temp.equals(attribute)){
						ret = attrs.getAttributeResourceValue(i, defValue);
						break;
					}
				}
			}
		}
		return ret;
	}

	public static int getAttributeIntValue(AttributeSet attrs, String namespace, String attribute, int defValue){
		int ret = defValue;

		if(attrs != null){
			ret = attrs.getAttributeIntValue(namespace, attribute, defValue);
			if(ret == defValue){
				int count = attrs.getAttributeCount();
				String temp = null;
				for(int i = 0; i < count; i++){
					temp = attrs.getAttributeName(i);
					if(temp.equals(attribute)){
						ret = attrs.getAttributeIntValue(i, defValue);
						break;
					}
				}
			}
		}
		return ret;
	}

	public static String getAttributeStringValue(AttributeSet attrs, String namespace, String attribute){
		String ret = null;

		if(attrs != null){
			ret = attrs.getAttributeValue(namespace, attribute);
			if(ret == null){
				int count = attrs.getAttributeCount();
				String temp = null;
				for(int i = 0; i < count; i++){
					temp = attrs.getAttributeName(i);
					if(temp.equals(attribute)){
						ret = attrs.getAttributeValue(i);
						break;
					}
				}
			}
		}

		return ret;
	}

	public static boolean getAttributeBooleanValue(AttributeSet attrs, String namespace, String attribute, boolean defValue){
		boolean ret = defValue;

		if(attrs != null){
			ret = attrs.getAttributeBooleanValue(namespace, attribute, defValue);
			if(ret != defValue){
				return ret;
			}

			ret = attrs.getAttributeBooleanValue(namespace, attribute, !defValue);
			if(ret != (!defValue)){
				return ret;
			}

			String temp = null;
			int count = attrs.getAttributeCount();
			for(int i = 0; i < count; i++){
				temp = attrs.getAttributeName(i);
				if(temp.equals(attribute)){
					ret = attrs.getAttributeBooleanValue(i, defValue);
					break;
				}
			}
		}

		return ret;
	}

	public static float getAttributeFloatValue(AttributeSet attrs, String namespace, String attribute, float defValue){
		float ret = defValue;

		if(attrs != null){
			ret = attrs.getAttributeFloatValue(namespace, attribute, defValue);
			if(ret == defValue){
				int count = attrs.getAttributeCount();
				String temp = null;
				for(int i = 0; i < count; i++){
					temp = attrs.getAttributeName(i);
					if(temp.equals(attribute)){
						ret = attrs.getAttributeFloatValue(i, defValue);
						break;
					}
				}
			}
		}
		return ret;
	}

	public static void setBackgroundDrawable(View view, Drawable bgDrawable){
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
			view.setBackground(bgDrawable);
		}else{
			view.setBackgroundDrawable(bgDrawable);
		}
	}
}
