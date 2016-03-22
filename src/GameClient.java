/**ネットワーク対戦型リバーシ
* 
*/


import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;



class GameClient extends JFrame implements ActionListener, Runnable
{
	
	private static final long serialVersionUID = 1L;
	private static Thread thread;
			//盤面のクラスを用意
			static BoardState bs;
			static ReversiPanel panel = null;
			static int turn = 0;
			static int stone = 4;
			static Hand hand = null;
			static JPanel cardPanel;
			static CardLayout layout;
			static String ip = null;
			static JTextField ipField = null;

	public GameClient(String title) {
		super(title);
		Font font = new Font("", Font.PLAIN, 20);
		JPanel menu = new JPanel();
		menu.setLayout(new BorderLayout());
		JLabel label = new JLabel("あなたはどちら側でプレイしますか？");
		label.setHorizontalAlignment(JLabel.CENTER);
		label.setFont(font);
		JButton sBtn = new JButton("サーバー側（黒）");
		sBtn.addActionListener(this);
		sBtn.setActionCommand("server");
		sBtn.setFont(font);
		JButton cBtn = new JButton("クライアント側（白）");
		cBtn.addActionListener(this);
		cBtn.setActionCommand("client");
		cBtn.setFont(font);
		menu.add(label,BorderLayout.CENTER);
		menu.add(sBtn, BorderLayout.LINE_START);
		menu.add(cBtn,BorderLayout.LINE_END);
		
		SpringLayout sLayout = new SpringLayout();
		JPanel server = new JPanel();
		server.setLayout(sLayout);
		label = new JLabel("以下に表示されるIPアドレスを対戦相手に教えてください");
		label.setFont(font);
		JTextField ipAddress = new JTextField();
		GetIp ip = new GetIp();
		ipAddress.setText(ip.GetIpAddress());
		ipAddress.setEditable(false);
		ipAddress.setFont(font);
		
		JButton ssButton = new JButton("ゲームスタート");
		ssButton.addActionListener(this);
		ssButton.setActionCommand("serverStart");
		ssButton.setFont(font);
		
		sLayout.putConstraint(SpringLayout.NORTH, label, 50, SpringLayout.NORTH, server);
		//sLayout.putConstraint(SpringLayout.WEST, label, 333-label.getWidth()/2, SpringLayout.NORTH, server);
		sLayout.putConstraint(SpringLayout.NORTH, ipAddress, 100, SpringLayout.NORTH, label);
		//sLayout.putConstraint(SpringLayout.WEST, ipAddress, 333-ipAddress.getWidth()/2, SpringLayout.NORTH, server);
		sLayout.putConstraint(SpringLayout.NORTH, ssButton, 100, SpringLayout.NORTH, ipAddress);
		//sLayout.putConstraint(SpringLayout.WEST, ssButton, 333-ssButton.getWidth()/2, SpringLayout.NORTH, server);
		server.add(label);
		server.add(ipAddress);
		server.add(ssButton);
		
		sLayout = new SpringLayout();
		JPanel client = new JPanel();
		client.setLayout(sLayout);
		label = new JLabel("以下に対戦相手のIPアドレスを入力してください");
		label.setFont(font);
		ipField = new JTextField("",12);
		ipField.setFont(font);
		
		JButton ccButton = new JButton("ゲームスタート");
		ccButton.addActionListener(this);
		ccButton.setActionCommand("clientStart");
		ccButton.setFont(font);
		
		sLayout.putConstraint(SpringLayout.NORTH, label, 50, SpringLayout.NORTH, client);
		//sLayout.putConstraint(SpringLayout.WEST, label, 333-label.getWidth()/2, SpringLayout.NORTH, client);
		sLayout.putConstraint(SpringLayout.NORTH, ipField, 100, SpringLayout.NORTH, label);
		//sLayout.putConstraint(SpringLayout.WEST, ipField, 333-ipField.getWidth()/2, SpringLayout.NORTH, client);
		sLayout.putConstraint(SpringLayout.NORTH, ccButton, 100, SpringLayout.NORTH, ipField);
		//sLayout.putConstraint(SpringLayout.WEST, ccButton, 333-ipField.getWidth()/2, SpringLayout.NORTH, client);
		
		client.add(label);
		client.add(ipField);
		client.add(ccButton);
		
		cardPanel = new JPanel();
		layout = new CardLayout();
		cardPanel.setLayout(layout);
		
		cardPanel.add(menu,"menu");
		cardPanel.add(server, "server");
		cardPanel.add(client, "client");
		
		
		getContentPane().add(cardPanel);
	}
	
	public void BoardState(BoardState bs){
		//ボードの盤面を生成するクラス
		panel = new ReversiPanel(bs,turn);
		panel.addMouseListener(panel.new MouseListener());
		cardPanel.add(panel, "bs");
		layout.show(cardPanel, "bs");
	}
	public static void main(String[] args)
	{
		System.setProperty("awt.useSystemAAFontSettings","on");

		System.setProperty("swing.aatext", "true");
		
		GameClient tcpC = new GameClient("Reversi");
		tcpC.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		tcpC.setSize(666, 689);
		tcpC.setVisible(true);
		thread = new Thread(tcpC);


    }
	
	public void run(){
		ObjectOutputStream oos = null;

		ObjectInputStream ois = null;
	

	try {
		Socket socket;
		Object obj;
 	    BoardState newBs;
 	   InetSocketAddress socketAddress;
		while(true){
			if(ip.equals("localhost")){
				socketAddress = new InetSocketAddress(ip, 9999);
			}else{
				socketAddress = new InetSocketAddress(ip, 8001);
			}



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


	    ois = new ObjectInputStream(socket.getInputStream());
	    if(turn ==0){


	    	turn = (Integer)(ois.readObject());
	    	System.out.println("あなたのターンは"+turn);
	    	//サーバーから、BoardStateクラスをもらう
	 	    obj = ois.readObject();
	 	    newBs = (BoardState)(obj);
	 	    stone = newBs.getBlackStone()+newBs.getWhiteStone();
	    }else{
	    	//サーバーから、BoardStateクラスをもらう
	 	    obj = ois.readObject();
	 	    newBs = (BoardState)(obj);
	    }




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
    		this.BoardState(bs);
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
	public void actionPerformed(ActionEvent e){
	    String cmd = e.getActionCommand();

	    if (cmd.equals("server")){
	    	layout.show(cardPanel, "server");
	    	TCPServer gameServer = new TCPServer();
	    	gameServer.start();
	    	ip = "localhost";
	    }else if (cmd.equals("client")){
	    	layout.show(cardPanel, "client");

	    }else if(cmd.equals("serverStart")){
	    	thread.start();
	    }else if(cmd.equals("clientStart")){
	    	if(ip == null){
	    		ip = ipField.getText();
	    	}
	    	if(ipField.getText() !=null)
	    	thread.start();
	    }
	    System.out.println("ボタンが押されました");
	  }
}
