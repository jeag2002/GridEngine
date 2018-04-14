package es.grid.client.config;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.config.Config;
import com.hazelcast.config.NetworkConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;

import es.grid.client.controller.SudokuController;

@Configuration
public class HazelcastClusterConfig {
	
	
	private final Logger logger = LoggerFactory.getLogger(HazelcastClusterConfig.class);
	
	@Value("${cluster.number}")
	private int memberCount;
	@Value("${cluster.ip_1}")
	private String ipHazelCast;
	@Value("${cluster.port_1}")
	private String portHazelCast;

	@Bean
	public HazelcastInstance processHazelcastInstance(){
		
		logger.info("[HazelCastClusterConfig] ipHazelCast (" + ipHazelCast + ":" + portHazelCast + ") - memberCount (" + memberCount + ") -- INI");
		
		ClientConfig clientConfig = new ClientConfig();
		String[] addresses = {ipHazelCast + ":" + portHazelCast};
		clientConfig.getNetworkConfig().addAddress(addresses);
        HazelcastInstance hazelcastInstance = HazelcastClient.newHazelcastClient(clientConfig);
        
        return hazelcastInstance;
		
		/*
		Config config = new Config();
        NetworkConfig networkConfig = config.getNetworkConfig();
        networkConfig.getJoin().getMulticastConfig().setEnabled(false);
        networkConfig.getJoin().getTcpIpConfig().setEnabled(true);
        networkConfig.getJoin().getTcpIpConfig().setMembers(Arrays.asList(new String[]{ipHazelCast}));

        HazelcastInstance[] hazelcastInstances = new HazelcastInstance[memberCount];
        for (int i = 0; i < memberCount; i++) {
            hazelcastInstances[i] = Hazelcast.newHazelcastInstance(config);
        }
        return hazelcastInstances[0];
		*/
		
		
	}
}
