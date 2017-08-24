package br.com.economicdrive;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashActivity extends Activity implements Runnable {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		Handler handler = new Handler();
		handler.postDelayed(this, 3000);
	}

	@Override
	public void run() {
		Usuario usuario = Usuario.ConsultaUsuario(this);
		if (usuario == null){
			startActivity(new Intent(this, MainActivity.class));
			finish();
		}
		else{
			Intent i = new Intent(this, LoginActivity.class);
			i.putExtra("senha", usuario.getSenha());
			startActivity(i);
			finish();
		}
		
	}
}
