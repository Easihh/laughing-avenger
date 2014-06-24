import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;

public class Input implements KeyListener {
	@Override
	public void keyPressed(KeyEvent e) {
		int keycode=e.getKeyCode();
		if(keycode==KeyEvent.VK_A){
			if(Labyrinth.GameState==Game.GameState.NotStarted)
				Labyrinth.GameState=Game.GameState.Normal;
			if(!Character.checkIfpressed(Game.button.A)){
				Character.keypressed.add(Game.button.A);
			}
		}
		if(keycode==KeyEvent.VK_D){
			if(Labyrinth.GameState==Game.GameState.NotStarted)
				Labyrinth.GameState=Game.GameState.Normal;
				if(!Character.checkIfpressed(Game.button.D)){
					Character.keypressed.add(Game.button.D);
				}
		}
		if(keycode==KeyEvent.VK_W){
			if(Labyrinth.GameState==Game.GameState.NotStarted)
				Labyrinth.GameState=Game.GameState.Normal;
				if(!Character.checkIfpressed(Game.button.W)){
					Character.keypressed.add(Game.button.W);
				}
		}
		if(keycode==KeyEvent.VK_S){
			if(Labyrinth.GameState==Game.GameState.NotStarted)
				Labyrinth.GameState=Game.GameState.Normal;
				if(!Character.checkIfpressed(Game.button.S)){
					Character.keypressed.add(Game.button.S);
				}
		}
		if(keycode==KeyEvent.VK_SPACE){
			boolean check=false;
			if(Character.powerActivated_ladder || Character.powerActivated_hammer || Character.powerActivated_arrow)
				check=Character.checkPower();
			if(!check)
				Character.fireProjectile();
		}
		if(keycode==KeyEvent.VK_Q){
			Labyrinth.GameState=Game.GameState.Death;
			Sound.StageMusic.stop();
			Sound.Death.start();
		}
		if(keycode==KeyEvent.VK_ESCAPE){
			shortestPath();
		}
	}
	@Override
	public void keyReleased(KeyEvent e) {
		int keycode=e.getKeyCode();
		Character.releaseButton(keycode);
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}
	private void shortestPath(){
		Node goal=new Node(Character.x,Character.y);
		ArrayList<Node> Open=new ArrayList<Node>();
		ArrayList<Node> Closed=new ArrayList<Node>();
		
		Stack<Node> Path = new Stack<Node>();
		//path_exist=false;
		Node root=new Node(192,192);
		Node neighbor;
		Open.add(root);	
		while(!Open.isEmpty()){
			Collections.sort(Open);
			Node current=Open.get(0);
			System.out.println("CurrentX:"+current.data.x);
			System.out.println("CurrentY:"+current.data.y);
			if(Closed.contains(goal)){
				System.out.println("FOUND");
				//path_exist=true;
				break;
			}
			Open.remove(current);
			Closed.add(current);
			if(!checkCollison(new Rectangle(current.data.x-16,current.data.y,16,16),
					new Rectangle(current.data.x-16,current.data.y+16,16,16))){//left
				neighbor=new Node(current.data.x-16,current.data.y);
				if(!Closed.contains(neighbor)){
					if(!Open.contains(neighbor)){
						Open.add(neighbor);
						neighbor.parent=current;
						neighbor.Gscore=current.Gscore+1;
						neighbor.updateScore(192, 192);
					}
					if(Open.contains(neighbor) && (current.Gscore+1<neighbor.Gscore)){
						neighbor.parent=current;
						neighbor.Gscore=current.Gscore+1;
						neighbor.updateScore(192, 192);
					}
				}
			}
			if(!checkCollison(new Rectangle(current.data.x,current.data.y+32,16,16),
					new Rectangle(current.data.x+16,current.data.y+32,16,16))){//down
				neighbor=new Node(current.data.x,current.data.y+16);
					if(!Closed.contains(neighbor)){
						if(!Open.contains(neighbor)){
							Open.add(neighbor);
							neighbor.parent=current;
							neighbor.Gscore=current.Gscore+1;
							neighbor.updateScore(192, 192);
						}
						if(Open.contains(neighbor) && (current.Gscore+1<neighbor.Gscore)){
							neighbor.parent=current;
							neighbor.Gscore=current.Gscore+1;
							neighbor.updateScore(192, 192);
						}
					}
				}
			}
			Node test=Closed.get(Closed.size()-1);
			while(test!=null){
				Path.add(test);
				test=test.parent;
			}		
			for(Node aNode:Path){
				System.out.println("NodeX:"+aNode.data.x);
				System.out.println("NodeY:"+aNode.data.y);
			}
		}
	public boolean checkCollison(Rectangle mask1,Rectangle mask2) {
		for(int i=0;i<Level.map_tile.size();i++){
			//infront of a full tile.
			if(Level.map_tile.get(i).shape.intersects(mask1)&& Level.map_tile.get(i).shape.intersects(mask2)){
				System.out.println("Type:"+Level.map_tile.get(i).getType());
				if(Level.map_tile.get(i).isSolid)return true;
			}
			if(Level.map_tile.get(i).shape.intersects(mask1)|| Level.map_tile.get(i).shape.intersects(mask2)){
				if(Level.map_tile.get(i).isSolid)return true;
			}
		}
		return false;
	}
}


