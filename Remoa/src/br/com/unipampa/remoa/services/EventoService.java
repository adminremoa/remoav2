package br.com.unipampa.remoa.services;

import java.util.ArrayList;
import java.util.List;

import br.com.unipampa.remoa.beans.Evento;
import br.com.unipampa.remoa.beans.TipoEvento;
import br.com.unipampa.remoa.beans.Voto;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;


import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.Toast;
/**
 * @author Ricardo Burg Machado, Alencar Machado
 * @version 2.0
 * @since 2012
 * Classe responsável pela persistência de dados dos objetos do tipo Evento
 */
public class EventoService {


	private SQLiteDatabase db;
	private final static String TABELA_EVENTO = "eventos";
	private final static String ATRIBUTO_DB_ID = "_id";
	private final static String ATRIBUTO_DB_NOME = "nome";
	private final static String ATRIBUTO_DB_INFORMACAO = "informacao";
	private final static String ATRIBUTO_DB_LATITUDE = "latitude";
	private final static String ATRIBUTO_DB_LONGITUDE = "longitude";
	private final static String ATRIBUTO_DB_ID_TIPO_EVENTO = "id_tipo_evento";
	private final static String ATRIBUTO_DB_DATA = "data";
	private final static String ATRIBUTO_DB_HORA = "hora";
	private TipoEventoService tipoEvService;
	private Context context;
	
	
	/**
	 * Método construtor que faz a atribuição de seu parâmetro do tipo SQLiteDatabase a variável global.
	 * Faz a montagem e execução de uma instrução SQL que visa criar a tabela 'eventos' se a mesma ainda não existir
	 * @param SQLiteDatabase d
	 */
	public EventoService(SQLiteDatabase d) {				
		
		this.db = d;		

		StringBuilder sb = new StringBuilder();
		sb.append(" CREATE TABLE IF NOT EXISTS "+TABELA_EVENTO);
		sb.append(" ( ");
		sb.append(""+ATRIBUTO_DB_ID+" integer primary key autoincrement NOT NULL,");	
		sb.append(""+ATRIBUTO_DB_NOME+" varchar(50) NOT NULL,");
		sb.append(""+ATRIBUTO_DB_INFORMACAO+" varchar(500) NOT NULL,");
		sb.append(""+ATRIBUTO_DB_LATITUDE+" varchar(20) NOT NULL,");
		sb.append(""+ATRIBUTO_DB_LONGITUDE+" varchar(20) NOT NULL,");
		sb.append(""+ATRIBUTO_DB_ID_TIPO_EVENTO+" integer NOT NULL,");
		
		sb.append(""+ATRIBUTO_DB_DATA+" varchar(20) NOT NULL,");
		sb.append(""+ATRIBUTO_DB_HORA+" varchar(20) NOT NULL, ");
		sb.append(" FOREIGN KEY ("+ATRIBUTO_DB_ID_TIPO_EVENTO+") REFERENCES tipoEvento (id)"); 
		sb.append(" ); ");
		//Log.d("SQL GERADA construtor EventoService:  ", ""+sb.toString() );		
		db.execSQL(sb.toString());
	
	}
	
	/**
	 * Método que registra um voto de um determinado evento
	 * Inicializa o votoService e executa o método inserirVoto
	 * Caso o voto seja inserido na base de dados e  apresentado mensagem ao usuário, caso não seja apresenta-se outra mensagem
	 * @param Voto voto
	 * @param Context context
	 */
	public void votar(Voto voto, Context context){		
		
		VotoService votoService = new VotoService(this.db);
		
		if(votoService.inserirVoto(voto)){
			
			Toast.makeText( context , "Voto computado com sucesso!", Toast.LENGTH_SHORT).show();				
		}else{
			
			Toast.makeText( context , "Erro\nSeu voto não pôde ser processado", Toast.LENGTH_LONG).show();				
		}
		
	}
	
	/**
	 * Método que registra um voto de um determinado evento
	 * Inicializa o votoService e executa o método inserirVoto
	 * @param Voto voto
	 */
	public void votar(Voto voto){		
		
		VotoService votoService = new VotoService(this.db);
		
		votoService.inserirVoto(voto);
		
	}
	
