import java.util.Scanner;

/**
 * Main game class for 9dt implementation.
 * @author jwtag
 *
 */
public class game {

	private static int[][] board;  // Stores the board.
	
	// booleans for possible win paths eliminated.  true if win path eliminated.
	private static boolean diagLeftRight;
	private static boolean diagRightLeft;
	private static boolean[] cols;
	private static boolean[] rows;
	
	// Stores if game is won.
	private static boolean won;
	
	public static void main(String[] args) {
		setupGame();
		Scanner input = new Scanner(System.in);
		
		input.close();
	}
	
	/**
	 * Updates the booleans in the game that store if certain win routes are possible.
	 * @param row The row where the token is being placed.
	 * @param col The column where the token is being placed.
	 * @param currPlayer The player placing the token.
	 */
	private static void updateBooleans(int row, int col, int currPlayer) {
		// Check diagonals (if applicable)
		if (row == col && !diagLeftRight) {  // check top left -> bottom right
			boolean diagHasZero = false;  // Stores if
			for (int i = 0; i < 4; i++) {
				if (board[i][i] == 0) {
					diagHasZero = true;
				} else if (board[i][i] != currPlayer) {
					diagLeftRight = true;
					break;
				}
			}
			won = !diagHasZero && !diagLeftRight;
		}
		if (!won && row + col == 3 && !diagRightLeft) {  // check bottom left -> top right
			boolean diagHasZero = false;  // Stores if
			for (int i = 0; i < 4; i++) {
				if (board[3 - i][i] == 0) {
					diagHasZero = true;
				} else if (board[3 - i][i] != currPlayer) {
					diagRightLeft = true;
					break;
				}
			}
			won = !diagHasZero && !diagRightLeft;
		}
		// Check column where token was inserted
		if (!won && !cols[col]) {
			for (int i = 0; i < row; i++) {
				if (board[row][col] != currPlayer && board[row][col] != 0) {
					cols[col] = true;
					break;
				}
			}
			won = row == 4 && !cols[col];
		}
		// Check row where token was inserted
		if (!won && !rows[row]) {
			boolean rowHasZero = false;  // Stores if
			for (int i = 0; i < col; i++) {
				if (board[row][col] == 0) {
					rowHasZero = true;
				} else if (board[row][col] != currPlayer) {
					rows[row] = true;
					break;
				}
			}
			won = !rowHasZero && !rows[row];
		}
	}
	
	/**
	 * Attempts to place a token in the board in the specified location.
	 * Returns true if successfully placed, false otherwise.
	 * @param row The row where the token is being placed.
	 * @param col The column where the token is being placed.
	 * @param currPlayer The player placing the token.
	 * @returns true if token is successfully placed, false otherwise.
	 */
	private static boolean placeToken(int col, int currPlayer) {
		if (col < -1 || col > 3) { // If col is not a valid column, return false.
			return false;
		}
		int row = 3;
		while (board[row][col] != 0) row--;  // Get the row for the token.
		if (row != -1) {  // There is a row for the token.
			board[row][col] = currPlayer;
			updateBooleans(row, col, currPlayer);
			return true;
		}
		return false;  // There is no row for the token.
	}
	
	/**
	 * Prints board in a format specified as in the spec.
	 */
	private static void printBoard() {
		for (int[] row : board) {
			System.out.print("|");
			for (int i = 0; i < row.length; i++) {
				System.out.print(" " + row[i]);
			}
			System.out.println();
		}
		System.out.print("+--------");
	}
	
	/**
	 * Sets up the default values of the game.
	 */
	private static void setupGame() {
		 board = new int[4][4];
		 diagLeftRight = false;
		 diagRightLeft = false;
		 cols = new boolean[4];
		 rows = new boolean[4];
		 won = false;
	}
}
