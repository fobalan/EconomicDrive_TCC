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

public class AbastecimentoListFragment extends Fragment implements Button.OnClickListener,
        OnListViewListener {

    private RecyclerView recyclerView;
    private MyAdapter myAdapter;
    private FloatingActionButton fabActionButton;
    private Carro veiculoEscolhido;
    private Intent i;
    private List<Information> abastecimentoList;
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
        fabActionButton.setColorNormal(getResources().getColor(R.color.colorDeepPurple));
        fabActionButton.setColorPressed(getResources().getColor(R.color.colorRipplePurple));
        fabActionButton.setColorRipple(getResources().getColor(R.color.colorDeepPurple));
        deletarButton.setOnClickListener(this);
        customizeToolBar();
        onCarSelected();
        onRecyclerView();
        onSetEditMode();
    }

    @Override
    public void onResume() {
        super.onResume();
        onLoadAbastecimento();
        myAdapter.setList(abastecimentoList);
    }

    private void onRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        onLoadAbastecimento();

        myAdapter = new MyAdapter(getActivity(), abastecimentoList, R.layout.three_info_list);
        myAdapter.setOnViewClickListener(this);

        recyclerView.setAdapter(myAdapter);

    }

    private void onLoadAbastecimento() {
        abastecimentoList = Abastecimento.ConsultaAbastecimentos(getActivity(), "SELECT * "
                + "FROM TB_ABASTECIMENTO "
                + "WHERE idCarro = "
                + veiculoEscolhido.getCodigo());
    }

    private void onCarSelected() {
        veiculoEscolhido = getArguments().getParcelable("veiculo");

    }

    private void customizeToolBar() {
        getActivity().setTitle("Lista de Abastecimento");

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fabActionButton:
                if (deleteModeON()) {
                    DialogFragmentMessage fragmentDialog = new DialogFragmentMessage("Deletar", "Deseja deletar os registros?", deleteList, myAdapter, Constantes.GERENCIAR_ABASTECIMENTO);
                    fragmentDialog.show(getActivity().getFragmentManager(), "Deletar");
                } else {
                    i = new Intent(getActivity(), AbastecimentoActivity.class);
                    i.putExtra("acao", Constantes.INSERIR);
                    i.putExtra("veiculo", veiculoEscolhido);
                    startActivityForResult(i, Constantes.ABASTECIMENTO_CODE);
                }
                break;

            case R.id.deletarButton:
                if (deleteModeON()) {
                    onSetEditMode();
                } else {
                    onSetDeleteMode();
                }
                break;
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

    @Override
    public void onViewClick(View v, int position) {
        i = new Intent(getActivity(), AbastecimentoActivity.class);
        switch (v.getId()) {
            case R.id.editImageButton:
                i.putExtra("acao", Constantes.EDITAR);
                break;
            default:
                i.putExtra("acao", Constantes.VISUALIZAR);
        }
        if (!myAdapter.isDeletable()) {
            i.putExtra("parcel", (Abastecimento) myAdapter.getItem(position));
            i.putExtra("veiculo", veiculoEscolhido);
            startActivity(i);
        }
    }

    public boolean deleteModeON() {
        return myAdapter.isDeletable();
    }

    @Override
    public void onCheckedClick(CompoundButton buttonView, boolean isChecked, int position) {
        Abastecimento abastecimento = (Abastecimento) myAdapter.getItem(position);
        if (deleteList == null) {
            deleteList = new ArrayList<>();
        }
        if (isChecked) {
            deleteList.add(abastecimento);
        } else {
            deleteList.remove(abastecimento);
        }
    }

}
