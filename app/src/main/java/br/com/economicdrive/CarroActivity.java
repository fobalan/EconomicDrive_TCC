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
import android.widget.Toast;

import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import br.com.economicdrive.constantes.Constantes;
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
    private MaterialBetterSpinner marcaBetterSpinner;
    private MaterialBetterSpinner modeloBetterSpinner;
    private MaterialBetterSpinner combustivelBetterSpinner;
    private MaterialEditText apelidoEditText;
    private MaterialEditText placaEditText;
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

        //BetterSpinner
        marcaBetterSpinner = (MaterialBetterSpinner) findViewById(R.id.marcaCarroMaterialBetterSpinner);
        modeloBetterSpinner = (MaterialBetterSpinner) findViewById(R.id.modeloCarroMaterialBetterSpinner);
        combustivelBetterSpinner = (MaterialBetterSpinner) findViewById(R.id.combustivelCarroMaterialBetterSpinner);

        //MaterialEditText
        apelidoEditText = (MaterialEditText) findViewById(R.id.apelidoCarroMaterialEditText);
        placaEditText = (MaterialEditText) findViewById(R.id.placaCarroMaterialEditText);
        toolbar = (Toolbar) findViewById(R.id.carroToolbar);

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
        List<Marca> marca = Marca.consultarMarca(this, "SELECT * FROM tb_marcas");
        Marca[] itens =
                marca.toArray(new Marca[0]);
        adapterMarca = new ArrayAdapter<Marca>(this, R.layout.spinner_dropdown_list, itens);
        marcaBetterSpinner.setAdapter(adapterMarca);
        if (intAcao == Constantes.EDITAR || intAcao == Constantes.VISUALIZAR) {
            marcaBetterSpinner.setSelection(marcabd - 1);
        }
        List<TipoCombustivel> tipocombustivel = TipoCombustivel.consultarCombustivel(this, "SELECT * FROM tb_tipo_combustivel");
        TipoCombustivel[] itens3 =
                tipocombustivel.toArray(new TipoCombustivel[0]);
        adapterTpCombustivel = new ArrayAdapter<TipoCombustivel>(this, R.layout.spinner_dropdown_list, itens3);
        combustivelBetterSpinner.setAdapter(adapterTpCombustivel);
        if (intAcao == Constantes.EDITAR || intAcao == Constantes.VISUALIZAR) {
            combustivelBetterSpinner.setSelection(combbd - 1);
        }
        //Cria o objeto com o tipo de combustivel escolhido e restringe os modelos de acordo com a marca
        marcaBetterSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                escolhaMarca = adapterMarca.getItem(position);
                String sql = "SELECT * FROM TB_MODELOS WHERE marcaMODELO = " + escolhaMarca.getCodigo();
                List<Modelo> modelo = Modelo.consultarModelo(CarroActivity.this, sql);
                Modelo[] itens2 =
                        modelo.toArray(new Modelo[0]);
                adapterModelo = new ArrayAdapter<Modelo>(CarroActivity.this, R.layout.spinner_dropdown_list, itens2);
                modeloBetterSpinner.setAdapter(adapterModelo);
                if ((intAcao == Constantes.EDITAR || intAcao == Constantes.VISUALIZAR) && edicao == 0) {
                    for (int i = 0; i <= itens2.length; i++) {
                        if (itens2[i].getCodigo() == modelobd) {
                            modelobd = i;
                            break;
                        }
                    }
                    modeloBetterSpinner.setSelection(modelobd);
                    edicao = 1;
                }
            }
        });
        //Cria o objeto com o modelo escolhido
        modeloBetterSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                escolhaModelo = adapterModelo.getItem(position);
            }
        });
        //Cria o objeto com o tipo de combustivel escolhido
        combustivelBetterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                escolhaComb = adapterTpCombustivel.getItem(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

    }

    private void onGetCarro() {
        apelidoEditText.setText(carro.getNome());
        placaEditText.setText(carro.getPlaca());
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
        placaEditText.setEnabled(false);
        modeloBetterSpinner.setEnabled(false);
        marcaBetterSpinner.setEnabled(false);
        combustivelBetterSpinner.setEnabled(false);

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
        try {
            if (apelidoEditText.getEditableText().toString().equals("")) {
                apelidoEditText.setError("Campo Obrigatório");
                apelidoEditText.requestFocus();
            } else if (placaEditText.getEditableText().equals("")) {
                placaEditText.setError("Campo Obrigatório");
                placaEditText.requestFocus();
            } else if (escolhaComb == null) {
                combustivelBetterSpinner.setError("Campo Obrigatório");
                combustivelBetterSpinner.requestFocus();
            } else if(escolhaMarca == null){
                marcaBetterSpinner.setError("Campo Obrigatório");
                marcaBetterSpinner.requestFocus();
            } else if ( escolhaModelo == null) {
                modeloBetterSpinner.setError("Campo Obrigatório");
                modeloBetterSpinner.requestFocus();
            } else{
                String apelido = apelidoEditText.getEditableText().toString();
                String placa = placaEditText.getEditableText().toString();
                //validar se o veiculo ja existe para garantir que não exitirá duplicidade no banco de dados
                if (intAcao == Constantes.INSERIR) {
                    Carro carro = null;
                    if (Carro.consultaCarroAtivo(this) == null)
                        carro = new Carro(0, apelido, placa, 0, escolhaMarca.getCodigo(), escolhaModelo.getCodigo(), escolhaComb.getCodigo(), "sim");
                    else
                        carro = new Carro(0, apelido, placa, 0, escolhaMarca.getCodigo(), escolhaModelo.getCodigo(), escolhaComb.getCodigo(), "nao");
                    String sql = "SELECT idCARRO,nomeCARRO, kmCARRO, placaCARRO, idMARCA, idMODELO, idTPCOMBUSTIVEL, ativo " +
                            "FROM tb_carro " +
                            "INNER JOIN tb_modelos ON idMODELO = modeloCARRO " +
                            "INNER JOIN tb_marcas ON marcaMODELO = idMARCA " +
                            "INNER JOIN tb_tipo_combustivel ON idTPCOMBUSTIVEL = combCARRO " +
                            "WHERE placaCARRO = '" + carro.getPlaca() + "' " +
                            "OR nomeCARRO = '" + carro.getNome() + "';";
                    List<Information> carro2 = Carro.consultaCarro(this, sql);
                    Carro[] itens = carro2.toArray(new Carro[0]);
                    if (itens.length > 0) {
                        Toast.makeText(getApplicationContext(), "Já existe um veículo com esta placa ou nome cadastrado", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Veículo incluido com sucesso", Toast.LENGTH_SHORT).show();
                        placaEditText.setText("");
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

                } else {
                    Carro carro = new Carro(codcarro, apelido, placa, 0, escolhaMarca.getCodigo(), escolhaModelo.getCodigo(), escolhaComb.getCodigo(), ativo);
                    String sql = "SELECT idCARRO,nomeCARRO, kmCARRO, placaCARRO, idMARCA, idMODELO, idTPCOMBUSTIVEL, ativo " +
                            "FROM tb_carro " +
                            "INNER JOIN tb_modelos ON idMODELO = modeloCARRO " +
                            "INNER JOIN tb_marcas ON marcaMODELO = idMARCA " +
                            "INNER JOIN tb_tipo_combustivel ON idTPCOMBUSTIVEL = combCARRO " +
                            "WHERE placaCARRO = '" + carro.getPlaca() + "'";
                    List<Information> carro2 = Carro.consultaCarro(this, sql);
                    Carro[] itens = carro2.toArray(new Carro[0]);
                    if (itens.length > 0 && itens[0].getCodigo() != carro.getCodigo()) {
                        Toast.makeText(getApplicationContext(), "Já existe um veículo com esta placa cadastrado", Toast.LENGTH_SHORT).show();
                    } else {
                        carro.alteraCarro(this);
                        Toast.makeText(getApplicationContext(), "Veículo alterado com sucesso", Toast.LENGTH_SHORT).show();
                        placaEditText.setText("");
                        apelidoEditText.setText("");
                        finish();
                    }
                }
            }
        } catch (Error e) {
            Toast.makeText(getApplicationContext(), "Erro ao gerenciar veículo", Toast.LENGTH_SHORT).show();
        }

    }

}
