package br.ufg.inf.es.saep.sandbox.mongo;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;

public class DataUtil {
	MongoDatabase mongoDB = getMongoDb();
	
	public static MongoDatabase getMongoDb(){
		MongoClient mongoClient = new MongoClient(getProperties("mongo.url"), Integer.parseInt(getProperties("mongo.port")));

		return  mongoClient.getDatabase(getProperties("mongo.database"));
	}
	
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
