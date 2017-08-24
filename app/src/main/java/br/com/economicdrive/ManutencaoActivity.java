package br.com.economicdrive;

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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mikepenz.materialdrawer.holder.StringHolder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;

@SuppressLint("SimpleDateFormat")
public class ManutencaoActivity extends AppCompatActivity implements OnClickListener,
		TextWatcher, OnItemSelectedListener {
	private EditText descricaoManutencaoEditText;
	private EditText valorManutencaoEditText;
	private TextView localManutencaoTextView;
	private TextView dataManutencaoTextView;
	private ImageButton salvarManutencaoButton;
	private Spinner tipoManutencaoSpinner;
	private ArrayAdapter<TipoManutencao> adapterTipoManutencao;
	private TipoManutencao tipoManutencao;
	private TipoManutencao [] itens;
	private Manutencao manutencao;
	private Toolbar toolbar;
	private Local local;
	private int posicaoTipoManutencao;
	private Carro veiculoEscolhido;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_manutencao);
		descricaoManutencaoEditText = (EditText) this.findViewById(R.id.descricaoManutencaoEditText);
		valorManutencaoEditText = (EditText) this.findViewById(R.id.valorManutencaoEditText);
		localManutencaoTextView = (TextView) this.findViewById(R.id.localManutencaoTextView);
		dataManutencaoTextView = (TextView) this.findViewById(R.id.dataManutencaoTextView);
		salvarManutencaoButton = (ImageButton) this.findViewById(R.id.salvarManutencaoButton);
		tipoManutencaoSpinner = (Spinner) this.findViewById(R.id.tipoManutencaoSpinner);
		toolbar = (Toolbar) findViewById(R.id.myManuActivityToolbar);
        applyCustomizingActionBar();
		onInflateSpinner();
		dataManutencaoTextView.setOnClickListener(this);
		localManutencaoTextView.setOnClickListener(this);
		salvarManutencaoButton.setOnClickListener(this);
		tipoManutencaoSpinner.setOnItemSelectedListener(this);
		switch(getIntent().getIntExtra("acao", 0)){
		case Constantes.EDITAR:
			onStartVariables();
			break;
		case Constantes.INSERIR :
			manutencao = new Manutencao(this);
			veiculoEscolhido = getIntent().getParcelableExtra("veiculo");
			manutencao.setIdCarro(veiculoEscolhido.getCodigo());
			dataManutencaoTextView.setText(getDateTime());
			break;
		case Constantes.VISUALIZAR:
			onStartVariables();
			onDisableInputVariables();
			break;
		}
		onSetTextListeners();
	}



	private void onInflateSpinner() {
		List <TipoManutencao> tipoManutencao = TipoManutencao.consultarTipoManutencao(this);
		itens = tipoManutencao.toArray(new TipoManutencao[0]);
		adapterTipoManutencao = 
				new ArrayAdapter <> (this, R.layout.spinner_dropdown_list, itens);
		tipoManutencaoSpinner.setAdapter(adapterTipoManutencao);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.dataManutencaoTextView:
			showDatePickerDialog(v);
			break;
		case R.id.localManutencaoTextView:
			Intent i = new Intent(this, LocalListActivity.class);
			i.putExtra("activity", "Manutencao");
			startActivityForResult(i, Constantes.MANUTENCAO_CODE);
			break;
		case R.id.salvarManutencaoButton:
			if (descricaoManutencaoEditText.getEditableText().toString().equals("")){
				Toast.makeText(getApplicationContext(), "Obrigatório preencher campo Descrição", Toast.LENGTH_SHORT).show();
			}
			else if(valorManutencaoEditText.getEditableText().toString().equals("") || valorManutencaoEditText.getEditableText().toString().equals("R$0,00")) {
				Toast.makeText(getApplicationContext(), "Obrigatório preencher campo Valor", Toast.LENGTH_SHORT).show();
			}
			else if (local == null){
				Toast.makeText(getApplicationContext(), "Obrigatório preencher campo Local", Toast.LENGTH_SHORT).show();
			}
			else{
					manutencao.setDescricaoManutencao(descricaoManutencaoEditText.getEditableText().toString());
					String valorManutencao = valorManutencaoEditText.getEditableText().toString();
					manutencao.setValorGasto(Float.parseFloat(valorManutencao.replace("R$","").replace(".", "").replace(",", ".")));
					manutencao.setLocalGasto(local.getCodigo());
					manutencao.setTipoManutencao(tipoManutencao.getCodigo());
					manutencao.setDataGasto(dataManutencaoTextView.getText().toString());
					if (getIntent().getIntExtra("acao", 0) == Constantes.EDITAR){
						manutencao.alteraManutencao(this);
					}
					else{
						manutencao.insereManutencao(this);
						MainActivity.getNavigationDrawerLeft().updateBadge(3,
								new StringHolder(Integer.toString(Manutencao.ContaManutencao(this, veiculoEscolhido.getCodigo()))));
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
		if (requestCode == Constantes.MANUTENCAO_CODE)
			if (resultCode == Constantes.LOCAL_LIST) {
				local = data.getParcelableExtra("parcel");
				localManutencaoTextView.setText(local.getNome());
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
		String valorMask;
		try {
			float valorAnterior = Float.parseFloat(s.toString()
					.replace("R$", "").replace(".", "").replaceAll(",", "")
					.replaceFirst(" ", ""));

		    valorMask = NumberFormat.getCurrencyInstance().format(valorAnterior / 100);
			valorManutencaoEditText.setText(valorMask);
			valorManutencaoEditText.setSelection(valorMask.length());
			onSetTextListeners();
		} catch (NumberFormatException e) {
		} catch (ArithmeticException e) {
		}
	}
	private void onRemoveTextListeners() {
		valorManutencaoEditText.removeTextChangedListener(this);	
	}
	
	private void onSetTextListeners() {
		valorManutencaoEditText.addTextChangedListener(this);
	}
	private void applyCustomizingActionBar() {
		setSupportActionBar(toolbar);
		ActionBar actionBar = getSupportActionBar();
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
		manutencao = getIntent().getParcelableExtra("parcel");
		onSetTipoManutencao();
		descricaoManutencaoEditText.setText(manutencao.getDescricaoManutencao());
		valorManutencaoEditText.setText(NumberFormat.getCurrencyInstance().format(manutencao.getValorGasto()));
		local = Manutencao.ConsultaLocal(this, "SELECT *"
				 + " FROM tb_local"
				 + " WHERE codigoLOCAL = " + manutencao.getLocalGasto());
		localManutencaoTextView.setText(local.getNome());
		dataManutencaoTextView.setText(manutencao.tratadata());
		tipoManutencaoSpinner.setSelection(posicaoTipoManutencao);
	}
	
	private void onSetTipoManutencao() {
		for(int i = 0; i< itens.length; i++){
			if (itens[i].getCodigo() == manutencao.getTipoManutencao())
				posicaoTipoManutencao = i;
		}
		
	}

	private void onDisableInputVariables() {
		descricaoManutencaoEditText.setEnabled(false);
		valorManutencaoEditText.setEnabled(false);
		localManutencaoTextView.setEnabled(false);
		dataManutencaoTextView.setEnabled(false);
		tipoManutencaoSpinner.setEnabled(false);
		salvarManutencaoButton.setVisibility(View.INVISIBLE);
		
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		tipoManutencao = adapterTipoManutencao.getItem(position);
		
	}
	@Override
	public void onNothingSelected(AdapterView<?> parent) {}
}
