package com.example.factory;

import android.content.res.Resources;

import com.example.object.BigFish;
import com.example.object.BossFish;
import com.example.object.GameObject;
import com.example.object.MiddleFish;
import com.example.object.MyFish;
import com.example.object.SmallFish;

/*游戏对象的工厂类*/
public class GameObjectFactory {
	//创建小型敌机的方法
	public GameObject createSmallFish(Resources resources){
		return new SmallFish(resources);
	}
	//创建中型敌机的方法
	public GameObject createMiddleFish(Resources resources){
		return new MiddleFish(resources);
	}
	//创建大型敌机的方法
	public GameObject createBigFish(Resources resources){
		return new BigFish(resources);
	}
	//创建BOSS敌机的方法
	public GameObject createBossFish(Resources resources){
		return new BossFish(resources);
	}
	//创建玩家飞机的方法
	public GameObject createMyFish(Resources resources){
		return new MyFish(resources);
	}
	/*//创建玩家子弹
	public GameObject createMyBullet(Resources resources){
		return new MyBullet(resources);
	}
	//创建玩家子弹
	public GameObject createMyBullet2(Resources resources){
		return new MyBullet2(resources);
	}
	//创建BOSS子弹
	public GameObject createBossBullet(Resources resources){
		return new BossBullet(resources);
	}
	//创建导弹物品
	public GameObject createMissileGoods(Resources resources){
		return new MissileGoods(resources);
	}
	//创建子弹物品
	public GameObject createBulletGoods(Resources resources){
		return new BulletGoods(resources);
	}*/
}
