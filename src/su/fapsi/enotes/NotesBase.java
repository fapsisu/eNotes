package su.fapsi.enotes;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import su.fapsi.enotes.NoteDBShema.NoteTable;
import su.fapsi.enotes.Utils.*;

public class NotesBase {
	private static NotesBase sNotesBase;
	
	private Context mContext;
	private SQLiteDatabase mDatabase;
	
	private String whereClause;
	private String orderBy;
	private String orderType;
	
	public static NotesBase get(Context context) {
		if(sNotesBase == null ){
			sNotesBase = new NotesBase(context);
		}
		return sNotesBase;
	}
	
	private NotesBase(Context context) {
		mContext = context.getApplicationContext();
		mDatabase = new NotesBaseHelper(mContext).getWritableDatabase();
	}
	
	
	private static ContentValues getContentValues(Note note) {
		ContentValues values = new ContentValues();
		values.put(NoteTable.Cols_note.UUID_N, note.getUuidN().toString());
		values.put(NoteTable.Cols_note.DATE_CREATED_N, note.getDateCreateN().getTime());
		values.put(NoteTable.Cols_note.DATE_LAST_EDIT_N, note.getDateLastEditN().getTime());
		values.put(NoteTable.Cols_note.TYPE_N, note.getTypeNote());
		values.put(NoteTable.Cols_note.TITLE_N, note.getTitleN());
		values.put(NoteTable.Cols_note.TEXT_N, note.getTextN());
		values.put(NoteTable.Cols_note.ENCRYPT_STATUS_N, note.getEncryptStatusN() ? 1 : 0);
		values.put(NoteTable.Cols_note.COLOR_N_INDEX, note.getColorNoteIndex());
		values.put(NoteTable.Cols_note.DATE_REMINDER_N, note.getDateReminderN().getTime());
		values.put(NoteTable.Cols_note.DATE_ARCHIVE_N, note.getDateArchiveN().getTime());
		values.put(NoteTable.Cols_note.DATE_RECYCLE_N, note.getDateRecycleN().getTime());
		values.put(NoteTable.Cols_note.RC_REMINDER_N, note.getRcReminderN());
		values.put(NoteTable.Cols_note.SCROLL_POSITION_N, note.getScrollPositionN());
		return values;
	}
	
	
	/*TODO
	 * в методах добавления, обновления и удаления заметок прописать возвращение статуса
	 */
	
	public int addNote(Note note){
		ContentValues values = getContentValues(note);
		mDatabase.insert(NoteTable.NAME_NT, null, values);
		
		return 0;
	}
	
	public int updateNote(Note note) {
		String id = note.getUuidN().toString();
		ContentValues values = getContentValues(note);
		mDatabase.update(NoteTable.NAME_NT, values, NoteTable.Cols_note.UUID_N + " = ? ", new String[] {id});
		
		return 0;
	}
	
	public int changeColorIndex(Note note) {
		String id = note.getUuidN().toString();
		ContentValues values = new ContentValues();
		values.put(NoteTable.Cols_note.COLOR_N_INDEX, note.getColorNoteIndex());
		mDatabase.update(NoteTable.NAME_NT, values, NoteTable.Cols_note.UUID_N + " = ? ", new String[] {id});
		
		return 0;
	}
	
	public int moveNoteToArchive(String[] id) {
		ContentValues values = new ContentValues();
		values.put(NoteTable.Cols_note.TYPE_N, Notes.NOTETYPE_ARCHIVE);
		values.put(NoteTable.Cols_note.DATE_ARCHIVE_N, (new Date()).getTime());
		mDatabase.update(NoteTable.NAME_NT, values, NoteTable.Cols_note.UUID_N + " = ? ", id);
		
		return 0;
	}
	
	public int moveNoteToRecycle(String[] id) {
		ContentValues values = new ContentValues();
		values.put(NoteTable.Cols_note.TYPE_N, Notes.NOTETYPE_RECYCLE);
		values.put(NoteTable.Cols_note.DATE_RECYCLE_N, (new Date()).getTime());
		mDatabase.update(NoteTable.NAME_NT, values, NoteTable.Cols_note.UUID_N + " = ? ", id);
		
		return 0;
	}
	
	public int recoveryNote(String[] id) {
		ContentValues values = new ContentValues();
		values.put(NoteTable.Cols_note.TYPE_N, Notes.NOTETYPE_NOTE);
		mDatabase.update(NoteTable.NAME_NT, values, NoteTable.Cols_note.UUID_N + " = ? ", id);
		
		return 0;
	}
	
	public  int delNote(String[] id) {
		return mDatabase.delete(NoteTable.NAME_NT, NoteTable.Cols_note.UUID_N + "= ?", id);
	}
	
