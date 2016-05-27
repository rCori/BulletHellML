package com.example.bulletmlhell;

import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import android.util.Log;
import jp.gr.java_conf.abagames.bulletml.Bullet;
import jp.gr.java_conf.abagames.bulletml.Direction;
import jp.gr.java_conf.abagames.bulletml.IActionElmChoice;
import jp.gr.java_conf.abagames.bulletml.Speed;
import jp.gr.java_conf.abagames.util.DegUtil;
import jp.gr.java_conf.abagames.util.SCTable;

public class BulletImpl {

	public static final int NOT_EXIST = -9999;
	private final int SCREEN_WIDTH_16 = GameActivity.CAMERA_WIDTH << 4;
	private final int SCREEN_HEIGHT_16 = GameActivity.CAMERA_HEIGHT << 4;

	private final int ACTION_MAX = 8;

	private ActionImpl[] action = new ActionImpl[ACTION_MAX];
	private int acIdx;

	private TextureRegion mBulletTextureRegion;
	private Sprite bulletSprite;
	public float x, y, px, py;

	public Direction drcElm;
	public Speed spdElm;
	public float direction, speed;

	public float mx, my;

	public int clrIdx;

	private int cnt;

	private BulletManager gameManager;

	private float[] prms;

	
	public BulletImpl(TextureRegion textureRegion, BulletManager gameManager2) {
		textureRegion = mBulletTextureRegion;
		gameManager = gameManager2;
		x = NOT_EXIST;

	}

	public float getAimDeg() {
		// return (float)DegUtil.getDeg(gameManager.xp - x, gameManager.yp - y)
		// * (float)Math.PI / 128;
		return (float) DegUtil.getDeg((float)(gameManager.xp - x), (float)(gameManager.yp - y)) * 360 / SCTable.TABLE_SIZE;
	}

	public void vanish() {
		for (int i = 0; i < acIdx; i++) {
			action[i].vanish();
		}
		bulletSprite.setVisible(false);
		x = NOT_EXIST;
	}

	public void changeAction(ActionImpl bfr, ActionImpl aft) {
		for (int i = 0; i < acIdx; i++) {
			if (action[i].equals(bfr)) {
				action[i] = aft;
				return;
			}
		}
	}
	
	public void set(IActionElmChoice[] aec, int x, int y, int ci) {
		this.x = px = x;
		this.y = py = y;
		mx = my = 0;
		clrIdx = ci & 3;
		cnt = 0;
		acIdx = 0;
		for (int i = 0; i < aec.length; i++) {
			action[acIdx] = gameManager.getActionImplInstance();
			if (action[acIdx] == null)
				break;
			action[acIdx].set(BulletmlNoizUtil.getActionElm(aec[i]), this);
			float[] actPrms = BulletmlNoizUtil.getActionParams(aec[i], prms);
			if (actPrms == null) {
				action[acIdx].setParams(prms);
			} else {
				action[acIdx].setParams(actPrms);
			}
			acIdx++;
			if (acIdx >= ACTION_MAX)
				break;
		}
	}

	public void set(IActionElmChoice[] aec, float x, float y, int ci) {
		this.x = px = x;
		this.y = py = y;
		mx = my = 0;
		clrIdx = ci & 3;
		cnt = 0;
		acIdx = 0;
		for (int i = 0; i < aec.length; i++) {
			action[acIdx] = gameManager.getActionImplInstance();
			if (action[acIdx] == null)
				break;
			action[acIdx].set(BulletmlNoizUtil.getActionElm(aec[i]), this);
			float[] actPrms = BulletmlNoizUtil.getActionParams(aec[i], prms);
			if (actPrms == null) {
				action[acIdx].setParams(prms);
			} else {
				action[acIdx].setParams(actPrms);
			}
			acIdx++;
			if (acIdx >= ACTION_MAX)
				break;
		}
	}
	
	public void set(Bullet bullet, int x, int y, int ci) {
		drcElm = bullet.getDirection();
		spdElm = bullet.getSpeed();
		IActionElmChoice[] aec = bullet.getActionElm();
		set(aec, x, y, ci);
	}

	public void set(Bullet bullet, float x, float y, int ci) {
		drcElm = bullet.getDirection();
		spdElm = bullet.getSpeed();
		IActionElmChoice[] aec = bullet.getActionElm();
		set(aec, x, y, ci);
	}
	
	public boolean isAllActionFinished() {
		for (int i = 0; i < acIdx; i++) {
			if (action[i].pc != ActionImpl.NOT_EXIST) {
				return false;
			}
		}
		return true;
	}


	public void move(float factor) {
		for (int i = 0; i < acIdx; i++) {
			action[i].move();
		}

		// int d = (int)(direction*SCTable.TABLE_SIZE/Math.PI/2);
		int d = (int) (direction * SCTable.TABLE_SIZE / 360);
		d &= (SCTable.TABLE_SIZE - 1);

		
		float mvx = (((int) ((speed * SCTable.sintbl[d])) + (int) (mx * 32)))*factor;
		float mvy = ((((int) (-speed * SCTable.costbl[d])) + (int) (my * 32)))*factor;
		x += mvx;
		y += mvy;
		px = x - mvx;
		py = y - mvy;
		

		if (px < 0 || px >= GameActivity.CAMERA_WIDTH || py < 0 || py >= GameActivity.CAMERA_HEIGHT) {
			vanish();
		}
	}
	
	public Sprite getSprite() {
		return bulletSprite;
	}
	
	public void SetSprite(Sprite bulletSprite){
		 this.bulletSprite = bulletSprite;
	}
	
	public void setParams(float[] prms) {
		this.prms = prms;
	}
	
	public boolean TestCollision(Player player) {
		if(px >= player.mx && px <= (player.mx + player.width) && 
				py >= player.my && py <= player.my + player.height) {
			return true;
		}
		return false;
	}
	
	public void draw() {
		if(x!=NOT_EXIST){
			bulletSprite.setVisible(true);
			bulletSprite.setPosition(px, py);
		} else {
			bulletSprite.setVisible(false);
		}
	}
}
