package br.com.economicdrive;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.app.DialogFragment;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import br.com.economicdrive.fragment.DatePickerFragment;
import br.com.economicdrive.holder.Combustivel;

public class OpcaoAbastecimentoActivity extends AppCompatActivity implements OnClickListener{
	private Spinner tpcombustiveSpinner;
	private TextView datainicioTextView;
	private TextView datafimTextView;
	private Button gerarrelatorioButton;
	private ActionBar actionBar;
	private Toolbar toolbar;
	private ArrayAdapter<Combustivel> adapterTpComb;
	private int operacao;
	private int codVeiculo;
	private Combustivel escolhaComb;
	private int tpcomb;
	private String sql;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_opcao_abastecimento);
		tpcombustiveSpinner = (Spinner) findViewById(R.id.tpCombustivelrelaSpinner);
		datainicioTextView = (TextView) findViewById(R.id.datainicioTextView);
		datafimTextView = (TextView) findViewById(R.id.datafimTextView);
		gerarrelatorioButton = (Button) findViewById(R.id.GerarRelaAbasbutton);
		toolbar = (Toolbar) findViewById(R.id.myOpcoesToolbar);
		OnActionBar();
		datainicioTextView.setText(getDateTimeinicio());
		datafimTextView.setText(getDateTime());
		datainicioTextView.setOnClickListener(this);
		datafimTextView.setOnClickListener(this);
		gerarrelatorioButton.setOnClickListener(this);
		Intent origemIntent = this.getIntent();
		Bundle b = origemIntent.getExtras();
		codVeiculo = b.getInt("veiculo");
		tpcomb = b.getInt("tpcomb");
		switch (tpcomb){
		case Constantes.GASOLINA:
			sql = "SELECT *"
			     + " FROM tb_Combustivel"
			     + " WHERE idCOMBUSTIVEL = 4"
			     + " OR idCOMBUSTIVEL = 5;";
				break;
		case Constantes.ALCOOL:
			sql =  "SELECT *"
					+ " FROM tb_Combustivel"
					+ " WHERE idCOMBUSTIVEL = 1"
					+ " OR idCOMBUSTIVEL = 2;";
			break;
		case Constantes.FLEX:
			sql =  "SELECT *"
					+ " FROM tb_Combustivel"
					+ " WHERE idCOMBUSTIVEL = 1"
			        + " OR idCOMBUSTIVEL = 2"
			        + " OR idCOMBUSTIVEL = 4"
			        + " OR idCOMBUSTIVEL = 5;";
			break;
		case Constantes.DIESEL:
			sql =  "SELECT *"
					+ " FROM tb_Combustivel"
					+ " WHERE idCOMBUSTIVEL = 3;";
			break;
		}
		
		List <Combustivel> combustivel = Combustivel.consultarComb(this, sql);
		Combustivel [] itens = 
				combustivel.toArray(new Combustivel[0]);
		adapterTpComb = new ArrayAdapter <Combustivel> (this, R.layout.rowsrelatorio,R.id.abast, itens);
		tpcombustiveSpinner.setAdapter(adapterTpComb);
		tpcombustiveSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
		{
			@Override
		    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
		    {
				escolhaComb = adapterTpComb.getItem(position);
		    }

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}
		});

	}

	private void OnActionBar() {
		setSupportActionBar(toolbar);
		actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setHomeButtonEnabled(true);
		actionBar.setBackgroundDrawable(new ColorDrawable(Color.rgb(3, 103, 221)));
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private String getDateTime() {
		Date date = new Date();
		return new SimpleDateFormat("dd/MM/yyyy").format(date);
	}
	
	private String getDateTimeinicio() {
		Date date = new Date();
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DATE, -30);
		date = c.getTime();
		return new SimpleDateFormat("dd/MM/yyyy").format(date);
	}
	
	public void showDatePickerDialog(View v) {
		switch (v.getId()) {
		case R.id.datainicioTextView:
			operacao = 0;
			break;
		case R.id.datafimTextView:
			operacao = 1;
		}
		DialogFragment newFragment = new DatePickerFragment(operacao);
		newFragment.show(getFragmentManager(), "datePicker");
	}

	@Override
	public void onClick(View v) {
		SimpleDateFormat data = new SimpleDateFormat("dd/MM/yyyy");
		Date fim;
		Date inicio;
		Date hoje = new Date();
		try {
			fim = data.parse(datafimTextView.getText().toString());
			inicio = data.parse(datainicioTextView.getText().toString());
			hoje = data.parse(data.format(hoje));
			switch (v.getId()) {
			case R.id.datainicioTextView:
				showDatePickerDialog(v);
				break;
			case R.id.datafimTextView:
				showDatePickerDialog(v);
				break;
			case R.id.GerarRelaAbasbutton:
				if (tpcombustiveSpinner == null){
					Toast.makeText(getApplicationContext(), "Selecione um tipo de combustível", Toast.LENGTH_SHORT).show();
				}
				else if (inicio.after(fim)){
					Toast.makeText(getApplicationContext(), "Data de início não pode ser maior que a data final", Toast.LENGTH_SHORT).show();
				}
				else if (fim.after(hoje)){
					Toast.makeText(getApplicationContext(), "Data final não pode ser maior que hoje", Toast.LENGTH_SHORT).show();
				}
				else if (inicio.after(hoje)){
					Toast.makeText(getApplicationContext(), "Data de início não pode ser maior que hoje", Toast.LENGTH_SHORT).show();
				}
				else{
					Intent i = new Intent(this, AbastecimentoChartActivity.class);
					i.putExtra("inicio", datainicioTextView.getText().toString());
					i.putExtra("fim", datafimTextView.getText().toString());
					i.putExtra("codVeiculo", codVeiculo);
					i.putExtra("comb", escolhaComb.getCodigo());
					startActivity(i);
				}
				break;
			
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
