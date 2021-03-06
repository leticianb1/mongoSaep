package br.ufg.inf.es.saep.sandbox.mongo;

import static com.mongodb.client.model.Filters.all;
import static com.mongodb.client.model.Filters.eq;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.Document;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Updates;

import br.ufg.inf.es.saep.sandbox.dominio.Atributo;
import br.ufg.inf.es.saep.sandbox.dominio.Avaliavel;
import br.ufg.inf.es.saep.sandbox.dominio.ExisteParecerReferenciandoRadoc;
import br.ufg.inf.es.saep.sandbox.dominio.IdentificadorDesconhecido;
import br.ufg.inf.es.saep.sandbox.dominio.IdentificadorExistente;
import br.ufg.inf.es.saep.sandbox.dominio.Nota;
import br.ufg.inf.es.saep.sandbox.dominio.Parecer;
import br.ufg.inf.es.saep.sandbox.dominio.ParecerRepository;
import br.ufg.inf.es.saep.sandbox.dominio.Pontuacao;
import br.ufg.inf.es.saep.sandbox.dominio.Radoc;
import br.ufg.inf.es.saep.sandbox.dominio.Relato;
import br.ufg.inf.es.saep.sandbox.dominio.Resolucao;
import br.ufg.inf.es.saep.sandbox.dominio.Tipo;
import br.ufg.inf.es.saep.sandbox.dominio.Valor;

/**
 * Classe que implementa a interface de ParecerRepository para o uso do MongoDB para armazenamento dos dados
 * Além dos métodos da interface, métodos de apoio foram criados para facilitar a transição de instancias das Classes de Domínio do SAEP
 * para classes de Document que são usadas para persistência na base de dados do Mongo
 *
 * @see ParecerRepository
 */
public class ParecerRepositoryMongo implements ParecerRepository {

	static String COLECAO_PARECER = "pareceres";
	static String COLECAO_RADOC = "radocs";

	static String PARECER_ID = "parecerId";
	static String RESOLUCAO_ID = "resolucaoId";
	static String LISTA_RADOCS = "listaRadocs";
	static String LISTA_PONTUACOES = "listaPontuacoes";
	static String FUNDAMENTACAO = "fundamentacao";
	static String ATRIBUTO = "atributo";
	static String VALOR = "valor";

	static String TIPO_ID = "tipoId";

	static String LISTA_NOTAS = "listaNotas";
	static String ORIGINAL = "original";
	static String DESTINO = "destino";
	static String JUSTIFICATIVA = "justificativa";

	static String RADOC_ID = "radocId";
	static String ANO_BASE = "anoBase";
	static String LISTA_RELATOS = "listaRelatos";
	static String LISTA_VALORES = "listaValores";

	@Override
	public void adicionaNota(String id, Nota nota) {
		MongoCollection<Document> listaPareceres = DataUtil.getMongoDb().getCollection(COLECAO_PARECER);

		Parecer parecer = byId(id);
		parecer.getNotas().add(nota);

		Document documento = criarDocumentoDoParecer(parecer);

		listaPareceres.replaceOne(eq(PARECER_ID, id), documento);
	}

	@Override
	public void removeNota(String id, Avaliavel original) {
		MongoCollection<Document> listaPareceres = DataUtil.getMongoDb().getCollection(COLECAO_PARECER);

		Parecer parecer = byId(id);
		for (Nota nota : parecer.getNotas()) {
			if (nota.getItemOriginal() instanceof Relato) {
				if (nota.getItemOriginal().equals(original)) {
					parecer.getNotas().remove(nota);
					break;
				}
			} else {
				verificaPontuacaoIgual((Pontuacao) nota.getItemOriginal(), (Pontuacao) original);
				parecer.getNotas().contains(nota);
				parecer.getNotas().remove(nota);
				break;
			}
		}
		Document documento = criarDocumentoDoParecer(parecer);

		listaPareceres.replaceOne(eq(PARECER_ID, id), documento);
	}

	public boolean verificaPontuacaoIgual(Pontuacao comparar, Pontuacao original) {
		boolean igual = false;

		if (comparar.getAtributo().equals(original.getAtributo())
				&& comparar.getValor().getString().equals(original.getValor().getString())) {
			igual = true;
		}

		return igual;
	}

