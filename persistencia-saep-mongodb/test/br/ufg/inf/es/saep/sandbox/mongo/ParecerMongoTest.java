package br.ufg.inf.es.saep.sandbox.mongo;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import br.ufg.inf.es.saep.sandbox.dominio.Atributo;
import br.ufg.inf.es.saep.sandbox.dominio.CampoExigidoNaoFornecido;
import br.ufg.inf.es.saep.sandbox.dominio.ExisteParecerReferenciandoRadoc;
import br.ufg.inf.es.saep.sandbox.dominio.IdentificadorDesconhecido;
import br.ufg.inf.es.saep.sandbox.dominio.IdentificadorExistente;
import br.ufg.inf.es.saep.sandbox.dominio.Nota;
import br.ufg.inf.es.saep.sandbox.dominio.Parecer;
import br.ufg.inf.es.saep.sandbox.dominio.Pontuacao;
import br.ufg.inf.es.saep.sandbox.dominio.Radoc;
import br.ufg.inf.es.saep.sandbox.dominio.Relato;
import br.ufg.inf.es.saep.sandbox.dominio.Tipo;
import br.ufg.inf.es.saep.sandbox.dominio.Valor;

import org.junit.Assert;
import org.junit.Assert.*;

public class ParecerMongoTest {
	ParecerRepositoryMongo parecerDao;
	ResolucaoRepositoryMongo resolucaoDao;
	
	
	@Before
	public void setUp(){
		parecerDao = new ParecerRepositoryMongo();
		resolucaoDao = new ResolucaoRepositoryMongo();
	} 
	
	@Test
	public void persisteParecerDadosValidos(){
		String parecerId  = "12";
		String resolucaoId = "resolucao2013";
		
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
		
		Parecer parecer02 = new Parecer(parecerId, resolucaoId, radocsIds, pontuacoes, fundamentacao, notas);

		parecerDao.persisteParecer(parecer02);
		
		assertNotNull(parecerDao.byId(parecerId));
	}
	
	@Test(expected=IdentificadorDesconhecido.class)
	public void persisteParecerSemId(){
		String id  ="12";
		String resolucaoId = "";
		
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
		
		ParecerRepositoryMongo parecerDao = new ParecerRepositoryMongo();
		
		parecerDao.persisteParecer(parecer02);
		
	}
	
	@Test (expected=IdentificadorExistente.class)
	public void persisteParecerComIdExistenteNaBase(){
		String id  = "12";
		String resolucaoId = "13";
		
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
		
		ParecerRepositoryMongo parecerDao = new ParecerRepositoryMongo();
		
		parecerDao.persisteParecer(parecer02);
		
		parecerDao.persisteParecer(parecer02);
		
	}
	
	@Test
	public void adicionaNotaDadosValidos(){
		String parecerId  = "12";
		
		String atributoPontuado = "nome";
		 int tamanhoAntes = 0;
		 int tamanhoDepois = 0;
		String valorPontuado = "3256";
		Valor valor = new Valor(valorPontuado);
		Pontuacao pontuacao = new Pontuacao(atributoPontuado, valor);
		
		
		Pontuacao pontuacaoModificada = new Pontuacao(atributoPontuado, new Valor("Leticia Nunes"));
		Nota nota = new Nota(pontuacao, pontuacaoModificada, "Q tipo de nome é 3256:");

		
		ParecerRepositoryMongo parecerDao = new ParecerRepositoryMongo();
		
		tamanhoAntes = parecerDao.byId(parecerId).getNotas().size();
		parecerDao.adicionaNota(parecerId, nota);
		tamanhoDepois = parecerDao.byId(parecerId).getNotas().size();
		
		assertNotEquals(tamanhoAntes, tamanhoDepois);
	}
	
	@Test (expected=IdentificadorDesconhecido.class)
	public void adicionaNotaEmParecerDesconhecido(){
		String parecerId = "abcdef";
		
		String atributoPontuado = "nome";
		
		String valorPontuado = "3256";
		Valor valor = new Valor(valorPontuado);
		Pontuacao pontuacao = new Pontuacao(atributoPontuado, valor);
		
		Pontuacao pontuacaoModificada = new Pontuacao(atributoPontuado, new Valor("Leticia Nunes"));
		Nota nota = new Nota(pontuacao, pontuacaoModificada, "Q tipo de nome é 3256:");

		
		ParecerRepositoryMongo parecerDao = new ParecerRepositoryMongo();
		
		parecerDao.adicionaNota(parecerId, nota);
	}
	
