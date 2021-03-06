package br.ufg.inf.es.saep.sandbox.mongo;

import static com.mongodb.client.model.Filters.eq;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bson.Document;

import com.mongodb.Block;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.DeleteResult;

import br.ufg.inf.es.saep.sandbox.dominio.Atributo;
import br.ufg.inf.es.saep.sandbox.dominio.IdentificadorExistente;
import br.ufg.inf.es.saep.sandbox.dominio.Regra;
import br.ufg.inf.es.saep.sandbox.dominio.Resolucao;
import br.ufg.inf.es.saep.sandbox.dominio.ResolucaoRepository;
import br.ufg.inf.es.saep.sandbox.dominio.ResolucaoUsaTipoException;
import br.ufg.inf.es.saep.sandbox.dominio.Tipo;

/**
 * Classe que implementa a interface de ResolucaoRepository para o uso do MongoDB para armazenamento dos dados
 * Além dos métodos da interface, métodos de apoio foram criados para facilitar a transição de instancias das Classes de Domínio do SAEP
 * para classes de Document que são usadas para persistência na base de dados do Mongo
 *
 * @see ResolucaoRepository
 */
public class ResolucaoRepositoryMongo implements ResolucaoRepository {

	static String COLECAO_RESOLUCAO = "resolucoes";
	static String COLECAO_TIPO = "tipos";

	static String TIPO_ID = "tipoID";
	static String TIPO_INT = "tipoInt";
	static String NOME = "nome";
	static String DESCRICAO = "descricao";
	static String ATRIBUTOS = "atributos";

	static String DATA_APROVACAO = "dataAprovacao";

	static String REGRA_ID = "regraId";
	static String LISTA_REGRAS = "regras";
	static String VARIAVEL = "variavel";
	static String RESOLUCAO_ID = "resolucaoId";
	static String VALOR_MAXIMO = "valorMaximo";
	static String VALOR_MINIMO = "valorMinimo";

	static String EXPRESSAO = "expressao";
	static String ENTAO = "entao";
	static String SENAO = "senao";
	static String PONTOS_POR_ITEM = "pontosPorItem";
	static String DEPENDE = "dependeDe";

	@Override
	public Resolucao byId(String id) {
		MongoCollection<Document> collection = DataUtil.getMongoDb().getCollection(COLECAO_RESOLUCAO);

		FindIterable<Document> find = collection.find(eq(RESOLUCAO_ID, id));
	
		if (find == null || find.first() == null || find.first().isEmpty()) {
			return null;
		}

		String resolucaoId = find.first().getString(RESOLUCAO_ID);
		String nome = find.first().getString(NOME);
		String descricao = find.first().getString(DESCRICAO);
		Date dataAprovacao = find.first().getDate(DATA_APROVACAO);
		List<Document> regrasDocument = (List<Document>) find.first().get(LISTA_REGRAS);
		List<Regra> regras = new ArrayList<Regra>();

		for (Document document : regrasDocument) {
			String variavel = document.getString(VARIAVEL);
			int tipo = document.getInteger(TIPO_INT);
			String descricaoDaRegra = document.getString(DESCRICAO);
			float valorMaximo = Float.parseFloat(document.getString(VALOR_MAXIMO));
			float valorMinimo = Float.parseFloat(document.getString(VALOR_MINIMO));

			String entao = "";
			String senao = "";
			String expressao = "";
			String tipoRelato = "";
			float pontosPorItem = 0;

			List<String> dependeDe = new ArrayList<String>();

			if (tipo == Regra.PONTOS) {
				tipoRelato = document.getString(TIPO_ID);
				pontosPorItem = Float.parseFloat(document.getString(PONTOS_POR_ITEM));
			} else {
				expressao = document.getString(EXPRESSAO);
				dependeDe = (List<String>) document.get(DEPENDE);
			}

			if (tipo == Regra.CONDICIONAL) {
				entao = document.getString(ENTAO);
				senao = document.getString(SENAO);
			}

			Regra regra = new Regra(variavel, tipo, descricaoDaRegra, valorMaximo, valorMinimo, expressao, entao, senao,
					tipoRelato, pontosPorItem, dependeDe);

			regras.add(regra);
		}

		Resolucao resolucao = new Resolucao(resolucaoId, nome, descricao, dataAprovacao, regras);

		return resolucao;
	}

