package org.ittd.imd.ca;
import ord.ittd.imd.ca.update.Entity;

import org.jbox2d.collision.shapes.PolygonShape;
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
public class Boundary extends Entity
{

	float angle;
	PolygonShape sd;
	
	public Boundary(float x,float y, float w, float h, float a, boolean move,  PBox2D ref,PApplet p) 
	{
		super(x, y, w, h, move, ref, p);
		this.angle = a;
		makeBody();
	}

	public void makeBody()
	{

		//bd = new BodyDef();
		setBodyDef(new BodyDef());
		//Figure out the box2d coordinates
		float box2dW = getBox2d().scalarPixelsToWorld(getWidth()/2);
		float box2dH = getBox2d().scalarPixelsToWorld(getHeight()/2);

		// Define the polygon
		sd = new PolygonShape();
		// We're just a box
		sd.setAsBox(box2dW, box2dH);

		// Create the body
		//BodyDef bd = new BodyDef();

		getBodyDef().angle = angle;
		getBodyDef().position.set(getBox2d().coordPixelsToWorld(getXposition(),getYposition()));


		if(isMove() == false)
		{
			getBodyDef().type = BodyType.STATIC;
		}
		else
		{
			getBodyDef().type = BodyType.DYNAMIC;
		}

		setBody(getBox2d().createBody(getBodyDef()));
		getBody().createFixture(sd,1);

	}

	// Draw the boundary, it doesn't move so we don't have to ask the Body for location
	@SuppressWarnings("static-access")
	public void display() 
	{
		getParent().pushMatrix();
		getParent().fill(198,141,56);
		getParent().stroke(0);
		getParent().strokeWeight(1);
		getParent().rectMode(getParent().CENTER);
		float a = getBody().getAngle();
		getParent().translate(getXposition(),getYposition());

		getParent().rotate(-a);
		getParent().rect(0,0,getWidth(),getHeight());
		getParent().popMatrix();

	}

}