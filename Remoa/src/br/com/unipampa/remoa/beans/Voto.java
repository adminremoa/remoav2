package br.com.unipampa.remoa.beans;

/**
 * @author Ricardo Burg Machado, Alencar Machado
 * @version 2.0
 * @since 2012 
 * Classe que cont�m todas as caracter�sticas/atributos de um Voto e seus m�todos acessores e modificadores
 */
public class Voto {
	
	private int id;
	private String voto;
	private int idEvento;
	
	
	/**
	 * M�todo construtor vazio sem nenhuma execu��o interna
	 */
	public Voto(){}
	
	/**
	 * M�todo construtor que faz a atribui��o de seus par�metros as vari�veis globais
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
