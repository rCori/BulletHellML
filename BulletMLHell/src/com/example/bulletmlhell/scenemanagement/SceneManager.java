package com.example.bulletmlhell.scenemanagement;

import java.io.IOException;
import java.io.InputStream;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.bitmap.BitmapTexture;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.texture.region.TextureRegionFactory;
import org.andengine.ui.activity.BaseGameActivity;
import org.andengine.util.adt.io.in.IInputStreamOpener;
import org.andengine.util.debug.Debug;

import com.example.bulletmlhell.BulletImpl;
import com.example.bulletmlhell.BulletManager;
import com.example.bulletmlhell.Enemy;
import com.example.bulletmlhell.GameActivity;
import com.example.bulletmlhell.LevelUI;
import com.example.bulletmlhell.Player;
import com.example.bulletmlhell.PlayerBullet;
import com.example.bulletmlhell.scenemanagement.SceneManager.AllScenes;

import android.content.res.AssetManager;

public class SceneManager implements IUpdateHandler,IOnSceneTouchListener {
	private AllScenes currentSceneType;
	private BaseGameActivity activity;
	private Engine engine;
	private Camera camera;
	private Scene gameScene, splashScene, resultScene;
	private Scene currentBase;
	
	private TextureRegion mBulletTextureRegion, mBackgroundTextureRegion, mPlayerTextureRegion, mEnemyTextureRegion, mPlayerBulletTextureRegion;
	private TextureRegion mSplashScreenTextureRegion;
	
	private Font font;
	private BulletManager bulletManager;
	
	private Enemy boss;
	private Player player;
	private LevelUI levelUI;
	private float currentTime = 0.0f;
	private final float HIT_PENALTY = 20.0f;
	
	private Sprite playerSprite;
	
	//Basic simple logic for switching logic between what patterns is used.
	private float bulletPatternSwitchTimer = 0.0f;
	private final float bulletPatternSwitchTimeLimit = 5.0f;
	private String currentKey = "struggle";
	private float lastEventX = -1.0f;
	private float lastEventY = -1.0f;
	
	public enum AllScenes{
		SPLASH,
		GAME,
		RESULT,
	}
	public SceneManager(BaseGameActivity act, Engine eng, Camera cam) {
		this.activity = act;
		this.engine = eng;
		this.camera = cam;
		this.currentBase = null;
	}
	public AllScenes getCurrentSceneType() {
		return currentSceneType;
	}

