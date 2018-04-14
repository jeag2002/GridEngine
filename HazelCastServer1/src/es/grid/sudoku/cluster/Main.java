package es.grid.sudoku.cluster;

import java.util.Map;
import java.util.Set;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.Member;
import com.hazelcast.instance.HazelcastInstanceFactory;

import es.grid.client.bean.BWrapper;

public class Main {

	public static void main(String[] args) {
		
		HazelcastInstance hazelcast = Hazelcast.newHazelcastInstance();
        Set<HazelcastInstance> instances = HazelcastInstanceFactory.getAllHazelcastInstances();
        
        Map<String, BWrapper> empMap = hazelcast.getMap("matrixSudokus");
        
        String data = "";
        
        String idCluster = args[0];
        if (idCluster == null){idCluster = "1";}
        
        System.out.println("[Cluster "+idCluster+"] -- INIT");
        
        while(true){
	        BWrapper bw = null;
	        while (bw == null){bw = empMap.get("data");}
	        if (bw!=null){
	        	if (!data.equalsIgnoreCase(bw.toString())){
	        		System.out.println("Data received from client (" + bw.toString()+ ")");
	        		data = bw.toString();
	        	}
	        	empMap.put("1", bw);
	        }
        }

	}

}
