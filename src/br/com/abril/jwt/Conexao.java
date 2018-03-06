package br.com.abril.jwt;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.crypto.spec.SecretKeySpec;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class Conexao {
	private String url;
	private String token;
	private String privatekey;

	public String conectar(String path, String dados, String metodo) throws Exception {
		if (dados == null)
			dados = "";

		dados = new String(dados.getBytes(), "UTF-8");
		SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

		SecretKeySpec privateKey = new SecretKeySpec(
				javax.xml.bind.DatatypeConverter.parseBase64Binary(this.privatekey), "HmacSHA256");

		JsonObject payload = new JsonObject();
		payload.addProperty("token", token);

		JwtBuilder builder = Jwts.builder();
		builder.signWith(signatureAlgorithm, privateKey);
		builder.setPayload(payload.toString());

		String authorization = builder.compact();
		URL url = new URL(this.url + path);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();

		conn.setRequestProperty("Authorization", "Bearer " + authorization);
		conn.setRequestProperty("Content-Type", "application/json");
		conn.setRequestProperty("Accept-Charset", "UTF-8");
		conn.setRequestProperty("Content-Length", String.valueOf(dados.getBytes().length));
		conn.setRequestMethod(metodo);

		if ("POST".equals(metodo)) {
			conn.setDoOutput(true);
			OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
			writer.write(dados);
			writer.flush();
		}

		boolean erro = false;
		InputStream in = null;
		System.out.println(conn.getResponseCode());
		if (conn.getResponseCode() == 200) {
			in = conn.getInputStream();
		} else {
			erro = true;
			in = conn.getErrorStream();
		}

		String strResponse = "";
		BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
		String line;

		while ((line = br.readLine()) != null)
			strResponse += line;

		if (erro) {
			if (strResponse.contains("\"erro\":")) {
				Gson o = new Gson();
				JsonObject objErro = o.fromJson(strResponse, JsonObject.class);

				throw new Exception(objErro.get("erro").getAsString());
			} else {
				throw new Exception(strResponse);
			}
		}

		return strResponse;
	}

	public void setURL(String url) {
		this.url = url;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public void setPrivateKey(String privateKey) {
		this.privatekey = privateKey;
	}
}
