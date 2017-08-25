package br.com.economicdrive;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.BarChart;
import org.achartengine.chart.LineChart;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

import br.com.economicdrive.model.Abastecimento;

public class AbastecimentoChartActivity extends AppCompatActivity {

	private View mChart;
	private TabHost abas = null;
	private ListView listView;
	private ArrayAdapter <String> adapter;
	private String inicio;
	private String fim;
	private String sql;
	private int codVeiculo;
	private int comb;
	private NumberFormat formatarFloat = new DecimalFormat("0.00");
	private Toolbar toolbar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_abastecimento_chart);
		listView = (ListView) findViewById(R.id.relaabastec);
		toolbar = (Toolbar) findViewById(R.id.myAbastChartToolbar);

		//configura a action bar
		applyCustomizingActionBar();
		
		//configura as abas
		abas = (TabHost) findViewById(R.id.tabhost);
		abas.setup();
		TabSpec descritor = abas.newTabSpec("aba1");
	    descritor.setContent(R.id.relaabastec);
	    descritor.setIndicator("Lista");
	    abas.addTab(descritor);
	    descritor = abas.newTabSpec("aba2");
	    descritor.setContent(R.id.chart2);
	    descritor.setIndicator("Gráfico");
	    abas.addTab(descritor);
	    
	    
	    sql = "SELECT * FROM TB_ABASTECIMENTO"
	    		+ " WHERE kilometrosrodados > 0"
	    		+ " AND dataGasto BETWEEN '"
	    		
	    		+ inicio.substring(6,10) + "-" + inicio.subSequence(3, 5) + "-"  + inicio.subSequence(0,2) + "' AND '"
				+ fim.substring(6,10) + "-" + fim.subSequence(3, 5) + "-"  + fim.subSequence(0,2) + "'"
	    		+ " AND idCarro = " + codVeiculo 
	    		+ " AND idCombustivel = " + comb 
	    		+ " ORDER BY dataGasto, codigoGasto";
	    
	    //carrega a lista de abastecimento
		List<Information> abastecimentoList = Abastecimento.ConsultaAbastecimentos(this, sql);
		Abastecimento[] itens = abastecimentoList.toArray(new Abastecimento[0]);
		String valor[];
		if (itens.length > 0){
			valor = new String[itens.length];
			for (int i=0;i<valor.length;i++){
				valor[i] = itens[i].mediaRodada();
			}
		}else{
			valor = new String[1];
			valor[0] = "Não existem dados para serem exibidos";
		}
		
		adapter = new ArrayAdapter<>(this,
				android.R.layout.simple_list_item_1,valor);
		listView.setAdapter(adapter);
		registerForContextMenu(listView);
		
		//carrega o grafico do demonio
	    //openPIEChart();
		openBARchart();
	}

	private void applyCustomizingActionBar() {
		setSupportActionBar(toolbar);
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setHomeButtonEnabled(true);
		actionBar.setBackgroundDrawable(new ColorDrawable(Color.rgb(3, 103, 221)));

		Intent origemIntent = this.getIntent();
		Bundle b = origemIntent.getExtras();
		inicio = b.getString("inicio");
		fim = b.getString("fim");
		codVeiculo = b.getInt("codVeiculo");
		comb = b.getInt("comb");
		actionBar.setTitle(inicio + " A " + fim);
	}

	private void openBARchart(){
		
		List<Information> abastecimentoList = Abastecimento.ConsultaAbastecimentos(this, sql);
		Abastecimento[] itens = abastecimentoList.toArray(new Abastecimento[0]);
		int size = 0;
		
		if (itens.length == 1){
			size = 2;
		}else{
			size =itens.length; 
		}
		String[] mMonth = new String[size];
		int[] x = new int[size];
		double[] income = new double[size];
		double[] media = new double[size];
		double total = 0;
		BigDecimal bd;
		
		for(int i=0;i<itens.length;i++){
			if (itens.length == 1){
				mMonth[i] = itens[i].tratadata();
				mMonth[i + 1] = itens[i].tratadata();
				x[i] = 0;
				x[i + 1 ] = 1 ;
				income[i] = Double.parseDouble(formatarFloat.format(itens[i].getKmdif() / (itens[i].getLitros())).replace(",", "."));
				income[i + 1] = 0;
				total = total + (itens[i].getKmdif() / (itens[i].getLitros()));	
			}else{
				mMonth[i] = itens[i].tratadata();
				x[i] = i;
				income[i] = Double.parseDouble(formatarFloat.format(itens[i].getKmdif() / (itens[i].getLitros())).replace(",", "."));
				total = total + (itens[i].getKmdif() / (itens[i].getLitros()));
			}
		}
		
		if (total > 0){
			if (itens.length == 1){
				total = Double.parseDouble(formatarFloat.format(total/1).replace(",", "."));
			}else{
				total = Double.parseDouble(formatarFloat.format(total/media.length).replace(",", "."));	
			}
			
		}
		if (itens.length == 1){
			media[0] = Double.parseDouble("0");			
		}else{
			for(int i=0;i<x.length;i++){	
				media[i] = Double.parseDouble(formatarFloat.format(total).replace(",", "."));	
			}
		}
		// Creating an? XYSeries for Income
		XYSeries mediaSeries = new XYSeries("Média");
		XYSeries incomeSeries = new XYSeries("Datas de abastecimento");
		
		
		// Adding data to Income and Expense Series
		for(int i=0;i<x.length;i++){
			incomeSeries.add(i,income[i]);
			mediaSeries.add(i, media[i]);
		}	

		// Creating a dataset to hold each series
		XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
		// Adding Income Series to the dataset
		dataset.addSeries(mediaSeries);
		dataset.addSeries(incomeSeries);
		

		//linha da media 
        XYSeriesRenderer mediaRenderer = new XYSeriesRenderer();
        mediaRenderer.setColor(Color.rgb(46, 139, 87));
        mediaRenderer.setPointStyle(PointStyle.CIRCLE);
        mediaRenderer.setFillPoints(true);
        mediaRenderer.setLineWidth(10);
        mediaRenderer.setChartValuesTextSize(30);
        mediaRenderer.setDisplayChartValues(true);
		
		// Creating XYSeriesRenderer to customize incomeSeries
		XYSeriesRenderer incomeRenderer = new XYSeriesRenderer();
		incomeRenderer.setColor(Color.rgb(3, 103, 221));
		incomeRenderer.setFillPoints(true);
		incomeRenderer.setDisplayChartValues(true);
		incomeRenderer.setChartValuesTextSize(30);
		incomeRenderer.setChartValuesTextAlign(Align.CENTER);
		
		// Creating a XYMultipleSeriesRenderer to customize the whole chart
		XYMultipleSeriesRenderer multiRenderer = new XYMultipleSeriesRenderer();
		multiRenderer.setXLabels(0);
		multiRenderer.setChartTitle("Abastecimentos");
		multiRenderer.setXTitle("Datas");
		multiRenderer.setYTitle("Km/L");	
		multiRenderer.setZoomButtonsVisible(false);
		multiRenderer.setPanEnabled(true, true);
		for(int i=0; i< x.length;i++){
			multiRenderer.addXTextLabel(i, mMonth[i]);
		}
		
		multiRenderer.setChartTitleTextSize(40);
		multiRenderer.setLegendTextSize(40);
		multiRenderer.setAxisTitleTextSize(40);
		multiRenderer.setLabelsTextSize(30);
		multiRenderer.setBarSpacing(1);
		multiRenderer.setBarWidth(1);
		multiRenderer.setMarginsColor(Color.argb(0x00, 0xff, 0x00, 0x00));
		multiRenderer.setYLabelsColor(0, Color.BLACK);
		multiRenderer.setXLabelsColor(Color.BLACK);
		multiRenderer.setLabelsColor(Color.BLACK);
		multiRenderer.setGridColor(Color.GRAY);
		multiRenderer.setShowGrid(true);
		multiRenderer.setMargins(new int[] { 80, 70, 80, 0 });
		multiRenderer.setXAxisMin(0);
		multiRenderer.setYAxisMin(0);
		// Adding incomeRenderer and expenseRenderer to multipleRenderer
		// Note: The order of adding dataseries to dataset and renderers to multipleRenderer
		// should be same
		
		multiRenderer.addSeriesRenderer(mediaRenderer);
		multiRenderer.addSeriesRenderer(incomeRenderer);
		
		
		LinearLayout chartContainer = (LinearLayout) findViewById(R.id.chart2);
		//remove any views before u paint the chart
		chartContainer.removeAllViews();
		
		String[] types = new String[] { LineChart.TYPE, BarChart.TYPE };
		  
		    // Creating a combined chart with the chart types specified in types array
	  	mChart = (GraphicalView) ChartFactory.getCombinedXYChartView(AbastecimentoChartActivity.this, dataset, multiRenderer, types);
		//drawing bar chart
		
	  	//mChart = ChartFactory.getBarChartView(AbastecimentoChartActivity.this, dataset, multiRenderer,Type.DEFAULT);
		//adding the view to the linearlayout
		chartContainer.addView(mChart);

		/*// Creating an intent to plot bar chart using dataset and multipleRenderer
		Intent intent = ChartFactory.getBarChartIntent(getBaseContext(), dataset, multiRenderer, Type.STACKED);

		// Start Activity
		startActivity(intent);	*/
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
}
