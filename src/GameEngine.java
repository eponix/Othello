import java.util.HashSet;
import java.util.Set;

public class GameEngine {
	
	private int boardSize;
	
	public GameEngine(int boardSize){
		this.boardSize = boardSize;
	}

	public Set<Coordinates> findAllLegalMoves(State state){
		Set<Coordinates> legalMoves = new HashSet<Coordinates>();
		for(int i = 0; i < boardSize; i++){
			for(int j = 0; j < boardSize; j++){
				if(state.matrix[i][j] == state.turn){
					Set<Coordinates> list = findLegalMovesForDisc(i,j,state);
					legalMoves.addAll(list);
				}
			}
		}
		return legalMoves;
	}

	private Set<Coordinates> findLegalMovesForDisc(int row, int col, State state) {
		Set<Coordinates> coordinates = new HashSet<Coordinates>();
		int dir = 1;
		for(int i = row-1; i < row+1; i++){
			for(int j = col-1; j < col+1; j++){
				dir++;
				if(!outOfBounds2(i, j, state, row, col) && state.matrix[i][j] == state.turn *-1){
					Coordinates c = changePosition(dir, i, j);
					Coordinates legalMove = legalMove(dir, c.getRow(), c.getCol(), state);
					if(legalMove != null){
						coordinates.add(legalMove);
					}
				}
			}
		}
		return coordinates;
	}
	
	private Coordinates legalMove(int dir, int row, int col, State state) {
		if(outOfBounds(row, col, state) || state.matrix[row][col] == state.turn){
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
	
	private boolean outOfBounds(int row, int col, State state){
		System.out.println("row: " + row + " col: " + col + " length: " + state.matrix.length);
		return (row < 0 || col < 0 || row > state.matrix.length || col > state.matrix[0].length);
	}

	private boolean outOfBounds2(int row, int col, State state, int posRow, int posCol){
		System.out.println("row: " + row + " col: " + col + " length: " + state.matrix.length);
		return ((posRow + row) < 0 || (posCol + col) < 0 || (posRow + row) > state.matrix.length || (posCol + col) > state.matrix[0].length);
	}
}
