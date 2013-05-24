package org.ittd.imd.ca;
import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;

import pbox2d.PBox2D;
import processing.core.PApplet;

	// The Nature of Code
	// <http://www.shiffman.net/teaching/nature>
	// Spring 2012
	// PBox2D example

	// A blob skeleton
	// Could be used to create blobbly characters a la Nokia Friends
	// http://postspectacular.com/work/nokia/friends/start

	// The Nature of Code
	// <http://www.shiffman.net/teaching/nature>
	// Spring 2011
	// PBox2D example

	// A rectangular box
	/**
	 * @author <http://www.shiffman.net/teaching/nature>, Jason Lloyd
	 *
	 * This code has been edited to allow me to use it the way i like.
	 */
	public class Ball 
	{

	  // We need to keep track of a Body and a width and height
	  Body body;
	  float w;
	  float h;
	  float r;
	  
	  Vec2 pos;
	  
	  CircleShape circle;
	  
	  boolean start = false;
	  
	  BodyDef bd;
	  
	  Vec2 offset;
	  
	  PBox2D box2d;
	  
	  PApplet parent;

	  
	  /**
	 * @param x: X position of the ball
	 * @param y: Y position of the ball
	 * @param r: Radius of the ball
	 * @param move: if its going to move or not
	 * @param restitution: how much restitution
	 * @param density: how much density(mass)
	 * @param pReference: our pbox2d reference
	 * @param pAppletRef: our applet reference
	 */
	  public Ball(float x, float y, float r, boolean move,float restitution,float density, PBox2D pReference, PApplet pAppletRef) 
	  {
	    w = 8;
	    h = 24;
	    this.r = r;
	    start = move;
	    box2d = pReference;
	    parent = pAppletRef;
	    // Add the box to the box2d world
	    makeBody(new Vec2(x, y),restitution,density);
	  }

	  // This function removes the particle from the box2d world
	  public void killBody() 
	  {
	    box2d.destroyBody(body);
	  }

	  // Is the particle ready for deletion?
	  public boolean done() 
	  {
	    // Let's find the screen position of the particle
	     pos = box2d.getBodyPixelCoord(body);
	    // Is it off the bottom of the screen?
	    if (pos.y > parent.height+w*h) 
	    {
	      killBody();
	      return true;
	    }
	    return false;
	  }

	  public void display(int r2,int g,int b) 
	  {
	    // We look at each body and get its screen position
	    pos = box2d.getBodyPixelCoord(body);
	    // Get its angle of rotation
	    float a = body.getAngle();

	    parent.pushMatrix();
	    parent.rectMode(parent.CENTER);
	    parent.translate(pos.x, pos.y);
	    parent.rotate(-a);
	    parent.fill(r2,g,b);
	    parent.stroke(0);

	    //rect(0,0,w,h);
	    parent.ellipse(0, h/2, r*2, r*2);
	    parent.popMatrix();
	  }

	  // This function adds the rectangle to the box2d world
	  public void makeBody(Vec2 center, float bouncyness, float density) 
	  {

	    // Define the body and make it from the shape
	    bd = new BodyDef();
	    
	    if(start)
	    	bd.type = BodyType.DYNAMIC;
	    else
	    	bd.type = BodyType.STATIC;
	    
	    
	    bd.position.set(box2d.coordPixelsToWorld(center));
	    body = box2d.createBody(bd);
	    

		circle = new CircleShape();
		circle.m_radius = box2d.scalarPixelsToWorld(r);
		offset = new Vec2(0,h/2);
		offset = box2d.vectorPixelsToWorld(offset);
		circle.m_p.set(offset.x,offset.y);
		    
		    
		FixtureDef fd = new FixtureDef();
		fd.restitution = bouncyness;
		fd.density =density;
		fd.friction = 0.0f;
	
		fd.shape = circle;
	
		body.createFixture(fd);
		body.setFixedRotation(false);

	  }
	  
	}
