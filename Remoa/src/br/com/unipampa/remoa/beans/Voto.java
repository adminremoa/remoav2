package br.com.unipampa.remoa.beans;

/**
 * @author Ricardo Burg Machado, Alencar Machado
 * @version 2.0
 * @since 2012 
 * Classe que contém todas as características/atributos de um Voto e seus métodos acessores e modificadores
 */
public class Voto {
	
	private int id;
	private String voto;
	private int idEvento;
	
	
	/**
	 * Método construtor vazio sem nenhuma execução interna
	 */
	public Voto(){}
	
	/**
	 * Método construtor que faz a atribuição de seus parâmetros as variáveis globais
	 * @param int id
	 * @param String voto
	 * @param int idEvento
	 */
	public Voto(int id, String voto, int idEvento){
		
		setId(id);
		setVoto(voto);
		setIdEvento(idEvento);
	}
	
	public String getVoto() {
		
		return voto;
	}

	public void setVoto(String voto) {
		
		this.voto = voto;
	}

	public int getId() {
		
		return id;
	}

	public void setId(int id) {
		
		this.id = id;
	}
	
	public int getIdEvento() {
		
		return idEvento;
	}

	public void setIdEvento(int idEvento) {
		
		this.idEvento = idEvento;
	}

}
