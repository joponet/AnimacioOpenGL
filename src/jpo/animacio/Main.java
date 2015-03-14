package jpo.animacio;

import static javax.media.opengl.fixedfunc.GLLightingFunc.*;
import static javax.media.opengl.GL.GL_FRONT_AND_BACK;
import static javax.media.opengl.GL2ES1.GL_LIGHT_MODEL_AMBIENT;
import static javax.media.opengl.GL3bc.*;



import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.media.opengl.GL3bc;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLProfile;
import javax.media.opengl.awt.GLCanvas;

import com.jogamp.opengl.util.Animator;

public class Main implements GLEventListener {

	GLCanvas canvas;
	Info info;
	MainListener listener;
	Animator animator;
	Body terra;
	Body arbre;
	boolean ready=false;
	
	public Main()  {
		GLProfile glp = GLProfile.getDefault();
        GLCapabilities caps = new GLCapabilities(glp);
        canvas = new GLCanvas(caps);
        
        // frame init
        Frame frame = new Frame("Window OpenGL Test");
        frame.setSize(500, 500);
        frame.setLocation(500, 200);
        frame.add(canvas);
        frame.setVisible(true);
        
        // the close button ask to close
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });		
        canvas.addGLEventListener(this);
        
        // Info
        info = new Info();
        
        // listener
        listener = new MainListener();
        canvas.addMouseListener(listener);
        canvas.addMouseMotionListener(listener);
        canvas.addMouseWheelListener(listener);
        canvas.addKeyListener(listener);
        canvas.requestFocus();
        
        // carrega 'obj'
        terra = new Body("D:\\5-personal\\00-documents\\Jordi\\Fitxers\\blender\\terra.obj");
        arbre = new Body("D:\\5-personal\\00-documents\\Jordi\\Fitxers\\blender\\arbre.obj");
	}
	
	public void Run() {
	    // Carrega objectes
	    terra.read();
        arbre.read();
        
	    // Animator
	    animator = new Animator(canvas);
		animator.start();

		// Status
        listener.escala = (float) (2/(terra.xmax-terra.xmin));
	    info.display();
	    listener.display();

	    ready=true;
}
	
	@Override
	public void init(GLAutoDrawable drawable) {
		GL3bc gl = drawable.getGL().getGL3bc();
	    gl.setSwapInterval(1);
	    
		// projection
	    gl.glMatrixMode(GL_PROJECTION);
		gl.glLoadIdentity();
		
		// modelview
		gl.glMatrixMode(GL3bc.GL_MODELVIEW);
		gl.glLoadIdentity();
	    gl.glEnable(GL_DEPTH_TEST); // enables depth testing
	    gl.glEnable(GL_LIGHT0);	    
	    gl.glEnable(GL_LIGHTING);
		gl.glShadeModel(GL_SMOOTH); // blends colors nicely, and smoothes out lighting
	    gl.glClearColor(0.5f, 0.5f, 1, 0);

	    // light
	    float ambientLight[] = {0.8f, 0.8f, 0.8f, 1.0f};
	    float lightColor[] = {0.2f, 0.2f, 0.2f, 1.0f};
	    gl.glLightModelfv(GL_LIGHT_MODEL_AMBIENT, ambientLight,0);
	    gl.glLightfv(GL_LIGHT0, GL_DIFFUSE, lightColor,0);
	    gl.glLightfv(GL_LIGHT0, GL_SPECULAR, lightColor,0);
	    float lightPosition[] = { 20.0f, 30.0f, -20.0f, 0.0f };
	    gl.glLightfv(GL_LIGHT0, GL_POSITION, lightPosition,0);
	    
	    // material
	    float mat_specular[] = { 0.0f, 0.0f, 1.0f, 1.0f };
	    float mat_shininess[] = { 20.0f };
	    float materialColor[] = {0.8f, 0.2f, 0.0f, 1.0f};
	    float materialEmission[] = {0.2f,0.2f,0.2f, 0.6f};
	    gl.glMaterialfv(GL_FRONT_AND_BACK, GL_AMBIENT_AND_DIFFUSE, materialColor,0);
	    gl.glMaterialfv(GL_FRONT_AND_BACK, GL_SPECULAR, mat_specular,0);
	    gl.glMaterialfv(GL_FRONT_AND_BACK, GL_SHININESS, mat_shininess,0);
	    gl.glMaterialfv(GL_FRONT_AND_BACK, GL_EMISSION, materialEmission,0);
	    
	    // orientation
	    gl.glRotated(-20, 1, 0, 0);
	    gl.glRotated(-30, 0, 1, 0);

	    // set status
	    info.setGL(gl);	    
	}
	
	@Override
	public void dispose(GLAutoDrawable drawable) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void display(GLAutoDrawable drawable) {
//		System.out.print(System.currentTimeMillis());
		if (!ready) return;
//		listener.tick();
		if (listener.exit) System.exit(0);
		
		GL3bc gl = drawable.getGL().getGL3bc();
		// projection
		gl.glMatrixMode(GL_PROJECTION);
		gl.glLoadIdentity();
		float translateX = listener.getTranslateX();
		float translateY = listener.getTranslateY();
		float escala = 1/listener.getEscala();
		gl.glOrtho(translateX-escala,translateX+escala,translateY-escala,translateY+escala, 30, -20);
		
		// modelview
		gl.glMatrixMode(GL_MODELVIEW);
	    gl.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	    gl.glRotatef(listener.getRotateX(), 0, 1, 0);
	    gl.glRotatef(listener.getRotateY(), 1, 0, 0);

	    // draw
//		System.out.print("-"+System.currentTimeMillis());
	    terra.draw(gl);
//		System.out.print("-"+System.currentTimeMillis());
	    gl.glPushMatrix();
	    gl.glTranslatef(listener.getPosX(), 0, listener.getPosY());
	    arbre.draw(gl);
	    gl.glPopMatrix();
	    gl.glFlush();
//		System.out.println("-"+System.currentTimeMillis());
	}
	
	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width,
			int height) {
		// TODO Auto-generated method stub
		
	}
}