	/**
	 * Método que executa uma instrução SQL que visa retornar a quantidade de votos da base de dados descritos como 'CONCORDO' de um evento (representado pelo parâmetro int idEvento)
	 * @param int idEvento
	 * @return int retorno
	 */
	public int getQtdVotosConcordo(int idEvento){
		
		VotoService vs = new VotoService(db);
		
		int retorno = 0;
		
		Cursor c = db.rawQuery("SELECT COUNT(voto) FROM votos WHERE idEvento = "+idEvento+" AND voto = 'Concordo' " , null );					
		
		c.moveToFirst();
		
		retorno = c.getInt(0);
		
		return retorno;
		
	}
	/**
	 * Método que executa uma instrução SQL que visa retornar a quantidade de votos da base de dados descritos como 'DISCORDO' de um evento (representado pelo parâmetro int idEvento)
	 * @param int idEvento
	 * @return int retorno
	 */
	public int getQtdVotosDiscordo(int idEvento){
	
		VotoService vs = new VotoService(db);
		
		int retorno = 0;
		
		Cursor c = db.rawQuery("SELECT COUNT(voto) FROM votos WHERE idEvento = "+idEvento+" AND voto = 'Discordo' " , null );					
		
		c.moveToFirst();
		
		retorno = c.getInt(0);
		
		return retorno;
		
	}
	
	
	
	

	/**
	 * Método que visa inserir no banco de dados um evento recebido por parâmetro.
	 * É feita uma concatenação e valores com suas respectivas chaves para serem atribuidos a uma instrução SQL;
	 * É executado o método de verficação de dados.
	 * Se a verificação retornar 'true' é executada a instrução SQL
	 * a qual atribui ao atributo '_id' da tabela automaticamente o próximo id disponível por isso o atributo id do objeto Evento não é necessário
	 * No final caso a instrução SQL seja executa com sucesso é mostrada uma mensagem ao usuário 'Toast', por isso a necessidade do 'Context' como parâmetro
	 * @param Evento evento
	 * @param Context context
	 * @return long
	 */
	public long inserir(Evento evento, Context context){
		
		long res = 0 ;
		ContentValues cv = new ContentValues();		
		cv.put(ATRIBUTO_DB_NOME, 		   ""+evento.getNome()+"");
		cv.put(ATRIBUTO_DB_INFORMACAO,     ""+evento.getInformacao()+"");
		cv.put(ATRIBUTO_DB_LATITUDE,       ""+evento.getLatitude()+"");		
		cv.put(ATRIBUTO_DB_LONGITUDE,      ""+evento.getLongitude()+"");			
		cv.put(ATRIBUTO_DB_ID_TIPO_EVENTO, ""+evento.getTipo().getId()+"");
		cv.put(ATRIBUTO_DB_DATA,           ""+evento.getData()+"");
		cv.put(ATRIBUTO_DB_HORA,           ""+evento.getHora()+"");
		
		
		if( verificaDados(evento, context) ){
			
			res = db.insert(TABELA_EVENTO, "_id", cv);
		}
		
		Log.d("RETORNO INSERÇÃO: ", ""+res);
		
		if(res > 0){
			Toast.makeText( context , "Evento cadastrado com sucesso!", Toast.LENGTH_LONG).show();				
			Log.d("INSERÇÃO DE DADOS ", "Dados inseridos com sucesso!");
			return res;
		}else{
			
			Log.d("ERRO INSERÇÃO DE DADOS ", "Erro, a inserção não pode ser efetuada.!");
			return res;
		}
	}
	
	/**
	 * Método para uso interno da classe que verifica se alguns atributos do evento estão preenchidos
	 * Caso exista um ou mais erros é apresentado uma mensagem 'Toast' ao usuário com a descrição dos erros encontrados (por isso o Context) 
	 * @param Evento evento
	 * @param Context context
	 * @return boolean
	 */
	 private boolean verificaDados(Evento evento, Context context){
		
	    int erros = 0;
	    String errosDescricao = "";
		
		if(evento.getNome().isEmpty()){
			
			erros++;
			errosDescricao += "\n[ Nome ] está vazio.";
		}
		
		if(evento.getInformacao().isEmpty()){
			
			erros++;
			errosDescricao += "\n[ Descrição ] está vazio.";
		}
		
		if(evento.getTipo().getId() == 0){
			
			erros++;
			errosDescricao += "\n[ Classificação ] não foi selecionado.";
		}
		
		if(erros > 0){
			Toast.makeText( context , "Os seguintes campos estão com problemas:"+errosDescricao, 60).show();				
			return false;
		}else{
			
			return true;
		}
		
	}
	
