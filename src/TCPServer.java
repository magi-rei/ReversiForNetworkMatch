

//ChatServer.java
//チャットサーバープログラム
//チャットクライアントプログラムからの接続を待つ。
//接続後は1行の文字列読み取りを行い、接続終了する。
//java CharServer 99
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;





class TCPServer extends Thread
{

    public static void main(String[] args) throws IOException
    {
    	TCPServer server = new TCPServer();
    	server.start();

    }

    public void run()
    {
    	System.out.println("Game Start");
    	ServerSocket[] srvSock = new ServerSocket[2];

    	//ターン処理の変数
    	int turn = 0;
	try {
	    // サーバーソケット作成
		int port=9999;
		int port2 = 8088;

	    srvSock[0] = new ServerSocket(port);
	    srvSock[1] = new ServerSocket(port2);
	    TurnThread[] thread = new TurnThread[2];

	    //Reversiの盤面をセット

	    Reversi reversi = new Reversi();
	    BoardState bs = new BoardState(reversi);


	 // 先攻と後攻それぞれのスレッドを走らせる
	    for(int i=0;i<2;i++){
	    	turn++;
	    	Socket socket = srvSock[i].accept();
	    	thread[i] = new TurnThread(socket,turn,reversi,bs,srvSock[i]);
	    	thread[i].start();
	    }



	    thread[0].join();
	    thread[1].join();




	    System.out.println("Game End");
	}
	catch (IOException e) {
	    e.printStackTrace();
	} catch (InterruptedException e) {
		// TODO 自動生成された catch ブロック
		e.printStackTrace();
	}
    }


}


class TurnThread extends Thread {

	private static final int BLACK = 1;
	private static final int WHITE = 2;


	  private Socket socket;
	  private int nowTurn = 1;
	  private int turn= 0;
	  private ServerSocket srvSock;
	  Hand hand = null;
	  Reversi reversi;
	    BoardState bs;
	    BoardState oldBs=null;
	    ObjectOutputStream oos = null;
	    ObjectInputStream ois = null;

	  public TurnThread(Socket socket,int turn,Reversi reversi,BoardState bs,ServerSocket srvSock) {
	    this.socket = socket;
	    System.out.println("接続されました "
	                       + socket.getRemoteSocketAddress());
	    this.turn = turn;
	    this.reversi = reversi;
	    this.bs = bs;
	    this.srvSock = srvSock;
	  }

	  public void run() {
		  //一度だけsocketをつなぐ
		  try {
			  if(socket.isClosed()){

					socket = srvSock.accept();

      	}



			  oos = new ObjectOutputStream(socket.getOutputStream());
	        while(true){
	  	    //　通信処理



	        		System.out.println("OutputStreamを生成"+turn);

	          //データコンテナオブジェクトをクライアントに送信
	          System.out.println(bs.getCurrentColor()+"のターンになりました"+turn);

	          oos.reset();
	          oos.writeObject(bs);
	          System.out.println(bs+"の盤面を送信しました"+turn);

	          if(bs.isEnd()){
	              	break;
	              }

	          //自分のターンの時のみ、手の入力を受け付ける
	              if(turn == nowTurn){
	            	  if(ois == null)
	            	  ois = new ObjectInputStream(socket.getInputStream());


	            	  System.out.println("手を受け取り待ち"+turn);

	              	hand = (Hand) (ois.readObject());
	              	System.out.println("手を受け取りました"+turn);
	              	System.out.println(hand);

	              	reversi.putStone(hand.getI(),hand.getJ());

		              bs.setBox(reversi.getBox());
		      		bs.setCurrentColor(reversi.getCurrentColor());

		      		bs.setBlackStone(reversi.getBlackStone());
		      		bs.setWhiteStone(reversi.getWhiteStone());
		      		bs.setEnd(reversi.isEnd());





	              }else{
	            	  waitThread();

	              }

	              nowTurn = opponentColor(nowTurn);


	        }

	        ois.close();
	        oos.close();
            socket.close();
		  }catch (IOException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}


	  }

	  //もう片方のスレッドでの処理を待つ
	  synchronized void waitThread(){
		  int n[] = new int[2];
		  n[0] = bs.getBlackStone();
		  n[1] = bs.getWhiteStone();
		  System.out.println(opponentColor(turn)+"のターンを待ちます"+turn);
		  while(n[0] == bs.getBlackStone() && n[1] == bs.getWhiteStone()){

			  try {
				sleep(1000);
			} catch (InterruptedException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}

		  }

		  System.out.println(bs + "ウェイト"+turn);
	  }

//ターンを引数として与えると、相手のターンを返す
	  public int opponentColor(int color) {
			if (color == BLACK) {
				return WHITE;
			}
			if (color == WHITE) {
				return BLACK;
			}
			return -1;
		}
}