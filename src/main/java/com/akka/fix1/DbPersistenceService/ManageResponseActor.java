package com.akka.fix1.DbPersistenceService;

import java.util.ArrayList;
import java.util.List;

import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class ManageResponseActor extends UntypedActor {
	
	LoggingAdapter log = Logging.getLogger(getContext().system(), this);
	public List<Object> task = new ArrayList<Object>();

	@Override
	public void onReceive(Object arg0) throws Exception {
		log.info("Handled Task::"+ arg0);
		if(arg0 instanceof Object){
			task.add(arg0);
		}
	}

}
