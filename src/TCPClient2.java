


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

import javax.swing.JFrame;



class TCPClient2 extends JFrame
{
	//盤面のクラスを用意
			static BoardState bs;
			static ReversiPanel panel = null;
			static int turn = 0;
			static int stone = 4;
			static Hand hand = null;

	public TCPClient2(String title, BoardState bs) {
		super(title);
		panel = new ReversiPanel(bs,turn);
		panel.addMouseListener(panel.new MouseListener());
		add(panel);
	}
	public static void main(String[] args)
	{


		ObjectOutputStream oos = null;

		ObjectInputStream ois = null;


	try {
		Socket socket;
		Object obj;
 	    BoardState newBs;
		while(true){
	    //
	    //InetSocketAddress socketAddress =
		//new InetSocketAddress(args[0], Integer.parseInt(args[1]));

		//InetSocketAddress socketAddress = new InetSocketAddress("localhost", 9999);
		InetSocketAddress socketAddress = new InetSocketAddress("localhost", 9999);

		//隣の人にチャットができるか確認してみよう。
		//InetSocketAddress socketAddress = new InetSocketAddress("133.14.47.131", 8888);


	    socket = new Socket();
	    socket.connect(socketAddress, 10000);

	    InetAddress inetadrs;
	    if ((inetadrs = socket.getInetAddress()) != null) {
		System.out.println("address:" + inetadrs);
	    }
	    else {
		System.out.println("Connection fail");
		socket.close();
		return;
	    }


	    //if(ois == null)
	    ois = new ObjectInputStream(socket.getInputStream());
	    if(turn ==0){


	    	turn = (Integer)(ois.readObject());
	    	System.out.println("あなたのターンは"+turn);
	    	//ois.close();
	    	//サーバーから、BoardStateクラスをもらう
	 	   // ois = new ObjectInputStream(socket.getInputStream());
	 	    obj = ois.readObject();
	 	    newBs = (BoardState)(obj);
	 	    stone = newBs.getBlackStone()+newBs.getWhiteStone();
	    }else{
	    	//サーバーから、BoardStateクラスをもらう
	 	    obj = ois.readObject();
	 	    newBs = (BoardState)(obj);
	    }

	    Thread.sleep(500);



	    System.out.println("石の数は"+stone);
	    while(!(obj instanceof BoardState) || stone != newBs.getBlackStone()+newBs.getWhiteStone()){
	    	ois.reset();
	    	obj = ois.readObject();
	    	newBs = (BoardState)(obj);
	    	System.out.println(newBs);
	    	Thread.sleep(500);
	    }
    	bs = newBs;
    	System.out.println("盤面を受け取りました");
    	System.out.println(bs);


    	//（最初は）盤面を生成
    	if(panel == null){
    		System.out.println("盤面を生成");
    		TCPClient2 tcpC = new TCPClient2("Reversi",bs);
    		tcpC.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    		tcpC.setSize(666, 689);
    		tcpC.setVisible(true);
    		System.out.println("盤面を生成しました");

    	}
    		//もらったBoardStateを盤面に反映
    		panel.updade(bs);
    		hand = panel.getHand();





    	//BoardStateのisEndがtrueなら、プログラムを終える
    	if(bs.isEnd()){
    		System.out.print("ゲーム終了");
    		break;
    	}

    	//System.out.println("今のターンは"+turn);
    	if(turn == bs.getCurrentColor()){
    		System.out.println("あなたのターンです");
    		while(true){
    			Thread.sleep(500);
    			if(panel.getHand()!= hand){

    				System.out.println("手を取得");
    				hand = panel.getHand();
    				//if(oos == null)
    			    	oos = new ObjectOutputStream(socket.getOutputStream());
    			        //データコンテナオブジェクトをクライアントに送信
    			        oos.writeObject(hand);
    			        Thread.sleep(500);
    			        oos.flush();
    			        oos.close();
    				break;
    			}
    		}
    	}

    	stone++;





    	ois.close();



		}

		socket.close();
	}
	catch (IOException e) {
	    e.printStackTrace();
	} catch (ClassNotFoundException e) {
		// TODO 自動生成された catch ブロック
		e.printStackTrace();
	} catch (InterruptedException e) {
		// TODO 自動生成された catch ブロック
		e.printStackTrace();
	}
    }
}
