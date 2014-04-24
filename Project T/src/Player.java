import java.io.Serializable;
public class Player implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3302709860989992185L;
	String name;
	int numberof_Diceleft;
	int number_Bet_On;
	int numberOf_Dice_Bet_On;
	public Player(String name,int dice_left,int numberBet,int numberOf_Dice_BetOn){
		this.name=name;
		numberof_Diceleft=dice_left;
		number_Bet_On=numberBet;
		numberOf_Dice_Bet_On=numberOf_Dice_BetOn;
	}
	public String getName(){
		return name;
	}
	public int getDiceLeft(){
		return numberof_Diceleft;
	}
	public int getNumberBet(){
		return number_Bet_On;
	}
	public int getDiceAmount(){
		return numberOf_Dice_Bet_On;
	}
}
