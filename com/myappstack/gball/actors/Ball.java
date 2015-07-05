package com.myappstack.gball.actors;

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

public class Ball extends Actor{

	World world;
	Body body;
	
	private int xVel;
	private int yVel;
	
	Rectangle bounds;
	
	public Ball(World world){
		this.world = world;
		makeBody();
		body.setLinearVelocity(new Vector2(0,-100));
		Vector2 pos = body.getPosition();
		bounds = new Rectangle(pos.x,pos.y,2*Constants.BALL_RADIUS,2*Constants.BALL_RADIUS);
	}
	
	@Override
	public void act(float delta) {
		Vector2 pos = body.getPosition();
		bounds = new Rectangle(pos.x,pos.y,2*Constants.BALL_RADIUS,2*Constants.BALL_RADIUS);
		//System.out.println(bounds.x+" "+bounds.y);
	}
	
	public Rectangle getBounds(){
		return bounds;
	}
	
	public void makeBody(){
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyDef.BodyType.DynamicBody;
		bodyDef.position.set(new Vector2(Constants.VIEWPORT_WIDTH/2, Constants.VIEWPORT_HEIGHT-2));
		CircleShape shape = new CircleShape();
		shape.setRadius(Constants.BALL_RADIUS);
		MassData md = new MassData();
		md.mass = 10f;
		body = world.createBody(bodyDef);
		body.setMassData(md);
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = shape;
		fixtureDef.density = 1f; 
		fixtureDef.friction = 0f;
		fixtureDef.restitution = 1f; // Make it bounce a little bit
		Fixture fixture = body.createFixture(fixtureDef);
		shape.dispose();
	}
}
