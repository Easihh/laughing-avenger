import java.io.Serializable;


public class Bid implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7521242275870416285L;
	public int amount;
	public int number;
	public Bid(int number,int amount){
		this.amount=amount;
		this.number=number;
	}
}
