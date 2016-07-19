package br.ufg.inf.es.saep.sandbox.mongo;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.BeforeClass;
import org.junit.Test;

import br.com.six2six.fixturefactory.Fixture;
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
import br.ufg.inf.es.saep.sandbox.fixtures.CustomProcessor;
import br.ufg.inf.es.saep.sandbox.fixtures.TemplateLoader;

public class ParecerMongoTestFixture {

	static ParecerRepositoryMongo parecerDao;
	static ResolucaoRepositoryMongo resolucaoDao;
	
	
	@BeforeClass
	public  static void setUp(){
		parecerDao = new ParecerRepositoryMongo();
		resolucaoDao = new ResolucaoRepositoryMongo();
	//	FixtureFactoryLoader.loadTemplates("br.ufg.inf.es.saep.sandbox.fixtures");
	} 
	
	@Test
	public void persisteParecerDadosValidos(){
		TemplateLoader.loadTemplates();
		Parecer parecer = Fixture.from(Parecer.class).gimme("parecer");

		parecerDao.persisteParecer(parecer);
		
		
		assertNotNull(parecerDao.byId(parecer.getId()));
	}
	
	
	@Test (expected=IdentificadorExistente.class)
	public void persisteParecerComIdExistenteNaBase(){
		Parecer parecer = Fixture.from(Parecer.class).gimme("parecer");

		parecerDao.persisteParecer(parecer);
		parecerDao.persisteParecer(parecer);
	}
	
	@Test
	public void adicionaNotaDadosValidos(){
		TemplateLoader.loadTemplates();
		Parecer parecer = Fixture.from(Parecer.class).uses(new CustomProcessor()).gimme("parecer");
		
		Nota nota = Fixture.from(Nota.class).gimme("notaPont");
		
		int tamanhoAntes = 0;
		int tamanhoDepois = 0;

		tamanhoAntes = parecerDao.byId(parecer.getId()).getNotas().size();
		
		parecerDao.adicionaNota(parecer.getId(), nota);
		
		tamanhoDepois = parecerDao.byId(parecer.getId()).getNotas().size();
		
		assertNotEquals(tamanhoAntes, tamanhoDepois);
	}
	
	@Test (expected=IdentificadorDesconhecido.class)

	public void adicionaNotaEmParecerDesconhecido(){
		TemplateLoader.loadTemplates();
		String parecerId = "abcdef";
		
		Nota nota = Fixture.from(Nota.class).gimme("notaPont");
		
		parecerDao.adicionaNota(parecerId, nota);
	}
	
	@Test
	public void removeNotaDadosValidos(){
		Parecer parecer = Fixture.from(Parecer.class).uses(new CustomProcessor()).gimme("parecer");
		
		Nota nota = Fixture.from(Nota.class).gimme("notaOriginal");
		Pontuacao pontuacaoOriginal = Fixture.from(Pontuacao.class).gimme("pontuacaoNotaFixa");
		
		parecerDao.adicionaNota(parecer.getId(), nota);
		
		
		int tamanhoAntes = 0;
		int tamanhoDepois = 0;

		tamanhoAntes = parecerDao.byId(parecer.getId()).getNotas().size();
		
		parecerDao.removeNota(parecer.getId(), pontuacaoOriginal);
		
		tamanhoDepois = parecerDao.byId(parecer.getId()).getNotas().size();
		
		assertNotEquals(tamanhoAntes, tamanhoDepois);
	}
	
	@Test (expected=IdentificadorDesconhecido.class)
	public void atualizaFundamentacaoEmParecerDesconhecido(){
		Parecer parecer = Fixture.from(Parecer.class).uses(new CustomProcessor()).gimme("parecer");
		
		String fundamentacao = "pq sim";
		
		parecerDao.atualizaFundamentacao(parecer.getId(), fundamentacao);
	}
	
	@Test
	public void atualizaFundamentacaoDadosValidos(){
		Parecer parecer = Fixture.from(Parecer.class).uses(new CustomProcessor()).gimme("parecer");
		
		String fundamentacao = "pq sim";
		
		parecerDao.atualizaFundamentacao(parecer.getId(), fundamentacao);
		
		assertEquals(parecerDao.byId(parecer.getId()).getFundamentacao(), fundamentacao);		
	}
	
	@Test (expected=CampoExigidoNaoFornecido.class)
	public void persisteRadocSemId(){
		Radoc radocAleatorio =  Fixture.from(Radoc.class).uses(new CustomProcessor()).gimme("radocFixo");
		
		List<Relato> relatos = radocAleatorio.getRelatos();
		String radocId = "";
		Radoc radoc = new Radoc(radocId, 2015, relatos);

		parecerDao.persisteRadoc(radoc);
	}
	
	@Test (expected=IdentificadorExistente.class)
	public void persisteRadocComIdExistenteNaBase(){
		Radoc radoc =  Fixture.from(Radoc.class).gimme("radocFixo");
		
		parecerDao.persisteRadoc(radoc);

		parecerDao.persisteRadoc(radoc);
	}
	
	@Test 
	public void persisteRadocComDadosValidos(){
		Radoc radoc =  Fixture.from(Radoc.class).gimme("radocFixo");
		
		parecerDao.persisteRadoc(radoc);
		
		assertNotNull(parecerDao.radocById(radoc.getId()));
	}
	
	@Test 
	public void deletaRadocComDadosValidos(){
		Radoc radoc =  Fixture.from(Radoc.class).gimme("radoc");

		parecerDao.persisteRadoc(radoc);
		
		assertNotNull(parecerDao.radocById(radoc.getId()));
	}
	
	@Test (expected=ExisteParecerReferenciandoRadoc.class)
	public void deletaRadocReferenciado(){
		Parecer parecer = Fixture.from(Parecer.class).gimme("parecerComRadocFixo");

		parecerDao.persisteParecer(parecer);
		
		Radoc radoc =  Fixture.from(Radoc.class).gimme("radocFixo");
		
		parecerDao.persisteRadoc(radoc);
		
		parecerDao.removeRadoc(radoc.getId());
		
	}
	
	
	private Tipo montarTipo(String id) {
		String tipoId = id;

		Atributo atributo = new Atributo("a", "d", 1);
        Set<Atributo> atrs = new HashSet<>(0);
        atrs.add(atributo);
		
		return new Tipo(tipoId, "c", "acd", atrs);
	}
	
//	private Relato montarRelato(String id) {
//
//		Atributo atributo = new Atributo("a", "d", 1);
//        Set<Atributo> atrs = new HashSet<>(0);
//        atrs.add(atributo);
//		
//		//return new Tipo(tipoId, "c", "acd", atrs);
//		
//		Map<String, Valor> valores = new HashMap<String, Valor>();
//		valores.put("nome", new Valor("Fulano da Silva"));
//		valores.put("cargaHoraria", new Valor(32));
//		
//		Map<String, Valor> valoresTeste = new HashMap<String, Valor>();
//		valores.put("trabalho", new Valor("Pesquisa em Teste de Software"));
//		valores.put("paginas", new Valor(40));
//		
//		Relato relato = new Relato("a", valores);
//
//
//	}

}
