package com.akka.fix1.DbPersistenceService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;

public class UsrDAOImp {
	
Logger logger = LoggerFactory.getLogger(UsrDAOImp.class);
	
	public ResultSet insrtUsrDetails(UsrDetail user, Session ss){
		logger.debug("Inserting UsrDetail----->");
		//WriteResult result = null; 
		ResultSet results = null;
		try {
			results = ss.execute("INSERT INTO testkeyspace.usr (id, fname, lname) VALUES (?, ?, ?)",
				      user.getId(), user.getFname(), user.getLname());
        	logger.debug("Inserted in cassandra DB--->" + results);
        } catch (Exception ex) {
        	System.out.println(ex.getMessage());
        }
		return results;
    }

}
