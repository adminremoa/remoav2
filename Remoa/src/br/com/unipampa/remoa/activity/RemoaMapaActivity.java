package br.com.unipampa.remoa.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import br.com.unipampa.remoa.R;
import br.com.unipampa.remoa.beans.Evento;
import br.com.unipampa.remoa.beans.TipoEvento;
import br.com.unipampa.remoa.services.ConexaoBanco;
import br.com.unipampa.remoa.services.EventoService;
import br.com.unipampa.remoa.services.TipoEventoService;
import br.com.unipampa.remoa.services.VotoService;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.OverlayItem;

/**
 * @author Ricardo Burg Machado, Alencar Machado
 * @version 2.0
 * @since 2012
 * Classe que extende MapActicity e implementa LocationListener, é chamada após o usuário estar logado.
 *
 */
public class RemoaMapaActivity extends MapActivity  implements LocationListener{

	private static final int INSERT_ID = Menu.FIRST;
	private static final int LISTAR_ID = Menu.FIRST + 1;
	private static final int VOLTAR_ID = Menu.FIRST + 2;
	private EventoService eventoService;
	private TipoEventoService tipoEventoService;
	private List mapOverlays;
	private MapView mapView;
	private MapController controleMapa;
	private MyLocationOverlay myLocationOverlay;
	private LocalizacaoGPS localizacaoGPS;
 
	
	/**
	 * Este método executa as seguintes ações:
	 * - inicialização do mapa;
	 * - carregamento do overlay na posição (GPS) atual do usuário;
	 * - inicializa uma conexão com base de dados
	 * - inicializa a classe eventoService 
	 * - inicializa a classe tipoEventoService a qual tem em seu construtor uma verificação importante  para o cadastro de eventos
	 * - inicializa a classe localizacaoGPS para disponibilizar a localização do usuário 
	 * - executa o método inicializaEventos para inicializar todos os eventos contidos na base de dados
	 */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remoa_mapa);
 
        new ConexaoBanco(this.openOrCreateDatabase("bancoremoa", Context.MODE_PRIVATE, null));
        eventoService = new EventoService(ConexaoBanco.sQLiteDatabase);
        tipoEventoService = new TipoEventoService(ConexaoBanco.sQLiteDatabase);	   
        
        mapView = (MapView) findViewById(R.id.mapa);
        controleMapa = mapView.getController();
        controleMapa.setZoom(15);
        mapView.setBuiltInZoomControls(true);        		
		mapOverlays = mapView.getOverlays();	      
	    
		insereOverlayLocalizacaoGPS();
	    localizacaoGPS = new LocalizacaoGPS(this, mapView);
	    
	    inicializaEventos();

	    
    }

    @Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
     
    /**
     * Cria o menu de opções(Cadastrar novo evento) que é ativado ao clicar no botão menu do dispositivo/emulador
     * @param Menu menu
     * @return boolean true
     */
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_remoa_mapa, menu);
        boolean result = super.onCreateOptionsMenu(menu);
		menu.add(0, INSERT_ID, 0, "ADD EVENTO");
		return result;
    }

   
    
    /**
     * Faz o gerenciamento das ações do menu 
     * @param MenuItem menuItem
     * @param int featureId
     * @return boolean
     */
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		
		switch (item.getItemId()) {
			case INSERT_ID:
				addEvento1();
				return true;
				
			case LISTAR_ID:
				return true;
				
			case VOLTAR_ID:
				finish();
				return true;
		}
		
		return super.onMenuItemSelected(featureId, item);
	}

	/**
	 * Método que chama AddEventoDialog que representa um caixa de diálogo para cadastrar novo evento
	 * @param Context context
	 * @param List mapOverlays
	 * @param MapView mapView
	 * @param GeoPoint geoPoint
	 * @return void
	 */
	private void addEvento1(){
		AddEventoDialog adEvD = new AddEventoDialog(this, mapOverlays, mapView, localizacaoGPS.getGeoPointGPS() );
		adEvD.show();
	}
	
	/**
     * Este método executa as seguintes ações:
     * - preenche um array de objetos do tipo Evento com todos os eventos contido no banco de dados;
     * - percorre este array executando ações para cada objeto:
     *   - verifica qual o tipo/classificação do evento;
     *   - cria um icone do tipo Drawble que corresponde ao tipo obtido anteriormente;
     *   - cria um objeto do tipo OverlayItemized que será utilizado para representar o evento no mapa que tem como um de seus parâmetros o ícone criado acima;
     *   - cria um objeto do tipo GeoPoint que contém como parâmetro a latitude e longitude que são obtidas do objeto do tipo Evento
     *   - cria um objeto do tipo OverlayItem que tem como um de seus parâmetros o GeoPoint criado anteriormente
     *   - executa o método addOverlay que tem como um de seus parâmetros o OverlayItem criado anteriormente  
     *   - adiciona o OverlayItemized ao List mapOverlays que representa os eventos contido no mapa, atualiza o mapa com este novo evento e 
     *   centraliza o mapa na localização deste evento.
     * 
     * @return void
     */
	public void inicializaEventos(){
		
		ArrayList<Evento> eventosBanco = eventoService.getEventos();
	
		for (Evento evento : eventosBanco) {
			
			Drawable icone;
			if(evento.getTipo().getDescricao().equalsIgnoreCase("SAÚDE")){
				icone = getResources().getDrawable(R.drawable.saude);									
			}else if(evento.getTipo().getDescricao().equalsIgnoreCase("TRÂNSITO")){
				icone = getResources().getDrawable(R.drawable.transito);
			}else if(evento.getTipo().getDescricao().equalsIgnoreCase("SEGURANÇA")){
				icone = getResources().getDrawable(R.drawable.seguranca);
			}else if(evento.getTipo().getDescricao().equalsIgnoreCase("ENTRETENIMENTO")){
				icone = getResources().getDrawable(R.drawable.entretenimento);
			}else{
				icone = getResources().getDrawable(R.drawable.app_icon);
			}

		
			OverlayItemized itemizedoverlay = new OverlayItemized( icone , this, ConexaoBanco.sQLiteDatabase);
			GeoPoint gp = new GeoPoint( evento.getLatitude() , evento.getLongitude() );		
			OverlayItem overlayitem1 = new OverlayItem(gp, null, null);
			
			itemizedoverlay.addOverlay(overlayitem1, icone, evento , this);
			mapOverlays.add(itemizedoverlay);
			MapController mc = mapView.getController();
			mc.animateTo(gp);
			mc.setZoom(14);
			mc.setCenter( gp );
	        mapView.invalidate();
		}
	}

	/**
     * Método reponsável de inserir um (ícone padrão) do tipo MyLocationOverlay que representa a localização atual do usuário (obtida pelo GPS)
     * a execução deste método é automática, ou seja, a cada mudança de localização do usuário o ícone é atualizado (muda sua localização) no mapa. 
     * Executa constantemente o método requestLocationUpdates vericando alguma mudança na localização do GPS.
     * @return void
     */
	public void insereOverlayLocalizacaoGPS(){	    	
	    	myLocationOverlay = new MyLocationOverlay(this, mapView);
	 	    mapView.getOverlays().add(myLocationOverlay);	 	    
	 		retornaLocationManager().requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, (LocationListener) this);	    		    
	}
	  
	/**
	 * Método utilizado para retornar a classe LocationManager que fornece acessoa aos serviços de localização do sistema
	 * @return LocationManager
	 */
	private LocationManager retornaLocationManager() {
		LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
	    return locationManager;
	}

	/**
	 * Método que diponibiliza opções ao usuário por meio de teclas do dispositivo as seguintes ações:
	 * tecla 's' -> mostra o mapa no modo satélite
	 * tecla 'r' ou 't' -> mostra o mapa no modo satélite
	 * @param int keyCode
	 * @param KeyEvent event
	 * @return boolean
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_S) {
				mapView.setSatellite(true);
				mapView.setStreetView(false);
				mapView.setTraffic(false);
				return true;
		} else if (keyCode == KeyEvent.KEYCODE_R) {
				mapView.setSatellite(false);
				mapView.setStreetView(true);
				mapView.setTraffic(false);
				return true;
		} else if (keyCode == KeyEvent.KEYCODE_T) {
				mapView.setSatellite(false);
				mapView.setStreetView(false);
				mapView.setTraffic(true);
				return true;
		}

			return super.onKeyDown(keyCode, event);
	}
	    
	public void onLocationChanged(Location location) {
			// TODO Auto-generated method stub
	}

	public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub
	}

	public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub
	}

	public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub
	}
		
	@Override
	protected void onResume(){
		super.onResume();
		myLocationOverlay.enableMyLocation();
		inicializaEventos();
	}
		    
	@Override
	protected void onPause(){
		
		super.onPause();
		myLocationOverlay.disableMyLocation();
	}
		
		
		
		
}
