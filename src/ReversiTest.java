import java.awt.Point;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;
import javax.swing.event.MouseInputAdapter;

public class ReversiTest extends JFrame {
	Reversi reversi;
	ReversiPanel panel;
	BoardState bs;

	public ReversiTest(String title) {
		super(title);
		reversi = new Reversi();
		bs = new BoardState(reversi);
		panel = new ReversiPanel(bs);
		panel.addMouseListener(new MouseListener());
		add(panel);
	}

	public static void main(String[] args) {
		ReversiTest test = new ReversiTest("リバーシ");
		test.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		test.setSize(666, 689);
		test.setVisible(true);
	}

	public void sendHand(Point p) {
		// サーバ側の処理
		reversi.putStone(p.x,p.y);//←に送られてきたHandが入る
		bs.setBox(reversi.getBox());
		bs.setCurrentColor(reversi.getCurrentColor());
		bs.setBlackStone(reversi.getBlackStone());
		bs.setWhiteStone(reversi.getWhiteStone());
		bs.setEnd(reversi.isEnd());

		// クライアント側の処理
		panel.updade(bs);//←に送られてきたbsが入る
	}
	class MouseListener extends MouseInputAdapter {
		public void mouseClicked(MouseEvent e) {
			Point p = e.getPoint();
			//マウスのクリック位置をHandに変換
			sendHand((panel.pointToBoardPoint(p)));
		}
	}
}
