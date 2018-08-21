package su.fapsi.enotes;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import su.fapsi.enotes.Utils.Notes;

public class RecycleNotesListActivity extends NotesListBaseActivity {
	
	@Override
	public void onResume() {
		super.onResume();
		
		navigationDrawerFragment = (NavigationDrawerFragment) getFragmentManager()
				.findFragmentById(R.id.navigation_drawer);
		
		navigationDrawerFragment.setUp(R.id.navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), RecycleNotesListActivity.this);
		
		setTypeNote(Notes.NOTETYPE_RECYCLE);
		setSpMethodSort();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.recycle_menu, menu);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
				
			case R.id.clearRecycler:
				clearRecycle();
			return true;
			
			default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	private void clearRecycle() {
		AlertDialog.Builder crBuilder = new AlertDialog.Builder(this);
		final AlertDialog crDialog = crBuilder.create();
		crDialog.setTitle(getResources().getString(R.string.title_recycle_clear));
		crDialog.setMessage(getResources().getString(R.string.msg_recycle_clear));
		crDialog.setButton(getResources().getString(R.string.btnOk), new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				int statusQuery = NotesBase.get(getApplicationContext()).clearRecycler();
				NotesListBaseActivity.clearNotesList();
				crDialog.dismiss();
			}	
		}); 
		crDialog.setButton2(getResources().getString(R.string.btnCancel), new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				crDialog.dismiss();	
			}	
		});
		crDialog.show();
	}
	
	@Override
	public void onBackPressed() {
		startActivity(new Intent(this, NotesListActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
		finish();
	}

}