package br.com.economicdrive;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class RelatorioListActivity extends AppCompatActivity implements OnItemClickListener {

	private ListView lv;
	private int codVeiculo;
	private int tpcomb;
	private ActionBar actionBar;
	private Toolbar toolbar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_relatorio_list);
        toolbar = (Toolbar) findViewById(R.id.myRelatorioToolbar);
		onActionBar();
		Intent origemIntent = this.getIntent();
		Bundle b = origemIntent.getExtras();
		tpcomb = b.getInt("tpcomb");
		codVeiculo = b.getInt("veiculo");
		lv = (ListView) findViewById(R.id.relatoriolist);
		String [] itens = 
				new String[]{"Gastos","Desempenho"};
		ArrayAdapter <String> adapter = 
				new ArrayAdapter <String> (this,android.R.layout.simple_list_item_1, itens);
		lv.setAdapter(adapter);
		registerForContextMenu(lv);
		lv.setOnItemClickListener(this);
	}

	private void onActionBar() {
		setSupportActionBar(toolbar);
		actionBar = getSupportActionBar();
		actionBar.setBackgroundDrawable(new ColorDrawable(Color.rgb(3, 103, 221)));
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setHomeButtonEnabled(true);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		finish();
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Intent i;
		switch (position) {
		case 0:
			i = new Intent(this, OpcaoGastoActivity.class);
			i.putExtra("veiculo", codVeiculo);
			startActivity(i);
			break;
		case 1:
			i = new Intent(this, OpcaoAbastecimentoActivity.class);
			i.putExtra("veiculo", codVeiculo);
			i.putExtra("tpcomb", tpcomb);
			startActivity(i);
			break;
		}
	}
}
