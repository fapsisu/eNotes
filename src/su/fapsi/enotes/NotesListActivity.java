package su.fapsi.enotes;

import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import su.fapsi.enotes.Utils.Notes;

public class NotesListActivity extends NotesListBaseActivity {

	@Override
	public void onResume() {
		super.onResume();
		
		navigationDrawerFragment = (NavigationDrawerFragment) getFragmentManager()
				.findFragmentById(R.id.navigation_drawer);
		
		navigationDrawerFragment.setUp(R.id.navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), NotesListActivity.this);
		
		setTypeNote(Notes.NOTETYPE_NOTE);
		setSpMethodSort();
	}

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.notes_list_menu, menu);
		
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
			case R.id.NoteSearch:
				startActivity(new Intent(this, SearchActivity.class));
			return true;
		
			case R.id.NoteAdd:
				startActivity(new Intent(this, NoteEditActivity.class)
					.putExtra(Notes.NOTE_EDITTYPE, Notes.NOTE_EDITTYPE_ADD));
				return true;
			
			default:
				return super.onOptionsItemSelected(item);
		}
	}
}