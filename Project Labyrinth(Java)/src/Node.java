import java.awt.Point;
/**
 * Utility Class used for the A* algorithm used to find the shortest path.
 */
	public class Node implements Comparable<Node>{
	private int Fscore=0;
	private int Hscore=0;
	/*** value of the depth level of the A* Algorithmn */
	public 	int Gscore=0;
	/*** the X and Y-axis location of a given point*/
	public Point data=null;
	/*** The parent of the Node we are looking at*/
	public Node parent=null;
	public Node(int x,int y){
		data=new Point(x,y);}
	public Node(Point point){
		data=point;
	}
	/** How the Heuristic Value is calculated for each path.
	 */
	public void updateScore(int TargetX,int TargetY){
			Hscore=Math.abs(TargetX-data.x)+Math.abs(TargetY-data.y);
			Hscore=Hscore/16;
			Fscore=Gscore+Hscore;
		}
	/*** Return whether two Node are equal.Two node are only equal if they have the same data*/
	public boolean equals(Object object){
		if(object!=null && object instanceof Node){
			Node aNode=(Node)object;
			return(aNode.data.x==data.x && aNode.data.y==data.y);
		}
		return false;
	}
	/** we want to add to the path only node that makes the whole path have the lowest
	 * Heuristic Value(shortest path).
	 */
	@Override
	public int compareTo(Node anotherNode) {
		if(Fscore>anotherNode.Fscore)return 1;
		if(Fscore<anotherNode.Fscore)return -1;
		return 0;
	}
}