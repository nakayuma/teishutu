import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;
public class Shooting1 {
	public static void main(String[] args) {
    	ShootingFrame f = new ShootingFrame("Shooting1");
        f.setBackground(Color.black);
        f.setForeground(Color.white);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);
	}
}
class ShootingFrame extends JFrame implements KeyListener,Runnable{
    Image myship,bullet;
    int scene=0,score=0,hiscore=0;
    static int en = 4;
    Image enemy[] = new Image[en];
    int mx, my, bx, by,level=1,a=1;
    int ex[] = new int[en], ey[] = new int[en];
    int dex[]=new int[en];
    // ウィンドウのサイズ(xsize,ysize), 自機の大きさ(mssize,60x60ピクセル)
    static int xsize = 300, ysize = 400, mssize=60,bsize=8,esize=60;
    Thread thread;
    Image offImage;
    boolean keyLeft, keyRight,keySpace,keyUp,keyDown;// キーが押されているかどうかのフラグ
    boolean bflag;
	public ShootingFrame(String title){
        super(title);
        Toolkit tk = Toolkit.getDefaultToolkit();
        bullet = tk.getImage("bullet.gif");
        keyLeft = keyRight = keySpace = false;
        
        myship = tk.getImage("myship.gif");
        keyLeft = keyRight = false;
        mx = (xsize-mssize) / 2; // 自機の横方向の初期位置
        my = ysize - mssize -10; // 自機の縦方向の初期位置
        for (int i=0; i<en; i++) {
            enemy[0] = tk.getImage("enemy.gif");
            enemy[1] =tk.getImage("a.gif");
            enemy[2] =tk.getImage("b.gif");
            enemy[3] =tk.getImage("c.gif");
            ex[i] = (int)(Math.random() * (xsize - esize));
            ey[i] = -esize - (int)(Math.random() * ysize);
            dex[i]=5;
        }
        setSize(xsize, ysize);
        addKeyListener(this);
        requestFocusInWindow();
        thread = new Thread(this);
        thread.start();
    }
	

	void gameOver() {
		Graphics gv = offImage.getGraphics();
		if (keySpace) {
		    keyLeft = keyRight = keyUp = keyDown = keySpace = false;
		    bflag = false;
		    mx = (xsize-mssize) / 2;
		    my = ysize - mssize -10;
		    for (int i=0; i<en; i++) {
		        ex[i] = (int)(Math.random() * (xsize - esize));
		        ey[i] = -esize - (int)(Math.random() * ysize);
		    }
		    scene = 0;
		    score = 0;
		    level = 1;
		    a=1;
		}
		if (score > hiscore) {
		    hiscore = score;
		}
		
		// 裏画面の消去
		gv.clearRect(0, 0, xsize, ysize);
		// ゲームオーバー文字列描画
		gv.setFont(new Font("Serif", Font.BOLD, 28)); gv.drawString("GAME OVER", 64, 180);
		gv.setFont(new Font("Serif", Font.PLAIN, 20)); gv.drawString("SCORE : " + String.valueOf(score), 90, 350); repaint();
		gv.drawString("HISCORE : " + String.valueOf(hiscore), 70, 370);
		if(score>5000){
			gv.drawString("Congratulation!!  " , 70, 250);
		}
	    }
	
	void gameStart() {
		Graphics gv = offImage.getGraphics();
		// 裏画面の消去
		gv.clearRect(0, 0, xsize, ysize);
		// スタート画面文字列描画
		gv.setFont(new Font("Serif", Font.BOLD, 28)); gv.drawString("Shooting Game", 40, 180); gv.setFont(new Font("Serif", Font.PLAIN, 20)); gv.drawString("Hit Space key", 90, 350);
		// 再描画
		repaint();
		// スペースキーが押された
		if (keySpace) {
		         keyLeft = keyRight = keyUp = keyDown = keySpace = false;
		scene = 1; }
		}
	
