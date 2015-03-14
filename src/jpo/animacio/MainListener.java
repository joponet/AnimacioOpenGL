package jpo.animacio;

import static java.awt.event.KeyEvent.*;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

public class MainListener implements MouseListener, MouseMotionListener, MouseWheelListener, KeyListener{
	
	private static float escalaInc = -0.05f;

	private float rotateX = 0f;
	private float rotateY = 0f;
	
	private float translateX = 0f;
	private float translateY = 0f;
	
	private int mouseDragX = 0;	
	private int mouseDragY = 0;

	private int mouseKey;
	
	float escala = 1;
	
	private float posX=0;
	private float posY=0;
	private float posInc = 0.05f;
	private int keyPressed = 0;
	
	boolean exit=false;
	
	Joystick joystick;
	
	public MainListener() {
		joystick = new Joystick();
		joystick.display();
	}
	
	public float getRotateX() {
		float rotate;
		rotate = rotateX+joystick.getIncW();
		rotateX = 0;
		return rotate;
	}
	
	public float getRotateY() {
		float rotate;
		rotate = rotateY+joystick.getIncZ();
		rotateY = 0;
		return rotate;
	}

	public float getTranslateX() {
		return translateX;
	}

	public float getTranslateY() {
		return translateY;
	}
	
	public float getEscala() {
		return escala;
	}
	
	public float getPosX() {
		if (keyPressed == VK_RIGHT) posX +=posInc; 
		if (keyPressed == VK_LEFT) posX -=posInc; 
		posX -= joystick.getIncX();
		return posX;
	}
	
	public float getPosY() {
		if (keyPressed == VK_UP) posY +=posInc; 
		if (keyPressed == VK_DOWN) posY -=posInc; 
		posY += joystick.getIncY();
		return posY;
	}
	
	public void display() {
		joystick.display();
	}
	
	public void tick() {
		joystick.tick();
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		switch (arg0.getKeyCode()) {
		case VK_UP:
		case VK_DOWN:
		case VK_RIGHT:
		case VK_LEFT:
			keyPressed = arg0.getKeyCode();
			break;
		}
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		keyPressed = 0;
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		switch (arg0.getKeyChar()) {
		case VK_ESCAPE:
			exit = true;
			break;
		case 'X':
		case 'x':
			exit = true;
			break;
		}
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		escala = escala * (1 + escalaInc*e.getWheelRotation());		
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		float sens= 3f;
		if (mouseKey == MouseEvent.BUTTON1) {
			rotateX=rotateX + (mouseDragX-e.getX()) / sens;
			rotateY=rotateY + (mouseDragY-e.getY()) / sens;
			mouseDragX = e.getX();
			mouseDragY = e.getY();
		}
		if (mouseKey == MouseEvent.BUTTON3) {
			translateX = translateX + ((mouseDragX-e.getX())/200f)/escala;
			translateY = translateY - ((mouseDragY-e.getY())/200f)/escala;
			mouseDragX = e.getX();
			mouseDragY = e.getY();
		}		
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		if (mouseKey == MouseEvent.BUTTON2) {
			exit=true;
		}
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
//		canvas.setCursor(new Cursor(Cursor.MOVE_CURSOR));
		mouseKey = e.getButton();
		mouseDragX = e.getX();
		mouseDragY = e.getY();
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
