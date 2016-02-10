public	class Node {
	public int value;
	public int alpha;
	public int beta;
	public boolean MAX;

	public Node(boolean MAX){
		setValues(MAX, Integer.MIN_VALUE, Integer.MAX_VALUE);
	}

	public Node(boolean MAX, int alpha, int beta){
		setValues(MAX, alpha, beta);
	}

	private void setValues(boolean MAX, int alpha, int beta){
		this.MAX = MAX;
		if(MAX){
			value = Integer.MIN_VALUE;
		}else{
			value = Integer.MAX_VALUE;
		}
		this.alpha = alpha;
		this.beta = beta;
	}
}