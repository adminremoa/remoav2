package br.com.unipampa.remoa.beans;

import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;


/**
 * @author Ricardo Burg Machado, Alencar Machado
 * @version 2.0
 * @since 2012
 * Classe utilizada para obtenção da data e hora atual 
 * Observação: o horário obtido é baseado no Locale "pt_BR"
 */
public class DataHora {

	private static GregorianCalendar calendar = new GregorianCalendar(TimeZone.getTimeZone("GMT-3"),new Locale("pt_BR")); 

	public DataHora(){ }
	
	/**
	 * Método estático que retorna a data atual no formato ( "dd'/'MM'/'yyyy" ); exemplo: 03/08/2012
	 * @return String dataAtual
	 */
	public static String getDataAtual(){

		SimpleDateFormat formatador = new SimpleDateFormat("dd'/'MM'/'yyyy",new Locale("pt_BR")); 
		formatador.setTimeZone( TimeZone.getTimeZone("GMT-03:00"));  
		String dataAtual =formatador.format(calendar.getTime()).toString();
		return dataAtual;
	}
	
	/**
	 * Método estático que retorna a hora atual no formato ( "HH':'mm" ); exemplo:  14:52
	 * @return String horaAtual
	 */
	public static String getHoraAtual(){
		
		SimpleDateFormat formatadorHora = new SimpleDateFormat("HH':'mm",new Locale("pt_BR")); 
		formatadorHora.setTimeZone( TimeZone.getTimeZone("GMT-03:00"));  
		String horaAtual = formatadorHora.format(calendar.getTime()).toString();
		return horaAtual;
	}
	
}
