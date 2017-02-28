package jp.co.thcomp.util;

import android.graphics.Bitmap;
import android.graphics.Color;

import java.util.HashMap;

import static jp.co.thcomp.util.Constant.BITMAP_POOL_SIZE_PER_TYPE;

public class MediaUtil {
	private static final String TAG = "MediaUtil";

	public static class BitmapPool{
		private static final HashMap<Long, Bitmap[]> BITMAP_POOL_565 = new HashMap<Long, Bitmap[]>();
		private static final HashMap<Long, Bitmap[]> BITMAP_POOL_8888 = new HashMap<Long, Bitmap[]>();
		private static final HashMap<Long, Bitmap[]> BITMAP_POOL_4444 = new HashMap<Long, Bitmap[]>();
		public static boolean reserveBitmapSize(int width, int height, Bitmap.Config config, int poolSize){
			boolean ret = false;
			HashMap<Long, Bitmap[]> tempBitmapPool = null;

			switch(config){
			case ARGB_4444:
				tempBitmapPool = BITMAP_POOL_4444;
				break;
			case ARGB_8888:
				tempBitmapPool = BITMAP_POOL_8888;
				break;
			case RGB_565:
				tempBitmapPool = BITMAP_POOL_565;
				break;
			}

			if(tempBitmapPool != null){
				long key = (width << Integer.SIZE) | height;
				tempBitmapPool.put(key, new Bitmap[poolSize]);
				ret = true;
			}

			return ret;
		}

		public static Bitmap createBitmap(int width, int height, Bitmap.Config config){
			Bitmap ret = null;
			HashMap<Long, Bitmap[]> tempBitmapPool = null;

			switch(config){
			case ARGB_4444:
				tempBitmapPool = BITMAP_POOL_4444;
				break;
			case ARGB_8888:
				tempBitmapPool = BITMAP_POOL_8888;
				break;
			case RGB_565:
				tempBitmapPool = BITMAP_POOL_565;
				break;
			}

			if(tempBitmapPool != null){
				boolean existBitmapPool = false;
				long key = 0;
				Bitmap tempBitmaps[] = null;
				synchronized(tempBitmapPool){
					key = ((long)width << Integer.SIZE) | height;
					tempBitmaps = tempBitmapPool.get(key);
					if(tempBitmaps != null){
						existBitmapPool = true;
					}else{
						tempBitmaps = new Bitmap[BITMAP_POOL_SIZE_PER_TYPE];
						ret = Bitmap.createBitmap(width, height, config);
						tempBitmapPool.put(key, tempBitmaps);
						LogUtil.d(TAG, "create BitmapPool(" + ret.toString() + "): width=" + width + ", height=" + height + ", config=" + config);
					}
				}

				if(existBitmapPool){
					synchronized(tempBitmaps){
						for(int i=0; i<tempBitmaps.length; i++){
							if(tempBitmaps[i] != null){
								ret = tempBitmaps[i];
								ret.eraseColor(Color.TRANSPARENT);
								LogUtil.d(TAG, "reuse(" + ret.toString() + "): width=" + width + ", height=" + height + ", config=" + config);
								tempBitmaps[i] = null;
								break;
							}
						}
					}
				}
			}

			if(ret == null){
				ret = Bitmap.createBitmap(width, height, config);
				LogUtil.d(TAG, "createBitmap(" + ret.toString() + "): width=" + width + ", height=" + height + ", config=" + config);
			}

			return ret;
		}

		public static void recycle(Bitmap bitmap){
			if(!bitmap.isRecycled()){
				Bitmap.Config config = (bitmap != null) ? bitmap.getConfig() : null;

				if(config != null && bitmap.isMutable()){
					HashMap<Long, Bitmap[]> tempBitmapPool = null;

					switch(config){
					case ARGB_4444:
						tempBitmapPool = BITMAP_POOL_4444;
						break;
					case ARGB_8888:
						tempBitmapPool = BITMAP_POOL_8888;
						break;
					case RGB_565:
						tempBitmapPool = BITMAP_POOL_565;
						break;
					}

					if(tempBitmapPool != null){
						long key = ((long)bitmap.getWidth() << Integer.SIZE) | bitmap.getHeight();
						Bitmap tempBitmaps[] = tempBitmapPool.get(key);
						if(tempBitmaps != null){
							synchronized(tempBitmaps){
								for(int i=0; i<tempBitmaps.length; i++){
									if(tempBitmaps[i] == null){
										tempBitmaps[i] = bitmap;
										LogUtil.d(TAG, "pool(" + bitmap.toString() + "): width=" + bitmap.getWidth() + ", height=" + bitmap.getHeight() + ", config=" + config);
										bitmap = null;
										break;
									}
								}
							}
						}
					}
				}

				if(bitmap != null){
					synchronized(bitmap){
						if(!bitmap.isRecycled()){
							LogUtil.d(TAG, "recycle(" + bitmap.toString() + "): width=" + bitmap.getWidth() + ", height=" + bitmap.getHeight() + ", config=" + config);
							bitmap.recycle();
						}
						bitmap = null;
					}
				}
			}
		}

		@SuppressWarnings({ "unchecked", "rawtypes" })
		public static void removeAllCache(){
			HashMap tempBitmapPoolList[] = new HashMap[]{BITMAP_POOL_4444, BITMAP_POOL_8888, BITMAP_POOL_565};
			HashMap<Long, Bitmap[]> tempBitmapPool = null;
			Bitmap[] tempBitmapList = null;
			Long keySet[] = null;

			for(int i=0; i<tempBitmapPoolList.length; i++){
				tempBitmapPool = tempBitmapPoolList[i];
				keySet = tempBitmapPool.keySet().toArray(new Long[0]);
				for(int j=0; j<keySet.length; j++){
					tempBitmapList = tempBitmapPool.get(keySet[j]);
					if(tempBitmapList != null){
						synchronized(tempBitmapList){
							for(int k=0; k<tempBitmapList.length; k++){
								if(tempBitmapList[k] != null){
									tempBitmapList[k].recycle();
									tempBitmapList[k] = null;
								}
							}
						}
					}
				}

				tempBitmapPool.clear();
			}
		}
	}
}
