package br.com.economicdrive;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class OpcaoGastoActivity extends AppCompatActivity implements OnClickListener {
	
	private TextView datainicio2TextView;
	private TextView datafim2TextView;
	private Button gerarrelatorio2Button;
	private Toolbar toolbar;
	private ActionBar actionBar;
	private int operacao;
	private int codVeiculo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_opcao_gasto);
		datainicio2TextView = (TextView) findViewById(R.id.datainicio2TextView);
		datafim2TextView = (TextView) findViewById(R.id.datafim2TextView);
		gerarrelatorio2Button = (Button) findViewById(R.id.GerarRela2Abasbutton);
        toolbar = (Toolbar) findViewById(R.id.myGastosActivityToolbar);
        onActionBar();
		datainicio2TextView.setText(getDateTimeinicio());
		datafim2TextView.setText(getDateTime());
		datainicio2TextView.setOnClickListener(this);
		datafim2TextView.setOnClickListener(this);
		gerarrelatorio2Button.setOnClickListener(this);
		Intent origemIntent = this.getIntent();
		Bundle b = origemIntent.getExtras();
		codVeiculo = b.getInt("veiculo");
		
	}

	private void onActionBar() {
        setSupportActionBar(toolbar);
		actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setHomeButtonEnabled(true);
		actionBar.setBackgroundDrawable(new ColorDrawable(Color.rgb(3, 103, 221)));
	}

	public void onClick(View v) {
		SimpleDateFormat data = new SimpleDateFormat("dd/MM/yyyy");
		Date fim;
		Date inicio;
		Date hoje = new Date();
		try {
			fim = data.parse(datafim2TextView.getText().toString());
			inicio = data.parse(datainicio2TextView.getText().toString());
			hoje = data.parse(data.format(hoje));
			switch (v.getId()) {
			case R.id.datainicio2TextView:
				showDatePickerDialog(v);
				break;
			case R.id.datafim2TextView:
				showDatePickerDialog(v);
				break;
			case R.id.GerarRela2Abasbutton:
				if (inicio.after(fim)){
					Toast.makeText(getApplicationContext(), "Data de início não pode ser maior que a data final", Toast.LENGTH_SHORT).show();
				}
				else if (fim.after(hoje)){
					Toast.makeText(getApplicationContext(), "Data final não pode ser maior que hoje", Toast.LENGTH_SHORT).show();
				}
				else if (inicio.after(hoje)){
					Toast.makeText(getApplicationContext(), "Data de início não pode ser maior que hoje", Toast.LENGTH_SHORT).show();
				}
				else{
					Intent i = new Intent(this, GastoChartActivity.class);
					i.putExtra("inicio", datainicio2TextView.getText().toString());
					i.putExtra("fim", datafim2TextView.getText().toString());
					i.putExtra("codVeiculo", codVeiculo);
					startActivity(i);
				}
				break;
			
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
		case R.id.datainicio2TextView:
			operacao = 0;
			break;
		case R.id.datafim2TextView:
			operacao = 1;
		}
		DialogFragment newFragment = new DatePickerFragment(operacao);
		newFragment.show(getFragmentManager(), "datePicker");
	}
}
