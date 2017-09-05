package br.com.economicdrive;

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
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.mikepenz.materialdrawer.holder.StringHolder;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import br.com.economicdrive.constantes.Constantes;
import br.com.economicdrive.fragment.DatePickerFragment;
import br.com.economicdrive.model.Abastecimento;
import br.com.economicdrive.model.Carro;
import br.com.economicdrive.model.Combustivel;
import br.com.economicdrive.model.Local;

@SuppressLint("SimpleDateFormat")
public class AbastecimentoActivity extends AppCompatActivity implements OnClickListener,
		TextWatcher {
    private Toolbar abastecimentoToolbar;
    private MaterialEditText dataMaterialEditText;
    private TextView quantidadeLitrosTextView;
    private MaterialEditText localMaterialEditText;
    private MaterialBetterSpinner combustivelBetterSpinner;
    private MaterialEditText valorLitroMaterialEditText;
    private MaterialEditText valorGastoMaterialEditText;
    private MaterialEditText hodometroMaterialEditText;
    private CheckBox tanqueCheioCheckBox;
    private Abastecimento abastecimento;
    private Local local;
    private float valorGasto;
    private float valorLitro;
    private NumberFormat dinheiroGastoFormat;
    private NumberFormat dinheiroLitroFormat;
    private ArrayAdapter<Combustivel> adapterCombustivel;
    private Combustivel escolhaCombustivel;
    private Combustivel[] itens;
    private Carro veiculoEscolhido;
    private int acao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_abastecimento);

        acao = getIntent().getIntExtra("acao", 0);
        //Toolbar
        abastecimentoToolbar = (Toolbar) findViewById(R.id.abastecimentoToolbar);

        //MaterialEditTexts
        dataMaterialEditText = (MaterialEditText) findViewById(R.id.dataAbastecimentoMaterialEditText);
        valorLitroMaterialEditText = (MaterialEditText) findViewById(R.id.valorLitroMaterialEditText);
        valorGastoMaterialEditText = (MaterialEditText) findViewById(R.id.valorGastoMaterialEditText);
        hodometroMaterialEditText = (MaterialEditText) findViewById(R.id.hodometroMaterialEditText);
        localMaterialEditText = (MaterialEditText) findViewById(R.id.localAbastecimentoMaterialEditText);

        //Material Better Spinner
        combustivelBetterSpinner = (MaterialBetterSpinner) findViewById(R.id.combustivelBetterSpinner);

        //TextViews
        quantidadeLitrosTextView = (TextView) findViewById(R.id.quantidadeLitrosTextView);

        //CheckBoxs
        tanqueCheioCheckBox = (CheckBox) findViewById(R.id.tanqueCheioCheckBox);

        dataMaterialEditText.setOnClickListener(this);
        localMaterialEditText.setOnClickListener(this);

        applyCustomizingActionBar();
        onCreateCustomizingFormats();
        onCarSelected();
        onFuelSelection();
        switch (acao) {
            case Constantes.EDITAR:
                onStartVariables();
                break;
            case Constantes.INSERIR:
                abastecimento = new Abastecimento(this);
                abastecimento.setIdCarro(veiculoEscolhido.getCodigo());
                dataMaterialEditText.setText(getDateTime());
                break;
            case Constantes.VISUALIZAR:
                onStartVariables();
                onDisableInputVariables();
                break;
        }
        onSetTextListeners();
    }

    private void onFuelSelection() {
        List<Combustivel> combustivel = null;

        switch (veiculoEscolhido.getComb()) {
            case Constantes.GASOLINA:
                combustivel = Combustivel.consultarComb(this, "SELECT *"
                        + " FROM tb_Combustivel"
                        + " WHERE idCOMBUSTIVEL = 4"
                        + " OR idCOMBUSTIVEL = 5;");
                break;
            case Constantes.ALCOOL:
                combustivel = Combustivel.consultarComb(this, "SELECT *"
                        + " FROM tb_Combustivel"
                        + " WHERE idCOMBUSTIVEL = 1"
                        + " OR idCOMBUSTIVEL = 2;");
                break;
            case Constantes.FLEX:
                combustivel = Combustivel.consultarComb(this, "SELECT *"
                        + " FROM tb_Combustivel"
                        + " WHERE idCOMBUSTIVEL = 1"
                        + " OR idCOMBUSTIVEL = 2"
                        + " OR idCOMBUSTIVEL = 4"
                        + " OR idCOMBUSTIVEL = 5;");

                break;
            case Constantes.DIESEL:
                combustivel = Combustivel.consultarComb(this, "SELECT *"
                        + " FROM tb_Combustivel"
                        + " WHERE idCOMBUSTIVEL = 3;");
                break;
        }
        itens = combustivel.toArray(new Combustivel[0]);
        adapterCombustivel =
                new ArrayAdapter<>(this, R.layout.spinner_dropdown_list, itens);
        combustivelBetterSpinner.setAdapter(adapterCombustivel);
        combustivelBetterSpinner.setText(itens[0].getNome());
        escolhaCombustivel = itens[0];
    }

    private void onCarSelected() {
        veiculoEscolhido = getIntent().getParcelableExtra("veiculo");
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment(0);
        newFragment.show(getFragmentManager(), "datePicker");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dataAbastecimentoMaterialEditText:
                showDatePickerDialog(v);
                break;
            case R.id.localAbastecimentoMaterialEditText:
                Intent i = new Intent(this, LocalListActivity.class);
                i.putExtra("activity", "Abastecimento");
                startActivityForResult(i, Constantes.ABASTECIMENTO_CODE);
                break;
        }

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
        MaterialEditText selecionadoMaterialEditText = null;
        try {
            float valorAnterior = Float.parseFloat(s.toString()
                    .replace("R$", "").replace(".", "").replaceAll(",", "")
                    .replaceFirst(" ", ""));
            if (valorGastoMaterialEditText.isFocused()) {
                selecionadoMaterialEditText = valorGastoMaterialEditText;
                valorGasto = valorAnterior / 100;
                valorMask = dinheiroGastoFormat.format(valorAnterior / 100);
                valorGastoMaterialEditText.setText(valorMask);
            } else if (valorLitroMaterialEditText.isFocused()) {
                selecionadoMaterialEditText = valorLitroMaterialEditText;
                valorLitro = valorAnterior / 1000;
                valorMask = dinheiroLitroFormat.format(valorAnterior / 1000);
                valorLitroMaterialEditText.setText(valorMask);
            }
            onCalculateQuantityLitros(valorLitro, valorGasto);
            selecionadoMaterialEditText.setSelection(valorMask.length());
            onSetTextListeners();
        } catch (NumberFormatException e) {
        } catch (ArithmeticException e) {
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constantes.ABASTECIMENTO_CODE)
            if (resultCode == Constantes.LOCAL_LIST) {
                local = data.getParcelableExtra("parcel");
                localMaterialEditText.setText(local.getNome());
            }

    }

    private void onCalculateQuantityLitros(float valorLitro, float valorGasto) {
        float quantidadeLitros;
        if (valorLitro == 0 || valorGasto == 0) {
            quantidadeLitros = 0;
        } else {
            quantidadeLitros = valorGasto / valorLitro;
        }
        quantidadeLitrosTextView.setText(new DecimalFormat("##,###.##L")
                .format(quantidadeLitros));
    }

    private void applyCustomizingActionBar() {
        setSupportActionBar(abastecimentoToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setBackgroundDrawable(new ColorDrawable(Color
                .rgb(3, 103, 221)));
    }

    private void onSetTextListeners() {
        valorLitroMaterialEditText.addTextChangedListener(this);
        valorGastoMaterialEditText.addTextChangedListener(this);
    }

    private void onRemoveTextListeners() {
        valorLitroMaterialEditText.removeTextChangedListener(this);
        valorGastoMaterialEditText.removeTextChangedListener(this);
    }

    private void onCreateCustomizingFormats() {
        dinheiroGastoFormat = NumberFormat.getCurrencyInstance();
        dinheiroLitroFormat = NumberFormat.getCurrencyInstance();
        dinheiroLitroFormat.setMinimumFractionDigits(3);
        dinheiroLitroFormat.setMaximumFractionDigits(3);
    }

    private void onDisableInputVariables() {
        combustivelBetterSpinner.setEnabled(false);
        tanqueCheioCheckBox.setEnabled(false);
        valorLitroMaterialEditText.setEnabled(false);
        valorGastoMaterialEditText.setEnabled(false);
        hodometroMaterialEditText.setEnabled(false);
        localMaterialEditText.setEnabled(false);
        dataMaterialEditText.setEnabled(false);
        quantidadeLitrosTextView.setEnabled(false);

    }

    private void onStartVariables() {
        abastecimento = getIntent().getParcelableExtra("parcel");
        onSetCombustivel();
        if (abastecimento.getTanqueCheio().equals("sim"))
            tanqueCheioCheckBox.setChecked(true);
        valorLitroMaterialEditText.setText(dinheiroLitroFormat.format(abastecimento.getValorLitro()));
        valorLitroMaterialEditText.setSelection(valorLitroMaterialEditText.getEditableText().length());
        valorGastoMaterialEditText.setText(dinheiroGastoFormat.format(abastecimento.getValorGasto()));
        hodometroMaterialEditText.setText(Integer.toString(abastecimento.getKilometros()));
        local = Abastecimento.ConsultaLocal(this, "SELECT *"
                + " FROM tb_local"
                + " WHERE codigoLOCAL = " + abastecimento.getLocalGasto());
        localMaterialEditText.setText(local.getNome());
        dataMaterialEditText.setText(abastecimento.tratadata());
        onCalculateQuantityLitros(abastecimento.getValorLitro(), abastecimento.getValorGasto());
    }

    private void onSetCombustivel() {
        for (Combustivel combustivel : itens) {
            if (combustivel.getCodigo() == abastecimento.getCombustivel()) {
                combustivelBetterSpinner.setText(combustivel.getNome());
            }
        }
    }

    private String getDateTime() {
        Date date = new Date();
        return new SimpleDateFormat("dd/MM/yyyy").format(date);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;

    }

    public void onClickSave() {
            if(checkFields()){
                onCombustivelSelected();
                abastecimento.setCombustivel(escolhaCombustivel.getCodigo());
                abastecimento.setDataGasto(dataMaterialEditText.getText().toString());
                abastecimento.setKilometros(Integer.parseInt(hodometroMaterialEditText.getEditableText().toString()));
                abastecimento.setValorLitro(Float.parseFloat(valorLitroMaterialEditText.getEditableText().toString().replace("R$", "").replace(",", ".")));
                abastecimento.setValorGasto(Float.parseFloat(valorGastoMaterialEditText.getEditableText().toString().replace("R$", "").replace(",", ".")));
                abastecimento.setLocalGasto(local.getCodigo());
                abastecimento.setKmdif(0);
            if (tanqueCheioCheckBox.isChecked()) {
                abastecimento.setTanqueCheio("sim");
            } else {
                abastecimento.setTanqueCheio("nao");
            }
            if (getIntent().getIntExtra("acao", 0) == Constantes.EDITAR) {
                abastecimento.alteraAbastecimento(this);
                abastecimento.AtualizakmRodado(this, abastecimento.getIdCarro());
            } else {
                abastecimento.insereAbastecimento(this);
                MainActivity.getNavigationDrawerLeft().updateBadge(2,
                        new StringHolder(Integer.toString(Abastecimento.ContaAbastecimentos(this, veiculoEscolhido.getCodigo()))));
            }
            abastecimento.AtualizakmRodado(this, abastecimento.getIdCarro());
            finish();
        }
    }

    private void onCombustivelSelected() {
        for (Combustivel combustivel : itens) {
            if (combustivel.getNome().equals(combustivelBetterSpinner.getText().toString())) {
                escolhaCombustivel = combustivel;
            }
        }
    }

    private boolean checkFields() {
        if (valorLitroMaterialEditText.getEditableText().toString().equals("") || valorLitroMaterialEditText.getEditableText().toString().equals("R$0,000")) {
            valorLitroMaterialEditText.setError("Campo Obrigatório");
            valorLitroMaterialEditText.requestFocus();
        } else if (valorGastoMaterialEditText.getEditableText().toString().equals("") || valorGastoMaterialEditText.getEditableText().toString().equals("R$0,00")) {
            valorGastoMaterialEditText.setError("Campo Obrigatório");
            valorGastoMaterialEditText.requestFocus();
        } else if (hodometroMaterialEditText.getEditableText().toString().equals("") || hodometroMaterialEditText.getEditableText().toString().equals("0")) {
            hodometroMaterialEditText.setError("Campo Obrigatório");
            hodometroMaterialEditText.requestFocus();
        } else if (local == null) {
            localMaterialEditText.setError("Campo Obrigatório");
        } else if (Integer.parseInt(hodometroMaterialEditText.getEditableText().toString()) <= Abastecimento.getMaxHodometro(this, veiculoEscolhido)
                && acao != Constantes.EDITAR && acao != Constantes.VISUALIZAR) {
            hodometroMaterialEditText.setError("Valor do hodometro menor que o ultimo inserido");
        } else {
            return true;
        }
        return false;
    }
}
