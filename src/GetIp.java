import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class GetIp {
	
	public String GetIpAddress(){
		String ip = null;
		try {
			URL whatismyip = new URL("http://checkip.amazonaws.com");

		BufferedReader in = new BufferedReader(new InputStreamReader(
			                whatismyip.openStream()));


		

		ip = in.readLine();
			
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		} //you get the IP as a String
		return ip;
	}
}
