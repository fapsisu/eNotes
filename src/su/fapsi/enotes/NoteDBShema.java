package su.fapsi.enotes;

public class NoteDBShema {
	public static final class NoteTable{
		public static final String NAME_NT = "notes";
		public static final class Cols_note {
			public static final String UUID_N = "uuid_n";
			public static final String DATE_CREATED_N = "date_created_n";
			public static final String DATE_LAST_EDIT_N = "date_last_edit_n";
			public static final String TYPE_N = "type_n";
			public static final String TITLE_N = "title_n";
			public static final String TEXT_N = "text_n";
			public static final String ENCRYPT_STATUS_N = "encrypt_status_n";
			public static final String COLOR_N_INDEX = "color_n_index";
			public static final String DATE_REMINDER_N = "date_reminder_n";
			public static final String DATE_ARCHIVE_N = "date_archive_n";
			public static final String DATE_RECYCLE_N = "date_recycle_n";
			public static final String RC_REMINDER_N = "code_reminder_n";
			public static final String SCROLL_POSITION_N = "scroll_position_n";
		}
	}
	
}