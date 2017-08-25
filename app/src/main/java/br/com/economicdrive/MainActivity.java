package br.com.economicdrive;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.holder.StringHolder;
import com.mikepenz.materialdrawer.model.BaseDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileSettingDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;

import java.util.ArrayList;
import java.util.List;

import br.com.economicdrive.adapter.MyAdapter;
import br.com.economicdrive.communicator.Communicator;
import br.com.economicdrive.constantes.Constantes;
import br.com.economicdrive.fragment.AbastecimentoListFragment;
import br.com.economicdrive.fragment.DespesasListFragment;
import br.com.economicdrive.fragment.DialogFragmentMessage;
import br.com.economicdrive.fragment.FirstCarFragment;
import br.com.economicdrive.fragment.ManutencaoListFragment;
import br.com.economicdrive.model.Abastecimento;
import br.com.economicdrive.model.Carro;
import br.com.economicdrive.model.Despesas;
import br.com.economicdrive.model.Manutencao;
import br.com.economicdrive.model.Usuario;

public class MainActivity extends AppCompatActivity implements Communicator,
        Drawer.OnDrawerItemClickListener, AccountHeader.OnAccountHeaderListener {

    private DialogFragmentMessage fragmentDialog;
    private Carro veiculoEscolhido = null;
    private boolean vazio = false;
    private Toolbar toolbar;
    private static AccountHeader accountHeaderLeft;
    private Bundle savedInstance;
    private String[] optionList;
    private List<Information> veiculos;
    private Fragment fragment;
    private String fragmentSelected;
    private Bundle b;
    private static Drawer navigationDrawerLeft;
    private FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.myMainToolbar);
        optionList = getResources().getStringArray(R.array.optionsList);
        vazio = onCheckEmpty();
        onActionBar();
        OnNavigationDrawer();
        OnStartFragments();

    }

    private void OnStartFragments() {
        fragmentTransaction = getFragmentManager().beginTransaction();
        if (vazio) {
            fragment = new FirstCarFragment();
            fragmentSelected = "FirstCarFragment";
        } else {
            b = new Bundle();
            b.putParcelable("veiculo", veiculoEscolhido);
            fragment = new DespesasListFragment();
            fragment.setArguments(b);
            fragmentSelected = "Despesas";
        }
        fragmentTransaction.add(R.id.contentFrame, fragment, fragmentSelected);
        fragmentTransaction.commit();


    }

    @Override
    protected void onRestart() {
        super.onRestart();
        veiculoEscolhido = checkVeiculoAtivo();
        if(veiculoEscolhido != null)
            onChangeFragment();
        vazio = onCheckEmpty();

    }

    private void OnNavigationDrawer() {

        accountHeaderLeft = new AccountHeaderBuilder()
                .withActivity(this)
                .withCompactStyle(false)
                .withHeaderBackground(R.drawable.roadtrip_span)
                .withThreeSmallProfileImages(false)
                .withSavedInstance(savedInstance)
                .withOnAccountHeaderListener(this)
                .withAlternativeProfileHeaderSwitching(true)
                .build();

        LoadCarroAtivo(accountHeaderLeft);
        LoadCarros(accountHeaderLeft);

        accountHeaderLeft.addProfiles(
                new ProfileSettingDrawerItem().withName(Constantes.NOVO_VEICULO).withIcon(getResources().getDrawable(R.mipmap.ic_header_plus)).withIconTinted(true),
                new ProfileSettingDrawerItem().withName(Constantes.GERENCIAR_VEICULOS).withIcon(getResources().getDrawable(R.mipmap.ic_config)).withIconTinted(true)
        );
        navigationDrawerLeft = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withDisplayBelowStatusBar(true)
                .withActionBarDrawerToggleAnimated(true)
                .withDrawerGravity(Gravity.LEFT)
                .withOnDrawerItemClickListener(this)
                .withAccountHeader(accountHeaderLeft)
                .withSavedInstance(savedInstance)
                .build();

        navigationDrawerLeft.addItem(new SectionDrawerItem().withName(optionList[0]).setDivider(false));
        if (veiculoEscolhido == null){
            navigationDrawerLeft.addItem(new PrimaryDrawerItem().withName(optionList[1]).withIdentifier(1)
                    .withIcon(R.mipmap.ic_money).withIconTintingEnabled(true));
            navigationDrawerLeft.addItem(new PrimaryDrawerItem().withName(optionList[2]).withIdentifier(2)
                    .withIcon(R.mipmap.ic_gas_station).withIconTintingEnabled(true));
            navigationDrawerLeft.addItem(new PrimaryDrawerItem().withName(optionList[3]).withIdentifier(3)
                    .withIcon(R.mipmap.ic_build).withIconTintingEnabled(true));
        }
        else {
            navigationDrawerLeft.addItem(new PrimaryDrawerItem().withName(optionList[1]).withIdentifier(1)
                    .withIcon(R.mipmap.ic_money).withIconTintingEnabled(true).withBadge(Integer.toString(Despesas.ContaDespesas(this, veiculoEscolhido.getCodigo()))));
            navigationDrawerLeft.addItem(new PrimaryDrawerItem().withName(optionList[2]).withIdentifier(2)
                    .withIcon(R.mipmap.ic_gas_station).withIconTintingEnabled(true).withBadge(Integer.toString(Abastecimento.ContaAbastecimentos(this, veiculoEscolhido.getCodigo()))));
            navigationDrawerLeft.addItem(new PrimaryDrawerItem().withName(optionList[3]).withIdentifier(3)
                    .withIcon(R.mipmap.ic_build).withIconTintingEnabled(true).withBadge(Integer.toString(Manutencao.ContaManutencao(this, veiculoEscolhido.getCodigo()))));
        }
        navigationDrawerLeft.addItem(new PrimaryDrawerItem().withName(optionList[4])
                .withIcon(R.mipmap.ic_local).withIconTintingEnabled(true));
        navigationDrawerLeft.addItem(new SectionDrawerItem().withName(optionList[7]));
        navigationDrawerLeft.addItem(new PrimaryDrawerItem().withName(optionList[5])
                .withIcon(R.mipmap.ic_paste).withIconTintingEnabled(true));
        navigationDrawerLeft.addItem(new SectionDrawerItem().withName(optionList[8]));
        navigationDrawerLeft.addItem(new PrimaryDrawerItem().withName(optionList[6])
                .withIcon(R.mipmap.ic_lock).withIconTintingEnabled(true));
        navigationDrawerLeft.setSelection(1, false);
    }

    private void onActionBar() {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.rgb(3, 103, 221)));
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        savedInstance = savedInstanceState;
        super.onPostCreate(savedInstanceState);
    }

    private void LoadCarroAtivo(AccountHeader accountHeader) {
        veiculoEscolhido = checkVeiculoAtivo();
        if (veiculoEscolhido != null)
            accountHeader.addProfiles(
                    new ProfileDrawerItem().withName(veiculoEscolhido.getNome()).withIcon(getResources().getDrawable(R.mipmap.ic_car)).withSetSelected(true)
            );
    }

    public Carro checkVeiculoAtivo() {
        return Carro.consultaCarroAtivo(this);
    }

    @Override
    public void onBackPressed() {
        fragmentDialog = new DialogFragmentMessage("Sair", "Deseja sair?");
        fragmentDialog.show(getFragmentManager(), "Sair");
    }

    @Override
    public void onDialogMessage(String data) {
        if (data.equals("Sim"))
            finish();
        else if (data.equals("Não"))
            fragmentDialog.dismiss();
    }

    @Override
    public void OnDeleteMessage(List<Information> deleteList, MyAdapter myAdapter, String information) {
        int badgeValor = 0;
        int badge = 0;
        if (deleteList != null) {
            for (Information deletado : deleteList) {
                myAdapter.removeItem(deletado);
                switch (information) {
                    case Constantes.GERENCIAR_ABASTECIMENTO:
                        ((Abastecimento) deletado).deletaAbastecimento(this);
                        ((Abastecimento) deletado).AtualizakmRodado(this, ((Abastecimento) deletado).getIdCarro());
                        badgeValor = Abastecimento.ContaAbastecimentos(this, veiculoEscolhido.getCodigo());
                        badge = 2;
                        break;
                    case Constantes.GERENCIAR_DESPESAS:
                        ((Despesas) deletado).deletaDespesas(this);
                        badgeValor = Despesas.ContaDespesas(this, veiculoEscolhido.getCodigo());
                        badge = 1;
                        break;
                    case Constantes.GERENCIAR_MANUTENCAO:
                        ((Manutencao) deletado).deletaManutencao(this);
                        badgeValor = Manutencao.ContaManutencao(this,veiculoEscolhido.getCodigo());
                        badge = 3;
                        break;
                }
            }
            MainActivity.getNavigationDrawerLeft().updateBadge(badge,
                    new StringHolder(Integer.toString(badgeValor)));
            deleteList.clear();
            Toast.makeText(getApplicationContext(), "Itens deletados com sucesso",
                    Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), "Selecione pelo menos um item da lista para ser deletado",
                    Toast.LENGTH_SHORT).show();
        }
    }

    public void LoadCarros(AccountHeader accountHeader) {
        if (!vazio) {
            for (Information info : veiculos) {
                if (!((Carro) info).getNome().equals(veiculoEscolhido.getNome()))
                    accountHeader.addProfiles(
                            new ProfileDrawerItem().withName(((Carro) info).getNome()).withIcon(getResources().getDrawable(R.mipmap.ic_car)));
            }
        } else {
            accountHeader.addProfiles(
                    new ProfileDrawerItem().withName("Cadastre seu veículo").withIcon(getResources().getDrawable(R.mipmap.ic_car)));
        }
    }

    public boolean onCheckEmpty() {
        veiculos = Carro.consultaCarro(this, "SELECT idCARRO,nomeCARRO, kmCARRO, placaCARRO, idMARCA, idMODELO, idTPCOMBUSTIVEL, ativo " +
                "FROM tb_carro " +
                "INNER JOIN tb_modelos ON idMODELO = modeloCARRO " +
                "INNER JOIN tb_marcas ON marcaMODELO = idMARCA " +
                "INNER JOIN tb_tipo_combustivel ON idTPCOMBUSTIVEL = combCARRO ");

        return veiculos.size() == 0;
    }

    @Override
    public boolean onItemClick(View view, int position, IDrawerItem iDrawerItem) {
        fragment = null;
        b = new Bundle();
        fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentSelected = ((BaseDrawerItem) iDrawerItem).getName().toString();
        Intent i;
        switch (fragmentSelected) {
            case Constantes.GERENCIAR_LOCAIS:
                i = new Intent(this, LocalListActivity.class);
                i.putExtra("activity", "Main");
                startActivity(i);
                break;
            case Constantes.GERENCIAR_SENHA:
                Usuario usuario = Usuario.ConsultaUsuario(this);
                i = new Intent(this, UsuarioActivity.class);
                if (usuario == null) {
                    i.putExtra("acao", Constantes.INSERIR);
                } else {
                    i.putExtra("acao", Constantes.EDITAR);
                }
                startActivity(i);
                break;
            case Constantes.GERENCIAR_ABASTECIMENTO:
                if (!vazio) {
                    b.putParcelable("veiculo", veiculoEscolhido);
                    fragment = new AbastecimentoListFragment();
                    fragment.setArguments(b);
                } else {
                    Toast.makeText(getApplicationContext(), "Cadastre um veículo para acessar abastacimento", Toast.LENGTH_SHORT).show();
                }
                break;
            case Constantes.GERENCIAR_DESPESAS:
                if (!vazio) {
                    b.putParcelable("veiculo", veiculoEscolhido);
                    fragment = new DespesasListFragment();
                    fragment.setArguments(b);
                } else {
                    Toast.makeText(getApplicationContext(), "Cadastre um veículo para acessar despesas", Toast.LENGTH_SHORT).show();
                }
                break;
            case Constantes.GERAR_RELATORIO:
                if (!vazio) {
                    i = new Intent(this, RelatorioListActivity.class);
                    i.putExtra("veiculo", veiculoEscolhido.getCodigo());
                    i.putExtra("tpcomb", veiculoEscolhido.getComb());
                    startActivity(i);
                } else {
                    Toast.makeText(getApplicationContext(), "Cadastre um veículo para acessar relatório", Toast.LENGTH_SHORT).show();
                }
                break;
            case Constantes.GERENCIAR_MANUTENCAO:
                if (!vazio) {
                    b.putParcelable("veiculo", veiculoEscolhido);
                    fragment = new ManutencaoListFragment();
                    fragment.setArguments(b);
                } else {
                    Toast.makeText(getApplicationContext(), "Cadastre um veículo para acessar manutenção", Toast.LENGTH_SHORT).show();
                }
                break;
        }
        if (fragment != null)
            fragmentTransaction.replace(R.id.contentFrame, fragment, fragmentSelected).commit();
        return false;

    }

    @Override
    public boolean onProfileChanged(View view, IProfile iProfile, boolean b) {
        Intent i;
        if (iProfile != null)
            switch (iProfile.getName().toString()) {
                case Constantes.NOVO_VEICULO:
                    i = new Intent(this, CarroActivity.class);
                    i.putExtra("acao", Constantes.INSERIR);
                    startActivity(i);
                    break;
                case Constantes.GERENCIAR_VEICULOS:
                    i = new Intent(this, CarroListActivity.class);
                    startActivity(i);
                    break;
                default:
                    onSetCarroAtivo(iProfile.getName().toString());
                    onChangeFragment();
                    navigationDrawerLeft.updateBadge(1, new StringHolder(
                            Integer.toString(Despesas.ContaDespesas(this, veiculoEscolhido.getCodigo()))));
                    navigationDrawerLeft.updateBadge(2, new StringHolder(
                            Integer.toString(Abastecimento.ContaAbastecimentos(this, veiculoEscolhido.getCodigo()))));
                    navigationDrawerLeft.updateBadge(3, new StringHolder(
                            Integer.toString(Manutencao.ContaManutencao(this, veiculoEscolhido.getCodigo()))));

            }
        return false;
    }

    private void onChangeFragment() {
        b = new Bundle();
        fragmentTransaction = getFragmentManager().beginTransaction();
        int fragmentPosition = navigationDrawerLeft.getCurrentSelectedPosition() - 1;
        if (fragmentPosition > 0) {
            ArrayList<IDrawerItem> iDrawerItems = navigationDrawerLeft.getDrawerItems();
            IDrawerItem iDrawerItem = iDrawerItems.get(fragmentPosition);
            fragmentSelected = ((BaseDrawerItem) iDrawerItem).getName().toString();
        }
        switch (fragmentSelected) {
            case Constantes.GERENCIAR_MANUTENCAO:
                fragment = new ManutencaoListFragment();
                break;
            case Constantes.GERENCIAR_ABASTECIMENTO:
                fragment = new AbastecimentoListFragment();
                break;
            default:
                fragment = new DespesasListFragment();
                navigationDrawerLeft.setSelection(1,false);
        }
        b.putParcelable("veiculo", veiculoEscolhido);
        fragment.setArguments(b);
        fragmentTransaction.replace(R.id.contentFrame, fragment, fragmentSelected).commit();
    }

    private void onSetCarroAtivo(String nomeCarro) {
        List<Information> veiculos = Carro.consultaCarro(this, "SELECT idCARRO,nomeCARRO, kmCARRO, placaCARRO, idMARCA, idMODELO, idTPCOMBUSTIVEL, ativo " +
                "FROM tb_carro " +
                "INNER JOIN tb_modelos ON idMODELO = modeloCARRO " +
                "INNER JOIN tb_marcas ON marcaMODELO = idMARCA " +
                "INNER JOIN tb_tipo_combustivel ON idTPCOMBUSTIVEL = combCARRO " +
                "WHERE nomeCARRO = '" + nomeCarro + "';");
        if (!vazio) {
            veiculoEscolhido = (Carro) veiculos.get(0);
            veiculoEscolhido.alteraCarroParaAtivo(this);
        }

    }

    public static AccountHeader getAccountHeaderLeft() {
        return accountHeaderLeft;
    }

    public static Drawer getNavigationDrawerLeft(){ return navigationDrawerLeft; }
}