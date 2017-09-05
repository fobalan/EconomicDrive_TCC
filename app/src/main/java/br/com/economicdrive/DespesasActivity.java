package br.com.economicdrive;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.DialogFragment;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;

import com.mikepenz.materialdrawer.holder.StringHolder;
import com.rengwuxian.materialedittext.MaterialEditText;

import br.com.economicdrive.constantes.Constantes;
import br.com.economicdrive.fragment.DatePickerFragment;
import br.com.economicdrive.model.Carro;
import br.com.economicdrive.model.Despesas;
import br.com.economicdrive.model.Local;

@SuppressLint("SimpleDateFormat")
public class DespesasActivity extends AppCompatActivity implements OnClickListener,
        TextWatcher {
    private MaterialEditText descricaoMaterialEditText;
    private MaterialEditText valorMaterialEditText;
    private MaterialEditText localMaterialEditText;
    private MaterialEditText dataMaterialEditText;
    private Despesas despesa;
    private Intent i;
    private ActionBar actionBar;
    private Toolbar toolbar;
    private Local local;
    private Carro veiculoEscolhido;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_despesas);

        //Toolbar
        toolbar = (Toolbar) findViewById(R.id.despesaToolbar);

        //Material EditText
        descricaoMaterialEditText = (MaterialEditText) this.findViewById(R.id.descricaoDespesaMaterialEditText);
        valorMaterialEditText = (MaterialEditText) this.findViewById(R.id.valorDespesaMaterialEditText);
        localMaterialEditText = (MaterialEditText) this.findViewById(R.id.localDespesaMaterialEditText);
        dataMaterialEditText = (MaterialEditText) this.findViewById(R.id.dataDespesaMaterialEditText);

        dataMaterialEditText.setOnClickListener(this);
        localMaterialEditText.setOnClickListener(this);
        applyCustomizingActionBar();
        switch (getIntent().getIntExtra("acao", 0)) {
            case Constantes.EDITAR:
                onStartVariables();
                break;
            case Constantes.INSERIR:
                despesa = new Despesas(this);
                veiculoEscolhido = getIntent().getParcelableExtra("veiculo");
                despesa.setIdCarro(veiculoEscolhido.getCodigo());
                dataMaterialEditText.setText(getDateTime());
                break;
            case Constantes.VISUALIZAR:
                onStartVariables();
                onDisableInputVariables();
                break;
        }
        onSetTextListeners();
        onSetTextListeners();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dataDespesaMaterialEditText:
                showDatePickerDialog(v);
                break;
            case R.id.localDespesaMaterialEditText:
                i = new Intent(this, LocalListActivity.class);
                i.putExtra("activity", "Despesas");
                startActivityForResult(i, Constantes.DESPESAS_CODE);
                break;
        }

    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment(0);
        newFragment.show(getFragmentManager(), "datePicker");
    }

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
        if (checkFields()) {
            despesa.setDescricaoDespesa(descricaoMaterialEditText.getEditableText().toString());
            despesa.setValorGasto(Float.parseFloat(valorMaterialEditText.getEditableText().toString().replace("R$", "").replace(".", "").replace(",", ".")));
            despesa.setLocalGasto(local.getCodigo());
            despesa.setDataGasto(dataMaterialEditText.getText().toString());
            if (getIntent().getIntExtra("acao", 0) == Constantes.EDITAR) {
                despesa.alteraDespesas(this);
            } else if (getIntent().getIntExtra("acao", 0) != Constantes.VISUALIZAR){
                MainActivity.getNavigationDrawerLeft().updateBadge(1,
                        new StringHolder(Integer.toString(Despesas.ContaDespesas(this, veiculoEscolhido.getCodigo()))));
            }
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constantes.DESPESAS_CODE)
            if (resultCode == Constantes.LOCAL_LIST) {
                local = data.getParcelableExtra("parcel");
                localMaterialEditText.setText(local.getNome());
            }

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count,
                                  int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        onRemoveTextListeners();
        String valorMask = null;
        try {
            float valorAnterior = Float.parseFloat(s.toString()
                    .replace("R$", "").replace(".", "").replaceAll(",", "")
                    .replaceFirst(" ", ""));

            valorMask = NumberFormat.getCurrencyInstance().format(valorAnterior / 100);
            valorMaterialEditText.setText(valorMask);
            valorMaterialEditText.setSelection(valorMask.length());
            onSetTextListeners();
        } catch (NumberFormatException e) {
        } catch (ArithmeticException e) {
        }
    }

    private boolean checkFields() {
        if (descricaoMaterialEditText.getEditableText().toString().equals("")) {
            descricaoMaterialEditText.setError("Campo Obrigatório");
            descricaoMaterialEditText.requestFocus();
        } else if (valorMaterialEditText.getEditableText().toString().equals("") || valorMaterialEditText.getEditableText().toString().equals("R$0,00")) {
            valorMaterialEditText.setError("Campo Obrigatório");
            valorMaterialEditText .requestFocus();
        } else if (local == null) {
            localMaterialEditText.setError("Campo Obrigatório");
            localMaterialEditText.requestFocus();
        } else {
            return true;
        }
        return false;
    }

    private void onRemoveTextListeners() {
        valorMaterialEditText.removeTextChangedListener(this);
    }

    private void onSetTextListeners() {
        valorMaterialEditText.addTextChangedListener(this);
    }

    private void applyCustomizingActionBar() {
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setBackgroundDrawable(new ColorDrawable(Color
                .rgb(3, 103, 221)));
    }

    private String getDateTime() {
        Date date = new Date();
        return new SimpleDateFormat("dd/MM/yyyy").format(date);
    }

    private void onStartVariables() {
        despesa = getIntent().getParcelableExtra("parcel");
        descricaoMaterialEditText.setText(despesa.getDescricaoDespesa());
        valorMaterialEditText.setText(NumberFormat.getCurrencyInstance().format(despesa.getValorGasto()));
        local = Despesas.ConsultaLocal(this, "SELECT *"
                + " FROM tb_local"
                + " WHERE codigoLOCAL = " + despesa.getLocalGasto());
        localMaterialEditText.setText(local.getNome());
        dataMaterialEditText.setText(despesa.tratadata());
    }

    private void onDisableInputVariables() {
        descricaoMaterialEditText.setEnabled(false);
        valorMaterialEditText.setEnabled(false);
        localMaterialEditText.setEnabled(false);
        dataMaterialEditText.setEnabled(false);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;

    }

}