	public  int clearRecycler() {
		return mDatabase.delete(NoteTable.NAME_NT, NoteTable.Cols_note.TYPE_N +"= ?", new String[] {Notes.NOTETYPE_RECYCLE});
	}
	
	
	private NoteCursorWrapper queryNotes(String whereClause, String[] typeNote, String orderBy, String orderType) {
		Cursor cursor = mDatabase.query(
				NoteTable.NAME_NT,
				null,	//Columns - select all columns
				whereClause+"=?",	//whereClause	column type_n
				typeNote,	//whereArgs		type_note
				null,	//groupBy
				null,	//having
				orderBy + " " +	orderType	//orderBy column + DESC/ASC
			);
		
		return new NoteCursorWrapper(cursor);
	}
	
	private class NoteCursorWrapper extends CursorWrapper {
		public NoteCursorWrapper(Cursor cursor) {
			super(cursor);
		}
		
		public Note getNote() {
			String uuidStringNote = getString(getColumnIndex(NoteTable.Cols_note.UUID_N));
			long dateCreatedNote = getLong(getColumnIndex(NoteTable.Cols_note.DATE_CREATED_N));
			long dateLastEditNote = getLong(getColumnIndex(NoteTable.Cols_note.DATE_LAST_EDIT_N));
			String typeNote = getString(getColumnIndex(NoteTable.Cols_note.TYPE_N));
			String titleNote = getString(getColumnIndex(NoteTable.Cols_note.TITLE_N));
			String textNote = getString(getColumnIndex(NoteTable.Cols_note.TEXT_N));
			boolean encryptStatusNote = (getInt(getColumnIndex(NoteTable.Cols_note.ENCRYPT_STATUS_N))!=0);
			int colorNoteIndex = getInt(getColumnIndex(NoteTable.Cols_note.COLOR_N_INDEX));
			long dateReminderNote = getLong(getColumnIndex(NoteTable.Cols_note.DATE_REMINDER_N));
			long dateArchiveNote = getLong(getColumnIndex(NoteTable.Cols_note.DATE_ARCHIVE_N));
			long dateRecycleNote = getLong(getColumnIndex(NoteTable.Cols_note.DATE_RECYCLE_N));
			int rqReminderN = getInt(getColumnIndex(NoteTable.Cols_note.RC_REMINDER_N));
			int scrollPositionN = getInt(getColumnIndex(NoteTable.Cols_note.SCROLL_POSITION_N));
			
			Note note = new Note(UUID.fromString(uuidStringNote));
			note.setDateCreateN(new Date(dateCreatedNote));
			note.setDateLastEditN(new Date(dateLastEditNote));
			note.setTypeNote(typeNote);
			note.setTitleN(titleNote);
			note.setTextN(textNote);
			note.setEncryptStatusN(encryptStatusNote);
			note.setColorNoteIndex(colorNoteIndex);
			note.setDateReminderN(new Date(dateReminderNote));
			note.setDateArchiveN(new Date(dateArchiveNote));
			note.setDateRecycleN(new Date (dateRecycleNote));
			note.setRcReminderN(rqReminderN);
			note.setScrollPositionN(scrollPositionN);
			
			return note;
		}
	}
	
	public List<Note> getNotes(String[] typeNote, int methodSort) {
		List<Note> notes = new ArrayList<Note>();
		whereClause = NoteTable.Cols_note.TYPE_N;
		
		switch(methodSort) {
			case MethSort.DATE_CR_DESC:
				orderBy = NoteTable.Cols_note.DATE_CREATED_N;
				orderType = "DESC";
			break;
			
			case MethSort.DATE_CR_ASC:
				orderBy = NoteTable.Cols_note.DATE_CREATED_N;
				orderType = "ASC";
			break;
			
			case MethSort.DATE_ED_DESC:
				orderBy = NoteTable.Cols_note.DATE_LAST_EDIT_N;
				orderType = "DESC";
			break;
			
			case MethSort.DATE_ED_ASC:
				orderBy = NoteTable.Cols_note.DATE_LAST_EDIT_N;
				orderType = "ASC";
			break;
			
			case MethSort.AZ_DESC:
				orderBy = NoteTable.Cols_note.TITLE_N;
				orderType = "DESC";
			break;
			
			case MethSort.AZ_ASC:
				orderBy = NoteTable.Cols_note.TITLE_N;
				orderType = "ASC";
			break;
			
			case MethSort.DATE_REM_DESC:
				orderBy = NoteTable.Cols_note.DATE_REMINDER_N;
				orderType = "DESC";
			break;
			
			case MethSort.DATE_REM_ASC:
				orderBy = NoteTable.Cols_note.DATE_REMINDER_N;
				orderType = "ASC";
			break;
			
			case MethSort.BY_COLOR:
				orderBy = NoteTable.Cols_note.COLOR_N_INDEX;
				orderType = "DESC";
			break;
			
			case MethSort.DATE_ARCH_DESC:
				orderBy = NoteTable.Cols_note.DATE_ARCHIVE_N;
				orderType = "DESC";
			break;
			
			case MethSort.DATE_ARCH_ASC:
				orderBy = NoteTable.Cols_note.DATE_ARCHIVE_N;
				orderType = "ASC";
			break;
			
			case MethSort.DATE_RECYC_DESC:
				orderBy = NoteTable.Cols_note.DATE_RECYCLE_N;
				orderType = "DESC";
			break;
			
			case MethSort.DATE_RECYC_ASC:
				orderBy = NoteTable.Cols_note.DATE_RECYCLE_N;
				orderType = "ASC";
			break;
		}
		
	
		NoteCursorWrapper cursor = queryNotes(whereClause, typeNote, orderBy, orderType);
		try {
			cursor.moveToFirst();
			while(!cursor.isAfterLast()) {
				notes.add(cursor.getNote());
				cursor.moveToNext();
			}
		} finally {
				cursor.close();
		}
			
		return notes;	
	}
	
