package br.ufg.inf.es.saep.sandbox.mongo;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;

public class DataUtil {
	MongoDatabase mongoDB = getMongoDb();
	
	public static MongoDatabase getMongoDb(){

		MongoClient mongoClient = new MongoClient("localhost", 27017);
		
		return  mongoClient.getDatabase("saepDB");
	}
}
