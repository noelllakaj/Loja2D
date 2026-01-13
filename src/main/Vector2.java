package main;


public class Vector2 {
	double x;
	double y;
	
	public Vector2(double X,double Y) {
		this.x = X;
		this.y = Y;
	}
	
	public Vector2() {}
	
	public void setEqual(Vector2 vector) {
		this.x=vector.x;
		this.y=vector.y;
	}
	
	public Vector2 add(Vector2 vectorToAdd) {
		Vector2 temporary = new Vector2();
		temporary.x = this.x + vectorToAdd.x;
		temporary.y = this.y + vectorToAdd.y;
		return temporary;
	}
	
	public Vector2 sub(Vector2 vectorToSub) {
		Vector2 temporary = new Vector2();
		temporary.x = this.x - vectorToSub.x;
		temporary.y = this.y - vectorToSub.y;
		return temporary;
	}
	
	public Vector2 multiplyC(double c) {
		Vector2 temporary = new Vector2();
		temporary.x = this.x * c;
		temporary.y = this.y * c;
		return temporary;
	}
	
	public double dotProduct(Vector2 first,Vector2 second) {
		double result;
		
		result = first.x*second.x + first.y*second.y;
		
		return result;
	}
	
	public void multiply(Matrix2 matrix) {
		double tempX=this.x;
		double tempY=this.y;
		
		this.x = tempX*matrix.a11 + tempY*matrix.a12;
		this.y = tempX*matrix.a21 + tempY*matrix.a22;
	}
	
	public void rotate(Matrix2 matrix,Vector2 point) {
		double tempX = this.x;
		double tempY = this.y;
		double pointX = point.x;
		double pointY = point.y;
		
		this.x = (tempX-pointX)*matrix.a11 + (tempY-pointY)*matrix.a12 + pointX;
		this.y = (tempX-pointX)*matrix.a21 + (tempY-pointY)*matrix.a22 + pointY;
	}
	
	public String toString() {
		return "[ "+this.x+" , "+this.y+" ]";
	}
	
	public double magnitude2() {
		return this.x*this.x+this.y*this.y;
	}
	
	public double magnitude() {
		return Math.sqrt(this.x*this.x+this.y*this.y);
	}
	public void normalize() {
		double length = this.magnitude();
		this.x = this.x/length;
		this.y = this.y/length;
	}
	
}