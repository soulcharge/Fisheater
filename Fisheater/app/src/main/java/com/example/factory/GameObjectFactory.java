package com.example.factory;

import android.content.res.Resources;

import com.example.object.BigFish;
import com.example.object.BossFish;
import com.example.object.GameObject;
import com.example.object.MiddleFish;
import com.example.object.MyFish;
import com.example.object.SmallFish;

/*��Ϸ����Ĺ�����*/
public class GameObjectFactory {
	//����С�͵л��ķ���
	public GameObject createSmallFish(Resources resources){
		return new SmallFish(resources);
	}
	//�������͵л��ķ���
	public GameObject createMiddleFish(Resources resources){
		return new MiddleFish(resources);
	}
	//�������͵л��ķ���
	public GameObject createBigFish(Resources resources){
		return new BigFish(resources);
	}
	//����BOSS�л��ķ���
	public GameObject createBossFish(Resources resources){
		return new BossFish(resources);
	}
	//������ҷɻ��ķ���
	public GameObject createMyFish(Resources resources){
		return new MyFish(resources);
	}
	/*//��������ӵ�
	public GameObject createMyBullet(Resources resources){
		return new MyBullet(resources);
	}
	//��������ӵ�
	public GameObject createMyBullet2(Resources resources){
		return new MyBullet2(resources);
	}
	//����BOSS�ӵ�
	public GameObject createBossBullet(Resources resources){
		return new BossBullet(resources);
	}
	//����������Ʒ
	public GameObject createMissileGoods(Resources resources){
		return new MissileGoods(resources);
	}
	//�����ӵ���Ʒ
	public GameObject createBulletGoods(Resources resources){
		return new BulletGoods(resources);
	}*/
}