	@Override
	public void persisteParecer(Parecer parecer) {
		MongoCollection<Document> listaPareceres = DataUtil.getMongoDb().getCollection(COLECAO_PARECER);

		Document documento = criarDocumentoDoParecer(parecer);

		if (parecer.getId().isEmpty()) {
			throw new IdentificadorDesconhecido("Id vazio");
		}
		try {
			byId(parecer.getId());
			throw new IdentificadorExistente(PARECER_ID);

		} catch (IdentificadorDesconhecido e) {
			listaPareceres.insertOne(documento);

		}
	}

	@Override
	public void atualizaFundamentacao(String parecer, String fundamentacao) {
		MongoCollection<Document> listaPareceres = DataUtil.getMongoDb().getCollection(COLECAO_PARECER);

		if (byId(parecer) != null) {
			listaPareceres.updateOne(eq(PARECER_ID, parecer), Updates.set(FUNDAMENTACAO, fundamentacao));
		}
	}

	@Override
	public Parecer byId(String id) {

		MongoCollection<Document> collection = DataUtil.getMongoDb().getCollection(COLECAO_PARECER);

		FindIterable<Document> find = collection.find(eq(PARECER_ID, id));
		if (find == null || find.first() == null || find.first().isEmpty()) {
			throw new IdentificadorDesconhecido(PARECER_ID);
		}

		String parecerId = find.first().getString(PARECER_ID);
		String resolucaoId = find.first().getString(RESOLUCAO_ID);
		List<String> radocsIds = (List<String>) find.first().get(LISTA_RADOCS);
		List<Document> pontuacoesDocument = (List<Document>) find.first().get(LISTA_PONTUACOES);
		List<Pontuacao> pontuacoes = new ArrayList<Pontuacao>();

		for (Document document : pontuacoesDocument) {
			String atributoPontuado = document.getString(ATRIBUTO);
			String valorPontuado = document.getString(VALOR);
			Valor valor = new Valor(valorPontuado);
			Pontuacao pontuacao = new Pontuacao(atributoPontuado, valor);
			pontuacoes.add(pontuacao);

		}

		String fundamentacao = find.first().getString(FUNDAMENTACAO);

		List<Document> notasDocument = (List<Document>) find.first().get(LISTA_NOTAS);
		List<Nota> notas = new ArrayList<>();
		
		for (Document document : notasDocument) {

			Avaliavel origem;
			Document originalDocument = (Document) document.get(ORIGINAL);
			origem = getAvaliavel(originalDocument);

			Avaliavel destino;
			Document destinoDocument = (Document) document.get(DESTINO);
			destino = getAvaliavel(destinoDocument);

			String justificativa = document.getString(JUSTIFICATIVA);

			Nota nota = new Nota(origem, destino, justificativa);
			notas.add(nota);
		}
		Parecer parecer02 = new Parecer(id, resolucaoId, radocsIds, pontuacoes, fundamentacao, notas);

		return parecer02;
	}

	@Override
	public void removeParecer(String id) {
		MongoCollection<Document> collection = DataUtil.getMongoDb().getCollection(COLECAO_PARECER);

		collection.deleteOne(eq(PARECER_ID, id));
	}

	@Override
	public Radoc radocById(String identificador) {
		MongoCollection<Document> collection = DataUtil.getMongoDb().getCollection(COLECAO_RADOC);

		FindIterable<Document> find = collection.find(eq(RADOC_ID, identificador));
		
		if (find == null || find.first() == null || find.first().isEmpty()) {
			return null;
		}

		String radocId = find.first().getString(RADOC_ID);
		int anoBase = find.first().getInteger(ANO_BASE);

		List<Document> relatosDocument = (List<Document>) find.first().get(LISTA_RELATOS);
		List<Relato> relatos = new ArrayList<Relato>();

		for (Document document : relatosDocument) {
			String tipoId = document.getString(TIPO_ID);
			Document valoresEmDocumento = (Document) document.get(LISTA_VALORES);
			Map<String, Valor> mapaDeValores = new HashMap<String, Valor>();

			for (String atributo : valoresEmDocumento.keySet()) {
				Valor valor = new Valor(valoresEmDocumento.getString(atributo));
				mapaDeValores.put(atributo, valor);
			}

			Relato relato = new Relato(tipoId, mapaDeValores);
			relatos.add(relato);
		}
		Radoc radoc = new Radoc(radocId, anoBase, relatos);

		return radoc;
	}

