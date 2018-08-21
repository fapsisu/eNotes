package su.fapsi.enotes;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import com.scottyab.aescrypt.AESCrypt;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import su.fapsi.enotes.Utils.*;

public final class Cryptography {
	private static String stringSHA512Hash;
	
	private static String getSHA512Hash(String stringFor512) throws UnsupportedEncodingException {
		stringSHA512Hash = null;
		
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-512");
	        byte[] bytes = md.digest(stringFor512.getBytes("UTF_8"));
	        StringBuilder sb = new StringBuilder();
	        for(int i=0; i< bytes.length ;i++){
	        	sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
	        }
	        stringSHA512Hash = sb.toString();
	        }
		catch (NoSuchAlgorithmException e){
			e.printStackTrace();
		}
		
		return stringSHA512Hash;
	}
	
	public static boolean isSetMasterPasswordEncryptNote(Context context) {
		SharedPreferences sp = Utils.getPreferences(context);
		return sp.getBoolean(Preferences.IS_SET_MASTER_PASSWORD, false);
	}
	
	public static boolean compareMasterPasswordWithInput(String masterPasswordEncryptNote, Context context) {
		SharedPreferences sp = Utils.getPreferences(context);
		String hashMasterPasswordEncryptNote = sp.getString(Preferences.HASH_MPEN, null);
		try {
			if(hashMasterPasswordEncryptNote.equals(getSHA512Hash(masterPasswordEncryptNote))) {
				return true;
			} else {
				return false;
			}
		} catch (UnsupportedEncodingException e) {
			return false;
		}
	}
	
	public static void setSpMasterPasswordEncryptNote(String masterPasswordEncryptNote, Context context) {
		SharedPreferences sp = Utils.getPreferences(context);
		Editor e = sp.edit();
		try {
			e.putString(Preferences.HASH_MPEN, getSHA512Hash(masterPasswordEncryptNote));
			e.putBoolean(Preferences.IS_SET_MASTER_PASSWORD, true);
			e.commit();
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
	}
	
	public static void encryptNote(Note note, String masterPasswordEncryptNote, Context context) {
		String textNoteEncrypt = null;
		try {
			textNoteEncrypt = AESCrypt.encrypt(masterPasswordEncryptNote, note.getTextN());
		} catch (GeneralSecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int statusEncrypt = NotesBase.get(context).setCryptTextNote(textNoteEncrypt, note.getUuidN().toString(), true);
	}
	
	public static void decryptNote(Note note, String masterPasswordEncryptNote, Context context) {
		String textNoteDecrypt = null;
		try {
			textNoteDecrypt = AESCrypt.decrypt(masterPasswordEncryptNote, note.getTextN());
		} catch (GeneralSecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int statusEncrypt = NotesBase.get(context).setCryptTextNote(textNoteDecrypt, note.getUuidN().toString(), false);
	}
	
	public static String getDecryptedNoteText(String text, String masterPasswordEncryptNote) {
		try {
			return AESCrypt.decrypt(masterPasswordEncryptNote, text);
		} catch (GeneralSecurityException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static String getEncryptedNoteText(String text, String masterPasswordEncryptNote) {
		try {
			return AESCrypt.encrypt(masterPasswordEncryptNote, text);
		} catch (GeneralSecurityException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static void reEnrypt(String oldMasterPassword, String newMasterPassword, Context context) {
		List<Note> notes = NotesBase.get(context).getAllNotes();
		for(int i=0; i<notes.size(); i++) {
			Note note = notes.get(i);
			if(note.getEncryptStatusN()) {
				String textDecryptWithOldPassword = getDecryptedNoteText(note.getTextN(), oldMasterPassword);
				note.setTextN(textDecryptWithOldPassword);
				encryptNote(note, newMasterPassword, context);
			}			
		}
		setSpMasterPasswordEncryptNote(newMasterPassword, context);
	}
	
	public static void clearPassword(Context context) {
		List<Note> notes = NotesBase.get(context).getAllNotes();
		for(int i=0; i<notes.size(); i++) {
			Note note = notes.get(i);
			if(note.getEncryptStatusN()) {
				NotesBase.get(context).delEncryptedNotes(new String[] {note.getUuidN().toString()});
			}
		}
		SharedPreferences sp = Utils.getPreferences(context);
		Editor e = sp.edit();
		e.putString(Preferences.HASH_MPEN, "");
		e.putBoolean(Preferences.IS_SET_MASTER_PASSWORD, false);
		e.commit();
	}
	
}