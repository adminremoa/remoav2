package br.com.unipampa.remoa.activity;


import java.util.List;

import br.com.unipampa.remoa.beans.*;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

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
* Classe responsável por disponibilizar as coordenadas (latitude/longitude) do usuário
*/
public class LocalizacaoGPS  {
	
	
	private LocationManager locationManager;
	private Context context;
	private MapView mapView;
	private LocationListener locationLinstener;
	private MapController controleMapa;
	private GeoPoint geoPointGPS;
	
	public LocalizacaoGPS (Context context, MapView mapView){
		
		this.context = context;
		this.mapView = mapView;
		
		controleMapa = this.mapView.getController();
	    locationManager = (LocationManager) this.context.getSystemService(Context.LOCATION_SERVICE);
	    locationLinstener = new NovaLocalizacao();
	    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationLinstener);
	}
	
	
	
	    
	/**
	 * Método utilizado para conversão dos parâmetros em um novo GeoPoint
	 * @param double lat
	 * @param double lng
	 * @return GeoPoint geoPoint
	 */
	public static GeoPoint posicao(double lat, double lng){
    	
        GeoPoint geoPoint = new GeoPoint(		
        		(int) (lat * 1E6),
        		(int) (lng * 1E6));

        return geoPoint;    	
    }
	
	/**
	 * Método de acesso ao GeoPoint
	 * @return GeoPoint
	 */
	public GeoPoint getGeoPointGPS() {
		 
		return geoPointGPS;
	}

	/**
	 * Método modificador do GeoPoint
	 * @param double lat
	 * @param double lng
	 */
	public void setGeoPointGPS(double lat, double lng) {
			
		GeoPoint geoPoint = new GeoPoint((int) (lat * 1E6),(int) (lng * 1E6));
		this.geoPointGPS = geoPoint;	
	}

	
	class NovaLocalizacao extends MapActivity implements LocationListener {
    	

		/**
		 * Método chamado toda vez que uma localização for alterada.
		 * Verifica se a localização não é nula, caso se confirme atribui novos valores ao GeoPoint (lat/long)
		 * @param Location location
		 * @return void
		 */
		public void onLocationChanged(Location location) {
			
			if(location != null){	
				
				setGeoPointGPS(location.getLatitude(), location.getLongitude());
				controleMapa.animateTo(posicao(location.getLatitude(), location.getLongitude()));
				GeoPoint gp = posicao(location.getLatitude(),location.getLongitude());
				controleMapa.setCenter(gp);
			}	
		}
		
		
		
		//chamado quando um provedor é DEsabilitado pelo usuário
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub
			
		}

		//chamado quando um provedor é HAbilitado pelo usuário
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub
			
		}

		//quando status do provedor for alterado
	 
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub
			
		}



		@Override
		protected boolean isRouteDisplayed() {
			// TODO Auto-generated method stub
			return false;
		}
		
    	
	    @Override
	    protected void onDestroy(){
	    	
	    	super.onDestroy();
	    	locationManager.removeUpdates(this);
	    }
	    
	    protected boolean enableMyLocation(){
	    	
	    	return true;
	    }
	    
	    
    }
	
	
	
}
