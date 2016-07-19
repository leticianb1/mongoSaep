package br.ufg.inf.es.saep.sandbox.mongo;

import br.ufg.inf.es.saep.sandbox.dominio.*;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Testes do avaliador de regras
 */
public class ResolucaoTestFabio {

    private static final String REPOSITORIO = "br.ufg.inf.es.saep.sandbox.mongo.ResolucaoRepositoryMongo";
    private ResolucaoRepository repo;
    private List<String> dependencias;
    private Regra regra;
    private List<Regra> regras;

    /**
     * Assume que a classe definida por {@link #REPOSITORIO} possui um
     * construtor default (sem argumentos).
     *
     * @throws ClassNotFoundException Classe n�o encontrada no classpath.
     * @throws IllegalAccessException N�o h� permiss�o de acesso � classe.
     * @throws InstantiationException Erro durante a cria��o da inst�ncia.
     */
    @Before
    public void setUpClass() throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        Class<?> classe = Class.forName(REPOSITORIO);
        repo = (ResolucaoRepository)classe.newInstance();

        // Vari�veis utilit�rias para simplifica��o da montagem de testes.
        dependencias = new ArrayList<>();
        regra = new Regra("v", Regra.PONTOS, "pontos", 10, 0, null, null, null, "t", 1, dependencias);
        regras = new ArrayList<>();
        regras.add(regra);
    }

    @Test
    public void naoHaComoRecuperarResolucaoInexistente() {
        assertNull(repo.byId(UUID.randomUUID().toString()));
    }

    @Test
    public void recuperaResolucaoInserida() {
        List<String> dd = new ArrayList<>();
        Regra regra = new Regra("p", Regra.PONTOS, "pontos", 10, 0, null, null, null, "t", 1, dd);
        List<Regra> regras = new ArrayList<>();
        regras.add(regra);
        Resolucao r = new Resolucao("r", "nome", "resolu��o r", new Date(), regras);

        assertEquals("r", repo.persiste(r));

        Resolucao recuperada = repo.byId("r");
        assertEquals(r, recuperada);
        assertEquals("resolu��o r", recuperada.getDescricao());
    }

    @Test
    public void resolucaoRemovidaNaoPodeSerRecuperada() {
        String id = UUID.randomUUID().toString();
        Resolucao r = new Resolucao(id, "regra r", "resolu��o r", new Date(), regras);

        assertEquals(id, repo.persiste(r));

        // resolu��o inserida est� dispon�vel
        assertEquals(1, repo.resolucoes().size());

        assertTrue(repo.remove(id));

        assertNull(repo.byId("r"));

        // ap�s remo��o, nenhuma resolu��o est� dispon�vel
        assertEquals(0, repo.resolucoes().size());
    }

    @Test
    public void persisteRecuperaTipo() {
        Atributo a = new Atributo("a", "atributo", Atributo.REAL);
        Set<Atributo> atributos = new HashSet<>(1);
        atributos.add(a);

        Tipo tipo = new Tipo("id", "t", "tipo", atributos);

        repo.persisteTipo(tipo);

        Tipo recuperado = repo.tipoPeloCodigo("id");

        assertEquals(tipo, recuperado);
    }

    @Test
    public void recuperaTiposPorSemelhancaDeNome() {
        Atributo a = new Atributo("alcalina", "atributo", Atributo.REAL);

        Set<Atributo> atributos = new HashSet<>(1);
        atributos.add(a);

        Tipo t1 = new Tipo("t1", "alcalina", "tipo", atributos);
        Tipo t2 = new Tipo("t2", "alface", "tipo", atributos);
        Tipo t3 = new Tipo("t3", "cristalina", "tipo", atributos);

        repo.persisteTipo(t1);
        repo.persisteTipo(t2);
        repo.persisteTipo(t3);

        // "alcalina" e "cristalina"
        assertEquals(2, repo.tiposPeloNome("lina").size());

        // "alcalina", "alface" e "cristalina"
        assertEquals(3, repo.tiposPeloNome("al").size());

        assertEquals(0, repo.tiposPeloNome("x").size());
    }

}