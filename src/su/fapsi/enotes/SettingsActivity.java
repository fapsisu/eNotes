package su.fapsi.enotes;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import su.fapsi.enotes.Utils.Password;

public class SettingsActivity extends Activity implements NavigationDrawerFragment.NavigationDrawerCallbacks, 
		View.OnClickListener {
	private NavigationDrawerFragment navigationDrawerFragment;
	
	private Button btnCNDefault;
	private Spinner spSortMethodDefault;
	private CheckBox cbNotificationVibrate;
	private Button masterPasswordSettings;
	
	private TextView tvAppVersion;
	
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
	
	private int[] colors;
	private int colorIndex;
	
	private ArrayAdapter<?> adapterSetSortMethod;
	
	private final Context context = SettingsActivity.this;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings_activity_layout);
		
		btnCNDefault = (Button) findViewById (R.id.btnCNDefault);
		spSortMethodDefault = (Spinner) findViewById (R.id.spSortMethodDefault);
		cbNotificationVibrate = (CheckBox) findViewById (R.id.cbNotificationVibrate);
		masterPasswordSettings = (Button) findViewById (R.id.masterPasswordSettings);
		tvAppVersion = (TextView) findViewById(R.id.tvAppVersion);
		
	
		adapterSetSortMethod = ArrayAdapter.createFromResource(context, R.array.sortBaseNotesMethods, R.layout.spinner_item);
		adapterSetSortMethod.setDropDownViewResource(R.layout.spinner_item);
		spSortMethodDefault.setAdapter(adapterSetSortMethod);
		spSortMethodDefault.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				Utils.setDefaultIndexMethSort(position, SettingsActivity.this);
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				
			}			
		});
		
		tvAppVersion.setText(getResources().getString(R.string.label_app_version) + " " + getResources().getString(R.string.app_version));
		
		
		inflater = (LayoutInflater) SettingsActivity.this.getSystemService(LAYOUT_INFLATER_SERVICE);
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
		
		btnCNDefault.setOnClickListener(this);
		masterPasswordSettings.setOnClickListener(this);
		
		colors = Utils.getColors(context);
		
		btnCNDefault.setBackgroundColor(colors[Utils.getDefaultNoteColor(this)]);
		
		cbNotificationVibrate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				Utils.setNotificationVibrate(cbNotificationVibrate.isChecked(), SettingsActivity.this);				
			}			
		});
	}
	
	@Override
	public void onResume() {
		super.onResume();
		navigationDrawerFragment = (NavigationDrawerFragment) getFragmentManager()
				.findFragmentById(R.id.settings_navigation_drawer);
		
		navigationDrawerFragment.setUp(R.id.settings_navigation_drawer, (DrawerLayout) findViewById(R.id.settings_drawer_layout), SettingsActivity.this);
		
		cbNotificationVibrate.setChecked(Utils.isSetNotoficationVibrate(this));
		spSortMethodDefault.setSelection(Utils.getDefaultIndexMethSort(this));
	}
	
	@Override
	public void onBackPressed() {
		startActivity(new Intent(context, NotesListActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
		finish();
	}

	@Override
	public void onNavigationDrawerItemSelected(int position) {
	
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if (keyCode == KeyEvent.KEYCODE_MENU) {
	        event.startTracking();
	        navigationDrawerFragment.openCloseFragmentNavigation();
	        return true;
	    }
	    return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
			case R.id.btnCNDefault:
				ccPopupWindow = new PopupWindow(customView, android.view.ViewGroup.LayoutParams.WRAP_CONTENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT, true);
				ccPopupWindow.showAtLocation(llChoiceColor, Gravity.CENTER, 0, 0);
			break;			
			case R.id.color0:
				colorIndex = 0;
				setColor();
			break;
			case R.id.color1:
				colorIndex = 1;
				setColor();
			break;
			case R.id.color2:
				colorIndex = 2;
				setColor();
			break;
			case R.id.color3:
				colorIndex = 3;
				setColor();
			break;
			case R.id.color4:
				colorIndex = 4;
				setColor();
			break;
			case R.id.color5:
				colorIndex = 5;
				setColor();
			break;
			case R.id.color6:
				colorIndex = 6;
				setColor();
			break;
			case R.id.color7:
				colorIndex = 7;
				setColor();
			break;
			case R.id.color8:
				colorIndex = 8;
				setColor();
			break;
			case R.id.btnCloseCC:
				closeCCDialog();
			break;
			
			case R.id.masterPasswordSettings:
				startActivity(new Intent(context, SetMasterPasswordActivity.class).putExtra(Password.KEY_CALL_SOURCE, Password.SETTINGS_CALL_SOURCE));
			break;
		}
	}
	
	private void setColor() {
		btnCNDefault.setBackgroundColor(colors[colorIndex]);
		Utils.setDefaultNoteColor(colorIndex, context);
		closeCCDialog();
	}
	
	private void closeCCDialog() {
		if(ccPopupWindow!=null) {
			ccPopupWindow.dismiss();
			ccPopupWindow = null;
		}
	}
	
	@Override
	public void onPause() {
		closeCCDialog();
		super.onPause();
	}

}