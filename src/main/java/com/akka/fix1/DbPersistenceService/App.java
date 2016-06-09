package com.akka.fix1.DbPersistenceService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

/**
 * App
 *
 */
public class App 
{
	
	public static final Logger log = LoggerFactory.getLogger(App.class);
	
    public static void main( String[] args ) throws Exception
    {
    	final ActorSystem actorSystem = ActorSystem.create("actor-server");

        final ActorRef handler = actorSystem.actorOf(Props.create(EventHandler.class));
        actorSystem.eventStream().subscribe(handler, UserDetail.class);

        try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

        final ActorRef actorRef = actorSystem.actorOf(Props.create(BaseProcessor.class), "base-processor");
        actorRef.tell("Persist in DB", null);

    	
    }
}
