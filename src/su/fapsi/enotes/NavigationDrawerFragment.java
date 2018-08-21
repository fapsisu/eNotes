package su.fapsi.enotes;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class NavigationDrawerFragment extends Fragment implements View.OnClickListener {
	private View llFragmentNavigation;
	private View fnFragmentContainerView;
	private NavigationDrawerCallbacks fnCallbacks;
	private ActionBarDrawerToggle fnDrawerToggle;
	private DrawerLayout fnDrawerLayout;
	
	private LinearLayout llFnNotes;
	private LinearLayout llFnArchive;
	private LinearLayout llFnRecycle;
	private LinearLayout llFnSearch;
	private LinearLayout llFnSettings;
	private LinearLayout llFnNullLL;
	
	private Activity fnNameActivity;
	

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		llFragmentNavigation = inflater.inflate(R.layout.fragment_navigation_drawer, null);
		
		llFnNotes = (LinearLayout)llFragmentNavigation.findViewById (R.id.llFnNotes);
		llFnArchive = (LinearLayout) llFragmentNavigation.findViewById (R.id.llFnArchive);
		llFnRecycle = (LinearLayout) llFragmentNavigation.findViewById (R.id.llFnRecycle);
		llFnSearch = (LinearLayout) llFragmentNavigation.findViewById (R.id.llFnSearch);
		llFnSettings = (LinearLayout) llFragmentNavigation.findViewById (R.id.llFnSettings);
		llFnNullLL = (LinearLayout) llFragmentNavigation.findViewById (R.id.llFnNullLL);
		
		llFnNotes.setOnClickListener(this);
		llFnArchive.setOnClickListener(this);
		llFnRecycle.setOnClickListener(this);
		llFnSearch.setOnClickListener(this);
		llFnSettings.setOnClickListener(this);

		
		return llFragmentNavigation;
	}
	
	public boolean isDrawerOpen() {
		return fnDrawerLayout != null && fnDrawerLayout.isDrawerOpen(fnFragmentContainerView);
	}
	
	public void setUp(int fragmentId, DrawerLayout drawerLayout, Activity nameActivity) {
		fnFragmentContainerView = getActivity().findViewById(fragmentId);
		fnDrawerLayout = drawerLayout;
		
		fnDrawerToggle = new ActionBarDrawerToggle(
				getActivity(), fnDrawerLayout, 
				R.drawable.ic_drawer, 0, 0) {
			@Override
			public void onDrawerClosed(View drawerView) {
				super.onDrawerClosed(drawerView);
				if (!isAdded()) {
					return;
				}	
			}

			@Override
			public void onDrawerOpened(View drawerView) {
				super.onDrawerOpened(drawerView);
				if (!isAdded()) {
					return;
				}

			}
			
		};
		
		fnNameActivity = nameActivity;
		
		if(fnNameActivity.getClass().getSimpleName().equals(NotesListActivity.class.getSimpleName())) {
			llFnNotes.setBackgroundColor(R.color.checkItemMenu);
		}
		if(fnNameActivity.getClass().getSimpleName().equals(ArchiveNotesListActivity.class.getSimpleName())) {
			llFnArchive.setBackgroundColor(R.color.checkItemMenu);
		}
		if(fnNameActivity.getClass().getSimpleName().equals(RecycleNotesListActivity.class.getSimpleName())) {
			llFnRecycle.setBackgroundColor(R.color.checkItemMenu);
		}
		if(fnNameActivity.getClass().getSimpleName().equals(SearchActivity.class.getSimpleName())) {
			llFnSearch.setBackgroundColor(R.color.checkItemMenu);
		}
		if(fnNameActivity.getClass().getSimpleName().equals(SettingsActivity.class.getSimpleName())) {
			llFnSettings.setBackgroundColor(R.color.checkItemMenu);
		}
		
		fnDrawerLayout.closeDrawer(fnFragmentContainerView);

		// Defer code dependent on restoration of previous instance state.
		fnDrawerLayout.post(new Runnable() {
			@Override
			public void run() {
				fnDrawerToggle.syncState();
			}
		});

		fnDrawerLayout.setDrawerListener(fnDrawerToggle);
	}
	
	public void openCloseFragmentNavigation() {
		if(fnDrawerLayout!=null&&fnFragmentContainerView!=null) {
			if(!isDrawerOpen()) {
				fnDrawerLayout.openDrawer(fnFragmentContainerView);
			} else {
				fnDrawerLayout.closeDrawer(fnFragmentContainerView);
			}			
		}	
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			fnCallbacks = (NavigationDrawerCallbacks) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException("Activity must implement NavigationDrawerCallbacks.");
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		fnCallbacks = null;
	}
		
	public static interface NavigationDrawerCallbacks {
		/**
		 * Called when an item in the navigation drawer is selected.
		 */
		void onNavigationDrawerItemSelected(int position);
	}


	@Override
	public void onClick(View v) {
		switch(v.getId()) {
			case R.id.llFnNotes:
				fnDrawerLayout.closeDrawer(fnFragmentContainerView);
				if(!fnNameActivity.getClass().getSimpleName().equals(NotesListActivity.class.getSimpleName())) {
					startActivity(new Intent(fnNameActivity.getApplicationContext(), NotesListActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
					fnNameActivity.finish();
				}
			break;
			case R.id.llFnArchive:
				fnDrawerLayout.closeDrawer(fnFragmentContainerView);
				if(!fnNameActivity.getClass().getSimpleName().equals(ArchiveNotesListActivity.class.getSimpleName())) {
					startActivity(new Intent(fnNameActivity.getApplicationContext(), ArchiveNotesListActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
					fnNameActivity.finish();
				}
			break;
			case R.id.llFnRecycle:
				fnDrawerLayout.closeDrawer(fnFragmentContainerView);
				if(!fnNameActivity.getClass().getSimpleName().equals(RecycleNotesListActivity.class.getSimpleName())) {
					startActivity(new Intent(fnNameActivity.getApplicationContext(), RecycleNotesListActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
					fnNameActivity.finish();
				}
			break;
			case R.id.llFnSearch:
				fnDrawerLayout.closeDrawer(fnFragmentContainerView);
				if(!fnNameActivity.getClass().getSimpleName().equals(SearchActivity.class.getSimpleName())) {
					startActivity(new Intent(fnNameActivity.getApplicationContext(), SearchActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
					fnNameActivity.finish();
				}
			break;
			case R.id.llFnSettings:
				fnDrawerLayout.closeDrawer(fnFragmentContainerView);
				if(!fnNameActivity.getClass().getSimpleName().equals(SettingsActivity.class.getSimpleName())) {
					startActivity(new Intent(fnNameActivity.getApplicationContext(), SettingsActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
					fnNameActivity.finish();
				}
			break;
		}
		
	}
	

}