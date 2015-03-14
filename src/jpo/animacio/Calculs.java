package jpo.animacio;

public class Calculs {
	
	public static double[] normal (double a[], double b[], double c[]) {
		double n[] = new double[3];
		double u[] = new double[3];
		double v[] = new double[3];
		double m[] = new double[3];
		u[0]=b[0]-a[0];
		u[1]=b[1]-a[1];
		u[2]=b[2]-a[2];
		v[0]=c[0]-a[0];
		v[1]=c[1]-a[1];
		v[2]=c[2]-a[2];
		n[0]=u[1]*v[2]-u[2]*v[1];
		n[1]=u[2]*v[0]-u[0]*v[2];
		n[2]=u[0]*v[1]-u[1]*v[0];
		m[0]=(a[0]+b[0]+c[0])/3;
		m[1]=(a[1]+b[1]+c[1])/3;
		m[2]=(a[2]+b[2]+c[2])/3;
		double nm = Math.sqrt(n[0]*n[0]+n[1]*n[1]+n[2]*n[2]);
		n[0]=Math.signum(m[0])*Math.abs(n[0])/nm;
		n[1]=Math.signum(m[1])*Math.abs(n[1])/nm;
		n[2]=Math.signum(m[2])*Math.abs(n[2])/nm;
		return n;
	}
	public static float[] color (double n[], float c[]) {
		float cc[] = new float[4];
		cc[0]=c[0]+(float)n[0]*0.05f+(float)n[1]*0.1f-(float)n[2]*0.15f;
		cc[1]=c[1]+(float)n[0]*0.05f+(float)n[1]*0.1f-(float)n[2]*0.15f;
		cc[2]=c[2]+(float)n[0]*0.05f+(float)n[1]*0.1f-(float)n[2]*0.15f;
		return cc;
	}
}