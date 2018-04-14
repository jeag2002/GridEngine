package es.grid.client.bean;

public class InmutableInt {
	
	private int indexNode;
	
	public InmutableInt(){
		indexNode = 0;
	}
	
	public InmutableInt(int _i){
		indexNode = _i;
	}
	
	public int getIndexNode() {
		return indexNode;
	}

	public void setIndexNode(int indexNode) {
		this.indexNode = indexNode;
	}

	

}
