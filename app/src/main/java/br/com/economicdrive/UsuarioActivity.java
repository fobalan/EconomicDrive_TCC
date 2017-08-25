package br.com.economicdrive;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import br.com.economicdrive.constantes.Constantes;
import br.com.economicdrive.model.Usuario;

public class UsuarioActivity extends AppCompatActivity implements Button.OnClickListener {

    private EditText senhaEdittext;
    private EditText nSenhaEditText;
    private EditText csenhaEditText;
    private ImageButton usuarioButton;
    private CheckBox desatcCheckBox;
    private ActionBar actionBar;
    private Toolbar toolbar;
    int intAcao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario);
        senhaEdittext = (EditText) findViewById(R.id.senhaeditText);
        nSenhaEditText = (EditText) findViewById(R.id.nSenhaEditText);
        csenhaEditText = (EditText) findViewById(R.id.cSenhaEditText);
        usuarioButton = (ImageButton) findViewById(R.id.Usuariobutton);
        desatcCheckBox = (CheckBox) findViewById(R.id.desativacheckBox);
        toolbar = (Toolbar) findViewById(R.id.myUsuarioToolbar);
        usuarioButton.setOnClickListener(this);
        onActionBar();
        Intent origemIntent = this.getIntent();
        Bundle b = origemIntent.getExtras();
        intAcao = b.getInt("acao");
        if (intAcao == Constantes.INSERIR) {
            senhaEdittext.setEnabled(false);
            desatcCheckBox.setEnabled(false);
        } else {
            senhaEdittext.setEnabled(true);
        }
        desatcCheckBox.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (desatcCheckBox.isChecked()) {
                    nSenhaEditText.setEnabled(false);
                    csenhaEditText.setEnabled(false);
                    nSenhaEditText.setText("");
                    csenhaEditText.setText("");
                } else {
                    nSenhaEditText.setEnabled(true);
                    csenhaEditText.setEnabled(true);
                }
            }
        });
    }

    private void onActionBar() {
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.rgb(3, 103, 221)));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.Usuariobutton:
                Usuario usuario = null;
                try {
                    if (!desatcCheckBox.isChecked()) {
                        String nsenha = nSenhaEditText.getEditableText().toString();
                        String csenha = csenhaEditText.getEditableText().toString();
                        if (nsenha.equals("")) {
                            Toast.makeText(getApplicationContext(), "A nova senha não pode estar em branco", Toast.LENGTH_SHORT).show();
                        } else if (csenha.equals("")) {
                            Toast.makeText(getApplicationContext(), "A confirmação senha não pode estar em branco", Toast.LENGTH_SHORT).show();
                        } else if (nsenha.equals(csenha)) {
                            if (nsenha.length() == 4) {
                                if (csenha.length() == 4) {
                                    if (intAcao == Constantes.INSERIR) {
                                        usuario = new Usuario(nsenha);
                                        usuario.insereSenha(this);
                                        Toast.makeText(getApplicationContext(), "Senha cadastrada com sucesso!", Toast.LENGTH_SHORT).show();
                                        finish();
                                    } else {
                                        String senha = senhaEdittext.getEditableText().toString();
                                        usuario = Usuario.ConsultaUsuario(this);
                                        if (senha.equals(usuario.getSenha())) {
                                            usuario.setSenha(nsenha);
                                            usuario.alteraUsuario(this);
                                            Toast.makeText(getApplicationContext(), "Senha alterada com sucesso!", Toast.LENGTH_SHORT).show();
                                            finish();
                                        } else {
                                            Toast.makeText(getApplicationContext(), "Senha atual inválida", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                } else {
                                    Toast.makeText(getApplicationContext(), "A senha de conter 4 digitos", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(getApplicationContext(), "A senha de conter 4 digitos", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Nova senha diferente da confirmação", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        String senha = senhaEdittext.getEditableText().toString();
                        usuario = Usuario.ConsultaUsuario(this);
                        if (senha.equals(usuario.getSenha())) {
                            usuario.deletaUsuario(this);
                            Toast.makeText(getApplicationContext(), "Senha deletada com sucesso!", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), "Senha atual inválida", Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (Error e) {
                    Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.desativacheckBox:

                break;
        }
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

}
