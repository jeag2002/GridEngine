package es.grid.client.service;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.CountDownLatch;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCluster;
import org.apache.ignite.IgniteCompute;
import org.apache.ignite.IgniteException;
import org.apache.ignite.IgniteMessaging;
import org.apache.ignite.Ignition;
import org.apache.ignite.cluster.ClusterGroup;
import org.apache.ignite.lang.IgniteCallable;
import org.gridgain.grid.Grid;
import org.gridgain.grid.lang.GridCallable;

//https://github.com/gridgain/gridgain/blob/master/examples/config/example-compute.xml
//https://github.com/gridgain/gridgain/blob/master/examples/src/main/java/org/gridgain/examples/compute/ComputeBroadcastExample.java
//https://github.com/gridgain/gridgain/blob/master/examples/src/main/java/org/gridgain/examples/compute/ComputeTaskSplitExample.java

//https://github.com/apache/ignite/blob/master/examples/src/main/java/org/apache/ignite/examples/messaging/MessagingExample.java
	
	
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.grid.client.bean.BWrapper;

@Service
public class SudokuServiceImpl implements SudokuService {
	
	private final Logger logger = LoggerFactory.getLogger(SudokuService.class);
	private BWrapper response = null;
	
	//@Autowired
	//Grid grid;
	
	@Override
	public BWrapper solveSudoku(BWrapper wrapper) {
		
		logger.debug("[SudokuService] -- INI");		
		
		
		
		try{
			ClassLoader classLoader = getClass().getClassLoader();
			URL url = classLoader.getResource("example-ignite.xml");
			Ignite ignite = Ignition.start(url);
			
			ClusterGroup rmtGrp = ignite.cluster().forRemotes();
			int msgCnt = rmtGrp.nodes().size();
			
			CountDownLatch orderedLatch = new CountDownLatch(msgCnt);
			
		    localListen(ignite.message(ignite.cluster().forLocal()), orderedLatch);
			 
			startListening(ignite, ignite.message(rmtGrp));
			
			ignite.message(rmtGrp).sendOrdered("ORDERED", wrapper,0);
			
			orderedLatch.await();
			
		}catch(Exception e){
		}
		
		
		
		
		
		
		/*
		BWrapper response = null;
		try{
		
		Collection<GridCallable<BWrapper>> calls = new ArrayList<>();
		
		calls.add(
				new GridCallable<BWrapper>(){
					@Override
					public BWrapper call() throws Exception{
						logger.info("[SudokuService-node] get (" + wrapper.toString() + ")");
						return wrapper;
					}
					
				}
				);
		
		Collection<BWrapper> data = grid.compute().call(calls).get();
		response = data.iterator().next();
		
		}catch(Exception e){
			logger.warn("[SudokuService] Exception (" + e.getMessage() + ")");
		}
		*/
		
		
		return response;
		 
	}
	
    private void startListening(final Ignite ignite, IgniteMessaging imsg) throws IgniteException {
        // Add ordered message listener.
        imsg.remoteListen("ORDERED", (nodeId, msg) -> {
           System.out.println("[Ignite REMOTE LISTENER] get message from [msg=" + msg + ", fromNodeId=" + nodeId + ']');

            try {
                ignite.message(ignite.cluster().forNodeId(nodeId)).send("ORDERED", msg);
            }catch (IgniteException e) {
                e.printStackTrace();
            }

            return true; // Return true to continue listening.
        });

    }

	private void localListen(
	        IgniteMessaging imsg,
	        final CountDownLatch orderedLatch
	    ) {
	        imsg.localListen("ORDERED", (nodeId, msg) -> {
	        	if (msg instanceof BWrapper){
	        		response = (BWrapper)msg;
	        		System.out.println("[Ignite LOCAL LISTENER] get message from [msg=" + response.toString()+ ", fromNodeId= '" +nodeId+ "']");
	        	}
	        	orderedLatch.countDown();
	            // Return true to continue listening, false to stop.
	            return orderedLatch.getCount() > 0;
	        });

	    }
	
	
	
	
	
	
	
}
