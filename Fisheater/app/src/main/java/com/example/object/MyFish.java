package com.example.object;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;

import com.example.factory.GameObjectFactory;
import com.example.interfaces.IMyFish;
import com.example.fisheater.R;
import com.example.view.MainView;

/*��ҷɻ�����*/
public class MyFish extends GameObject implements IMyFish {
	private float middle_x;			 // �ɻ�����������
	private float middle_y;
	private float mul;
	public int level;
	private long startTime;	 	 	 // ��ʼ��ʱ��
	private long endTime;	 	 	 // ������ʱ��
	public Bitmap myplane;			 // �ɻ�����ʱ��ͼƬ
	private Bitmap myplane2;		 // �ɻ���ըʱ��ͼƬ
	private MainView mainView;
	private GameObjectFactory factory;
	public MyFish(Resources resources) {
		super(resources);
		// TODO Auto-generated constructor stub
		initBitmap();
		this.speed = 8;
		factory = new GameObjectFactory();
	}
	public void setMainView(MainView mainView) {
		this.mainView = mainView;
	}
	// ������Ļ��Ⱥ͸߶�
	@Override
	public void setScreenWH(float screen_width, float screen_height) {
		super.setScreenWH(screen_width, screen_height);
		object_x = screen_width/2 - object_width/2;
		object_y = screen_height/2 - object_height/2;
		middle_x = object_x + object_width/2;
		middle_y = object_y + object_height/2;
	}
	// ��ʼ��ͼƬ��Դ��
	@Override
	public void initBitmap() {
		// TODO Auto-generated method stub
		myplane = BitmapFactory.decodeResource(resources, R.drawable.myplane1);
		mul=0.200f;
		myplane = big(mul);
		myplane2 = BitmapFactory.decodeResource(resources, R.drawable.ex);
		object_width = myplane.getWidth() / 2; // ���ÿһ֡λͼ�Ŀ�
		object_height = myplane.getHeight() ; 	// ���ÿһ֡λͼ�ĸ�
	}
	// ����Ļ�ͼ����
	@Override
	public void drawSelf(Canvas canvas) {
		// TODO Auto-generated method stub
		if(isAlive){
			int x = (int) (currentFrame * object_width); // ��õ�ǰ֡�����λͼ��X����
			canvas.save();
			canvas.clipRect(object_x, object_y, (object_x + object_width), (object_y + object_height));
			canvas.drawBitmap(myplane, object_x - x, object_y, paint);
			canvas.restore();
			currentFrame++;
			if (currentFrame >= 2) {
				currentFrame = 0;
			}
		}
		else{
			int x = (int) (currentFrame * object_width); // ��õ�ǰ֡�����λͼ��Y����
			canvas.save();
			canvas.clipRect(object_x, object_y, (object_x + object_width), (object_y + object_height));
			canvas.drawBitmap(myplane2, object_x - x, object_y, paint);
			canvas.restore();
			currentFrame++;
			if (currentFrame >= 2) {
				currentFrame = 1;
			}
		}
	}
	// �ͷ���Դ�ķ���
	@Override
	public void release() {
		// TODO Auto-generated method stub
		if(!myplane.isRecycled()){
			myplane.recycle();
		}
		if(!myplane2.isRecycled()){
			myplane2.recycle();
		}
	}
	@Override
	public float getMiddle_x() {
		return middle_x;
	}
	@Override
	public void setMiddle_x(float middle_x) {
		this.middle_x = middle_x;
		this.object_x = middle_x - object_width/2;
	}
	@Override
	public float getMiddle_y() {
		return middle_y;
	}
	@Override
	public void setMiddle_y(float middle_y) {
		this.middle_y = middle_y;
		this.object_y = middle_y - object_height/2;
	}

	public Bitmap big(float mul){
		Matrix matrix = new Matrix();
		matrix.postScale(mul,mul);
		object_width = mul*object_width;
		object_height = mul*object_height;
		Bitmap newmyfishpic = Bitmap.createBitmap(myplane,0,0,myplane.getWidth(),myplane.getHeight(),matrix,true);
		if (newmyfishpic.equals(myplane)){
			return newmyfishpic;
		}
		myplane.recycle();
		return newmyfishpic;
	}
}
