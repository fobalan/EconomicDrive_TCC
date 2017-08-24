package br.com.economicdrive;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.DialogFragment;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.mikepenz.materialdrawer.holder.StringHolder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;

@SuppressLint("SimpleDateFormat")
public class DespesasActivity extends AppCompatActivity implements OnClickListener,
		TextWatcher {
	private EditText descricaoDespesaEditText;
	private EditText valorDespesaEditText;
	private TextView localDespesaTextView;
	private TextView dataDespesaTextView;
	private ImageButton salvarDespesaButton;
	private Despesas despesa;
	private Intent i;
	private ActionBar actionBar;
	private Toolbar toolbar;
	private Local local;
	private Carro veiculoEscolhido;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_despesas);
		descricaoDespesaEditText = (EditText) this.findViewById(R.id.descricaoDespesaEditText);
		valorDespesaEditText = (EditText) this.findViewById(R.id.valorDepesaEditText);
		localDespesaTextView = (TextView) this.findViewById(R.id.localDespesaTextView);
		dataDespesaTextView = (TextView) this.findViewById(R.id.dataDespesaTextView);
		salvarDespesaButton = (ImageButton) this.findViewById(R.id.salvarDespesasButton);
		toolbar = (Toolbar) findViewById(R.id.myDespActivityToolbar);
		dataDespesaTextView.setOnClickListener(this);
		localDespesaTextView.setOnClickListener(this);
		salvarDespesaButton.setOnClickListener(this);
        applyCustomizingActionBar();
		switch(getIntent().getIntExtra("acao", 0)){
		case Constantes.EDITAR:
			onStartVariables();
			break;
		case Constantes.INSERIR :
			despesa = new Despesas(this);
			veiculoEscolhido = getIntent().getParcelableExtra("veiculo");
			despesa.setIdCarro(veiculoEscolhido.getCodigo());
			dataDespesaTextView.setText(getDateTime());
			break;
		case Constantes.VISUALIZAR:
			onStartVariables();
			onDisableInputVariables();
			break;
		}
		onSetTextListeners();
		onSetTextListeners();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.dataDespesaTextView:
			showDatePickerDialog(v);
			break;
		case R.id.localDespesaTextView:
			i = new Intent(this, LocalListActivity.class);
			i.putExtra("activity", "Despesas");
			startActivityForResult(i, Constantes.DESPESAS_CODE);
			break;
		case R.id.salvarDespesasButton:
			if (descricaoDespesaEditText.getEditableText().toString().equals("")){
				Toast.makeText(getApplicationContext(), "Obrigatório preencher campo Descrição", Toast.LENGTH_SHORT).show();
			}
			else if(valorDespesaEditText.getEditableText().toString().equals("") || valorDespesaEditText.getEditableText().toString().equals("R$0,00")) {
				Toast.makeText(getApplicationContext(), "Obrigatório preencher campo Valor", Toast.LENGTH_SHORT).show();
			}

			else if (local == null){
				Toast.makeText(getApplicationContext(), "Obrigatório preencher campo Local", Toast.LENGTH_SHORT).show();
			}
			else{
				despesa.setDescricaoDespesa(descricaoDespesaEditText.getEditableText().toString());
				despesa.setValorGasto(Float.parseFloat(valorDespesaEditText.getEditableText().toString().replace("R$","").replace(".", "").replace(",", ".")));
				despesa.setLocalGasto(local.getCodigo());
				despesa.setDataGasto(dataDespesaTextView.getText().toString());
				if (getIntent().getIntExtra("acao", 0) == Constantes.EDITAR){
					despesa.alteraDespesas(this);
				}
				else{
					despesa.insereDespesas(this);
					MainActivity.getNavigationDrawerLeft().updateBadge(1,
							new StringHolder(Integer.toString(Despesas.ContaDespesas(this, veiculoEscolhido.getCodigo()))));
				}
				finish();
			}
			break;
		}
		
	}
	public void showDatePickerDialog(View v) {
		DialogFragment newFragment = new DatePickerFragment(0);
		newFragment.show(getFragmentManager(), "datePicker");
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == Constantes.DESPESAS_CODE)
			if (resultCode == Constantes.LOCAL_LIST) {
				local = data.getParcelableExtra("parcel");
				localDespesaTextView.setText(local.getNome());
			}

	}
	
	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {

	}

	@Override
	public void afterTextChanged(Editable s) {
		onRemoveTextListeners();
		String valorMask = null;
		try {
			float valorAnterior = Float.parseFloat(s.toString()
					.replace("R$", "").replace(".", "").replaceAll(",", "")
					.replaceFirst(" ", ""));

		    valorMask = NumberFormat.getCurrencyInstance().format(valorAnterior / 100);
			valorDespesaEditText.setText(valorMask);
			valorDespesaEditText.setSelection(valorMask.length());
			onSetTextListeners();
		} catch (NumberFormatException e) {
		} catch (ArithmeticException e) {
		}
	}
	private void onRemoveTextListeners() {
		valorDespesaEditText.removeTextChangedListener(this);	
	}
	
	private void onSetTextListeners() {
		valorDespesaEditText.addTextChangedListener(this);
	}
	private void applyCustomizingActionBar() {
		setSupportActionBar(toolbar);
		actionBar = getSupportActionBar();
		actionBar.setHomeButtonEnabled(true);
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setBackgroundDrawable(new ColorDrawable(Color
				.rgb(3, 103, 221)));
	}
	private String getDateTime() {
		Date date = new Date();
		return new SimpleDateFormat("dd/MM/yyyy").format(date);
	}
	private void onStartVariables() {
		despesa = getIntent().getParcelableExtra("parcel");
		descricaoDespesaEditText.setText(despesa.getDescricaoDespesa());
		valorDespesaEditText.setText(NumberFormat.getCurrencyInstance().format(despesa.getValorGasto()));
		local = Despesas.ConsultaLocal(this, "SELECT *"
				+ " FROM tb_local"
				+ " WHERE codigoLOCAL = " + despesa.getLocalGasto());
		localDespesaTextView.setText(local.getNome());
		dataDespesaTextView.setText(despesa.tratadata());	
	}
	
	private void onDisableInputVariables() {
		descricaoDespesaEditText.setEnabled(false);
		valorDespesaEditText.setEnabled(false);
		localDespesaTextView.setEnabled(false);
		dataDespesaTextView.setEnabled(false);
		salvarDespesaButton.setVisibility(View.INVISIBLE);
	}

}