	public boolean atualizar(Evento evento, int idParamtro){
		
		ContentValues cv = new ContentValues();		
		cv.put(ATRIBUTO_DB_NOME, 		   ""+evento.getNome()+"");
		cv.put(ATRIBUTO_DB_INFORMACAO,     ""+evento.getInformacao()+"");
		cv.put(ATRIBUTO_DB_LATITUDE,       ""+evento.getLatitude()+"");		
		cv.put(ATRIBUTO_DB_LONGITUDE,      ""+evento.getLongitude()+"");			
		cv.put(ATRIBUTO_DB_ID_TIPO_EVENTO, ""+evento.getTipo().getId()+"");
		cv.put(ATRIBUTO_DB_DATA,           ""+evento.getData()+"");
		cv.put(ATRIBUTO_DB_HORA,           ""+evento.getHora()+"");
		
		long res = db.update(TABELA_EVENTO, cv, ATRIBUTO_DB_ID +" = "+idParamtro+" ", null);
		
		Log.d("RETORNO ATUALIZAÇÃO: ", ""+res);
		
		if(res > 0){
			
			Log.d("ATUALIZAÇÃO DE DADOS ", "Dados atualizados com sucesso!");
			return true;
		}else{
			
			Log.d("ERRO ATUALIZAÇÃO DE DADOS ", "Erro, a atualização não pode ser efetuada.!");
			return false;
		}
	}
	
	
	public void removeTabelaEventos(){
		
		StringBuilder sb = new StringBuilder();
		sb.append(" DROP TABLE "+TABELA_EVENTO);
				
		Log.d("SQL GERADA removeTabelaEventoS():  ", ""+sb.toString() );
				
		db.execSQL(sb.toString());
				
	}
	
	
	public void deletarTodosEventos(){
		
		Cursor c = db.query(TABELA_EVENTO, new String[]{ATRIBUTO_DB_ID, ATRIBUTO_DB_LATITUDE, ATRIBUTO_DB_LONGITUDE, ATRIBUTO_DB_NOME, ATRIBUTO_DB_INFORMACAO, ATRIBUTO_DB_ID_TIPO_EVENTO, ATRIBUTO_DB_DATA, ATRIBUTO_DB_HORA},
				null, null, null, null, null);
		
		while(c.moveToNext()){
			
			if(deletar( c.getInt(c.getColumnIndex(ATRIBUTO_DB_ID))  ) ){
				
				Log.d("Remoção Evento ", "Remoção do evento id ["+c.getInt(c.getColumnIndex(ATRIBUTO_DB_ID))+"] efetuada com sucesso!" );
			}
				
		}
		//c.close();		
		
	}

	/**
	 * Método que obtém todos os eventos contidos no banco e os transforma em objetos
	 * @return ArrayList<Evento>
	 */
	public ArrayList<Evento> getEventos(){				
		
		ArrayList<Evento> list = new ArrayList<Evento>();
		tipoEvService = new TipoEventoService(this.db);
		
		Cursor c = db.query(TABELA_EVENTO, new String[]{ATRIBUTO_DB_ID, ATRIBUTO_DB_LATITUDE, ATRIBUTO_DB_LONGITUDE, ATRIBUTO_DB_NOME, ATRIBUTO_DB_INFORMACAO, ATRIBUTO_DB_ID_TIPO_EVENTO, ATRIBUTO_DB_DATA, ATRIBUTO_DB_HORA},
				null, null, null, null, null);					
		
		int contador = 0;		
		
		while(c.moveToNext()){
			
			contador++;
			
			list.add(new Evento( 
					c.getInt(c.getColumnIndex(ATRIBUTO_DB_ID)), 
					Integer.parseInt(  c.getString(c.getColumnIndex(ATRIBUTO_DB_LATITUDE)) ), 
					Integer.parseInt(  c.getString(c.getColumnIndex(ATRIBUTO_DB_LONGITUDE)) ),
					c.getString(c.getColumnIndex(ATRIBUTO_DB_NOME)),
					c.getString(c.getColumnIndex(ATRIBUTO_DB_INFORMACAO)),
					tipoEvService.getTipoEvento( c.getInt(c.getColumnIndex(ATRIBUTO_DB_ID_TIPO_EVENTO) ) ),
					c.getString(c.getColumnIndex(ATRIBUTO_DB_DATA)),
					c.getString(c.getColumnIndex(ATRIBUTO_DB_HORA))
					));
					
		}
		//c.close();		

		
		Log.d("Debug Eventos ", "QUANTIDADE EVENTOS: "+contador);
		
		for(int i = 0; i < list.size(); i++){
			
			Log.d("Debug Eventos: ["+i+"]", 
					"EventoId: "+list.get(i).getIdEvento()
					+" EventoNome: "+list.get(i).getNome() 
					+" EventoInformação: "+list.get(i).getInformacao()
					+" EventoLatitude: "+list.get(i).getLatitude()
					+" EventoLongitude: "+list.get(i).getLongitude()
					+" EventoTipoId: "+list.get(i).getTipo().getId()
					+" EventoTipoDescrição: "+list.get(i).getTipo().getDescricao()
					+" EventoData: "+list.get(i).getData()
					+" EventoHora: "+list.get(i).getHora()
			);
		}
				
		
		return list;
	}

