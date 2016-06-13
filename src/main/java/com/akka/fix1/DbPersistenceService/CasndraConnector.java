package com.akka.fix1.DbPersistenceService;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Metadata;
import com.datastax.driver.core.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * Class used for connecting to Cassandra database.
 */
public class CasndraConnector
{
   /** Cassandra Cluster. */
   private static Cluster cluster = null;
   /** Cassandra Session. */
   private static Session session = null;
   private static Logger log = LoggerFactory.getLogger(CasndraConnector.class);
   /**
    * Connect to Cassandra Cluster specified by provided node IP
    * address and port number.
    *
    * @param node Cluster node IP address.
    * @param port Port of cluster host.
    */
    
   /*public void connect(final String node, final int port)
   {
      this.cluster = Cluster.builder().addContactPoint(node).withPort(port).build();
      final Metadata metadata = cluster.getMetadata();
      out.printf("Connected to cluster: %s\n", metadata.getClusterName());
      for (final Host host : metadata.getAllHosts())
      {
         out.printf("Datacenter: %s; Host: %s; Rack: %s\n",
            host.getDatacenter(), host.getAddress(), host.getRack());
      }
      session = cluster.connect();
   }
   *//**
    * Provide my Session.
    *
    * @return My session.
    *//*
   public Session getSession()
   {
      return this.session;
   }
   *//** Close cluster. */
   public static void close()
   {
	  log.info("Connection Closed--");
      cluster.close();
   }
   public static Session getSession() {
	// TODO Auto-generated method stub
	String node = "127.0.0.1";
	   int port = 9042;
	if(cluster==null && session==null){
		cluster = Cluster.builder().addContactPoint(node).withPort(port).build();
		final Metadata metadata = cluster.getMetadata();
		log.info("Connected to cluster: ", metadata.getClusterName());
		session = cluster.connect();
	}
	return session;
   }   
}