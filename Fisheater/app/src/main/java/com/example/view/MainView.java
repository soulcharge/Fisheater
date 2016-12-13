package com.example.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Message;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import com.example.constant.ConstantUtil;
import com.example.factory.GameObjectFactory;
import com.example.fisheater.R;
import com.example.object.BigFish;
import com.example.object.BossFish;
import com.example.object.EnemyFish;
import com.example.object.GameObject;
import com.example.object.MiddleFish;
import com.example.object.MyFish;
import com.example.object.SmallFish;
import com.example.sounds.GameSoundPool;

import java.util.ArrayList;
import java.util.List;

/*��Ϸ���е�������*/
public class MainView extends BaseView{
    private int smallPlaneScore;	// С�͵л��Ļ���
	private int middlePlaneScore;	// ���͵л��Ļ���
	private int bigPlaneScore;		// ���͵л��Ļ���
	private int bossPlaneScore;		// boss�͵л��Ļ���
	private int sumScore;			// ��Ϸ�ܵ÷�
	private int speedTime;			// ��Ϸ�ٶȵı���
	public int level;
	private float bg_y;				// ͼƬ������
	private float bg_y2;
	private float play_bt_w;
	private float play_bt_h;	 
	private float missile_bt_y;		 	
	private boolean isPlay;			// �����Ϸ����״̬
	private boolean isTouchPlane;	// �ж�����Ƿ�����Ļ
	private Bitmap background; 		// ����ͼƬ
	private Bitmap playButton; 		// ��ʼ/��ͣ��Ϸ�İ�ťͼƬ
	private Bitmap missile_bt;		// ������ťͼ��
	public MyFish myFish;		// ��ҵķɻ�
	private BossFish bossPlane;	// boss�ɻ�
	private List<EnemyFish> enemyFishes;
	private GameObjectFactory factory;

