package com.example.object;

import android.content.res.Resources;
import android.graphics.Canvas;

import java.util.Random;

/*�л���*/
public class EnemyFish extends GameObject{
	protected int score;						 // ����ķ�ֵ
	protected int level;                      //����ĵȼ�
	protected int blood; 						 // ����ĵ�ǰѪ��
	protected int bloodVolume; 					 // �����ܵ�Ѫ��
	protected int leftorright;                   //�����ˢ�·���
	public boolean isExplosion;   			 // �ж��Ƿ�ը
	protected boolean isVisible;		 		 //	 �����Ƿ�Ϊ�ɼ�״̬
	public EnemyFish(Resources resources) {
		super(resources);
		// TODO Auto-generated constructor stub
		Random ran = new Random();
		leftorright = ran.nextInt(2);
		initBitmap();			// ��ʼ��ͼƬ��Դ
	}
	//��ʼ������
	@Override
	public void initial(int arg0,float arg1,float arg2){
		
	}
	// ��ʼ��ͼƬ��Դ
	@Override
	public void initBitmap() {
		// TODO Auto-generated method stub
	
	}
	// ����Ļ�ͼ����
	@Override
	public void drawSelf(Canvas canvas) {
		// TODO Auto-generated method stub
		//�жϵл��Ƿ�����״̬
		
	}
	// �ͷ���Դ
	@Override
	public void release() {
		// TODO Auto-generated method stub
		
	}
	// ������߼�����
	@Override
	public void logic() {
		if(leftorright==0){
			// TODO Auto-generated method stub
			if (object_y < screen_height) {
				object_y += speed;
			}
			else {
				isAlive = false;
			}
			if(object_y + object_height > 0){
				isVisible = true;
			}
			else{
				isVisible = false;
			}
		}
		else{
			// TODO Auto-generated method stub
			if (object_y + object_height > 0) {
				object_y -= speed;
			}
			else {
				isAlive = false;
			}
			if(object_y < screen_height){
				isVisible = true;
			}
			else{
				isVisible = false;
			}
		}
	}
	// ���������߼�����
	public void attacked(int harm) {
		// TODO Auto-generated method stub
		blood -= harm;
		if (blood <= 0) {
			isExplosion = true;
		}
	}
	// �����ײ
	@Override
	public boolean isCollide(GameObject obj) {
		// ����1λ�ھ���2�����
		if (object_x <= obj.getObject_x()
				&& object_x + object_width <= obj.getObject_x()) {
			return false;
		}
		// ����1λ�ھ���2���Ҳ�
		else if (obj.getObject_x() <= object_x
				&& obj.getObject_x() + obj.getObject_width() <= object_x) {
			return false;
		}
		// ����1λ�ھ���2���Ϸ�
		else if (object_y <= obj.getObject_y()
				&& object_y + object_height <= obj.getObject_y()) {
			return false;
		}
		// ����1λ�ھ���2���·�
		else if (obj.getObject_y() <= object_y
				&& obj.getObject_y() + obj.getObject_height() <= object_y) {
			return false;
		}
		return true;
	}
	// �ж��ܷ񱻼����ײ
	public boolean isCanCollide() {
		// TODO Auto-generated method stub
		return isAlive && !isExplosion && isVisible;
	}
	//getter��setter����
	public int getScore() {
		// TODO Auto-generated method stub
		return score;
	}
	public void setScore(int score) {
		// TODO Auto-generated method stub
		this.score = score;
	}
	public int getlevel() {
		// TODO Auto-generated method stub
		return level;
	}
	public void setlevel(int level) {
		// TODO Auto-generated method stub
		this.level = level;
	}
	public int getBlood() {
		// TODO Auto-generated method stub
		return blood;
	}
	public void setBlood(int blood) {
		// TODO Auto-generated method stub
		this.blood = blood;
	}
	public int getBloodVolume() {
		// TODO Auto-generated method stub
		return bloodVolume;
	}
	public void setBloodVolume(int bloodVolume) {
		// TODO Auto-generated method stub
		this.bloodVolume = bloodVolume;
	}
	public boolean isExplosion() {
		// TODO Auto-generated method stub
		return isExplosion;
	}
}