	@Test
	public void removeNotaDadosValidos(){
		String parecerId = "12";
		
		String atributoPontuado = "nome";
		 int tamanhoAntes = 0;
		 int tamanhoDepois = 0;
		String valorPontuado = "3256";
		Valor valor = new Valor(valorPontuado);
		Pontuacao pontuacao = new Pontuacao(atributoPontuado, valor);
		
		Pontuacao pontuacaoModificada = new Pontuacao(atributoPontuado, new Valor("Leticia Nunes"));
		Nota nota = new Nota(pontuacao, pontuacaoModificada, "Q tipo de nome é 3256:");
			
		ParecerRepositoryMongo parecerDao = new ParecerRepositoryMongo();
		parecerDao.adicionaNota(parecerId, nota);
		
		tamanhoAntes = parecerDao.byId(parecerId).getNotas().size();
		parecerDao.removeNota(parecerId, pontuacao);
		tamanhoDepois = parecerDao.byId(parecerId).getNotas().size();
		
		assertNotEquals(tamanhoAntes, tamanhoDepois);
	}
	
	@Test (expected=IdentificadorDesconhecido.class)
	public void atualizaFundamentacaoEmParecerDesconhecido(){
		String parecerId = "abcdef";
		
		String fundamentacao = "pq sim";
		
		parecerDao.atualizaFundamentacao(parecerId, fundamentacao);
	}
	
	@Test
	public void atualizaFundamentacaoDadosValidos(){
		String parecerId = "12";
		
		String fundamentacao = "oi";
		
		parecerDao.atualizaFundamentacao(parecerId, fundamentacao);
		
		assertEquals(parecerDao.byId(parecerId).getFundamentacao(), fundamentacao);		
	}
	
	@Test (expected=CampoExigidoNaoFornecido.class)
	public void persisteRadocSemId(){
		
		String tipoId = "01";
		Set<Atributo> atributos = new HashSet<Atributo>();
		Atributo atributo = new Atributo("nome", "qualquer coisa", 2);
		atributos.add(atributo);
		atributo = new Atributo("cargaHoraria", "hrs", 2);
		atributos.add(atributo);

		Tipo tipo = new Tipo(tipoId, "docente", "atributos do docente", atributos);
		resolucaoDao.persisteTipo(tipo);

		int anoBase = 2005;
		String tipoID = resolucaoDao.tipoPeloCodigo(tipoId).getId();
		Map<String, Valor> valores = new HashMap<String, Valor>();

		for (Atributo atributo2 : resolucaoDao.tipoPeloCodigo(tipoId).getAtributos()) {
			Valor value = new Valor("valor");
			valores.put(atributo2.getNome(), value);
		}
		
		Relato relato = new Relato(tipoID, valores);

		List<Relato> relatos = new ArrayList<Relato>();
		relatos.add(relato);
		String radocId = "";
		Radoc radoc = new Radoc(radocId, anoBase, relatos);

		parecerDao.persisteRadoc(radoc);
	}
	
	@Test (expected=IdentificadorExistente.class)
	public void persisteRadocComIdExistenteNaBase(){
		String tipoId = "01";
		Set<Atributo> atributos = new HashSet<Atributo>();
		Atributo atributo = new Atributo("nome", "qualquer coisa", 2);
		atributos.add(atributo);
		atributo = new Atributo("cargaHoraria", "hrs", 2);
		atributos.add(atributo);

		Tipo tipo = new Tipo(tipoId, "docente", "atributos do docente", atributos);
		resolucaoDao.persisteTipo(tipo);

		int anoBase = 2005;
		String tipoID = resolucaoDao.tipoPeloCodigo(tipoId).getId();
		Map<String, Valor> valores = new HashMap<String, Valor>();

		for (Atributo atributo2 : resolucaoDao.tipoPeloCodigo(tipoId).getAtributos()) {
			Valor value = new Valor("valor");
			valores.put(atributo2.getNome(), value);
		}
		
		Relato relato = new Relato(tipoID, valores);

		List<Relato> relatos = new ArrayList<Relato>();
		relatos.add(relato);
		String radocId = "13";
		Radoc radoc = new Radoc(radocId, anoBase, relatos);

		parecerDao.persisteRadoc(radoc);
		parecerDao.persisteRadoc(radoc);
	}
	