	@Override
	public String persisteRadoc(Radoc radoc) {
		MongoCollection<Document> listaRadoc = DataUtil.getMongoDb().getCollection(COLECAO_RADOC);

		Document documento = new Document();
		documento.put(RADOC_ID, radoc.getId());
		documento.put(ANO_BASE, radoc.getAnoBase());
		documento.put(LISTA_RELATOS, getListaRelatos(radoc.getRelatos()));

		if (radocById(radoc.getId()) == null) {
			listaRadoc.insertOne(documento);

			FindIterable<Document> find = listaRadoc.find(documento);

			return find.first().getString(RADOC_ID);
		} else {
			throw new IdentificadorExistente(RADOC_ID);
		}
	}

	@Override
	public void removeRadoc(String identificador) {
		MongoCollection<Document> collection = DataUtil.getMongoDb().getCollection(COLECAO_RADOC);
		MongoCollection<Document> collectionParecer = DataUtil.getMongoDb().getCollection(COLECAO_PARECER);

		FindIterable<Document> find = collectionParecer.find(all(LISTA_RADOCS, Arrays.asList(identificador)));

		if (find == null || find.first() == null || find.first().isEmpty()) {
			collection.deleteMany(eq(RADOC_ID, identificador));
		} else {
			throw new ExisteParecerReferenciandoRadoc("Algum parecer faz referencia a esse radoc: " + RADOC_ID);
		}
	}

	
    /**
     * Monta um documento do MongoDB (Document) com os atributos do parecer passado como parametro
     *
     * @param parecer O objeto da classe Parecer instanciado para ser salvo no formato de documento
     *
     * @return o documento no formato esperado 
     */
	
	private Document criarDocumentoDoParecer(Parecer parecer) {
		Document documento = new Document();
		documento.put(PARECER_ID, parecer.getId());
		documento.put(RESOLUCAO_ID, parecer.getResolucao());
		documento.put(LISTA_RADOCS, parecer.getRadocs());
		documento.append(LISTA_PONTUACOES, getListaPontuacoes(parecer.getPontuacoes()));
		documento.put(FUNDAMENTACAO, parecer.getFundamentacao());
		documento.append(LISTA_NOTAS, getListaNota(parecer.getNotas()));

		return documento;
	}

    /**
     * Monta o objeto da classe Avaliavel a partir de um objeto no formato de documento (Document) recuperado do banco.
     * Como Avaliavel é uma interface, há uma verificação de qual classe é o documento a partir da presença da propriedade "tipoId"
     * que só existe em objetos da classe Relato 
     *
     * @param avaliavelDocument O objeto da classe Document para ser instanciado como Avaliavel
     *
     * @return um objeto da classe Avaliavel (Relato ou Pontuacao)
     */
	
	private Avaliavel getAvaliavel(Document avaliavelDocument) {
		if (avaliavelDocument.containsKey(TIPO_ID)) {
			String tipo = avaliavelDocument.getString(TIPO_ID);
			List<Document> valoresDocument = (List<Document>) avaliavelDocument.get(LISTA_VALORES);
			Map<String, Valor> valores = new HashMap<>();
			Document valorDocument = valoresDocument.get(0);

			for (String atributo : valorDocument.keySet()) {
				valores.put(atributo, new Valor(valorDocument.getString(atributo)));
			}

			Relato relato = new Relato(tipo, valores);

			return relato;
		} else {
			String atributoPontuado = avaliavelDocument.getString(ATRIBUTO);
			String valorPontuado = avaliavelDocument.getString(VALOR);
			Valor valor = new Valor(valorPontuado);
			Pontuacao pontuacao = new Pontuacao(atributoPontuado, valor);

			return pontuacao;
		}
	}
	
