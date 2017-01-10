package jp.co.thcomp.util;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

@SuppressWarnings("rawtypes")
public class SimplexHashMultimap<K extends Comparable, V> implements Multimap<K, V>{
    private static String TAG = "HashMultimap";
    private HashMap<K, HashMap<V, Object>> mDataMultimap = new HashMap<K, HashMap<V, Object>>();
    private ArrayList<K> mKeyList = new ArrayList<K>();
    private ArrayList<K> mSortedKeyList = new ArrayList<K>();
    private Object mSizeSemapho = new Object();
    private int mSize = 0;
    private boolean mFirstWin = false;

    public SimplexHashMultimap(){
        this(false);
    }

    public SimplexHashMultimap(boolean firstWin){
        mFirstWin = firstWin;
    }

    public void add(K key, V value) {
        HashMap<V, Object> tempList = mDataMultimap.get(key);
        if(tempList != null){
            int oldSize = tempList.size();
            if(mFirstWin){
                if(tempList.get(value) == null){
                    tempList.put(value, new Object());
                }
            }else{
                tempList.put(value, new Object());
            }

            synchronized(mSizeSemapho){
                mSize+=(tempList.size() - oldSize);
            }
        }else{
            tempList = new HashMap<V, Object>();
            tempList.put(value, new Object());
            mDataMultimap.put(key, tempList);
            mKeyList.add(key);

            synchronized(mSizeSemapho){
                mSize++;
            }
        }
    }

    public void addAll(K key, List<V> values) {
        HashMap<V, Object> tempList = mDataMultimap.get(key);
        for(V value : values){
            add(key, value);
        }
    }

    public void clear(K key) {
        HashMap<V, Object> tempList = mDataMultimap.remove(key);
        if(tempList != null){
            mKeyList.remove(key);
            synchronized(mSizeSemapho){
                mSize -= tempList.size();
            }
        }
    }

    public void clearAll() {
        mDataMultimap.clear();
        mKeyList.clear();
        synchronized(mSizeSemapho){
            mSize = 0;
        }
    }

    public boolean containsKey(Object key) {
        return mKeyList.contains(key);
    }

    public boolean containsValue(Object value) {
        boolean ret = false;
        HashMap<V, Object> tempValues = null;

        for(int i=0, size=mKeyList.size(); i<size; i++){
            tempValues = mDataMultimap.get(mKeyList.get(i));
            if(tempValues != null){
                if(tempValues.get(value) != null){
                    ret = true;
                    break;
                }
            }
        }

        return ret;
    }

    public List<V> get(K key) {
        HashMap<V, Object> tempValues = mDataMultimap.get(key);
        ArrayList<V> ret = new ArrayList<V>();
        Set<V> keySet = tempValues.keySet();
        Iterator<V> iterator = keySet.iterator();

        while (iterator.hasNext()){
            ret.add(iterator.next());
        }

        return ret;
    }

    public List<V> getByIndex(int index) {
        K tempKey = mKeyList.get(index);
        HashMap<V, Object> tempValues = mDataMultimap.get(tempKey);
        ArrayList<V> ret = new ArrayList<V>();
        Set<V> keySet = tempValues.keySet();
        Iterator<V> iterator = keySet.iterator();

        while (iterator.hasNext()){
            ret.add(iterator.next());
        }

        return ret;
    }

    public List<V> getBySortedIndex(int index) {
        if(mSortedKeyList.size() == 0){
            mSortedKeyList.addAll(mKeyList);
        }

        K tempKey = mSortedKeyList.get(index);
        HashMap<V, Object> tempValues = mDataMultimap.get(tempKey);
        ArrayList<V> ret = new ArrayList<V>();
        Set<V> keySet = tempValues.keySet();
        Iterator<V> iterator = keySet.iterator();

        while (iterator.hasNext()){
            ret.add(iterator.next());
        }

        return ret;
    }

    @SuppressWarnings("unchecked")
    public void sort(){
        mSortedKeyList.clear();
        mSortedKeyList.addAll(mKeyList);
        Collections.sort(mKeyList);
    }

    public void sort(Comparator<K> comp){
        mSortedKeyList.clear();
        mSortedKeyList.addAll(mKeyList);
        Collections.sort(mKeyList, comp);
    }

    public boolean isEmpty() {
        return (mSize == 0);
    }

    public boolean replace(K key, V prevValue, V nextValue) {
        boolean ret = false;

        if(key != null && prevValue != null && nextValue != null){
            HashMap<V, Object> tempValueList = mDataMultimap.get(key);

            if(tempValueList != null){
                tempValueList.remove(prevValue);
                tempValueList.put(nextValue, new Object());
                ret = true;
            }
        }

        return ret;
    }

    public List<V> remove(K key) {
        HashMap<V, Object> tempRetObject = mDataMultimap.remove(key);

        if(tempRetObject != null){
            mKeyList.remove(key);
            synchronized(mSizeSemapho){
                mSize -= tempRetObject.size();
                if(mSize < 0){
                    Log.e(TAG, "illegal size, to 0");
                    mSize = 0;
                }
            }
        }

        ArrayList<V> ret = new ArrayList<V>();
        Set<V> keySet = tempRetObject.keySet();
        Iterator<V> iterator = keySet.iterator();

        while (iterator.hasNext()){
            ret.add(iterator.next());
        }

        return ret;
    }

    public boolean removeValue(V value) {
        boolean ret = false;
        HashMap<V, Object> tempDataList = null;

        for(int i=0, size=mKeyList.size(); i<size; i++){
            tempDataList = mDataMultimap.get(mKeyList.get(i));
            if(tempDataList != null){
                if(tempDataList.get(value) != null){
                    tempDataList.remove(value);
                    ret = true;
                    synchronized(mSizeSemapho){
                        mSize--;
                        if(mSize < 0){
                            Log.e(TAG, "illegal size, to 0");
                            mSize = 0;
                        }
                    }
                    break;
                }
            }
        }

        return ret;
    }

    public int size() {
        return mSize;
    }

    public int keySize() {
        return mKeyList.size();
    }

    public List<K> keyList() {
        return new ArrayList<K>(mKeyList);
    }
}
