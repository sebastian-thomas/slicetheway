package com.myappstack.gball.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class BgWhiteRep extends Actor{
	
	private Texture bg;
	private Vector2 screenDims,margins;
	private OrthographicCamera camera;
	
	public BgWhiteRep(Vector2 screenDims,Vector2 margins, OrthographicCamera camera){
		this.screenDims = screenDims;
		this.margins = margins;
		this.camera = camera;
		bg = new Texture(Gdx.files.internal("whitebg.png"));
		bg.setWrap(TextureWrap.Repeat, TextureWrap.Repeat); 
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		float bottomY = margins.y; 
		float leftX = margins.y;

		
		float width = screenDims.x - 2*margins.y;
		float height= screenDims.y - margins.x - margins.y;


		//float uRight = width * RATIO / bg.getWidth();
		//float vTop= height * RATIO / bg.getHeight();

		//batch.draw(bg, leftX, bottomY, width, height, 0, 0, uRight, vTop);
		batch.draw(bg, leftX, bottomY, width, height);
	}


}
