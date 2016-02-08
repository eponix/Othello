
public class State {
	public int[][] matrix;		
	public int turn;		// 1 white, 0 empty, -1 black
	
	public State(int size){
		matrix = new int[size][size];
		turn = -1;	// black begins
	}
	
	public void changeTurn(){
		turn *= -1;
	}
	
	public void clear(){
		for(int i = 0; i < matrix.length; i++){
			for(int j = 0; j < matrix.length; j++){
				if((i == 3 && j == 3) || (i == 4 && j == 4)){
					matrix[i][j] = 1;
				}else if((i == 3 && j == 4) || (i == 4 && j == 3)){
					matrix[i][j] = -1;
				}else{
					matrix[i][j] = 0;
				}
			}
		}
	}
	
	public int calculateScore(int player){
		int sum;
		for(int i = 0; i < matrix.length; i++){
			for(int j = 0; j < matrix.length; j++){
				if()
			}
		}
	}
}
