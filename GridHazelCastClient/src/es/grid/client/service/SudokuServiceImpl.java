package es.grid.client.service;

import java.util.Arrays;
import java.util.concurrent.CountDownLatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.hazelcast.config.Config;
import com.hazelcast.config.NetworkConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;

import es.grid.client.bean.BWrapper;
import es.grid.client.bean.InmutableInt;
import es.grid.client.controller.SudokuController;

//https://stackoverflow.com/questions/30883345/hazelcast-connecting-to-remote-cluster
//https://www.gridgain.com/resources/blog/benchmarking-data-grids-apache-ignite-vs-hazelcast-part-ii

@Service
@CacheConfig(cacheNames = "sudokus")
public class SudokuServiceImpl implements SudokuService {
	
	private final Logger logger = LoggerFactory.getLogger(SudokuService.class);
	
	@Autowired
	HazelcastInstance hInstance;
	
	@Value("${cluster.number}")
	int numClusters;
	
	@Override
	@Cacheable()
	public BWrapper solveSudoku(BWrapper wrapper) {
		
		logger.info("[SudokuService] -- solveSudoku INIT");
		
		IMap<String, BWrapper> nodes = hInstance.getMap("matrixSudokus");
		
		logger.info("[SudokuService] Data sent to cluster (" + wrapper.toString()+ ")");
		nodes.put("data", wrapper);
		
		final BWrapper retWrapper = new BWrapper();
		final InmutableInt iInt = new InmutableInt();
		
		try{
			CountDownLatch latch = new CountDownLatch(numClusters);
	        //for (int i = 0; i < numClusters; i++) {
	        	
	             new Thread(() -> {
	            	 
	            	 BWrapper dataWrapper = null;
	            	 
	            	 for (int i=0;i<numClusters; i++){
		            	 while(dataWrapper == null){dataWrapper = nodes.get(String.valueOf(i+1));}	            	 
		            	 if (dataWrapper!=null){
		            		 logger.info("[SudokuService] Data received from cluster ["+String.valueOf(i+1)+"] (" + dataWrapper.toString() + ")");
		            		 retWrapper.setClone(dataWrapper);
		            	 }
		            	 
		            	 latch.countDown();
	            	 }
	            	 
	             }).start();
	        //}
	        latch.await();
		}catch(InterruptedException iE){
			logger.warn("[SudokuService] -- error (" + iE.getMessage() + ")");
		}
		
		return retWrapper;
	}
	
	
	
	
	
	
	
}
