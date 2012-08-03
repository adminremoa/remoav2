package br.com.unipampa.remoa.beans;

import java.util.ArrayList;
/**
 * @author Ricardo Burg Machado, Alencar Machado
 * @version 2.0
 * @since 2012
 * Classe que contém todas as características de um evento e seus métodos acessores e modificadores
 */

public class Evento {
	
	private int idEvento;
	private int latitude;
	private int longitude;
	private String informacao;
	private TipoEvento tipo;
	private String nome;
	
	private int qtdConcordar;
	
	public int getQtdConcordar() {
		return qtdConcordar;
	}

	public void setQtdConcordar(int qtdConcordar) {
		this.qtdConcordar = qtdConcordar;
	}

	public int getQtdDiscordar() {
		return qtdDiscordar;
	}

	public void setQtdDiscordar(int qtdDiscordar) {
		this.qtdDiscordar = qtdDiscordar;
	}

	private int qtdDiscordar;
	
	private String data;
	private String hora;
	
	private ArrayList<Voto> votos = new ArrayList<Voto>();
	
	/**
	 * Método construtor sem nenhuma ação interna
	 */
	public Evento(){  }
	
	
	
	/**
	 * Método construtor que faz a atribuição dos parâmetros a seus atributos globais.
	 * Utilizado na criação de um evento com dados vindos da base de dados.
	 * @param int idEvento
	 * @param int latitude
	 * @param int longitude
	 * @param String nomeEvento
	 * @param String informacao
	 * @param TipoEvento tipo
	 * @param String data
	 * @param String hora
	 */
	public Evento(int idEvento, int latitude, int longitude, String nomeEvento, String informacao, 
			TipoEvento tipo, String data, String hora){
		
		setIdEvento(idEvento);
		setLatitude(latitude);
		setLongitude(longitude);
		setInformacao(informacao);
		setTipo(tipo);
		setNome(nomeEvento);
		setData(data);
		setHora(hora);
	}
	
	
	
	/**
	 * Método construtor que faz a atribuição dos parâmetros a seus atributos globais, cujo mesmo NÃO possui o atributo idEvento do tipo 'int'.
	 * Utilizado na criação de um Evento para ser inserido na base de dados, pois então não se sabe o 'id' identificador do novo evento 
	 * @param latitude
	 * @param longitude
	 * @param nomeEvento
	 * @param informacao
	 * @param tipo
	 * @param data
	 * @param hora
	 */
	public Evento(int latitude, int longitude, String nomeEvento, String informacao, TipoEvento tipo, String data, String hora){
			
			setLatitude(latitude);
			setLongitude(longitude);
			setInformacao(informacao);
			setTipo(tipo);
			setNome(nomeEvento);
			setData(data);
			setHora(hora);
	}
	
	
	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getHora() {
		return hora;
	}

	public void setHora(String hora) {
		this.hora = hora;
	}

	
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	
	public void setVoto(Voto voto){
		
		this.votos.add(voto);		
	}
	
	public ArrayList<Voto> getVotos(){
		
		return this.votos;
	}
	

	public int getIdEvento() {
		
		return idEvento;
	}

	public void setIdEvento(int idEvento) {
		
		this.idEvento = idEvento;
	}

	public void setLatitude(int latitude){
		
		this.latitude = latitude;
	}
	
	public int getLatitude(){
		
		return this.latitude;
	}

	public void setLongitude(int longitude){
		
		this.longitude = longitude;
	}
	
	public int getLongitude(){
		
		return this.longitude;
	}

	public void setInformacao(String informacao){
		
		this.informacao = informacao;
	}
	
	public String getInformacao(){
		
		return this.informacao;
	}
	
	public void setTipo(TipoEvento tipo){
		
		this.tipo = tipo;
	}
	
	public TipoEvento getTipo(){
		
		return this.tipo;
	}
	
}
