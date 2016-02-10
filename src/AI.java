import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

public class AI {

	private int boardSize = 3;
	private GameEngine gameEngine;
	private Game game;
	private static int nbrOfEndNodes = 0;

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
		while(true){
			suggestions.clear();
			String player = board.turn == 1 ? "white" : "black"; 
			
			long start = System.currentTimeMillis();
			Set<Coordinates> moves = gameEngine.findAllLegalMoves(board);
			System.out.println("Time for finding all legal moves: " + (System.currentTimeMillis()-start));
			for(Coordinates move : moves){
				Node startingNode = new Node(false);
				State boardClone = board.clone();
				gameEngine.makeMove(boardClone, move);
//				suggestions.put(move, generateValueForMove2(boardClone.getMatrix(), boardClone.getTurn(), startingNode, boardClone.turn*-1, false, 0));
				suggestions.put(move, generateValueForMove(startingNode, boardClone, boardClone.turn*-1, false, 0));
				System.out.println("Time for generating value for move: " + (System.currentTimeMillis()-start));
			}
			System.out.println("Total time for IA: " + (System.currentTimeMillis()-start));
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
			System.out.println("nbrOfEndNodes: " + nbrOfEndNodes);
			System.out.println("make a move " + player + " by entering row(0-8) [Enter] col(a-h) [Enter]");
			System.out.print("row: ");
			int row = scan.nextInt();
			System.out.print("col: ");
			int col = charColToIntCol(scan.next());
			Coordinates chosenMove = new Coordinates(row,col);
			while(!suggestions.containsKey(chosenMove)){
				System.out.println("Your input was incorrect. Please write prefered row(0-8) [Enter] col(a-h) [Enter]");
				row = scan.nextInt();
				col = charColToIntCol(scan.next());
				chosenMove = new Coordinates(row,col);
			}
			gameEngine.makeMove(board, chosenMove);
			System.out.println();
		}

	}
	
	public void run3(){
		HashMap<Coordinates, Integer> suggestions = new HashMap<Coordinates, Integer>();
		State board = new State(boardSize);
		board.clear();
		Scanner scan = new Scanner(System.in);
		boolean noMovesAvailable = false;
		ArrayList<Coordinates> pathOfMoves = new ArrayList<>();
		initPathOfMoves(pathOfMoves);
		while(true){
			suggestions.clear();
			String player = board.turn == 1 ? "white" : "black"; 
			
			long start = System.currentTimeMillis();
			Set<Coordinates> moves = gameEngine.findAllLegalMoves(board);
			System.out.println("Time for finding all legal moves: " + (System.currentTimeMillis()-start));
			for(Coordinates move : moves){
				Node startingNode = new Node(false);
				suggestions.put(move, generateValueForMove3(pathOfMoves, move, startingNode, board.turn*-1, false, 0));
				System.out.println("Time for generating value for move: " + (System.currentTimeMillis()-start));
			}
			System.out.println("Total time for IA: " + (System.currentTimeMillis()-start));
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
			System.out.println("nbrOfEndNodes: " + nbrOfEndNodes);
			System.out.println("make a move " + player + " by entering row(0-8) [Enter] col(a-h) [Enter]");
			System.out.print("row: ");
			int row = scan.nextInt();
			System.out.print("col: ");
			int col = charColToIntCol(scan.next());
			Coordinates chosenMove = new Coordinates(row,col);
			while(!suggestions.containsKey(chosenMove)){
				System.out.println("Your input was incorrect. Please write prefered row(0-8) [Enter] col(a-h) [Enter]");
				row = scan.nextInt();
				col = charColToIntCol(scan.next());
				chosenMove = new Coordinates(row,col);
			}
			pathOfMoves.add(chosenMove);
			gameEngine.makeMove(board, chosenMove);
			System.out.println();
		}

	}

	private int generateValueForMove(Node node, State board, int player, boolean skipTurn, int depth){
		depth++;
		Set<Coordinates> moves = gameEngine.findAllLegalMoves(board);
		
		if(moves.isEmpty() && skipTurn){ // Leaf
			AI.nbrOfEndNodes++;
			int score = board.calculateScore(player);
			System.out.println("level " + depth + " is a leaf, player: " + player + " value: " + score);
			return score;
		}else if(moves.isEmpty()){
			System.out.println("level " + depth +" has no more moves, skipping, turn: " + board.turn);
			skipTurn = true;
			Node child = new Node(!node.MAX, node.alpha, node.beta);
			board.changeTurn();
			node.value = generateValueForMove(child, board, player, skipTurn, depth);
			System.out.println("level " + depth + " has no more children");
		}else{
			skipTurn = false;
		}
		Iterator<Coordinates> movesIter = moves.iterator();
		int childCounter = 0;
		if(node.MAX){
			//		Do we have a smaller value than Beta in node.value ? In that case, we need to evaluate eventual worse cases (in the children)
			//		If its bigger, it's not going to replace the parent anyway.	
			while(movesIter.hasNext() && node.value < node.beta){
				System.out.println("Max level: " + depth + " child " + ++childCounter + " (" + moves.size() + ") turn: " + board.turn);
				State boardClone = board.clone();
				Node child = new Node(!node.MAX, node.alpha, node.beta);
				gameEngine.makeMove(boardClone, (Coordinates) movesIter.next());
				int temp = generateValueForMove(child, boardClone, player, skipTurn, depth);
				if(temp > node.value){
					node.value = temp;
					if(node.value > node.alpha){
						node.alpha = node.value;
					}
				}
				if(node.value >= node.beta && movesIter.hasNext()){
					System.out.println("Max pruning");
				}
				if(!movesIter.hasNext()){
					System.out.println("Max level " + depth +" has no more children");
				}
			}
			return node.value;
		}else{
			while(movesIter.hasNext() && node.value > node.alpha){
				System.out.println("Min level: " + depth + " child " + ++childCounter + " (" + moves.size() + ") turn: " + board.turn);
				if(depth == 1 && childCounter == 2){
					System.out.println("debugging");
				}
				State boardClone = board.clone();
				Node child = new Node(!node.MAX, node.alpha, node.beta);
				gameEngine.makeMove(boardClone, (Coordinates) movesIter.next());
				int temp = generateValueForMove(child, boardClone, player, skipTurn, depth);
				if(temp < node.value){
					node.value = temp;
					if(node.value < node.beta){
						node.beta = node.value;
					}
				}
				if(node.value <= node.alpha && movesIter.hasNext()){
					System.out.println("Min pruning");
				}
				if(!movesIter.hasNext()){
					System.out.println("Min level " + depth +" has no more children");
				}
			}
			return node.value;
		}
		
	}
	
	private int generateValueForMove2(int[][] matrix, int turn, Node node, int player, boolean skipTurn, int depth){
		depth++;
//		System.out.println("turn: " + turn);
		State encapsulate2 = new State(matrix, turn);
		Set<Coordinates> moves = gameEngine.findAllLegalMoves(encapsulate2);
		
		if(moves.isEmpty() && skipTurn){ // Leaf
			AI.nbrOfEndNodes++;
			int score = gameEngine.calculateScore(matrix, player);
			System.out.println("level " + depth + " is a leaf, player: " + player + " value: " + score);
			return score;
		}else if(moves.isEmpty()){
			System.out.println("level " + depth +" has no more moves, skipping, turn: " + turn);
			skipTurn = true;
			Node child = new Node(!node.MAX, node.alpha, node.beta);
			turn *= -1;
			node.value = generateValueForMove2(matrix, turn, child, player, skipTurn, depth);
			System.out.println("level " + depth + " has no more children");
		}else{
			skipTurn = false;
		}
		Iterator<Coordinates> movesIter = moves.iterator();
		int childCounter = 0;
		if(node.MAX){
			//		Do we have a smaller value than Beta in node.value ? In that case, we need to evaluate eventual worse cases (in the children)
			//		If its bigger, it's not going to replace the parent anyway.	
			while(movesIter.hasNext() && node.value < node.beta){
				System.out.println("Max level: " + depth + " child " + ++childCounter + " (" + moves.size() + ") turn: " + turn);
				State encapsulate = new State(matrix, turn);
				Node child = new Node(!node.MAX, node.alpha, node.beta);
				gameEngine.makeMove(encapsulate, (Coordinates) movesIter.next());
				int temp = generateValueForMove2(encapsulate.getMatrix(), encapsulate.getTurn(), child, player, skipTurn, depth);
				if(temp > node.value){
					node.value = temp;
					if(node.value > node.alpha){
						node.alpha = node.value;
					}
				}
				if(node.value >= node.beta && movesIter.hasNext()){
					System.out.println("Max pruning");
				}
				if(!movesIter.hasNext()){
					System.out.println("Max level " + depth +" has no more children");
				}
			}
			return node.value;
		}else{
			while(movesIter.hasNext() && node.value > node.alpha){
				System.out.println("Min level: " + depth + " child " + ++childCounter + " (" + moves.size() + ") turn: " + turn);
				State encapsulate = new State(matrix, turn);
				Node child = new Node(!node.MAX, node.alpha, node.beta);
				
//				System.out.println("this node's matrix before move");
//				game.printMatrix(matrix);
//				System.out.println("encapsulated matrix before move");
//				game.printBoard(encapsulate, new HashMap<Coordinates, Integer>());
				
				gameEngine.makeMove(encapsulate, (Coordinates) movesIter.next());
				
//				System.out.println("this node's matrix after move");
//				game.printMatrix(matrix);
//				System.out.println("encapsulated matrix after move");
//				game.printBoard(encapsulate, new HashMap<Coordinates, Integer>());
//				System.exit(0);
				int temp = generateValueForMove2(encapsulate.getMatrix(), encapsulate.getTurn(), child, player, skipTurn, depth);
			
				if(temp < node.value){
					node.value = temp;
					if(node.value < node.beta){
						node.beta = node.value;
					}
				}
				if(node.value <= node.alpha && movesIter.hasNext()){
					System.out.println("Min pruning");
				}
				if(!movesIter.hasNext()){
					System.out.println("Min level " + depth +" has no more children");
				}
			}
			return node.value;
		}
		
	}	
	
	private int generateValueForMove3(ArrayList<Coordinates> pathOfMoves, Coordinates tryThisMove, Node node, int player, boolean skipTurn, int depth){
		int[][] matrix = new int[boardSize][boardSize];
		State encapsulate = new State(matrix, -1);
		pathOfMoves.add(tryThisMove);
		
		for (Coordinates doMove : pathOfMoves){
			gameEngine.makeMove(encapsulate, doMove);
		}

		// now we have a new matrix in the current state.
		
		depth++;
		// Find leagal moves
		Set<Coordinates> moves = gameEngine.findAllLegalMoves(encapsulate);
		
		if(moves.isEmpty() && skipTurn){ // Leaf
			AI.nbrOfEndNodes++;
			int score = gameEngine.calculateScore(encapsulate.getMatrix(), player);
			System.out.println("level " + depth + " is a leaf, player: " + player + " value: " + score);
			return score;
		}else if(moves.isEmpty()){
			System.out.println("level " + depth +" has no more moves, skipping, turn: " + encapsulate.turn);
			skipTurn = true;
			Node child = new Node(!node.MAX, node.alpha, node.beta);
			encapsulate.turn *= -1;
			node.value = generateValueForMove3(pathOfMoves, new Coordinates(-1,-1), child, player, skipTurn, depth);
			System.out.println("level " + depth + " has no more children");
			pathOfMoves.remove(pathOfMoves.size() -1);
		}else{
			skipTurn = false;
		}
		Iterator<Coordinates> movesIter = moves.iterator();
		int childCounter = 0;
		if(node.MAX){
			//		Do we have a smaller value than Beta in node.value ? In that case, we need to evaluate eventual worse cases (in the children)
			//		If its bigger, it's not going to replace the parent anyway.	
			while(movesIter.hasNext() && node.value < node.beta){
				System.out.println("Max level: " + depth + " child " + ++childCounter + " (" + moves.size() + ") turn: " + encapsulate.turn);
				Node child = new Node(!node.MAX, node.alpha, node.beta);
				int temp = generateValueForMove3(pathOfMoves,(Coordinates) movesIter.next(), child, player, skipTurn, depth);
				if(temp > node.value){
					node.value = temp;
					if(node.value > node.alpha){
						node.alpha = node.value;
					}
				}
				if(node.value >= node.beta && movesIter.hasNext()){
					System.out.println("Max pruning");
				}
				if(!movesIter.hasNext()){
					System.out.println("Max level " + depth +" has no more children");
				}
			}
			pathOfMoves.remove(pathOfMoves.size() -1);
			return node.value;
		}else{
			while(movesIter.hasNext() && node.value > node.alpha){
				System.out.println("Min level: " + depth + " child " + ++childCounter + " (" + moves.size() + ") turn: " + encapsulate.turn);
				Node child = new Node(!node.MAX, node.alpha, node.beta);
				int temp = generateValueForMove3(pathOfMoves,(Coordinates) movesIter.next(), child, player, skipTurn, depth);
				if(temp < node.value){
					node.value = temp;
					if(node.value < node.beta){
						node.beta = node.value;
					}
				}
				if(node.value <= node.alpha && movesIter.hasNext()){
					System.out.println("Min pruning");
				}
				if(!movesIter.hasNext()){
					System.out.println("Min level " + depth +" has no more children");
				}
			}
			pathOfMoves.remove(pathOfMoves.size() -1);
			return node.value;
		}
	}
	
	private void initPathOfMoves(ArrayList<Coordinates> pathOfMoves){
		pathOfMoves.add(new Coordinates(boardSize/2 -1,boardSize/2)); // black disc
		pathOfMoves.add(new Coordinates(boardSize/2,boardSize/2)); // white disc
		pathOfMoves.add(new Coordinates(boardSize/2,boardSize/2 -1)); // black disc
		pathOfMoves.add(new Coordinates(boardSize/2 -1,boardSize/2 -1)); // white disc
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
