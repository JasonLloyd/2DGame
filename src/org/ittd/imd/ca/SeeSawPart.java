package org.ittd.imd.ca;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
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
public class SeeSawPart 
{

	  // We need to keep track of a Body and a width and height
	  Body body;
	  float w;
	  float h;
	
	  PApplet parent;	// Variable to hold the parent element 
	  
	  PBox2D box2d;		// holds the box2d world
	  
	  BodyDef bd;
	  
	 /**
	 * @param x: X Position for see-saw part.
	 * @param y: Y Position for see-saw part.
	 * @param w_: Width for see-saw part.
	 * @param h_: Height for see-saw part.
	 * @param lock: : if the part of the see-saw is locked
	 * @param p:The parent element so we can attach it to our PApplet.
	 * @param b: The box2d element so we can attach it to our physics world.
	 */
	public SeeSawPart(float x, float y, float w_, float h_, boolean lock, PApplet p, PBox2D b) 
	{
	    w = w_;
	    h = h_;
	    parent = p;
	    box2d = b;
	    // Define and create the body
	     bd = new BodyDef();
	    
	    if(lock == true){}
	    else bd.angle = parent.radians(-30);
	    
	    bd.position.set(box2d.coordPixelsToWorld(new Vec2(x,y)));
	    if (lock) bd.type = BodyType.STATIC;
	    else bd.type = BodyType.DYNAMIC;
	
	    body = box2d.createBody(bd);
	
	    // Define the shape -- a (this is what we use for a rectangle)
	    PolygonShape sd = new PolygonShape();
	    float box2dW = box2d.scalarPixelsToWorld(w/2);
	    float box2dH = box2d.scalarPixelsToWorld(h/2);
	    sd.setAsBox(box2dW, box2dH);
	
	    // Define a fixture
	    FixtureDef fd = new FixtureDef();
	    fd.shape = sd;
	    // Parameters that affect physics
	    fd.density = 1;
	    fd.friction = (float) 0.3;
	    fd.restitution = (float) 0.5;
	
	    body.createFixture(fd);
	
	    // Give it some initial random velocity
	    //body.setLinearVelocity(new Vec2(parent.random(-5,5),parent.random(2,5)));
	    body.setAngularVelocity(1);
	  }
	
	 /**
	 * This function removes the particle from the box2d world
	 */
	 void killBody() 
	 {
	    box2d.destroyBody(body);
	 }
	
	 /**
	 * This method is used to display the parts of the see-saw(RECT's)
	 */
	 void display() 
	 {
	    // We look at each body and get its screen position
	    Vec2 pos = box2d.getBodyPixelCoord(body);
	   
	    float a = body.getAngle();			// Get its angle of rotation
	    
	    parent.pushMatrix();
	    
	    parent.rectMode(parent.CENTER);
	    
	    parent.translate(pos.x,pos.y);
	    parent.rotate(-a);
	    parent.fill(192,192,192);
	    parent.stroke(0);
	    parent.rect(0,0,w,h);
	    parent.popMatrix();
	 }
}
