package br.com.unipampa.remoa.beans;

/**
 * @author Ricardo Burg Machado, Alencar Machado
 * @version 2.0
 * @since 2012
 * Classe que contém todas as características/atributos de um Tipo de evento (classificação) e seus métodos acessores e modificadores
 */
public class TipoEvento {
	
	private int id;
	private String descricao;
	
	/**
	 * Método construtor vazio sem nenhuma ação
	 */
	public TipoEvento(){	}
	
	/**
	 * Método construtor que faz a atribuição de seus parâmetros as variáveis globais
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
