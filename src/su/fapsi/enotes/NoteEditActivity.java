package su.fapsi.enotes;

import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.GestureDetector;
import android.view.GestureDetector.OnDoubleTapListener;
import android.view.GestureDetector.OnGestureListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import su.fapsi.enotes.Utils.Notes;

public class NoteEditActivity extends Activity implements OnGestureListener, View.OnClickListener {
	private boolean isStart;
	private boolean isAdded;
	private boolean isModeView;
			
	private Note note;
	private String typeEditNote;
	
	private String stringTitle;
	private String stringText;
	
	
	private EditText tvTitleNote;
	private EditText etTitleNote;
	private Button btnSelectColor;
	private TextView tvDateCreated;
	private TextView tvDateLastEdition;
	private EditText tvTextNote;
	private EditText etTextNote;
	
	private ScrollView sv;
	
	private MenuItem itemNoteEdit;
	private MenuItem itemNoteSave;
	
	private GestureDetector gd;
		
	private RelativeLayout rlEditActivity;
	private LinearLayout llTitleLayout;
	private LinearLayout llDateLayout;
	private LinearLayout llTextLayout;
	
	private int colorIndex;
	private int[] colors;
	private int[] colors1;
	private int[] colors2;
	
	private PopupWindow ccPopupWindow;
	private View customView;
	private LinearLayout llChoiceColor;
	private LayoutInflater inflater;
	private Button btnColor0;
	private Button btnColor1;
	private Button btnColor2;
	private Button btnColor3;
	private Button btnColor4;
	private Button btnColor5;
	private Button btnColor6;
	private Button btnColor7;
	private Button btnColor8;
	private Button btnClose;
	
	private boolean isPasswordCorrect;
	
	private String masterPassword;
	
	private final Context context = NoteEditActivity.this;

	@Override
	protected void onCreate(Bundle savedInstanceState)  {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.note_edit_activity_layout);
		
		tvTitleNote = (EditText)findViewById(R.id.tvTitleNote);
		etTitleNote = (EditText)findViewById(R.id.etTitleNote);
		btnSelectColor = (Button)findViewById(R.id.btnSelectColor);
		tvDateCreated = (TextView)findViewById(R.id.tvDateCreated);
		tvDateLastEdition = (TextView)findViewById(R.id.tvDateLastEdition);
		tvTextNote = (EditText)findViewById(R.id.tvTextNote);
		etTextNote = (EditText)findViewById(R.id.etTextNote);
		
		tvTitleNote.setBackgroundResource(android.R.color.transparent);
		tvTextNote.setBackgroundResource(android.R.color.transparent);
		
		sv = (ScrollView) findViewById(R.id.sv);
				
		isStart = true;
		
		gd = new GestureDetector(this);
		
		rlEditActivity = (RelativeLayout) findViewById(R.id.rlEditActivity);
		llTitleLayout = (LinearLayout) findViewById(R.id.llTitleLayout);
		llDateLayout = (LinearLayout) findViewById(R.id.llDateLayout);
		llTextLayout = (LinearLayout) findViewById(R.id.llTextLayout);
		
		inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
		customView = inflater.inflate(R.layout.choice_color, null);
		llChoiceColor = (LinearLayout) customView.findViewById(R.id.llChoiceColor);
		btnColor0 = (Button) customView.findViewById(R.id.color0);
		btnColor1 = (Button) customView.findViewById(R.id.color1);
		btnColor2 = (Button) customView.findViewById(R.id.color2);
		btnColor3 = (Button) customView.findViewById(R.id.color3);
		btnColor4 = (Button) customView.findViewById(R.id.color4);
		btnColor5 = (Button) customView.findViewById(R.id.color5);
		btnColor6 = (Button) customView.findViewById(R.id.color6);
		btnColor7 = (Button) customView.findViewById(R.id.color7);
		btnColor8 = (Button) customView.findViewById(R.id.color8);
		btnClose = (Button) customView.findViewById(R.id.btnCloseCC);
		
