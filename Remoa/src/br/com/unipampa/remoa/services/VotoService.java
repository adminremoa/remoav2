package br.com.unipampa.remoa.services;

import br.com.unipampa.remoa.beans.Voto;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
/**
 * @author Ricardo Burg Machado, Alencar Machado
 * @version 2.0
 * @since 2012
 * Classe respons�vel pela persist�ncia de dados dos objetos do tipo <Voto>
 */
public class VotoService {

	private SQLiteDatabase db;
	private final static String TABELA_VOTOS= "votos";
	private final static String ATRIBUTO_DB_ID = "id";
	private final static String ATRIBUTO_DB_IDEVENTO = "idEvento";
	private final static String ATRIBUTO_DB_VOTO = "voto";	
	
	/**
	 * M�todo construtor que faz a atribui��o de seu par�metro do tipo SQLiteDatabase a vari�vel global.
	 * Faz a montagem e execu��o de uma instru��o SQL que visa criar a tabela 'votos' se a mesma ainda n�o existir.
	 * @param SQLiteDatabase d
	 */
	public VotoService (SQLiteDatabase d) {
		
		this.db = d;
		
		StringBuilder sb3 = new StringBuilder();
		sb3.append(" CREATE TABLE IF NOT EXISTS "+TABELA_VOTOS);
		sb3.append(" ( ");
		sb3.append(ATRIBUTO_DB_ID+" integer primary key autoincrement,");	
		sb3.append(ATRIBUTO_DB_IDEVENTO+" integer NOT NULL,");	
		sb3.append(ATRIBUTO_DB_VOTO+" varchar(50) NOT NULL, ");
		sb3.append("FOREIGN KEY ("+ATRIBUTO_DB_IDEVENTO+") REFERENCES eventos (_id)"); 
		sb3.append(" ); ");
		db.execSQL(sb3.toString());

	}

	/**
	 * M�todo que visa inserir no banco de dados um voto recebido por par�metro.
	 * � feita uma concatena��o e valores com suas respectivas chaves para serem atribuidos a uma instru��o SQL;
	 * a qual atribui ao atributo 'id' da tabela automaticamente o pr�ximo id dispon�vel por isso o atributo id do objeto voto n�o � necess�rio
	 * @param Voto voto
	 * @return boolean
	 */
	public boolean inserirVoto(Voto voto){
		
		ContentValues cv = new ContentValues();		
		cv.put(ATRIBUTO_DB_IDEVENTO, 	  ""+voto.getIdEvento()+"");
		cv.put(ATRIBUTO_DB_VOTO,        ""+voto.getVoto()+"");		
	
		long res = db.insert(TABELA_VOTOS, ATRIBUTO_DB_ID, cv);
		
		
		Log.d("RETORNO INSER��O: ", ""+res);
		
		if(res > 0){
			
			Log.d("INSER��O DE DADOS ", "Dados inseridos com sucesso!");
			return true;
		}else{
			
			Log.d("ERRO INSER��O DE DADOS ", "Erro, a inser��o n�o pode ser efetuada.!");
			return false;
		}
	}


	
	public void removeTabelaVotos(){
		
		StringBuilder sb = new StringBuilder();
		sb.append(" DROP TABLE "+TABELA_VOTOS);
				
		Log.d("SQL GERADA removeTabelaVotos():  ", ""+sb.toString() );
				
		db.execSQL(sb.toString());
				
	}

	
	public void deletarTodosVotos(){
		
		Cursor c = db.query(TABELA_VOTOS, new String[]{ATRIBUTO_DB_ID, ATRIBUTO_DB_VOTO, ATRIBUTO_DB_IDEVENTO},
				null, null, null, null, null);
		
		while(c.moveToNext()){
			
			if(deletarVoto( c.getInt(c.getColumnIndex(ATRIBUTO_DB_ID))  ) ){
				
				Log.d("Remo��o Voto ", "Remo��o do Voto id ["+c.getInt(c.getColumnIndex("id"))+"] efetuada com sucesso!" );
			}
				
		}
		//c.close();		
		
	}

	public boolean deletarVoto(int id){
		
		return db.delete(TABELA_VOTOS, ATRIBUTO_DB_ID+" = "+ id, null) > 0;	
	}
	
	
	
}
