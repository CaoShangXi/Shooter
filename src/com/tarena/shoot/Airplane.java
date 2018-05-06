package com.tarena.shoot;



/**
 * 敌飞机: 是飞行物，也是敌人
 */
public class Airplane extends FlyingObject implements Enemy {

	int speed = 2;  //移动步骤
	
	public Airplane(){
		this.image = ShootGame.airplane;
		this.ember = ShootGame.airplaneEmber;
		width = image.getWidth();
		height = image.getHeight();
		y = -height;          
		x = (int)(Math.random()*(ShootGame.WIDTH - width));
	}

	
	@Override
	public int getScore() {  //获取分数
		return 5;
	}

	@Override
	public 	boolean outOfBounds() {   //越界处理
		return y>ShootGame.HEIGHT;
	}

	@Override
	public void step() {   //移动
		y += speed;
	}

}

