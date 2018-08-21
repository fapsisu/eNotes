package su.fapsi.enotes;

import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.LinearLayout;

public class SearchActivity extends NotesListBaseActivity {
	private MenuItem actionSearch;
	private EditText etSearch;
	
	private LinearLayout ll;
	
	@Override
	public void onResume() {
		super.onResume();
		setInvisibleMethodSort();
		
		if(etSearch!=null) {
			setNotesList(etSearch.getText().toString());
		}
		
		navigationDrawerFragment = (NavigationDrawerFragment) getFragmentManager()
				.findFragmentById(R.id.navigation_drawer);
		
		navigationDrawerFragment.setUp(R.id.navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), SearchActivity.this);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.search_menu, menu);
		actionSearch = menu.findItem(R.id.Search);
		actionSearch.expandActionView();
		
		ll = (LinearLayout) actionSearch.getActionView();
		etSearch = (EditText) ll.findViewById(R.id.etSearch);
		setNotesList(etSearch.getText().toString());
		
		addKeyListener();
		
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
			
			default:
				return super.onOptionsItemSelected(item);
		}
	}
	
	private void addKeyListener() {
		
		if(etSearch!=null) {
			etSearch.addTextChangedListener(new TextWatcher() {

				@Override
				public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				}

				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) {
					setNotesList(etSearch.getText().toString());
				}

				@Override
				public void afterTextChanged(Editable s) {
				}	
			});
		}
	}
	
	@Override
	public void onBackPressed() {
		startActivity(new Intent(this, NotesListActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
		finish();
	}
	
}