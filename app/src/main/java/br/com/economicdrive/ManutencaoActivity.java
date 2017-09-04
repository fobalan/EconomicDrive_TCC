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
import android.view.Menu;
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
import com.rengwuxian.materialedittext.MaterialEditText;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import br.com.economicdrive.constantes.Constantes;
import br.com.economicdrive.fragment.DatePickerFragment;
import br.com.economicdrive.model.Carro;
import br.com.economicdrive.model.Local;
import br.com.economicdrive.model.Manutencao;
import br.com.economicdrive.model.TipoManutencao;

@SuppressLint("SimpleDateFormat")
public class ManutencaoActivity extends AppCompatActivity implements OnClickListener,
		TextWatcher {
	private MaterialEditText descricaoMaterialEditText;
	private MaterialEditText valorMaterialEditText;
	private MaterialEditText localMaterialEditText;
	private MaterialEditText dataMaterialEditText;
	private MaterialBetterSpinner tipoBetterSpinner;
	private ArrayAdapter<TipoManutencao> adapterTipoManutencao;
	private TipoManutencao tipoEscolhido;
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
		descricaoMaterialEditText = (MaterialEditText) this.findViewById(R.id.descricaoManutencaoMaterialEditText);
		valorMaterialEditText = (MaterialEditText) this.findViewById(R.id.valorManutencaoMaterialEditText);
		localMaterialEditText= (MaterialEditText) this.findViewById(R.id.localManutencaoMaterialEditText);
		dataMaterialEditText = (MaterialEditText) this.findViewById(R.id.dataManutencaoMaterialEditText);
		tipoBetterSpinner = (MaterialBetterSpinner) this.findViewById(R.id.tipoManutencaoBetterSpinner);
		toolbar = (Toolbar) findViewById(R.id.manutencaoToolbar);
        applyCustomizingActionBar();
		onInflateSpinner();
		dataMaterialEditText.setOnClickListener(this);
		localMaterialEditText.setOnClickListener(this);
		switch(getIntent().getIntExtra("acao", 0)){
		case Constantes.EDITAR:
			onStartVariables();
			break;
		case Constantes.INSERIR :
			manutencao = new Manutencao(this);
			veiculoEscolhido = getIntent().getParcelableExtra("veiculo");
			manutencao.setIdCarro(veiculoEscolhido.getCodigo());
			dataMaterialEditText.setText(getDateTime());
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
		tipoBetterSpinner.setAdapter(adapterTipoManutencao);
		tipoBetterSpinner.setText(itens[0].getNome());
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.dataManutencaoMaterialEditText:
			showDatePickerDialog(v);
			break;
		case R.id.localManutencaoMaterialEditText:
			Intent i = new Intent(this, LocalListActivity.class);
			i.putExtra("activity", "Manutencao");
			startActivityForResult(i, Constantes.MANUTENCAO_CODE);
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
			case R.id.save_menu:
				onClickSave();
				return true;
		}


		return super.onOptionsItemSelected(item);
	}

	private void onClickSave() {
		if(checkFields()) {
			onTipoSelected();
			manutencao.setDescricaoManutencao(descricaoMaterialEditText.getEditableText().toString());
			String valorManutencao = valorMaterialEditText.getEditableText().toString();
			manutencao.setValorGasto(Float.parseFloat(valorManutencao.replace("R$", "").replace(".", "").replace(",", ".")));
			manutencao.setLocalGasto(local.getCodigo());
			manutencao.setTipoManutencao(tipoEscolhido.getCodigo());
			manutencao.setDataGasto(dataMaterialEditText.getText().toString());
			if (getIntent().getIntExtra("acao", 0) == Constantes.EDITAR) {
				manutencao.alteraManutencao(this);
			} else if (getIntent().getIntExtra("acao", 0) != Constantes.VISUALIZAR) {
				manutencao.insereManutencao(this);
				MainActivity.getNavigationDrawerLeft().updateBadge(3,
						new StringHolder(Integer.toString(Manutencao.ContaManutencao(this, veiculoEscolhido.getCodigo()))));
			}
			finish();
		}
	}

	private void onTipoSelected() {
		for (TipoManutencao tipoManutencao : itens) {
			if(tipoManutencao.getNome().equals(tipoBetterSpinner.getText().toString())){
				tipoEscolhido = tipoManutencao;
			}
		}
	}

	private boolean checkFields() {
		if (descricaoMaterialEditText.getEditableText().toString().equals("")){
			descricaoMaterialEditText.setError("Campo Obrigatório");
			descricaoMaterialEditText.requestFocus();
		}
		else if(valorMaterialEditText.getEditableText().toString().equals("") || valorMaterialEditText.getEditableText().toString().equals("R$0,00")) {
			valorMaterialEditText.setError("Campo Obrigatório");
			valorMaterialEditText.requestFocus();
		}
		else if (local == null){
			localMaterialEditText.setError("Campo Obrigatório");
			localMaterialEditText.requestFocus();
		} else{
			return true;
		}
		return false;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == Constantes.MANUTENCAO_CODE)
			if (resultCode == Constantes.LOCAL_LIST) {
				local = data.getParcelableExtra("parcel");
				localMaterialEditText.setText(local.getNome());
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
			valorMaterialEditText.setText(valorMask);
			valorMaterialEditText.setSelection(valorMask.length());
			onSetTextListeners();
		} catch (NumberFormatException e) {
		} catch (ArithmeticException e) {
		}
	}
	private void onRemoveTextListeners() {
		valorMaterialEditText.removeTextChangedListener(this);
	}
	
	private void onSetTextListeners() {
		valorMaterialEditText.addTextChangedListener(this);
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
		descricaoMaterialEditText.setText(manutencao.getDescricaoManutencao());
		valorMaterialEditText.setText(NumberFormat.getCurrencyInstance().format(manutencao.getValorGasto()));
		local = Manutencao.ConsultaLocal(this, "SELECT *"
				 + " FROM tb_local"
				 + " WHERE codigoLOCAL = " + manutencao.getLocalGasto());
		localMaterialEditText.setText(local.getNome());
		dataMaterialEditText.setText(manutencao.tratadata());
		tipoBetterSpinner.setSelection(posicaoTipoManutencao);
	}
	
	private void onSetTipoManutencao() {
		for(int i = 0; i< itens.length; i++){
			if (itens[i].getCodigo() == manutencao.getTipoManutencao())
				posicaoTipoManutencao = i;
		}
		
	}

	private void onDisableInputVariables() {
		descricaoMaterialEditText.setEnabled(false);
		valorMaterialEditText.setEnabled(false);
		localMaterialEditText.setEnabled(false);
		dataMaterialEditText.setEnabled(false);
		tipoBetterSpinner.setEnabled(false);
		
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.toolbar_menu, menu);
		return true;

	}
}
