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

import java.util.ArrayList;
import java.util.List;

import br.com.economicdrive.adapter.MyAdapter;
import br.com.economicdrive.communicator.Communicator;
import br.com.economicdrive.constantes.Constantes;
import br.com.economicdrive.fragment.DialogFragmentMessage;
import br.com.economicdrive.listener.OnListViewListener;
import br.com.economicdrive.model.Abastecimento;
import br.com.economicdrive.model.Despesas;
import br.com.economicdrive.model.Local;
import br.com.economicdrive.model.Manutencao;

public class LocalListActivity extends AppCompatActivity implements Communicator,
        Button.OnClickListener, OnListViewListener {

    private RecyclerView recyclerView;
    private MyAdapter myAdapter;
    private FloatingActionButton fabActionButton;
    private Toolbar toolbar;
    private DialogFragmentMessage fragmentDialog;
    private List<Information> locais;
    private List<Information> deleteList;
    private ImageButton deletarLocalButton;

    @Override
    protected void onRestart() {
        super.onRestart();
        onLoadLocais();
        myAdapter.setList(locais);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_list);
        recyclerView = (RecyclerView) findViewById(R.id.myLocalRecycleView);
        fabActionButton = (FloatingActionButton) findViewById(R.id.fabActionButton);
        toolbar = (Toolbar) findViewById(R.id.myLocalToolbar);
        deletarLocalButton = (ImageButton) findViewById(R.id.deletarLocalButton);
        fabActionButton.attachToRecyclerView(recyclerView);
        fabActionButton.setOnClickListener(this);
        deletarLocalButton.setOnClickListener(this);
        onActionBar();
        onRecyclerView();
    }

    private void onRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        onLoadLocais();

        myAdapter = new MyAdapter(this, locais, R.layout.two_info_list);
        myAdapter.setOnViewClickListener(this);

        recyclerView.setAdapter(myAdapter);

    }

    private void onActionBar() {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
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

    public void onLoadLocais() {
        locais = Local.ConsultaLocais(this, "SELECT * FROM tb_local");
    }

    @Override
    public void onDialogMessage(String data) {
        if (data.equals("Sim")) {
            onDeleteItems();
        } else if (data.equals("Não"))
            fragmentDialog.dismiss();
    }

    @Override
    public void OnDeleteMessage(List<Information> deleteList, MyAdapter myAdapter, String information) {

    }

    private boolean onCheckDBData(Local local) {
        //VALIDA SE EXISTE ALGUM REGISTRO VINCULADO AO LOCAL
        List<Information> abastecimentoList = Abastecimento.ConsultaAbastecimentos(this, "SELECT * FROM TB_ABASTECIMENTO WHERE idlocal = '" + local.getCodigo() + "'");
        Abastecimento[] itens = abastecimentoList.toArray(new Abastecimento[0]);
        if (itens.length > 0) {
            Toast.makeText(getApplicationContext(), "Não é possivel deletar este local, pois existem abastecimentos vinculados",
                    Toast.LENGTH_SHORT).show();
            return false;
        } else {
            List<Information> despesasList = Despesas.ConsultaDespesas(this, "SELECT * FROM TB_DESPESAS WHERE localGasto = '" + local.getCodigo() + "'");
            Despesas[] itens2 = despesasList.toArray(new Despesas[0]);
            if (itens2.length > 0) {
                Toast.makeText(getApplicationContext(), "Não é possivel deletar este local, pois existem despesas vinculadas",
                        Toast.LENGTH_SHORT).show();
                return false;
            } else {
                List<Information> manutencaoList = Manutencao.ConsultaManutencao(this, "SELECT * FROM TB_MANUTENCAO WHERE localGasto = '" + local.getCodigo() + "'");
                Manutencao[] itens3 = manutencaoList.toArray(new Manutencao[0]);
                if (itens3.length > 0) {
                    Toast.makeText(getApplicationContext(), "Não é possivel deletar este local, pois existem manutenções vinculadas",
                            Toast.LENGTH_SHORT).show();
                    return false;
                } else {
                    return true;
                }
            }
        }
    }

    private boolean deleteModeON() {
        return myAdapter.isDeletable();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fabActionButton:
                if (deleteModeON()) {
                    fragmentDialog = new DialogFragmentMessage("Deletar", "Deseja deletar os registros?");
                    fragmentDialog.show(getFragmentManager(), "Deletar");
                } else {
                    Intent i = new Intent(this, LocalActivity.class);
                    i.putExtra("acao", Constantes.INSERIR);
                    startActivity(i);
                }
                break;
            case R.id.deletarLocalButton:
                if (deleteModeON()) {
                    myAdapter.setDeletable(false);
                    deletarLocalButton.setImageResource(R.mipmap.ic_trash);
                    fabActionButton.setImageResource(R.mipmap.ic_plus);
                } else {
                    myAdapter.setDeletable(true);
                    deletarLocalButton.setImageResource(R.mipmap.ic_cancel);
                    fabActionButton.setImageResource(R.mipmap.ic_trash);
                }
                myAdapter.notifyDataSetChanged();
                break;
        }
    }

    private void onDeleteItems() {
        if (deleteList != null) {
            for (Information deletado : deleteList) {
                myAdapter.removeItem(deletado);
                ((Local) deletado).deletaLocal(this);
            }
            deleteList.clear();
            Toast.makeText(getApplicationContext(), "Locais deletados com sucesso",
                    Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), "Selecione pelo menos 1 item da lista para ser deletado",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onViewClick(View v, int position) {
        String activity = this.getIntent().getStringExtra("activity");
        Local local = (Local) myAdapter.getItem(position);
        Intent chamada = new Intent(this, LocalActivity.class).putExtra("parcel", local);
        switch (v.getId()) {
            case R.id.editImageButton:
                chamada.putExtra("acao", Constantes.EDITAR);
                startActivity(chamada);
                break;
            default:
                if (!myAdapter.isDeletable()) {
                    if (activity.equals("Abastecimento") || activity.equals("Despesas") || activity.equals("Manutencao")) {
                        setResult(Constantes.LOCAL_LIST, chamada);
                        finish();
                    } else {
                        chamada.putExtra("acao", Constantes.VISUALIZAR);
                        startActivity(chamada);
                    }
                }
        }
    }

    @Override
    public void onCheckedClick(CompoundButton buttonView, boolean isChecked, int position) {
        Local local = (Local) myAdapter.getItem(position);
        if (deleteList == null) {
            deleteList = new ArrayList<>();
        }
        if (isChecked) {
            if (onCheckDBData(local)) {
                deleteList.add(local);
            } else {
                buttonView.setVisibility(View.INVISIBLE);
            }
        } else {
            deleteList.remove(local);
        }
    }
}
