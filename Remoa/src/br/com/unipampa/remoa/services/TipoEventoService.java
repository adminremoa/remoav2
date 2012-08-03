package br.com.unipampa.remoa.services;

import java.util.ArrayList;

import br.com.unipampa.remoa.beans.TipoEvento;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
/**
 * @author Ricardo Burg Machado, Alencar Machado
 * @version 2.0
 * @since 2012
 * Classe responsável pela persistência de dados dos objetos do tipo <TipoEvento>
 */
public class TipoEventoService {

	private SQLiteDatabase db;	
	private final static String TABELA_TIPOEVENTO = "tipoEvento";
	private final static String ATRIBUTO_DB_ID = "id";
	private final static String ATRIBUTO_DB_DESCRICAO = "descricao";

	/**
	 * Método construtor que faz a atribuição de seu parâmetro do tipo SQLiteDatabase a variável global.
	 * Faz a montagem e execução de uma instrução SQL que visa criar a tabela 'tipoEvento' se a mesma ainda não existir.
	 * Executa o método interno  verificaTabelaVazia() para caso não a tabela 'tipoEvento' esteja vazia seja executa um script que insira os tipos de eventos/classificação na tabela
	 * pois se faz necesária no cadastro de eventos
	 * @param SQLiteDatabase d
	 */
	public TipoEventoService(SQLiteDatabase d){

		this.db  = d;
		
		StringBuilder sb2 = new StringBuilder();
		sb2.append(" CREATE TABLE IF NOT EXISTS "+TABELA_TIPOEVENTO);
		sb2.append(" ( ");
		sb2.append( ATRIBUTO_DB_ID + " integer primary key autoincrement NOT NULL,");	
		sb2.append( ATRIBUTO_DB_DESCRICAO + " varchar(50) NOT NULL");
		sb2.append(" ); ");
		//Log.d("SQL GERADA construtor TipoEventoService:  ", ""+sb2.toString() );
		db.execSQL(sb2.toString());

		if( verificaTabelaVazia() ){
			
			insereDadosTabela();
		}
	}


	/**
	 * Método que visa inserir no banco de dados um evento recebido por parâmetro.
	 * É feita uma concatenação e valores com suas respectivas chaves para serem atribuidos a uma instrução SQL;
	 * a qual atribui ao atributo 'id' da tabela automaticamente o próximo id disponível por isso o atributo id do objeto tipoEvento não é necessário
	 * @param Evento evento
	 * @param Context context
	 * @return long
	 */
	public boolean inserirTipoEvento(TipoEvento tipoEvento){
		
		ContentValues cv = new ContentValues();		
		cv.put(ATRIBUTO_DB_DESCRICAO, tipoEvento.getDescricao() );
			
		long res = db.insert(TABELA_TIPOEVENTO, ATRIBUTO_DB_ID, cv);
		
		
		//Log.d("RETORNO INSERÇÃO TipoEvento: ", ""+res);
		
		if(res > 0){
			
			//Log.d("INSERÇÃO DE DADOS ", "Dados inseridos com sucesso!");
			return true;
		}else{
			
			//Log.d("ERRO INSERÇÃO DE DADOS ", "Erro, a inserção não pode ser efetuada.!");
			return false;
		}
	}

	/**
	 * Método que verifica se há algum registro na tabela 'tipoEvento'
	 * @return boolean
	 */
	private boolean verificaTabelaVazia(){
		
		int retorno = 0;
		Cursor c = db.rawQuery("SELECT COUNT("+ATRIBUTO_DB_ID+") FROM "+TABELA_TIPOEVENTO , null );					
		c.moveToFirst();
		retorno = c.getInt(0);
		
		if(retorno == 0)
			return true;// a tabela está vazia
		else
			return false;// a tabela contém dados
	}
	
	/**
	 * Método que executa o métod inserirTipoEvento inserindo 4 registros na tabela 'tipoEvento'
	 */
	private void insereDadosTabela (){
		
	    inserirTipoEvento( new TipoEvento(1, "SAÚDE") );
	    inserirTipoEvento( new TipoEvento(2, "TRÂNSITO") );
	    inserirTipoEvento( new TipoEvento(3, "ENTRETENIMENTO") );
	    inserirTipoEvento( new TipoEvento(4, "SEGURANÇA") );		
	}
	
