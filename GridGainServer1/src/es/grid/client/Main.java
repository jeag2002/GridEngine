package es.grid.client;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;
import org.gridgain.grid.Grid;
import org.gridgain.grid.GridGain;
import org.gridgain.grid.lang.GridCallable;

import es.grid.client.bean.BWrapper;

public class Main {

	public static void main(String[] args) throws Exception{
		
		//Grid grid = GridGain.start("C:\\workspaces\\workEclipse\\workGrid\\GridGainClient\\src\\main\\resources\\example-compute.xml");
		
		URL url = Main.class.getClassLoader().getResource("example-ignite.xml");
		Ignite ignite = Ignition.start(url);
		
		
		/*
		BWrapper response = null;
		
		try{
		
		BWrapper wrapper = new BWrapper();
		wrapper.setSizeX("9");
		wrapper.setSizeY("9");
		wrapper.setData("100100100100");
		wrapper.setRes(BWrapper.OK);
		wrapper.setErrMsg("");
		
		Collection<GridCallable<BWrapper>> calls = new ArrayList<>();
		
		calls.add(
				new GridCallable<BWrapper>(){
					@Override
					public BWrapper call() throws Exception{
						System.out.println("[SudokuService-node] get (" + wrapper.toString() + ")");
						return wrapper;
					}
					
				}
				);
		
		Collection<BWrapper> data = grid.compute().call(calls).get();
		response = data.iterator().next();
		
		}catch(Exception e){
			System.out.println("[SudokuService] Exception (" + e.getMessage() + ")");
		}
		*/
		
		
		
	}

}
