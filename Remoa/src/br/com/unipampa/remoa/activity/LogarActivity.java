package br.com.unipampa.remoa.activity;

import java.io.IOException;

import br.com.unipampa.remoa.R;
import br.com.unipampa.remoa.R.layout;
import br.com.unipampa.remoa.R.menu;
import br.com.unipampa.remoa.conexaoHTTP.Logar;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * @author Ricardo Burg Machado, Alencar Machado
 * @version 2.0
 * @since 2012
 * Classe que inicia a aplica��o apresentando uma tela de login ao usu�rio, extende Activity e implementa Runnable
 */
public class LogarActivity extends Activity implements Runnable{
	
	private ProgressDialog dialog;
	private EditText loginTela;
	private EditText senhaTela;
	private Handler handler = new Handler();

	/**
	 * M�todo respons�vel pela constru��o da interface e gerenciamento das a��es/onClick do usu�rio com a mesma
	 */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logar);
        
        loginTela = (EditText)findViewById(R.id.edUsuario);
        senhaTela = (EditText) findViewById(R.id.edSenha);
        
        Button btLog = (Button) findViewById(R.id.btLogar);
        Button btSair = (Button) findViewById(R.id.btSair);
        
        
        btLog.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				
				logar();
			}
		});
        
        btSair.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				
				finish();
			}
		});
        
    }

    /**
     * M�todo que inicializa um ProgressDialog que avisa ao usu�rio que o sistema est� sendo carregado
     */
    private void logar(){
    	dialog = ProgressDialog.show(this, "LOGAR", "LOGANDO NO REMOA, por favor aguarde...",false,true);
    	new Thread(this).start();
    }

    /**
     * M�todo de implementa��o de assinatura da classe Runnable
     */
	public void run() {
		String login = loginTela.getText().toString();
		String sen = senhaTela.getText().toString();

		try {
			final boolean logado = true;

			handler.post(new Runnable() {
				public void run() {

					iniciaApp(logado);
				}
			});
			
		} catch (Exception e) {
			
			Log.e("ERRO AO LOGAR", e.getMessage(), e);
		} finally {
			
			dialog.dismiss();
		}		
	}
	
	/**
	 * M�todo que verifica o par�metro boolean e inicializa a Activity principal da aplica��o 'RemoaMapaActivity'
	 * @param logado
	 */
    private void iniciaApp(boolean logado){
        //alterar para pegar a conexao server
    	logado = true;
        	
        if(logado){
        	Intent it = new Intent(getBaseContext(), RemoaMapaActivity.class);
			startActivity(it);
				
		}else{
			Log.d("LOGOU", "LOGOU N�O LOGOU");
				(Toast.makeText(getBaseContext(), "ERRO AO LOGAR", Toast.LENGTH_SHORT)).show();
		}
    }
    
    
    
    
}
