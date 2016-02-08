import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

public class AI {

	private int boardSize = 8;
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
		State board = new State(boardSize);
		board.clear();
		Set<Coordinates> moves = gameEngine.findAllLegalMoves(board);
		game.printBoard(board, moves);

		boolean skipTurn = false;
		first = new Node(true);
		first.move =  moves.toArray(new Coordinates[0])[0];
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
			game.printBoard(board, moves);
		}
	}

	private int generateValueForMove(Node node, State board, int player){
		//		Leaf
		board.changeTurn();
		Set<Coordinates> moves = gameEngine.findAllLegalMoves(board);
		if(board.noLegalMoves()){ // moves.isEmpty() och med skipTurn
			return state.calculateScore(player);
		}
		if(node.MAX){
			Iterator movesIter = moves.iterator();
//			if(node.value <= node.beta){
//				Node child = new Node(!node.MAX);
//				node.children.add(child);
//				gameEngine.makeMove(board, (Coordinates) movesIter.next()); // check hasNExt first
//				board.changeTurn();
//				node.value = generateValueForMove(child, board, player);
//				node.alpha = node.value;
//			}
			//		Do we have a smaller value than Beta in node.value ? In that case, we need to evaluate eventual worse cases (in the children)
			//		If its bigger, it's not going to replace the parent anyway.
			while(movesIter.hasNext() && node.value <= node.beta){
				Node child = new Node(!node.MAX, node.alpha, node.beta);
				node.children.add(child);
				gameEngine.makeMove(board, (Coordinates) movesIter.next());
				int temp = generateValueForMove(child, board, player); // flytta till före ifsatsen
				if(temp > node.value){
					node.value = temp;
					node.alpha = node.value;
				}
			}
			return node.value;
		}else{
			Iterator movesIter = moves.iterator();
//			if(node.value >= node.alpha){
//				Node child = new Node(!node.MAX);
//				node.children.add(child);
//				gameEngine.makeMove(board, (Coordinates) movesIter.next()); // check hasNExt first
//				board.changeTurn();
//				node.value = generateValueForMove(child, board, player);
//				node.beta = node.value;
//			}
			//		Do we have a smaller value than Beta in node.value ? In that case, we need to evaluate eventual worse cases (in the children)
			//		If its bigger, it's not going to replace the parent anyway.
			while(movesIter.hasNext() && node.value >= node.alpha){
				Node child = new Node(!node.MAX);
				node.children.add(child);
				gameEngine.makeMove(board, (Coordinates) movesIter.next());
				int temp = generateValueForMove(child, board, player); // flytta till före ifsatsen
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
		public Coordinates move;
		public ArrayList<Node> children;

		public Node(boolean MAX){
			if(MAX){
				value = Integer.MIN_VALUE;
			}else{
				value = Integer.MAX_VALUE;
			}
			alpha = Integer.MIN_VALUE;
			beta = Integer.MAX_VALUE;
		}
		
		public Node(boolean MAX, int alpha, int beta){
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
