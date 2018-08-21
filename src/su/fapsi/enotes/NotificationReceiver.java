package su.fapsi.enotes;

import java.util.Date;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import su.fapsi.enotes.Utils.Notes;
import su.fapsi.enotes.Utils.Preferences;

public class NotificationReceiver extends BroadcastReceiver {
	private Note note;

	@Override
	public void onReceive(Context context, Intent intent) {
		int rCR = intent.getIntExtra(Preferences.REQUEST_CODE_REMINDER, 0);
		String nUuid = intent.getStringExtra(Preferences.NOTE_R_UUID);
		note = NotesBase.get(context).getNoteReminder(nUuid);
		NotesBase.get(context).clearReminderDate(note.getUuidN().toString());
		note.setRcReminderN(Preferences.START_RCR);
		note.setDateReminderN(new Date(Long.valueOf("0")));
		
		if(rCR>Preferences.START_RCR&&note!=null) {
			Intent nIntent = new Intent(context, NoteEditActivity.class)
					.putExtra(Notes.NOTE_EDITTYPE, Notes.NOTE_EDITTYPE_EDIT)
					.putExtra(Note.class.getCanonicalName(), note);
			PendingIntent nPendingIntent = PendingIntent.getActivity(context, 0, nIntent,
		               PendingIntent.FLAG_UPDATE_CURRENT);
			
			if(Utils.isSetNotoficationVibrate(context)) {
				final Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
				Thread thread = new Thread(new Runnable() {
					@Override
					public void run() {
						v.vibrate(1000);
					}
				});
				thread.start();
			}			
			
			NotificationCompat.Builder nBuilder = new NotificationCompat.Builder(context)
				.setSmallIcon(R.drawable.icon_notification_note)
				.setContentText(note.getTitleN())
				.setTicker("eNotes")
				.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
				.setContentIntent(nPendingIntent)
				.setAutoCancel(true);
			Notification nNotification = nBuilder.build();
			NotificationManager notificationManager = 
				(NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
			notificationManager.notify(rCR, nNotification);			
		}
	}

}