package su.fapsi.enotes;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Environment;
import android.text.format.DateFormat;
import android.widget.Toast;

public final class Utils {
	
	public static final class Password {
		public static final String KEY_CALL_SOURCE = "keyCallSource";
		public static final String SETTINGS_CALL_SOURCE = "settingsCallSource";
		public static final String NOTE_LIST_CALL_SOURCE = "noteListCallSource";
		public static final int RC_SP_FNL = 2;
	}
	
	public static final class Preferences {
		private static SharedPreferences enSettings;
		private static final String APP_PREFERENCES = "enSettings";
		private static final String IS_SET_SETTINGS = "isSetSettings";
		public static final String IS_SET_MASTER_PASSWORD = "isSetMasterPassword";
		public static final String HASH_MPEN = "hashMPEN";
		public static final String REQUEST_CODE_REMINDER = "requestCodeReminder";
		public static final String NOTE_R_UUID = "noteReminderUuid";
		public static final String IS_SET_VIBRATE = "isSetVibrate";
		public static final String KEY_DEFAULT_NOTE_COLOR = "defaultColor";
		public static final int DEFAULT_COLOR_INDEX = 6;
		public static final String KEY_DEFAULT_METHOD_SORT = "indexMethodSort";
		public static final int DEFAULT_INDEX_METHOD_SORT = 0;
		public static final int START_RCR = 100000;
	}
		
	public static final class Notes {
		public static final String NOTETYPE_NOTE = "note";
		public static final String NOTETYPE_ARCHIVE = "archive";
		public static final String NOTETYPE_RECYCLE = "recycle";
		public static final String NOTE_EDITTYPE = "EditType";
		public static final String NOTE_EDITTYPE_ADD = "AddNote";
		public static final String NOTE_EDITTYPE_EDIT = "EditNote";
		public static final String NOTE_STRING_SEARCH = "NoteStringSearch";
	}
	
	public static final class MethSort {
		public static final int DATE_CR_DESC = 201;
		public static final int DATE_CR_ASC = 202;
		public static final int DATE_ED_DESC = 203;
		public static final int DATE_ED_ASC = 204;
		public static final int AZ_DESC = 205;
		public static final int AZ_ASC = 206;
		public static final int DATE_REM_DESC = 207;
		public static final int DATE_REM_ASC = 208;
		public static final int BY_COLOR = 209;
		public static final int DATE_ARCH_DESC = 210;
		public static final int DATE_ARCH_ASC = 211;
		public static final int DATE_RECYC_DESC = 212;
		public static final int DATE_RECYC_ASC = 213;
		
	}
	
	public static final class CntxMenu {
		public static final int TO_ARCHIVE = 101;
		public static final int TO_RECYCLE = 102;
		public static final int DELETE = 103;
		public static final int RECOVE = 104;
		public static final int UNPUCK = 105;
		public static final int NOTE_TO_FILE = 106;
		public static final int NOTE_TO_PDF = 107;
		public static final int EN_DE_CRYPT_NOTE = 108;
		public static final int REMINDER_NOTE = 109;
	}
	
	public static SharedPreferences getPreferences(Context context) {
		Preferences.enSettings = context.getSharedPreferences(Preferences.APP_PREFERENCES, Context.MODE_PRIVATE);
		boolean isSetSettings = Preferences.enSettings.getBoolean(Preferences.IS_SET_SETTINGS, false);
		if(!isSetSettings) {
			Editor e = Preferences.enSettings.edit();
			e.putBoolean(Preferences.IS_SET_SETTINGS, true);
			e.putBoolean(Preferences.IS_SET_MASTER_PASSWORD, false);
			e.putInt(Preferences.REQUEST_CODE_REMINDER, Preferences.START_RCR);
			e.putBoolean(Preferences.IS_SET_VIBRATE, true);
			e.putInt(Preferences.KEY_DEFAULT_NOTE_COLOR, Preferences.DEFAULT_COLOR_INDEX);
			e.putInt(Preferences.KEY_DEFAULT_METHOD_SORT, Preferences.DEFAULT_INDEX_METHOD_SORT);
			e.commit();
		}
		
		return Preferences.enSettings;
	}
	
	public static void setCtxMenu(int itemCtcMenu, String alertTitle, String alertMessage,
			final UUID uuid, final Context context, final int position) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		final AlertDialog alertNote = builder.create();
		alertNote.setTitle(alertTitle);
		alertNote.setMessage(alertMessage);
		
