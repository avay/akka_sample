package com.akka.fix1.DbPersistenceService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.WriteResult;

public class UserDAOImp {
	
	Logger logger = LoggerFactory.getLogger(UserDAOImp.class);
	
	public  WriteResult insetUserDetails(UserDetail user, DB db){
		logger.debug("Inserting UserDetail----->");
		WriteResult result=null; 
		try {
        	DBCollection table = db.getCollection("mycollection");
            BasicDBObject document = new BasicDBObject();
        	document.put("name", user.getName());
        	document.put("age", user.getAge());
        	document.put("sex", user.getSex());
        	document.put("company", user.getCompany());
        	result = table.insert(document);

        	logger.debug("Inserted in Mongo DB--->" + result);
        } catch (Exception ex) {
        	System.out.println(ex.getMessage());
        }
		return result;
    }
}
