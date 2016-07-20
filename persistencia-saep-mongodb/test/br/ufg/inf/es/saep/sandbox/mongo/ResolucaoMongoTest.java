package br.ufg.inf.es.saep.sandbox.mongo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import br.ufg.inf.es.saep.sandbox.dominio.Atributo;
import br.ufg.inf.es.saep.sandbox.dominio.IdentificadorExistente;
import br.ufg.inf.es.saep.sandbox.dominio.Regra;
import br.ufg.inf.es.saep.sandbox.dominio.Resolucao;
import br.ufg.inf.es.saep.sandbox.dominio.Tipo;

/**
 * Testes da classe de implementação (ResolucaoRepositoryMongo) de ResolucaoRepository
 */
public class ResolucaoMongoTest {

		private static List<Regra> regras;

		ParecerRepositoryMongo parecerDao;
		ResolucaoRepositoryMongo resolucaoDao;
		
		@Before
		public void setUp(){
			parecerDao = new ParecerRepositoryMongo();
			resolucaoDao = new ResolucaoRepositoryMongo();
			DataUtil.getMongoDb().drop();	
			Regra r = new Regra("v", 1, "d", 1, 0, "a", null, null, null, 1, new ArrayList<String>());
	        regras = new ArrayList<>();
	        regras.add(r);
		}

		@Test
		public void persisteTipoDadosValidos() {
			Tipo tipo = montarTipo("Tipo1");

			resolucaoDao.persisteTipo(tipo);

			assertNotEquals(resolucaoDao.tipoPeloCodigo(tipo.getId()), null);
		}

		@Test(expected = IdentificadorExistente.class)
		public void persisteTipoComIdExistenteNaBase() {
			Tipo tipo = montarTipo("Tipo2");

			resolucaoDao.persisteTipo(tipo);
			resolucaoDao.persisteTipo(tipo);
		}
		
		@Test
		public void removerTipo() {
			Tipo tipo = montarTipo("Tipo3");
			
			resolucaoDao.persisteTipo(tipo);
			resolucaoDao.removeTipo(tipo.getId());
		}
		
		private Tipo montarTipo(String id) {
			String tipoId = id;

			Atributo atributo = new Atributo("a", "d", 1);
	        Set<Atributo> atrs = new HashSet<>(0);
	        atrs.add(atributo);
			
			return new Tipo(tipoId, "c", "acd", atrs);
		}
		
		@Test
		public void persisteResolucaoDadosValidos() {
			Resolucao resolucao = montarResolucao("Res1");
			
			resolucaoDao.persiste(resolucao);
			Resolucao resolucao2 = resolucaoDao.byId(resolucao.getId());
			assertNotEquals(resolucao2, null);
		}

		@Test(expected = IdentificadorExistente.class)
		public void persisteResolucaoComIdExistenteNaBase() {
			Resolucao resolucao = montarResolucao("Par2");
			
			resolucaoDao.persiste(resolucao);
			resolucaoDao.persiste(resolucao);
		}
		
		@Test
		public void removerResolucao() {
			Resolucao resolucao = montarResolucao("Par3");
			
			resolucaoDao.persiste(resolucao);
			assertEquals(resolucaoDao.remove(resolucao.getId()), true);
		}
		
		private Resolucao montarResolucao(String id) {
			String resolucaoId = id;

			return new Resolucao(resolucaoId, "r", "d", new Date(), regras);
		}
	}
