package jp.co.thcomp.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import android.util.Log;

@SuppressWarnings("rawtypes")
public class HashMultimap<K extends Comparable, V>{
    private static String TAG = "HashMultimap";
    private HashMap<K, List<V>> mDataMultimap = new HashMap<K, List<V>>();
    private ArrayList<K> mKeyList = new ArrayList<K>();
    private ArrayList<K> mSortedKeyList = new ArrayList<K>();
    private Object mSizeSemapho = new Object();
    private int mSize = 0;

    public void add(K key, V value) {
        List<V> tempList = mDataMultimap.get(key);
        if(tempList != null){
            tempList.add(value);
        }else{
            tempList = new ArrayList<V>();
            tempList.add(value);
            mDataMultimap.put(key, tempList);
            mKeyList.add(key);
        }

        synchronized(mSizeSemapho){
            mSize++;
        }
    }

    public void addAll(K key, List<V> values) {
        List<V> tempList = mDataMultimap.get(key);
        if(tempList != null){
            tempList.addAll(values);
        }else{
            tempList = new ArrayList<V>(values);
            mDataMultimap.put(key, tempList);
            mKeyList.add(key);
        }

        synchronized(mSizeSemapho){
            mSize += values.size();
        }
    }

    public void clear(K key) {
        List<V> tempList = mDataMultimap.remove(key);
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
        List<V> tempValues = null;

        for(int i=0, size=mKeyList.size(); i<size; i++){
            tempValues = mDataMultimap.get(mKeyList.get(i));
            if(tempValues != null){
                ret = tempValues.contains(value);
                if(ret){
                    break;
                }
            }
        }

        return ret;
    }

    public List<V> get(K key) {
        return mDataMultimap.get(key);
    }

    public List<V> getByIndex(int index) {
        K tempKey = mKeyList.get(index);
        return mDataMultimap.get(tempKey);
    }

    public List<V> getBySortedIndex(int index) {
        if(mSortedKeyList.size() == 0){
            mSortedKeyList.addAll(mKeyList);
        }

        K tempKey = mSortedKeyList.get(index);
        return mDataMultimap.get(tempKey);
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
            List<V> tempValueList = mDataMultimap.get(key);

            if(tempValueList != null){
                tempValueList.remove(prevValue);
                tempValueList.add(nextValue);
                ret = true;
            }
        }

        return ret;
    }

    public List<V> remove(K key) {
        List<V> retObject = mDataMultimap.remove(key);

        if(retObject != null){
            mKeyList.remove(key);
            synchronized(mSizeSemapho){
                mSize -= retObject.size();
                if(mSize < 0){
                    Log.e(TAG, "illegal size, to 0");
                    mSize = 0;
                }
            }
        }

        return retObject;
    }

    public boolean remove(V value) {
        boolean ret = false;
        List<V> tempDataList = null;

        for(int i=0, size=mKeyList.size(); i<size; i++){
            tempDataList = mDataMultimap.get(mKeyList.get(i));
            if(tempDataList != null){
                if(tempDataList.contains(value)){
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
