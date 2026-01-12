/*
 * 
 */
package fireworks_simulation;

import java.awt.Color;
import java.awt.Graphics2D;

/**
 * Represents a specific Firework. Keeps track of each dot position that belongs
 * to the firework along with its velocity.
 * 
 * @author Victoria
 */
class Firework{
	/**
	 *       _ (velocity_x[i], velocity_y[i])
	 *       ╱▏
	 *      ╱
	 *     ●   ← ith dot
 	 *  (pos_x[i], pos_y[i])
	 */
	private double[] pos_x; 
	private double[] pos_y; 
	private double[] velocity_x; 
	private double[] velocity_y; 
	public double time = 0; 
	public int radius = 100;
	private final Canvas canvas; 
	private double duration; 
	private TimeToColor colorFn = t -> 0xff_ff_ff_ff; 
	//where to get the coordinate type? 
	//private GenerateCoordinate generator = x -> new Coordinate(x); 
	
	

	
	
	/**
	 * overloaded constructor 
	 * difference is that we get to control the starting position 
	 * @param c1
	 * @param x
	 * @param y 
	 * @param dots
	 * @param duration
	 * @param colorFunction (ARGB)
	 */
	public Firework(Canvas c1, int x, int y, int dots, double duration, GenerateCoordinate generator){
		canvas = c1;
		
		//starting position 
		pos_x = new double[dots]; 
		pos_y = new double[dots]; 
		
		for (int i = 0; i < pos_x.length; i++) {
			pos_x[i] = x;
			pos_y[i] = y;
		}
		
		this.duration = duration; 
		
		
		//double[][] both = Circle(dots); 
		double[][] both = generator.generateVelocity(dots);  
		
		velocity_x = both[0]; 
		velocity_y = both[1]; 
		
		/////////////////////////
//		velocity_x = new double[dots]; 
//		velocity_y = new double[dots]; 
//		
//		for (int i = 0; i < pos_x.length; i++) {
//			double final_velocity_range = (Math.random() * 30);
//
//			//C^2 type of thing 
//			final_velocity_range *= final_velocity_range;
//			velocity_x[i] = Math.random() * final_velocity_range;
//			velocity_y[i] = final_velocity_range - velocity_x[i];
//			velocity_x[i] = Math.sqrt(velocity_x[i]);
//			velocity_y[i] = Math.sqrt(velocity_y[i]);
//
//			int kx = (int) (Math.random() * 2);
//			int ky = (int) (Math.random() * 2);
//
//			if (kx == 0) {
//				kx = -1;
//			}
//			if (ky == 0) {
//				ky = -1;
//			}
//
//			//application of pos or neg 
//			velocity_x[i] *= kx;
//			velocity_y[i] *= ky;
//		}
		
	}
	
	
	public Firework(Canvas c1, int x, int y, int dots, double duration, TimeToColor color){
		this(c1, x, y,dots,duration, Firework::circle); 
		this.colorFn = color; 
	}
	
	//new constructor 
	public Firework(Canvas c1, int x, int y, int dots, double duration, TimeToColor color, GenerateCoordinate generator){
		canvas = c1; 
		//starting position 
		pos_x = new double[dots]; 
		pos_y = new double[dots]; 
		velocity_x = new double[dots]; 
		velocity_y = new double[dots]; 
		
		this.duration = duration; 

		this.colorFn = color; 
	}
	
	int circle = 0;
	//parameters take in the starting pos 
	/**
	 * 
	 * @param n  number of dots 
	 * @return double array of velocities (x[] and y[])
	 */
	public static double[][] circle(int n){
		//double[] velocity_x = new double[n];
		//double[] velocity_y = new double[n];
		
		double[][] bothVelocities = new double[2][n];
		
		double[] velocity_x = bothVelocities[0];
		double[] velocity_y = bothVelocities[1];
		
		for (int i = 0; i < n; i++) {
			double final_velocity_range = (Math.random() * 30);			

			//C^2 type of thing 
			final_velocity_range *= final_velocity_range;
			velocity_x[i] = Math.random() * final_velocity_range;
			velocity_y[i] = final_velocity_range - velocity_x[i];
			velocity_x[i] = Math.sqrt(velocity_x[i]);
			velocity_y[i] = Math.sqrt(velocity_y[i]);

			int kx = (int) (Math.random() * 2);
			int ky = (int) (Math.random() * 2);

			if (kx == 0) {
				kx = -1;
			}
			if (ky == 0) {
				ky = -1;
			}

			//application of pos or neg 
			velocity_x[i] *= kx;
			velocity_y[i] *= ky;
		}
	
		return bothVelocities; 
		
		
	}
	
	
	
	/**
	 * Every time the screen is rendered this method is called.
	 * @param g 
	 */
	public void drawMe(Graphics2D g){
//		System.out.println((time/duration) +"->"+(color.color(time)>>> 24)); 
		//don't quite get this line
		int RGB = colorFn.getRBG(time);
		Color currentColor = new Color(RGB, true);
		g.setColor(currentColor);
//		System.out.println("Time:" + this.time/this.duration + g.getColor());
		
		for(int i = 0; i < pos_x.length; i++){
			g.drawLine((int)pos_x[i], (int)pos_y[i], (int)pos_x[i]+1, (int)pos_y[i]+1);
		
		}
		
	}
	

	
	//current time 
	public boolean update(){
		
		//time = keyTyped().time_constant; 
		
		double elapsedTime = canvas.time_constant/1000.0;
		time += elapsedTime;
		for(int i = 0; i < pos_x.length; i++){
			velocity_y[i] += (100.0/1000); 
			pos_x[i] += (velocity_x[i] * elapsedTime); 
			pos_y[i] += (velocity_y[i] * elapsedTime); 
		}
		
//		if(time > duration){
//			return false; 
//		} else {
//			return true;
//		}
		return duration >= time;
		
	}	
	
	//percent should be in decimal format already 
	public static Color blendColor(Color c1, Color c2, double percent){
		//percentage 
		double oppPercent = 1-percent; 
		
		double red = percent*c1.getRed() + oppPercent*c2.getRed(); 
		//red /= 2; 
		
		double green = percent*c1.getGreen() + oppPercent*c2.getGreen(); 
		//green /= 2; 
		
		double blue = percent*c1.getBlue() + oppPercent*c2.getBlue(); 
		//blue /= 2; 
		
		Color newColor = new Color((int)red, (int)green, (int)blue);
		//i'll comment it out later
		return newColor; 
	}
	

}

/**
 * Function that changes color over time
 * @author Victoria
 */
interface TimeToColor{
	public int getRBG(double time);
}