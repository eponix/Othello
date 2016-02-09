import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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
		State boardClone = board.clone();
		Set<Coordinates> moves = gameEngine.findAllLegalMoves(board);
		first = new Node(true);
		board.changeTurn();
		for(Coordinates move : moves){
			suggestions.put(move, generateValueForMove(first, board, -1, false, 0));
			game.printBoard(boardClone, suggestions);
		}
		System.out.println();
		game.printBoard(boardClone, suggestions);
	}

	private int generateValueForMove(Node node, State board, int player, boolean skipTurn, int counter){
		counter++;
		board.changeTurn();
		Set<Coordinates> moves = gameEngine.findAllLegalMoves(board);
		if(moves.isEmpty() && skipTurn){ // Leaf
//			System.out.println("leaf, depth: " + counter);
			int score = board.calculateScore(player);
			return score;
		}else if(moves.isEmpty()){
			skipTurn = true;
			Node child = new Node(!node.MAX, node.alpha, node.beta);
			node.value = generateValueForMove(child, board, player, skipTurn, counter);
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
			while(movesIter.hasNext() && node.value <= node.beta){
				State boardClone = board.clone();
				Node child = new Node(!node.MAX, node.alpha, node.beta);
				gameEngine.makeMove(boardClone, (Coordinates) movesIter.next());
				int temp = generateValueForMove(child, boardClone, player, skipTurn, counter); // flytta till före ifsatsen
				if(temp > node.value){
					node.value = temp;
					node.alpha = node.value;
				}
				System.out.println("Children: "+ ++childCounter + " on level " + counter);
			}
			if(counter < 10){
				System.out.println("level "+ counter + " reached");
			}
			return node.value;
		}else{
			//		Do we have a smaller value than Beta in node.value ? In that case, we need to evaluate eventual worse cases (in the children)
			//		If its bigger, it's not going to replace the parent anyway.
			while(movesIter.hasNext() && node.value >= node.alpha){
				State boardClone = board.clone();
				Node child = new Node(!node.MAX);
				gameEngine.makeMove(boardClone, (Coordinates) movesIter.next());
				int temp = generateValueForMove(child, boardClone, player, skipTurn, counter); // flytta till före ifsatsen
				if(temp < node.value){
					node.value = temp;
					node.beta = node.value;
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
