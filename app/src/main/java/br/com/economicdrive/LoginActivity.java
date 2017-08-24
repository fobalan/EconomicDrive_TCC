package br.com.economicdrive;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

public class LoginActivity extends AppCompatActivity implements Communicator {
	
	private EditText loginEditText;
	private DialogFragmentMessage fragmentDialog;
	private String senha;
	private Intent i;
	private ActionBar actionBar;
	private Toolbar toolbar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		loginEditText = (EditText) findViewById(R.id.loginEditText);
		toolbar = (Toolbar) findViewById(R.id.myLoginToolbar);
		onActionBar();
		Intent origemIntent = this.getIntent();
		Bundle b = origemIntent.getExtras();
		senha = b.getString("senha");
		i = new Intent(this, MainActivity.class);
		loginEditText.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				if (loginEditText.length() == 4){
					if (loginEditText.getEditableText().toString().equals(senha)){
						startActivity(i);	
						finish();
					}
					else{
						loginEditText.setText("");
						Toast.makeText(getApplicationContext(), "Login inv�lido", Toast.LENGTH_SHORT).show();
					}
						
				}
			}
		});
	}

	private void onActionBar() {
		setSupportActionBar(toolbar);
		actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(false);
		actionBar.setHomeButtonEnabled(false);
		actionBar.setBackgroundDrawable(new ColorDrawable(Color.rgb(3, 103, 221)));
	}

	public Context getContext(){
		return this;
	}
	
	@Override
	public void onBackPressed() {
		fragmentDialog = new DialogFragmentMessage("Sair", "Deseja sair?");
		fragmentDialog.show(getFragmentManager(), "Sair");
	}

	@Override
	public void onDialogMessage(String data) {
		if (data.equals("Sim"))
			finish();
		else if (data.equals("Não"))
			fragmentDialog.dismiss();
	}

	@Override
	public void OnDeleteMessage(List<Information> deleteList, MyAdapter myAdapter, String information) {

	}
}
