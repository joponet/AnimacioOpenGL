package jpo.animacio;

public class Material {
	
	public String name;
	public float[] color;
	
	public Material(String pName) {
		name=pName;
		color= new float[] {0.6f,0.6f,0.6f,1f};
	}
	
	public void setColor(double red,double green,double blue) {
		color[0]=(float)red;
		color[1]=(float)green;
		color[2]=(float)blue;
		color[3]=1f;
	}
}
