package br.com.unipampa.remoa.services;

import java.io.Serializable;

import android.database.sqlite.SQLiteDatabase;


/**
 * @author Ricardo Burg Machado, Alencar Machado
 * @version 2.0
 * @since 2012
 * Classe que implementa Serializable que faz a criação de uma conexão com o banco de dados
 */
public class ConexaoBanco implements Serializable{

	public static SQLiteDatabase sQLiteDatabase;
	 
	/**
	 * Método que atribui seu parâmetro a variável estática do tipo SQLiteDatabase
	 * @param sqlBase
	 */
	public ConexaoBanco(SQLiteDatabase sqlBase){
		this.sQLiteDatabase = sqlBase;
	}
	
	 
	
}
