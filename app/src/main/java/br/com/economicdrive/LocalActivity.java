package br.com.economicdrive;

import java.util.List;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class LocalActivity extends AppCompatActivity implements Button.OnClickListener{

	private EditText localEditText;
	private EditText endeEditText;
	private ImageButton salvarLocalButton;
	private int intCodigo;
	private Toolbar toolbar;
	private Local local;
	int intAcao = 1;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_local);
		localEditText = (EditText) findViewById(R.id.LocalEditText);
		endeEditText = (EditText) findViewById(R.id.EndeeditText);
		salvarLocalButton = (ImageButton) findViewById(R.id.salvarLocalButton);
        toolbar  = (Toolbar) findViewById(R.id.myLocalActivityToolbar);
		salvarLocalButton.setOnClickListener(this);
		onActionBar();
		intAcao = getIntent().getIntExtra("acao",0);
		local = getIntent().getParcelableExtra("parcel");
		if (intAcao == Constantes.EDITAR){
			localEditText.setText(local.getNome());
			endeEditText.setText(local.getEndereco());
			intCodigo = local.getCodigo();
		}
		else if (intAcao == Constantes.VISUALIZAR){
			localEditText.setText(local.getNome());
			endeEditText.setText(local.getEndereco());
			intCodigo = local.getCodigo();
			OnDisableVariables();
		}
	}

	private void onActionBar() {
        setSupportActionBar(toolbar);
		ActionBar actionBar = getSupportActionBar();
		actionBar.setHomeButtonEnabled(true);
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setBackgroundDrawable(new ColorDrawable(Color.rgb(3, 103, 221)));
	}

	private void OnDisableVariables() {
		localEditText.setEnabled(false);
		endeEditText.setEnabled(false);
		salvarLocalButton.setVisibility(View.INVISIBLE);
		
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		finish();
		return super.onOptionsItemSelected(item);
	}
	@Override
	public void onClick(View v) {
		try{
			//Variavel que receber qual acao esta sendo executada, edicao ou inclusao
			String strLocal = localEditText.getEditableText().toString();
			String strEnde = endeEditText.getEditableText().toString();
			if (strLocal.equals("")) {
				Toast.makeText(getApplicationContext(), "Nome não pode estar em branco",Toast.LENGTH_SHORT).show();
			}
			else{
				if (strEnde.equals("")){
					Toast.makeText(getApplicationContext(), "Endereço não pode estar em branco",Toast.LENGTH_SHORT).show();
				}
				else{
					if (intAcao == Constantes.INSERIR){
						local = new Local(strEnde, strLocal);
						List<Information> local2 = Local.ConsultaLocais(this, "SELECT * FROM tb_local WHERE enderecoLOCAL = '" + local.getEndereco() + "'");
						Local[] itens = local2.toArray(new Local[0]);
						if (itens.length > 0){
							Toast.makeText(getApplicationContext(), "Já existe um local cadastrado com esse endereço",Toast.LENGTH_SHORT).show();
						}else{
							local.insereLocal(this); 
							Toast.makeText(getApplicationContext(), "Local cadastrado com sucesso",Toast.LENGTH_SHORT).show();
							localEditText.setText("");
							endeEditText.setText("");
							finish();
						}
					}
					else if (intAcao == Constantes.EDITAR){
						local = new Local(intCodigo, strEnde, strLocal);
						List<Information> local2 = Local.ConsultaLocais(this, "SELECT * FROM tb_local WHERE enderecoLOCAL = '" + local.getEndereco() + "'");
						Local[] itens = local2.toArray(new Local[0]);
						if (itens.length > 0 && itens[0].getCodigo() != local.getCodigo()){
							Toast.makeText(getApplicationContext(), "Já existe um local cadastrado com esse endereço",Toast.LENGTH_SHORT).show();
						}else{
							local.alteraLocal(this);
							Toast.makeText(getApplicationContext(), "Local alterado com sucesso",Toast.LENGTH_SHORT).show();
							localEditText.setText("");
							endeEditText.setText("");
							finish();
						}
					}
					else{
						Toast.makeText(getApplicationContext(), "Operação inválida",Toast.LENGTH_SHORT).show();
					}
				}
			}
		}
		catch(Error e){
			Toast.makeText(getApplicationContext(), "Erro ao gerenciar local",Toast.LENGTH_SHORT).show();
		}
	}

}
