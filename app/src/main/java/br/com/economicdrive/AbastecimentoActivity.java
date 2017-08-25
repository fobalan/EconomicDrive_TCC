package br.com.economicdrive;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mikepenz.materialdrawer.holder.StringHolder;

import br.com.economicdrive.fragment.DatePickerFragment;
import br.com.economicdrive.holder.Combustivel;
import br.com.economicdrive.model.Carro;
import br.com.economicdrive.model.Local;

@SuppressLint("SimpleDateFormat")
public class AbastecimentoActivity extends AppCompatActivity implements OnClickListener,
		TextWatcher, OnItemSelectedListener {
	private Toolbar toolbar;
	private TextView dataAbastecimentoTextView;
	private TextView quantidadeLitrosTextView;
	private TextView localTextView;
	private ImageButton salvarAbastecimentoButton;
	private Spinner combustivelSpinner;
	private EditText valorLitroEditText;
	private EditText valorGastoEditText;
	private EditText hodometroEditText;
	private CheckBox tanqueCheioCheckBox;
	private Abastecimento abastecimento;
	private Local local;
	private float valorGasto;
	private float valorLitro;
	private NumberFormat dinheiroGastoFormat;
	private NumberFormat dinheiroLitroFormat;
	private ArrayAdapter <Combustivel> adapterCombustivel;
	private Combustivel escolhaCombustivel;
	private Combustivel [] itens;
	private Carro veiculoEscolhido;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_abastecimento);
		dataAbastecimentoTextView = (TextView) findViewById(R.id.dataAbastecimentoTextView);
		quantidadeLitrosTextView = (TextView) findViewById(R.id.quantidadeLitrosTextView);
		localTextView = (TextView) findViewById(R.id.localTextView);
		salvarAbastecimentoButton = (ImageButton) findViewById(R.id.salvarAbastecimentoButton);
		combustivelSpinner = (Spinner) findViewById(R.id.combustivelSpinner);
		valorLitroEditText = (EditText) findViewById(R.id.valorLitroEditText);
		valorGastoEditText = (EditText) findViewById(R.id.valorGastoEditText);
		hodometroEditText = (EditText) findViewById(R.id.hodometroEditText);
		tanqueCheioCheckBox = (CheckBox) findViewById(R.id.tanqueCheioCheckBox);
		dataAbastecimentoTextView.setOnClickListener(this);
		salvarAbastecimentoButton.setOnClickListener(this);
		localTextView.setOnClickListener(this);
		combustivelSpinner.setOnItemSelectedListener(this);
        toolbar = (Toolbar) findViewById(R.id.myAbastActivityToolbar);
        applyCustomizingActionBar();
        onCreateCustomizingFormats();
        onCarSelected();
        onFuelSelection();
		switch(getIntent().getIntExtra("acao", 0)){
		case Constantes.EDITAR:
			onStartVariables();
			break;
		case Constantes.INSERIR :
			abastecimento = new Abastecimento(this);
			abastecimento.setIdCarro(veiculoEscolhido.getCodigo());
			dataAbastecimentoTextView.setText(getDateTime());
			break;
		case Constantes.VISUALIZAR:
			onStartVariables();
			onDisableInputVariables();
			break;
		}
		onSetTextListeners();
	}

	private void onFuelSelection() {
		List <Combustivel> combustivel = null;

		switch (veiculoEscolhido.getComb()){
		case Constantes.GASOLINA:
			 combustivel = Combustivel.consultarComb(this, "SELECT *"
												         + " FROM tb_Combustivel"
												         + " WHERE idCOMBUSTIVEL = 4"
												         + " OR idCOMBUSTIVEL = 5;");
			break;
		case Constantes.ALCOOL:
			combustivel = Combustivel.consultarComb(this, "SELECT *"
														+ " FROM tb_Combustivel"
														+ " WHERE idCOMBUSTIVEL = 1"
														+ " OR idCOMBUSTIVEL = 2;");
			break;
		case Constantes.FLEX:
			combustivel = Combustivel.consultarComb(this, "SELECT *"
														+ " FROM tb_Combustivel"
														+ " WHERE idCOMBUSTIVEL = 1"
												        + " OR idCOMBUSTIVEL = 2"
												        + " OR idCOMBUSTIVEL = 4"
												        + " OR idCOMBUSTIVEL = 5;");
		
			break;
		case Constantes.DIESEL:
			combustivel = Combustivel.consultarComb(this, "SELECT *"
														+ " FROM tb_Combustivel"
														+ " WHERE idCOMBUSTIVEL = 3;");
			break;
		}
		itens = combustivel.toArray(new Combustivel[0]);
		adapterCombustivel =
				new ArrayAdapter<>(this, R.layout.spinner_dropdown_list, itens);
		combustivelSpinner.setAdapter(adapterCombustivel);
	}
	
	private void onCarSelected() {
		veiculoEscolhido = getIntent().getParcelableExtra("veiculo");
	}

	public void showDatePickerDialog(View v) {
		DialogFragment newFragment = new DatePickerFragment(0);
		newFragment.show(getFragmentManager(), "datePicker");
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.dataAbastecimentoTextView:
			showDatePickerDialog(v);
			break;
		case R.id.localTextView:
			Intent i = new Intent(this, LocalListActivity.class);
			i.putExtra("activity", "Abastecimento");
			startActivityForResult(i, Constantes.ABASTECIMENTO_CODE);
			break;
		case R.id.salvarAbastecimentoButton:
			if (Integer.parseInt(hodometroEditText.getEditableText().toString()) <= Abastecimento.getMaxHodometro(this, veiculoEscolhido)){
				Toast.makeText(getApplicationContext(), "Valor do hodometro menor que o ultimo inserido", Toast.LENGTH_LONG).show();
			}
			else if (valorLitroEditText.getEditableText().toString().equals("") || valorLitroEditText.getEditableText().toString().equals("R$0,000") ){
				Toast.makeText(getApplicationContext(), "Obrigatório preencher campo Valor Litro", Toast.LENGTH_SHORT).show();
			}
			else if (valorGastoEditText.getEditableText().toString().equals("") || valorGastoEditText.getEditableText().toString().equals("R$0,00")){
				Toast.makeText(getApplicationContext(), "Obrigatório preencher campo Valor Gasto", Toast.LENGTH_SHORT).show();
			}
			else if(hodometroEditText.getEditableText().toString().equals("") || hodometroEditText.getEditableText().toString().equals("0") ) {
				Toast.makeText(getApplicationContext(), "Obrigatório preencher campo Hodometro", Toast.LENGTH_SHORT).show();
			}
			else if (local == null){
				Toast.makeText(getApplicationContext(), "Obrigatório preencher campo Local", Toast.LENGTH_SHORT).show();
			}
			else{
				abastecimento.setCombustivel(escolhaCombustivel.getCodigo());
				abastecimento.setDataGasto(dataAbastecimentoTextView.getText().toString());
				abastecimento.setKilometros(Integer.parseInt(hodometroEditText.getEditableText().toString()));
				abastecimento.setValorLitro(Float.parseFloat(valorLitroEditText.getEditableText().toString().replace("R$","").replace(",",".")));
				abastecimento.setValorGasto(Float.parseFloat(valorGastoEditText.getEditableText().toString().replace("R$","").replace(",",".")));
				abastecimento.setLocalGasto(local.getCodigo());
				abastecimento.setKmdif(0);
				if (tanqueCheioCheckBox.isChecked()) {
					abastecimento.setTanqueCheio("sim");
				} else {
					abastecimento.setTanqueCheio("nao");
				}
				if (getIntent().getIntExtra("acao", 0) == Constantes.EDITAR) {
					abastecimento.alteraAbastecimento(this);
                    abastecimento.AtualizakmRodado(this, abastecimento.getIdCarro());
				} else {
					abastecimento.insereAbastecimento(this);
					MainActivity.getNavigationDrawerLeft().updateBadge(2,
							new StringHolder(Integer.toString(Abastecimento.ContaAbastecimentos(this,veiculoEscolhido.getCodigo()))));
				}
                abastecimento.AtualizakmRodado(this, abastecimento.getIdCarro());
				finish();
			}
			break;
		}

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
		EditText selecionadoEditText = null;
		try {
			float valorAnterior = Float.parseFloat(s.toString()
					.replace("R$", "").replace(".", "").replaceAll(",", "")
					.replaceFirst(" ", ""));
			if (valorGastoEditText.isFocused()) {
				selecionadoEditText = valorGastoEditText;
				valorGasto = valorAnterior / 100;
				valorMask = dinheiroGastoFormat.format(valorAnterior / 100);
				valorGastoEditText.setText(valorMask);
			} else if (valorLitroEditText.isFocused()) {
				selecionadoEditText = valorLitroEditText;
				valorLitro = valorAnterior / 1000;
				valorMask = dinheiroLitroFormat.format(valorAnterior / 1000);
				valorLitroEditText.setText(valorMask);
			}
			onCalculateQuantityLitros(valorLitro, valorGasto);
			selecionadoEditText.setSelection(valorMask.length());
			onSetTextListeners();
		} catch (NumberFormatException e) {
		} catch (ArithmeticException e) {
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == Constantes.ABASTECIMENTO_CODE)
			if (resultCode == Constantes.LOCAL_LIST) {
				local = data.getParcelableExtra("parcel");
				localTextView.setText(local.getNome());
			}

	}

	private void onCalculateQuantityLitros(float valorLitro, float valorGasto) {
		float quantidadeLitros;
		if (valorLitro == 0 || valorGasto == 0) {
			quantidadeLitros = 0;
		} else {
			quantidadeLitros = valorGasto / valorLitro;
		}
		quantidadeLitrosTextView.setText(new DecimalFormat("##,###.##L")
				.format(quantidadeLitros));
	}
	
	private void applyCustomizingActionBar() {
        setSupportActionBar(toolbar);
		ActionBar actionBar = getSupportActionBar();
		actionBar.setHomeButtonEnabled(true);
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setBackgroundDrawable(new ColorDrawable(Color
				.rgb(3, 103, 221)));
	}
	private void onSetTextListeners() {
		valorLitroEditText.addTextChangedListener(this);
		valorGastoEditText.addTextChangedListener(this);
	}

	private void onRemoveTextListeners() {
		valorLitroEditText.removeTextChangedListener(this);
		valorGastoEditText.removeTextChangedListener(this);
	}
	private void onCreateCustomizingFormats() {
		dinheiroGastoFormat = NumberFormat.getCurrencyInstance();
		dinheiroLitroFormat = NumberFormat.getCurrencyInstance();
		dinheiroLitroFormat.setMinimumFractionDigits(3);
		dinheiroLitroFormat.setMaximumFractionDigits(3);
	}
	private void onDisableInputVariables() {
		combustivelSpinner.setEnabled(false);
		tanqueCheioCheckBox.setEnabled(false);
		valorLitroEditText.setEnabled(false);
		valorGastoEditText.setEnabled(false);
		hodometroEditText.setEnabled(false);
		localTextView.setEnabled(false);
		dataAbastecimentoTextView.setEnabled(false);
		quantidadeLitrosTextView.setEnabled(false);
		salvarAbastecimentoButton.setVisibility(View.INVISIBLE);
		
	}

	private void onStartVariables() {
		abastecimento = getIntent().getParcelableExtra("parcel");
		onSetCombustivel();
		if (abastecimento.getTanqueCheio().equals("sim"))
			tanqueCheioCheckBox.setChecked(true);
		valorLitroEditText.setText(dinheiroLitroFormat.format(abastecimento.getValorLitro()));
		valorLitroEditText.setSelection(valorLitroEditText.getEditableText().length());
		valorGastoEditText.setText(dinheiroGastoFormat.format(abastecimento.getValorGasto()));
		hodometroEditText.setText(Integer.toString(abastecimento.getKilometros()));
		local = Abastecimento.ConsultaLocal(this, "SELECT *"
				+ " FROM tb_local"
				+ " WHERE codigoLOCAL = " + abastecimento.getLocalGasto());
		localTextView.setText(local.getNome());
		dataAbastecimentoTextView.setText(abastecimento.tratadata());	
		onCalculateQuantityLitros(abastecimento.getValorLitro(), abastecimento.getValorGasto());
	}

	private void onSetCombustivel() {
		for (int i = 0 ; i < itens.length; i++){
			if ( itens[i].getCodigo() == abastecimento.getCombustivel() ){
				combustivelSpinner.setSelection(i);
				break;
			}
		}
		
	}

	private String getDateTime() {
		Date date = new Date();
		return new SimpleDateFormat("dd/MM/yyyy").format(date);
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		escolhaCombustivel = adapterCombustivel.getItem(position);
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		
	}
}