    public void paint(Graphics g){
    	// offImageが作られていなかった場合には新しく作る
        if (offImage==null) offImage = createImage(xsize, ysize);
    	g.drawImage(offImage, 0, 0, this);
    }
	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
			// 左キーが押された
			case KeyEvent.VK_LEFT: keyLeft = true; break;
			// 右キーが押された
			case KeyEvent.VK_RIGHT: keyRight = true; break;
			case KeyEvent.VK_SPACE: keySpace = true; break;
			case KeyEvent.VK_UP: keyUp = true; break;
			case KeyEvent.VK_DOWN: keyDown = true; break;
		}
	}
	public void keyReleased(KeyEvent e) {
		switch (e.getKeyCode()) {
			// 左キーが離された
			case KeyEvent.VK_LEFT: keyLeft = false; break;
			// 右キーが離された
			case KeyEvent.VK_RIGHT: keyRight = false; break;
		    case KeyEvent.VK_SPACE: keySpace = false; break;
		    case KeyEvent.VK_UP: keyUp = false; break;
		    case KeyEvent.VK_DOWN: keyDown = false; break;
		}
	}
	// キーがタイプされたときの処理(なにもしない)
	public void keyTyped(KeyEvent e) {}
	
    public void run() {
        Thread thisThread = Thread.currentThread();
        while (thread == thisThread) {
        	// offImageが作られていたら＝描画準備が完了していればメインの処理
            if (offImage!=null)
        	switch(scene){
        	case 0:gameStart();break;
        	case 1:gameMain();break;
        	case 2:gameOver();
        	}
            
            try {
                Thread.sleep(20);
            }
            catch(InterruptedException e) {
                break;
            }
        }
}

	void gameMain() {
		// 左移動
		if (keyLeft) {
			mx -= 8;
			if (mx < 0) mx = 0;  // 左端を超えないように
		}
		// 右移動
		if (keyRight) {
			mx += 8;
			if (mx > xsize - mssize) mx = xsize - mssize; // 右端を超えないように
		}
		Graphics gv = offImage.getGraphics();
		// 裏画面の消去
		gv.clearRect(0, 0, xsize, ysize);
		// 自機の描画
		gv.drawImage(myship, mx, my, this);
		
		 // 弾の発射
		if (keySpace) {
		    if (!bflag) {
		       bx = mx + (mssize-bsize)/2;
		       by = my;
		       bflag = true;
		} }
		if (keyUp) {
		    my -= 8;
		    if (my < 0) my = 0;
		}
		if (keyDown) {
		    my += 8;
		    if (my > 350) my = 350;
		}
		// 弾の移動 if (bflag) {
		if (bflag) {
		    if (by < 0) bflag = false;
		else by -= 8;
		    }
		if(bflag) gv.drawImage(bullet, bx, by, this);
		
		for(int i=0;i<level;i++){
			ey[i] += 8;
			if(score>=1000){
				ex[i] +=dex[i];
			}
			if (ey[i] >= ysize) {
			    ex[i] = (int)(Math.random() * (xsize - esize));
			ey[i] = -esize;
			}
 			if(ex[i]<0 || ex[i]>xsize-esize){
				dex[i]=-dex[i];
				ex[i] +=2*dex[i];
			}
 			gv.drawImage(enemy[i], ex[i], ey[i], this);
		// 弾と敵との当たり判定
		if (bflag) {
				if (bx<ex[i]+esize && bx+bsize>ex[i] && by<ey[i]+esize && by+bsize>ey[i]) { 
					score += 100;
					if(score>=1100){
						level=3;
						if(score%500==0){
							level=4;
						}
						score+=200;
					}else if(score>=500) {
						level=2;
						if(score%500==0){
							level=4;
						}
					}
					ex[i] = (int)(Math.random() * (xsize - esize));
				    ey[i] = -esize;
		            bflag = false;
		    }
		}
		// 自機と敵との当たり判定 
		if (mx<ex[i]+esize && mx+mssize>ex[i] && my<ey[i]+esize && my+mssize>ey[i] && i==3 ) {
			a +=1;
		}else if (mx<ex[i]+esize && mx+mssize>ex[i] && my<ey[i]+esize && my+mssize>ey[i]) {
			a -=1;
		}
		
		if(a==0){
			scene=2;
		}
		
	    gv.setFont(new Font("Serif", Font.PLAIN, 16));
	    gv.drawString("SCORE : " + String.valueOf(score), 10, 40);
	    gv.drawString("LIFE : " + String.valueOf(a), 10, 60);
	    repaint();
		}
 }
}
