/**
 * This class is for recursive tree painting
 * It extends JComponent and implements MouseListener
 *
 * @author Nada Al-Thawr
 * @date 09/21/16
 *
 */
import java.awt.Color;
import java.awt.Graphics;
 import java.awt.Graphics2D; // optional, for drawing lines with varying thickness
 import java.awt.BasicStroke; // optional, for drawing lines with varying thickness
 import java.awt.event.MouseEvent;
 import java.awt.event.MouseListener;
 import java.awt.geom.Point2D;
 import javax.swing.JComponent;
 import java.awt.Stroke;
 import java.awt.geom.Line2D;
 import java.awt.geom.*;
 import javax.swing.*;
 import java.awt.*;
 import java.util.Random;
 import java.awt.geom.Line2D;
 import java.awt.geom.Point2D;




 public class RecursiveTreePainting extends JComponent implements MouseListener {
 	/** Number of generations to create branches. Play with this for coarser/finer detail. **/
 	public static final int NUM_GENERATIONS = 8;

 	/** Number of children for each branch. Play with this for sparser/fluffier trees. **/
 	public static final int NUM_CHILDREN = 3; 

 	/** Golden ratio makes the child branches aesthetically appealing **/
 	public static final double GOLDEN_RATIO = 1.618;

 	/** Maximum branching angle of children from a parent stick **/
 	public static final double MAX_BRANCHING_ANGLE = .5*Math.PI;

 	/** Diameter of the blossoms. **/
 	public static final int BLOSSOM_DIAM = 8;


	//set all our variables here
	//our x and y coordinates
	//boolean and our parameters
 	public double firstX;
 	public double secondX;
 	public double firstY;
 	public double secondY;
 	public boolean press;
 	public boolean release;
 	public int branchCoordX;
 	public int branchCoordY;
 	public double trunkLength;
 	public double branchLength;
 	public Point2D newP;


 	public RecursiveTreePainting()                                                                                                                                                  
 	{
	 	//for the mouse listener
 		addMouseListener(this);

 	}

 	public void paint( Graphics g )
 	{
        //paint the background
 		paintBackground (g);
        //set boolean to press and release so it doesn't paint until we click and release
 		if (press == true && release == true){
        //call the draw trunk and drawbrunch method
 			drawTrunk(g);
 		//call our paint branch method passing in all the calculated parameters that we have
 		//lTrunk and tAngle are methods defined below
 			paintBranch(g, newP, lTrunk ((int)firstX, (int)firstY, (int)secondX, (int)secondY), (int)tAngle(), NUM_GENERATIONS);
 		}
 	}

		//our paintbackground method
 	public void paintBackground (Graphics g){

		//create the background and color it
 		g.fillRect(0, 0, getWidth(), getHeight() );
 		g.setColor(Color.black); 
 	}

	//our draw trunk method	
 	public void drawTrunk ( Graphics g) {
			//create our graphics object
 		Graphics2D gTrunk = (Graphics2D ) g;
			//create a color and set it
 		Color trunkColor = new Color(0, 153, 51);
 		gTrunk.setColor(trunkColor);
            //change the thickness to 7
 		gTrunk.setStroke(new BasicStroke(3));
            //then we draw our line with the coordinates that we got from our mouse listener methods below
 		gTrunk.drawLine((int)firstX, (int)firstY, (int)secondX, (int)secondY);

 		

 	}
 		//method to compute the angle that is passed in paintbrush in the first time we call it
 	public double tAngle(){
 		//use the theta angle provided in assignment 2
 		double angle = Math.atan2(secondY- firstY, secondX-firstX);
 		return angle;

 	}
 		//method to calculate length of trunk and branch length in relation to that
 	public int lTrunk ( int firstX, int firstY, int secondX, int secondY) {

 		trunkLength = (int) (Math.sqrt(Math.pow( secondX- firstX,2) + Math.pow(secondY - firstY,2)));
 		int branLeng = (int)(trunkLength/GOLDEN_RATIO);
 		return branLeng;

 	}


 	public void paintBranch (Graphics g, Point2D startingP, int bLength, double bAngle, int genNum){
 			//create our graphics object
 		Graphics2D gBranch = (Graphics2D) g;
 		//base case
		/*if num generations is zero, as it goes down once it reaches zero generations
		it draws the blossoms*/
		if ( genNum == 0) {
 				//generate random colors

			Random rand = new Random();
			float r1 = rand.nextFloat();
			float g1 = rand.nextFloat();
			float b1 = rand.nextFloat();
 				//set random colors to the blossoms
			Color randomColor = new Color (r1,g1,b1);
			gBranch.setColor (randomColor);
			/* set the new angle and and the ending point
 				in angle we multiply .random with max angle to get the range 
 				and then subtract with half of it to get the proper angle*/
 				double nAngle = bAngle+(Math.random()*MAX_BRANCHING_ANGLE)-MAX_BRANCHING_ANGLE/2;
 			//computing our end point
 				Point2D endP = computeEndpoint (startingP, bLength, nAngle);

 				//branch coordinates for our blossoms
 				branchCoordX = (int)(endP.getX() + bLength*bAngle);
 				branchCoordY = (int)(endP.getY() + bLength*bAngle);

 			//create our ovlas
 				gBranch.fillOval ((int)(branchCoordX-BLOSSOM_DIAM/2), (int)(branchCoordY-BLOSSOM_DIAM/2), BLOSSOM_DIAM,BLOSSOM_DIAM ); 		}
 			//if the genNum is bigger than zero
 				else  if ( genNum > 0) {

 				//color the branches
 					Color branchColor = new Color(0, 153, 51);

				//set the thickness and make it smaller as we move from one generation to the other
 					gBranch.setStroke(new BasicStroke(genNum/4));

 				//a for loop that goes through the number of childresn and creates the branches 
 					for (int i = 0; i <= NUM_CHILDREN; i++) {
 						double nAngle = bAngle+(Math.random()*MAX_BRANCHING_ANGLE)-MAX_BRANCHING_ANGLE/2;
 						Point2D endP = computeEndpoint (startingP, bLength, nAngle);	
   					//set the branch color  
 						gBranch.setColor(branchColor);
 					//draw the line using out starting and ending points that we computed
 						gBranch.drawLine ( (int)startingP.getX(), (int)startingP.getY(), (int)endP.getX(), (int)endP.getY());
 					//call itself recursively while adjusting the branch length
 					//genNum decrements by 1 until we reach 0
 						paintBranch (g,endP,(int)(bLength/GOLDEN_RATIO), nAngle, genNum-1);

 					}



 				}


 			}
 		        /** 
	 * Compute the point that is length away from p at the angle.
	 * Uses cosine to get the new x coordinate, sine to get the new y coordinate.
	 */

 		        public Point2D computeEndpoint( Point2D p, double length, double angle )
 		        {
		return new Point2D.Double( 	p.getX() + length*Math.cos(angle), // x is cos
                				p.getY() + length*Math.sin(angle));	// y is sin
	}
	 /**
	 * Invoked when the mouse is pressed down. Adds a circle at the current location.
	 * 
	 * @param e the current state of the mouse
	 */
	 public void mousePressed(MouseEvent e)
	 {		
		//get the coordinates of where the mouse is pressed
	 	firstX=e.getX();
	 	firstY=e.getY();
    	 //to test where the mouse presses
	 	System.out.println("mouse pressed at (" + e.getX() + ", " + e.getY() + ")" );
    	 //set press as true
	 	press =true;
	 }

	/**
	 * Invoked when the mouse is released.
	 * 
	 * @param e the current state of the mouse
	 */
	public void mouseReleased(MouseEvent e)
	{
	      //get the coordinates of where the mouse is releasd	
		secondX=e.getX();
		secondY=e.getY();
    	  //test where the mouse is being released
		System.out.println("mouse released at (" + e.getX() + ", " + e.getY() + ")" );
		
    	 //set release as true
		release=true;
		//create our starting point that gets passed in our paintbranch method in the main
		newP = new Point2D. Double ( secondX, secondY);
		repaint();
	}

	/**
	 * Invoked when the mouse enters the component.
	 * 
	 * @param e the current state of the mouse
	 */
	public void mouseEntered(MouseEvent e){}

	/**
	 * Invoked when the mouse leaves the component.
	 * 
	 * @param e the current state of the mouse
	 */
	public void mouseExited(MouseEvent e){}

	/**
	 * Invoked when the mouse is clicked (pressed down and released).
	 * 
	 * @param e the current state of the mouse
	 */
	public void mouseClicked(MouseEvent e)
	{
		/** 
		* when the mouse clicked, it increases in side
		*/

	}

}

