package br.ufg.inf.es.saep.sandbox.mongo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.BeforeClass;
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
/**
 * Testes da classe de implementação (ParecerRepositoryMongo) de ParecerRepository
 */
public class ParecerMongoTest {
	static ParecerRepositoryMongo parecerDao;
	static ResolucaoRepositoryMongo resolucaoDao;
	
	static String atributoNome = "nome";
	static String atributoCarga = "cargaHoraria";
	static String atributoTrabalho = "trabalho";
	static String atributoPagina = "paginas";
	static String tipoDocente = "docente";
	static String tipoPesquisa = "pesquisa";
	static String idRadoc = "radoc1";
	static String idParecer = "parecer1";
	static String idResolucao = "resolucao1";
	
	@BeforeClass
	public  static void setUp(){
		parecerDao = new ParecerRepositoryMongo();
		resolucaoDao = new ResolucaoRepositoryMongo();
		DataUtil.getMongoDb().drop();
		resolucaoDao.persisteTipo(montarTipo(tipoDocente));
		resolucaoDao.persisteTipo(montarTipo(tipoPesquisa));
	} 
	
	@Test
	public void persisteParecerDadosValidos(){
		Parecer parecer = montarParecer("01");
		parecerDao.persisteParecer(parecer);
		
		
		assertNotNull(parecerDao.byId(parecer.getId()));
	}
	
	
	@Test (expected=IdentificadorExistente.class)
	public void persisteParecerComIdExistenteNaBase(){
		Parecer parecer = montarParecer("02");

		parecerDao.persisteParecer(parecer);
		parecerDao.persisteParecer(parecer);
	}
	
	@Test
	public void adicionaNotaDadosValidos(){
		Parecer parecer = montarParecer("04");
		parecerDao.persisteParecer(parecer);
		
		Nota nota = montarNotas(atributoTrabalho).iterator().next();
		
		int tamanhoAntes = 0;
		int tamanhoDepois = 0;

		tamanhoAntes = parecerDao.byId(parecer.getId()).getNotas().size();
		
		parecerDao.adicionaNota(parecer.getId(), nota);
		
		tamanhoDepois = parecerDao.byId(parecer.getId()).getNotas().size();
		
		assertNotEquals(tamanhoAntes, tamanhoDepois);
	}
	
	@Test (expected=IdentificadorDesconhecido.class)
	public void adicionaNotaEmParecerDesconhecido(){
		String parecerId = "abcdef";
		
		Nota nota = montarNotas(atributoNome).iterator().next();
		
		parecerDao.adicionaNota(parecerId, nota);
	}
	
	@Test
	public void removeNotaDadosValidos(){
		Parecer parecer = montarParecer("05");
		parecerDao.persisteParecer(parecer);
		
		Nota nota = montarNotas(atributoCarga).iterator().next();
		
		parecerDao.adicionaNota(parecer.getId(), nota);
		
		
		int tamanhoAntes = 0;
		int tamanhoDepois = 0;

		tamanhoAntes = parecerDao.byId(parecer.getId()).getNotas().size();
		
		parecerDao.removeNota(parecer.getId(), nota.getItemOriginal());
		
		tamanhoDepois = parecerDao.byId(parecer.getId()).getNotas().size();
		
		assertNotEquals(tamanhoAntes, tamanhoDepois);
	}
	
	@Test (expected=IdentificadorDesconhecido.class)
	public void atualizaFundamentacaoEmParecerDesconhecido(){
		Parecer parecer = montarParecer("abcdf");
		
		String fundamentacao = "pq sim";
		
		parecerDao.atualizaFundamentacao(parecer.getId(), fundamentacao);
	}
	
	@Test
	public void atualizaFundamentacaoDadosValidos(){
		Parecer parecer = montarParecer("06");
		parecerDao.persisteParecer(parecer);
		
		String fundamentacao = "pq sim";
		
		parecerDao.atualizaFundamentacao(parecer.getId(), fundamentacao);
		
		assertEquals(parecerDao.byId(parecer.getId()).getFundamentacao(), fundamentacao);		
	}
	
	@Test (expected=CampoExigidoNaoFornecido.class)
	public void persisteRadocSemId(){
		Radoc radoc =  montarRadoc("");
		
		parecerDao.persisteRadoc(radoc);
	}
	
