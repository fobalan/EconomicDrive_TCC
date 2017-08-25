package br.com.economicdrive.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import com.melnykov.fab.FloatingActionButton;

import br.com.economicdrive.CarroActivity;
import br.com.economicdrive.Constantes;
import br.com.economicdrive.MainActivity;
import br.com.economicdrive.R;
import br.com.economicdrive.fragment.DespesasListFragment;

public class FirstCarFragment extends Fragment implements Button.OnClickListener{
    private FloatingActionButton fabActionButton;
    private Intent i;
    private ImageButton deletarButton;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.first_car_fragment, container, false);
    }

    @Override
    public void onActivityCreated( Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        deletarButton = (ImageButton) getActivity().findViewById(R.id.deletarButton);
        fabActionButton = (FloatingActionButton) getActivity().findViewById(R.id.fabActionButton);
        fabActionButton.setOnClickListener(this);
        deletarButton.setVisibility(View.INVISIBLE);
        customizeToolBar();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(!((MainActivity)getActivity()).onCheckEmpty()) {
            Fragment fragment = new DespesasListFragment();
            Bundle b = new Bundle();
            b.putParcelable("veiculo", ((MainActivity) getActivity()).checkVeiculoAtivo());
            fragment.setArguments(b);
            getActivity().getFragmentManager().beginTransaction().replace(R.id.contentFrame,
                    fragment, "DespesasListFragment")
                    .commit();
        }
    }


    private void customizeToolBar() {
        getActivity().setTitle("Primeiro Carro!");

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fabActionButton:
                i = new Intent (getActivity(), CarroActivity.class);
                i.putExtra("acao", Constantes.INSERIR);
                startActivity(i);
                break;
        }
    }
}
