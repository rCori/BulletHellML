package com.example.bulletmlhell;

import org.andengine.entity.text.Text;

public class LevelUI {
	private float timeElapsed;
	private float enemyHealthRemaining;
	
	private Text timeElapsedText;
	private Text enemyHealthRemainingText;
	
	public LevelUI() {
		timeElapsed = 0.0f;
		enemyHealthRemaining = 0.0f;
	}
	
	public LevelUI(float enemyHealth) {
		timeElapsed = 0.0f;
		enemyHealthRemaining = enemyHealth;
	}
	
	public void SetTimeElapsedText(Text timeElapsedText) {
		this.timeElapsedText = timeElapsedText;
	}
	
	public void SetEnemyHealthRemainingText(Text enemyHealthRemainingText) {
		this.enemyHealthRemainingText = enemyHealthRemainingText;
	}
	
	
	public void UpdateLevelUI(float timeElapsed, float enemyHealthRemaining) {
		UpdateTimeElapsedUI(timeElapsed);
		UpdateEnemyHealth(enemyHealthRemaining);
		
		
	}
	
	public void EnemyHitUIUpdate(float damage) {
		this.enemyHealthRemaining -= damage;
	}
	
	private void UpdateTimeElapsedUI(float timeElapsed) {
		this.timeElapsed = timeElapsed;
		if(this.timeElapsedText != null) {
			this.timeElapsedText.setText("Time: " + timeElapsed);
		}
	}
	
	private void UpdateEnemyHealth(float enemyHealthRemaining) {
		this.enemyHealthRemaining = enemyHealthRemaining;
		if(this.enemyHealthRemainingText != null) {
			this.enemyHealthRemainingText.setText("Health: " + enemyHealthRemaining);
		}
	}
	
}
