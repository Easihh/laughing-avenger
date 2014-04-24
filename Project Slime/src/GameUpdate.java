
public class GameUpdate extends Thread{
	@Override
	public void run() {
		long nano=1000000000L;
		int fps_wanted=60;
		long refresh_delay=nano/fps_wanted;
		long timer=0;
		long prev=System.nanoTime();
		int fps=0;
		while(true){
			fps++;
			if((System.nanoTime()-prev)<refresh_delay){
				long sleep=refresh_delay-(System.nanoTime()-prev);
				sleep/=1000000;
				try {
					Thread.sleep(sleep);
					//checkMonsterCollision();
					//checkCollision();
					//hero.gravity();
					//repaint();
					prev=System.nanoTime();
				}catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			if((System.nanoTime()-timer)/nano>=1){	
				//platform.repaint();
				
				//setTitle("Project:Slime FPS:"+fps);
				fps=0;
				timer=System.nanoTime();
			}
		}
	}

}
