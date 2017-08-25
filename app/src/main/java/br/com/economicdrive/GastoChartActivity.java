package br.com.economicdrive;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.model.CategorySeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

import br.com.economicdrive.adapter.ExpandableListAdapter;
import br.com.economicdrive.model.Despesas;
import br.com.economicdrive.model.Manutencao;

public class GastoChartActivity extends AppCompatActivity {

	private View mChart;
	private TabHost abas = null;
	private String inicio;
	private String fim;
	private String sqlAbastecimento;
	private String sqlDespesa;
	private String sqlManutencao;
	private Double totala = 0.00;
	private Double totald = 0.00;
	private Double totalm = 0.00;
	private ActionBar actionBar;
	private Toolbar toolbar;
	
	
    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    private int codVeiculo;
    private NumberFormat formatarFloat = new DecimalFormat("0.00"); 

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gasto_chart);
		expListView = (ExpandableListView) findViewById(R.id.lvExp);
		toolbar = (Toolbar) findViewById(R.id.myGastoChartToolbar);

		//configura a action bar	
		onActionBar();
		
		//Recupera os bundle e configura as string sql de pesquisa
		Intent origemIntent = this.getIntent();
		Bundle b = origemIntent.getExtras();
		inicio = b.getString("inicio");
		fim = b.getString("fim");
		codVeiculo = b.getInt("codVeiculo");
		actionBar.setTitle(inicio + " A " + fim);
		
		sqlAbastecimento = "SELECT * FROM TB_ABASTECIMENTO WHERE dataGasto BETWEEN '" 
				   + inicio.substring(6,10) + "-" + inicio.subSequence(3, 5) + "-"  + inicio.subSequence(0,2) + "' AND '"
				   + fim.substring(6,10) + "-" + fim.subSequence(3, 5) + "-"  + fim.subSequence(0,2) + "'"
				   + " AND idCarro = " + codVeiculo
				   + " ORDER BY dataGasto, codigoGasto";

		sqlDespesa = "SELECT * FROM TB_DESPESAS WHERE dataGasto BETWEEN '" 
				   + inicio.substring(6,10) + "-" + inicio.subSequence(3, 5) + "-"  + inicio.subSequence(0,2) + "' AND '"
				   + fim.substring(6,10) + "-" + fim.subSequence(3, 5) + "-"  + fim.subSequence(0,2) + "'"				   
				   + " AND idCarro = " + codVeiculo
				   + " ORDER BY dataGasto, codigoGasto";
		
		sqlManutencao = "SELECT * FROM TB_MANUTENCAO WHERE dataGasto BETWEEN '" 
				   + inicio.substring(6,10) + "-" + inicio.subSequence(3, 5) + "-"  + inicio.subSequence(0,2) + "' AND '"
				   + fim.substring(6,10) + "-" + fim.subSequence(3, 5) + "-"  + fim.subSequence(0,2) + "'"				   
				   + " AND idCarro = " + codVeiculo
				   + " ORDER BY dataGasto, codigoGasto";
		
        // preparing list data
        prepareListData();
 
        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);
 
        // setting list adapter
        expListView.setAdapter(listAdapter);

		
		//configura as abas
		abas = (TabHost) findViewById(R.id.tabhost);
		abas.setup();
		TabSpec descritor = abas.newTabSpec("aba1");
	    descritor.setContent(R.id.lvExp);
	    descritor.setIndicator("Lista");
	    abas.addTab(descritor);
	    descritor = abas.newTabSpec("aba2");
	    descritor.setContent(R.id.chart3);
	    descritor.setIndicator("Gráfico");
	    abas.addTab(descritor);
	    
	    openPIEChart();
	}

	private void onActionBar() {
		setSupportActionBar(toolbar);
		actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setHomeButtonEnabled(true);
		actionBar.setBackgroundDrawable(new ColorDrawable(Color.rgb(3, 103, 221)));
	}

	private void openPIEChart(){
		// Pie Chart Section Names
		
		String[] code = new String[] {
				listDataHeader.get(1),listDataHeader.get(0) , listDataHeader.get(2)};

		// Pie Chart Section Value
		double[] distribution = { totald, totala, totalm};
	
		// Color of each Pie Chart Sections
		int[] colors = { Color.BLUE,  Color.rgb(50, 205, 50),  Color.RED};

		// Instantiating CategorySeries to plot Pie Chart
		CategorySeries distributionSeries = new CategorySeries("Gastos");
		for(int i=0 ;i < distribution.length;i++){
			// Adding a slice with its values and name to the Pie Chart
			distributionSeries.add(code[i], distribution[i]);
		}
		// Instantiating a renderer for the Pie Chart
		DefaultRenderer defaultRenderer = new DefaultRenderer();
		for(int i = 0 ;i<distribution.length;i++){
			SimpleSeriesRenderer seriesRenderer = new SimpleSeriesRenderer();
			seriesRenderer.setColor(colors[i]);
			seriesRenderer.setDisplayChartValues(true);
			// Adding a renderer for a slice
			defaultRenderer.addSeriesRenderer(seriesRenderer);
		}
		
		defaultRenderer.setLegendTextSize(30);
		defaultRenderer.setLabelsColor(Color.BLACK);
		defaultRenderer.setLabelsTextSize(30);

		defaultRenderer.setChartTitle("Gastos");
		defaultRenderer.setChartTitleTextSize(40);
		defaultRenderer.setZoomButtonsVisible(false);
		
		LinearLayout chartContainer = (LinearLayout) findViewById(R.id.chart3);
		
		mChart = ChartFactory.getPieChartView(GastoChartActivity.this, distributionSeries , defaultRenderer);
		chartContainer.addView(mChart);
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
	
	private void prepareListData() {
		
		listDataHeader = new ArrayList<String>();
		listDataChild = new HashMap<String, List<String>>();
		
		//carrega os dados de abastecimentos
		List<String> abastec = new ArrayList<String>();
		List<Information> abastecimentoList = Abastecimento.ConsultaAbastecimentos(this, sqlAbastecimento);
		Abastecimento[] itensAbastecimento = abastecimentoList.toArray(new Abastecimento[0]);
		if (itensAbastecimento.length > 0) {
			for (int i = 0; i < itensAbastecimento.length; i++){
				abastec.add(itensAbastecimento[i].tratadata() + " - R$ " + formatarFloat.format(itensAbastecimento[i].getValorGasto()));
				totala += itensAbastecimento[i].getValorGasto();
			}
			listDataHeader.add("Abastecimentos (R$" + formatarFloat.format(totala) + ")");
		}else {
			listDataHeader.add("Abastecimentos (R$0,00)");	
		}
		
		//Carrega as informa��es de despesas
		List<String> despesas = new ArrayList<String>();
		List<Information> despesasList = Despesas.ConsultaDespesas(this, sqlDespesa);
		Despesas[] itensdespesas = despesasList.toArray(new Despesas[0]);
		if (itensdespesas.length > 0) {
			for (int i = 0; i < itensdespesas.length; i++){
				despesas.add(itensdespesas[i].tratadata() + " - " + itensdespesas[i].getDescricaoDespesa() 
							+ " - R$ " + formatarFloat.format(itensdespesas[i].getValorGasto()));
				totald += itensdespesas[i].getValorGasto();
			}
			listDataHeader.add("Despesas (R$" + formatarFloat.format(totald) + ")");
		}else {
			listDataHeader.add("Despesas (R$0,00)");	
		}
		//Carrega as informacoes de manutencao
		List<String> manutencao = new ArrayList<String>();
		List<Information> manutencaoList = Manutencao.ConsultaManutencao(this, sqlManutencao);
		Manutencao[] itensManutencao = manutencaoList.toArray(new Manutencao[0]);
		if (itensManutencao.length > 0) {
			for (int i = 0; i < itensManutencao.length; i++){
				manutencao.add(itensManutencao[i].tratadata() + " - " + itensManutencao[i].getDescricaoManutencao() 
							+ " - R$ " + formatarFloat.format(itensManutencao[i].getValorGasto()));
				totalm += itensManutencao[i].getValorGasto();
			}
			listDataHeader.add("Manutenção (R$" + formatarFloat.format(totalm) + ")");
		}else {
			listDataHeader.add("Manutenção (R$0,00)");
		}
		listDataChild.put(listDataHeader.get(0), abastec); // Header, Child data
		listDataChild.put(listDataHeader.get(1), despesas);
		listDataChild.put(listDataHeader.get(2), manutencao);
	}
}
