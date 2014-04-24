import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JComboBox;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;


public class Game_view extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6110785576745014779L;
	static ArrayList<Integer> dice;
	private ArrayList<Image> diceimg=new ArrayList<Image>();
	private JButton btnTrue;
	private JLabel lblCurrentTurn;
	private JButton btnDiceRoll; 
	private JButton btnBid;
	private JComboBox<String> comboBox;
	private JSpinner jspin;
	private JLabel amount;
	private JLabel number;
	static Bid bid=new Bid(0,0);
	private JLabel[] playerlbl=new JLabel[4];
	private JLabel[] numberlbl=new JLabel[4];
	private JLabel[] amountlbl=new JLabel[4];
	private JLabel[] dice_Amount=new JLabel[4];
	private JButton btnLiar;
	public Game_view(){
		setLayout(null);
		dice_Amount[0]=new JLabel("Dice left:"+ClientGUI.numberof_Dice);
		dice_Amount[0].setBounds(200,325,80,20);
		add(dice_Amount[0]);
		numberlbl[0] = new JLabel(" #:"+ClientGUI.number);
		numberlbl[0].setBounds(200, 275, 80, 20);
		add(numberlbl[0]);
		
		amountlbl[0] = new JLabel("how many:"+ClientGUI.numberof_Dice);
		amountlbl[0].setBounds(200, 300, 100, 20);
		add(amountlbl[0]);
		
		btnTrue = new JButton("True");
		btnTrue.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent ev) {
				try {
					Client.out.writeObject(new ChatMessage(ClientGUI.username,16,"Calling Truth"));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		btnTrue.setEnabled(false);
		btnTrue.setBounds(290, 310, 100, 25);
		add(btnTrue);
		
		playerlbl[0] = new JLabel(ClientGUI.username);
		playerlbl[0].setBounds(200, 250, 80, 20);
		add(playerlbl[0]);
		
		btnDiceRoll = new JButton("Roll Dice");
		btnDiceRoll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					btnDiceRoll.setEnabled(false);
					Client.out.writeObject(new ChatMessage(ClientGUI.username,7,"Roll Dice"));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		btnDiceRoll.setBounds(290, 260, 100, 25);
		add(btnDiceRoll);
		
		lblCurrentTurn = new JLabel("Current Player Turn:");
		lblCurrentTurn.setBounds(148, 126, 175, 50);
		add(lblCurrentTurn);
		
		setupTurn();
		setupPlayer();
		loadImage();
	}
	public void setupPlayer(){
		if(ClientGUI.player_list.size()>1 && ClientGUI.player_list.get(1).name!=ClientGUI.username){
			dice_Amount[1]=new JLabel("Dice left:"+ClientGUI.numberof_Dice);
			dice_Amount[1].setBounds(30,225,80,20);
			add(dice_Amount[1]);
			playerlbl[1] = new JLabel(ClientGUI.player_list.get(1).name);
			playerlbl[1].setBounds(30, 150, 80, 20);
			add(playerlbl[1]);
			numberlbl[1] = new JLabel(" #:"+ClientGUI.player_list.get(1).number_Bet_On);
			numberlbl[1].setBounds(30, 175, 100, 20);
			add(numberlbl[1]);
			amountlbl[1] = new JLabel("how many:"+ClientGUI.player_list.get(1).numberOf_Dice_Bet_On);
			amountlbl[1].setBounds(30, 200, 100, 20);
			add(amountlbl[1]);
			if(ClientGUI.player_list.size()>2 && ClientGUI.player_list.get(2).name!=ClientGUI.username){
				dice_Amount[2]=new JLabel("Dice left:"+ClientGUI.numberof_Dice);
				dice_Amount[2].setBounds(200,100,80,20);
				add(dice_Amount[2]);
				playerlbl[2] = new JLabel(ClientGUI.player_list.get(2).name);
				playerlbl[2].setBounds(200, 25, 80, 20);
				add(playerlbl[2]);
				numberlbl[2] = new JLabel(" #:"+ClientGUI.player_list.get(2).number_Bet_On);
				numberlbl[2].setBounds(200, 50, 100, 20);
				add(numberlbl[2]);
				amountlbl[2] = new JLabel("how many:"+ClientGUI.player_list.get(2).numberOf_Dice_Bet_On);
				amountlbl[2].setBounds(200, 75, 100, 20);
				add(amountlbl[2]);
				if(ClientGUI.player_list.size()>3 && ClientGUI.player_list.get(3).name!=ClientGUI.username){
					dice_Amount[3]=new JLabel("Dice left:"+ClientGUI.numberof_Dice);
					dice_Amount[3].setBounds(375,225,80,20);
					add(dice_Amount[3]);
					playerlbl[3] = new JLabel(ClientGUI.player_list.get(3).name);
					playerlbl[3].setBounds(375, 150, 80, 20);
					add(playerlbl[3]);
					numberlbl[3] = new JLabel(" #:"+ClientGUI.player_list.get(3).number_Bet_On);
					numberlbl[3].setBounds(375, 175, 100, 20);
					add(numberlbl[3]);
					amountlbl[3] = new JLabel("how many:"+ClientGUI.player_list.get(3).numberOf_Dice_Bet_On);
					amountlbl[3].setBounds(375, 200, 100, 20);
					add(amountlbl[3]);
				}
			}
		}
	}
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		int index=0;
		Queue<Player> player=new LinkedList<Player>();
		for(Player aPlayer:ClientGUI.player_list){
			if(aPlayer.name.equalsIgnoreCase(ClientGUI.username))
				index=ClientGUI.player_list.lastIndexOf(aPlayer);
			player.add(aPlayer);
		}
		for(int i=0;i<index;i++){
			Player temp=player.remove();
			player.add(temp);
		}
		for(int i=0;i<ClientGUI.player_list.size();i++){
			Player temp=player.remove();
			playerlbl[i].setText(temp.name);
			amountlbl[i].setText("how many:"+temp.numberOf_Dice_Bet_On);
			numberlbl[i].setText(" #:"+temp.number_Bet_On);
			dice_Amount[i].setText("Dice Left:"+temp.numberof_Diceleft);
		}	
		drawDice(g);
	}
	public void drawDice(Graphics g){
		int x=175;
		int y=350;
		if(dice!=null){
			for(Integer number:dice){
				g.drawImage(diceimg.get(number-1), x, y, this);
				x+=50;			
			}
		}
	}
	public void loadImage(){
		BufferedImage img=null;
		try {
			img = ImageIO.read(getClass().getResource("image/Dice_1.png"));
			diceimg.add(img);
			img = ImageIO.read(getClass().getResource("image/Dice_2.png"));
			diceimg.add(img);
			img = ImageIO.read(getClass().getResource("image/Dice_3.png"));
			diceimg.add(img);
			img = ImageIO.read(getClass().getResource("image/Dice_4.png"));
			diceimg.add(img);
			img = ImageIO.read(getClass().getResource("image/Dice_5.png"));
			diceimg.add(img);
			img = ImageIO.read(getClass().getResource("image/Dice_6.png"));
			diceimg.add(img);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void checkTurn(String who){
		if(ClientGUI.username.equalsIgnoreCase(who)){
			if(!btnDiceRoll.isEnabled()){
				comboBox.setVisible(true);
				jspin.setVisible(true);
				amount.setVisible(true);
				number.setVisible(true);
				btnBid.setVisible(true);
				btnLiar.setEnabled(true);
				btnTrue.setEnabled(true);
			}
			else{comboBox.setVisible(false);
				jspin.setVisible(false);
				amount.setVisible(false);
				number.setVisible(false);
				btnBid.setVisible(false);
			}
			for(Player playerx:ClientGUI.player_list){
				if(playerx.name.equalsIgnoreCase(who))
					if(playerx.numberof_Diceleft==0){
						endTurn(ClientGUI.username);
						btnDiceRoll.setEnabled(false);
					}
			}
		}
		else{
			comboBox.setVisible(false);
			jspin.setVisible(false);
			amount.setVisible(false);
			number.setVisible(false);
			btnBid.setVisible(false);
		}
		lblCurrentTurn.setText("Current Player Turn:"+who);
		repaint();
	}
	public void setupTurn(){
		String[] list={"1","2","3","4","5","6"};
		comboBox=new JComboBox<String>(list);
		comboBox.setSelectedIndex(0);
		comboBox.setBounds(100,300,50,20);
		comboBox.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		       @SuppressWarnings("unchecked")
			JComboBox<String> cb = (JComboBox<String>)e.getSource();
		       int number = (int)cb.getSelectedIndex();
		       cb.setSelectedIndex(number);
		    } 
		});
		add(comboBox);
		SpinnerNumberModel model=new SpinnerNumberModel(1,1,99,1);
		jspin=new JSpinner(model);
		jspin.setBounds(100, 325, 50, 20);
		amount=new JLabel("Amount:");
		amount.setBounds(50,325,50,20);
		add(amount);
		add(jspin);
		number=new JLabel("Number:");
		number.setBounds(50,300, 50, 20);
		btnBid=new JButton("Bid");
		btnBid.setBounds(50, 350, 100, 20);
		btnBid.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent ev){
				int number=Integer.parseInt((String)comboBox.getSelectedItem());
				int amount=(Integer)jspin.getValue();
				try {
					Client.out.writeObject(new ChatMessage(ClientGUI.username,10,new Bid(number,amount)));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		add(number);
		add(btnBid);	
		
		btnLiar = new JButton("Liar");
		btnLiar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					Client.out.writeObject(new ChatMessage(ClientGUI.username,14,"Calling Liar"));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		btnLiar.setEnabled(false);
		btnLiar.setBounds(290, 285, 100, 25);
		add(btnLiar);
	}
	public void updateLabel(String from){
		for(Player Player:ClientGUI.player_list){
			if (Player.name.equalsIgnoreCase(from)){
				Player.number_Bet_On=bid.number;
				Player.numberOf_Dice_Bet_On=bid.amount;
			}
		}
		repaint();
	}
	public void endTurn(String playername){
		if(playername.equalsIgnoreCase(ClientGUI.username)){
			comboBox.setVisible(false);
			jspin.setVisible(false);
			amount.setVisible(false);
			number.setVisible(false);
			btnBid.setVisible(false);
			btnLiar.setEnabled(false);
			btnTrue.setEnabled(false);
			try {
				Client.out.writeObject(new ChatMessage(ClientGUI.username,12,"turn has ended"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	public void isLiar(String theliar,String message) {
		if(message.equalsIgnoreCase("true")){
			Chat.textArea.setText(Chat.textArea.getText()+"\n"+theliar+" is lying");
		}
		else Chat.textArea.setText(Chat.textArea.getText()+"\n"+theliar+" is telling the truth");
		repaint();
	}

	public void resetPlay() {
		dice=null;
		btnDiceRoll.setEnabled(true);
		for(Player playerx:ClientGUI.player_list){
			if(playerx.name.equalsIgnoreCase(ClientGUI.username))
				if(playerx.numberof_Diceleft==0)
					btnDiceRoll.setEnabled(false);
			}
	}
	public void endGame(String name) {
			btnBid.setEnabled(false);
			btnDiceRoll.setEnabled(false);
			btnLiar.setEnabled(false);
			btnTrue.setEnabled(false);
		repaint();
	}
}
