package com.example.bulletmlhell;

import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.TextureRegion;

public class PlayerBullet {
	private TextureRegion bulletTextureRegion;
	private Sprite bulletSprite;
	public float mx, my;
	private boolean exists;
	
	private final float BULLET_SPEED = 300.0f;
	
	public PlayerBullet() {
		mx = -10.0f;
		my = -10.0f;
		exists = false;
		bulletTextureRegion = null;
		bulletSprite = null;
	}
	
	public boolean GetExists() {
		return exists;
	}
	
	public void SetExists() {
		exists = true;
		bulletSprite.setVisible(true);
	}
	
	public void BulletUpdate(float elpasedSeconds) {
		my -= BULLET_SPEED * elpasedSeconds;
		if(my < 0 && exists) {
			Vanish();
		}
	}
	
	public void SetSprite(Sprite bulletSprite) {
		this.bulletSprite = bulletSprite;
	}
	
	public void Vanish() {
		exists = false;
		mx = -10.0f;
		my = -10.0f;
		bulletSprite.setVisible(false);
	}
	
	public void BulletDraw() {
		if(exists) {
			bulletSprite.setX(mx);
			bulletSprite.setY(my);	
		}
	}

}
