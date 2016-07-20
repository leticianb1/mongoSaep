package br.ufg.inf.es.saep.sandbox.mongo;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;

import br.ufg.inf.es.saep.sandbox.dominio.ResolucaoRepository;
/**
 * Classe de utilidades para conexão e configuração do MongoDB
 *
 */
public class DataUtil {
    /**
     * Recupera o banco de dados no MongoDB para registro das informações do SAEP
     *
     * @return o banco de dados correspondente ao nome, de acordo com o endereço usado. Caso não exista, o MongoDB cria a base de dados automaticamente
     */
	public static MongoDatabase getMongoDb(){
		MongoClient mongoClient = new MongoClient(getProperties("mongo.url"), Integer.parseInt(getProperties("mongo.port")));

		return  mongoClient.getDatabase(getProperties("mongo.database"));
	}
    /**
     * Método para auxiliar a recuperação de propriedades do documento de configurações criado
     * @param name Nome da propriedade esperada
     *
     * @return o valor correspondente no arquivo de configurações disponível no caminho: "resources/config.properties"
     */
    public static String getProperties(String name) {
        Properties properties = new Properties();
        String value = null;
        try {
            properties.load(new FileInputStream("resources/config.properties"));
            value = properties.getProperty(name);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return value;
    }
}
