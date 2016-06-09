package com.akka.fix1.DbPersistenceService;

import com.mongodb.WriteResult;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.Procedure;
import akka.persistence.SnapshotOffer;
import akka.persistence.UntypedEventsourcedProcessor;

public class DBPersistentActor extends UntypedEventsourcedProcessor {

	LoggingAdapter log = Logging.getLogger(getContext().system(), this);
	private ProcessorState processorState = new ProcessorState();

	@Override
	public void onReceiveCommand(Object arg0) {
		log.info("Persisting Data in DB:");
		if(arg0 instanceof UserDetail){
			
			//final ActorRef actorOf = getContext().system().actorOf(Props.create(BaseProcessor.class));
			processorState.update((UserDetail)arg0);
			log.info("Processor Status update--Invoking  to ");
			persist((UserDetail)arg0, new Procedure<UserDetail>(){
				public void apply(UserDetail arg0) throws Exception {
					UserDAOImp daoImp = new UserDAOImp();
					WriteResult mongo_resp = daoImp.insetUserDetails((UserDetail)arg0,MongoDBConnector.getConnection());
					sender().tell(mongo_resp, getSelf());
				}
			});
			log.info("Data Persisted");
		} else if(arg0.equals("snapshot")){
			saveSnapshot(processorState.copy());
		}
	}

	@Override
	public void onReceiveRecover(Object msg) {
		log.info("Received Recover: " + msg);
        if (msg instanceof UserDetail) {
            processorState.update((UserDetail) msg);
            log.info("Processor state updated on recover----"+processorState.toString());

        } else if (msg instanceof SnapshotOffer) {
            processorState = (ProcessorState) ((SnapshotOffer) msg).snapshot();
        }
	}

}
