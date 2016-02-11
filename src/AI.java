import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

public class AI {

	private int boardSize = 8;
	private GameEngine gameEngine;
	private Game game;
	public static int playerInt;
	private static long timeToBreak;
	private static boolean timesUp;
	
	public static void main(String[] args){
		new AI();
	}

	public AI(){
		game = new Game(boardSize);
		gameEngine = new GameEngine(boardSize);
		run();
	}

	public void run(){
		HashMap<Coordinates, Integer> suggestions = new HashMap<Coordinates, Integer>();
		State board = new State(boardSize);
		board.clear();
		Scanner scan = new Scanner(System.in);
		boolean noMovesAvailable = false;
		
		System.out.println("Please enter a time limit for the IA in secounds.");
		int timeLimit = scan.nextInt();
		
		while(true){
			suggestions.clear();
			String player = board.turn == 1 ? "white" : "black"; 
			playerInt = board.turn;
			long programStart = System.currentTimeMillis();
			Set<Coordinates> moves = gameEngine.findAllLegalMoves(board);
			System.out.println("Time for finding all legal moves: " + (System.currentTimeMillis()-programStart));
			long timePerAvilableMove = ( (long) ((double)timeLimit * 1000) / moves.size());
			for(Coordinates move : moves){
				Node startingNode = new Node(false);
				State boardClone = board.clone();
				timesUp = false;
				timeToBreak = timePerAvilableMove + System.currentTimeMillis();
				gameEngine.makeMove(boardClone, move);
				suggestions.put(move, generateValueForMove(startingNode, boardClone, false));
				System.out.println("Time for generating value for move: " + (System.currentTimeMillis()-programStart));
			}
			System.out.println("Total time for IA: " + (System.currentTimeMillis()-programStart));
			game.printBoard(board, suggestions);
			if(suggestions.isEmpty() && noMovesAvailable){
				System.out.println("The winner is: " + board.calculateWinner());
				return;
			}else if(suggestions.isEmpty()){
				noMovesAvailable = true;
				board.changeTurn();
				System.out.println("Skipping turn for " + player);
				continue;
			}else{
				noMovesAvailable = false;
			}
			System.out.println("make a move " + player + " by entering row(0-7) [Enter] col(a-h) [Enter]");
			System.out.print("row: ");
			int row = scan.nextInt();
			System.out.print("col: ");
			int col = charColToIntCol(scan.next());
			Coordinates chosenMove = new Coordinates(row,col);
			while(!suggestions.containsKey(chosenMove)){
				System.out.println("Your input was incorrect. Please write prefered row(0-7) [Enter] col(a-h) [Enter]");
				row = scan.nextInt();
				col = charColToIntCol(scan.next());
				chosenMove = new Coordinates(row,col);
			}
			gameEngine.makeMove(board, chosenMove);
			System.out.println();
		}

	}

	private int generateValueForMove(Node node, State board, boolean skipTurn){
		if(System.currentTimeMillis() > AI.timeToBreak){
			timesUp = true;
			return node.value;
		}
		
		Set<Coordinates> moves = gameEngine.findAllLegalMoves(board);
		
		if(skipTurn && moves.isEmpty()){ // Leaf
			int score = board.calculateScore();
			return score;
		}else if(moves.isEmpty()){
			skipTurn = true;
			Node child = new Node(!node.MAX, node.alpha, node.beta);
			board.changeTurn();
			return generateValueForMove(child, board, skipTurn);
		}else{
			skipTurn = false;
		}
		Iterator<Coordinates> movesIter = moves.iterator();
		if(node.MAX){ // MAX
			//		Do we have a smaller value than Beta in node.value ? In that case, we need to evaluate eventual worse cases (in the children)
			//		If its bigger, it's not going to replace the parent anyway.	
			while(movesIter.hasNext() && node.value < node.beta){
				State boardClone = board.clone();
				Node child = new Node(!node.MAX, node.alpha, node.beta);
				gameEngine.makeMove(boardClone, (Coordinates) movesIter.next());

				int temp = generateValueForMove(child, boardClone, skipTurn);
				
				if(AI.timesUp){
					if(temp != Integer.MAX_VALUE && temp > node.value){
						node.value = temp;
					}
					break;
				}
				
				if(temp > node.value){
					node.value = temp;
					if(node.value > node.alpha){
						node.alpha = node.value;
					}
				}
			}
			return node.value;
		}else{ // MIN
			while(movesIter.hasNext() && node.value > node.alpha){			
				State boardClone = board.clone();
				Node child = new Node(!node.MAX, node.alpha, node.beta);
				gameEngine.makeMove(boardClone, (Coordinates) movesIter.next());
				int temp = generateValueForMove(child, boardClone, skipTurn);
				
				if(AI.timesUp){
					if(temp != Integer.MIN_VALUE && temp < node.value){
						node.value = temp;
					}
					break;
				}
				
				if(temp < node.value){
					node.value = temp;
					if(node.value < node.beta){
						node.beta = node.value;
					}
				}
			}
			return node.value;
		}
		
	}
	
	private int charColToIntCol(String col){
		if(col.length() != 1){
			return -1;
		}
		int temp = -1;
		switch(col){
		case "a":	temp = 0;
					break;
		case "b":	temp = 1;
					break;
		case "c":	temp = 2;
					break;
		case "d":	temp = 3;
					break;
		case "e":	temp = 4;
					break;
		case "f":	temp = 5;
					break;
		case "g":	temp = 6;
					break;
		case "h":	temp = 7;
					break;	
		}
		return temp;
	}
}
