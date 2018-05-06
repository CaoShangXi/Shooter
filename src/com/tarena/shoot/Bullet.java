package com.tarena.shoot;


/**
 * �ӵ���
 */
public class Bullet extends FlyingObject {
	private int speed = 3;  //�ƶ����ٶ�
	private boolean bomb;
	public Bullet(int x,int y){
		this.x = x;
		this.y = y;
		this.image = ShootGame.bullet;
	}
	
	public void setBomb(boolean bomb) {
		this.bomb = bomb;
	}
	
	public boolean isBomb() {
		return bomb;
	}

	@Override
	public void step(){   //�ƶ�����
		y-=speed;
	}

	@Override
	public boolean outOfBounds() {
		return y<-height;
	}

}
