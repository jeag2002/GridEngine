package es.grid.client;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.Future;

import com.hazelcast.com.eclipsesource.json.JsonObject.Member;
import com.hazelcast.config.Config;
import com.hazelcast.config.ExecutorConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IExecutorService;
import com.hazelcast.core.IMap;
import com.hazelcast.core.IQueue;

import es.grid.client.bean.BWrapper;
import es.grid.client.task.DistTask;

public class Main {

	public static void main(String[] args) throws Exception{
		// TODO Auto-generated method stub
		
		
		Config config = new Config();
		ExecutorConfig executorConfig = config.getExecutorConfig("exec");
		executorConfig.setPoolSize( 1 ).setQueueCapacity( 2 ).setStatisticsEnabled( true);
		
		HazelcastInstance hazelcastInstance = Hazelcast.newHazelcastInstance();

		BWrapper bw = new BWrapper();
		bw.setSizeX("10");
		bw.setSizeY("10");
		bw.setData("100100100100");
		bw.setRes(BWrapper.OK);
		bw.setErrMsg("MAIN");
		
		IMap<String, BWrapper> nodes = hazelcastInstance.getMap("sudokus");
		IQueue<BWrapper> queue =  hazelcastInstance.getQueue("sudokuQ");
		
		DistTask dt = new DistTask();
		dt.setHazelcastInstance(hazelcastInstance);
		nodes.set("data",bw);
		
		IExecutorService executorService = hazelcastInstance.getExecutorService("exec");
		Map<com.hazelcast.core.Member, Future<String>> futures = executorService.submitToAllMembers(dt);
		
		String result = null;	
		String result_1 = "";
		
		//while(true){
			
			
		Collection<Future<String>> data = futures.values();
		Iterator<Future<String>> it = data.iterator();
			
		while(it.hasNext()){
				
				bw = queue.poll();
				
				if (bw != null){
					System.out.println("[MAIN] bw-pollQ (" +bw.toString()+ ")");
				}
				
				
				result = it.next().get();
				if (result != null){
					if (!result_1.equalsIgnoreCase(result)){
						System.out.println("[MAIN] result " + result);
						result_1 = result;
					}
				}
		}
		//}
		
		
		
	}

}
