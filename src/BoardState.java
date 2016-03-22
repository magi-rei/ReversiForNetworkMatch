import java.io.Serializable;

//Serializableを実装しないと、Streamで送受信できない
public class BoardState implements Serializable{
	private int size;
	private int[][] box;
	private int currentColor;
	private int blackStone;
	private int whiteStone;
	private boolean end;


	private static final long serialVersionUID = 1L;

	public BoardState(Reversi reversi) {
		this.size = reversi.getBoardSize();
		this.box = reversi.getBox();
		this.currentColor = reversi.getCurrentColor();
		this.blackStone = reversi.getBlackStone();
		this.whiteStone = reversi.getWhiteStone();
		this.end = reversi.isEnd();
	}

	/**
	 * sizeを取得します。
	 *
	 * @return size
	 */
	public int getSize() {
		return size;
	}

	/**
	 * sizeを設定します。
	 *
	 * @param size
	 *            size
	 */
	public void setSize(int size) {
		this.size = size;
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
	 * boxを設定します。
	 *
	 * @param box
	 *            box
	 */
	public void setBox(int[][] box) {
		this.box = box;
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
	 * currentColorを設定します。
	 *
	 * @param currentColor
	 *            currentColor
	 */
	public void setCurrentColor(int currentColor) {
		this.currentColor = currentColor;
	}

	/**
	 * blackStoneを取得します。
	 *
	 * @return blackStone
	 */
	public int getBlackStone() {
		return blackStone;
	}

	/**
	 * blackStoneを設定します。
	 *
	 * @param blackStone
	 *            blackStone
	 */
	public void setBlackStone(int blackStone) {
		this.blackStone = blackStone;
	}

	/**
	 * whiteStoneを取得します。
	 *
	 * @return whiteStone
	 */
	public int getWhiteStone() {
		return whiteStone;
	}

	/**
	 * whiteStoneを設定します。
	 *
	 * @param whiteStone
	 *            whiteStone
	 */
	public void setWhiteStone(int whiteStone) {
		this.whiteStone = whiteStone;
	}

	/**
	 * isEndを取得します。
	 *
	 * @return isEnd
	 */
	public boolean isEnd() {
		return end;
	}

	/**
	 * isEndを設定します。
	 *
	 * @param isEnd
	 *            isEnd
	 */
	public void setEnd(boolean isEnd) {
		this.end = isEnd;
	}

	public String toString(){
		return "黒の数は"+blackStone +",白の数は" +whiteStone;
	}

	public void setID(long a){
		//serialVersionUID = a;
	}

}
