package su.fapsi.enotes;

import java.util.Date;
import java.util.UUID;

import android.os.Parcel;
import android.os.Parcelable;
import su.fapsi.enotes.Utils.*;

public class Note implements Parcelable {
	private UUID mUuidN;
	private Date mDateCreateN;
	private Date mDateLastEditN;
	private String mTypeNote;
	private String mTitleN;
	private String mTextN;
	private boolean mEncryptStatusN;
	private int mColorNoteIndex;
	private Date mDateReminderN;
	private Date mDateArchiveN;
	private Date mDateRecycleN;
	private int mRcReminderN;
	private int mScrollPositionN;
	
	public Note() {
		setUuidN(UUID.randomUUID());
		setTypeNote(Notes.NOTETYPE_NOTE);
		setTextN("");
		setDateCreateN(new Date());
		setEncryptStatusN(false);
		setDateReminderN(new Date(Long.valueOf("0")));
		setDateArchiveN(new Date(Long.valueOf("0")));
		setDateRecycleN(new Date(Long.valueOf("0")));
		setRcReminderN(Preferences.START_RCR);
		setScrollPositionN(0);
	}
	
	
	public Note(UUID uuid) {
		setUuidN(uuid);
	}
	
	
	
	//inflate Note
	public UUID getUuidN() {
		return mUuidN;
	}
	
	public void setUuidN(UUID uuidN) {
		mUuidN = uuidN;
	}
		
	public String getTypeNote() {
		return mTypeNote;
	}
	
	public void setTypeNote(String typeNote) {
		mTypeNote = typeNote;
	}
	
	public Date getDateCreateN() {
		return mDateCreateN;
	}
	
	public void setDateCreateN(Date dateCreateN) {
		mDateCreateN = dateCreateN;
	}
	
	public Date getDateLastEditN() {
		return mDateLastEditN;
	}
	
	public void setDateLastEditN(Date dateLastEditN) {
		mDateLastEditN = dateLastEditN;
	}
	
	public String getTitleN() {
		return mTitleN;
	}
	
	public void setTitleN(String titleN) {
		mTitleN = titleN;
	}
	
	public String getTextN() {
		return mTextN;
	}
	
	public void setTextN(String textN) {
		mTextN = textN;
	}
		
	public boolean getEncryptStatusN() {
		return mEncryptStatusN;
	}
	
	public void setEncryptStatusN(boolean encryptStatus) {
		mEncryptStatusN = encryptStatus;
	}
	
	public int getColorNoteIndex() {
		return mColorNoteIndex;
	}
	
	public void setColorNoteIndex(int colorNoteIndex) {
		mColorNoteIndex = colorNoteIndex;
	}
	
	public Date getDateReminderN() {
		return mDateReminderN;
	}
	
	public void setDateReminderN (Date dateReminder) {
		mDateReminderN = dateReminder;
	}
	
	public Date getDateArchiveN() {
		return mDateArchiveN;
	}
	
	public void setDateArchiveN(Date dateArchive) {
		mDateArchiveN = dateArchive;
	}
	
	public Date getDateRecycleN() {
		return mDateRecycleN;
	}
	
	
	public void setDateRecycleN(Date dateRecycle) {
		mDateRecycleN = dateRecycle;
	}
	
	public int getRcReminderN() {
		return mRcReminderN;
	}
	
	
	public void setRcReminderN(int rqReminderN) {
		mRcReminderN = rqReminderN;
	}
	
	public int getScrollPositionN() {
		return mScrollPositionN;
	}


	public void setScrollPositionN(int scrollPositionN) {
		mScrollPositionN = scrollPositionN;
	}

	@Override
	public int describeContents() {
		return 0;
	}


	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeStringArray(new String[] {getUuidN().toString(), getTypeNote(), getTitleN(), getTextN()});
		dest.writeIntArray(new int[] {getEncryptStatusN() ? 1:0, getColorNoteIndex(), getRcReminderN(), getScrollPositionN()});
		dest.writeLongArray(new long[] {getDateCreateN().getTime(), getDateLastEditN().getTime(), 
				getDateReminderN().getTime(), getDateArchiveN().getTime(), getDateRecycleN().getTime()});
	}
	
	public static final Parcelable.Creator<Note> CREATOR = new Parcelable.Creator<Note>() {
		@Override
		public Note createFromParcel(Parcel in) {
			return new Note(in);
		}
	 
		@Override
		public Note[] newArray(int size) {
			return new Note[size];
		}
	};
	 
	private Note(Parcel parcel) {
		String[] strData = new String[4];
		int[] intData = new int[4];
		long[] longData = new long[5];
		parcel.readStringArray(strData);
		parcel.readIntArray(intData);
		parcel.readLongArray(longData);
		setUuidN(UUID.fromString(strData[0]));
		setDateCreateN(new Date(longData[0])); setDateLastEditN(new Date(longData[1]));
		setTypeNote(strData[1]);
		setTitleN(strData[2]); setTextN(strData[3]);
		setEncryptStatusN(intData[0]!=0); setColorNoteIndex(intData[1]); setRcReminderN(intData[2]); setScrollPositionN(intData[3]);
		setDateReminderN(new Date(longData[2]));
		setDateArchiveN(new Date(longData[3])); setDateRecycleN(new Date(longData[4]));
	}

}