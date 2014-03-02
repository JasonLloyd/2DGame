package org.ittd.imd.ca;
import java.io.File;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import javax.xml.namespace.QName;

import ord.ittd.imd.ca.update.Entity;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;

import pbox2d.PBox2D;
import processing.core.PApplet;

/**
 	The Nature of Code
	 <http://www.shiffman.net/teaching/nature>
 Spring 2012
	PBox2D example

	A blob skeleton
	 Could be used to create blobbly characters a la Nokia Friends
	 http://postspectacular.com/work/nokia/friends/start

	The Nature of Code
	 <http://www.shiffman.net/teaching/nature>
	 Spring 2011
	 PBox2D example

	 A rectangular box**/
/**
 * @author <http://www.shiffman.net/teaching/nature>, Jason Lloyd
 *
 * This code has been edited to allow me to use it the way i like.
 */
public class Ball extends Entity
{
	@XmlElement(name="radius")
	private float radius;
	@XmlElement(name="restitution")
	private float restitution;
	@XmlElement(name="density")
	private float density;
	@XmlAttribute
	private String type = "Ball";
	@XmlTransient
	File xml = new File("C:\\Users\\Jay\\Desktop\\test.xml");
	Vec2 pos;

	CircleShape circle;

	Vec2 offset;

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

	public Ball(){}
	public Ball(float x, float y, float r, boolean move,float restitution,float density, PBox2D pReference)
	{
		super(x, y, 8, 24, move);
	}
	public Ball(float x, float y, float r, boolean move,float restitution,float density, PBox2D pReference, PApplet pAppletRef) 
	{
		super(x, y, 8, 24, move, pReference, pAppletRef);
		this.radius = r;
		this.restitution = restitution;
		this.density = density;

		// Add the box to the box2d world
		makeBody(new Vec2(x, y),restitution,density);

		writeAsXML();

	}

	public void writeAsXML()
	{
		Ball b = new Ball(0, 0, 0, false, 0, 0, getBox2d());
		JAXBContext jaxbCtx = null;
		StringWriter xmlWriter = null;

		//QName qName = new QName("test", "Ball");
		//JAXBElement<Ball> root = new JAXBElement<Ball>(qName, Ball.class, b);

		System.out.println("ici");
		try {
			//XML Binding code using JAXB

			jaxbCtx = JAXBContext.newInstance(Entity.class);
			xmlWriter = new StringWriter();
			
			jaxbCtx.createMarshaller().setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			jaxbCtx.createMarshaller().marshal(b, xmlWriter);
			System.out.println("XML Marshal example in Java");
			System.out.println(xmlWriter);

			//Ball b2 = (Ball) jaxbCtx.createUnmarshaller().unmarshal(xml);
			//System.out.println("XML Unmarshal example in JAva");
			//System.out.println(b2.toString());
		} catch (JAXBException ex) 
		{
			ex.printStackTrace();
		}
	}

	@SuppressWarnings("static-access")
	public void display(int r,int g,int b) 
	{
		// We look at each body and get its screen position
		pos = getBox2d().getBodyPixelCoord(getBody());
		// Get its angle of rotation
		float a = getBody().getAngle();

		getParent().pushMatrix();
		getParent().rectMode(getParent().CENTER);
		getParent().translate(pos.x, pos.y);
		getParent().rotate(-a);
		getParent().fill(r,g,b);
		getParent().stroke(1);

		//rect(0,0,w,h);
		getParent().ellipse(0, getHeight()/2, radius*2, radius*2);
		getParent().popMatrix();
	}

	// This function adds the rectangle to the box2d world
	public void makeBody(Vec2 center, float bouncyness, float density) 
	{

		// Define the body and make it from the shape
		setBodyDef(new BodyDef());

		if(isMove())
			getBodyDef().type = BodyType.DYNAMIC;
		else
			getBodyDef().type = BodyType.STATIC;


		getBodyDef().position.set(getBox2d().coordPixelsToWorld(center));
		setBody(getBox2d().createBody(getBodyDef()));


		circle = new CircleShape();
		circle.m_radius = getBox2d().scalarPixelsToWorld(radius);
		offset = new Vec2(0,getHeight()/2);
		offset = getBox2d().vectorPixelsToWorld(offset);
		circle.m_p.set(offset.x,offset.y);


		FixtureDef fd = new FixtureDef();
		fd.restitution = bouncyness;
		fd.density =density;
		fd.friction = 0.0f;

		fd.shape = circle;

		getBody().createFixture(fd);
		getBody().setFixedRotation(false);


	}

	public boolean readyForDeletion() {
		// Let's find the screen position of the particle
		pos = getBox2d().getBodyPixelCoord(getBody());
		// Is it off the bottom of the screen?
		if (pos.y > getParent().height+getWidth()*getHeight()) 
		{
			killBody();
			return true;
		}
		return false;
	}

	@Override
	public void makeBody() {
		// TODO Auto-generated method stub

	}

	@Override
	public void display() {
		// TODO Auto-generated method stub

	}
	@Override
	public String toString() {
		return "Ball{" + "x= " + getXposition() + "y= " + getYposition() 
				+ "width=" + getWidth() + "height= " + getHeight() + "move=" + isMove()
				+ "radius=" + radius + "restitution=" + restitution + "density=" + density;
	}

}
