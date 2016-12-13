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

/*游戏进行的主界面*/
public class MainView extends BaseView{
    private int smallPlaneScore;	// 小型敌机的积分
	private int middlePlaneScore;	// 中型敌机的积分
	private int bigPlaneScore;		// 大型敌机的积分
	private int bossPlaneScore;		// boss型敌机的积分
	private int sumScore;			// 游戏总得分
	private int speedTime;			// 游戏速度的倍数
	public int level;
	private float bg_y;				// 图片的坐标
	private float bg_y2;
	private float play_bt_w;
	private float play_bt_h;	 
	private float missile_bt_y;		 	
	private boolean isPlay;			// 标记游戏运行状态
	private boolean isTouchPlane;	// 判断玩家是否按下屏幕
	private Bitmap background; 		// 背景图片
	private Bitmap playButton; 		// 开始/暂停游戏的按钮图片
	private Bitmap missile_bt;		// 导弹按钮图标
	public MyFish myFish;		// 玩家的飞机
	private BossFish bossPlane;	// boss飞机
	private List<EnemyFish> enemyFishes;
	private GameObjectFactory factory;

	public MainView(Context context,GameSoundPool sounds) {
		super(context,sounds);
		// TODO Auto-generated constructor stub
		isPlay = true;
		speedTime = 1;
		level=1;
		factory = new GameObjectFactory();						  //工厂类
		enemyFishes = new ArrayList<EnemyFish>();
		myFish = (MyFish) factory.createMyFish(getResources());//生产玩家的飞机
		myFish.setMainView(this);
		for(int i = 0; i < SmallFish.sumCount; i++){
			//生产小型敌机
			SmallFish smallPlane = (SmallFish) factory.createSmallFish(getResources());
			enemyFishes.add(smallPlane);
		}
		for(int i = 0; i < MiddleFish.sumCount; i++){
			//生产中型敌机
			MiddleFish middlePlane = (MiddleFish) factory.createMiddleFish(getResources());
			enemyFishes.add(middlePlane);
		}
		for(int i = 0; i < BigFish.sumCount; i++){
			//生产大型敌机
			BigFish bigPlane = (BigFish) factory.createBigFish(getResources());
			enemyFishes.add(bigPlane);
		}

		//生产BOSS敌机
		bossPlane = (BossFish)factory.createBossFish(getResources());
		enemyFishes.add(bossPlane);
		thread = new Thread(this);	
	}
	// 视图改变的方法
	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		super.surfaceChanged(arg0, arg1, arg2, arg3);
	}
	// 视图创建的方法
	@Override
	public void surfaceCreated(SurfaceHolder arg0) {
		// TODO Auto-generated method stub
		super.surfaceCreated(arg0);
		initBitmap(); // 初始化图片资源
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
	// 视图销毁的方法
	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {
		// TODO Auto-generated method stub
		super.surfaceDestroyed(arg0);
		release();
	}
	// 响应触屏事件的方法
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
			//判断玩家飞机是否被按下
			else if(x > myFish.getObject_x() && x < myFish.getObject_x() + myFish.getObject_width()
					&& y > myFish.getObject_y() && y < myFish.getObject_y() + myFish.getObject_height()){
				if(isPlay){
					isTouchPlane = true;
				}
				return true;
			}
		}
		//响应手指在屏幕移动的事件
		else if(event.getAction() == MotionEvent.ACTION_MOVE && event.getPointerCount() == 1){
			//判断触摸点是否为玩家的飞机
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
	// 初始化图片资源方法
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
	//初始化游戏对象
	public void initObject(){
		for(EnemyFish obj: enemyFishes){

			//初始化小型敌机	
			if(obj instanceof SmallFish){
				if(!obj.isAlive()){
					obj.initial(speedTime,0,0);
					break;
				}	
			}
			//初始化中型敌机
			else if(obj instanceof MiddleFish){

					if(!obj.isAlive()){
						obj.initial(speedTime,0,0);
						break;
					}	

			}
			//初始化大型敌机
			else if(obj instanceof BigFish){

					if(!obj.isAlive()){
						obj.initial(speedTime,0,0);
						break;
					}	

			}
			//初始化BOSS敌机
			else{

					if(!obj.isAlive()){
						obj.initial(0,0,0);
						break;
					}

			}
		}

		//提升等级
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
	// 释放图片资源的方法
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
	// 绘图方法
	@Override
	public void drawSelf() {
		// TODO Auto-generated method stub
		try {
			canvas = sfh.lockCanvas();
			canvas.drawColor(Color.BLACK); // 绘制背景色
			canvas.save();
			// 计算背景图片与屏幕的比例
			canvas.scale(scalex, scaley, 0, 0);
			canvas.drawBitmap(background, 0, bg_y, paint);   // 绘制背景图
			canvas.restore();	
			//绘制按钮
			canvas.save();
			canvas.clipRect(10, 10, 10 + play_bt_w,10 + play_bt_h);
			if(isPlay){
				canvas.drawBitmap(playButton, 10, 10, paint);			 
			}
			else{
				canvas.drawBitmap(playButton, 10, 10 - play_bt_h, paint);
			}
			canvas.restore();

			//绘制敌机
			for(EnemyFish obj: enemyFishes){
				if(obj.isAlive()){
					obj.drawSelf(canvas);					
					//检测敌机是否与玩家的飞机碰撞					
					if(obj.isCanCollide() && myFish.isAlive()){
						if(obj.isCollide(myFish)){
							if(level>obj.getlevel())
							{
								myFish.setAlive(true);
								obj.isExplosion = true;
								float mul = 1;//吃鱼后的放大倍数
								if(obj instanceof BigFish)
									mul = 1.012f;
								else if (obj instanceof MiddleFish)
									mul = 1.009f;
								else if (obj instanceof SmallFish)
									mul = 1.007f;
								else if (obj instanceof BossFish)
									mul = 1.018f;
								myFish.myplane = myFish.big(mul);
								addGameScore(obj.getScore());// 获得分数
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
				sounds.playSound(4, 0);			//飞机炸毁的音效
			}
			myFish.drawSelf(canvas);	//绘制玩家的飞机

			//绘制积分文字
			paint.setTextSize(30);
			paint.setColor(Color.rgb(235, 161, 1));
			canvas.drawText("积分:"+String.valueOf(sumScore), 30 + play_bt_w, 40, paint);		//绘制文字
			canvas.drawText("等级 X "+String.valueOf(level), screen_width - 150, 40, paint); //绘制文字
		} catch (Exception err) {
			err.printStackTrace();
		} finally {
			if (canvas != null)
				sfh.unlockCanvasAndPost(canvas);
		}
	}

	// 增加游戏分数的方法 
	public void addGameScore(int score){
		smallPlaneScore +=score;
		middlePlaneScore += score;	// 中型敌机的积分
		bigPlaneScore += score;		// 大型敌机的积分
		bossPlaneScore += score;	// boss型敌机的积分	
//		missileScore += score;		// 导弹的积分
//		bulletScore += score;		// 子弹的积分
		sumScore += score;			// 游戏总得分
	}
	// 播放音效
	public void playSound(int key){
		sounds.playSound(key, 0);
	}
	// 线程运行的方法
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
