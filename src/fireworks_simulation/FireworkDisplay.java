/*
 * 
 */
package fireworks_simulation;


import javax.swing.JFrame;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;



/**
 * Starts the program by creating window and adding the UI canvas
 * 
 * @author Victoria
 */
public class FireworkDisplay {
	public static void main (String[] args){
		JFrame window = new JFrame();
		Canvas c1 = new Canvas();
		window.setContentPane(c1);
		window.addKeyListener(c1);
		window.setDefaultCloseOperation(EXIT_ON_CLOSE);
		window.setBounds(500, 100, 900, 900);
		window.setVisible(true);
	}
	
}