	@Test (expected=IdentificadorExistente.class)
	public void persisteRadocComIdExistenteNaBase(){
		Radoc radoc =  montarRadoc("01");
		
		parecerDao.persisteRadoc(radoc);

		parecerDao.persisteRadoc(radoc);
	}
	
	@Test 
	public void persisteRadocComDadosValidos(){
		Radoc radoc =  montarRadoc("02");
		
		parecerDao.persisteRadoc(radoc);
		
		
		assertNotNull(parecerDao.radocById(radoc.getId()));
	}
	
	@Test 
	public void deletaRadocComDadosValidos(){
		Radoc radoc =  montarRadoc("deletar");
		
		parecerDao.persisteRadoc(radoc);
		
		parecerDao.removeRadoc(radoc.getId());
		
		assertNull(parecerDao.radocById(radoc.getId()));
	}
	
	@Test (expected=ExisteParecerReferenciandoRadoc.class)
	public void deletaRadocReferenciado(){
		ArrayList<String> radocs = new ArrayList<String>();
		radocs.add("radocReferenciado");
		
		Parecer parecer = new Parecer("parecerComRadoc", idResolucao, radocs, montarPontuacoes(), "algo", montarNotas(atributoNome));
		parecerDao.persisteParecer(parecer);

		Radoc radoc =  montarRadoc("radocReferenciado");
		
		parecerDao.persisteRadoc(radoc);
		
		parecerDao.removeRadoc(radoc.getId());
		
	}
	
	public Radoc montarRadoc(String id){
		
		Radoc radoc = new Radoc(id, 2015, montarRelatos());
		
		return radoc;
		
	}
	
	public Parecer montarParecer(String id){
		ArrayList<String> radocs = new ArrayList<String>();
		radocs.add(idRadoc);
		Parecer parecer = new Parecer(id, idResolucao, radocs, montarPontuacoes(), "algo", montarNotas(atributoNome));
		
		return parecer;
		
	}
	
	public Atributo montarAtributo(){

		return new Atributo(atributoNome, atributoNome, 2);
	}
	
	public static Tipo montarTipo(String id){
		Set<Atributo> atributos = new HashSet<Atributo>();
		
		if(id.equals(tipoDocente)){
			atributos.add(new Atributo(atributoNome, atributoNome, 2));
			atributos.add(new Atributo(atributoCarga, atributoNome, 2));
		}
		
		if(id.equals(tipoPesquisa)){
			atributos.add(new Atributo(atributoTrabalho, atributoNome, 2));
			atributos.add(new Atributo(atributoPagina, atributoNome, 2));
		}
		return new Tipo(id, id, id, atributos);
	}
	
	public List<Relato> montarRelatos(){
		ArrayList<Relato> relatos = new ArrayList<Relato>();
		
		Map<String, Valor> valores = new HashMap<String, Valor>();
		valores.put(atributoNome, new Valor("Fulano da Silva"));
		valores.put(atributoCarga, new Valor(32));
		
		relatos.add(new Relato(tipoDocente, valores));
		
		Map<String, Valor> valoresPesquisa = new HashMap<String, Valor>();
		valoresPesquisa.put(atributoTrabalho, new Valor("Teste de software"));
		valoresPesquisa.put(atributoPagina, new Valor(32));
		
		relatos.add(new Relato(tipoPesquisa, valoresPesquisa));
		
		return relatos;
	}
	
	public List<Pontuacao> montarPontuacoes(){
		ArrayList<Pontuacao> pontuacoes = new ArrayList<Pontuacao>();
		
		pontuacoes.add(new Pontuacao(atributoNome, new Valor(40)));
		pontuacoes.add(new Pontuacao(atributoCarga, new Valor(40)));
		pontuacoes.add(new Pontuacao(atributoTrabalho, new Valor(40)));
		pontuacoes.add(new Pontuacao(atributoPagina, new Valor(40)));
		return pontuacoes;
	}
	
	public List<Nota> montarNotas(String atributo){
		ArrayList<Nota> notas = new ArrayList<Nota>();
		
		
		notas.add(new Nota(new Pontuacao(atributo, new Valor(40)), new Pontuacao(atributo, new Valor(40)), "pqsim"));
		
		return notas;
	}
}
