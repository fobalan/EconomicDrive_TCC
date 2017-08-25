package br.com.economicdrive;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Toast;

import com.melnykov.fab.FloatingActionButton;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;

import java.util.ArrayList;
import java.util.List;

import br.com.economicdrive.adapter.MyAdapter;
import br.com.economicdrive.communicator.Communicator;
import br.com.economicdrive.constantes.Constantes;
import br.com.economicdrive.fragment.DialogFragmentMessage;
import br.com.economicdrive.listener.OnListViewListener;
import br.com.economicdrive.model.Abastecimento;
import br.com.economicdrive.model.Carro;
import br.com.economicdrive.model.Despesas;
import br.com.economicdrive.model.Manutencao;

public class CarroListActivity extends AppCompatActivity implements Communicator,
        Button.OnClickListener, OnListViewListener {

    private RecyclerView recyclerView;
    private MyAdapter myAdapter;
    private LinearLayoutManager layoutManager;
    private ImageButton deletarCarroButton;
    private FloatingActionButton fabActionButton;
    private Carro carro;
    private ActionBar actionBar;
    private Toolbar toolbar;
    private List<Information> carros;
    private List<Information> deleteList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carro_list);
        recyclerView = (RecyclerView) findViewById(R.id.myCarroRecycleView);
        toolbar = (Toolbar) findViewById(R.id.myCarroToolbar);
        deletarCarroButton = (ImageButton) findViewById(R.id.deletarCarroButton);
        fabActionButton = (FloatingActionButton) findViewById(R.id.fabActionButton);
        fabActionButton.attachToRecyclerView(recyclerView);
        fabActionButton.setOnClickListener(this);
        deletarCarroButton.setOnClickListener(this);
        onActionBar();
        //configura a action bar
        onRecyclerView();
    }

    private void onRecyclerView() {
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        onLoadCarros();

        myAdapter = new MyAdapter(this, carros, R.layout.two_info_list);
        myAdapter.setOnViewClickListener(this);

        recyclerView.setAdapter(myAdapter);

    }

    private void onLoadCarros() {
        carros = Carro.consultaCarro(this, "SELECT idCARRO,nomeCARRO, kmCARRO, placaCARRO, idMARCA, idMODELO, idTPCOMBUSTIVEL, ativo " +
                "FROM tb_carro " +
                "INNER JOIN tb_modelos ON idMODELO = modeloCARRO " +
                "INNER JOIN tb_marcas ON marcaMODELO = idMARCA " +
                "INNER JOIN tb_tipo_combustivel ON idTPCOMBUSTIVEL = combCARRO");
    }

    private void onActionBar() {
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

    private boolean onCheckDBData(Carro carro) {
        List<Information> abastecimentoList = Abastecimento.ConsultaAbastecimentos(this, "SELECT * FROM TB_ABASTECIMENTO WHERE idCarro = '" + carro.getCodigo() + "'");
        Abastecimento[] itens = abastecimentoList.toArray(new Abastecimento[0]);
        if (itens.length > 0) {
            Toast.makeText(this, "Não é possivel deletar este veículo, pois existem abastecimentos vinculados",
                    Toast.LENGTH_SHORT).show();
            return false;
        } else {
            List<Information> despesasList = Despesas.ConsultaDespesas(this, "SELECT * FROM TB_DESPESAS WHERE idCarro = '" + carro.getCodigo() + "'");
            Despesas[] itens2 = despesasList.toArray(new Despesas[0]);
            if (itens2.length > 0) {
                Toast.makeText(this, "Não é possivel deletar este veículo, pois existem despesas vinculadas",
                        Toast.LENGTH_SHORT).show();
                return false;
            } else {
                List<Information> manutencaoList = Manutencao.ConsultaManutencao(this, "SELECT * FROM TB_MANUTENCAO WHERE idCarro = '" + carro.getCodigo() + "'");
                Manutencao[] itens3 = manutencaoList.toArray(new Manutencao[0]);
                if (itens3.length > 0) {
                    Toast.makeText(this, "Não é possivel deletar este veículo, pois existem manutenções vinculadas",
                            Toast.LENGTH_SHORT).show();
                    return false;
                } else {
                    return true;
                }
            }
        }
    }

    @Override
    public void onDialogMessage(String data) {
    }

    @Override
    public void OnDeleteMessage(List<Information> deleteList, MyAdapter myAdapter, String information) {
        if (deleteList != null) {
            for (Information deletado : deleteList) {
                myAdapter.removeItem(deletado);
                ((Carro) deletado).deletaCarro(this);
                OnDeleteProfile((Carro) deletado);
            }
            if(Carro.consultaCarroAtivo(this) == null){
                onLoadCarros();
                if(carros != null){
                    Carro carroEscolhido = (Carro)carros.get(0);
                    carroEscolhido.alteraCarroParaAtivo(this);
                    for(IProfile iprofile : MainActivity.getAccountHeaderLeft().getProfiles()){
                        if(carroEscolhido.getNome().equals(iprofile.getName().toString())){
                            MainActivity.getAccountHeaderLeft().setActiveProfile(iprofile);
                        }
                    }
                }
            }
            deleteList.clear();
            Toast.makeText(getApplicationContext(), "Itens deletados com sucesso",
                    Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), "Selecione pelo menos um item da lista para ser deletado",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void OnDeleteProfile(Carro deletado) {
        int cont = 0;
        AccountHeader accountHeaderLeft = MainActivity.getAccountHeaderLeft();
        ArrayList<IProfile> profiles = accountHeaderLeft.getProfiles();
        for (IProfile profile : profiles) {
            if (profile.getName().toString().equals(deletado.getNome())) {
                accountHeaderLeft.removeProfile(cont);
                break;
            }
            cont++;
        }

    }

    private boolean deleteModeON() {
        return myAdapter.isDeletable();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fabActionButton:
                DialogFragmentMessage fragmentDialog = new DialogFragmentMessage("Deletar", "Deseja deletar os registros?", deleteList, myAdapter, Constantes.GERENCIAR_VEICULOS);
                fragmentDialog.show(getFragmentManager(), "Deletar");
                break;
            case R.id.deletarCarroButton:
                if (deleteModeON()) {
                    myAdapter.setDeletable(false);
                    fabActionButton.setVisibility(View.INVISIBLE);
                } else {
                    myAdapter.setDeletable(true);
                    deletarCarroButton.setImageResource(R.mipmap.ic_cancel);
                    fabActionButton.setVisibility(View.VISIBLE);
                }
                myAdapter.notifyDataSetChanged();
                break;
        }
    }

    @Override
    public void onViewClick(View v, int position) {
        Intent chamada = new Intent(this, CarroActivity.class);
        switch (v.getId()) {
            case R.id.editImageButton:
                chamada.putExtra("acao", Constantes.EDITAR);
                break;
            default:
                chamada.putExtra("acao", Constantes.VISUALIZAR);
        }
        if (!myAdapter.isDeletable()) {
            carro = (Carro) myAdapter.getItem(position);
            chamada.putExtra("parcel", carro);
            startActivity(chamada);
        }
    }

    @Override
    public void onCheckedClick(CompoundButton buttonView, boolean isChecked, int position) {
        Carro carro = (Carro) myAdapter.getItem(position);
        if (deleteList == null) {
            deleteList = new ArrayList<>();
        }
        if (isChecked) {
            if (onCheckDBData(carro)) {
                deleteList.add(carro);
            } else {
                buttonView.setVisibility(View.INVISIBLE);
            }
        } else {
            deleteList.remove(carro);
        }
    }
}
