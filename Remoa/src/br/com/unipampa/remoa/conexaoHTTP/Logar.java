package br.com.unipampa.remoa.conexaoHTTP;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class Logar {

	private final String url;
	public Logar(String url) throws IOException{
		this.url = url;
	}
	public boolean logar(String sen, String login) {
		Map params = new HashMap();
		params.put("usuario", login);
		params.put("senha", sen);
		boolean retornoServer = Boolean.parseBoolean(Http.getInstance(Http.JAKARTA).doPost(url, params));
		return retornoServer;
	}
	
}
