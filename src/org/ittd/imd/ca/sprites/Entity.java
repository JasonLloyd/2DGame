package org.ittd.imd.ca.sprites;

import javax.xml.bind.annotation.*;

import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;

import pbox2d.PBox2D;
import processing.core.PApplet;


/**
 * <p>Java class for BaseEntity complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="BaseEntity">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="xposition" type="{http://www.w3.org/2001/XMLSchema}float"/>
 *         &lt;element name="yposition" type="{http://www.w3.org/2001/XMLSchema}float"/>
 *         &lt;element name="width" type="{http://www.w3.org/2001/XMLSchema}float"/>
 *         &lt;element name="height" type="{http://www.w3.org/2001/XMLSchema}float"/>
 *         &lt;element name="move" type="{http://www.w3.org/2001/XMLSchema}float"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BaseEntity", propOrder = {
    "xposition",
    "yposition",
    "width",
    "height",
    "move"
})
@XmlSeeAlso({
    SeeSawPart.class,
    Boundary.class,
    Ball.class
})

public abstract class Entity 
{

	private float xposition;
	
	private float yposition;
	
	private float width;
	
	private float height;
	
	private boolean move;

	
	@XmlTransient
	private Body body;
	@XmlTransient
	private BodyDef bodyDef;
	@XmlTransient
	private PBox2D box2d;
	@XmlTransient
	private  PApplet parent;
	
	
	public Entity(){}
	public Entity(float x, float y, float w, float h, boolean m)
	{
		this.xposition = x;
		this.yposition = y;
		this.width = w;
		this.height = h;
		this.move = m;
	}

	public Entity(float x, float y, float w, float h, boolean m, PBox2D b, PApplet child) 
	{
		this.xposition = x;
		this.yposition = y;
		this.width = w;
		this.height = h;
		this.move = m;
		this.box2d = b;
		this.parent = child;
		
	}
	
	public abstract String toString();
	public abstract void display();
	public abstract void makeBody();

	public void killBody()
	{
		getBox2d().destroyBody(getBody());
	}
	
	public float getXposition() {
		return xposition;
	}

	public void setXposition(float xposition) {
		this.xposition = xposition;
	}

	public float getYposition() {
		return yposition;
	}

	public void setYposition(float yposition) {
		this.yposition = yposition;
	}

	public float getWidth() {
		return width;
	}

	public void setWidth(float width) {
		this.width = width;
	}

	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
	}

	public boolean isMove() {
		return move;
	}

	public void setMove(boolean move) {
		this.move = move;
	}

	public Body getBody() {
		return body;
	}

	public void setBody(Body body) {
		this.body = body;
	}

	public BodyDef getBodyDef() {
		return bodyDef;
	}

	public void setBodyDef(BodyDef bodyDef) {
		this.bodyDef = bodyDef;
	}

	public PBox2D getBox2d() {
		return box2d;
	}

	public void setBox2d(PBox2D box2d) {
		this.box2d = box2d;
	}

	public PApplet getParent() {
		return parent;
	}

	public void setParent(PApplet parent) {
		this.parent = parent;
	}
	
	
	
}
