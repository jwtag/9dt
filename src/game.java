import java.util.ArrayList;
import java.util.List;
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
	
	// Stores number of invalid paths to a win.  Once equal to 10, game is a draw.
	private static int invalidPaths;
	
	// Stores if game is won.
	private static boolean won;
	
	public static void main(String[] args) {
		setupGame();
		List<Integer> plays = new ArrayList<Integer>();  // Stores all of the plays so far.
		Scanner input = new Scanner(System.in);
		boolean isPlayer1 = true;  // Stores if it is player 1's turn
		boolean exit = false;  // Stores if exit has been called.
		while (!exit) {
			String cmd = input.nextLine();
			if (cmd.equals("GET")) {
				for (Integer i : plays) {
					System.out.println(i);
				}
			} else if (cmd.equals("BOARD")) {
				printBoard();
			} else if (cmd.equals("EXIT")) {
				exit = true;
			} else if (cmd.equals("RESET")) {
				// Reset board/game.
				isPlayer1 = true;
				plays = new ArrayList<Integer>();
				setupGame();
			} else if (won || invalidPaths == 10) {
				System.out.println("GAME OVER");
			} else if (cmd.contains("PUT")) {
				
				// Setup the values being passed to placeToken.
				int col = 0;
				
				// Use try/catch to handle potential parseInt exception
				try {
					col = Integer.parseInt(cmd.substring(4)) - 1;
				} catch (NumberFormatException e) {
					System.out.println("INVALID COMMAND");
					continue;
				}
				int currPlayer = 1;
				if (isPlayer1 == false) currPlayer = 2;
				
				// Attempt to place the token, print ERROR if failure.
				if (!placeToken(col, currPlayer)) {
					System.out.println("ERROR");
				} else {
					plays.add(col + 1);
					
					// Endgame conditions. Print result and reset.
					if (won || invalidPaths == 10) {
						
						// Print result.
						if (won) System.out.println("WIN");
						if (invalidPaths == 10) System.out.println("DRAW");
					} else {
						System.out.println("OK");
						isPlayer1 = !isPlayer1;
					}
				}
			} else {
				System.out.println("INVALID COMMAND!  TRY AGAIN.");
			}
		}
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
					invalidPaths++;
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
					invalidPaths++;
					break;
				}
			}
			won = !diagHasZero && !diagRightLeft;
		}
		
		// Check column where token was inserted
		if (!won && !cols[col]) {
			for (int i = row; i < 4; i++) {
				if (board[i][col] != currPlayer) {
					cols[col] = true;
					invalidPaths++;
					break;
				}
			}
			won = row == 0 && !cols[col];
		}
		
		// Check row where token was inserted
		if (!won && !rows[row]) {
			boolean rowHasZero = false;  // Stores if
			for (int i = 0; i < 4; i++) {
				if (board[row][i] == 0) {
					rowHasZero = true;
				} else if (board[row][i] != currPlayer) {
					rows[row] = true;
					invalidPaths++;
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
		while (row >= 0 && board[row][col] != 0) row--;  // Get the row for the token.
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
		System.out.print("+--------\n  1 2 3 4\n");
	}
	
	/**
	 * Sets up the values used to run the game.
	 */
	private static void setupGame() {
		 board = new int[4][4];
		 diagLeftRight = false;
		 diagRightLeft = false;
		 cols = new boolean[4];
		 rows = new boolean[4];
		 won = false;
		 invalidPaths = 0;
	}
}
