package com.myappstack.gball.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.myappstack.gball.utils.Constants;
import com.myappstack.gball.utils.WorldUtils;
import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;

public class BallT extends Actor {

	OrthographicCamera camera;
	
	private int xVal;
	private int yVal;
	
	Vector2 pos;
	Vector2 posScreen;
	Vector2 screenDims;
	private Vector2 direction;
	private boolean inScreen;
	
	private Sprite sprite;
	private Vector2 dims;
	Rectangle bounds;
	Circle bound;
	
	
	public BallT(OrthographicCamera camera,Vector2 direction, int x, int y,String img){
		this.pos = new Vector2(x,y);
		this.camera = camera;
		this.direction = direction.nor();
		this.inScreen = true;
		
		dims = WorldUtils.viewportToScreen(new Vector2(2*Constants.BALL_RADIUS,2*Constants.BALL_RADIUS),camera);
		posScreen = WorldUtils.viewportToScreen(pos, camera);
		screenDims = WorldUtils.viewportToScreen(new Vector2(Constants.VIEWPORT_WIDTH,Constants.VIEWPORT_HEIGHT), camera);
		
		Texture t = new Texture(Gdx.files.internal(img));
		sprite = new Sprite(t);
		sprite.setPosition(posScreen.x, posScreen.y);
		sprite.setSize(dims.x, dims.y);

		bounds = new Rectangle(pos.x,pos.y,2*Constants.BALL_RADIUS,2*Constants.BALL_RADIUS);
		
		//change bound
		bound  = new Circle(pos,dims.x);
	}
	
	public Rectangle getBounds(){
		return bounds;
	}
	
	public void setDirection(Vector2 direction){
		this.direction = direction;
	}
	
	public Vector2 getDirection(){
		return this.direction;
	}
	
	public Vector2 getPos(){
		return pos;
	}
	
	@Override
	public void act(float delta) {
		// pos.add(direction.scl(2));
		//System.out.println(pos.toString());
		
		if((posScreen.x >= 0) && (posScreen.x + dims.x <= screenDims.x) &&
				(posScreen.y >= 0) && (posScreen.y+ dims.y<= screenDims.y)){
			inScreen = true;
		}
		else{
			//System.out.println("Out :"+pos +" ");
			inScreen = true;
		}
		

		if (inScreen) {
			if (posScreen.x <= 0) {
				// hit left edge
				this.pos.x = 0;
				//this.posScreen.x = 0;
				this.direction = WorldUtils.reflect(direction,new Vector2(1, 0));
				inScreen = false;
			} else if (posScreen.x +dims.x >= screenDims.x) {
				// hit right edge
				this.pos.x = Constants.VIEWPORT_WIDTH - 2*Constants.BALL_RADIUS;
				//this.posScreen.x = 
				this.direction = WorldUtils.reflect(direction, new Vector2(-1,0));
				inScreen = false;
			}

			if (posScreen.y <= 0) {
				// hit bottom edge
				this.pos.y = 0;
				this.direction = WorldUtils.reflect(direction,new Vector2(0, 1));
				inScreen = false;
			} else if (posScreen.y + dims.y >= screenDims.y) {
				// hit upper edge
				this.pos.y = Constants.VIEWPORT_HEIGHT -2* Constants.BALL_RADIUS;
				this.direction = WorldUtils.reflect(direction, new Vector2(0,-1));
				inScreen = false;
			}

		}
		
	
		
		pos.add(new Vector2(direction.x* Constants.BALL_SPEED, direction.y*Constants.BALL_SPEED));
		//System.out.println(direction.scl(2));
		posScreen = WorldUtils.viewportToScreen(pos, camera);
		sprite.setPosition(posScreen.x, posScreen.y);
		bounds = new Rectangle(pos.x, pos.y, 2*Constants.BALL_RADIUS, 2*Constants.BALL_RADIUS);
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		sprite.draw(batch);
	}

}
