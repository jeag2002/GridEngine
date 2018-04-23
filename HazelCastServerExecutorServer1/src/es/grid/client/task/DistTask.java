package es.grid.client.task;

import java.io.Serializable;
import java.util.concurrent.Callable;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.HazelcastInstanceAware;
import com.hazelcast.core.IMap;
import com.hazelcast.core.IQueue;

import es.grid.client.bean.BWrapper;

public class DistTask implements Callable<String>, Serializable, HazelcastInstanceAware{

	private BWrapper bw;
	private transient HazelcastInstance hazelcastInstance;
	
	public DistTask(){
	}
	
	@Override
	public String call() throws Exception {
		IMap<String, BWrapper> map = hazelcastInstance.getMap("sudokus");
		IQueue<BWrapper> queue =  hazelcastInstance.getQueue("sudokuQ");
		
		BWrapper bwdata = null;
		
		while(bwdata == null){bwdata=map.get("data");}
		
		System.out.println("[DistTask] received data (" + bwdata.toString() + ")");
		bwdata.setErrMsg("DISTTASK (" + hazelcastInstance.getCluster().getLocalMember().toString() + ")");
		queue.put(bwdata);
		System.out.println("[DistTask] put queue data (" + bwdata.toString() + ")");
		
		return hazelcastInstance.getCluster().getLocalMember().toString() + ": END";
	}

	@Override
	public void setHazelcastInstance(HazelcastInstance hazelcastInstance) {
		this.hazelcastInstance = hazelcastInstance;
		
	}

}