		btnColor0.setOnClickListener(this);
		btnColor1.setOnClickListener(this);
		btnColor2.setOnClickListener(this);
		btnColor3.setOnClickListener(this);
		btnColor4.setOnClickListener(this);
		btnColor5.setOnClickListener(this);
		btnColor6.setOnClickListener(this);
		btnColor7.setOnClickListener(this);
		btnColor8.setOnClickListener(this);
		btnClose.setOnClickListener(this);
		
		colors = Utils.getColors(context);
		colors1 = Utils.getColors1(context);
		colors2 = Utils.getColors2(context);
		
	}
		
	@Override
	public void onResume() {
		super.onResume();
		
		typeEditNote = getIntent().getStringExtra(Notes.NOTE_EDITTYPE).toString();
		
		if(typeEditNote.equals(Notes.NOTE_EDITTYPE_ADD)) {
			if(isStart) {
				note = new Note();
				colorIndex = Utils.getDefaultNoteColor(this);
				note.setColorNoteIndex(colorIndex);
				setLayoutColor(colorIndex);
				tvDateCreated.setText(DateFormat.format("yyyy-MM-dd kk:mm:ss", note.getDateCreateN()).toString());
				isStart = false;
				isAdded = false;
				isModeView = false;
			}
		}
		
		if(typeEditNote.equals(Notes.NOTE_EDITTYPE_EDIT)) {
			note = (Note) getIntent().getParcelableExtra(Note.class.getCanonicalName());
			colorIndex = note.getColorNoteIndex();
			setLayoutColor(colorIndex);
			if(!note.getEncryptStatusN()) {
				setLayoutOnView();
			} else {
				isPasswordCorrect = false;
				
				etTitleNote.setText("");
				etTextNote.setText("");
				etTitleNote.setVisibility(View.GONE);
				etTextNote.setVisibility(View.GONE);
				btnSelectColor.setVisibility(View.GONE);
				tvTitleNote.setVisibility(View.VISIBLE);
				tvTextNote.setVisibility(View.VISIBLE);
				tvTitleNote.setText(note.getTitleN());
				tvTextNote.setText("");
				
				
				showPasswordDialog();
			}
			
			tvDateCreated.setText(DateFormat.format("yyyy-MM-dd kk:mm:ss", note.getDateCreateN()).toString());
			tvDateLastEdition.setText(DateFormat.format("yyyy-MM-dd kk:mm:ss", note.getDateLastEditN()).toString());
			
			isAdded = true;
			
		}
		
		tvTitleNote.setOnTouchListener(new View.OnTouchListener() {
	        @Override
	        public boolean onTouch(View v, MotionEvent event) {
	            gd.onTouchEvent(event);
	            return false;
	        }
	    });
		
		tvTextNote.setOnTouchListener(new View.OnTouchListener() {
	        @Override
	        public boolean onTouch(View v, MotionEvent event) {
	           gd.onTouchEvent(event);
	           return false;
	        }
	    });
		
		
		
		gd.setOnDoubleTapListener(new OnDoubleTapListener() {
		    @Override
		    public boolean onDoubleTap(MotionEvent e) {
		    	if(!note.getTypeNote().equals(Notes.NOTETYPE_RECYCLE)&&(!note.getEncryptStatusN()||isPasswordCorrect)) {
		    		setLayoutOnEdit();
		    	} 	
		    	return false;
		    }

		    @Override
		    public boolean onDoubleTapEvent(MotionEvent e) {
		    	return false;
		    }

		    @Override
		    public boolean onSingleTapConfirmed(MotionEvent e) {
		    	if(isModeView&&!note.getTypeNote().equals(Notes.NOTETYPE_RECYCLE)&&(!note.getEncryptStatusN()||isPasswordCorrect)) {
		    		Toast.makeText(context, getResources().getString(R.string.msg_dblcl_4_edit), Toast.LENGTH_SHORT).show();
		    	}
		    	return false;
		    }

		});
		
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.edit_note_menu, menu);
		
		itemNoteEdit = menu.findItem(R.id.NoteEdit);
		itemNoteSave = menu.findItem(R.id.NoteSave);
		if(itemNoteSave!=null) {
			itemNoteSave.setVisible(false);
		}
		if(itemNoteEdit!=null) {
			itemNoteEdit.setVisible(false);
		}
				
		if(typeEditNote.equals(Notes.NOTE_EDITTYPE_ADD)) {
			if(itemNoteSave!=null) {
				itemNoteSave.setVisible(true);
			}
		}
		
		if(typeEditNote.equals(Notes.NOTE_EDITTYPE_EDIT)&&(!note.getEncryptStatusN()||isPasswordCorrect)) {
			if(itemNoteEdit!=null&&!note.getTypeNote().equals(Notes.NOTETYPE_RECYCLE)) {
				itemNoteEdit.setVisible(true);
			}
		}

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
		case R.id.NoteSave:
			if (!isAdded) {
				addNote();
			} else {
					updateNote();
			}
			
		return true;
			
		
		case R.id.NoteEdit:
			setLayoutOnEdit();
			
		return true;
		
		default:
		return super.onOptionsItemSelected(item);
		}
	}
	
	@Override
	public void onPause() {
		if(ccPopupWindow!=null) {
			ccPopupWindow.dismiss();
			ccPopupWindow = null;
		}
		
		if(typeEditNote.equals(Notes.NOTE_EDITTYPE_ADD)) {
			if(!isAdded) {
				addNote();
			} else {
				if( (!isModeView&&(!note.getEncryptStatusN()||isPasswordCorrect)) ) {
					updateNote();
				}
			}
		}
		
		if(typeEditNote.equals(Notes.NOTE_EDITTYPE_EDIT)) {
			if( (!isModeView&&(!note.getEncryptStatusN()||isPasswordCorrect)) ) {
				updateNote();
			}
		}
		if(sv.getScrollY()!=note.getScrollPositionN()) {
			NotesBase.get(context).updateScrollPosition(note.getUuidN().toString(), sv.getScrollY());
		}
		super.onPause();
	}
	
	@Override
	public void onBackPressed() {
		if(!isAdded) {
			addNote();			
		} else {
			if( (!isModeView&&(!note.getEncryptStatusN()||isPasswordCorrect)) ) {
				updateNote();
			} else {
				if(sv.getScrollY()!=note.getScrollPositionN()) {
					NotesBase.get(context).updateScrollPosition(note.getUuidN().toString(), sv.getScrollY());
				}
				super.onBackPressed();
			}
		}
	}
	
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.btnSelectColor:
				ccPopupWindow = new PopupWindow(customView, android.view.ViewGroup.LayoutParams.WRAP_CONTENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT, true);
				ccPopupWindow.showAtLocation(llChoiceColor, Gravity.CENTER, 0, 0);
				break;
			
			case R.id.color0:
				colorIndex = 0;
				setLayoutColor(colorIndex);
				break;
			case R.id.color1:
				colorIndex = 1;
				setLayoutColor(colorIndex);
				break;
			case R.id.color2:
				colorIndex = 2;
				setLayoutColor(colorIndex);
				break;
			case R.id.color3:
				colorIndex = 3;
				setLayoutColor(colorIndex);
				break;
			case R.id.color4:
				colorIndex = 4;
				setLayoutColor(colorIndex);
				break;
			case R.id.color5:
				colorIndex = 5;
				setLayoutColor(colorIndex);
				break;
			case R.id.color6:
				colorIndex = 6;
				setLayoutColor(colorIndex);
				break;
			case R.id.color7:
				colorIndex = 7;
				setLayoutColor(colorIndex);
				break;
			case R.id.color8:
				colorIndex = 8;
				setLayoutColor(colorIndex);
				break;
			case R.id.btnCloseCC:
				if(ccPopupWindow!=null) {
					ccPopupWindow.dismiss();
					ccPopupWindow = null;
				}
				break;
		}
		
	}
	
	private void showPasswordDialog() {
		AlertDialog.Builder pdBuilder = new AlertDialog.Builder(context);
		final AlertDialog spDialog = pdBuilder.create();
		LayoutInflater pdInflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
		
		View pdView = pdInflater.inflate(R.layout.password_dialog_crypt, null);
		final EditText etMasterPasswordDialog = (EditText) pdView.findViewById(R.id.etMasterPasswordDialog);
		final Button btnOkMasterPasswordDialog = (Button) pdView.findViewById(R.id.btnOkMasterPasswordDialog);
		
		spDialog.setTitle(getResources().getString(R.string.master_password));
		spDialog.setView(pdView);
		
		spDialog.show();
		
		btnOkMasterPasswordDialog.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(Cryptography.compareMasterPasswordWithInput(etMasterPasswordDialog.getText().toString(), context)) {
					isPasswordCorrect = true;
					masterPassword = etMasterPasswordDialog.getText().toString();
					tvTextNote.setText(Cryptography.getDecryptedNoteText(note.getTextN(), masterPassword));

					isModeView = true;
					setMenuItemVisible(isModeView);
					
					spDialog.dismiss();
				} else {
					Toast.makeText(context, getResources().getString(R.string.msg_wrong_password), Toast.LENGTH_SHORT).show();
					etMasterPasswordDialog.setText("");
				}
			}	
		});
	}
	
	private void setLayoutColor(int colIndex) {
			btnSelectColor.setBackgroundColor(colors[colIndex]);
			rlEditActivity.setBackgroundColor(colors2[colIndex]);
			llTitleLayout.setBackgroundColor(colors1[colIndex]);
			llDateLayout.setBackgroundColor(colors1[colIndex]);
			llTextLayout.setBackgroundColor(colors2[colIndex]);
		
		if(ccPopupWindow!=null) {
			ccPopupWindow.dismiss();
			ccPopupWindow = null;
		}
	}
	
	
	private void addNote() {
		note.setScrollPositionN(sv.getScrollY());
		if(!etTitleNote.getText().toString().equals("")||!etTextNote.getText().toString().equals("")) {
			note.setTitleN(etTitleNote.getText().toString());
			note.setTextN(etTextNote.getText().toString());
			note.setDateLastEditN(new Date());
			tvDateLastEdition.setText(DateFormat.format("yyyy-MM-dd kk:mm:ss", note.getDateLastEditN()).toString());
			note.setColorNoteIndex(colorIndex);
			
			setLayoutOnView();
			
			isAdded = true;
			
			int statusAddNote = NotesBase.get(context).addNote(note);
			Toast.makeText(context, getResources().getString(R.string.msg_note_added), Toast.LENGTH_SHORT).show();
		} else {
			isAdded = true;
			isModeView = true;
			onBackPressed();
		}
		
	}
	
	private void updateNote() {
		note.setScrollPositionN(sv.getScrollY());
		stringTitle = etTitleNote.getText().toString();
		stringText = etTextNote.getText().toString();
		if( ( !note.getEncryptStatusN()&&note.getTitleN().equals(stringTitle)&&note.getTextN().equals(stringText) ) ||
				( note.getEncryptStatusN()&&note.getTitleN().equals(stringTitle)&&Cryptography.getDecryptedNoteText(note.getTextN(), masterPassword).equals(stringText) ) )  {
			if(note.getColorNoteIndex()!=colorIndex) {
				note.setColorNoteIndex(colorIndex);
				int statusChangeColor = NotesBase.get(context).changeColorIndex(note);
			} 
			setLayoutOnView();
			
		} else {
			note.setTitleN(stringTitle);
			if(!note.getEncryptStatusN()) {
				note.setTextN(stringText);
			} else {
				String encryptedText = Cryptography.getEncryptedNoteText(stringText, masterPassword);
				note.setTextN(encryptedText);
			}			
			note.setDateLastEditN(new Date());
			note.setColorNoteIndex(colorIndex);
			tvDateLastEdition.setText(DateFormat.format("yyyy-MM-dd kk:mm:ss", note.getDateLastEditN()).toString());
			setLayoutOnView();
			int statusUpdateNote = NotesBase.get(context).updateNote(note);
			Toast.makeText(context, getResources().getString(R.string.msg_note_updated), Toast.LENGTH_SHORT).show();
		}
	}
	
	private void setLayoutOnView() {
		sv.postDelayed(new Runnable() {
			@Override
			public void run() {
				sv.scrollTo(0, note.getScrollPositionN());
			}
		}, 180);
		etTitleNote.setText("");
		etTextNote.setText("");
		etTitleNote.setVisibility(View.GONE);
		etTextNote.setVisibility(View.GONE);
		btnSelectColor.setVisibility(View.GONE);
		tvTitleNote.setVisibility(View.VISIBLE);
		tvTextNote.setVisibility(View.VISIBLE);
		tvTitleNote.setText(note.getTitleN());
		if(!note.getEncryptStatusN()) {
			tvTextNote.setText(note.getTextN());
		} else {
			String decryptedText = Cryptography.getDecryptedNoteText(note.getTextN(), masterPassword);
			tvTextNote.setText(decryptedText);
		}
		
		if(typeEditNote.equals(Notes.NOTE_EDITTYPE_EDIT)&&getIntent().getStringExtra(Notes.NOTE_STRING_SEARCH)!=null) {	
			String searchString = getIntent().getStringExtra(Notes.NOTE_STRING_SEARCH);
			int index = tvTextNote.getText().toString().indexOf(searchString);
			tvTextNote.setFocusable(true);
    		tvTextNote.requestFocus();
    		tvTextNote.setSelection(index, index + searchString.length());
		}
		
		isModeView = true;
		setMenuItemVisible(isModeView);
	}
	
	private void setLayoutOnEdit() {
		sv.postDelayed(new Runnable() {
			@Override
			public void run() {
				sv.scrollTo(0, note.getScrollPositionN());
			}
		}, 180);
		tvTitleNote.setText("");
		tvTextNote.setText("");
		tvTitleNote.setVisibility(View.GONE);
		tvTextNote.setVisibility(View.GONE);
		
		etTitleNote.setVisibility(View.VISIBLE);
		etTextNote.setVisibility(View.VISIBLE);
		btnSelectColor.setVisibility(View.VISIBLE);
		etTitleNote.setText(note.getTitleN());
		
		if(!note.getEncryptStatusN()) {
			etTextNote.setText(note.getTextN());
		} else {
			String decryptedText = Cryptography.getDecryptedNoteText(note.getTextN(), masterPassword);
			etTextNote.setText(decryptedText);
		}
		
		isModeView = false;
		setMenuItemVisible(isModeView);
	}
	
	private void setMenuItemVisible(boolean isView) {
		if(isView) {
			if(itemNoteSave!=null) {
				itemNoteSave.setVisible(false);
			}
			if(itemNoteEdit!=null&&!note.getTypeNote().equals(Notes.NOTETYPE_RECYCLE)) {
				itemNoteEdit.setVisible(true);
			}
		} else {
			if(itemNoteSave!=null) {
				itemNoteSave.setVisible(true);
			}
			if(itemNoteEdit!=null) {
				itemNoteEdit.setVisible(false);
			}
		}
	}
	
	@Override  
    public boolean onTouchEvent(MotionEvent event) {
		return gd.onTouchEvent(event);
	}

	@Override
	public boolean onDown(MotionEvent e) {
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		return false;
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
		return false;
	}

	
}