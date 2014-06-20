import java.awt.Point;
	public class Node implements Comparable<Node>{
	Point data=null;
	boolean visited=false;
	int Gscore=0;
	int Fscore=0;
	int Hscore=0;
	Node parent=null;
	public Node(int x,int y){
		data=new Point(x,y);
		}
	public void updateScore(int TargetX,int TargetY){
			Hscore=Math.abs(TargetX-data.x)+Math.abs(TargetY-data.y);
			Hscore=Hscore/16;
			Fscore=Gscore+Hscore;
		}
	public boolean equals(Object object){
		if(object!=null && object instanceof Node){
			Node aNode=(Node)object;
			return(aNode.data.x==data.x && aNode.data.y==data.y);
		}
		return false;
	}
	@Override
	public int compareTo(Node anotherNode) {
		if(Fscore>anotherNode.Fscore)return 1;
		if(Fscore<anotherNode.Fscore)return -1;
		return 0;
	}
}