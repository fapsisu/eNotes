package su.fapsi.enotes;

import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import su.fapsi.enotes.Utils.Notes;

public class ArchiveNotesListActivity extends NotesListBaseActivity {
	
	@Override
	public void onResume() {
		super.onResume();
		
		navigationDrawerFragment = (NavigationDrawerFragment) getFragmentManager()
				.findFragmentById(R.id.navigation_drawer);
		
		navigationDrawerFragment.setUp(R.id.navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), ArchiveNotesListActivity.this);
		
		setTypeNote(Notes.NOTETYPE_ARCHIVE);
		setSpMethodSort();
	}
		
	@Override
	public void onBackPressed() {
		startActivity(new Intent(this, NotesListActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
		finish();
	}

}