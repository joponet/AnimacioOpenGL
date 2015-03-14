package jpo.animacio;

import static javax.media.opengl.GL.*;
import static javax.media.opengl.fixedfunc.GLLightingFunc.GL_AMBIENT_AND_DIFFUSE;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import javax.media.opengl.GL2;
import javax.media.opengl.GL3bc;

public class Body {
	// variables
	boolean validFile=false;
	boolean validBody=false;
	ArrayList <Face> faces = new ArrayList<Face>();
	ArrayList <Material> materials = new ArrayList<Material>();
	File file;
	double xmax=0;
	double xmin=0;
	double ymax=0;
	double ymin=0;
	double zmax=0;
	double zmin=0;
	
	public Body(String path) {
		file = new File(path);
		validFile = true;
	}
	
	public void read() {
		if (!validFile) return;
		faces.clear();
		materials.clear();
		// fitxer material
		try {
			File filem;
			FileInputStream fileStream=null;
			BufferedReader fileReader;
			String values[];
			String line="";

			String fileName=file.getPath();
			fileName=fileName.replaceAll(".obj", ".mtl");
			filem=new File(fileName);
			System.out.println(filem.getPath());
			fileStream = new FileInputStream(filem);
			fileReader = new BufferedReader(new InputStreamReader(fileStream));
			while (line != null) {
				if (line.startsWith("newmtl")) {
					values=line.split(" ");
					Material material = new Material(values[1]);
					while (!line.startsWith("Kd")) line=fileReader.readLine();
					values=line.split(" ");
					material.setColor(Double.parseDouble(values[1]), Double.parseDouble(values[2]), Double.parseDouble(values[3]));
					materials.add(material);
					System.out.println(material.name+": "+material.color[0]+" "+material.color[1]+" "+material.color[2]);
				}
				line=fileReader.readLine();
			}
			fileReader.close();
			fileStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// fitxer vertex
		try {
			FileInputStream fileStream=null;
			BufferedReader fileReader;
			ArrayList <String> lines = new ArrayList<String>();
			double vertex[][];
			String values[];
			String line="";
			// vertex
			fileStream = new FileInputStream(file);
			fileReader = new BufferedReader(new InputStreamReader(fileStream));
			while ((line != null) && (!line.startsWith("usemtl"))) {
				if (line.startsWith("v")) {
					lines.add(line);
				}
				line=fileReader.readLine();
			}
			vertex = new double[lines.size()][3];
			for (int i=0; i<lines.size(); i++) {
				values=lines.get(i).split(" ");
				for (int j=0; j<3; j++) vertex[i][j]=Double.parseDouble(values[j+1]);
				if (vertex[i][0] > xmax) xmax = vertex[i][0];
				if (vertex[i][0] < xmin) xmin = vertex[i][0];
				if (vertex[i][1] > ymax) ymax = vertex[i][1];
				if (vertex[i][1] < ymin) ymin = vertex[i][1];
				if (vertex[i][2] > zmax) zmax = vertex[i][2];
				if (vertex[i][2] < zmin) zmin = vertex[i][2];
			}
			System.out.println("Vertex: "+lines.size());
			lines.clear();
			// fitxer cares
			Material material=null;
			int tipus;
			while (line != null) {
				// material utilitzat
				if (line.startsWith("usemtl")) {
					values=line.split(" ");
					System.out.println(values[1]);
					for (int i=0; i<materials.size(); i++) {
						if (materials.get(i).name.endsWith(values[1])) {
							material=materials.get(i);
							System.out.println("* "+values[1]);
						}
					}
				}
				// cara
				if (line.startsWith("f")) {
					values=line.split(" ");
					tipus=values.length-1;
					if (tipus==3 || tipus==4) {
						int ivertex[]= new int[tipus];
						for (int i=0; i<tipus; i++) ivertex[i]=Integer.parseInt(values[i+1]);
						Face face = new Face(material,tipus,ivertex,vertex);
						faces.add(face);
					}
				}
				line=fileReader.readLine();
			}
			System.out.println("Cares: "+faces.size());
			fileReader.close();
			fileStream.close();
			validBody = true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void draw(GL3bc gl) {
		if (!validBody) return;
	    // draw
	    gl.glBegin(GL2.GL_TRIANGLES);
	    if (faces.get(0).tipus==4) {
			gl.glEnd();	    
		    gl.glBegin(GL2.GL_QUADS);
	    }
	    for (int nf=0; nf<faces.size(); nf++) {
    		gl.glColor3fv(faces.get(nf).getColor(), 0);
    		gl.glNormal3dv(faces.get(nf).getNormal(), 0);
    		gl.glMaterialfv(GL_FRONT_AND_BACK, GL_AMBIENT_AND_DIFFUSE, faces.get(nf).color,0);
	    	for (int nfv=0; nfv<faces.get(nf).tipus; nfv++) {
	    		// escriu cara
	    		gl.glVertex3dv(faces.get(nf).getVertex(nfv),0);
	    	}
	    }
		gl.glEnd();
	}

}
