package br.com.unipampa.remoa.activity;


import java.util.List;

/**
 * @author Ricardo Burg Machado, Alencar Machado
 * @version 2.0
 * @since 2012
 *
 */


import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

import br.com.unipampa.remoa.R;
import br.com.unipampa.remoa.beans.DataHora;
import br.com.unipampa.remoa.beans.Evento;
import br.com.unipampa.remoa.beans.TipoEvento;
import br.com.unipampa.remoa.services.ConexaoBanco;
import br.com.unipampa.remoa.services.EventoService;
import br.com.unipampa.remoa.services.TipoEventoService;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

/**
 * @author Ricardo Burg Machado, Alencar Machado
 * @version 2.0
 * @since 2012
 * Classe que extende AlertDialog responsável pela criação de uma caixa de diálogo para cadastro de novo evento e pela manipulação dos eventos/ações ocorridas dentro da caixa de diálogo
 *
 */
public class AddEventoDialog extends AlertDialog {

	private EditText edNome;
	private EditText edDescricao;	
	private Spinner spinnerCategorias;
	private ArrayAdapter<CharSequence> adapter;
	private List mapOverlays;
	private MapView mapView;
	private GeoPoint geoPointGPS;
	private View promptsView;
	private TipoEventoService tipoEventoService;
	
	/**
	 * Método construtor que faz toda a construção da interface da caixa de diálogo e gerenciamento de ações com o usuário
	 * @param context
	 * @param mapOverlays
	 * @param mapView
	 * @param geoPointGPS
	 */
	public AddEventoDialog(Context context , List mapOverlays , MapView mapView,  GeoPoint geoPointGPS) {
		super(context);
		this.mapOverlays = mapOverlays;
		this.geoPointGPS = geoPointGPS;
		this.mapView = mapView;
		
		
		setTitle("CADASTRAR EVENTOS");
		setIcon(R.drawable.ic_launcher);
		LayoutInflater li = LayoutInflater.from(context);
		promptsView = li.inflate(R.layout.addeventos, null);
		setView(promptsView);
		
		edNome = (EditText) promptsView.findViewById(R.id.edNome);		
		
		edDescricao = (EditText) promptsView.findViewById(R.id.edDescricao);		 
		
		preencheSpinnerCategorias();
	    
				
		setCancelable(false);
		setButton("OK", new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int id) {
				
				Context context = getContext();
				List mapOverlays = AddEventoDialog.this.mapOverlays;
				MapView mapView  = AddEventoDialog.this.mapView;
				GeoPoint geoPointGPS = AddEventoDialog.this.geoPointGPS;


				
				EventoService eventoService = new EventoService(ConexaoBanco.sQLiteDatabase);

				EditText editTextNomeEvento = (EditText) promptsView.findViewById(R.id.edNome);
				
				EditText editTextDescricaoEvento = (EditText) promptsView.findViewById(R.id.edDescricao);
				 				
				String categoriaSelecionada = spinnerCategorias.getSelectedItem().toString();
				
				TipoEvento tipoEvento = new TipoEvento();
				
				Drawable icone;
				if(categoriaSelecionada.equalsIgnoreCase("SAÚDE")){
					icone = context.getResources().getDrawable(R.drawable.saude);		
					tipoEvento.setId( tipoEventoService.getTipoEvento(categoriaSelecionada).getId() );
					tipoEvento.setDescricao("SAÚDE");
				}else if(categoriaSelecionada.equalsIgnoreCase("TRÂNSITO")){
					icone = context.getResources().getDrawable(R.drawable.transito);
					tipoEvento.setId( tipoEventoService.getTipoEvento(categoriaSelecionada).getId() );
					tipoEvento.setDescricao("TRÂNSITO");
				}else if(categoriaSelecionada.equalsIgnoreCase("SEGURANÇA")){
					icone = context.getResources().getDrawable(R.drawable.seguranca);
					tipoEvento.setId( tipoEventoService.getTipoEvento(categoriaSelecionada).getId() );
					tipoEvento.setDescricao("SEGURANÇA");
				}else if(categoriaSelecionada.equalsIgnoreCase("ENTRETENIMENTO")){
					icone = context.getResources().getDrawable(R.drawable.entretenimento);
					tipoEvento.setId( tipoEventoService.getTipoEvento(categoriaSelecionada).getId() );
					tipoEvento.setDescricao("ENTRETENIMENTO");
				}else{
					icone = context.getResources().getDrawable(R.drawable.app_icon);
				}
				
				String nomeEvento = editTextNomeEvento.getText().toString();				
				String descricaoEvento = editTextDescricaoEvento.getText().toString();				

				Evento novoEvento = new Evento( geoPointGPS.getLatitudeE6(), geoPointGPS.getLongitudeE6(), nomeEvento, descricaoEvento, tipoEvento, DataHora.getDataAtual(), DataHora.getHoraAtual()  );
				
				
				long retornoCadastro = eventoService.inserir(novoEvento, context); 
				
				if( retornoCadastro > 0 ){
					
					novoEvento.setIdEvento( (int)retornoCadastro );
				
					OverlayItemized itemizedoverlay = new OverlayItemized( icone , context, ConexaoBanco.sQLiteDatabase);
					GeoPoint gp = new GeoPoint(  geoPointGPS.getLatitudeE6() , geoPointGPS.getLongitudeE6() );		
					OverlayItem overlayitem1 = new OverlayItem(gp, null, null);
					itemizedoverlay.addOverlay(overlayitem1, icone, novoEvento , context);
					mapOverlays.add(itemizedoverlay);
					MapController mc = mapView.getController();
					mc.animateTo(gp);
					mc.setZoom(14);
					mc.setCenter( gp );
			        mapView.invalidate();
						
			        //sQLiteDatabase.close();
					dialog.cancel();
				}				
				
				
			}
		});
		
		setButton2("Cancelar", new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				cancel();
			}
		});
		
	}

	/**
	 * Método utilizado apenas para inserir dados (categorias cadastradas) vindas do banco de dados em um comboBox
	 * @return void
	 */
	private void preencheSpinnerCategorias(){
		
		tipoEventoService = new TipoEventoService(ConexaoBanco.sQLiteDatabase);
	    String[] listaCategorias =  new String[ tipoEventoService.getTiposEvento().size() + 1 ];
	    listaCategorias[0] = "<<Selecione>>";
	    for(int i = 0; i < tipoEventoService.getTiposEvento().size(); i++ ){
	    	
	    	listaCategorias[i + 1] = tipoEventoService.getTiposEvento().get(i).getDescricao();
	    }
		
		spinnerCategorias = (Spinner) promptsView.findViewById(R.id.spinner);
        ArrayAdapter<String> todasCategorias; 
        todasCategorias = new ArrayAdapter<String>(AddEventoDialog.this.getContext(), android.R.layout.simple_spinner_item, listaCategorias);
        todasCategorias.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);//muda apenas a apresentação dos dados na tela
        spinnerCategorias.setAdapter(todasCategorias);
	}

	

}
