package com.tarena.shoot;

import java.util.Random;

public class BigPlane extends FlyingObject 
	implements Enemy, Award{
	private int speed = 1; 
	private int life;
	private int awardType;

	public BigPlane() {
		life = 4;
		this.image = ShootGame.bigPlane;
		this.ember = ShootGame.bigPlaneEmber;
		width = image.getWidth();
		height = image.getHeight();
		y = -height;       
		Random rand = new Random();
		x = rand.nextInt(ShootGame.WIDTH - width);
		awardType = rand.nextInt(2);
	}
	
	public int getType() {
		return awardType;
	}

	@Override
	public int getScore() {
		return 50;
	}

	@Override
	public boolean outOfBounds() {
		return false;
	}
 
	
//	@Override
//	public BufferedImage getImage() {
//		Graphics g = image.getGraphics();
//		g.setColor(Color.white);
//		g.fillRect(0, 0, 40, 30);
//		g.setColor(Color.black);
//		g.drawString("L:"+life, 10, 20);
//		return image;
//	}
	
	@Override
	public void step() {   //ÒÆ¶¯
		y += speed;
	}
	
	public boolean shootBy(Bullet bullet) {
		if(super.shootBy(bullet)){
			life--;
		}
		return life==0;
	}
}