	@Test 
	public void persisteRadocComDadosValidos(){
		
		String tipoId = "01";
		Set<Atributo> atributos = new HashSet<Atributo>();
		Atributo atributo = new Atributo("nome", "qualquer coisa", 2);
		atributos.add(atributo);
		atributo = new Atributo("cargaHoraria", "hrs", 2);
		atributos.add(atributo);

		Tipo tipo = new Tipo(tipoId, "docente", "atributos do docente", atributos);
		resolucaoDao.persisteTipo(tipo);

		int anoBase = 2005;
		String tipoID = resolucaoDao.tipoPeloCodigo(tipoId).getId();
		Map<String, Valor> valores = new HashMap<String, Valor>();

		for (Atributo atributo2 : resolucaoDao.tipoPeloCodigo(tipoId).getAtributos()) {
			Valor value = new Valor("valor");
			valores.put(atributo2.getNome(), value);
		}
		
		Relato relato = new Relato(tipoID, valores);

		List<Relato> relatos = new ArrayList<Relato>();
		relatos.add(relato);
		String radocId = "17";
		Radoc radoc = new Radoc(radocId, anoBase, relatos);

		parecerDao.persisteRadoc(radoc);
		
		assertNotNull(parecerDao.radocById(radocId));
	}
	
	@Test 
	public void deletaRadocComDadosValidos(){
		
		String tipoId = "01";
		Set<Atributo> atributos = new HashSet<Atributo>();
		Atributo atributo = new Atributo("nome", "qualquer coisa", 2);
		atributos.add(atributo);
		atributo = new Atributo("cargaHoraria", "hrs", 2);
		atributos.add(atributo);

		Tipo tipo = new Tipo(tipoId, "docente", "atributos do docente", atributos);
		resolucaoDao.persisteTipo(tipo);

		int anoBase = 2005;
		String tipoID = resolucaoDao.tipoPeloCodigo(tipoId).getId();
		Map<String, Valor> valores = new HashMap<String, Valor>();

		
		for (Atributo atributo2 : resolucaoDao.tipoPeloCodigo(tipoId).getAtributos()) {
			Valor value = new Valor("valor");
			valores.put(atributo2.getNome(), value);
		}
		
		Relato relato = new Relato(tipoID, valores);

		List<Relato> relatos = new ArrayList<Relato>();
		relatos.add(relato);
		String radocId = "17";
		Radoc radoc = new Radoc(radocId, anoBase, relatos);

		parecerDao.persisteRadoc(radoc);
		
		assertNotNull(parecerDao.radocById(radocId));
	}
	
	@Test (expected=ExisteParecerReferenciandoRadoc.class)
	public void deletaRadocReferenciado(){
		String parecerId  = "58";
		String resolucaoId = "resolucao2013";
		
		String radocId = "radoc da lets 2";
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
		
		Parecer parecer02 = new Parecer(parecerId, resolucaoId, radocsIds, pontuacoes, fundamentacao, notas);
		
		parecerDao.persisteParecer(parecer02);
		
		String tipoId = "01";
		Set<Atributo> atributos = new HashSet<Atributo>();
		Atributo atributo = new Atributo("nome", "qualquer coisa", 2);
		atributos.add(atributo);
		atributo = new Atributo("cargaHoraria", "hrs", 2);
		atributos.add(atributo);

		Tipo tipo = new Tipo(tipoId, "docente", "atributos do docente", atributos);
		resolucaoDao.persisteTipo(tipo);

		int anoBase = 2005;
		String tipoID = resolucaoDao.tipoPeloCodigo(tipoId).getId();
		Map<String, Valor> valores = new HashMap<String, Valor>();

		for (Atributo atributo2 : resolucaoDao.tipoPeloCodigo(tipoId).getAtributos()) {
			Valor value = new Valor("valor");
			valores.put(atributo2.getNome(), value);
		}
		
		Relato relato = new Relato(tipoID, valores);

		List<Relato> relatos = new ArrayList<Relato>();
		relatos.add(relato);
		Radoc radoc = new Radoc(radocId, anoBase, relatos);

		parecerDao.persisteRadoc(radoc);
		parecerDao.removeRadoc(radocId);
		
	}

}
