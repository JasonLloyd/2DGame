package org.ittd.imd.ca;
import ord.ittd.imd.ca.update.Entity;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;

import pbox2d.PBox2D;
import processing.core.PApplet;

/**
 * @author found in PBOX2D examples (Windmill) (<http://www.shiffman.net/teaching/nature>)
 * It has been edited for my purpose
 * 
 */
public class SeeSawPart extends Entity
{	  
	 /**
	 * @param x: X Position for see-saw part.
	 * @param y: Y Position for see-saw part.
	 * @param w_: Width for see-saw part.
	 * @param h_: Height for see-saw part.
	 * @param lock: : if the part of the see-saw is locked
	 * @param p:The parent element so we can attach it to our PApplet.
	 * @param b: The box2d element so we can attach it to our physics world.
	 */
	public SeeSawPart(float x, float y, float w, float h, boolean lock,  PBox2D b, PApplet p) 
	{
		super(x, y, w, h, lock, b, p);
		setBodyDef(new BodyDef());
	    makeBody();
	     
	}
	
	 /**
	 * This method is used to display the parts of the see-saw(RECT's)
	 */
	 @SuppressWarnings("static-access")
	public void display() 
	 {
	    // We look at each body and get its screen position
	    Vec2 pos = getBox2d().getBodyPixelCoord(getBody());
	   
	    float a = getBody().getAngle();			// Get its angle of rotation
	    
	    getParent().pushMatrix();
	    
	    getParent().rectMode(getParent().CENTER);
	    
	    getParent().translate(pos.x,pos.y);
	    getParent().rotate(-a);
	    getParent().fill(192,192,192);
	    getParent().stroke(0);
	    getParent().rect(0,0,getWidth(),getHeight());
	    getParent().popMatrix();
	 }

	@SuppressWarnings("static-access")
	@Override
	public void makeBody() 
	{
		if(isMove() == true){}
	    else getBodyDef().angle = getParent().radians(-30);
	    
		getBodyDef().position.set(getBox2d().coordPixelsToWorld(new Vec2(getXposition(),getYposition())));
	    if (isMove()) getBodyDef().type = BodyType.STATIC;
	    else getBodyDef().type = BodyType.DYNAMIC;
	
	    setBody(getBox2d().createBody(getBodyDef()));
	
	    // Define the shape -- a (this is what we use for a rectangle)
	    PolygonShape sd = new PolygonShape();
	    float box2dW = getBox2d().scalarPixelsToWorld(getWidth()/2);
	    float box2dH = getBox2d().scalarPixelsToWorld(getHeight()/2);
	    sd.setAsBox(box2dW, box2dH);
	
	    // Define a fixture
	    FixtureDef fd = new FixtureDef();
	    fd.shape = sd;
	    // Parameters that affect physics
	    fd.density = 1;
	    fd.friction = (float) 0.3;
	    fd.restitution = (float) 0.5;
	
	    getBody().createFixture(fd);
	    // Give it some initial random velocity
	    //body.setLinearVelocity(new Vec2(parent.random(-5,5),parent.random(2,5)));
	    getBody().setAngularVelocity(1);
		
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return null;
	}
}
