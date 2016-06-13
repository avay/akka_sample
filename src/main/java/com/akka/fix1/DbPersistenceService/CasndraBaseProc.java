package com.akka.fix1.DbPersistenceService;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.datastax.driver.core.ResultSet;


import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class CasndraBaseProc extends UntypedActor {

    LoggingAdapter log = Logging.getLogger(getContext().system(), this);
    private List<Object> task = new ArrayList<Object>();
    private List<UsrDetail> usrDetails = new ArrayList<UsrDetail>();
    /**
     * The state of the processor
     */
    //private CasndraProcState casndraProcState = new CasndraProcState();
    public void onReceiveCommand(Object msg) {/*

    */    }

    	private List<UsrDetail> loadCsvFile() {
        	String csvFile = "E:/eclipse-luna/usr.csv";
        	BufferedReader br = null;
        	String line = "";
        	String cvsSplitBy = ",";
        	String[] userDtl = null;
        	usrDetails = new ArrayList<UsrDetail>();
        	UsrDetail usrdetail = null;

        	try {
        		br = new BufferedReader(new FileReader(csvFile));
        		while ((line = br.readLine()) != null) {
        			userDtl = line.split(cvsSplitBy);
        			usrdetail = new UsrDetail();
        			usrdetail.setId(Integer.parseInt(userDtl[0]));
        			usrdetail.setFname(userDtl[1]);
        			usrdetail.setLname(userDtl[2]);
        			usrDetails.add(usrdetail);
        		}

        	} catch (FileNotFoundException e) {
        		e.printStackTrace();
        	} catch (IOException e) {
        		e.printStackTrace();
        	} finally {
        		if (br != null) {
        			try {
        				br.close();
        			} catch (IOException e) {
        				e.printStackTrace();
        			}
        		}
        	}
    		return usrDetails;
    	}

    	@Override
    	public void onReceive(Object msg) throws Exception {
    		log.info("Received UserDetails: "+msg);
        	if(msg.equals("Persist in DB")){
        		//int count=0;
        		List<UsrDetail> users = loadCsvFile();
        		log.info("Lists size :"+users.size());
        		final ActorRef actor = getContext().system().actorOf(Props.create(CasndraPersistor.class));
        		for (UsrDetail user : users) {
        				log.info("Publishing user and tell to Eventsource processor");
        				getContext().system().eventStream().publish(user);
        				actor.tell(user, getSelf());
        		}
        	} else if(msg instanceof ResultSet){
        		log.debug("Executing Command to check no of insertion");
        		task.add(msg);
        		if(task.size() == usrDetails.size()){
        			CasndraConnector.close();
        			getContext().system().shutdown();
        		}
        	 } 
    	}
    }