	public ArrayList<Voto> getVotos(){				
		
		ArrayList<Voto> list = new ArrayList<Voto>();
		
		Cursor c = db.query("votos", new String[]{"id", "voto", "idEvento"},
				null, null, null, null, null);					
		
		int contador = 0;
		
		while(c.moveToNext()){
			
			contador++;
			
			list.add( new Voto( 
					c.getInt(c.getColumnIndex("id")),
					c.getString(c.getColumnIndex("voto")),
					c.getInt(c.getColumnIndex("idEvento"))
					));
			

			Log.d("Debug Votos: ", 
					"Id: "+c.getInt(c.getColumnIndex("id"))
					+"Voto: "+c.getString(c.getColumnIndex("voto"))
					+" IdEvento: "+c.getInt(c.getColumnIndex("idEvento"))
				);

			
		}
		//c.close();		

		
		Log.d("Debug Votos ", "QUANTIDADE Votos: "+contador);
/*		
		Evento evento = new Evento();
		for(int i = 0; i < list.size(); i++){
			
			Log.d("Debug Votos: ["+i+"]", 
					"Id: "+list.get(i).getId()
					+"Voto: "+list.get(i).getVoto()
					+" IdEvento: "+list.get(i).getIdEvento()
					+" Evento: "+ getEvento( list.get(i).getIdEvento() ).getNome()
			);
		}		
*/		
		return list;
	}

	
	
	public Cursor getEventosCursor(){								
		
		return db.query(TABELA_EVENTO, new String[]{ATRIBUTO_DB_ID+" as _id",ATRIBUTO_DB_LATITUDE, ATRIBUTO_DB_LONGITUDE, ATRIBUTO_DB_NOME, ATRIBUTO_DB_INFORMACAO, ATRIBUTO_DB_ID_TIPO_EVENTO, ATRIBUTO_DB_DATA, ATRIBUTO_DB_HORA},
				null, null, null, null, null);		
	}
	
	public boolean deletar(int id){
		
		return db.delete(TABELA_EVENTO, ATRIBUTO_DB_ID +" = "+ id, null) > 0;	
	}