	public MainView(Context context,GameSoundPool sounds) {
		super(context,sounds);
		// TODO Auto-generated constructor stub
		isPlay = true;
		speedTime = 1;
		level=1;
		factory = new GameObjectFactory();						  //������
		enemyFishes = new ArrayList<EnemyFish>();
		myFish = (MyFish) factory.createMyFish(getResources());//������ҵķɻ�
		myFish.setMainView(this);
		for(int i = 0; i < SmallFish.sumCount; i++){
			//����С�͵л�
			SmallFish smallPlane = (SmallFish) factory.createSmallFish(getResources());
			enemyFishes.add(smallPlane);
		}
		for(int i = 0; i < MiddleFish.sumCount; i++){
			//�������͵л�
			MiddleFish middlePlane = (MiddleFish) factory.createMiddleFish(getResources());
			enemyFishes.add(middlePlane);
		}
		for(int i = 0; i < BigFish.sumCount; i++){
			//�������͵л�
			BigFish bigPlane = (BigFish) factory.createBigFish(getResources());
			enemyFishes.add(bigPlane);
		}

		//����BOSS�л�
		bossPlane = (BossFish)factory.createBossFish(getResources());
		enemyFishes.add(bossPlane);
		thread = new Thread(this);	
	}
	// ��ͼ�ı�ķ���
	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		super.surfaceChanged(arg0, arg1, arg2, arg3);
	}
	// ��ͼ�����ķ���
	@Override
	public void surfaceCreated(SurfaceHolder arg0) {
		// TODO Auto-generated method stub
		super.surfaceCreated(arg0);
		initBitmap(); // ��ʼ��ͼƬ��Դ
		for(GameObject obj: enemyFishes){
			obj.setScreenWH(screen_width,screen_height);
		}
		myFish.setScreenWH(screen_width,screen_height);
		myFish.setAlive(true);
		if(thread.isAlive()){
			thread.start();
		}
		else{
			thread = new Thread(this);
			thread.start();
		}
	}
	// ��ͼ���ٵķ���
	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {
		// TODO Auto-generated method stub
		super.surfaceDestroyed(arg0);
		release();
	}
	// ��Ӧ�����¼��ķ���
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if(event.getAction() == MotionEvent.ACTION_UP){
			isTouchPlane = false;
			return true;
		}
		else if(event.getAction() == MotionEvent.ACTION_DOWN){
			float x = event.getX();
			float y = event.getY();
			if(x > 10 && x < 10 + play_bt_w && y > 10 && y < 10 + play_bt_h){
				if(isPlay){
					isPlay = false;
				}		
				else{
					isPlay = true;	
					synchronized(thread){
						thread.notify();
					}
				}
				return true;
			}
			//�ж���ҷɻ��Ƿ񱻰���
			else if(x > myFish.getObject_x() && x < myFish.getObject_x() + myFish.getObject_width()
					&& y > myFish.getObject_y() && y < myFish.getObject_y() + myFish.getObject_height()){
				if(isPlay){
					isTouchPlane = true;
				}
				return true;
			}
		}
		//��Ӧ��ָ����Ļ�ƶ����¼�
		else if(event.getAction() == MotionEvent.ACTION_MOVE && event.getPointerCount() == 1){
			//�жϴ������Ƿ�Ϊ��ҵķɻ�
			if(isTouchPlane){
				float x = event.getX();
				float y = event.getY();
				if(x > myFish.getMiddle_x() + 20){
					if(myFish.getMiddle_x() + myFish.getSpeed() <= screen_width){
						myFish.setMiddle_x(myFish.getMiddle_x() + myFish.getSpeed());
					}					
				}
				else if(x < myFish.getMiddle_x() - 20){
					if(myFish.getMiddle_x() - myFish.getSpeed() >= 0){
						myFish.setMiddle_x(myFish.getMiddle_x() - myFish.getSpeed());
					}				
				}
				if(y > myFish.getMiddle_y() + 20){
					if(myFish.getMiddle_y() + myFish.getSpeed() <= screen_height){
						myFish.setMiddle_y(myFish.getMiddle_y() + myFish.getSpeed());
					}		
				}
				else if(y < myFish.getMiddle_y() - 20){
					if(myFish.getMiddle_y() - myFish.getSpeed() >= 0){
						myFish.setMiddle_y(myFish.getMiddle_y() - myFish.getSpeed());
					}
				}
				return true;
			}	
		}
		return false;
	}
	// ��ʼ��ͼƬ��Դ����
	@Override
	public void initBitmap() {
		// TODO Auto-generated method stub
		playButton = BitmapFactory.decodeResource(getResources(),R.drawable.play);
		background = BitmapFactory.decodeResource(getResources(), R.drawable.bg_01);
		missile_bt = BitmapFactory.decodeResource(getResources(), R.drawable.missile_bt);
		scalex = screen_width / background.getWidth();
		scaley = screen_height / background.getHeight();
		play_bt_w = playButton.getWidth();
		play_bt_h = playButton.getHeight()/2;
		bg_y = 0;
		bg_y2 = bg_y - screen_height;
		missile_bt_y = screen_height - 10 - missile_bt.getHeight();
	}
	//��ʼ����Ϸ����
	public void initObject(){
		for(EnemyFish obj: enemyFishes){

			//��ʼ��С�͵л�	
			if(obj instanceof SmallFish){
				if(!obj.isAlive()){
					obj.initial(speedTime,0,0);
					break;
				}	
			}
			//��ʼ�����͵л�
			else if(obj instanceof MiddleFish){

					if(!obj.isAlive()){
						obj.initial(speedTime,0,0);
						break;
					}	

			}
			//��ʼ�����͵л�
			else if(obj instanceof BigFish){

					if(!obj.isAlive()){
						obj.initial(speedTime,0,0);
						break;
					}	

			}
			//��ʼ��BOSS�л�
			else{

					if(!obj.isAlive()){
						obj.initial(0,0,0);
						break;
					}

			}
		}

		//�����ȼ�
		if(sumScore >= speedTime*100000 && speedTime < 6){
			speedTime++;	
		}
		if(sumScore >= 10000 &&sumScore <50000){
			level=2;
		}
		if(sumScore >= 50000 &&sumScore <250000){
			level=3;
		}
		if(sumScore >= 250000 ){
			level=4;
		}
		if(sumScore >= 1000000 ){
			threadFlag = false;
		}
	}
	// �ͷ�ͼƬ��Դ�ķ���
	@Override
	public void release() {
		// TODO Auto-generated method stub
		for(GameObject obj: enemyFishes){
			obj.release();
		}
		myFish.release();

		if(!playButton.isRecycled()){
			playButton.recycle();
		}
		if(!background.isRecycled()){
			background.recycle();
		}
		if(!missile_bt.isRecycled()){
			missile_bt.recycle();
		}
	}
	// ��ͼ����
	@Override
	public void drawSelf() {
		// TODO Auto-generated method stub
		try {
			canvas = sfh.lockCanvas();
			canvas.drawColor(Color.BLACK); // ���Ʊ���ɫ
			canvas.save();
			// ���㱳��ͼƬ����Ļ�ı���
			canvas.scale(scalex, scaley, 0, 0);
			canvas.drawBitmap(background, 0, bg_y, paint);   // ���Ʊ���ͼ
			canvas.restore();	
			//���ư�ť
			canvas.save();
			canvas.clipRect(10, 10, 10 + play_bt_w,10 + play_bt_h);
			if(isPlay){
				canvas.drawBitmap(playButton, 10, 10, paint);			 
			}
			else{
				canvas.drawBitmap(playButton, 10, 10 - play_bt_h, paint);
			}
			canvas.restore();

			//���Ƶл�
			for(EnemyFish obj: enemyFishes){
				if(obj.isAlive()){
					obj.drawSelf(canvas);					
					//���л��Ƿ�����ҵķɻ���ײ					
					if(obj.isCanCollide() && myFish.isAlive()){
						if(obj.isCollide(myFish)){
							if(level>obj.getlevel())
							{
								myFish.setAlive(true);
								obj.isExplosion = true;
								float mul = 1;//�����ķŴ���
								if(obj instanceof BigFish)
									mul = 1.012f;
								else if (obj instanceof MiddleFish)
									mul = 1.009f;
								else if (obj instanceof SmallFish)
									mul = 1.007f;
								else if (obj instanceof BossFish)
									mul = 1.018f;
								myFish.myplane = myFish.big(mul);
								addGameScore(obj.getScore());// ��÷���
							}
							else
							{
								myFish.setAlive(false);
							}
						}
					}
				}	
			}
			if(!myFish.isAlive()){
				threadFlag = false;
				sounds.playSound(4, 0);			//�ɻ�ը�ٵ���Ч
			}
			myFish.drawSelf(canvas);	//������ҵķɻ�

			//���ƻ�������
			paint.setTextSize(30);
			paint.setColor(Color.rgb(235, 161, 1));
			canvas.drawText("����:"+String.valueOf(sumScore), 30 + play_bt_w, 40, paint);		//��������
			canvas.drawText("�ȼ� X "+String.valueOf(level), screen_width - 150, 40, paint); //��������
		} catch (Exception err) {
			err.printStackTrace();
		} finally {
			if (canvas != null)
				sfh.unlockCanvasAndPost(canvas);
		}
	}

	// ������Ϸ�����ķ��� 
	public void addGameScore(int score){
		smallPlaneScore +=score;
		middlePlaneScore += score;	// ���͵л��Ļ���
		bigPlaneScore += score;		// ���͵л��Ļ���
		bossPlaneScore += score;	// boss�͵л��Ļ���	
//		missileScore += score;		// �����Ļ���
//		bulletScore += score;		// �ӵ��Ļ���
		sumScore += score;			// ��Ϸ�ܵ÷�
	}
	// ������Ч
	public void playSound(int key){
		sounds.playSound(key, 0);
	}
	// �߳����еķ���
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (threadFlag) {	
			long startTime = System.currentTimeMillis();
			initObject();
			drawSelf();
			long endTime = System.currentTimeMillis();	
			if(!isPlay){
				synchronized (thread) {  
				    try {  
				    	thread.wait();  
				    } catch (InterruptedException e) {  
				        e.printStackTrace();  
				    }  
				}  		
			}	
			try {
				if (endTime - startTime < 100)
					Thread.sleep(100 - (endTime - startTime));
			} catch (InterruptedException err) {
				err.printStackTrace();
			}
		}
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Message message = new Message();   
		message.what = 	ConstantUtil.TO_END_VIEW;
		message.arg1 = Integer.valueOf(sumScore);
		mainActivity.getHandler().sendMessage(message);
	}
}
