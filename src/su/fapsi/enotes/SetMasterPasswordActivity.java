package su.fapsi.enotes;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import su.fapsi.enotes.Utils.Password;

public class SetMasterPasswordActivity extends Activity {
	private EditText etCurrentMasterPassword;
	private EditText etNewMasterPassword;
	private EditText etConfirmNewMasterPassword;
	private Button btnSetMasterPassword;
	
	private String sourceCall;
	
	private boolean isSetPassword;
	
	private MenuItem itemPasswordClear;
	
	private final Context context = SetMasterPasswordActivity.this;
	
	private String currentPassword;
	private String newPassword;
	private String confirmedNewPassword;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.set_master_password_activity_layout);
		
		etCurrentMasterPassword = (EditText) findViewById(R.id.etCurrentMasterPassword);
		etNewMasterPassword = (EditText) findViewById(R.id.etNewMasterPassword);
		etConfirmNewMasterPassword = (EditText) findViewById(R.id.etConfirmNewMasterPassword);
		btnSetMasterPassword = (Button) findViewById(R.id.btnSetMasterPassword);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		
		sourceCall = getIntent().getStringExtra(Password.KEY_CALL_SOURCE);
		if(sourceCall.equals(Password.SETTINGS_CALL_SOURCE)) {
			if(Cryptography.isSetMasterPasswordEncryptNote(context)) {
				isSetPassword = true;
				etCurrentMasterPassword.setVisibility(View.VISIBLE);
				btnSetMasterPassword.setText(getResources().getString(R.string.btn_update_master_password));
			} else {
				isSetPassword = false;
			}
			
			
		}
		
		btnSetMasterPassword.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				currentPassword = etCurrentMasterPassword.getText().toString();
				newPassword = etNewMasterPassword.getText().toString();
				confirmedNewPassword = etConfirmNewMasterPassword.getText().toString();
				if(sourceCall.equals(Password.SETTINGS_CALL_SOURCE)) {
					 if(isSetPassword) {
						if(Cryptography.compareMasterPasswordWithInput(currentPassword.toString(), context)) {
							if(newPassword.toString().equals(confirmedNewPassword.toString())) {
								if(newPassword.length()<6) {
									 showPasswordConfirmedError(3);
								} else {
									Cryptography.reEnrypt(currentPassword.toString(), newPassword.toString(), context);
									Toast.makeText(context, getResources().getString(R.string.msg_update_master_password), Toast.LENGTH_SHORT).show();
									onBackPressed();
								}
							 } else {
								 showPasswordConfirmedError(1); 
							 }
						} else {
							showPasswordConfirmedError(2);
						}
					 } else {
						 if(newPassword.toString().equals(confirmedNewPassword.toString())) {
							 if(newPassword.length()<6) {
								 showPasswordConfirmedError(3);
							 } else {
								 Cryptography.setSpMasterPasswordEncryptNote(newPassword.toString(), context);
								 Toast.makeText(context, getResources().getString(R.string.msg_set_master_password), Toast.LENGTH_SHORT).show();
								 onBackPressed();
							 }							 
						 } else {
							 showPasswordConfirmedError(1); 
						 }
					 }	
				}
				
				if(sourceCall.equals(Password.NOTE_LIST_CALL_SOURCE)) {
					if(newPassword.toString().equals(confirmedNewPassword.toString())) {
						Cryptography.setSpMasterPasswordEncryptNote(newPassword.toString(), context);
						setResult(RESULT_OK);
						finish();
					} else {
						showPasswordConfirmedError(1);
					}
				}
			}	
		});	
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.set_password_menu, menu);
		itemPasswordClear = menu.findItem(R.id.ClearMasterPassword);
		if(!isSetPassword&&itemPasswordClear!=null) {
				itemPasswordClear.setVisible(false);
		}
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
		
			case R.id.ClearMasterPassword:
				showDialogMasterPasswordClear();
			return true;
		
			default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	private void showPasswordConfirmedError(int errorCode) {
		switch(errorCode) {
			case 1:
				Toast.makeText(context, getResources().getString(R.string.msg_unconfirmed_master_password), Toast.LENGTH_SHORT).show();
			break;
			case 2:
				Toast.makeText(context, getResources().getString(R.string.msg_wrong_current_master_password), Toast.LENGTH_SHORT).show();
			break;
			case 3:
				Toast.makeText(context, getResources().getString(R.string.msg_length_master_password), Toast.LENGTH_SHORT).show();
			break;
		}
		
		etCurrentMasterPassword.setText("");
		etNewMasterPassword.setText("");
		etConfirmNewMasterPassword.setText("");
	}
	
	@Override
	public void onBackPressed() {
		if(sourceCall.equals("NotesList")) {
			setResult(RESULT_CANCELED);
			finish();
		}	
		super.onBackPressed();
	}
	
	private void showDialogMasterPasswordClear() {
		AlertDialog.Builder builder = new AlertDialog.Builder(SetMasterPasswordActivity.this);
		final AlertDialog alertMasterPasswordClearDialog = builder.create();
		alertMasterPasswordClearDialog.setTitle(getResources().getString(R.string.title_reset_master_password));
		alertMasterPasswordClearDialog.setMessage(getResources().getString(R.string.attention_message_reset_master_password));
		alertMasterPasswordClearDialog.setButton(getResources().getString(R.string.btnOk), new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Cryptography.clearPassword(SetMasterPasswordActivity.this);
				Toast.makeText(context, getResources().getString(R.string.msg_reset_master_password), Toast.LENGTH_SHORT).show();
				alertMasterPasswordClearDialog.cancel();
				onBackPressed();
			}			
		});
		alertMasterPasswordClearDialog.setButton2(getResources().getString(R.string.btnCancel), new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				alertMasterPasswordClearDialog.cancel();
			}			
		});
		alertMasterPasswordClearDialog.show();
	}

}