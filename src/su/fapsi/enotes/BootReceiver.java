package su.fapsi.enotes;

import java.util.Date;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import su.fapsi.enotes.Utils.MethSort;
import su.fapsi.enotes.Utils.Notes;
import su.fapsi.enotes.Utils.Preferences;

public class BootReceiver extends BroadcastReceiver {
	private List<Note> notes;
	private Note note;
	private long currendDate;

	@Override
	public void onReceive(Context context, Intent intent) {
		currendDate = new Date().getTime();
		
		notes = NotesBase.get(context).getNotes(new String[] {Notes.NOTETYPE_NOTE}, MethSort.DATE_ARCH_DESC);
		for(int i=0; i<notes.size(); i++) {
			note = notes.get(i);
			if( (note.getRcReminderN()!=Preferences.START_RCR)&&(note.getDateReminderN().getTime()>currendDate) ) {
				Utils.setReminderIntent(context, note);
			} else {
				if(note.getDateReminderN().getTime()!=0) {
					NotesBase.get(context).clearReminderDate(note.getUuidN().toString());
				}				
			}
		}
	}
}