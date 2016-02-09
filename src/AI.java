import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

public class AI {

	private int boardSize = 5;
	private GameEngine gameEngine;
	private Game game;
	private Node first;

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
				suggestions.put(move, generateValueForMove(startingNode, boardClone, boardClone.turn*-1, false, 0));
				System.out.println("Time for generating value for move: " + (System.currentTimeMillis()-start));
//				game.printBoard(boardClone, suggestions);
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

	private int generateValueForMove(Node node, State board, int player, boolean skipTurn, int depth){
		depth++;
//		game.printBoard(board, new HashMap<Coordinates, Integer>());
//		System.exit(0);
		Set<Coordinates> moves = gameEngine.findAllLegalMoves(board);
		
//		if(depth < 10){
//			System.out.println("Level: " + counter + " with " + moves.size() + " children");
//		}
		
		if(moves.isEmpty() && skipTurn){ // Leaf
			//			System.out.println("leaf, depth: " + counter);
			int score = board.calculateScore(player);
//			System.out.println("level "+ counter + " is a leaf with value: " + score);
			return score;
		}else if(moves.isEmpty()){
//			System.out.println("level "+ counter + " has no moves, changes turn");
			skipTurn = true;
			Node child = new Node(!node.MAX, node.alpha, node.beta);
			board.changeTurn();
			node.value = generateValueForMove(child, board, player, skipTurn, depth);
//			System.out.println("no more children on level: " + counter);
		}else{
			skipTurn = false;
		}
		Iterator<Coordinates> movesIter = moves.iterator();
		int childCounter = 0;
		if(node.MAX){
			//		Do we have a smaller value than Beta in node.value ? In that case, we need to evaluate eventual worse cases (in the children)
			//		If its bigger, it's not going to replace the parent anyway.	
//			if(node.value >= node.beta){
//				System.out.println("Maxpruning on level " + counter);
//			}
			while(movesIter.hasNext() && node.value < node.beta){
//				System.out.println("Children: "+ ++childCounter + "(" + moves.size() + ") on level " + counter);
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

//				if(node.value > node.beta){
//					System.out.println("Maxpruning on level: " + depth);
//				}
//				if(!movesIter.hasNext()){
//					System.out.println("Maxlevel " + counter + "has no more children");
//				}
			}
//			if(depth < 10){
//				System.out.println("level "+ counter + " reached");
//			}
			return node.value;
		}else{
//			if(node.value < node.alpha){
//				System.out.println("Minpruning on level " + counter);
//			}
			while(movesIter.hasNext() && node.value > node.alpha){
//				System.out.println("Children: "+ ++childCounter + "(" + moves.size() + ") on level " + counter);
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
//				if(node.value < node.alpha){
//					System.out.println("Minpruning on level: " + counter);
//				}
//				if(!movesIter.hasNext()){
//					System.out.println("Minlevel " + counter + "has no more children");
//				}
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

	private class Node {
		public int value;
		public int alpha;
		public int beta;
		public boolean MAX;
		//		public Coordinates move;
		//		public ArrayList<Node> children;

		public Node(boolean MAX){
			//			children = new ArrayList<Node>();
			this.MAX = MAX;
			if(MAX){
				value = Integer.MIN_VALUE;
			}else{
				value = Integer.MAX_VALUE;
			}
			alpha = Integer.MIN_VALUE;
			beta = Integer.MAX_VALUE;
		}

		public Node(boolean MAX, int alpha, int beta){
			//			children = new ArrayList<Node>();
			this.MAX = MAX;
			if(MAX){
				value = Integer.MIN_VALUE;
			}else{
				value = Integer.MAX_VALUE;
			}
			this.alpha = alpha;
			this.beta = beta;
		}

	}
}