	private NoteCursorWrapper querySeach(String searchString) {
		Cursor cursor = mDatabase.query(
				NoteTable.NAME_NT,
				null,	//Columns - select all columns
				NoteTable.Cols_note.TITLE_N + " LIKE ? OR " + NoteTable.Cols_note.TEXT_N + " LIKE ?",	//whereClause Title&Text
				new String[] {"%"+ searchString + "%", "%"+ searchString + "%"},	//whereArgs search string
				null,	//groupBy
				null,	//having
				NoteTable.Cols_note.DATE_CREATED_N + " DESC"//
			);
		
		return new NoteCursorWrapper(cursor);
	}
	
	public List<Note> searchNote(String searchString) {
		List<Note> notes = new ArrayList<Note>();
		NoteCursorWrapper cursor = querySeach(searchString);
		try {
			cursor.moveToFirst();			
			while(!cursor.isAfterLast()) {
				notes.add(cursor.getNote());
				cursor.moveToNext();
			}			
		} finally {
				cursor.close();
		}
		
		return notes;
	}
	
	private NoteCursorWrapper queryGetNoteReminder(String id) {
		Cursor cursor = mDatabase.query(
				NoteTable.NAME_NT,
				null,	//Columns - select all columns
				NoteTable.Cols_note.UUID_N + " =? ",	//
				new String[] {id},	//
				null,	//groupBy
				null,	//having
				null	//orderBy
			);
		
		return new NoteCursorWrapper(cursor);
	}
	
	public Note getNoteReminder(String id) {
		NoteCursorWrapper cursor = queryGetNoteReminder(id);
		try {
			if (cursor.getCount() != 0) {
				cursor.moveToFirst();
				return cursor.getNote();
			} else {
				return null;
			}			
		} finally {
			cursor.close();
		}
	}
		
	public int setCryptTextNote(String cryptTextNote, String idNote, boolean isEncrypt) {
		ContentValues values = new ContentValues();
		values.put(NoteTable.Cols_note.TEXT_N, cryptTextNote);
		values.put(NoteTable.Cols_note.ENCRYPT_STATUS_N, isEncrypt ? 1:0);
		mDatabase.update(NoteTable.NAME_NT, values, NoteTable.Cols_note.UUID_N + " = ? ", new String[] {idNote});
		
		return 0;
	}
	
	public int setReminderDate(String id, long reminderDate, int rCR) {
		ContentValues values = new ContentValues();
		values.put(NoteTable.Cols_note.DATE_REMINDER_N, reminderDate);
		values.put(NoteTable.Cols_note.RC_REMINDER_N, rCR);
		mDatabase.update(NoteTable.NAME_NT, values, NoteTable.Cols_note.UUID_N + " = ? ", new String[] {id});
		
		return 0;
	}
	
	public int clearReminderDate(String id) {
		ContentValues values = new ContentValues();
		values.put(NoteTable.Cols_note.DATE_REMINDER_N, Long.valueOf("0"));
		values.put(NoteTable.Cols_note.RC_REMINDER_N, Preferences.START_RCR);
		mDatabase.update(NoteTable.NAME_NT, values, NoteTable.Cols_note.UUID_N + " = ? ", new String[] {id});
		
		return 0;
	}
	
	public  int delEncryptedNotes(String[] id) {
		return mDatabase.delete(NoteTable.NAME_NT, NoteTable.Cols_note.UUID_N + "= ?", id);
	}
	
	private NoteCursorWrapper queryGetAllNotes() {
		Cursor cursor = mDatabase.query(
				NoteTable.NAME_NT,
				null,	//Columns - select all columns
				null,	//
				null,	//
				null,	//groupBy
				null,	//having
				null	//orderBy
			);
		
		return new NoteCursorWrapper(cursor);
	}
	
	public List<Note> getAllNotes() {
		List<Note> notes = new ArrayList<Note>();
		NoteCursorWrapper cursor = queryGetAllNotes();
		try {
			cursor.moveToFirst();
			while(!cursor.isAfterLast()) {				
				notes.add(cursor.getNote());
				cursor.moveToNext();				
			}
		} finally {
				cursor.close();
		}
		return notes;
	}
	
	public int updateScrollPosition(String id, int newScrollPosition) {
		ContentValues values = new ContentValues();
		values.put(NoteTable.Cols_note.SCROLL_POSITION_N, newScrollPosition);
		mDatabase.update(NoteTable.NAME_NT, values, NoteTable.Cols_note.UUID_N + " = ? ", new String[] {id});
		
		return 0;
	}
	
}