	@Override
	public String persiste(Resolucao resolucao) {
		MongoCollection<Document> collection = DataUtil.getMongoDb().getCollection(COLECAO_RESOLUCAO);

		Document documento = criarDocumentoDaResolucao(resolucao);

		if (byId(resolucao.getId()) == null) {
			collection.insertOne(documento);

		} else {
			throw new IdentificadorExistente(RESOLUCAO_ID);
		}

		return collection.find(documento).first().getString(RESOLUCAO_ID);
	}

	@Override
	public boolean remove(String identificador) {
		MongoCollection<Document> collection = DataUtil.getMongoDb().getCollection(COLECAO_RESOLUCAO);

		DeleteResult deletou = collection.deleteOne(eq(RESOLUCAO_ID, identificador));

		if (deletou.getDeletedCount() >= 1) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public List<String> resolucoes() {
		MongoCollection<Document> collection = DataUtil.getMongoDb().getCollection(COLECAO_RESOLUCAO);

		FindIterable<Document> find = collection.find();
		List<String> resolucoes = new ArrayList<>();

		for (Document document : find) {
			resolucoes.add(document.getString(RESOLUCAO_ID));
		}

		return resolucoes;
	}

	@Override
	public void persisteTipo(Tipo tipo) {
		MongoCollection<Document> listaTipos = DataUtil.getMongoDb().getCollection("tipos");
		Document documento = new Document();
		
		if (tipoPeloCodigo(tipo.getId()) == null) {
			documento.put(TIPO_ID, tipo.getId());
			documento.put(DESCRICAO, tipo.getDescricao());
			documento.put(NOME, tipo.getNome());
			documento.append(ATRIBUTOS, getListaAtributos(tipo.getAtributos()));

			listaTipos.insertOne(documento);

		} else {
			throw new IdentificadorExistente(TIPO_ID);
		}
	}

	@Override
	public void removeTipo(String codigo) {
		MongoCollection<Document> collection = DataUtil.getMongoDb().getCollection(COLECAO_TIPO);
		MongoCollection<Document> collectionResolucao = DataUtil.getMongoDb().getCollection(COLECAO_RESOLUCAO);

		FindIterable<Document> find = collectionResolucao.find(eq(TIPO_ID, codigo));

		if (find == null || find.first() == null || find.first().isEmpty()) {
			collection.deleteOne(eq(TIPO_ID, codigo));
		} else {
			throw new ResolucaoUsaTipoException("Alguma resolucao faz referencia a esse tipo: " + TIPO_ID);
		}
	}

	@Override
	public Tipo tipoPeloCodigo(String codigo) {
		MongoCollection<Document> collection = DataUtil.getMongoDb().getCollection(COLECAO_TIPO);

		FindIterable<Document> find = collection.find(eq(TIPO_ID, codigo));

		if (find == null || find.first() == null || find.first().isEmpty()) {
			return null;
		}

		String id = find.first().getString(TIPO_ID);
		String nome = find.first().getString(NOME);
		String descricao = find.first().getString(DESCRICAO);
		List<Document> atributosDocument = (List<Document>) find.first().get(ATRIBUTOS);
		Set<Atributo> atributos = new HashSet<Atributo>();
		
		for (Document document : atributosDocument) {
			String nomeAtributo = document.getString(NOME);
			String descricaoAtributo = document.getString(DESCRICAO);
			int tipoInt = document.getInteger(TIPO_INT);
			Atributo atr = new Atributo(nomeAtributo, descricaoAtributo, tipoInt);
			atributos.add(atr);
		}

		Tipo tipo = new Tipo(id, nome, descricao, atributos);

		return tipo;
	}

	@Override
	public List<Tipo> tiposPeloNome(String nome) {
		MongoCollection<Document> collection = DataUtil.getMongoDb().getCollection("tipos");

		FindIterable<Document> find = collection.find(eq(NOME, nome));
		
		if (find == null || find.first() == null || find.first().isEmpty()) {
			return null;
		}

		List<Tipo> listaTipos = new ArrayList<Tipo>();

		Block<Document> tiposBlock = new Block<Document>() {
			@Override
			public void apply(final Document document) {
				String id = find.first().getString(TIPO_ID);
				String nome = find.first().getString(NOME);
				String descricao = find.first().getString(DESCRICAO);
				List<Document> atributosDocument = (List<Document>) find.first().get(ATRIBUTOS);
				Set<Atributo> atributos = new HashSet<Atributo>();
				
				for (Document documentAtr : atributosDocument) {
					String nomeAtributo = documentAtr.getString(NOME);
					String descricaoAtributo = documentAtr.getString(DESCRICAO);
					int tipoInt = documentAtr.getInteger(TIPO_INT);
					Atributo atr = new Atributo(nomeAtributo, descricaoAtributo, tipoInt);
					atributos.add(atr);
				}

				Tipo tipo = new Tipo(id, nome, descricao, atributos);
				listaTipos.add(tipo);
			}
		};

		find.forEach(tiposBlock);

		return listaTipos;

	}
    /**
     * Monta uma lista de documentos do MongoDB a partir de uma lista de regras passada
     * Para criação das propriedades do documento são levadas em consideração as restrições de propriedades da classe Regra,
     * de acordo com o tipo de Regra definido
     * Para criar cada documento, cada propriedade de Regra é salva no formato de "nomeDoCampo": "valor", semelhante a documentos Json
     * @param regras Uma lista de Regras (lista de objetos Regra)
     *
     * @return uma lista de Document correspondente a lista de Regra passada
     */
	private ArrayList<Document> getListaRegras(List<Regra> regras) {
		ArrayList<Document> regrasDocument = new ArrayList<Document>();

		for (Regra regra : regras) {
			Document documento = new Document();
			documento.put(VARIAVEL, regra.getVariavel());
			documento.put(TIPO_INT, regra.getTipo());
			documento.put(DESCRICAO, regra.getDescricao());
			documento.put(VALOR_MAXIMO, String.valueOf(regra.getValorMaximo()));
			documento.put(VALOR_MINIMO, String.valueOf(regra.getValorMinimo()));

			if (regra.getTipo() == Regra.PONTOS) {
				documento.put(TIPO_ID, regra.getTipoRelato());
				documento.put(PONTOS_POR_ITEM, String.valueOf(regra.getPontosPorItem()));
			} else {
				documento.put(EXPRESSAO, regra.getExpressao());
				documento.put(DEPENDE, regra.getDependeDe());
			}
			if (regra.getTipo() == Regra.CONDICIONAL) {
				documento.put(ENTAO, regra.getEntao());
				documento.put(SENAO, regra.getSenao());
			}

			regrasDocument.add(documento);
		}

		return regrasDocument;
	}

    /**
     * Monta um documento do MongoDB a partir do objeto instanciado de Resolucao
     * Para criar o documento, cada propriedade de Resolucao é salva no formato de "nomeDoCampo": "valor", semelhante a documentos Json
     * @param resolucao Um objeto de resolucao instanciado
     *
     * @return uma objeto Document correspondente (com os atributos de) ao objeto Resolucao recebido como parametro
     */
	private Document criarDocumentoDaResolucao(Resolucao resolucao) {
		Document documento = new Document();
		documento.put(RESOLUCAO_ID, resolucao.getId());
		documento.put(NOME, resolucao.getNome());
		documento.put(DESCRICAO, resolucao.getDescricao());
		documento.put(DATA_APROVACAO, resolucao.getDataAprovacao());
		documento.append(LISTA_REGRAS, getListaRegras(resolucao.getRegras()));

		return documento;
	}
	
    /**
     * Monta uma lista de documentos do MongoDB a partir de um conjunto (Set) de atributos passada
     * Para criar o documento, cada propriedade de Atributo é salva no formato de "nomeDoCampo": "valor", semelhante a documentos Json
     * @param regras Um conjunto de atributos (Set de objetos Atributo)
     *
     * @return uma lista de Document correspondente ao conjunto de Atributo
     */
	private ArrayList<Document> getListaAtributos(Set<Atributo> colecaoAtributo) {
		ArrayList<Document> atributosDocument = new ArrayList<Document>();

		for (Atributo atributo : colecaoAtributo) {
			Document documento = new Document();
			documento.put(TIPO_INT, atributo.getTipo());
			documento.put(DESCRICAO, atributo.getDescricao());
			documento.put(NOME, atributo.getNome());
			
			atributosDocument.add(documento);
		}
		
		return atributosDocument;
	}
}
