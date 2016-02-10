import java.util.HashMap;
import java.util.Set;

public class Game {
	
	private int boardSize = 3;
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
//		printBoard(board, moves);
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

	void printBoard(State state, HashMap<Coordinates, Integer> suggestions) {
		System.out.println("    a     b     c     d     e     f     g     h");
//		System.out.println("    a     b     c     d");
		for(int i = 0; i < boardSize; i++){
			System.out.print(i + "|");
			for(int j = 0; j < boardSize; j++){
				Coordinates c = new Coordinates(i,j);
				if(suggestions.containsKey(c)){
					int suggestion = suggestions.get(c);
					if((suggestion < 0 && suggestion > -10) || suggestion > 9){
						System.out.print("_" + suggestion + "__|");
					}else if(suggestion < -9 || suggestion > 99){
						System.out.print("_" + suggestion + "_|");
					}else{
						System.out.print("__" + suggestion + "__|");
					}
					
				}else if(state.matrix[i][j] == 0){
					System.out.print("_____|");
				}else {
					String m = (state.matrix[i][j] == 1) ? "º" : "•";
					System.out.print("__" + m + "__|");
				}
			}
			System.out.println(i);
		}
		System.out.println("    a     b     c     d     e     f     g     h");
//		System.out.println("    a     b     c     d");
	}
	
	void printBoard(State state, Set<Coordinates> moves) {
		for(int i = 0; i < boardSize; i++){
			System.out.print(i + "|");
			for(int j = 0; j < boardSize; j++){
				Coordinates c = new Coordinates(i,j);
				if(moves.contains(c)){
					System.out.print("_" + 1 + "_|");
				}else if(state.matrix[i][j] == 0){
					System.out.print("___|");
				}else {
					String m = (state.matrix[i][j] == 1) ? "º" : "•";
					System.out.print("_" + m + "_|");
				}
			}
			System.out.println(i);
		}
		System.out.println("   a   b   c   d   e   f   g   h");
	}
	
	public void printMatrix(int[][] matrix){
		System.out.println("    a     b     c     d     e     f     g     h");
		for(int i = 0; i < matrix.length; i++){
			System.out.print(i + "|");
			for(int j = 0; j < matrix.length; j++){
				if(matrix[i][j] == 0){
					System.out.print("_____|");
				}else {
					String m = (matrix[i][j] == 1) ? "º" : "•";
					System.out.print("__" + m + "__|");
				}
			}
			System.out.println(i);
		}
		System.out.println("    a     b     c     d     e     f     g     h");
	}
}