		switch(itemCtcMenu) {
			case CntxMenu.TO_ARCHIVE:
				alertNote.setButton(context.getResources().getString(R.string.btnOk), new DialogInterface.OnClickListener() {
					@Override
					public void onClick(final DialogInterface dialog, final int id) {
						Note note = NotesBase.get(context).getNoteReminder(uuid.toString());
						cancelReminderIntent(context, note);
						int statusQuery = NotesBase.get(context).moveNoteToArchive(new String[] {uuid.toString()});
						//NotesBase.get(context).clearReminderDate(uuid.toString());
						NotesListBaseActivity.removeNoteFromList(position);
						alertNote.cancel();
					}
				});
			break;
			
			case CntxMenu.TO_RECYCLE:
				alertNote.setButton(context.getResources().getString(R.string.btnOk), new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						Note note = NotesBase.get(context).getNoteReminder(uuid.toString());
						cancelReminderIntent(context, note);
						int statusQuery = NotesBase.get(context).moveNoteToRecycle(new String[] {uuid.toString()});
						//NotesBase.get(context).clearReminderDate(uuid.toString());
						NotesListBaseActivity.removeNoteFromList(position);
						alertNote.cancel();
					}
				});
			break;
			
			case CntxMenu.DELETE:
				alertNote.setButton(context.getResources().getString(R.string.btnOk), new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						int statusQuery = NotesBase.get(context).delNote(new String[] {uuid.toString()});
						NotesListBaseActivity.removeNoteFromList(position);
						alertNote.cancel();
					}
				});
			break;
		}
		
		alertNote.setButton2(context.getResources().getString(R.string.btnCancel), new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int id) {
				alertNote.cancel();
			}
		});
		alertNote.show();
	}
	
	public static int getMethodSort(String method, Context context) {
		Map<String, Integer> mapMethodSort = new HashMap<String, Integer>();
		mapMethodSort.put(context.getResources().getString(R.string.sort_date_cr_desk), MethSort.DATE_CR_DESC);
		mapMethodSort.put(context.getResources().getString(R.string.sort_date_cr_ask), MethSort.DATE_CR_ASC);
		mapMethodSort.put(context.getResources().getString(R.string.sort_date_ed_desk), MethSort.DATE_ED_DESC);
		mapMethodSort.put(context.getResources().getString(R.string.sort_date_ed_ask), MethSort.DATE_ED_ASC);
		mapMethodSort.put(context.getResources().getString(R.string.sort_az_desk), MethSort.AZ_DESC);
		mapMethodSort.put(context.getResources().getString(R.string.sort_az_ask), MethSort.AZ_ASC);
		mapMethodSort.put(context.getResources().getString(R.string.sort_date_rem_desk), MethSort.DATE_REM_DESC);
		mapMethodSort.put(context.getResources().getString(R.string.sort_date_rem_ask), MethSort.DATE_REM_ASC);
		mapMethodSort.put(context.getResources().getString(R.string.sort_color), MethSort.BY_COLOR);
		mapMethodSort.put(context.getResources().getString(R.string.sort_date_arch_desk), MethSort.DATE_ARCH_DESC);
		mapMethodSort.put(context.getResources().getString(R.string.sort_date_arch_ask), MethSort.DATE_ARCH_ASC);
		mapMethodSort.put(context.getResources().getString(R.string.sort_date_recyc_desk), MethSort.DATE_RECYC_DESC);
		mapMethodSort.put(context.getResources().getString(R.string.sort_date_recyc_ask), MethSort.DATE_RECYC_ASC);
		
		return mapMethodSort.get(method);
	}
	
	public static void exportNoteToTxt(Note note, Context context) {
		if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
		      Toast.makeText(context, context.getResources().getString(R.string.msg_not_access_SD), Toast.LENGTH_SHORT).show(); 
		      return;
		}
		
		if(!note.getEncryptStatusN()) {
			String fileName = note.getTitleN().replaceAll("[^\\w ]", "").replace(" ", "_");
			
			if(fileName.equals("")) {
				fileName = "Note " + DateFormat.format("yyyy-MM-dd kk-mm-ss", (new Date()).getTime()).toString();
			}
			String sdPath = Environment.getExternalStorageDirectory().getAbsolutePath()+ "/Document";
			
			File dir = new File(sdPath);
			if(!dir.exists()) {
				dir.mkdirs();
			}
			if(dir.isDirectory()) {
				File noteFile = new File(sdPath, fileName+".txt");
				try {
					BufferedWriter nFile = new BufferedWriter(new FileWriter(noteFile));
					nFile.write(note.getTextN());
					nFile.close();
					Toast.makeText(context, context.getResources().getString(R.string.msg_note_export_done), Toast.LENGTH_SHORT).show();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} else {
			Toast.makeText(context, context.getResources().getString(R.string.msg_wrong_export_encrypt_note), Toast.LENGTH_SHORT).show();
		}		
		
	}
	
	public static void setReminderIntent(Context context, Note note) {
		NotesBase.get(context).setReminderDate(note.getUuidN().toString(), note.getDateReminderN().getTime(), note.getRcReminderN());
		Intent reminderIntent = new Intent(context, NotificationReceiver.class)
				.putExtra(Preferences.REQUEST_CODE_REMINDER, note.getRcReminderN())
				.putExtra(Preferences.NOTE_R_UUID, note.getUuidN().toString());
		PendingIntent alarmIntent = PendingIntent.getBroadcast(context, note.getRcReminderN(), reminderIntent, 0);
		AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		alarmManager.set(AlarmManager.RTC_WAKEUP, note.getDateReminderN().getTime(), alarmIntent);
	}
	
	public static void updateReminderIntent(Context context, Note note) {
		NotesBase.get(context).setReminderDate(note.getUuidN().toString(), note.getDateReminderN().getTime(), note.getRcReminderN());
		Intent reminderIntent = new Intent(context, NotificationReceiver.class)
				.putExtra(Preferences.REQUEST_CODE_REMINDER, note.getRcReminderN())
				.putExtra(Preferences.NOTE_R_UUID, note.getUuidN().toString());
		PendingIntent alarmIntent = PendingIntent.getBroadcast(context, note.getRcReminderN(), reminderIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		alarmManager.set(AlarmManager.RTC_WAKEUP, note.getDateReminderN().getTime(), alarmIntent);
	}
	
	public static void cancelReminderIntent(Context context, Note note) {
		NotesBase.get(context).clearReminderDate(note.getUuidN().toString());
		Intent reminderIntent = new Intent(context, NotificationReceiver.class)
				.putExtra(Preferences.REQUEST_CODE_REMINDER, note.getRcReminderN())
				.putExtra(Preferences.NOTE_R_UUID, note.getUuidN().toString());
		PendingIntent alarmIntent = PendingIntent.getBroadcast(context, note.getRcReminderN(), reminderIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		alarmManager.cancel(alarmIntent);	
	}
	
	public static void setNotificationVibrate(boolean setVibrate, Context context) {
		context = context;
		SharedPreferences sp = getPreferences(context);
		Editor e = sp.edit();
		e.putBoolean(Preferences.IS_SET_VIBRATE, setVibrate);
		e.commit();
	}
	
	public static boolean isSetNotoficationVibrate(Context context) {
		context = context;
		return getPreferences(context).getBoolean(Preferences.IS_SET_VIBRATE, true);
	}
	
	public static int getDefaultNoteColor(Context context) {
		return getPreferences(context).getInt(Preferences.KEY_DEFAULT_NOTE_COLOR, Preferences.DEFAULT_COLOR_INDEX);
	}
	
	public static void setDefaultNoteColor(int indexColor, Context context) {
		SharedPreferences sp = getPreferences(context);
		Editor e = sp.edit();
		e.putInt(Preferences.KEY_DEFAULT_NOTE_COLOR, indexColor);
		e.commit();
	}
	
	public static int getDefaultIndexMethSort(Context context) {
		return getPreferences(context).getInt(Preferences.KEY_DEFAULT_METHOD_SORT, Preferences.DEFAULT_INDEX_METHOD_SORT);
	}
	
	public static void setDefaultIndexMethSort(int indexMethSort, Context context) {
		SharedPreferences sp = getPreferences(context);
		Editor e = sp.edit();
		e.putInt(Preferences.KEY_DEFAULT_METHOD_SORT, indexMethSort);
		e.commit();
	}
	
	public static int[] getColors(Context context) {
		return new int[] {context.getResources().getColor(R.color.col0), context.getResources().getColor(R.color.col1), context.getResources().getColor(R.color.col2), 
				context.getResources().getColor(R.color.col3), context.getResources().getColor(R.color.col4), context.getResources().getColor(R.color.col5), 
				context.getResources().getColor(R.color.col6), context.getResources().getColor(R.color.col7), context.getResources().getColor(R.color.col8)};
	}
	
	public static int[] getColors1(Context context) {
		return new int[] {context.getResources().getColor(R.color.col1_0), context.getResources().getColor(R.color.col1_1), context.getResources().getColor(R.color.col1_2), 
				context.getResources().getColor(R.color.col1_3), context.getResources().getColor(R.color.col1_4), context.getResources().getColor(R.color.col1_5), 
				context.getResources().getColor(R.color.col1_6), context.getResources().getColor(R.color.col1_7), context.getResources().getColor(R.color.col1_8)};
	}
	
	public static int[] getColors2(Context context) {
		return new int[] {context.getResources().getColor(R.color.col2_0), context.getResources().getColor(R.color.col2_1), context.getResources().getColor(R.color.col2_2), 
				context.getResources().getColor(R.color.col2_3), context.getResources().getColor(R.color.col2_4), context.getResources().getColor(R.color.col2_5), 
				context.getResources().getColor(R.color.col2_6), context.getResources().getColor(R.color.col2_7), context.getResources().getColor(R.color.col2_8)};
	}
	
}