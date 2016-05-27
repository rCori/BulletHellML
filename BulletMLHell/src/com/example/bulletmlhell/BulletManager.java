/*
 * $Id: GameManager.java,v 1.5 2001/06/03 00:19:12 ChoK Exp $
 *
 * Copyright 2001 Kenta Cho. All rights reserved.
 */
package com.example.bulletmlhell;

import jp.gr.java_conf.abagames.bulletml.*;
import jp.gr.java_conf.abagames.util.*;
import java.io.*;
import java.util.Vector;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Random;
//import java.awt.TextArea;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXParseException;

import android.util.Log;

import org.andengine.opengl.texture.region.TextureRegion;
import org.w3c.dom.Document;

/**
 * Handle game status.
 *
 * @version $Revision: 1.5 $
 */
public class BulletManager {

	private final int BULLET_NOT_EXIST = BulletImpl.NOT_EXIST;

	private final int BULLET_MAX = 256;
	private BulletImpl[] bullet = new BulletImpl[BULLET_MAX];
	private int bltIdx = 0;

	private final int ACTION_MAX = 1024;
	private ActionImpl[] action = new ActionImpl[ACTION_MAX];
	private int actIdx = 0;
	private TextureRegion mBulletTextureRegion;

	private float timer = 0f;
	private final float BULLET_RESET_TIMER = 3.0f;
	

	private Hashtable<String,IActionElmChoice[]> topActionTable;
	
	public BulletManager(TextureRegion bulletTextureRegion) {
		bulletTextureRegion = mBulletTextureRegion;
		topActionTable = new Hashtable<String,IActionElmChoice[]>();
		
	}

	public void initBullets() {
		for (int i = 0; i < bullet.length; i++) {
			bullet[i] = new BulletImpl(mBulletTextureRegion, this);
		}
		for (int i = 0; i < action.length; i++) {
			action[i] = new ActionImpl(this);
		}
	}

	private void resetBullets() {
		int i;
		for (i = 0; i < BULLET_MAX; i++) {
			bullet[i].x = BULLET_NOT_EXIST;
		}
	}

	public BulletImpl getBulletImplInstance() {
		for (int i = BULLET_MAX - 1; i >= 0; i--) {
			bltIdx++;
			bltIdx &= (BULLET_MAX - 1);
			if (bullet[bltIdx].x == BULLET_NOT_EXIST) {
				return bullet[bltIdx];
			}
		}
		return null;
	}

	public BulletImpl[] getAllBullets() {		
		return bullet;
	}
	
	public ActionImpl getActionImplInstance() {
		for (int i = ACTION_MAX - 1; i >= 0; i--) {
			actIdx++;
			actIdx &= (ACTION_MAX - 1);
			if (action[actIdx].pc == ActionImpl.NOT_EXIST) {
				return action[actIdx];
			}
		}
		return null;
	}

	// ported from Palm

	public int xp, yp;
	private int pxp, pyp;

	void startGame() {
		Log.i("screen", "startGame");
		xp = playerPosX;
		yp = playerPosY;
		pxp = xp + 1;
		pyp = yp;
	}

	private final int CLS_WIDTH = 32;
	private final int PEN_COLOR = 0xffddbb;

	// BulletML handler.

	
	public final void movePlayer() {
		xp = playerPosX;
		yp = playerPosY;
	}
	
	public void loadBulletML(String document, String patternName) {
		try {
			ErrorHandler errorHandler = new ErrorHandler() {
				public void error(SAXParseException e) {
					Log.e("loading", "error:" + e.getLocalizedMessage() );
				}

				public void fatalError(SAXParseException e) {
					Log.e("loading", "fatal error:" + e.getLocalizedMessage() );
				}

				public void warning(SAXParseException e) {
					Log.e("loading", "Parsing warning: " + e.getLocalizedMessage());
				}
			};
			
			Document doc = UJAXP.getValidDocument(
					new StringReader(document), errorHandler);
			Bulletml bulletML = new Bulletml(doc);
			String type = bulletML.getType();

			IBulletmlChoice[] bmc = bulletML.getContent();
			Vector aecVct = new Vector();
			//BulletmlNoizUtil.clear();
			for (int i = 0; i < bmc.length; i++) {
				IBulletmlChoice be = bmc[i];
				if (be instanceof Action) {
					Action act = (Action) be;
					if (act.getLabel().startsWith("top")) {
						aecVct.addElement(act);
					}
					BulletmlNoizUtil.addAction(act);
				} else if (be instanceof Bullet) {
					BulletmlNoizUtil.addBullet((Bullet) be);
				} else if (be instanceof Fire) {
					BulletmlNoizUtil.addFire((Fire) be);
				}
			}
			IActionElmChoice[] topAction = new IActionElmChoice[aecVct.size()];
			aecVct.copyInto(topAction);
			topActionTable.put(patternName, topAction);
		} catch (Exception e) {
			System.out.println("loading error: " + e.getLocalizedMessage());
			e.printStackTrace();
		}
	}

	public int hvStat;

	public void setHVStat(int hvs) {
		hvStat = hvs;
	}

	private BulletImpl topBullet;
	private Random rnd = new Random();

	public void addBullets(String name) {
		if (topBullet != null && topBullet.x != BulletImpl.NOT_EXIST && !topBullet.isAllActionFinished())
			return;
		topBullet = getBulletImplInstance();
		if (topBullet == null)
			return;
		topBullet.set(topActionTable.get(name), GameActivity.CAMERA_WIDTH/2, 70f , 0);
		topBullet.speed = 0;
		topBullet.direction = 0;
	}

	// java lifecycle management

	public int playerPosX, playerPosY;

	public final void setPlayerPos(int x, int y) {
	  playerPosX = x; playerPosY = y;
	}
	
	private final int INTERVAL = 16;

	private Timer timerTrd;

	public void init() {

	}

	private void moveBullets(float factor) {
		for (int i = BULLET_MAX - 1; i >= 0; i--) {
			if (bullet[i].x != BULLET_NOT_EXIST) {
				bullet[i].move(factor);
			}
		}
	}

	private void drawBullets() {
		for (int i = BULLET_MAX - 1; i >= 0; i--) {
			if (bullet[i].x != BULLET_NOT_EXIST) {
				bullet[i].draw();
			}
		}
	}

	
	public void update(float elapsedSeconds, String patternName){
		timer += elapsedSeconds;
		if(timer > BULLET_RESET_TIMER) {
			addBullets(patternName);
			timer = 0;
		}
		
		moveBullets(elapsedSeconds);
		drawBullets();
	}
	
	public void update(float elapsedSeconds, String[] patternNames){
		timer += elapsedSeconds;
		if(timer > BULLET_RESET_TIMER) {
			for(String patternName : patternNames) {
				addBullets(patternName);
			}
			timer = 0;
		}
		
		moveBullets(elapsedSeconds);
		drawBullets();
	}
	
	public boolean updateBulletCollision(Player player) {
		for (int i = BULLET_MAX - 1; i >= 0; i--) {
			if (bullet[i].x != BULLET_NOT_EXIST) {
				if(bullet[i].TestCollision(player)) {
					return true;
				}
			}
		}
		return false;
	}
	
}
