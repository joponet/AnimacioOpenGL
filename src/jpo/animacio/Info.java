package jpo.animacio;

import static javax.media.opengl.GL3bc.GL_MAX_MODELVIEW_STACK_DEPTH;
import static javax.media.opengl.GL3bc.GL_MAX_CLIENT_ATTRIB_STACK_DEPTH;

import javax.media.opengl.GL3bc;
import javax.media.opengl.GLProfile;

public class Info {
	// variables
	String mode;
	int modelDepth[] = new int[1];
	float depthRange[] = new float[1];
	
	// constructor
	public Info() {
		mode = GLProfile.getDefault().getImplName();
	}
	
	// display GL information
	public void display() {
		System.out.println("\nStatus");
		System.out.println("Mode: "+mode);
		System.out.println("Model View Stack: "+modelDepth[0]);
		System.out.println("Depth Range: "+depthRange[0]);		
	}
	
	// set GL information
	public void setGL (GL3bc gl) {
		gl.glGetIntegerv(GL_MAX_MODELVIEW_STACK_DEPTH, modelDepth, 0);
		gl.glGetFloatv(GL_MAX_CLIENT_ATTRIB_STACK_DEPTH, depthRange, 0);
	}
}
