package su.fapsi.enotes;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import su.fapsi.enotes.NoteDBShema.NoteTable;

public class NotesBaseHelper extends SQLiteOpenHelper {
	public static final int VERSION = 1;
	public static final String DATABASE_NAME = "notesBase.db";
	
	public NotesBaseHelper(Context context) {
		super(context, DATABASE_NAME, null, VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table " + NoteDBShema.NoteTable.NAME_NT + 
				"(" + "_id_n integer primary key autoincrement," + 
				NoteTable.Cols_note.UUID_N + ", " +
				NoteTable.Cols_note.DATE_CREATED_N + ", " +
				NoteTable.Cols_note.DATE_LAST_EDIT_N + ", " +
				NoteTable.Cols_note.TYPE_N + ", " +
				NoteTable.Cols_note.TITLE_N + ", " +
				NoteTable.Cols_note.TEXT_N + ", " +
				NoteTable.Cols_note.ENCRYPT_STATUS_N + ", " +
				NoteTable.Cols_note.COLOR_N_INDEX + ", " +
				NoteTable.Cols_note.DATE_REMINDER_N + ", " +
				NoteTable.Cols_note.DATE_ARCHIVE_N + ", " +
				NoteTable.Cols_note.DATE_RECYCLE_N + ", " +
				NoteTable.Cols_note.RC_REMINDER_N + ", " +
				NoteTable.Cols_note.SCROLL_POSITION_N +
				")"
			);
		
				
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}

}