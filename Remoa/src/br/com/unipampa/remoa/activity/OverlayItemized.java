package br.com.unipampa.remoa.activity;


import java.util.ArrayList;

import br.com.unipampa.remoa.beans.*;


import android.R;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;
 
/**
 * @author Ricardo Burg Machado, Alencar Machado
 * @version 2.0
 * @since 2012
 * Classe que extende ItemizedOverlay que tem como finalidade criar os ícones dentro da Mapa, pois cada ícone representa um evento dentro mapa (MapView)
 */

public class OverlayItemized extends ItemizedOverlay{
	
	
	private ArrayList mOverlays = new ArrayList();
	private Drawable iconeTituloMensagem;
	private String categoriaEvento;
	private String nomeEvento;
	private String descricaoEvento;
	private String data;
	private String hora;
	private SQLiteDatabase sQLiteDatabase;
	private Evento evento;
	private Context context;
	
	/**
	 * Método construtor que recebe entre outros como parâmetro o objeto do tipo 'SQLiteDatabase' que serve para ser repassado como parâmetro ao objeto do tipo <VotoEventoDialog> 
	 * @param Drawable defaultMarker
	 * @param Context context
	 * @param SQLiteDatabase sQLiteDatabase
	 */
	public OverlayItemized(Drawable defaultMarker, Context context, SQLiteDatabase sQLiteDatabase) {
		
		super(boundCenterBottom(defaultMarker));
		this.context = context;
		this.sQLiteDatabase = sQLiteDatabase;		  
	}
 
	
	@Override
	protected OverlayItem createItem(int i) {
		
	  return (OverlayItem) mOverlays.get(i);
	}
	
	/**
	 * Método que tem como finalidade adicionar um novo 'OverlayItem' ao ArrayList
	 * @param OverlayItem overlay
	 * @param Drawable iconeRecebido
	 * @param Evento evento
	 * @param Context context
	 */
	public void addOverlay(OverlayItem overlay, Drawable iconeRecebido, Evento evento, Context context) {
		
		mOverlays.add(overlay);
	    populate();	    
	    this.iconeTituloMensagem = iconeRecebido;
	    this.categoriaEvento = evento.getTipo().getDescricao();
	    this.nomeEvento = evento.getNome();
	    this.descricaoEvento = evento.getInformacao();
	    this.data = evento.getData();
	    this.hora = evento.getHora();
	    this.evento = evento;
	    this.context = context;
	}	
	
	
	@Override
	public int size() {
	  return mOverlays.size();
	}
 
	/**
	 * Método que é acionado toda vez que um <OverlayItem> que está no mapa é clicado, fazendo posteriormente a chamada da caixa de diálogo representada pela classe do tipo <VotoEventoDialog>
	 * @param int index
	 * @return boolean
	 */
	@Override
	protected boolean onTap(int index) {
		
	 	OverlayItem item = (OverlayItem) mOverlays.get(index);
		VotoEventoDialog votoEventoDialog = new VotoEventoDialog(context, evento, sQLiteDatabase);
		votoEventoDialog.show();
		
		return true;
	
	}
	
	
}