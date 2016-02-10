import java.util.HashSet;
import java.util.Set;

public class GameEngine {

	private int boardSize;

	public GameEngine(int boardSize){
		this.boardSize = boardSize;
	}

	public void makeMove(State state, Coordinates move){
		if(move.getRow() < 0 || move.getCol() < 0){
			state.changeTurn();
			return;
		}
		state.matrix[move.getRow()][move.getCol()] = state.turn;
		findLegalMovesForDisc(move.getRow(), move.getCol(), state, true);
		state.changeTurn();
	}

	private boolean turnDisc(int dir, int row, int col, State state) {
		if(outOfBounds(row, col) || state.matrix[row][col] == 0){
			return false;
		}else if(state.matrix[row][col] == state.turn){
			return true;
		}else if(state.matrix[row][col] == state.turn * -1){
			Coordinates c = changePosition(dir, row, col);
			if (turnDisc(dir, c.getRow(), c.getCol(), state)){
				//				System.out.println("Turning "+ row + "," + col);
				state.matrix[row][col] = state.turn;
				return true;
			}else{
				return false;
			}
		}
		return false;
	}


	public Set<Coordinates> findAllLegalMoves(State state){
		Set<Coordinates> legalMoves = new HashSet<Coordinates>();
		String m = (state.turn == 1) ? "white" : "black";
		//		System.out.println("Turn: " + m);
		for(int i = 0; i < boardSize; i++){
			for(int j = 0; j < boardSize; j++){
				if(state.matrix[i][j] == state.turn){
					//					System.out.println("Found my disc at (" + i + "," + j + ")");
					Set<Coordinates> list = findLegalMovesForDisc(i,j,state, false);
					legalMoves.addAll(list);
				}
			}
		}
		return legalMoves;
	}

	private Set<Coordinates> findLegalMovesForDisc(int row, int col, State state, boolean doMove) {
		Set<Coordinates> coordinates = new HashSet<Coordinates>();
		int dir = 0;
		for(int i = row-1; i <= row+1; i++){
			for(int j = col-1; j <= col+1; j++){
				dir++;
				if(!outOfBounds(i, j) && state.matrix[i][j] == state.turn *-1 ){
					//					System.out.println("Found opponent's disc at (" + i + "," + j + ")" );
					if(doMove){
						//						System.out.println("Making a move!");
						turnDisc(dir, i, j, state);
					}else{
						Coordinates c = changePosition(dir, i, j);
						Coordinates legalMove = legalMove(dir, c.getRow(), c.getCol(), state);
						if(legalMove != null){
							//							System.out.println("Added legal move to list");
							coordinates.add(legalMove);
						}
					}
				}
			}
		}
		return coordinates;
	}

	private Coordinates legalMove(int dir, int row, int col, State state) {
		if(outOfBounds(row, col) || state.matrix[row][col] == state.turn){
			return null;
		}else if(state.matrix[row][col] == 0){
			return new Coordinates(row, col);
		}else if(state.matrix[row][col] == state.turn * -1){
			Coordinates c = changePosition(dir, row, col);
			return legalMove(dir, c.getRow(), c.getCol(), state);
		}
		return null;
	}

	private Coordinates changePosition(int dir, int row, int col) {
		switch(dir){
		case 1:
		case 2:
		case 3: row -=1;
		break;
		case 7:
		case 8:
		case 9:	row +=1;
		break;
		}

		switch(dir){
		case 1:
		case 4:
		case 7: col -=1;
		break;
		case 3:
		case 6:
		case 9: col +=1;
		break;
		}
		return new Coordinates(row, col);
	}

	private boolean outOfBounds(int row, int col){
		return (row < 0 || col < 0 || row > boardSize -1 || col > boardSize -1);
	}

	public int calculateScore(int[][] matrix, int player){ // player: 1 white, -1 black
		int sum = 0;
		for(int i = 0; i < matrix.length; i++){
			for(int j = 0; j < matrix.length; j++){
				sum += matrix[i][j]*player;
			}
		}
		return sum;
	}
}
