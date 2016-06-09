package com.akka.fix1.DbPersistenceService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
 
public class MongoDBConnector {
	private static MongoClient mongoClient = null;
	private static DB db2=null;
	
 	private static Logger log = LoggerFactory.getLogger(MongoDBConnector.class);
    @SuppressWarnings("deprecation")
	public static DB getConnection(){
    	String dbURI = "mongodb://dbuser1:dbpwd1@ds043972.mlab.com:43972/akkadb";
    	if(mongoClient==null && db2==null){
    		mongoClient = new MongoClient(new MongoClientURI(dbURI));
    		db2 = mongoClient.getDB("akkadb");
    		log.info("First Time Connection done");
    	}
    	return db2;
    }
    
    public static void closeConnection(){
    	log.info("Connection Closed--");
    	mongoClient.close();
    }
}