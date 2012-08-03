package br.com.unipampa.remoa.activity;

import br.com.unipampa.remoa.R;
import br.com.unipampa.remoa.beans.Evento;
import br.com.unipampa.remoa.beans.Voto;
import br.com.unipampa.remoa.services.ConexaoBanco;
import br.com.unipampa.remoa.services.EventoService;
import android.app.AlertDialog;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
/**
 * @author Ricardo Burg Machado, Alencar Machado
 * @version 2.0
 * @since 2012
 * Classe que extende AlertDialog que tem como finalidade a criação de uma caixa de diálogo que mostra dados de um evento ao usuário e opções de votar
 */
public class VotoEventoDialog extends AlertDialog {

	private Evento evento;
	private EventoService eventoService;
	private Context context;
	
	/**
	 * Método construtor que inicializa todos os componentes da interface do usuário mostrando os dados do evento selecionado.
	 * Oferecendo as opções de concordar ou discordarm, também é feito todo o gerenciamento de ações do usuário 'clicks'
	 * @param Context context
	 * @param Evento ev
	 * @param SQLiteDatabase sQLiteDatabase
	 */
	protected VotoEventoDialog(Context context, Evento ev, SQLiteDatabase sQLiteDatabase) {
		super(context);
		
		this.evento = ev;
		this.eventoService = new EventoService(sQLiteDatabase);
		this.context = context;
		
		setTitle("VOTAR EVENTO");
		setIcon(R.drawable.ic_launcher);
		LayoutInflater li = LayoutInflater.from(context);
		View promptsView = li.inflate(R.layout.votoeventos, null);
		setView(promptsView);
		
		TextView tfNome = (TextView)promptsView.findViewById(R.id.tfNome);
		TextView tfTipo = (TextView)promptsView.findViewById(R.id.tfTipo);
		TextView tfDesc = (TextView)promptsView.findViewById(R.id.tfDescricao);
		TextView tfDataHora = (TextView)promptsView.findViewById(R.id.tfDataHora);
		TextView tfQtdDis = (TextView) promptsView.findViewById(R.id.tfQtdDiscordar);
		TextView tfQtdCon = (TextView) promptsView.findViewById(R.id.tfQtdConconcordar);
		
		tfNome.setText(evento.getNome());
		tfTipo.setText(evento.getTipo().getDescricao());
		tfDesc.setText(evento.getInformacao());
		tfDataHora.setText("Evento criado: "+evento.getData()+" às "+evento.getHora()+" horas.");
		tfQtdCon.setText("Votos Conc: "+eventoService.getQtdVotosConcordo( evento.getIdEvento() )+ "");
		tfQtdDis.setText("Votos Disc: "+eventoService.getQtdVotosDiscordo( evento.getIdEvento() )+ "");
		
		Button btConc = (Button) promptsView.findViewById(R.id.btConcordar);
		
		btConc.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {

				Voto voto = new Voto();
				voto.setVoto("Concordo");
				voto.setIdEvento( evento.getIdEvento() );									
				eventoService.votar( voto , VotoEventoDialog.this.context );				
				//apenas Debug				
				eventoService.getVotos();				
				//sQLiteDatabase.close();				
				cancel();
			}
		});
		
		Button btDisc = (Button) promptsView.findViewById(R.id.btDiscordar);
		btDisc.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {

				Voto voto = new Voto();
				voto.setVoto("Discordo");
				voto.setIdEvento( evento.getIdEvento() );									
				eventoService.votar( voto , VotoEventoDialog.this.context);				
				//apenas Debug				
				eventoService.getVotos();				
				//sQLiteDatabase.close();				
				cancel();
			}
		});
	}

}
