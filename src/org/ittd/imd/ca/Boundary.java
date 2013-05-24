package org.ittd.imd.ca;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;

import pbox2d.PBox2D;
import processing.core.PApplet;



/**
 * @author Jason Lloyd
 * 
 * This is a modification of the SeeSawPart class, its a smaller version that allows us to create
 * boundaries for the level and add structures in our level.
 *
 */
public class Boundary 
{

		  // A boundary is a simple rectangle with x,y,width,and height
		  float x;
		  float y;
		  float w;
		  float h;

		  Body body;
		  
		  PolygonShape sd;
		  
		  BodyDef bd;
		  
		  PBox2D box2d;
		  
		  PApplet parent;

		 Boundary(float x_,float y_, float w_, float h_, float a, boolean move, PApplet p, PBox2D ref) 
		 {
		    x = x_;
		    y = y_;
		    w = w_;
		    h = h_;
		    parent = p;
		    box2d = ref;

		    // Define the polygon
		     sd = new PolygonShape();
		    // Figure out the box2d coordinates
		    float box2dW = ref.scalarPixelsToWorld(w/2);
		    float box2dH = ref.scalarPixelsToWorld(h/2);
		    // We're just a box
		    sd.setAsBox(box2dW, box2dH);


		    // Create the body
		    BodyDef bd = new BodyDef();
		    
		    bd.angle = a;
		    bd.position.set(ref.coordPixelsToWorld(x,y));
		    
		    
		    if(move == false)
		    {
		    	bd.type = BodyType.STATIC;
		    }
		    else
		    {
		    	bd.type = BodyType.DYNAMIC;
		    }
		
		    body = ref.createBody(bd);
	    	//b.createFixture(sd,1);
	    	body.createFixture(sd,1);
		    
		   // body.createFixture(sd,1);
		  }
		 
		 // Remove the body (if we dont there will be ghost shapes)
		 void killBody() 
		 {
		    box2d.destroyBody(body);
		 }

		  // Draw the boundary, it doesn't move so we don't have to ask the Body for location
		  void display() 
		  {
			  parent.pushMatrix();
			  parent.fill(198,141,56);
			  parent.stroke(0);
			  parent.strokeWeight(1);
			  parent.rectMode(parent.CENTER);
		    float a = body.getAngle();
		    parent.translate(x,y);
		   
		    parent.rotate(-a);
		    parent.rect(0,0,w,h);
		    parent.popMatrix();
		  
		  }
		  
		}