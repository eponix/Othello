import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

public class AI {

	private int boardSize = 4;
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
		while(true){
			State boardClone = board.clone();
			Set<Coordinates> moves = gameEngine.findAllLegalMoves(board);
			first = new Node(true);
			board.changeTurn();
			for(Coordinates move : moves){
				suggestions.put(move, generateValueForMove(first, boardClone, boardClone.turn, false, 0));
				//				game.printBoard(boardClone, suggestions);
			}
			board.changeTurn();
			System.out.println();
			game.printBoard(board, suggestions);
			System.out.println("make a move " + board.turn);
			int row = scan.nextInt();
			int col = scan.nextInt();
			gameEngine.makeMove(board, new Coordinates(row,col));
			suggestions.clear();
			game.printBoard(board, suggestions);
			board.changeTurn();
		}

	}

	private int generateValueForMove(Node node, State board, int player, boolean skipTurn, int counter){
		counter++;
		board.changeTurn();
		Set<Coordinates> moves = gameEngine.findAllLegalMoves(board);
		if(moves.isEmpty() && skipTurn){ // Leaf
			//			System.out.println("leaf, depth: " + counter);
			int score = board.calculateScore(player);
			System.out.println("level "+ counter + " is a leaf with value: " + score);
			return score;
		}else if(moves.isEmpty()){
			System.out.println("level "+ counter + " has no moves, changes turn");
			skipTurn = true;
			Node child = new Node(!node.MAX, node.alpha, node.beta);
			node.value = generateValueForMove(child, board, player, skipTurn, counter);
			System.out.println("no more children on level: " + counter);
			if(node.MAX){
				node.alpha = node.value;
			}else{
				node.beta = node.value;
			}
		}else{
			skipTurn = false;
		}
		Iterator<Coordinates> movesIter = moves.iterator();
		if(node.MAX){
			int childCounter = 0;
			//		Do we have a smaller value than Beta in node.value ? In that case, we need to evaluate eventual worse cases (in the children)
			//		If its bigger, it's not going to replace the parent anyway.	
			if(node.value > node.beta){
				System.out.println("Maxpruning on level " + counter);
			}
			while(movesIter.hasNext() && node.value <= node.beta){
				System.out.println("Children: "+ ++childCounter + "(" + moves.size() + ") on level " + counter);
				State boardClone = board.clone();
				Node child = new Node(!node.MAX, node.alpha, node.beta);
				gameEngine.makeMove(boardClone, (Coordinates) movesIter.next());
				int temp = generateValueForMove(child, boardClone, player, skipTurn, counter);
				if(temp > node.value){
					node.value = temp;
					node.alpha = node.value;
				}
				if(node.value > node.beta){
					System.out.println("Maxpruning on level: " + counter);
				}
				if(!movesIter.hasNext()){
					System.out.println("Maxlevel " + counter + "has no more children");
				}
			}
			if(counter < 10){
				System.out.println("level "+ counter + " reached");
			}
			return node.value;
		}else{
			int childCounter = 0;
			//		Do we have a smaller value than Beta in node.value ? In that case, we need to evaluate eventual worse cases (in the children)
			//		If its bigger, it's not going to replace the parent anyway.
			if(node.value < node.alpha){
				System.out.println("Minpruning on level " + counter);
			}
			while(movesIter.hasNext() && node.value >= node.alpha){
				System.out.println("Children: "+ ++childCounter + "(" + moves.size() + ") on level " + counter);
				State boardClone = board.clone();
				Node child = new Node(!node.MAX);
				gameEngine.makeMove(boardClone, (Coordinates) movesIter.next());
				int temp = generateValueForMove(child, boardClone, player, skipTurn, counter);
				if(temp < node.value){
					node.value = temp;
					node.beta = node.value;
				}
				if(node.value < node.alpha){
					System.out.println("Minpruning on level: " + counter);
				}
				if(!movesIter.hasNext()){
					System.out.println("Minlevel " + counter + "has no more children");
				}
			}
			return node.value;
		}

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
