package jp.co.thcomp.secure;


import android.util.SparseBooleanArray;

public class PasswordGenerator {
	public static final int CHAR_TYPE_UPPER_CASE = 0;
	public static final int CHAR_TYPE_LOWER_CASE = 1;
	public static final int CHAR_TYPE_DIGIT = 2;
	public static final int CHAR_TYPE_MARKS = 3;
	public static final int MAX_CHAR_TYPE = 4;

	private static final int DEFAUT_PASSWORD_SIZE = 8;
	private static final SparseBooleanArray DEFAULT_AVAILABLE_FIRST_CHAR = new SparseBooleanArray();
	private static final SparseBooleanArray DEFAUL_AVAILABLE_MARKS_ARRAY = new SparseBooleanArray();
	static{
		DEFAULT_AVAILABLE_FIRST_CHAR.put(CHAR_TYPE_UPPER_CASE, true);
		DEFAULT_AVAILABLE_FIRST_CHAR.put(CHAR_TYPE_LOWER_CASE, true);
		DEFAULT_AVAILABLE_FIRST_CHAR.put(CHAR_TYPE_DIGIT, true);
		DEFAULT_AVAILABLE_FIRST_CHAR.put(CHAR_TYPE_MARKS, false);

		DEFAUL_AVAILABLE_MARKS_ARRAY.put(0x20, false);
		DEFAUL_AVAILABLE_MARKS_ARRAY.put(0x21, true);
		DEFAUL_AVAILABLE_MARKS_ARRAY.put(0x22, true);
		DEFAUL_AVAILABLE_MARKS_ARRAY.put(0x23, true);
		DEFAUL_AVAILABLE_MARKS_ARRAY.put(0x24, true);
		DEFAUL_AVAILABLE_MARKS_ARRAY.put(0x25, true);
		DEFAUL_AVAILABLE_MARKS_ARRAY.put(0x26, true);
		DEFAUL_AVAILABLE_MARKS_ARRAY.put(0x27, true);
		DEFAUL_AVAILABLE_MARKS_ARRAY.put(0x28, true);
		DEFAUL_AVAILABLE_MARKS_ARRAY.put(0x29, true);
		DEFAUL_AVAILABLE_MARKS_ARRAY.put(0x2A, true);
		DEFAUL_AVAILABLE_MARKS_ARRAY.put(0x2B, true);
		DEFAUL_AVAILABLE_MARKS_ARRAY.put(0x2C, true);
		DEFAUL_AVAILABLE_MARKS_ARRAY.put(0x2D, true);
		DEFAUL_AVAILABLE_MARKS_ARRAY.put(0x2E, true);
		DEFAUL_AVAILABLE_MARKS_ARRAY.put(0x2F, true);
		DEFAUL_AVAILABLE_MARKS_ARRAY.put(0x3A, true);
		DEFAUL_AVAILABLE_MARKS_ARRAY.put(0x3B, true);
		DEFAUL_AVAILABLE_MARKS_ARRAY.put(0x3C, true);
		DEFAUL_AVAILABLE_MARKS_ARRAY.put(0x3D, true);
		DEFAUL_AVAILABLE_MARKS_ARRAY.put(0x3E, true);
		DEFAUL_AVAILABLE_MARKS_ARRAY.put(0x3F, true);
		DEFAUL_AVAILABLE_MARKS_ARRAY.put(0x40, true);
		DEFAUL_AVAILABLE_MARKS_ARRAY.put(0x5B, true);
		DEFAUL_AVAILABLE_MARKS_ARRAY.put(0x5C, true);
		DEFAUL_AVAILABLE_MARKS_ARRAY.put(0x5D, true);
		DEFAUL_AVAILABLE_MARKS_ARRAY.put(0x5E, true);
		DEFAUL_AVAILABLE_MARKS_ARRAY.put(0x5F, true);
		DEFAUL_AVAILABLE_MARKS_ARRAY.put(0x60, true);
		DEFAUL_AVAILABLE_MARKS_ARRAY.put(0x7B, true);
		DEFAUL_AVAILABLE_MARKS_ARRAY.put(0x7C, true);
		DEFAUL_AVAILABLE_MARKS_ARRAY.put(0x7D, true);
		DEFAUL_AVAILABLE_MARKS_ARRAY.put(0x7E, true);
		DEFAUL_AVAILABLE_MARKS_ARRAY.put(0x7F, false);
	};
	private int mPasswordSize = DEFAUT_PASSWORD_SIZE;
	private SparseBooleanArray mAvailableFirstChar = DEFAULT_AVAILABLE_FIRST_CHAR.clone();
	private SparseBooleanArray mAvailableMarks = DEFAUL_AVAILABLE_MARKS_ARRAY.clone();

	public void setEnableMark(byte mark, boolean enable){
		if(mAvailableMarks.indexOfKey(mark) >= 0){
			mAvailableMarks.put(mark, enable);
		}
	}

	public boolean isEnableMark(byte mark){
		boolean ret = false;
		if(mAvailableMarks.indexOfKey(mark) >= 0){
			ret = mAvailableMarks.get(mark);
		}
		return ret;
	}

	public void setEnableFirstChar(int type, boolean enable){
		if(mAvailableFirstChar.indexOfKey(type) >= 0){
			mAvailableFirstChar.put(type, enable);
		}
	}

	public boolean isEnableFirstChar(int type){
		boolean ret = false;
		if(mAvailableFirstChar.indexOfKey(type) >= 0){
			ret = mAvailableFirstChar.get(type);
		}
		return ret;
	}

	public void setPasswordLength(int length){
		mPasswordSize = length;
	}

	public int getPasswordLength(){
		return mPasswordSize;
	}

	public byte[] generate(){
		byte[] ret = new byte[mPasswordSize];

		while(true){
			int type = ((int)(Math.random() * 100)) % MAX_CHAR_TYPE;
			if(mAvailableFirstChar.get(type)){
				ret[0] = getRandomByte(type);
				break;
			}
		}

		for(int i=1, size=mPasswordSize; i<size; i++){
			ret[i] = getRandomByte(((int)(Math.random() * 100)) % MAX_CHAR_TYPE);
		}
		
		return ret;
	}

	private byte getRandomByte(int type){
		int startCode = 0;
		int endCode = 0;

		switch(type){
		case CHAR_TYPE_UPPER_CASE:
			startCode = 0x41;
			endCode = 0x5A + 1;
			break;
		case CHAR_TYPE_LOWER_CASE:
			startCode = 0x61;
			endCode = 0x7A + 1;
			break;
		case CHAR_TYPE_DIGIT:
			startCode = 0x30;
			endCode = 0x39 + 1;
			break;
		case CHAR_TYPE_MARKS:
		default:
			endCode = mAvailableMarks.size();
			while(true){
				int mark = ((int)Math.random() * 100) % endCode;
				if(mAvailableMarks.get(mark)){
					return (byte)mAvailableMarks.keyAt(mark);
				}
			}
		}

		return  (byte)(((int)Math.random() * 100) % (endCode - startCode) + startCode);
	}
}
