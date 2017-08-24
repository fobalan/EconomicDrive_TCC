package br.com.economicdrive;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;

import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;


public class DespesasListFragment extends Fragment implements
        Button.OnClickListener, OnListViewListener {

    private RecyclerView recyclerView;
    private MyAdapter myAdapter;
    private Carro veiculoEscolhido;
    private FloatingActionButton fabActionButton;
    protected DialogFragmentMessage fragmentDialog;
    private List<Information> despesasList;
    private List<Information> deleteList;
    private ImageButton deletarButton;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list, container, false);
    }
    @Override
    public void onActivityCreated( Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        recyclerView = (RecyclerView) getActivity().findViewById(R.id.myRecycleView);
        fabActionButton = (FloatingActionButton) getActivity().findViewById(R.id.fabActionButton);
        deletarButton = (ImageButton) getActivity().findViewById(R.id.deletarButton);
        fabActionButton.attachToRecyclerView(recyclerView);
        fabActionButton.setOnClickListener(this);
        fabActionButton.setColorNormal(getResources().getColor(R.color.colorAccent));
        fabActionButton.setColorPressed(getResources().getColor(R.color.colorRippleAccent));
        fabActionButton.setColorRipple(getResources().getColor(R.color.colorAccent));
        deletarButton.setOnClickListener(this);
        onCarSelected();
        onRecyclerView();
        customizeToolbar();
        onSetEditMode();
    }

    private void customizeToolbar() {
        getActivity().setTitle("Lista de Despesas");
    }

    private void onCarSelected() {
        veiculoEscolhido = getArguments().getParcelable("veiculo");
    }

    private void onRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        onLoadDespesas();

        myAdapter = new MyAdapter(getActivity(), despesasList, R.layout.four_info_list);
        myAdapter.setOnViewClickListener(this);

        recyclerView.setAdapter(myAdapter);

    }

    private void onLoadDespesas() {
        despesasList = Despesas.ConsultaDespesas(getActivity(), "SELECT * "
                + "FROM TB_DESPESAS "
                + "WHERE idCarro = "
                + veiculoEscolhido.getCodigo());
    }

    @Override
    public void onResume() {
        super.onResume();
        onLoadDespesas();
        myAdapter.setList(despesasList);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fabActionButton:
                if (deleteModeON()) {
                    fragmentDialog = new DialogFragmentMessage("Deletar", "Deseja deletar os registros?", deleteList, myAdapter, Constantes.GERENCIAR_DESPESAS);
                    fragmentDialog.show(getFragmentManager(), "Deletar");
                } else {
                    Intent i = new Intent(getActivity(), DespesasActivity.class);
                    i.putExtra("acao", Constantes.INSERIR);
                    i.putExtra("veiculo", veiculoEscolhido);
                    startActivity(i);
                }
                break;
            case R.id.deletarButton:
                if (deleteModeON()) {
                    onSetEditMode();
                } else {
                    onSetDeleteMode();
                    break;
                }
        }
    }

    private void onSetDeleteMode() {
        myAdapter.setDeletable(true);
        deletarButton.setImageResource(R.mipmap.ic_cancel);
        fabActionButton.setImageResource(R.mipmap.ic_trash);
        myAdapter.notifyDataSetChanged();
    }

    private void onSetEditMode() {
        myAdapter.setDeletable(false);
        deletarButton.setImageResource(R.mipmap.ic_trash);
        fabActionButton.setImageResource(R.mipmap.ic_plus);
        myAdapter.notifyDataSetChanged();
    }

    private boolean deleteModeON() {
        return myAdapter.isDeletable();
    }

    @Override
    public void onViewClick(View v, int position) {
        Intent chamada = new Intent(getActivity(), DespesasActivity.class);
        switch (v.getId()) {
            case R.id.editImageButton:
                chamada.putExtra("acao", Constantes.EDITAR);
                break;
            default:
                chamada.putExtra("acao", Constantes.VISUALIZAR);
        }
        if (!myAdapter.isDeletable()) {
            Despesas despesas = (Despesas) myAdapter.getItem(position);
            chamada.putExtra("parcel", despesas);
            startActivity(chamada);
        }
    }

    @Override
    public void onCheckedClick(CompoundButton buttonView, boolean isChecked, int position) {
        Despesas despesa = (Despesas) myAdapter.getItem(position);
        if (deleteList == null) {
            deleteList = new ArrayList<>();
        }
        if (isChecked) {
            deleteList.add(despesa);
        } else {
            deleteList.remove(despesa);
        }
    }

}


