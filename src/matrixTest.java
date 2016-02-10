import java.util.ArrayList;

public class matrixTest {
	
	public static void main(String[] args){
		new matrixTest();
	}

	public matrixTest(){
		int[][] matrix = new int[8][8];
		initMatrix(matrix);
		printMatrix(matrix);
		doSmtWithMatrix(matrix);
		printMatrix(matrix);
		
		int a = 0;
		increase(a);
		System.out.println("a = " + a);
		
		ArrayList<Coordinates> pathOfMoves = new ArrayList<Coordinates>();
		addToArrayList(pathOfMoves);
		System.out.println("size of ArrayList = " + pathOfMoves.size());
	}
	
	private void doSmtWithMatrix(int[][] matrix){
		for(int i = 0; i < matrix.length; i++){
			for(int j = 0; j < matrix.length; j++){
				matrix[i][j] = i+j;
			}
		}
	}
	
	private void printMatrix(int[][] matrix){
		for(int i = 0; i < matrix.length; i++){
			System.out.print("| ");
			for(int j = 0; j < matrix.length; j++){
				System.out.print(matrix[i][j] + " ");
			}
			System.out.println("|");
		}
		System.out.println("");
	}
	
	private void initMatrix(int[][] matrix){
		for(int i = 0; i < matrix.length; i++){
			for(int j = 0; j < matrix.length; j++){
				matrix[i][j] = 0;
			}
		}
	}
	
	private void increase(int a){
		a++;
	}
	
	private void addToArrayList(ArrayList<Coordinates> pathOfMoves){
		pathOfMoves.add(new Coordinates(4,4));
	}
}