	/**
	 * Método que obtém único tipoEvento contido no banco que tenha o id passada por parâmetro
	 * @param int id
	 * @return TipoEvento tipoEvento
	 */
	public TipoEvento getTipoEvento(int id){
		
		Cursor c = db.query(TABELA_TIPOEVENTO, new String[]{ATRIBUTO_DB_ID, ATRIBUTO_DB_DESCRICAO}, ATRIBUTO_DB_ID+" = "+id, null, null, null, null);
				
		while(c.moveToNext()){
		
/*			Log.d("Debug getTipoEvento(int id): ", 
					"TipoEventoId: "+c.getString(c.getColumnIndex("id"))
					+"| TipoEventoDescrição: "+c.getString(c.getColumnIndex("descricao")) 
			);
*/			
			TipoEvento tipoEventoRetorno = new TipoEvento( 
					c.getInt(c.getColumnIndex(ATRIBUTO_DB_ID)), 
					c.getString(c.getColumnIndex(ATRIBUTO_DB_DESCRICAO))  
					);
			
			//c.close();
			
			return tipoEventoRetorno;
		}
		return null;
	}
	
	/**
	 * Método que obtém único tipoEvento contido no banco que tenha a descrição passada por parâmetro
	 * @param String descricao
	 * @return TipoEvento tipoEvento
	 */
	public TipoEvento getTipoEvento(String descricao){
		
		Cursor c = db.query("tipoEvento", new String[]{ATRIBUTO_DB_ID, ATRIBUTO_DB_DESCRICAO}, ATRIBUTO_DB_DESCRICAO+" = '"+descricao+"'", null, null, null, null);
		
		while(c.moveToNext()){
		
/*			Log.d("Debug getTipoEvento(String descricao): ", 
					"TipoEventoId: "+c.getString(c.getColumnIndex("id"))
					+"| TipoEventoDescrição: "+c.getString(c.getColumnIndex("descricao")) 
			);
*/			
			TipoEvento tipoEventoRetorno = new TipoEvento( 
					c.getInt(c.getColumnIndex(ATRIBUTO_DB_ID)), 
					c.getString(c.getColumnIndex(ATRIBUTO_DB_DESCRICAO))  
					);
			
			//c.close();
			
			return tipoEventoRetorno;
		}
		return null;
	}

	public void removeTabelaTipoEvento(){
		
		StringBuilder sb = new StringBuilder();
		sb.append(" DROP TABLE "+TABELA_TIPOEVENTO);
				
		//Log.d("SQL GERADA removeTabelaTipoEvento():  ", ""+sb.toString() );
				
		db.execSQL(sb.toString());
				
	}

	public void deletarTodosTiposDeEvento(){
		
		Cursor c = db.query(TABELA_TIPOEVENTO, new String[]{ATRIBUTO_DB_ID, ATRIBUTO_DB_DESCRICAO},
				null, null, null, null, null);
		
		while(c.moveToNext()){
			
			if(deletarTipoEvento( c.getInt(c.getColumnIndex("id"))  ) ){
				
				Log.d("Remoção TipoEvento ", "Remoção do Tipoevento id ["+c.getInt(c.getColumnIndex("id"))+"] efetuada com sucesso!" );
			}
				
		}
		//c.close();		
		
	}

	/**
	 * Método que obtém todos os tipoEvento contidos no banco e os transforma em objetos
	 * @return ArrayList<TipoEvento>
	*/
	public ArrayList<TipoEvento> getTiposEvento(){				
		
		ArrayList<TipoEvento> list = new ArrayList<TipoEvento>();
		
		Cursor c = db.query(TABELA_TIPOEVENTO, new String[]{ATRIBUTO_DB_ID, ATRIBUTO_DB_DESCRICAO},
				null, null, null, null, null);					
		
		int contador = 0;
		
		while(c.moveToNext()){
			
			contador++;
			
			list.add( new TipoEvento( 
					Integer.parseInt( c.getString(c.getColumnIndex(ATRIBUTO_DB_ID)) ),
					c.getString(c.getColumnIndex(ATRIBUTO_DB_DESCRICAO))
					));
		}
		//c.close();		
		
/*		Log.d("Debug TipoEventos ", "QUANTIDADE TipoEventos: "+contador);
		
		for(int i = 0; i < list.size(); i++){
			
			Log.d("Debug TiposEvento: ["+i+"]", 
					"TipoEventoId: "+list.get(i).getId()
					+" TipoEventoDescrição: "+list.get(i).getDescricao() 
			);
		}		
*/		
		return list;
	}

	
	public boolean deletarTipoEvento(int id){
		
		return db.delete(TABELA_TIPOEVENTO, ATRIBUTO_DB_ID+" = "+ id, null) > 0;	
	}

	
	
}
