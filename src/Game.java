import java.util.Set;

public class Game {
	
	private int boardSize = 8;
	private GameEngine gameEngine;
	
	public static void main(String[] args){
		new Game();
	}

	public Game(){
		gameEngine = new GameEngine(boardSize);
		run();
	}
	
	public void run(){
		State board = new State(boardSize);
		board.clear();
		Set<Coordinates> moves = gameEngine.findAllLegalMoves(board);
		printBoard(board, moves);
		
//		while(!moves.isEmpty()){
		for(int k = 0; k < 10; k++){
			Coordinates avaliableMove = moves.toArray(new Coordinates[0])[0];
			board.matrix[avaliableMove.getRow()][avaliableMove.getCol()] = board.turn;
			board.changeTurn();
			moves = gameEngine.findAllLegalMoves(board);
			printBoard(board, moves);
		}
	}

	private void printBoard(State state, Set<Coordinates> moves) {
		Coordinates[] movesArray = moves.toArray(new Coordinates[0]);
		for(int k = 0; k < movesArray.length; k++){
			System.out.println("coordinate for legal move (" + movesArray[k].getRow() + "," + movesArray[k].getCol() + ")");
		}
		
		for(int i = 0; i < boardSize; i++){
			System.out.print("|");
			for(int j = 0; j < boardSize; j++){
				if(moves.contains(new Coordinates(i,j))){
					System.out.print("_1_|");
				}else if(state.matrix[i][j] == 0){
					System.out.print("___|");
				}else {
					String m = (state.matrix[i][j] == 1) ? "º" : "•";
					System.out.print("_" + m + "_|");
				}
			}
			System.out.println("");
		}
	}
}
