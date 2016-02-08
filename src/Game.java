import java.util.Set;

public class Game {
	
	private int boardSize = 8;
	private GameEngine gameEngine;
	
	public static void main(String[] args){
		new Game(8);
	}

	public Game(int boardSize){
		gameEngine = new GameEngine(boardSize);
//		run();
	}
	
	public void run(){
		State board = new State(boardSize);
		board.clear();
		Set<Coordinates> moves = gameEngine.findAllLegalMoves(board);
		printBoard(board, moves);
		boolean skipTurn = false;
		while(!moves.isEmpty() || skipTurn){
//		for(int k = 0; k < 10; k++){
			if (!skipTurn){
				Coordinates avaliableMove = moves.toArray(new Coordinates[0])[0];
				gameEngine.makeMove(board, avaliableMove);
			}
			board.changeTurn();
			moves = gameEngine.findAllLegalMoves(board);
			if (moves.isEmpty() && skipTurn){
				skipTurn = false;
			}
			else if(moves.isEmpty()){
				skipTurn = true;
			}
			else{
				skipTurn = false;
			}
			printBoard(board, moves);
		}
	}

	void printBoard(State state, Set<Coordinates> moves) {
		Coordinates[] movesArray = moves.toArray(new Coordinates[0]);
		for(int k = 0; k < movesArray.length; k++){
			System.out.println("coordinate for legal move (" + movesArray[k].getRow() + "," + movesArray[k].getCol() + ")");
		}
		
		for(int i = 0; i < boardSize; i++){
			System.out.print(i + "|");
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
			System.out.println(i);
		}
		System.out.println("   0   1   2   3   4   5   6   7   ");
	}
}
