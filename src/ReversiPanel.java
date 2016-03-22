import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.Stack;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.event.MouseInputAdapter;

public class ReversiPanel extends JPanel {
	private int width; // ウィンドウサイズの幅
	private int height; // ウィンドウサイズの高さ
	private int boxWidth; // 1マスの幅
	private int boxHeight; // 1マスの高さ
	private static final int EMPTY = 0;
	private static final int BLACK = 1;
	private static final int WHITE = 2;
	private int currentColor;
	private int blackStone;
	private int whiteStone;
	private int size;
	private int[][] box; // マスごとの石の状態
	private Image buffer = null; // オフラインイメージを入れる
	private Graphics bufferContext = null; // オフラインイメージのグラフィックスを入れる
	private Font font, turnFont, coordFont;
	private boolean end;
	private Hand hand;
	private int yourTurn= 0;
	private Stack<Integer> stack;

	ReversiPanel(BoardState bs) {
		size = bs.getSize();
		box = bs.getBox();
		currentColor = bs.getCurrentColor();
		this.addMouseListener(new MouseListener());
	}

	ReversiPanel(BoardState bs, int turn) {
		size = bs.getSize();
		box = bs.getBox();
		currentColor = bs.getCurrentColor();
		this.addMouseListener(new MouseListener());
		yourTurn = turn;
		stack = new Stack<Integer>();
		hand  = null;
	}


	public void paintComponent(Graphics g) {
		if (buffer == null) { // 一度だけ初期化
			width = getSize().width;
			height = getSize().height;
			boxWidth = width / size;
			boxHeight = height / size;
			buffer = createImage(width, height); // オフラインイメージを生成
			bufferContext = buffer.getGraphics(); // オフラインイメージのグラフィックスを得る
			font = new Font(null, Font.PLAIN, 20);
			turnFont = new Font(null, Font.PLAIN, 40);
			coordFont = new Font(null, Font.PLAIN, 20);
			bufferContext.setColor(Color.lightGray);
			bufferContext.fillRect(0, 0, width, height);

			// 盤を描く
			bufferContext.setColor(Color.GREEN);
			bufferContext.fillRect(boxWidth, boxHeight, width - boxWidth * 2, height - boxHeight * 2);
			// 枠の線を描く
			bufferContext.setColor(Color.WHITE);
			for (int i = boxWidth; i < width; i += boxWidth) {
				bufferContext.drawLine(i, boxHeight, i, height - boxHeight);
			}
			for (int i = boxHeight; i < height; i += boxHeight) {
				bufferContext.drawLine(boxWidth, i, width - boxWidth, i);
			}
			// 座標を描く
			String[] col = { "a", "b", "c", "d", "e", "f", "g", "h" };
			String[] row = { "1", "2", "3", "4", "5", "6", "7", "8" };
			bufferContext.setColor(Color.BLACK);
			bufferContext.setFont(coordFont);
			int fontSize = coordFont.getSize();
			for (int i = 0; i < 8; i++) {
				int x = boxWidth * (i + 1) + (boxWidth - fontSize / 2) / 2;
				int y = boxHeight * (i + 1) + (boxHeight + fontSize / 2) / 2;
				bufferContext.drawString(col[i], x, boxHeight - fontSize / 2);
				bufferContext.drawString(row[i], boxWidth - fontSize, y);
			}
		}
		// 石を描く
		for (int j = 0; j < size; j++) {
			for (int i = 0; i < size; i++) {
				if (box[i][j] == BLACK) {
					bufferContext.setColor(Color.BLACK);
				} else if (box[i][j] == WHITE) {
					bufferContext.setColor(Color.WHITE);
				} else {
					continue;
				}
				bufferContext.fillOval(i * boxWidth, j * boxHeight, boxWidth, boxHeight);
			}
		}

		//自分がどちらの手番なのかを表示
		if(yourTurn != 0){
		String turnStrMe1 = "あなたは";
		String turnStrMe2 = "";
		if (yourTurn == BLACK) {
			turnStrMe2 += "黒";
			bufferContext.setColor(Color.BLACK);
		} else if (yourTurn == WHITE) {
			turnStrMe2 += "白";
			bufferContext.setColor(Color.WHITE);
		}
		bufferContext.setFont(font);
		bufferContext.setColor(Color.BLACK);
		bufferContext.drawString(turnStrMe1, width / 4 - font.getSize()*6, boxHeight / 2);

		bufferContext.setFont(turnFont);
		bufferContext.drawString(turnStrMe2, width / 4 - turnFont.getSize(), boxHeight / 2);


		}

		// 手番を描く
		String turnStr1 = "";
		String turnStr2 = "の番です";
		if (currentColor == BLACK) {
			turnStr1 = "黒";
			bufferContext.setColor(Color.BLACK);
		} else if (currentColor == WHITE) {
			turnStr1 = "白";
			bufferContext.setColor(Color.WHITE);
		}
		bufferContext.setFont(turnFont);
		bufferContext.drawString(turnStr1, width / 2 - turnFont.getSize(), boxHeight / 2);

		bufferContext.setFont(font);
		bufferContext.setColor(Color.BLACK);
		bufferContext.drawString(turnStr2, width / 2, boxHeight / 2);

		// 石の数を描く
		String stoneStr = "　黒：" + blackStone + "　白：" + whiteStone;
		bufferContext.drawString(stoneStr, (width - font.getSize() * stoneStr.length()) / 2, height - boxHeight / 2);

		buffer.getGraphics();
		g.drawImage(buffer, 0, 0, this); // オフラインイメージをパネルに描く

		// 書いた文字を消す
		bufferContext.setColor(Color.lightGray);
		bufferContext.drawString(stoneStr, (width - font.getSize() * stoneStr.length()) / 2, height - boxHeight / 2);
		bufferContext.setFont(turnFont);
		bufferContext.drawString(turnStr1, width / 2 - turnFont.getSize(), boxHeight / 2);
	}

