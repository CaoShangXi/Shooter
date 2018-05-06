package com.tarena.shoot;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class ShootGame extends JPanel {
	public static final int WIDTH = 400; // ����
	public static final int HEIGHT = 654; // ����
	/** ��Ϸ�ĵ�ǰ״̬: START RUNNING PAUSE GAME_OVER */
	private int state;
	public static final int START = 0;
	public static final int RUNNING = 1;
	public static final int PAUSE = 2;
	public static final int GAME_OVER = 3;


	private int score = 0; // �÷�
	private Timer timer; // ��ʱ��
	private int intervel = 1000/100; // ʱ����(����)
	
	public static BufferedImage background;
	public static BufferedImage start;
	public static BufferedImage pause;
	public static BufferedImage gameover;
	public static BufferedImage bullet;
	public static BufferedImage airplane;
	public static BufferedImage[] airplaneEmber=new BufferedImage[4];
	public static BufferedImage bee;
	public static BufferedImage[] beeEmber=new BufferedImage[4];;
	public static BufferedImage hero0;
	public static BufferedImage hero1;
	public static BufferedImage[] heroEmber=new BufferedImage[4];;
	public static BufferedImage bigPlane;
	public static BufferedImage[] bigPlaneEmber=new BufferedImage[4];;

	private FlyingObject[] flyings = {}; // �л�����
	private Bullet[] bullets = {}; // �ӵ�����
	private Hero hero = new Hero(); // Ӣ�ۻ�
	private Ember[] embers = {}; // �ҽ�

	static {// ��̬�����
		try {
			background = ImageIO.read(ShootGame.class
					.getResource("background.png"));
			bigPlane = ImageIO
					.read(ShootGame.class.getResource("bigplane.png"));
			airplane = ImageIO
					.read(ShootGame.class.getResource("airplane.png"));
			bee = ImageIO.read(ShootGame.class.getResource("bee.png"));
			bullet = ImageIO.read(ShootGame.class.getResource("bullet.png"));
			hero0 = ImageIO.read(ShootGame.class.getResource("hero0.png"));
			hero1 = ImageIO.read(ShootGame.class.getResource("hero1.png"));
			pause = ImageIO.read(ShootGame.class.getResource("pause.png"));
			gameover = ImageIO
					.read(ShootGame.class.getResource("gameover.png"));
			start = ImageIO
					.read(ShootGame.class.getResource("start.png"));
			for(int i=0; i<4; i++){
				beeEmber[i] = ImageIO.read(
						ShootGame.class.getResource("bee_ember"+i+".png"));
				airplaneEmber[i] = ImageIO.read(
						ShootGame.class.getResource("airplane_ember"+i+".png"));
				bigPlaneEmber[i] = ImageIO.read(
						ShootGame.class.getResource("bigplane_ember"+i+".png"));
				heroEmber[i] = ImageIO.read(
						ShootGame.class.getResource("hero_ember"+i+".png"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void paint(Graphics g) {
		g.drawImage(background, 0, 0, null); // ������ͼ
		paintEmber(g);
		paintHero(g); // ��Ӣ�ۻ�
		paintBullets(g); // ���ӵ�
		paintFlyingObjects(g); // ��������
		paintScore(g); // ������
		paintState(g); // ����Ϸ״̬
	}

	/** ��Ӣ�ۻ� */
	public void paintHero(Graphics g) {
		g.drawImage(hero.getImage(), hero.getX(), hero.getY(), null);
	}
	
	public void paintEmber(Graphics g) {
		for (int i = 0; i < embers.length; i++) {
			Ember e = embers[i];
			g.drawImage(e.getImage(), e.getX(), e.getY(), null);
		}
	}

	/** ���ӵ� */
	public void paintBullets(Graphics g) {
		for (int i = 0; i < bullets.length; i++) {
			Bullet b = bullets[i];
			if(! b.isBomb())
				g.drawImage(b.getImage(), b.getX() - b.getWidth() / 2, b.getY(),
					null);
		}
	}

	/** �������� */
	public void paintFlyingObjects(Graphics g) {
		for (int i = 0; i < flyings.length; i++) {
			FlyingObject f = flyings[i];
			g.drawImage(f.getImage(), f.getX(), f.getY(), null);
		}
	}
Object a = new Object();
	/** ������ */
	public void paintScore(Graphics g) {
		int x = 10;
		int y = 25;
		Font font = new Font(Font.SANS_SERIF,Font.BOLD, 14);
		g.setColor(new Color(0x3A3B3B));
		g.setFont(font); // ��������
		g.drawString("SCORE:" + score, x, y); // ������
		y+=20;
		g.drawString("LIFE:" + hero.getLife(), x, y);
	}
	/** ����Ϸ״̬ */
	public void paintState(Graphics g) {
		switch (state) {
		case START:
			g.drawImage(start, 0, 0, null);
			break;
		case PAUSE:
			g.drawImage(pause, 0, 0, null);
			break;
		case GAME_OVER:
			g.drawImage(gameover, 0, 0, null);
			break;
		}
	}

	public static void main(String[] args) {
		JFrame frame = new JFrame("Shoot Game");
		ShootGame game = new ShootGame(); // ������
		frame.add(game); // �������ӵ�JFrame��
		frame.setSize(WIDTH, HEIGHT); // ��С
		frame.setAlwaysOnTop(true); // ����������
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Ĭ�Ϲرղ���
		frame.setIconImage(new ImageIcon("images/icon.jpg").getImage()); // ���ô����ͼ��
		frame.setLocationRelativeTo(null); // ���ô����ʼλ��
		frame.setVisible(true); // �������paint

		game.action(); // ����ִ��
	}

	public void action() { // ����ִ�д���
		// �������¼�
		MouseAdapter l = new MouseAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) { // ����ƶ�
				if (state == RUNNING) { // ����ʱ�ƶ�Ӣ�ۻ�
					int x = e.getX();
					int y = e.getY();
					hero.moveTo(x, y);
				}
			}

			@Override
			public void mouseEntered(MouseEvent e) { // ������
				if (state == PAUSE) { // ��ͣʱ����
					state = RUNNING;
				}
			}

			@Override
			public void mouseExited(MouseEvent e) { // ����˳�
				if (state != GAME_OVER) {
					state = PAUSE; // ��Ϸδ��������������Ϊ��ͣ
				}
			}

			@Override
			public void mouseClicked(MouseEvent e) { // �����
				switch (state) {
				case START:
					state = RUNNING;
					break;
				case GAME_OVER: // ��Ϸ�����������ֳ�
					flyings = new FlyingObject[0];
					bullets = new Bullet[0];
					hero = new Hero();
					score = 0;
					state = START;
					break;
				}
			}
		};
		this.addMouseListener(l); // �������������
		this.addMouseMotionListener(l); // ������껬������

		timer = new Timer(); // �����̿���
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				if (state == RUNNING) {
					enterAction(); // �������볡
					stepAction(); // ��һ��
					shootAction(); // ���
					bangAction(); // �ӵ��������
					outOfBoundsAction(); // ɾ��Խ������Ｐ�ӵ�
					checkGameOverAction(); // �����Ϸ����
					emberAction();
				}
				repaint(); // �ػ棬����paint()����
			}


		}, intervel, intervel);
	}

	private void emberAction() {
		Ember[] live = new Ember[embers.length];
		int index = 0;
		for (int i = 0; i < embers.length; i++) {
			Ember ember = embers[i];
			if(! ember.burnDown()){
				live[index++]=ember;
			}
		}
		embers = Arrays.copyOf(live, index);
	}
	int flyEnteredIndex = 0; // �������볡����

	/** �������볡 */
	public void enterAction() {
		flyEnteredIndex++;
		if (flyEnteredIndex % 40 == 0) { // 300����--10*30
			FlyingObject obj = nextOne(); // �������һ��������
			flyings = Arrays.copyOf(flyings, flyings.length + 1);
			flyings[flyings.length - 1] = obj;
		}
	}

	
	public void stepAction() {
		/** ��������һ�� */
		for (int i = 0; i < flyings.length; i++) { // ��������һ��
			FlyingObject f = flyings[i];
			f.step();
		}

		/** �ӵ���һ�� */
		for (int i = 0; i < bullets.length; i++) {
			Bullet b = bullets[i];
			b.step();
		}
		hero.step();
	}

	int shootIndex = 0; // �������

	/** ��� */
	public void shootAction() {
		shootIndex++;
		if (shootIndex % 30 == 0) { // 100���뷢һ��
			Bullet[] bs = hero.shoot(); // Ӣ�۴���ӵ�
			bullets = Arrays.copyOf(bullets, bullets.length + bs.length); // ����
			System.arraycopy(bs, 0, bullets, bullets.length - bs.length,
					bs.length); // ׷������
		}
	}

	/** �ӵ����������ײ��� */
	public void bangAction() {
		for (int i = 0; i < bullets.length; i++) { // ���������ӵ�
			Bullet b = bullets[i];//��ȡÿһ���ӵ�
			bang(b);
		}
	}

	/** ɾ��Խ������Ｐ�ӵ� */
	public void outOfBoundsAction() {
		int index = 0;
		FlyingObject[] flyingLives = new FlyingObject[flyings.length]; // ���ŵķ�����
		for (int i = 0; i < flyings.length; i++) {
			FlyingObject f = flyings[i];
			if (!f.outOfBounds()) {
				flyingLives[index++] = f; // ��Խ�������
			}
		}
		flyings = Arrays.copyOf(flyingLives, index); // ����Խ��ķ����ﶼ����

		index = 0; // ����Ϊ0
		Bullet[] bulletLives = new Bullet[bullets.length];
		for (int i = 0; i < bullets.length; i++) {
			Bullet b = bullets[i];
			if (!b.outOfBounds()) {
				bulletLives[index++] = b;
			}
		}
		bullets = Arrays.copyOf(bulletLives, index); // ����Խ����ӵ�����
	}

	/** �����Ϸ���� */
	public void checkGameOverAction() {
		if (isGameOver()) {
			state = GAME_OVER; // �ı�״̬
		}
	}

	/** �����Ϸ�Ƿ���� */
	public boolean isGameOver() {
		int index = -1;
		for (int i = 0; i < flyings.length; i++) {
			FlyingObject obj = flyings[i];
			if (hero.hit(obj)) { // ���Ӣ�ۻ���������Ƿ���ײ
				hero.subtractLife();
				hero.setDoubleFire(0);
				index = i;
				
				Ember ember = new Ember(hero);
				embers = Arrays.copyOf(embers, embers.length+1);
				embers[embers.length-1]=ember;
			}
		}
		if(index!=-1){
			FlyingObject t = flyings[index];
			flyings[index] = flyings[flyings.length-1];
			flyings[flyings.length-1] = t;
			flyings = Arrays.copyOf(flyings, flyings.length-1);
			
			Ember ember = new Ember(t);
			embers = Arrays.copyOf(embers, embers.length+1);
			embers[embers.length-1]=ember;
		}
		return  hero.getLife() == 0;
	}

	/** �ӵ��ͷ�����֮�����ײ��� */
	public void bang(Bullet bullet) {
		int index = -1; // ���еķ���������
		for (int i = 0; i < flyings.length; i++) {
			FlyingObject obj = flyings[i];
			if (obj.shootBy(bullet)) { // �ж��Ƿ����
				index = i; // ��¼�����еķ����������
				break;
			}
		}
		if (index != -1) { // �л��еķ�����
			FlyingObject one = flyings[index]; // ��¼�����еķ�����
			FlyingObject temp = flyings[index]; // �����еķ����������һ�������ｻ��
			flyings[index] = flyings[flyings.length - 1];
			flyings[flyings.length - 1] = temp;

			flyings = Arrays.copyOf(flyings, flyings.length - 1); // ɾ�����һ��������(�������е�)

			// ���one������ ����ǵ���, �����
			if (one instanceof Enemy) { // ������ͣ��ǵ��ˣ���ӷ�
				Enemy e = (Enemy) one; // ǿ������ת��
				score += e.getScore(); // �ӷ�
			} 
			
			if (one instanceof Award) { // ��Ϊ���������ý���
				Award a = (Award) one;
				int type = a.getType(); // ��ȡ��������
				switch (type) {
				case Award.DOUBLE_FIRE:
					hero.addDoubleFire(); // ����˫������
					break;
				case Award.LIFE:
					hero.addLife(); // ���ü���
					break;
				}
			}
			
			//�������ɻҽ�
			Ember ember = new Ember(one);
			embers = Arrays.copyOf(embers, embers.length+1);
			embers[embers.length-1]=ember;
		}
	}

	/**
	 * ������ɷ�����
	 * 
	 * @return ���������
	 */
	public static FlyingObject nextOne() {
		Random random = new Random();
		int type = random.nextInt(20); // [0,4)
		if (type==0) {
			return new Bee();
		}else if(type<=2){
			return new BigPlane();
		}else{
			return new Airplane();
		}
	}
}
