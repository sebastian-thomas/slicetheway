package com.myappstack.gball.actors.startscreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.myappstack.gball.utils.WorldUtils;

public class GameName extends Actor {

	private Sprite spr;
	private Vector2 screenDims;
	private Vector2 oSpriteDims;
	private Vector2 nSpriteDims;
	
	public GameName(Vector2 screenDims){
		this.screenDims = screenDims;
		spr = new Sprite (new Texture(Gdx.files.internal("huball.png")));
		oSpriteDims = new Vector2(spr.getWidth(),spr.getHeight());
		setSize();
		setPosition();
	}
	
	public void setPosition(){
		int x = (int)screenDims.x/8;
		int y = (int)(screenDims.y - 2*screenDims.x/10-nSpriteDims.y);
		spr.setPosition(x, y);
	}
	
	public void setSize(){
		int gNameWid = (int)(3*screenDims.x/4);
		int gNameHei = WorldUtils.getProportionalHeight(gNameWid, oSpriteDims);
		spr.setSize(gNameWid, gNameHei);
		nSpriteDims = new Vector2(gNameWid,gNameHei);
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		spr.draw(batch);
	}

}