package fr.ouest_insa.network;

import java.io.IOException;
import java.io.Reader;

import org.json.JSONException;

public abstract class Retrieve<E> implements Runnable {
	public static final String API_URL = "http://api.ouest-insa.fr/";
	public static final String API_URL_GET_STUDIES = API_URL + "study";

	private String url;
	private Object result = null;

	public Retrieve(String url) {
		this.url = url;
	}

	@Override
	public void run() {
		try {
			result = getFromJSON(readJsonFromUrl(url));
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	protected abstract E readJsonFromUrl(String url) throws IOException, JSONException;

	protected String readAll(Reader rd) throws IOException {
		StringBuilder sb = new StringBuilder();
		int cp;
		while ((cp = rd.read()) != -1) {
			sb.append((char) cp);
		}
		return sb.toString();
	}

	abstract protected Object getFromJSON(E json);

	public Object getResult() {
		return result;
	}
}
