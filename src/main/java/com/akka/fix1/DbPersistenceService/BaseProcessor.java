/**
 * (C) Copyright 2014 Roy Russo
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 *
 *
 */

package com.akka.fix1.DbPersistenceService;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.mongodb.WriteResult;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;


/**
 * Processor receives an instance of Command. Event is generated from this and persisted. On successful
 * persist, it updates the state of the processor. This allows a complete recovery of state in case of failure
 * by replaying the events that are in the journal and snapshot(s).
 *
 * @author Raj
 */
public class BaseProcessor extends UntypedActor {

    LoggingAdapter log = Logging.getLogger(getContext().system(), this);
    private List<Object> task = new ArrayList<Object>();
    private List<UserDetail> userDetails = new ArrayList<UserDetail>();
    /**
     * The state of the processor
     */
    private ProcessorState processorState = new ProcessorState();

    /**
     * Called on restart. Loads from Snapshot first, and then replays Journal Events to update state.
     *
     * @param msg
     */
    /*public void onReceiveRecover(Object msg) {
        log.info("Received Recover: " + msg);
        if (msg instanceof Event) {
            processorState.update((Event) msg);

        } else if (msg instanceof SnapshotOffer) {
            processorState = (ProcessorState) ((SnapshotOffer) msg).snapshot();
        }
    
    	log.info("Received Recover: " + msg);
        if (msg instanceof UserDetail) {
            processorState.update((UserDetail) msg);

        } else if (msg instanceof SnapshotOffer) {
            processorState = (ProcessorState) ((SnapshotOffer) msg).snapshot();
        }
	
    }
*/
    /**
     * Called on Command dispatch
     *
     * @param msg
     */
    public void onReceiveCommand(Object msg) {/*

*/    }

	private List<UserDetail> loadCsvFile() {
    	String csvFile = "E:/eclipse-luna/user.csv";
    	BufferedReader br = null;
    	String line = "";
    	String cvsSplitBy = ",";
    	String[] userDtl = null;
    	userDetails = new ArrayList<UserDetail>();
    	UserDetail userdetail = null;

    	try {
    		br = new BufferedReader(new FileReader(csvFile));
    		while ((line = br.readLine()) != null) {
    			userDtl = line.split(cvsSplitBy);
    			userdetail = new UserDetail();
    			userdetail.setName(userDtl[0]);
    			userdetail.setAge(Integer.parseInt(userDtl[1]));
    			userdetail.setSex(userDtl[2]);
    			userdetail.setCompany(userDtl[3]);
    			userDetails.add(userdetail);
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
		return userDetails;
	}

	@Override
	public void onReceive(Object msg) throws Exception {
		log.info("Received UserDetails: "+msg);
    	if(msg.equals("Persist in DB")){
    		int count=0;
    		List<UserDetail> users = loadCsvFile();
    		log.info("Lists size :"+users.size());
    		final ActorRef actor = getContext().system().actorOf(Props.create(DBPersistentActor.class));
    		for (UserDetail user : users) {
    				log.info("Publishing user and tell to Evensource processor");
    				getContext().system().eventStream().publish(user);
    				actor.tell(user, getSelf());
    		}
    	} else if(msg instanceof WriteResult){
    		log.debug("Executing Command to check no of insertion");
    		task.add(msg);
    		if(task.size() == userDetails.size()){
    			MongoDBConnector.closeConnection();
    			getContext().system().shutdown();
    		}
    	 } 
	}
}
