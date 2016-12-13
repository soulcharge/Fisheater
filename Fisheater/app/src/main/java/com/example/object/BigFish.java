package com.example.object;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import com.example.fisheater.R;

import java.util.Random;
/*���͵л�����*/
public class BigFish extends EnemyFish {
	private static int currentCount = 0;	 //	����ǰ������
	public static int sumCount = 2;	 	 	 //	�����ܵ�����
	private Bitmap bigPlane; // ����ͼƬ
	public BigFish(Resources resources) {
		super(resources);
		// TODO Auto-generated constructor stub
		this.score = 15000;		// Ϊ�������÷���
		this.level = 2;
	}
	//��ʼ������
	@Override
	public void initial(int arg0,float arg1,float arg2){
		isAlive = true;
		bloodVolume = 30;
		blood = bloodVolume;
		Random ran = new Random();
		speed = ran.nextInt(2) + 4 * arg0;	
		object_x = ran.nextInt((int)(screen_width - object_width));
		if(leftorright==0)
		{
			object_y = -object_height * (currentCount*2 + 1);
		}
		else{
			object_y =screen_height;
		}
		currentCount++;
		if(currentCount >= sumCount){
			currentCount = 0;
		}
	}
	// ��ʼ��ͼƬ��Դ	
	@Override
	public void initBitmap() {
		// TODO Auto-generated method stub
		bigPlane = BitmapFactory.decodeResource(resources, R.drawable.big);
		object_width = bigPlane.getWidth();			//���ÿһ֡λͼ�Ŀ�
		object_height = bigPlane.getHeight()/5;		//���ÿһ֡λͼ�ĸ�
	}
	// ����Ļ�ͼ����
	@Override
	public void drawSelf(Canvas canvas) {
		// TODO Auto-generated method stub
		if(isAlive){
			if(!isExplosion){
				if(isVisible){
					canvas.save();
					canvas.clipRect(object_x,object_y,object_x + object_width,object_y + object_height);
					canvas.drawBitmap(bigPlane, object_x, object_y,paint);
					canvas.restore();
				}	
				logic();
			}
			else{
				int y = (int) (currentFrame * object_height); // ��õ�ǰ֡�����λͼ��Y����
				canvas.save();
				canvas.clipRect(object_x,object_y,object_x + object_width,object_y + object_height);
				canvas.drawBitmap(bigPlane, object_x, object_y - y,paint);
				canvas.restore();
				currentFrame++;
				if(currentFrame >= 5){
					currentFrame = 0;
					isExplosion = false;
					isAlive = false;
				}
			}
		}
	}
	// �ͷ���Դ
	@Override
	public void release() {
		// TODO Auto-generated method stub
		if(!bigPlane.isRecycled()){
			bigPlane.recycle();
		}
	}
}
