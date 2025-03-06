package Models;

import java.awt.Color;
import java.awt.Graphics2D;

import SwingShapes.Ellipse;

public class Pellet {
	private Ellipse pellet;
	
	public Pellet() {
		pellet = new Ellipse();
		pellet.setColor(Color.gray);
		pellet.setSize(20, 20);
	}
	
	public int getXLocation() {
		return pellet.getXLocation();
	}

	public void setXLocation(int locationX) {
		pellet.setLocation(locationX, pellet.getYLocation());
	}

	public int getYLocation() {
		return pellet.getYLocation();
	}

	public void setYLocation(int locationY) {
		pellet.setLocation(pellet.getXLocation(), locationY);
	}	
	
	public void draw(Graphics2D g) {
		pellet.paint(g);
	}
}
