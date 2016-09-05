package jp.co.thcomp.secure;

import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import jp.co.thcomp.util.LogUtil;

public class SecureController {
	public static final int SECURE_ALG_MD5 = 0;
	public static final int SECURE_ALG_SHA1 = 1;
	public static final int SECURE_ALG_SHA256 = 2;
	public static final int SECURE_ALG_SHA384 = 3;
	public static final int SECURE_ALG_SHA512 = 4;
	private static final String TAG = "SecureController";
	private SecretKey mDesKey = null;

	public SecureController(){
		
	}

	public boolean setPassword(byte[] password, int algType){
		boolean ret = true;
		MessageDigest md = null;
		DESKeySpec desKeySpec = null;

		try {
			md = MessageDigest.getInstance(exchangeAlgorithmName(algType));
		} catch (NoSuchAlgorithmException e) {
			LogUtil.e(TAG, e.getLocalizedMessage());
			ret = false;
		}

		if(ret){
			md.update(password);
			try {
				desKeySpec = new DESKeySpec(md.digest());
			} catch (InvalidKeyException e) {
				LogUtil.e(TAG, e.getLocalizedMessage());
				ret = false;
			}
		}
		
		if(ret){
			SecretKeyFactory desKeyFac;
			try {
				desKeyFac = SecretKeyFactory.getInstance("DES");
				mDesKey = desKeyFac.generateSecret(desKeySpec);
			} catch (NoSuchAlgorithmException e) {
				LogUtil.e(TAG, e.getLocalizedMessage());
				ret = false;
			} catch (InvalidKeySpecException e) {
				LogUtil.e(TAG, e.getLocalizedMessage());
				ret = false;
			}
		}

		return ret;
	}

	public byte[] encryptData(byte[] plainData){
		byte[] ret = null;

		if(mDesKey != null){
			Cipher cipher;
			try {
				cipher = Cipher.getInstance("DES");
				cipher.init(Cipher.ENCRYPT_MODE, mDesKey);
		        ret = cipher.doFinal(plainData);
			} catch (InvalidKeyException e) {
				LogUtil.e(TAG, e.getLocalizedMessage());
			} catch (NoSuchAlgorithmException e) {
				LogUtil.e(TAG, e.getLocalizedMessage());
			} catch (NoSuchPaddingException e) {
				LogUtil.e(TAG, e.getLocalizedMessage());
			} catch (IllegalBlockSizeException e) {
				LogUtil.e(TAG, e.getLocalizedMessage());
			} catch (BadPaddingException e) {
				LogUtil.e(TAG, e.getLocalizedMessage());
			}
		}else{
			LogUtil.e(TAG, "not set password");
		}

		return ret;
	}
	
	public byte[] decryptData(byte[] encryptedData){
		byte[] ret = null;

		if(mDesKey != null){
			Cipher cipher;
			try {
				cipher = Cipher.getInstance("DES");
				cipher.init(Cipher.DECRYPT_MODE, mDesKey);
		        ret = cipher.doFinal(encryptedData);
			} catch (InvalidKeyException e) {
				LogUtil.e(TAG, e.getLocalizedMessage());
			} catch (NoSuchAlgorithmException e) {
				LogUtil.e(TAG, e.getLocalizedMessage());
			} catch (NoSuchPaddingException e) {
				LogUtil.e(TAG, e.getLocalizedMessage());
			} catch (IllegalBlockSizeException e) {
				LogUtil.e(TAG, e.getLocalizedMessage());
			} catch (BadPaddingException e) {
				LogUtil.e(TAG, e.getLocalizedMessage());
			}
		}else{
			LogUtil.e(TAG, "not set password");
		}

		return ret;
	}
	
	private String exchangeAlgorithmName(int algType){
		String ret = null;

		switch(algType){
		case SECURE_ALG_MD5:
			ret = "MD5";
			break;
		case SECURE_ALG_SHA1:
			ret = "SHA-1";
			break;
		case SECURE_ALG_SHA256:
			ret = "SHA-256";
			break;
		case SECURE_ALG_SHA384:
			ret = "SHA-384";
			break;
		case SECURE_ALG_SHA512:
			ret = "SHA-512";
			break;
		default:
			LogUtil.w(TAG, "no type for " + algType + ", use default type");
			ret = "SHA-512";
			break;
		}
		return ret;
	}
}
