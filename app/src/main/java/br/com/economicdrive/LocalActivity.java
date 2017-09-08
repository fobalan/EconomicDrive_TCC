package br.com.economicdrive;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.rengwuxian.materialedittext.MaterialEditText;

import br.com.economicdrive.constantes.Constantes;
import br.com.economicdrive.model.Local;

public class LocalActivity extends AppCompatActivity {

    private MaterialEditText nomeMaterialEditText;
    private MaterialEditText enderecoMaterialEditText;
    private int intCodigo;
    private Toolbar toolbar;
    private Local local;
    int intAcao = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local);

        //Toolbar
        toolbar = (Toolbar) findViewById(R.id.localToolbar);

        //MaterialEditText
        nomeMaterialEditText = (MaterialEditText) findViewById(R.id.nomeLocalMaterialEditText);
        enderecoMaterialEditText = (MaterialEditText) findViewById(R.id.enderecoLocalMaterialEditText);

        onActionBar();
        intAcao = getIntent().getIntExtra("acao", 0);
        local = getIntent().getParcelableExtra("parcel");
        if (intAcao == Constantes.EDITAR) {
            nomeMaterialEditText.setText(local.getNome());
            enderecoMaterialEditText.setText(local.getEndereco());
            intCodigo = local.getCodigo();
        } else if (intAcao == Constantes.VISUALIZAR) {
            nomeMaterialEditText.setText(local.getNome());
            enderecoMaterialEditText.setText(local.getEndereco());
            intCodigo = local.getCodigo();
            OnDisableVariables();
        }
    }

    private void onActionBar() {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.rgb(3, 103, 221)));
    }

    private void OnDisableVariables() {
        nomeMaterialEditText.setEnabled(false);
        enderecoMaterialEditText.setEnabled(false);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.save_menu:
                onClickSave();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void onClickSave() {
        try {
            //Variavel que receber qual acao esta sendo executada, edicao ou inclusao
            String strLocal = nomeMaterialEditText.getEditableText().toString();
            String strEnde = enderecoMaterialEditText.getEditableText().toString();
            if (strLocal.equals("")) {
                nomeMaterialEditText.setError("Campo Obrigatório");
                nomeMaterialEditText.requestFocus();
            } else if (strEnde.equals("")) {
                enderecoMaterialEditText.setError("Campo Obrigatório");
                enderecoMaterialEditText.requestFocus();
            } else {
                List<Information> local2;
                Local[] itens;
                switch (intAcao) {
                    case Constantes.INSERIR:
                        local = new Local(strEnde, strLocal);
                        local2 = Local.ConsultaLocais(this, "SELECT * FROM tb_local WHERE enderecoLOCAL = '" + local.getEndereco() + "'");
                        itens = local2.toArray(new Local[0]);
                        if (itens.length > 0) {
                            Toast.makeText(getApplicationContext(), "Já existe um local cadastrado com esse endereço", Toast.LENGTH_SHORT).show();
                        } else {
                            local.insereLocal(this);
                            Toast.makeText(getApplicationContext(), "Local cadastrado com sucesso", Toast.LENGTH_SHORT).show();
                            nomeMaterialEditText.setText("");
                            enderecoMaterialEditText.setText("");
                            finish();
                        }
                        break;
                    case Constantes.EDITAR:
                        local = new Local(intCodigo, strEnde, strLocal);
                        local2 = Local.ConsultaLocais(this, "SELECT * FROM tb_local WHERE enderecoLOCAL = '" + local.getEndereco() + "'");
                        itens = local2.toArray(new Local[0]);
                        if (itens.length > 0 && itens[0].getCodigo() != local.getCodigo()) {
                            Toast.makeText(getApplicationContext(), "Já existe um local cadastrado com esse endereço", Toast.LENGTH_SHORT).show();
                        } else {
                            local.alteraLocal(this);
                            Toast.makeText(getApplicationContext(), "Local alterado com sucesso", Toast.LENGTH_SHORT).show();
                            nomeMaterialEditText.setText("");
                            enderecoMaterialEditText.setText("");
                            finish();
                        }
                        break;
                    case Constantes.VISUALIZAR:
                        break;
                    default:
                        Toast.makeText(getApplicationContext(), "Operação inválida", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        } catch (Error e) {
            Toast.makeText(getApplicationContext(), "Erro ao gerenciar local", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }
}
