package br.ufg.inf.es.saep.sandbox.mongo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import br.ufg.inf.es.saep.sandbox.dominio.Regra;
import br.ufg.inf.es.saep.sandbox.dominio.Resolucao;

public class ExempoResolucaoMain {
	public static void main(String[] args) {
		
		String id = "01";
		String nome = "Resolucao123";
		String descricao = "ResolucaoMaraVilhosa";
		Calendar cal = Calendar.getInstance();
		cal.set(2016, Calendar.JANUARY, 15, 0, 0, 0);
		
		Date dataAprovacao = cal.getTime();
		
		String variavel = "pontos";
		int tipo = Regra.PONTOS;
		String descricaoDaRegra = "RegraDaora";
		float valorMaximo = 100;
		float valorMinimo = 0;
		
		String expressao = "";
		String entao = "";
		String senao = "";
		String tipoRelato = "artigo";
		float pontosPorItem = 10;
		List<String> dependeDe = new ArrayList<String>();
		
		Regra regra = new Regra(variavel, tipo, descricaoDaRegra, valorMaximo, valorMinimo, expressao, entao, senao, tipoRelato, pontosPorItem, dependeDe);
		List<Regra> regras = new ArrayList<Regra>();
		
		regras.add(regra);
		
		variavel = "condicional";
		tipo = Regra.CONDICIONAL;
		descricaoDaRegra = "RegraDaoraCondicional";
		valorMaximo = 100;
		valorMinimo = 0;
		
		expressao = "uma regra qualquer";
		entao = "faz essa coisa se der certo";
		senao = "faz essa coisa se der ERRADO";
		tipoRelato = "pesquisa";
		pontosPorItem = 10;
		dependeDe = new ArrayList<String>();
		dependeDe.add("essa coisa");
		
		regra = new Regra(variavel, tipo, descricaoDaRegra, valorMaximo, valorMinimo, expressao, entao, senao, tipoRelato, pontosPorItem, dependeDe);
		regras.add(regra);
		
		Resolucao resolucao = new Resolucao(id, nome, descricao, dataAprovacao, regras);
		
		ResolucaoRepositoryMongo resolucaoRepositoryMongo = new ResolucaoRepositoryMongo();
		
		resolucaoRepositoryMongo.remove(id);
		
		resolucaoRepositoryMongo.persiste(resolucao);
		
		resolucaoRepositoryMongo.byId(id);
		
		resolucaoRepositoryMongo.tipoPeloCodigo("01");
		
		
for (String regra2 : resolucaoRepositoryMongo.resolucoes()) {
	System.out.println(regra2);
}
		
	}
}
