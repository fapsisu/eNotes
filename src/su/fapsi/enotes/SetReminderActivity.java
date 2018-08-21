package su.fapsi.enotes;

import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import su.fapsi.enotes.Utils.Preferences;

public class SetReminderActivity extends Activity {
	private TextView tvRNoteTitle;
	private TextView tvRDate;
	private DatePicker dpReminder;
	private TimePicker tpReminder;
	private Button btnSetReminder;
	
	private Note note;
	
	private int CR;
	
	private static final int MODE_SET = 1;
	private static final int MODE_CANCEL = 2;
	private static final int MODE_UPDATE = 3;
	
	private int modeSetReminder = MODE_SET;
	
	private Calendar calendar;
	private String reminderDate;
	
	private final Context context = SetReminderActivity.this;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.reminder_activity_layout);
		
		calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
		
		tvRNoteTitle = (TextView) findViewById(R.id.tvRNoteTitle);
		tvRDate = (TextView) findViewById(R.id.tvRDate);
		dpReminder = (DatePicker) findViewById(R.id.dpReminder);
		tpReminder = (TimePicker) findViewById(R.id.tpReminder);
		btnSetReminder = (Button) findViewById(R.id.btnSetReminder);
		
		dpReminder.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), 
				calendar.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {
			@Override
			public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				if(modeSetReminder == MODE_CANCEL) {
					modeSetReminder = MODE_UPDATE;
					btnSetReminder.setText(getResources().getString(R.string.btn_update_reminder));
				}				
			}			
		});
				
		
		tpReminder.setIs24HourView(true);
		tpReminder.setCurrentHour(Calendar.getInstance().get(Calendar.HOUR_OF_DAY));
		tpReminder.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
			@Override
			public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
				if(modeSetReminder == MODE_CANCEL) {
					modeSetReminder = MODE_UPDATE;
					btnSetReminder.setText(getResources().getString(R.string.btn_update_reminder));
				}
			}			
		});
		
		btnSetReminder.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				switch(modeSetReminder) {
					case MODE_SET: 
						setReminder(note, getDate());
					break;
					
					case MODE_CANCEL:
						cancelReminder(note);						
					break;
					
					case MODE_UPDATE:
						updateReminder(note, getDate());						
					break;
				}				
			}
		});	
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		note = getIntent().getParcelableExtra(Note.class.getCanonicalName());
		tvRNoteTitle.setText(getResources().getString(R.string.reminder_note_title) + " " + note.getTitleN());
		CR = note.getRcReminderN();
		if(CR>100000) {
			reminderDate = DateFormat.format("yyyy-MM-dd kk:mm:ss", note.getDateReminderN()).toString();
			tvRDate.setText(reminderDate);
			btnSetReminder.setText(getResources().getString(R.string.btn_reset_reminder));
			setPickersFromRemDate(reminderDate);
			modeSetReminder = MODE_CANCEL;
		}
	}
	
	private long getDate() {
		int day = dpReminder.getDayOfMonth();
		int month = dpReminder.getMonth();
		int year = dpReminder.getYear();
		
		int hour = tpReminder.getCurrentHour();
		int minute = tpReminder.getCurrentMinute();
		
		Calendar calendarTmp = Calendar.getInstance();
		calendarTmp.set(Calendar.YEAR, year);
		calendarTmp.set(Calendar.MONTH, month);
		calendarTmp.set(Calendar.DAY_OF_MONTH, day);
		calendarTmp.set(Calendar.HOUR_OF_DAY, hour);
		calendarTmp.set(Calendar.MINUTE, minute);
		calendarTmp.set(Calendar.SECOND, 0);
		
		return calendarTmp.getTimeInMillis();
	}
	
	private void setReminder(Note sNote, long lReminderDate) {
		SharedPreferences sp = Utils.getPreferences(context);
		int rCR = sp.getInt(Preferences.REQUEST_CODE_REMINDER, 0) + 1;
		Editor eSP = sp.edit();
		eSP.putInt(Preferences.REQUEST_CODE_REMINDER, rCR);
		eSP.commit();
		
		if(lReminderDate>new Date().getTime()) {
			sNote.setDateReminderN(new Date(lReminderDate));
			sNote.setRcReminderN(rCR);
			reminderDate = DateFormat.format("yyyy-MM-dd kk:mm:ss", sNote.getDateReminderN()).toString();
			tvRDate.setText(getResources().getString(R.string.reminder_date_title) + " " + reminderDate);
			setPickersFromRemDate(reminderDate);
			Utils.setReminderIntent(context, sNote);
			
			modeSetReminder = MODE_CANCEL;
			btnSetReminder.setText(getResources().getString(R.string.btn_reset_reminder));
			
			Toast.makeText(context, getResources().getString(R.string.msg_set_reminder), Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(context, getResources().getString(R.string.msg_wrong_reminder_date), Toast.LENGTH_SHORT).show();
		}		
	}
	
	private void updateReminder(Note uNote, long lReminderDate) {
		if(lReminderDate>new Date().getTime()) {
			uNote.setDateReminderN(new Date(lReminderDate));
			reminderDate = DateFormat.format("yyyy-MM-dd kk:mm:ss", uNote.getDateReminderN()).toString();
			tvRDate.setText(getResources().getString(R.string.reminder_date_title) + " " + reminderDate);
			setPickersFromRemDate(reminderDate);
			Utils.updateReminderIntent(context, uNote);
			
			modeSetReminder = MODE_CANCEL;
			btnSetReminder.setText(getResources().getString(R.string.btn_reset_reminder));
			
			Toast.makeText(context, getResources().getString(R.string.msg_update_reminder), Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(context, getResources().getString(R.string.msg_wrong_reminder_date), Toast.LENGTH_SHORT).show();
		}
	}
	
	private void cancelReminder(Note cNote) {
		Utils.cancelReminderIntent(context, cNote);
		tvRDate.setText("");
		setPickersDateNow();
		modeSetReminder = MODE_SET;
		btnSetReminder.setText(getResources().getString(R.string.btn_set_reminder));
		
		Toast.makeText(context, getResources().getString(R.string.msg_reset_reminder), Toast.LENGTH_SHORT).show();
	}
	
	private void setPickersFromRemDate(String remDate) {
		String[] dateTime = remDate.split(" ");
		String[] days = dateTime[0].split("-");
		String[] time = dateTime[1].split(":");
		int year = Integer.valueOf(days[0]);
		int month = Integer.valueOf(days[1])-1;
		int day = Integer.valueOf(days[2]);
		int hour = Integer.valueOf(time[0]);
		int minute = Integer.valueOf(time[1]);
		dpReminder.updateDate(year, month, day);
		tpReminder.setCurrentHour(hour);
		tpReminder.setCurrentMinute(minute);
	}
	
	private void setPickersDateNow() {
		Calendar calendarTmp = Calendar.getInstance();
		dpReminder.updateDate(calendarTmp.get(Calendar.YEAR), calendarTmp.get(Calendar.MONTH), calendarTmp.get(Calendar.DAY_OF_MONTH));
		tpReminder.setCurrentHour(calendarTmp.get(Calendar.HOUR_OF_DAY));
		tpReminder.setCurrentMinute(calendarTmp.get(Calendar.MINUTE));
	}
	
}