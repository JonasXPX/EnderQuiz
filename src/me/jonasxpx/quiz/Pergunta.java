package me.jonasxpx.quiz;

import java.util.ArrayList;
import java.util.Collection;

public class Pergunta {

	private final Collection<String> respostaList;
	private final String resposta;
	private final String pergunta;
	
	public Pergunta(String pergunta){
		respostaList = new ArrayList<String>();
		if(!pergunta.contains(":"))
			throw new NullPointerException("Pergunta mal formada, faltando o \":\"");
		String[] format = pergunta.split(":");
		this.pergunta = format[0];
		this.resposta = format[1].split(",")[0];
		for(String string : format[1].split(",")){
			respostaList.add(string.toLowerCase());
		}
	}
	
	public String getPergunta(){
		return pergunta;
	}
	
	public Collection<String> getRespostaList(){
		return respostaList;
	}
	
	public String getResposta(){
		return resposta;
	}

}
