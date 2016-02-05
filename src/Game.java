import java.util.HashSet;
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
		board.Clear();
		Set<Coordinates> moves = gameEngine.findAllLegalMoves(board);
//		Set<Coordinates> moves = new HashSet<Coordinates>();
		printBoard(board, moves);
	}

	private void printBoard(State state, Set<Coordinates> moves) {
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
