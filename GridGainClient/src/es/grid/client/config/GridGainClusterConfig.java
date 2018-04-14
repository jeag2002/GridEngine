package es.grid.client.config;

import java.net.URL;

import org.gridgain.grid.Grid;
import org.gridgain.grid.GridGain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;



//https://github.com/gridgain/gridgain/blob/master/examples/src/main/java/org/gridgain/examples/compute/ComputeBroadcastExample.java

@Configuration
public class GridGainClusterConfig {

	private final Logger logger = LoggerFactory.getLogger(GridGainClusterConfig.class);
	
	/*
	@Bean
	public Grid configureGridGain(){
		logger.info("[configureGridGain] -- INI");
		Grid grid = null;
		try{
			
			ClassLoader classLoader = getClass().getClassLoader();
			URL url = classLoader.getResource("example-compute.xml");
			grid = GridGain.start(url);
		}catch(Exception e){
			logger.warn("[configureGridGain] ERROR (" + e.getMessage() + ")");
		}		
		return grid;
	}
	*/
}