	public void loadSplashResources() {
		try {
			ITexture splashScreenTexture = new BitmapTexture(engine.getTextureManager(), new IInputStreamOpener() {
				@Override
				public InputStream open() throws IOException {
					return activity.getAssets().open("splashscreen.png");
				}
			});
			splashScreenTexture.load();
			this.mSplashScreenTextureRegion = TextureRegionFactory.extractFromTexture(splashScreenTexture);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//splashScene = new SplashScene(activity,engine,camera,this);
		//splashScene.LoadSceneResources();
	}

	public void loadGameResources(){
		//gameScene = new GameScene(activity,engine,camera,this);
		//gameScene.LoadSceneResources();
		try{
			ITexture bulletTexture = new BitmapTexture(engine.getTextureManager(), new IInputStreamOpener() {
				@Override
				public InputStream open() throws IOException {
					return activity.getAssets().open("bullet.png");
				}
			});
			ITexture backgroundTexture = new BitmapTexture(engine.getTextureManager(), new IInputStreamOpener() {
				@Override
				public InputStream open() throws IOException {
					return activity.getAssets().open("background.jpg");
				}
			});
			ITexture playerTexture = new BitmapTexture(engine.getTextureManager(), new IInputStreamOpener() {
				@Override
				public InputStream open() throws IOException {
					return activity.getAssets().open("player.png");
				}
			});
			ITexture enemyTexture = new BitmapTexture(engine.getTextureManager(), new IInputStreamOpener() {
				@Override
				public InputStream open() throws IOException {
					return activity.getAssets().open("boss1.png");
				}
			});
			ITexture playerBulletTexture = new BitmapTexture(engine.getTextureManager(), new IInputStreamOpener() {
				@Override
				public InputStream open() throws IOException {
					return activity.getAssets().open("playerBullet.png");
				}
			});
			bulletTexture.load();
			backgroundTexture.load();
			playerTexture.load();
			enemyTexture.load();
			playerBulletTexture.load();
			this.mBackgroundTextureRegion = TextureRegionFactory.extractFromTexture(backgroundTexture);
			this.mBulletTextureRegion = TextureRegionFactory.extractFromTexture(bulletTexture);
			this.mPlayerTextureRegion = TextureRegionFactory.extractFromTexture(playerTexture);
			this.mEnemyTextureRegion = TextureRegionFactory.extractFromTexture(enemyTexture);
			this.mPlayerBulletTextureRegion = TextureRegionFactory.extractFromTexture(playerBulletTexture);
			font = FontFactory.createFromAsset(activity.getEngine().getFontManager(), activity.getEngine().getTextureManager(), 256, 256, activity.getAssets(),
				    "fonts/Retro_Computer_DEMO.ttf", 24, true, android.graphics.Color.WHITE);
			font.load();
		} catch(IOException e) {
			Debug.e(e);
		}
	}
	
	public void loadResultResources() throws IOException{
		try{
			ITexture backgroundTexture = new BitmapTexture(engine.getTextureManager(), new IInputStreamOpener() {
				@Override
				public InputStream open() throws IOException {
					return activity.getAssets().open("background.jpg");
				}
			});
			backgroundTexture.load();
			this.mBackgroundTextureRegion = TextureRegionFactory.extractFromTexture(backgroundTexture);
			font = FontFactory.createFromAsset(activity.getEngine().getFontManager(), activity.getEngine().getTextureManager(), 256, 256, activity.getAssets(),
				    "fonts/Retro_Computer_DEMO.ttf", 30, true, android.graphics.Color.WHITE);
			font.load();
		} catch(IOException e) {
			Debug.e(e);
		}
	}
	
	public Scene createSplashScene() {
		splashScene = new Scene();
		Sprite icon = new Sprite(0,0,this.mSplashScreenTextureRegion, activity.getVertexBufferObjectManager());
		splashScene.attachChild(icon);
		return splashScene;
	}
	
	public Scene createGameScene() {
		//gameScene.CreateScene();
		//return gameScene;
		gameScene = new Scene();
		Sprite backgroundSprite = new Sprite(0,0, this.mBackgroundTextureRegion, activity.getVertexBufferObjectManager());
		gameScene.attachChild(backgroundSprite);
		
		bulletManager = new BulletManager(mBulletTextureRegion);
		bulletManager.initBullets();
		AssetManager aManager = activity.getApplicationContext().getAssets();
		try {
	        InputStream is = aManager.open("bulletPatterns/[Progear]_round_2_boss_struggling.xml");
	        int length = is.available();
	        byte[] data = new byte[length];
	        is.read(data);
	        String demoInput = new String(data).toString();
	        bulletManager.loadBulletML(demoInput,"struggle");
	        
	        is = aManager.open("bulletPatterns/[Progear]_round_1_boss_grow_bullets.xml");
	        length = is.available();
	        data = new byte[length];
	        is.read(data);
	        demoInput = new String(data).toString();
	        bulletManager.loadBulletML(demoInput,"grow_bullets");
	        
		} catch (IOException io) {
			io.printStackTrace();
		}
	    bulletManager.setHVStat(0);
	    bulletManager.addBullets("struggle");
	    
	    BulletImpl[] bullets = bulletManager.getAllBullets();
	    for(BulletImpl bullet : bullets) {
	    	Sprite bulletSprite = new Sprite(bullet.px,bullet.py,this.mBulletTextureRegion, activity.getVertexBufferObjectManager());
	    	gameScene.attachChild(bulletSprite);
		    bullet.SetSprite(bulletSprite);
	    }
	    
	   	bulletManager.startGame();
	    gameScene.registerUpdateHandler(this);
	    gameScene.setOnSceneTouchListener(this);
	    
	    final float centerX = (GameActivity.CAMERA_WIDTH - this.mPlayerTextureRegion.getWidth()) / 2;
		final float centerY = (GameActivity.CAMERA_HEIGHT - this.mPlayerTextureRegion.getHeight()) / 2;
		player = new Player();
	    playerSprite = new Sprite(centerX, centerY, this.mPlayerTextureRegion, activity.getVertexBufferObjectManager());
	    gameScene.attachChild(playerSprite);
	    player.SetSprite(playerSprite);
	    player.SetPosition(centerX, centerY);
	    
	    for(PlayerBullet playerBullet : player.playerBullets) {
	    	Sprite playerBulletSprite = new Sprite(playerBullet.mx, playerBullet.my, this.mPlayerBulletTextureRegion, activity.getVertexBufferObjectManager());
	    	gameScene.attachChild(playerBulletSprite);
	    	playerBullet.SetSprite(playerBulletSprite);
	    }
	    
	    Sprite enemySprite = new Sprite(GameActivity.CAMERA_WIDTH/2 - this.mEnemyTextureRegion.getWidth()/2,70f, this.mEnemyTextureRegion, activity.getVertexBufferObjectManager());
	    boss = new Enemy(this.mEnemyTextureRegion);
	    boss.SetSprite(enemySprite);
	    gameScene.attachChild(enemySprite);
	    
	    
	    levelUI = new LevelUI();
	    Text timeElapsedText = new Text(50f,50f,font, "Time: 1234567890", activity.getVertexBufferObjectManager());
	    Text enemyHealthRemaining = new Text(0f,20f,font, "Health: 1234567890", activity.getVertexBufferObjectManager());
	    levelUI.SetTimeElapsedText(timeElapsedText);
	    levelUI.SetEnemyHealthRemainingText(enemyHealthRemaining);
	    gameScene.attachChild(enemyHealthRemaining);
	    gameScene.attachChild(timeElapsedText);
		return gameScene;
	}
	
	public Scene createResultScene() {
		resultScene = new Scene();
		Sprite backgroundSprite = new Sprite(0,0, this.mBackgroundTextureRegion, activity.getVertexBufferObjectManager());
		resultScene.attachChild(backgroundSprite);
		
		Text timeElapsedText = new Text(80f,200f,font, "Your Time: " + Math.round(currentTime*100.0)/100.0 , activity.getVertexBufferObjectManager());
		resultScene.attachChild(timeElapsedText);
		
		Text restartText = new Text(130f,400f,font, "Tap screen\n to restart" , activity.getVertexBufferObjectManager());
		resultScene.attachChild(restartText);
		
		currentTime = 0.0f;
		resultScene.setOnSceneTouchListener(this);
		
		return resultScene;
	}
	
	public Scene GetCurrentBaseScene() {
		return currentBase;
	}
	
	public void setCurrentScene(AllScenes currentScene) {
		this.currentSceneType = currentScene;
		switch(currentScene){
		case SPLASH:
			engine.setScene(splashScene);
			currentBase = splashScene;
			break;
		case GAME:
			engine.setScene(gameScene);
			currentBase = gameScene;
			break;
		case RESULT:
			engine.setScene(resultScene);
			currentBase = resultScene;
			break;
		default:
			break;
		}
	}
	@Override
	public void onUpdate(float pSecondsElapsed) {
		switch(currentSceneType){
		case GAME:
			if(bulletManager != null) {
	        	bulletManager.update(pSecondsElapsed,currentKey);
	        	if(player != null && !player.GetIsHit()) {
	        		boolean isHit = bulletManager.updateBulletCollision(player);
	        		if(isHit) {
	        			player.TakeHit();
	        			currentTime += HIT_PENALTY;
	        		}
	        	}
	        }
	        bulletPatternSwitchTimer += pSecondsElapsed;
	        if(bulletPatternSwitchTimer > bulletPatternSwitchTimeLimit) {
	        	bulletPatternSwitchTimer = 0.0f;
	        	if(currentKey == "struggle" ) {
	        		currentKey = "grow_bullets";
	        	} else {
	        		currentKey = "struggle";
	        	}
	        }
	        if(boss != null && player != null) {
	        	
	        	for(PlayerBullet bullet : player.playerBullets) {
	        		boss.DamageUpdate(bullet, player);
	        	}
	        	player.PlayerUpdate(pSecondsElapsed);
	        	player.PlayerDraw();
	        	if(boss.GetHealth() <= 0) {
					try {
						loadResultResources();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					createResultScene();
					setCurrentScene(AllScenes.RESULT);
	        	}
	        }
	        currentTime += pSecondsElapsed;
	        if(levelUI != null) {
	        	levelUI.UpdateLevelUI(currentTime, boss.GetHealth());
	        }
		case RESULT:
			break;
		case SPLASH:
			break;
		default:
			break;
		}
		
	}
	@Override
	public void reset() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onSceneTouchEvent(Scene pScene, org.andengine.input.touch.TouchEvent pSceneTouchEvent) {
		switch(currentSceneType){
		case GAME:
			if (pSceneTouchEvent.isActionMove()) {
				if (lastEventX != -1.0f && bulletManager != null) {
					float X = playerSprite.getX() + (pSceneTouchEvent.getX() - lastEventX );
					float Y = playerSprite.getY() + (pSceneTouchEvent.getY() - lastEventY );
					bulletManager.setPlayerPos((int) X, (int) Y);
					bulletManager.movePlayer();
					playerSprite.setPosition(X, Y);
					if(player != null) {
						player.mx = X;
						player.my = Y;
						player.MovementConstraint();
					}
				}
				lastEventX = pSceneTouchEvent.getX();
				lastEventY = pSceneTouchEvent.getY();
		    } else if(pSceneTouchEvent.isActionUp()) {
				lastEventX = -1.0f;
				lastEventY = -1.0f;
		    }
			break;
		case RESULT:
			if (pSceneTouchEvent.isActionDown()) {
				loadGameResources();
				createGameScene();
				setCurrentScene(AllScenes.GAME);
			}
			break;
		case SPLASH:
			break;
		default:
			break;
		}
		
		return false;
	}
}
