package br.com.unipampa.remoa.beans;

/**
 * @author Ricardo Burg Machado, Alencar Machado
 * @version 2.0
 * @since 2012
 * Classe que cont�m todas as caracter�sticas/atributos de um Tipo de evento (classifica��o) e seus m�todos acessores e modificadores
 */
public class TipoEvento {
	
	private int id;
	private String descricao;
	
	/**
	 * M�todo construtor vazio sem nenhuma a��o
	 */
	public TipoEvento(){	}
	
	/**
	 * M�todo construtor que faz a atribui��o de seus par�metros as vari�veis globais
	 * @param int id
	 * @param String descricao
	 */
	public TipoEvento(int id, String descricao){
		
		setId(id);
		setDescricao(descricao);
	}
	
	
	
	
	public int getId() {
		
		return id;
	}
	
	public void setId(int id) {
	
		this.id = id;
	}
	
	public String getDescricao() {
		
		return descricao;
	}
	
	public void setDescricao(String descricao) {
		
		this.descricao = descricao;
	}

	

}
