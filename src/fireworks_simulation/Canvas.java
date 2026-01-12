/*
 * 
 */
package fireworks_simulation;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JComponent;
import javax.swing.Timer;

/**
 * Creates {@link Firework} object at various positions in the sky. Runs a 
 * timer object to animate each Firework.
 * 
 * @author Victoria
 */
class Canvas extends JComponent implements ActionListener, KeyListener{
	Timer t1;
	int second_counter = 0;
	//Dot[] d1 = new Dot[100]; 
	Firework[] d1 = new Firework[10]; 
	
	int time_constant = 20; 
	
	
	public Canvas(){
		t1 = new Timer(time_constant, this); // this will call actionPerformed periodically on this object
		t1.start();
		
//		for(int i = 0; i < d1.length; i++){
//			d1[i] = new Dot(this, 450,450); 
//		}
		for(int i = 0; i < d1.length; i++){
			
			
			d1[i] = createFirework(); 
		}
		
		addKeyListener(this);
	}


	
	public Firework createFirework(){
			double place_x = Math.random()*this.getWidth(); 
			double place_y = Math.random()*this.getHeight(); 
			
			double numberOfDots = Math.random()*100;
			
			double duration = Math.random()*10+5; 
			
			int red = (int) (Math.random()*128) + 128; 
			int green = (int) (Math.random()*128) + 128; 
			int blue = (int) (Math.random()*128) + 128; 
			int alpha = 255; 
			
			//don't really use this 
			int totalColor = (alpha << 24) + (red<<16) + (green<<8) + blue; 
			

			//orginal
			//TimeToColor f1 = t -> ((int)((1-t/duration)*256) << 24) + (red<<16) + (green<<8) + blue;  
			
			TimeToColor f2 = t -> (alpha << 24) + ((int)((1-t/duration)*256) <<16) + ((int)((1-t/duration)*256) <<8) + (int)((t/duration)*256);  
			
			TimeToColor f1 = t -> (Firework.blendColor(new Color(0,0,255), new Color(255,255,0), t/duration).getRGB()); 
			
			TimeToColor[] fn = {
				t -> ((int)((1-t/duration)*256) << 24) + (red<<16) + (green<<8) + blue,  
				t -> (alpha << 24) + ((int)((1-t/duration)*256) <<16) + ((int)((1-t/duration)*256) <<8) + (int)((t/duration)*256),
				t -> (Firework.blendColor(new Color(0,0,255), new Color(255,255,0), t/duration).getRGB()),
				t -> this.colorMe(t),
				this::colorMe
				
			};
			//TimeToColor f1 = t -> ((((int)(1-t/duration)*256) << 24) + (red<<16) + (green<<8) + blue); 
//			TimeToColor f2 = t -> ((alpha << 24) + (((int)(1-t/duration)*128)<<16) + (((int)(1-t/duration)*128)<<8) + (((int)(t/duration)*128))); 


			
			//HOW FUNCTIONS WORK
//			//example of the function 1
//				TimeToColor t1 = new DirectRGB(); 
//			
//			//example of the function 2
//				TimeToColor t2 = new TimeToColor(){
//					public int getRBG(double time) {
//						return (int)time;
//					}
//				};
//			
//			//example of the function 3 
//				TimeToColor t3 = (double time) -> (int)time + 1;
//			
			int random = (int)(Math.random()*fn.length); 
			
			
			return new Firework(this, (int)place_x, (int)place_y, (int)numberOfDots, duration, fn[random]); 
		
	}
	
	/**
	 * generates a color that changes over time (t) for the duration (duration).
	 * @param duration
	 * @param t must be a value between 0 and duration
	 * @return ARGB integer representing the color at time (t)
	 */
	public int colorMe(double t){
		double duration = 3.0;
		 return (255 << 24) + ((int)((1-t/duration)*256) <<16) + ((int)((1-t/duration)*256) <<8) + (int)((t/duration)*256);
		
	}
	
	//example of the function 1
//		class DirectRGB implements TimeToColor{
//
//			public int getRBG(double time) {
//				return (int)time;
//			}
//
//		}
	
	@Override
	public void keyPressed(KeyEvent e) {
		t1.setDelay(time_constant*8);

	}

	@Override
	public void keyReleased(KeyEvent e) {
		t1.setDelay(time_constant);
	}

	@Override
	public void keyTyped(KeyEvent e) {
		//throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
	}	
		
	@Override
	//WHREE IS THIS GETTING CALLED?
	public void paint(Graphics g1) {
		//background color 
		Graphics2D g = getGraphics(g1);		
		g.setPaint(Color.BLACK);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
		
		int time = second_counter; 	
		g.setPaint(Color.WHITE);
		for(int i = 0; i < d1.length; i ++){
			if(d1[i] != null) d1[i].drawMe(g); 
		}
		
		
	}	
	
	Graphics2D getGraphics(Graphics g) {
		Graphics2D g1 = (Graphics2D) g;
		g1.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		return g1;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		second_counter++;
		repaint();
	
		for(int i = 0; i < d1.length; i++){
			if((d1[i]!=null) && (!d1[i].update())){
				//d1[i] = null; 
				d1[i] = createFirework();
			}
		} 
	}	
	
}

