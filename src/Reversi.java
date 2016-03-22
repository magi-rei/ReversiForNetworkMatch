import java.util.Stack;

public class Reversi {
	private static final int BOARD_SIZE = 8;
	private static final int EMPTY = 0;
	private static final int BLACK = 1;
	private static final int WHITE = 2;
	private static final int OUTSIDE = 3; // 盤外
	private int currentColor;
	private int blackStone;
	private int whiteStone;
	private int size;
	private int[][] box; // マスごとの石の状態
	private Stack<Integer> stack;
	private boolean isEnd;

	Reversi() {
		this.size = BOARD_SIZE + 2;// 盤の外側を追加

		clear();

		stack = new Stack<Integer>();
	}

	Reversi(boolean flag){
		if(true){
			this.size = BOARD_SIZE + 2;// 盤の外側を追加
			clear();
			stack = new Stack<Integer>();

			for(int i=0;i<8;i++){
				for(int j=0;j<8;j++){

					if(i==7){
						if(j<6){
							box[i+1][j+1] = BLACK;
						}else if(j==6){
							box[i+1][j+1] = WHITE;
						}
					}else{
						box[i+1][j+1] = BLACK;
					}

				}
			}
			blackStone = 62;
			whiteStone = 1;


		}
	}

	// 盤面の初期化
	public void clear() {
		box = new int[size][size];
		for (int j = 0; j < size; j++) {
			for (int i = 0; i < size; i++) {
				box[i][j] = OUTSIDE;
			}
		}
		for (int j = 1; j < size - 1; j++) {
			for (int i = 1; i < size - 1; i++) {
				box[i][j] = EMPTY;
			}
		}
		// 初期位置に石を置く
		box[size / 2 - 1][size / 2 - 1] = WHITE;
		box[size / 2][size / 2] = WHITE;
		box[size / 2][size / 2 - 1] = BLACK;
		box[size / 2 - 1][size / 2] = BLACK;
		blackStone = 2;
		whiteStone = 2;
		currentColor = BLACK;
	}

	// 逆の石の色を返す
	public int opponentColor(int color) {
		if (color == BLACK) {
			return WHITE;
		}
		if (color == WHITE) {
			return BLACK;
		}
		return -1;
	}

	// 隣接するマスに返せる石があるか調べる
	public int reverse(int x, int y) {
		int result = 0;// 返せる石の数
		if (box[x][y] != EMPTY) {
			return result;
		}
		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				if (box[x + i][y + j] == opponentColor(currentColor)) {
					result += reverseLine(x, y, i, j, currentColor);
				}
			}
		}
		if (result != 0) {
			box[x][y] = currentColor;
			if (currentColor == BLACK) {
				blackStone += result + 1;
				whiteStone -= result;
			} else if (currentColor == WHITE) {
				whiteStone += result + 1;
				blackStone -= result;
			}
			currentColor = opponentColor(currentColor);
		}
		return result;
	}

	// 1方向で返せる石を調べる
	public int reverseLine(int x, int y, int xdir, int ydir, int color) {
		int result = 0; // 返した石の数
		do {
			x += xdir;
			y += ydir;
			stack.push(y);
			stack.push(x);
		} while (box[x][y] == opponentColor(color));
		if (box[stack.pop()][stack.pop()] == color) {// 挟まれているならば
			while (!stack.isEmpty()) {
				box[stack.pop()][stack.pop()] = color;
				result++;
			}
		} else {
			stack.clear();
		}
		return result;
	}

	public String putStone(int x, int y) {
		if (reverse(x, y) != 0) {
			if (blackStone + whiteStone >= (size - 2) * (size - 2)) {
				isEnd = true;
			}
		}
		return "";
	}

	/**
	 * currentColorを取得します。
	 *
	 * @return currentColor
	 */
	public int getCurrentColor() {
		return currentColor;
	}

	/**
	 * sizeを取得します。
	 *
	 * @return size
	 */
	public int getBoardSize() {
		return size;
	}

	/**
	 * boxを取得します。
	 *
	 * @return box
	 */
	public int[][] getBox() {
		return box;
	}

	/**
	 * isEndを取得します。
	 *
	 * @return isEnd
	 */
	/**
	 * blackStoneを取得します。
	 *
	 * @return blackStone
	 */
	public int getBlackStone() {
		return blackStone;
	}

	/**
	 * whiteStoneを取得します。
	 *
	 * @return whiteStone
	 */
	public int getWhiteStone() {
		return whiteStone;
	}

	public boolean isEnd() {
		return isEnd;
	}

}