	public void updade(BoardState bs) {
		box = bs.getBox();
		currentColor = bs.getCurrentColor();
		blackStone = bs.getBlackStone();
		whiteStone = bs.getWhiteStone();
		end = bs.isEnd();
		repaint();
		if (end) {
			gameEnd();
		}
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
				//currentColor = opponentColor(currentColor);

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
					stack.pop();
					result++;
				}
			} else {
				stack.clear();
			}
			return result;
		}

	public void gameEnd() {
		Object msg;
		if (blackStone > whiteStone) {
			msg = "黒の勝ちです。";
		} else if (blackStone < whiteStone) {
			msg = "白の勝ちです。";
		} else {
			msg = "引き分けです。";
		}
		JOptionPane.showMessageDialog(this, msg, "ゲーム終了", JOptionPane.YES_OPTION);
	}

	public Point pointToBoardPoint(Point p) {
		for (int j = 0; j < size; j++) {
			for (int i = 0; i < size; i++) {
				if (p.getX() > i * boxWidth && p.getX() < (i + 1) * boxWidth && p.getY() > j * boxHeight
						&& p.getY() < (j + 1) * boxHeight) {
					p.setLocation(i, j);
					break;
				}
			}
		}
		System.out.println("打った位置は"+p);
		return p;
	}

	public Hand getHand(){
		return hand;
	}

	public void setHand(int x, int y, int turn){
		hand =new Hand(x,y,turn);
	}


	class MouseListener extends MouseInputAdapter {
		public void mouseClicked(MouseEvent e) {
			Point p = e.getPoint();
			//マウスのクリック位置をHandに変換
			p=pointToBoardPoint(p);
			if(yourTurn == currentColor){
				System.out.println("Your Turn");
				int r = reverse(p.x,p.y);
				System.out.println(r);
				if(r!= 0){
					hand =new Hand(p.x,p.y,yourTurn);
			}


			}
		}
	}


}
