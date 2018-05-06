package com.tarena.shoot;



/**
 * �зɻ�: �Ƿ����Ҳ�ǵ���
 */
public class Airplane extends FlyingObject implements Enemy {

	int speed = 2;  //�ƶ�����
	
	public Airplane(){
		this.image = ShootGame.airplane;
		this.ember = ShootGame.airplaneEmber;
		width = image.getWidth();
		height = image.getHeight();
		y = -height;          
		x = (int)(Math.random()*(ShootGame.WIDTH - width));
	}

	
	@Override
	public int getScore() {  //��ȡ����
		return 5;
	}

	@Override
	public 	boolean outOfBounds() {   //Խ�紦��
		return y>ShootGame.HEIGHT;
	}

	@Override
	public void step() {   //�ƶ�
		y += speed;
	}

}

