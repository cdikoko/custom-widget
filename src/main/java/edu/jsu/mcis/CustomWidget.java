package edu.jsu.mcis;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;

public class CustomWidget extends JPanel implements MouseListener {
    private java.util.List<ShapeObserver> observers;
    
    
    private final Color HEX_SELECTED_COLOR = Color.green;
    private final Color DEFAULT_COLOR = Color.white;
    private final Color OCTA_SELECTED_COLOR = Color.red;
    
    private boolean selected;
    private Point[] hexVertices;
    private Point[] octaVertices; 

    
    public CustomWidget() {
        observers = new ArrayList<>();
        selected = false;
        Dimension dim = getPreferredSize();
        
        hexVertices = new Point[6];
        for(int i = 0; i < hexVertices.length; i++) { hexVertices[i] = new Point(); }
        
        octaVertices = new Point[8];
        for(int i = 0; i < octaVertices.length; i++) { octaVertices[i] = new Point(); }
       
        calculateVertices(dim.width, dim.height);
        setBorder(BorderFactory.createLineBorder(Color.black));
        addMouseListener(this);
    }

    
    public void addShapeObserver(ShapeObserver observer) {
        if(!observers.contains(observer)) observers.add(observer);
    }
    public void removeShapeObserver(ShapeObserver observer) {
        observers.remove(observer);
    }
    private void notifyObservers() {
        ShapeEvent event = new ShapeEvent(selected);
        for(ShapeObserver obs : observers) {
            obs.shapeChanged(event);
        }
    }
    
    
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(200, 200);
    }

    private void calculateVertices(int width, int height) {
       
        int side = Math.min(width, height) / 2;
        
        for(int i = 0; i < hexVertices.length; i++) {
            double rad = (i * (Math.PI / hexVertices.length / 2));
             hexVertices[i].setLocation(width/3 +(Math.cos(rad)* (side/2)), height/2 + (Math.sin(rad) * (side/2))); 
                            
        }
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D a = (Graphics2D)g;
        calculateVertices(getWidth(), getHeight());
        Shape[] shape = getShapes();
        a.setColor(Color.black);
        a.draw(shape[0]);
        a.draw(shape[1]);
        if(selected == true) {
            a.setColor(HEX_SELECTED_COLOR);
            a.fill(shape[0]);
            a.setColor(DEFAULT_COLOR);
            a.fill(shape[1]);
        }
        else if (selected == false){
            a.setColor(DEFAULT_COLOR);
            a.fill(shape[0]);
            a.setColor(OCTA_SELECTED_COLOR);
            a.fill(shape[1]);
            
        }
    }

    public void mouseClicked(MouseEvent event) {
        Shape shape = getShapes();
        if(shape.contains(event.getX(), event.getY())) {
            selected = true;
            notifyObservers();
        }
        repaint(shape.getBounds());
    }
    public void mousePressed(MouseEvent event) {};
    public void mouseReleased(MouseEvent event) {};
    public void mouseEntered(MouseEvent event) {};
    public void mouseExited(MouseEvent event) {};
    
    public Shape [] getShapes(Shape[] shape) {
		int[] Octx;
		int[] Octy;
        int[] x = new int[hexVertices.length];
        int[] y = new int[hexVertices.length];
        for(int i = 0; i < hexVertices.length; i++) {
            x[i] = hexVertices[i].x;
            y[i] = hexVertices[i].y;
        }
        shape[0] = new Polygon(x, y, hexVertices.length);
        
         Octx = new int[octaVertices.length];
         Octy = new int[octaVertices.length];
        for(int i = 0; i < octaVertices.length; i++) {
            x[i] = octaVertices[i].x;
            y[i] = octaVertices[i].y;
        }
        shape[1] = new Polygon(x, y, octaVertices.length);
        return shape;
    
    
    }
    public boolean isSelected() { return selected; }



	public static void main(String[] args) {
		JFrame window = new JFrame("Custom Widget");
        window.add(new CustomWidget());
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setSize(300, 300);
        window.setVisible(true);
	}
}
