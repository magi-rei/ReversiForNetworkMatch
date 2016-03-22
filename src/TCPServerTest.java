

//ChatServer.java
//チャットサーバープログラム
//チャットクライアントプログラムからの接続を待つ。
//接続後は1行の文字列読み取りを行い、接続終了する。
//java CharServer 99
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;





class TCPServerTest
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

	    Reversi reversi = new Reversi(true);
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


