import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;

public class Snake extends JFrame implements KeyListener {
	private SnakePanel[][] pixel = new SnakePanel[40][40];
	private final Color worm = new Color(0,180,0);
	private char direction = 'r';
	private char bufdir = ' ';
	private int length;
	private int xsnake;
	private int ysnake;
	final int sleep = 100;
	boolean key;
	private Thread thread;

	public Snake() {
		this.setSize(400+6,400+28);
		this.setResizable(false);
		this.setLayout(new GridLayout(40,40));
		this.setTitle("Snake");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		addKeyListener(this);
		length = 2;
		xsnake = 20;
		ysnake = 5;
		key = true;

		for(int j = 0;j < 40;j++) {
			for (int i = 0;i < 40;i++) {
				pixel[i][j] = new SnakePanel();
				if(i == 0 || i == 39 || j == 0 || j == 39) {
					pixel[i][j].setBackground(Color.orange);
				}else {
					pixel[i][j].setBackground(Color.black);
				}
				this.add(pixel[i][j]);
			}
		}
		startWorm();
		this.setVisible(true);
		startMovement();
		makeApple();
	}
	private void startWorm() {
		pixel[19][5].setBackground(worm);
		pixel[19][5].setLength(1);
		pixel[20][5].setBackground(worm);
		pixel[20][5].setLength(2);
	}
	public void startMovement() {
		thread = new Thread(new Running());
		thread.setDaemon(true);
		thread.start();
	}
	@Override
	public void keyPressed(KeyEvent ke) {
		if (key) {
			switch (ke.getKeyCode()) {
			case KeyEvent.VK_DOWN:
				if(direction != 'u') { direction = 'd';	}
				break;
			case KeyEvent.VK_UP:
				if(direction != 'd') { direction = 'u';	}
				break;
			case KeyEvent.VK_LEFT:
				if(direction != 'r') { direction = 'l';	}
				break;
			case KeyEvent.VK_RIGHT:
				if(direction != 'l') { direction = 'r';	}
				break;
			}
			key = false;
		} else {
			switch (ke.getKeyCode()) {
			case KeyEvent.VK_DOWN:
				if(direction != 'u') { bufdir = 'd';	}
				break;
			case KeyEvent.VK_UP:
				if(direction != 'd') { bufdir = 'u';	}
				break;
			case KeyEvent.VK_LEFT:
				if(direction != 'r') { bufdir = 'l';	}
				break;
			case KeyEvent.VK_RIGHT:
				if(direction != 'l') { bufdir = 'r';	}
				break;
			}
		}
	}
	@Override
	public void keyReleased(KeyEvent e) {

	}
	@Override
	public void keyTyped(KeyEvent e) {

	}
	public void moveSnake() {
		if (direction == 'u'){
			ysnake--;
		}else if (direction == 'd') {
			ysnake++;
		}else if (direction == 'r') {
			xsnake++;
		}else if (direction == 'l') {
			xsnake--;
		}
		if (pixel[xsnake][ysnake].getLength() > 1 || xsnake == 0 || xsnake == 39 || ysnake == 0 || ysnake == 39) {
			collision();
		}else if (pixel[xsnake][ysnake].isApple()) {
			length++;
			pixel[xsnake][ysnake].setApple(false);
			makeApple();
		}
		pixel[xsnake][ysnake].setLength(length + 1);
	}
	public void collision() {
		thread.stop();
	}
	public void iterateSnake() {
		for (int j = 1;j < 39;j++) {
			for (int i = 1;i < 39;i++) {
				pixel[i][j].iterate();
				if(pixel[i][j].getLength() > 0) {
					pixel[i][j].setBackground(worm);
				}else if (pixel[i][j].isApple()) {
					pixel[i][j].setBackground(Color.red);
				}else {
					pixel[i][j].setBackground(Color.black);
				}
			}
		}
	}
	public void makeApple() {
		int x = (int) Math.round(Math.random() * 38) + 1;
		int y = (int) Math.round(Math.random() * 38) + 1;
		if (pixel[x][y].getLength() > 0) {
			makeApple();
		}else {
			pixel[x][y].setApple(true);
		}
	}

	public class Running implements Runnable {

		@Override
		public void run() {
			while(true){
				try {
					Thread.sleep(sleep);
					key = true;
					moveSnake();
					if(bufdir != ' ') {
						direction = bufdir;
						bufdir = ' ';
					}
					iterateSnake();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

	}
}
