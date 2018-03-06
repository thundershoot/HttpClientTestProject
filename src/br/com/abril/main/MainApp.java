package br.com.abril.main;

import java.io.IOException;

import br.com.abril.jwt.Conexao;

public class MainApp {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {

			String privateKey = "...";
			String token = "...";
			String url = "...";

			Conexao conexao = new Conexao();
			conexao.setPrivateKey(privateKey);
			conexao.setToken(token);
			conexao.setURL(url);

			String retornoConexao = conexao.conectar("/rest/banca/movimento/6389563/2018-03-02/2018-03-02", "", "GET");
			System.out.println(retornoConexao);

			// LocalDateTime dataTime = LocalDateTime.now();
			// dataTime = dataTime.plusMinutes(5);
			//
			// JSONObject payload = new JSONObject();
			// payload.put("token", "fea6261d13e6dd2911ac8aa11f90d3fa");
			// payload.put("exp",
			// dataTime.atZone(ZoneId.systemDefault()).toEpochSecond());

		} catch (IOException ex) {
			// handle exception
			ex.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
