package br.com.economicdrive;

import java.util.List;

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;

import br.com.economicdrive.model.Carro;
import br.com.economicdrive.model.Marca;
import br.com.economicdrive.model.Modelo;
import br.com.economicdrive.model.TipoCombustivel;

public class CarroActivity extends AppCompatActivity implements Button.OnClickListener {
	
	private int marcabd;
	private int modelobd;
	private int combbd;
	private int codcarro;
	private String ativo;
	private int edicao = 0;
	private Spinner marcaSpinner;
	private Spinner modeloSpinner;
	private Spinner combustivelSpinner;
	private EditText apelidoEditText;
	private EditText placaalfaEditText;
	private EditText placanumEditText;
	private ImageButton salvarCarroButton;
	private ArrayAdapter<Marca> adapterMarca;
	private ArrayAdapter<Modelo> adapterModelo;
	private ArrayAdapter<TipoCombustivel> adapterTpCombustivel;
	private int intAcao;
	private Marca escolhaMarca = null;
	private Modelo escolhaModelo = null;
	private TipoCombustivel escolhaComb = null;
	private ActionBar actionBar;
    private Toolbar toolbar;
	private Carro carro;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_carro);
		

		marcaSpinner = (Spinner) findViewById(R.id.Marcaspinner);
		modeloSpinner = (Spinner) findViewById(R.id.modelospinner);
		combustivelSpinner = (Spinner) findViewById(R.id.combsspinner);
		apelidoEditText = (EditText) findViewById(R.id.apelidoEditText);
		placaalfaEditText = (EditText) findViewById(R.id.placaalfaEdittext);
		placanumEditText = (EditText) findViewById(R.id.placanumEdittext);
		salvarCarroButton = (ImageButton) findViewById(R.id.salvarCarroButon);
        toolbar = (Toolbar) findViewById(R.id.myCarroActivityToolbar);
		
		//recuperar as informacoes do bundle
		Intent origemIntent = this.getIntent();
		Bundle b = origemIntent.getExtras();
		carro = getIntent().getParcelableExtra("parcel");
		intAcao = b.getInt("acao");

        if (intAcao != Constantes.INSERIR) {
            onGetCarro();
            if (intAcao == Constantes.VISUALIZAR) {
                OnDisableVariables();
            }
        }
		onActionBar();
		salvarCarroButton.setOnClickListener(this);
		List <Marca> marca = Marca.consultarMarca(this, "SELECT * FROM tb_marcas");
		Marca [] itens = 
				 marca.toArray(new Marca[0]);
		adapterMarca = new ArrayAdapter <Marca> (this, R.layout.spinner_dropdown_list, itens);
		marcaSpinner.setAdapter(adapterMarca);
		if (intAcao == Constantes.EDITAR || intAcao == Constantes.VISUALIZAR){
			marcaSpinner.setSelection(marcabd - 1);
		}
		List <TipoCombustivel> tipocombustivel = TipoCombustivel.consultarCombustivel(this, "SELECT * FROM tb_tipo_combustivel");
		TipoCombustivel [] itens3 = 
				tipocombustivel.toArray(new TipoCombustivel[0]);
		adapterTpCombustivel = new ArrayAdapter <TipoCombustivel> (this, R.layout.spinner_dropdown_list, itens3);
		combustivelSpinner.setAdapter(adapterTpCombustivel);
		if (intAcao == Constantes.EDITAR || intAcao == Constantes.VISUALIZAR){
			combustivelSpinner.setSelection(combbd - 1);
		}
		//Cria o objeto com o tipo de combustivel escolhido e restringe os modelos de acordo com a marca
		marcaSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
		{
			@Override
		    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
		    {
				escolhaMarca = adapterMarca.getItem(position);
				String sql = "SELECT * FROM TB_MODELOS WHERE marcaMODELO = " + escolhaMarca.getCodigo();
				List <Modelo> modelo = Modelo.consultarModelo(CarroActivity.this, sql);
				Modelo [] itens2 = 
						 modelo.toArray(new Modelo[0]);
				adapterModelo = new ArrayAdapter <Modelo> (CarroActivity.this, R.layout.spinner_dropdown_list, itens2);
				modeloSpinner.setAdapter(adapterModelo);
				if ((intAcao == Constantes.EDITAR || intAcao == Constantes.VISUALIZAR) && edicao == 0){
					for (int i = 0 ; i <= itens2.length ; i++){
						if (itens2[i].getCodigo() == modelobd){
							modelobd = i;
							break;
						}
					}
					modeloSpinner.setSelection(modelobd);
					edicao = 1;
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				
			}
		});
		//Cria o objeto com o modelo escolhido
		modeloSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,int position, long id) {
				escolhaModelo = adapterModelo.getItem(position);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				
			}
			
		});
		//Cria o objeto com o tipo de combustivel escolhido
		combustivelSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,int position, long id) {
				escolhaComb = adapterTpCombustivel.getItem(position);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {}
		});
		
		placaalfaEditText.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
			
			@Override
			public void afterTextChanged(Editable s) {
				if (placaalfaEditText.length() == 3){
					placanumEditText.requestFocus();
				}
			}
		});
	}

	private void onGetCarro() {
		apelidoEditText.setText(carro.getNome());
		placaalfaEditText.setText(carro.getPlaca().substring(0, 3));
		placanumEditText.setText(carro.getPlaca().substring(4, 8));
		marcabd = carro.getMarca();
		modelobd = carro.getModelo();
		combbd = carro.getComb();
		codcarro = carro.getCodigo();
		ativo = carro.getAtivo();
	}

	private void onActionBar() {
		setSupportActionBar(toolbar);
		actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setHomeButtonEnabled(true);
		actionBar.setBackgroundDrawable(new ColorDrawable(Color.rgb(3, 103, 221)));
	}

	private void OnDisableVariables() {
		apelidoEditText.setEnabled(false);
		placaalfaEditText.setEnabled(false);
		placanumEditText.setEnabled(false);
		modeloSpinner.setEnabled(false);
		marcaSpinner.setEnabled(false);
		combustivelSpinner.setEnabled(false);
		salvarCarroButton.setVisibility(View.INVISIBLE);
		
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

	@Override
	public void onClick(View v) {
		try{
			if(apelidoEditText.getEditableText().toString().equals("")){
				Toast.makeText(getApplicationContext(), "O apelido não pode estar vazio", Toast.LENGTH_SHORT).show();
			}
			else{
				if(placanumEditText.getEditableText().equals("") || placaalfaEditText.getEditableText().equals("") ){
					Toast.makeText(getApplicationContext(), "A placa não pode estar vazia", Toast.LENGTH_SHORT).show();
				}
				else{
					if (placanumEditText.getEditableText().length() != 4 || placaalfaEditText.getEditableText().length() != 3 ) {
						Toast.makeText(getApplicationContext(), "A placa inválida", Toast.LENGTH_SHORT).show();
					}
					else{
						if (escolhaComb == null || escolhaMarca == null || escolhaModelo == null ){
							Toast.makeText(getApplicationContext(), "Marca,veículo ou combustivel não selecionado", Toast.LENGTH_SHORT ).show();
						}
						else{
							String apelido = apelidoEditText.getEditableText().toString();
							String placa = placaalfaEditText.getEditableText().toString() + '-' + placanumEditText.getEditableText().toString();
							//validar se o veiculo ja existe para garantir que não exitirá duplicidade no banco de dados
							if (intAcao == Constantes.INSERIR){
								Carro carro = null;
								if (Carro.consultaCarroAtivo(this) == null)
									carro = new Carro(0, apelido, 0, placa, escolhaMarca.getCodigo(), escolhaModelo.getCodigo(), escolhaComb.getCodigo(),"sim");
								else
									carro = new Carro(0, apelido, 0, placa, escolhaMarca.getCodigo(), escolhaModelo.getCodigo(), escolhaComb.getCodigo(),"nao");
								String sql = "SELECT idCARRO,nomeCARRO, kmCARRO, placaCARRO, idMARCA, idMODELO, idTPCOMBUSTIVEL, ativo " +
										"FROM tb_carro " +
										"INNER JOIN tb_modelos ON idMODELO = modeloCARRO " +
										"INNER JOIN tb_marcas ON marcaMODELO = idMARCA " +
										"INNER JOIN tb_tipo_combustivel ON idTPCOMBUSTIVEL = combCARRO " +
										"WHERE placaCARRO = '" + carro.getPlaca() + "' " +
										"OR nomeCARRO = '" + carro.getNome() + "';";
								List<Information> carro2 = Carro.consultaCarro(this,sql);
								Carro[] itens = carro2.toArray(new Carro[0]);
								if (itens.length > 0 ){
									Toast.makeText(getApplicationContext(), "Já existe um veículo com esta placa ou nome cadastrado", Toast.LENGTH_SHORT).show();
								}else{
									Toast.makeText(getApplicationContext(), "Veículo incluido com sucesso", Toast.LENGTH_SHORT).show();
									placaalfaEditText.setText("");
									placanumEditText.setText("");
									apelidoEditText.setText("");
                                    ProfileDrawerItem profileDrawerItem =
                                            new ProfileDrawerItem().withName(carro.getNome()).withIcon(getResources().getDrawable(R.mipmap.ic_car));
                                    AccountHeader accountHeader =
                                            MainActivity.getAccountHeaderLeft();
                                    if (Carro.consultaCarroAtivo(this) == null) {
                                        accountHeader.getProfiles().remove(0);
                                    }
                                    accountHeader.addProfile(profileDrawerItem, 0);
                                    carro.insereCarro(this);
                                    finish();

								}
	
							}	
							else{
								Carro carro = new Carro(codcarro, apelido, 0, placa, escolhaMarca.getCodigo(), escolhaModelo.getCodigo(), escolhaComb.getCodigo(), ativo);
								String sql = "SELECT idCARRO,nomeCARRO, kmCARRO, placaCARRO, idMARCA, idMODELO, idTPCOMBUSTIVEL, ativo " +
										"FROM tb_carro " +
										"INNER JOIN tb_modelos ON idMODELO = modeloCARRO " +
										"INNER JOIN tb_marcas ON marcaMODELO = idMARCA " +
										"INNER JOIN tb_tipo_combustivel ON idTPCOMBUSTIVEL = combCARRO " +
										"WHERE placaCARRO = '" + carro.getPlaca() + "'";
								List<Information> carro2 = Carro.consultaCarro(this,sql);
								Carro[] itens = carro2.toArray(new Carro[0]);
								if (itens.length > 0 && itens[0].getCodigo() != carro.getCodigo()){
									Toast.makeText(getApplicationContext(), "Já existe um veículo com esta placa cadastrado", Toast.LENGTH_SHORT).show();
								}else{
									carro.alteraCarro(this);
									Toast.makeText(getApplicationContext(), "Veículo alterado com sucesso", Toast.LENGTH_SHORT).show();
									placaalfaEditText.setText("");
									placanumEditText.setText("");
									apelidoEditText.setText("");
									finish();
								}
							}
						}
					}
				}
			}
		}
		catch(Error e){
			Toast.makeText(getApplicationContext(), "Erro ao gerenciar veículo",Toast.LENGTH_SHORT).show();
		}
		
	}
	
}
