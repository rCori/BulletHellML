package com.example.bulletmlhell;

import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.TextureRegion;

public class Player {
	
	private final int MAX_BULLETS = 20;
	
	private TextureRegion enemyTextureRegion;
	private Sprite playerSprite;
	public float mx, my;
	public float width, height;
	
	//These are public to avoid needing a getter for effeciency sake.
	public PlayerBullet[] playerBullets;
	
	private float shootCounter;
	private final float SHOOT_TIME_LIMIT = 0.3f;
	
	private boolean isHit = false;
	private float hitCounter;
	private final float HIT_TIME_LIMIT = 1.5f;
	
	public Player(){
		playerBullets = new PlayerBullet[MAX_BULLETS];
		for(int i = 0; i < MAX_BULLETS; i++) {
			playerBullets[i] = new PlayerBullet();
		}
		mx = -1.0f;
		my = -1.0f;
		shootCounter = 0.0f;
	}
	
	public void SetSprite(Sprite playerSprite){
		this.playerSprite = playerSprite;
		width = playerSprite.getWidth();
		height = playerSprite.getHeight();
	}
	
	public void SetPosition(float mx, float my) {
		this.mx = mx;
		this.my = my;
	}
	
	public void PlayerUpdate(float elapsedSeconds) {
		for(PlayerBullet bullet : playerBullets) {
			bullet.BulletUpdate(elapsedSeconds);
		}
		if(isHit) {
			hitCounter -= elapsedSeconds;
			if(hitCounter <= 0f) {
				hitCounter = 0.0f;
				isHit = false;
				playerSprite.setAlpha(1.0f);
			}
		}
		shootCounter += elapsedSeconds;
		if(shootCounter > SHOOT_TIME_LIMIT) {
			shootCounter = 0.0f;
			if(!GetIsHit()) {
				for(PlayerBullet bullet : playerBullets) {
					if(!bullet.GetExists()) {
						bullet.SetExists();
						bullet.mx = mx;
						bullet.my = my;
						break;
					}
				}
			}
		}
	}

	public void TakeHit() {
		if(!isHit) {
			isHit = true;
			hitCounter = HIT_TIME_LIMIT;
			playerSprite.setAlpha(0.5f);
		}
	}
	
	public void PlayerDraw() {
		playerSprite.setX(mx);
		playerSprite.setY(my);
		for(PlayerBullet bullet : playerBullets) {
			bullet.BulletDraw();
		}
	}
	
	public boolean GetIsHit() {
		return isHit;
	}
	
	public void MovementConstraint() {
		if(mx > GameActivity.CAMERA_WIDTH - (playerSprite.getWidth())) {
			mx = GameActivity.CAMERA_WIDTH - (playerSprite.getWidth());
		} else if(mx < 0) {
			mx = 0;
		}
		
		if(my > GameActivity.CAMERA_HEIGHT - (playerSprite.getHeight())) {
			my = GameActivity.CAMERA_HEIGHT - (playerSprite.getHeight());
		} else if(my < 0) {
			my = 0;
		}
	}
	
}
