
public class State {
	public int[][] matrix;		
	public int turn;		// 1 white, 0 empty, -1 black
	private int size;

	public State(int size){
		matrix = new int[size][size];
		turn = -1;	// black begins
		this.size = size;
	}
	
	public State(int[][] matrix, int turn){
		this.matrix = new int[matrix.length][matrix.length];
		for(int i = 0; i < matrix.length; i++){
			for(int j = 0; j < matrix.length; j++){
				this.matrix[i][j] = matrix[i][j];
			}
		}
		this.turn = turn;
		this.size = matrix.length;
	}

	public void changeTurn(){
		turn *= -1;
	}

	public void clear(){
		for(int i = 0; i < matrix.length; i++){
			for(int j = 0; j < matrix.length; j++){
				if((i == (matrix.length/2 -1) && j == (matrix.length/2 -1)) || (i == (matrix.length/2) && j == (matrix.length/2))){
					matrix[i][j] = 1;
				}else if((i == (matrix.length/2 -1) && j == (matrix.length/2)) || (i == (matrix.length/2) && j == (matrix.length/2 -1))){
					matrix[i][j] = -1;
				}else{
					matrix[i][j] = 0;
				}
			}
		}
	}

	public int calculateScore(){ // player: 1 white, -1 black
		int sum = 0;
		for(int i = 0; i < matrix.length; i++){
			for(int j = 0; j < matrix.length; j++){
				sum += matrix[i][j]*AI.playerInt;
			}
		}
		return sum;
	}
	
	public String calculateWinner(){
		int blackSum = 0;
		for(int i = 0; i < matrix.length; i++){
			for(int j = 0; j < matrix.length; j++){
				blackSum += matrix[i][j]*-1;
			}
		}
		
		int whiteSum = 0;
		for(int i = 0; i < matrix.length; i++){
			for(int j = 0; j < matrix.length; j++){
				whiteSum += matrix[i][j];
			}
		}
		return blackSum > whiteSum ? "Black" : "White";
	}

	public State clone(){
		State temp = new State(size);
		for(int i = 0; i < size; i++){
			for(int j = 0; j < size; j++){
				temp.matrix[i][j] = this.matrix[i][j];
			}
		}
		temp.turn = this.turn;
		return temp;
	}
	
	public int[][] getMatrix(){
		return matrix;
	}
	
	public int getTurn(){
		return turn;
	}
}
