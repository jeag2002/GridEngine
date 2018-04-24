package es.grid.client;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Future;

import com.hazelcast.config.Config;
import com.hazelcast.config.ExecutorConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IExecutorService;
import com.hazelcast.core.IMap;
import com.hazelcast.core.IQueue;
import com.hazelcast.core.Member;

import es.grid.client.bean.BWrapper;
import es.grid.client.task.DistTask;

public class Main {
	
	private static final String THIS = "this";

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
		
		
		//Execute task parallel to ALL MEMBERS
		/////////////////////////////////////////////////////////////////////////////////////
		IExecutorService executorService = hazelcastInstance.getExecutorService("exec");
		Map<Member, Future<String>> futures = executorService.submitToAllMembers(dt);
		/////////////////////////////////////////////////////////////////////////////////////
		
		//Execute task in parallel to SELECTED MEMBERS (in this case, to all node except main node)
		///////////////////////////////////////////////////////////////////////////////////////
		/*
		Set<Member> all = hazelcastInstance.getCluster().getMembers();
		List<Member> named = new ArrayList<Member>(all.size());
		
		for (Member member: all) {
			System.out.println("Member of cluster [" + member.toString() + "]");
			String memberStr = member.toString();
			if (memberStr.trim().indexOf(THIS)==-1){
				named.add(member);
			}
		}
		Map<Member, Future<String>> futures = executorService.submitToMembers(dt, named);
		*/
		///////////////////////////////////////////////////////////////////////////////////////
		
		String result = null;	
		String result_1 = "";
		
		
			
			
		Collection<Future<String>> data = futures.values();
		Iterator<Future<String>> it = data.iterator();
			
		
		while(true){
				
				bw = queue.poll();
				
				if (bw != null){
					System.out.println("[MAIN] bw-pollQ (" +bw.toString()+ ")");
				}
				
				while(it.hasNext()){
				
					result = it.next().get();
					if (result != null){
						if (!result_1.equalsIgnoreCase(result)){
							System.out.println("[MAIN] result " + result);
							result_1 = result;
						}
					}
				}
		}
		//}
		
		
		
	}

}
