package su.fapsi.enotes;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import su.fapsi.enotes.Utils.*;

public class NotesListBaseActivity extends Activity implements NavigationDrawerFragment.NavigationDrawerCallbacks {
	
	private ListView lvNotes;
	private Spinner spMethodSort;
	
	private static List<Note> notes;
	
	private int statusQuery;
	
	private int methodSort;
	private String[] noteType;
	private int idArrayMethodSort;
	private String[] arrMethodSort;
			
	private static MyListNoteAdapter<Note> listNoteAdapter;
	private ArrayAdapter<?> adapterNoteSortMethod;
	
	private int[] colors;
	private int[] colors1;
	
	private AssetManager assetManager;
	
	protected NavigationDrawerFragment navigationDrawerFragment;
	
	private Note note;
	
	private ContextMenu cntxMenu;
	
	private String srchString = "";
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.base_list_notes_activity_layout);
		
		lvNotes = (ListView) findViewById(R.id.lvNotes);
		registerForContextMenu(lvNotes);
		
		spMethodSort = (Spinner) findViewById(R.id.spMethodSort);
		
		assetManager = getAssets();
		
		colors = Utils.getColors(getApplicationContext());
		colors1 = Utils.getColors1(getApplicationContext());
		
		
	}
	
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		cntxMenu = menu;
		super.onCreateContextMenu(cntxMenu, v, menuInfo);
		if(noteType[0].equals(Notes.NOTETYPE_NOTE)) {
			cntxMenu.add(Menu.NONE, CntxMenu.TO_ARCHIVE, Menu.NONE, getResources().getString(R.string.cntx_to_archive));
			cntxMenu.add(Menu.NONE, CntxMenu.TO_RECYCLE, Menu.NONE, getResources().getString(R.string.cntx_to_recycle));
			cntxMenu.add(Menu.NONE, CntxMenu.NOTE_TO_FILE, Menu.NONE, getResources().getString(R.string.cntx_save_as_text));
			cntxMenu.add(Menu.NONE, CntxMenu.EN_DE_CRYPT_NOTE, Menu.NONE, getResources().getString(R.string.cntx_crypt));
			cntxMenu.add(Menu.NONE, CntxMenu.REMINDER_NOTE, Menu.NONE, getResources().getString(R.string.cntx_reminder));
		}
		if(noteType[0].equals(Notes.NOTETYPE_ARCHIVE)) {
			cntxMenu.add(Menu.NONE, CntxMenu.UNPUCK, Menu.NONE, getResources().getString(R.string.cntx_unpack));
			cntxMenu.add(Menu.NONE, CntxMenu.TO_RECYCLE, Menu.NONE, getResources().getString(R.string.cntx_to_recycle));
			cntxMenu.add(Menu.NONE, CntxMenu.NOTE_TO_FILE, Menu.NONE, getResources().getString(R.string.cntx_save_as_text));
			cntxMenu.add(Menu.NONE, CntxMenu.EN_DE_CRYPT_NOTE, Menu.NONE, getResources().getString(R.string.cntx_crypt));
		}
		if(noteType[0].equals(Notes.NOTETYPE_RECYCLE)) {
			cntxMenu.add(Menu.NONE, CntxMenu.RECOVE, Menu.NONE, getResources().getString(R.string.cntx_recovery));
			cntxMenu.add(Menu.NONE, CntxMenu.DELETE, Menu.NONE, getResources().getString(R.string.cntx_delete));
		}
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		final AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		note = notes.get(info.position);
		switch (item.getItemId()) {
			case CntxMenu.TO_ARCHIVE:
				Utils.setCtxMenu(CntxMenu.TO_ARCHIVE, getResources().getString(R.string.title_to_archive), 
						getResources().getString(R.string.msg_to_archive), note.getUuidN(), NotesListBaseActivity.this, info.position);
			return true;
			
			case CntxMenu.TO_RECYCLE:
				Utils.setCtxMenu(CntxMenu.TO_RECYCLE, getResources().getString(R.string.title_to_recycle), 
						getResources().getString(R.string.msg_to_recycle), note.getUuidN(), NotesListBaseActivity.this, info.position);			
			return true;
			
			case CntxMenu.UNPUCK:
				statusQuery = NotesBase.get(getApplicationContext()).recoveryNote(new String[] {note.getUuidN().toString()});
				removeNoteFromList(info.position);
			return true;
			
			case CntxMenu.RECOVE:
				statusQuery = NotesBase.get(getApplicationContext()).recoveryNote(new String[] {note.getUuidN().toString()});
				removeNoteFromList(info.position);
			return true;
			
			case CntxMenu.DELETE:
				Utils.setCtxMenu(CntxMenu.DELETE, getResources().getString(R.string.title_note_delete), 
						getResources().getString(R.string.msg_note_delete), note.getUuidN(), NotesListBaseActivity.this, info.position);		
			return true;
			
			case CntxMenu.NOTE_TO_FILE:
				Utils.exportNoteToTxt(note, this);
			return true;
			
			case CntxMenu.EN_DE_CRYPT_NOTE:
				if(!note.getEncryptStatusN()) {
					if(Cryptography.isSetMasterPasswordEncryptNote(getApplicationContext())) {
						cryptPasswordDialog(true);
					} else {
						Intent intentSetMP = new Intent(this, SetMasterPasswordActivity.class);
						intentSetMP.putExtra(Password.KEY_CALL_SOURCE, Password.NOTE_LIST_CALL_SOURCE);
						startActivityForResult(intentSetMP, Password.RC_SP_FNL);
					}
				} else {
					cryptPasswordDialog(false);
				}
			return true;
			
			case CntxMenu.REMINDER_NOTE:
				startActivity(new Intent(getApplicationContext(), SetReminderActivity.class).putExtra(Note.class.getCanonicalName(), note));
			return true;
		}
		return true;
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode==Password.RC_SP_FNL&&resultCode==RESULT_OK) {
			cryptPasswordDialog(true);
		}
	}
	
	private void cryptPasswordDialog(final boolean isNoteEncrypt) {
		AlertDialog.Builder pdBuilder = new AlertDialog.Builder(this);
		final AlertDialog spDialog = pdBuilder.create();
		LayoutInflater pdInflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
		
		View pdView = pdInflater.inflate(R.layout.password_dialog_crypt, null);
		final EditText etMasterPasswordDialog = (EditText) pdView.findViewById(R.id.etMasterPasswordDialog);
		final Button btnOkMasterPasswordDialog = (Button) pdView.findViewById(R.id.btnOkMasterPasswordDialog);
		
		spDialog.setTitle(getResources().getString(R.string.master_password));
		spDialog.setView(pdView);
		
		spDialog.show();
		
		btnOkMasterPasswordDialog.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(Cryptography.compareMasterPasswordWithInput(etMasterPasswordDialog.getText().toString(), getApplicationContext())) {
					if(isNoteEncrypt) {
						Cryptography.encryptNote(note, etMasterPasswordDialog.getText().toString(), getApplicationContext());
						notes.clear();
						setNotesList();
						spDialog.dismiss();
					} else {
						Cryptography.decryptNote(note, etMasterPasswordDialog.getText().toString(), getApplicationContext());
						notes.clear();
						setNotesList();
						spDialog.dismiss();
					}	
				} else {
					Toast.makeText(getApplicationContext(), getResources().getString(R.string.msg_wrong_password), Toast.LENGTH_SHORT).show();
					etMasterPasswordDialog.setText("");
				}
			}
			
		});		
	}
	
	public static final void removeNoteFromList(int posNote) {
		notes.remove(posNote);
		listNoteAdapter.notifyDataSetChanged();
	}
	
	public static final void clearNotesList() {
		notes.clear();
		listNoteAdapter.notifyDataSetChanged();
	}
	
			
	private class MyListNoteAdapter<Note> extends ArrayAdapter<Note> {
		private class ViewHolder {
			private TextView tvNoteTitleItem;
			private ImageView ivNoteRecyclerIcoItem;
			private ImageView ivNoteEncryptIcoItem;
			private TextView tvNoteDateTimeItem;
			private LinearLayout llNoteItem;
			private LinearLayout subllNI;
		}
			
		public MyListNoteAdapter(Context context, int textViewResourceId, List<Note> items) {
			super(context, textViewResourceId, items);
		}
			
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder = new ViewHolder();
			if (convertView == null) {
				convertView = LayoutInflater.from(getContext()).inflate(R.layout.note_item, parent, false);
					
				viewHolder.llNoteItem = (LinearLayout) convertView.findViewById(R.id.llNoteItem);	
				viewHolder.tvNoteTitleItem = (TextView) convertView.findViewById(R.id.tvNoteTitleItem);
				viewHolder.subllNI = (LinearLayout) convertView.findViewById(R.id.subllNI);
				viewHolder.ivNoteRecyclerIcoItem = (ImageView) convertView.findViewById(R.id.ivNoteRecyclerIcoItem);
				viewHolder.ivNoteEncryptIcoItem = (ImageView) convertView.findViewById(R.id.ivNoteEncryptIcoItem);
				viewHolder.tvNoteDateTimeItem = (TextView) convertView.findViewById(R.id.tvNoteDateTimeItem);
					
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
				
				
			Note item = getItem(position);
			if (item!=null) {
				viewHolder.llNoteItem.setBackgroundColor(colors[((su.fapsi.enotes.Note) item).getColorNoteIndex()]);
				viewHolder.tvNoteTitleItem.setBackgroundColor(colors1[((su.fapsi.enotes.Note) item).getColorNoteIndex()]);
				viewHolder.subllNI.setBackgroundColor(colors1[((su.fapsi.enotes.Note) item).getColorNoteIndex()]);				
				viewHolder.tvNoteTitleItem.setText(((su.fapsi.enotes.Note) item).getTitleN().toString());
				if (((su.fapsi.enotes.Note) item).getTypeNote().equals(Notes.NOTETYPE_RECYCLE)) {
					viewHolder.ivNoteRecyclerIcoItem.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.recycle_ico));		
				} else {
					viewHolder.ivNoteRecyclerIcoItem.setImageBitmap(null);
				}
				if (((su.fapsi.enotes.Note) item).getRcReminderN()>Preferences.START_RCR) {
					viewHolder.ivNoteRecyclerIcoItem.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.alarm_ico));		
				} else {
					viewHolder.ivNoteRecyclerIcoItem.setImageBitmap(null);
				}
				if (((su.fapsi.enotes.Note) item).getEncryptStatusN()) {
					viewHolder.ivNoteEncryptIcoItem.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.padlock_ico));
				} else {
					viewHolder.ivNoteEncryptIcoItem.setImageBitmap(null);
				}
				if(methodSort==MethSort.DATE_CR_DESC||methodSort==MethSort.DATE_CR_ASC) {
					viewHolder.tvNoteDateTimeItem.setText(DateFormat.format("yyyy-MM-dd kk:mm:ss",((su.fapsi.enotes.Note) item).getDateCreateN()).toString());
				}
				if(methodSort==MethSort.DATE_ED_DESC||methodSort==MethSort.DATE_ED_ASC) {
					viewHolder.tvNoteDateTimeItem.setText(DateFormat.format("yyyy-MM-dd kk:mm:ss",((su.fapsi.enotes.Note) item).getDateLastEditN()).toString());
				}
				if(methodSort==MethSort.DATE_ARCH_DESC||methodSort==MethSort.DATE_ARCH_ASC) {
					viewHolder.tvNoteDateTimeItem.setText(DateFormat.format("yyyy-MM-dd kk:mm:ss",((su.fapsi.enotes.Note) item).getDateArchiveN()).toString());
				}
				if(methodSort==MethSort.DATE_RECYC_DESC||methodSort==MethSort.DATE_RECYC_ASC) {
					viewHolder.tvNoteDateTimeItem.setText(DateFormat.format("yyyy-MM-dd kk:mm:ss",((su.fapsi.enotes.Note) item).getDateRecycleN()).toString());
				}
				if(methodSort==MethSort.DATE_REM_DESC||methodSort==MethSort.DATE_REM_ASC) {
					if(((su.fapsi.enotes.Note) item).getDateReminderN().getTime()!=0) {
						viewHolder.tvNoteDateTimeItem.setText(DateFormat.format("yyyy-MM-dd kk:mm:ss",((su.fapsi.enotes.Note) item).getDateReminderN()).toString());
					} else {
						viewHolder.tvNoteDateTimeItem.setText("");
					}
				}
				if(methodSort==MethSort.AZ_DESC||methodSort==MethSort.AZ_ASC) {
					viewHolder.tvNoteDateTimeItem.setText(DateFormat.format("yyyy-MM-dd kk:mm:ss",((su.fapsi.enotes.Note) item).getDateCreateN()).toString());
				}
				if(methodSort==MethSort.BY_COLOR) {
					viewHolder.tvNoteDateTimeItem.setText(DateFormat.format("yyyy-MM-dd kk:mm:ss",((su.fapsi.enotes.Note) item).getDateCreateN()).toString());
				}
			}
			
			return convertView;
				
		}
		
	}
	
	public void setInvisibleMethodSort() {
		spMethodSort.setVisibility(View.GONE);
	}
		
	public void setTypeNote(String noteType) {
		this.noteType = new String[] {noteType};
	}
		
	public void setSpMethodSort() {
		if(noteType[0].equals(Notes.NOTETYPE_NOTE)) {
			idArrayMethodSort = R.array.sortBaseNotesMethods;
		}
		if(noteType[0].equals(Notes.NOTETYPE_ARCHIVE)) {
			idArrayMethodSort = R.array.sortArchiveNotesMethods;
		}
		if(noteType[0].equals(Notes.NOTETYPE_RECYCLE)) {
			idArrayMethodSort = R.array.sortRecycleNotesMethods;
		}
		arrMethodSort = getResources().getStringArray(idArrayMethodSort);
		
		adapterNoteSortMethod = ArrayAdapter.createFromResource(getApplicationContext(), idArrayMethodSort, R.layout.spinner_item);
		adapterNoteSortMethod.setDropDownViewResource(R.layout.spinner_item);
		spMethodSort.setAdapter(adapterNoteSortMethod);
		if(noteType[0].equals(Notes.NOTETYPE_NOTE)) {
			spMethodSort.setSelection(Utils.getDefaultIndexMethSort(this));
		} else {
			spMethodSort.setSelection(0);
		}		
		spMethodSort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				methodSort = Utils.getMethodSort(arrMethodSort[position], getApplicationContext());
				setNotesList();
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				
			}
		});
	}
	
	private void setNotesList() {
		notes = NotesBase.get(getApplicationContext()).getNotes(noteType, methodSort);
		listNoteAdapter = new MyListNoteAdapter<Note> (this, R.layout.note_item, notes);
		lvNotes.setAdapter(listNoteAdapter);
		lvNotes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Note note = notes.get(position);
				startActivity(new Intent(getApplicationContext(), NoteEditActivity.class)
						.putExtra(Notes.NOTE_EDITTYPE, Notes.NOTE_EDITTYPE_EDIT)
						.putExtra(Note.class.getCanonicalName(), note));
			}
		});
	}
	
	public void setNotesList(String searchString) {
		notes = NotesBase.get(getApplicationContext()).searchNote(searchString);
		if(!searchString.equals("")&&isNotWhitespace(searchString)) {
			listNoteAdapter = new MyListNoteAdapter<Note> (this, R.layout.note_item, notes);
			lvNotes.setAdapter(listNoteAdapter);
			srchString = searchString;
		} else {
			lvNotes.setAdapter(null);
		}
		
		lvNotes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Note note = notes.get(position);
				startActivity(new Intent(getApplicationContext(), NoteEditActivity.class)
						.putExtra(Notes.NOTE_EDITTYPE, Notes.NOTE_EDITTYPE_EDIT)
						.putExtra(Note.class.getCanonicalName(), note)
						.putExtra(Notes.NOTE_STRING_SEARCH, srchString));
			}
		});
	}
	
	
	private boolean isNotWhitespace(String string) {
		if(string.replaceAll("\\s+","")=="") {
			return false;
		}  else {
			return true;
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if (keyCode == KeyEvent.KEYCODE_MENU) {
	        event.startTracking();
	        navigationDrawerFragment.openCloseFragmentNavigation();
	        return true;
	    }
	    return super.onKeyDown(keyCode, event);
	}

	
	@Override
	public void onNavigationDrawerItemSelected(int position) {
		
	}

}