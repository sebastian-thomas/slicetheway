package com.myappstack.gball.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.MassData;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.myappstack.gball.utils.Constants;
import com.myappstack.gball.utils.WorldUtils;

public class Food extends Actor{
	
	

	World world;
	Body body;
	OrthographicCamera camera;
	
	private int xVal;
	private int yVal;
	private Sprite sprite;
	Vector2 dims;
	
	Rectangle bounds;
	Vector2 pos;
	
	public Food(World world,OrthographicCamera camera, int x, int y){
		this.xVal = x;
		this.yVal = y;
		this.camera = camera;
		
		dims = WorldUtils.viewportToScreen(new Vector2(Constants.FOOD_WIDTH, Constants.FOOD_HEIGHT),camera);
		pos = WorldUtils.viewportToScreen(new Vector2(x,y), camera);
		
		Texture t = new Texture(Gdx.files.internal("yellows.png"));
		sprite = new Sprite(t);
		sprite.setPosition(pos.x, pos.y);
		sprite.setSize(dims.x, dims.y);
		//this.world = world;
		
		//makeBody();
		//Vector2 pos = body.getPosition();
		bounds = new Rectangle(x,y,Constants.FOOD_WIDTH,Constants.FOOD_HEIGHT);
	}
	
	public Rectangle getBounds(){
		return bounds;
	}
	
	public void changePos(int xVal, int yVal){
		this.xVal = xVal;
		this.yVal = yVal;
		
		pos = WorldUtils.viewportToScreen(new Vector2(this.xVal,this.yVal), camera);
		sprite.setPosition(pos.x, pos.y);
		bounds.set(this.xVal,this.yVal,Constants.FOOD_WIDTH,Constants.FOOD_HEIGHT);
	}
	
	public void makeBody(){
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyDef.BodyType.StaticBody;
		bodyDef.position.set(new Vector2(xVal, yVal));
		CircleShape shape = new CircleShape();
		shape.setRadius(Constants.BALL_RADIUS);
		body = world.createBody(bodyDef);
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = shape;
		fixtureDef.density = 1f; 
		fixtureDef.friction = 0f;
		fixtureDef.restitution = 1f; // Make it bounce a little bit
		Fixture fixture = body.createFixture(fixtureDef);
		shape.dispose();
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		sprite.draw(batch);
	}

}
