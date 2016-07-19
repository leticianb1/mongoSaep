package br.ufg.inf.es.saep.sandbox.mongo;

import java.util.ArrayList;
import java.util.List;

import br.ufg.inf.es.saep.sandbox.dominio.Nota;
import br.ufg.inf.es.saep.sandbox.dominio.Parecer;
import br.ufg.inf.es.saep.sandbox.dominio.Pontuacao;
import br.ufg.inf.es.saep.sandbox.dominio.Valor;

//Para iniciar o serviço do mongo seguir os passos: https://docs.mongodb.com/manual/tutorial/install-mongodb-on-windows/

public class ExemploMain {

	public static void main(String[] args) {
		
		String id = "11";
		String resolucaoId = "resolucao2015";
		
		String radocId = "radoc da lets";
		List<String> radocsIds = new ArrayList<String>();
		radocsIds.add(radocId);
		
		String atributoPontuado = "nome";
		
		String valorPontuado = "3256";
		Valor valor = new Valor(valorPontuado);
		Pontuacao pontuacao = new Pontuacao(atributoPontuado, valor);
		
		List<Pontuacao> pontuacoes = new ArrayList<Pontuacao>();
		pontuacoes.add(pontuacao);
		
		String fundamentacao = "Gostei e pontuei";
		
		List<Nota> notas = new ArrayList<>();
		
		Pontuacao pontuacaoModificada = new Pontuacao(atributoPontuado, new Valor("Leticia Nunes"));
		Nota nota = new Nota(pontuacao, pontuacaoModificada, "Q tipo de nome é 3256:");
		notas.add(nota);
		
		Parecer parecer02 = new Parecer(id, resolucaoId, radocsIds, pontuacoes, fundamentacao, notas);
		System.out.println("\n parecer02");
		System.out.println("ID " +parecer02.getId());
		System.out.println("Resolucao " +parecer02.getResolucao());
		for (Pontuacao pont : parecer02.getPontuacoes()) {
			System.out.println("Atributo da pontuacao" +pont.getAtributo());
			System.out.println("valor da pontuacao"+pont.getValor().getString());
		}
		for (String radocDaListaId : parecer02.getRadocs()) {
			System.out.println("Radoc "+radocDaListaId);
		}
		System.out.println("Fundamentacao "+parecer02.getFundamentacao());
		
		ParecerRepositoryMongo parecerDao = new ParecerRepositoryMongo();
		
		parecerDao.persisteParecer(parecer02);
		
		Parecer parecer03 = parecerDao.byId(id);
//		System.out.println("\n parecer03");
//		System.out.println("ID " +parecer03.getId());
//		System.out.println("Resolucao " +parecer03.getResolucao());
//		for (Pontuacao pont : parecer03.getPontuacoes()) {
//			System.out.println("Atributo da pontuacao" +pont.getAtributo());
//			System.out.println("valor da pontuacao"+pont.getValor().getString());
//		}
//		for (String radocDaListaId : parecer03.getRadocs()) {
//			System.out.println("Radoc "+radocDaListaId);
//		}
//		System.out.println("Fundamentacao "+parecer03.getFundamentacao());
//	
		
//		parecerDao.removeParecer(id);
//		parecer03 = parecerDao.byId(id);
		
		
//		ResolucaoRepositoryMongo resolucaoDao = new ResolucaoRepositoryMongo();
//		String tipoId = "01";
//				Set<Atributo> atributos = new HashSet<Atributo>();
//				Atributo atributo = new Atributo("nome", "qualquer coisa", 2);
//				atributos.add(atributo);
//				atributo = new Atributo("cargaHoraria", "hrs", 2);
//				atributos.add(atributo);
//				
//		Tipo tipo = new Tipo(tipoId, "docente", "atributos do docente", atributos);
//		resolucaoDao.persisteTipo(tipo);
//		
//		
//		int anoBase = 2005;
//		String tipoID = resolucaoDao.tipoPeloCodigo(tipoId).getId();
//		Map<String, Valor> valores = new HashMap<String, Valor>();
//		
//		for (Atributo atributo2 : resolucaoDao.tipoPeloCodigo(tipoId).getAtributos()) {
//			Valor value = new Valor("Leticia Nunes");
//			valores.put("nome", value);
//			value = new Valor("40");
//			valores.put("cargaHoraria", value);
//		}
//		
//		
//		Relato relato = new Relato(tipoID, valores);
//		
//		List<Relato> relatos = new ArrayList<Relato>();
//		relatos.add(relato);
//		radocId = "01";
//		Radoc radoc = new Radoc(radocId, anoBase, relatos);
//		
//		parecerDao.persisteRadoc(radoc);
	}
	}