

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





class TCPServer
{

    public static void main(String[] args) throws IOException
    {
    	System.out.println("Game Start");
    	ServerSocket[] srvSock = new ServerSocket[2];

    	//ターン処理の変数
    	int turn = 0;
	try {
	    // サーバーソケット作成
      //起動時パラメータからポートを読み取り、
      //そのポートで接続要求を待つ
      //ServerSocketクラスはクライアントからの接続を待ち、
      //srvSock.accept();によって接続したSocketオブジェクト
      //を返す。
      //その後の通信には、このSocketオブジェクトを使用する。
	    //int port = Integer.parseInt(args[0]);
		int port=9999;
		int port2 = 8001;

	    srvSock[0] = new ServerSocket(port);
	    srvSock[1] = new ServerSocket(port2);
	    TurnThread[] thread = new TurnThread[2];

	    //Reversiの盤面をセット

	    Reversi reversi = new Reversi();
	    BoardState bs = new BoardState(reversi);


	 // 接続待機。接続完了後、次行命令に移る。
	    for(int i=0;i<2;i++){
	    	turn++;
	    	Socket socket = srvSock[i].accept();
	    	thread[i] = new TurnThread(socket,turn,reversi,bs,srvSock[i]);
	    	thread[i].start();
	    }



	    thread[0].join();
	    System.out.println("0が終わり");
	    thread[1].join();




	    System.out.println("Game End");
	}
	catch (IOException e) {
	    e.printStackTrace();
	} catch (InterruptedException e) {
		// TODO 自動生成された catch ブロック
		e.printStackTrace();
	} finally{
		if(srvSock[0] != null){
			//srvSock[0].close();
		}
	}
    }



}


class TurnThread extends Thread {

	private static final int BLACK = 1;
	private static final int WHITE = 2;

	  private Socket socket;
	  private int nowTurn = 0;
	  private int turn= 0;
	  private ServerSocket srvSock;
	  Hand hand = null;
	  Reversi reversi;
	    BoardState bs;
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
	    try {


	        while(true){
	  	    //　通信処理

	        	if(socket.isClosed()){
	        		socket = srvSock.accept();
	        	}



	          //二人のプレイヤーに、自らの色を教える
	        	//if(oos == null){
	        		System.out.println("OutputStreamを生成"+turn);
	        	oos = new ObjectOutputStream(socket.getOutputStream());
	        	//}

	          if(nowTurn == 0){
	        	  //oos = new ObjectOutputStream(socket.getOutputStream());
	        	  //データコンテナオブジェクトをクライアントに送信
	        	  oos.writeObject(turn);

	          	sleep(500);
	              //oos.close();
	          	//oos.flush();
	              nowTurn = 1;

	          }

	          //oos = new ObjectOutputStream(socket.getOutputStream());
	          //データコンテナオブジェクトをクライアントに送信
	          //oos = new ObjectOutputStream(socket.getOutputStream());
	          System.out.println(bs.getCurrentColor()+"のターンになりました");
	          oos.writeObject(bs);
	          System.out.println(bs+"の盤面を送信しました"+turn);
	          System.out.println(bs);
	          sleep(500);
	          if(bs.isEnd()){
	              	break;
	              }
	          //oos.flush();



	              if(turn == nowTurn){
	            	  //if(ois == null)
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

		      		 ois.close();



	              }else{
	            	  waitThread();

	              }

	              nowTurn = opponentColor(nowTurn);





	              oos.close();

	        }

	        socket.close();





	    } catch (IOException e) {
	      e.printStackTrace();
	    } catch (ClassNotFoundException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		} catch (InterruptedException e) {
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