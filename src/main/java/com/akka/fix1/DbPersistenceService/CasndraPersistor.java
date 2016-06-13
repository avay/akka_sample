package com.akka.fix1.DbPersistenceService;

import com.datastax.driver.core.ResultSet;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.Procedure;
import akka.persistence.SnapshotOffer;
import akka.persistence.UntypedEventsourcedProcessor;

public class CasndraPersistor extends UntypedEventsourcedProcessor {

	LoggingAdapter log = Logging.getLogger(getContext().system(), this);
	private CasndraProcState casndraProcState = new CasndraProcState();

	@Override
	public void onReceiveCommand(Object arg0) {
		log.info("Persisting Data in Casndra DB:");
		if(arg0 instanceof UsrDetail){
			
			//final ActorRef actorOf = getContext().system().actorOf(Props.create(BaseProcessor.class));
			casndraProcState.update((UsrDetail)arg0);
			log.info("Cassandra Processor Status update--Invoking  to ");
			persist((UsrDetail)arg0, new Procedure<UsrDetail>(){
				public void apply(UsrDetail arg0) throws Exception {
					UsrDAOImp daoImp = new UsrDAOImp();
					ResultSet cassandra_resp = daoImp.insrtUsrDetails((UsrDetail)arg0, CasndraConnector.getSession());
					sender().tell(cassandra_resp, getSelf());
				}
			});
			log.info("Data Persisted");
		} else if(arg0.equals("snapshot")){
			saveSnapshot(casndraProcState.copy());
		}
	}

	@Override
	public void onReceiveRecover(Object msg) {
		log.info("Received Recover: " + msg);
        if (msg instanceof UsrDetail) {
        	casndraProcState.update((UsrDetail) msg);
            log.info("Processor state updated on recover----"+ casndraProcState.toString());

        } else if (msg instanceof SnapshotOffer) {
        	casndraProcState = (CasndraProcState) ((SnapshotOffer) msg).snapshot();
        }
	}

}
