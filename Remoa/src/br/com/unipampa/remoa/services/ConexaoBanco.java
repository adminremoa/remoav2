package br.com.unipampa.remoa.services;

import java.io.Serializable;

import android.database.sqlite.SQLiteDatabase;


/**
 * @author Ricardo Burg Machado, Alencar Machado
 * @version 2.0
 * @since 2012
 * Classe que implementa Serializable que faz a cria��o de uma conex�o com o banco de dados
 */
public class ConexaoBanco implements Serializable{

	public static SQLiteDatabase sQLiteDatabase;
	 
	/**
	 * M�todo que atribui seu par�metro a vari�vel est�tica do tipo SQLiteDatabase
	 * @param sqlBase
	 */
	public ConexaoBanco(SQLiteDatabase sqlBase){
		this.sQLiteDatabase = sqlBase;
	}
	
	 
	
}