	/**
	 * Método que obtém único evento contido no banco que tenha a informação passada por parâmetro
	 * @param String informacao
	 * @return Evento
	 */
	public Evento getEvento(String informacao){
		
		tipoEvService = new TipoEventoService(this.db);
		Cursor c = db.query(TABELA_EVENTO, new String[]{ATRIBUTO_DB_ID, ATRIBUTO_DB_LATITUDE, ATRIBUTO_DB_LONGITUDE, ATRIBUTO_DB_NOME, ATRIBUTO_DB_INFORMACAO, ATRIBUTO_DB_ID_TIPO_EVENTO, ATRIBUTO_DB_DATA, ATRIBUTO_DB_HORA},
				ATRIBUTO_DB_INFORMACAO +" = '"+informacao+"'", null, null, null, null);

		while(c.moveToNext()){
			
			Log.d("Debug getEvento(String informacao): ", 
					"EventoId: "+c.getString(c.getColumnIndex(ATRIBUTO_DB_ID))
					+"| EventoNome: "+c.getString(c.getColumnIndex(ATRIBUTO_DB_NOME)) 
					+"| EventoInformação: "+c.getString(c.getColumnIndex(ATRIBUTO_DB_INFORMACAO))
					+"| EventoLatitude: "+c.getString(c.getColumnIndex(ATRIBUTO_DB_LATITUDE))
					+"| EventoLongitude: "+c.getString(c.getColumnIndex(ATRIBUTO_DB_LONGITUDE))
			        );
			
			Evento eventoRetorno = new Evento( 
					c.getInt(c.getColumnIndex(ATRIBUTO_DB_ID)), 
					Integer.parseInt(  c.getString(c.getColumnIndex(ATRIBUTO_DB_LATITUDE)) ), 
					Integer.parseInt(  c.getString(c.getColumnIndex(ATRIBUTO_DB_LONGITUDE)) ),
					c.getString(c.getColumnIndex(ATRIBUTO_DB_NOME)),
					c.getString(c.getColumnIndex(ATRIBUTO_DB_INFORMACAO)),
					tipoEvService.getTipoEvento( c.getInt(c.getColumnIndex(ATRIBUTO_DB_ID_TIPO_EVENTO) ) ),
					//new TipoEvento(c.getInt(c.getColumnIndex(ATRIBUTO_DB_ID_TIPO_EVENTO) ) ),
					c.getString(c.getColumnIndex(ATRIBUTO_DB_DATA)),
					c.getString(c.getColumnIndex(ATRIBUTO_DB_HORA))
					);
			
			//c.close();
			return eventoRetorno;				
		}
		
		return null;		
	}
	
	/**
	 * Método que obtém único evento contido no banco que tenha o 'id' passado por parâmetro
	 * @param String informacao
	 * @return Evento
	 */
	public Evento getEvento(int id){
		
		tipoEvService = new TipoEventoService(this.db);
		Cursor c = db.query(TABELA_EVENTO, new String[]{ATRIBUTO_DB_ID, ATRIBUTO_DB_LATITUDE, ATRIBUTO_DB_LONGITUDE, ATRIBUTO_DB_NOME, ATRIBUTO_DB_INFORMACAO, ATRIBUTO_DB_ID_TIPO_EVENTO, ATRIBUTO_DB_DATA, ATRIBUTO_DB_HORA},
				ATRIBUTO_DB_ID +" = "+id, null, null, null, null);
		
		while(c.moveToNext()){
			
			Log.d("Debug getEvento(String informacao): ", 
					"EventoId: "+c.getString(c.getColumnIndex(ATRIBUTO_DB_ID))
					+"| EventoNome: "+c.getString(c.getColumnIndex(ATRIBUTO_DB_NOME)) 
					+"| EventoInformação: "+c.getString(c.getColumnIndex(ATRIBUTO_DB_INFORMACAO))
					+"| EventoLatitude: "+c.getString(c.getColumnIndex(ATRIBUTO_DB_LATITUDE))
					+"| EventoLongitude: "+c.getString(c.getColumnIndex(ATRIBUTO_DB_LONGITUDE))
			        );
			
			Evento eventoRetorno = new Evento( 
					c.getInt(c.getColumnIndex(ATRIBUTO_DB_ID)), 
					Integer.parseInt(  c.getString(c.getColumnIndex(ATRIBUTO_DB_LATITUDE)) ), 
					Integer.parseInt(  c.getString(c.getColumnIndex(ATRIBUTO_DB_LONGITUDE)) ),
					c.getString(c.getColumnIndex(ATRIBUTO_DB_NOME)),
					c.getString(c.getColumnIndex(ATRIBUTO_DB_INFORMACAO)),
					tipoEvService.getTipoEvento( c.getInt(c.getColumnIndex(ATRIBUTO_DB_ID_TIPO_EVENTO) ) ),
					//new TipoEvento(c.getInt(c.getColumnIndex(ATRIBUTO_DB_ID_TIPO_EVENTO) ) ),
					c.getString(c.getColumnIndex(ATRIBUTO_DB_DATA)),
					c.getString(c.getColumnIndex(ATRIBUTO_DB_HORA))
					);
			
			//c.close();
			return eventoRetorno;				
		}
		
		return null;
	}
	

	


	
	
	
}
