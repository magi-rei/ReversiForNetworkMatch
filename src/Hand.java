import java.io.Serializable;




public class Hand implements Serializable{
	int i;
	int j;
	int turn;

	Hand(int i,int j,int turn){
		this.i=i;
		this.j=j;
		this.turn = turn;

	}


	Hand returnHand(){
		return this;

	}

	int getI(){
		return i;
	}

	int getJ(){
		return j;
	}

	public String toString(){
		return i+","+j+","+turn;
	}

}
