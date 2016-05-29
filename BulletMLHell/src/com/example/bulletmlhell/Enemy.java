package com.example.bulletmlhell;

import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.TextureRegion;

import android.util.Log;

/**
 * Enemy boss that serves as origin of bullets
 *
 * @author Ryan Cori
 * @since 5/21/16
 */

public class Enemy {
	private TextureRegion enemyTextureRegion;
	private Sprite enemySprite;
	public float mx, my;
	private float health;
	private float width, height;
	private float maxDistance;
	public Enemy(TextureRegion textureRegion) {
		textureRegion = enemyTextureRegion;
		maxDistance = (float) Math.sqrt(Math.pow(GameActivity.CAMERA_HEIGHT, 2.0f) + Math.pow(GameActivity.CAMERA_WIDTH, 2.0f));
	}
	
	public Sprite GetSprite() {
		return enemySprite;
	}
	
	public void SetSprite(Sprite enemySprite){
		 this.enemySprite = enemySprite;
		 this.width = enemySprite.getWidth();
		 this.height = enemySprite.getHeight();
		 this.mx = enemySprite.getX();
		 this.my = enemySprite.getY();
		 health = 8000;
	}
	
	public float GetHealth() {
		return health;
	}
	
	public void GetHealth(float health) {
		this.health = health;
	}
	
	/**
	* Needs to be called in main activity's Update loop
	*
	*/
	public void DamageUpdate(PlayerBullet bullet, Player player) {
		if(bullet == null || !bullet.GetExists()) {
			return;
		}
		if(TestHit(bullet)) {
			float damageFactor = CalculateDamageFactor(player);
			TakeDamage(damageFactor);
			bullet.Vanish();
		}
	}
	
	private void TakeDamage(float damageFactor){
		Log.i("bullet", "damage: " + damageFactor);
		health -= damageFactor;
	}
	
	private float CalculateDamageFactor(Player player){
		float xDiff = player.mx - mx;
		float yDiff = player.my - my;
		float magnitude = (float) Math.sqrt(Math.pow(xDiff, 2.0f) + Math.pow(yDiff, 2.0f));
		
		//Later we may do some calculation to get the damageFactor. This is fine for now
		float diff = maxDistance - magnitude;
		diff *= 0.2;
		float damageFactor = diff;
		
		return damageFactor;
	}
	
	private boolean TestHit(PlayerBullet bullet){
		if(bullet.mx >= mx && bullet.mx <= mx + width && 
				bullet.my >= my && bullet.my <= my + height) {
			return true;
		}
		return false;
	}
	
}