    /**
     * Monta uma lista de documentos do MongoDB a partir de uma lista de pontuacoes passada
     *
     * @param pontuacoes Uma lista de pontuacoes (lista de objetos Pontuacao)
     *
     * @return uma lista de Document correspondente a lista de Pontuacao passada
     */
	private ArrayList<Document> getListaPontuacoes(List<Pontuacao> pontuacoes) {

		ArrayList<Document> pontuacoesDocument = new ArrayList<Document>();

		for (Pontuacao pontuacao : pontuacoes) {
			Document documento = new Document();
			documento.put(ATRIBUTO, pontuacao.getAtributo());
			documento.put(VALOR, pontuacao.getValor().getString());
			pontuacoesDocument.add(documento);
		}

		return pontuacoesDocument;
	}

    /**
     * Monta uma lista de documentos do MongoDB a partir de uma lista de relatos passada
     *
     * @param relatos Uma lista de relatos (lista de objetos Relato)
     *
     * @return uma lista de Document correspondente a lista de Relato passada
     */
	private ArrayList<Document> getListaRelatos(List<Relato> relatos) {
		ArrayList<Document> relatosDocument = new ArrayList<Document>();
		
		for (Relato relato : relatos) {
			Document documento = new Document();
			documento.put(TIPO_ID, relato.getTipo());
			documento.append(LISTA_VALORES, getListaValores(relato));
			relatosDocument.add(documento);
		}

		return relatosDocument;
	}
   /**
    * Monta um documento do MongoDB (Document) a partir de um objeto da classe Relato. Como na classe de domínio Relato não há um 
    * método para recuperar a lsita de todos os atributos, essa lista é obtida a partir da lista de atributos do tipo correspondente
    *
    * @param relato Um objeto da classe Relato
    *
    * @return um Document correspondente ao Relato passado
    */
	private Document getListaValores(Relato relato) {
		ArrayList<Document> relatosDocument = new ArrayList<Document>();
		ResolucaoRepositoryMongo resolucaoDao = new ResolucaoRepositoryMongo();
		Document documento = new Document();
		Tipo tipo = resolucaoDao.tipoPeloCodigo(relato.getTipo());

		for (Atributo atributo : tipo.getAtributos()) {
			String nome = atributo.getNome();
			Valor valor = relato.get(nome);
			
			if (valor != null) {
				documento.put(nome, valor.getString());
			}
		}

		return documento;
	}

    /**
     * Monta uma lista de documentos do MongoDB a partir de uma lista de notas passada
     *
     * @param notas Uma lista de notas (lista de objetos Nota)
     *
     * @return uma lista de Document correspondente a lista de Nota passada
     */
	private ArrayList<Document> getListaNota(List<Nota> notas) {
		ArrayList<Document> notasDocument = new ArrayList<Document>();

		for (Nota nota : notas) {
			Document documento = new Document();
			documento.append(ORIGINAL, getAvaliavel(nota.getItemOriginal()));
			documento.append(DESTINO, getAvaliavel(nota.getItemNovo()));
			documento.put(JUSTIFICATIVA, nota.getJustificativa());
			notasDocument.add(documento);
		}

		return notasDocument;
	}
    /**
     * Monta o documento a partir de um objeto classe Avaliavel passado no parametro.
     * Como Avaliavel é uma interface, há uma verificação de qual classe o objeto é uma instância 
     *
     * @param avaliavel O objeto da classe Avaliavel usado para montagem do documento
     *
     * @return um objeto Document com informações do objeto passado
     * 
     * @see getAvaliavel(Document avaliavelDocument)
     */
	private Document getAvaliavel(Avaliavel avaliavel) {
		Document avaliavelDocument = new Document();
		
		if (avaliavel instanceof Relato) {

			avaliavelDocument.put(TIPO_ID, ((Relato) avaliavel).getTipo());
			avaliavelDocument.append(LISTA_VALORES, getListaValores((Relato) avaliavel));

		} else {

			String atributo = ((Pontuacao) avaliavel).getAtributo();
			avaliavelDocument.put(ATRIBUTO, atributo);
			avaliavelDocument.append(VALOR, ((Pontuacao) avaliavel).getValor().getString());
		}

		return avaliavelDocument;
	}
}
