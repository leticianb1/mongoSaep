package br.ufg.inf.es.saep.sandbox.mongo;

import br.ufg.inf.es.saep.sandbox.dominio.*;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Testes b�sicos (funcionais) de implementa��o
 * do reposit�rio de pareceres.
 */
public class ParecerTestFabio {

    private static final String REPOSITORIO = "br.ufg.inf.es.saep.sandbox.mongo.ParecerRepositoryMongo";
    private ParecerRepository repo;

    private List<String> radocsIds;
    private List<Pontuacao> pontuacoes;
    private List<Nota> notas;
    private Radoc radoc;

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
        repo = (ParecerRepository)classe.newInstance();

        radocsIds = new ArrayList<>(1);
        radocsIds.add("radocId");

        pontuacoes = new ArrayList<>(1);
        pontuacoes.add(new Pontuacao("x", new Valor(true)));

        notas = new ArrayList<>(0);

        // Monta radoc trivial
        Map<String, Valor> valores = new HashMap<>();
        valores.put("v", new Valor(123f));

        Relato relato = new Relato("r", valores);
        List<Relato> relatos = new ArrayList<>();
        relatos.add(relato);
        radoc = new Radoc("r", 2016, relatos);
    }

    @Test
    public void naoHaComoRecuperarParecerInexistente() {

        assertNull(repo.byId(UUID.randomUUID().toString()));
    }

    @Test
    public void insereRecuperaParecer() {

        Parecer p = new Parecer("rid", "res", radocsIds, pontuacoes, "f", notas);
        repo.persisteParecer(p);
        String id = p.getId();

        Parecer r = (repo).byId(id);
        assertEquals(p, r);
    }

    @Test
    public void removeParecer() {
        Parecer p = new Parecer("rid", "res", radocsIds, pontuacoes, "f", notas);
        repo.persisteParecer(p);
        String id = p.getId();

        // Verifica exist�ncia
        assertEquals(id, repo.byId(id).getId());

        repo.removeParecer(id);
        assertNull(repo.byId(id));
    }

    @Test
    public void insereRecuperaRemoveRadoc() {

        repo.persisteRadoc(radoc);
        String id = radoc.getId();

        Radoc r = repo.radocById(id);
        assertEquals(radoc, r);

        repo.removeRadoc(id);
        assertNull(repo.radocById(id));
    }
}