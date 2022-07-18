package com.abc;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MouseController extends MouseAdapter {
    
    public boolean mouse_pressed = false;
    public int mouse_button = -1;
    public boolean mouse_dragged = false;
    public double x = 0.0, y = 0.0;
    public double dx = -1.0, dy = -1.0;
    
    @Override
    public void mousePressed(MouseEvent e) {
        this.mouse_pressed = true;
        this.mouse_button = e.getButton();
    }
    @Override
    public void mouseReleased(MouseEvent e) {
        this.mouse_pressed = false;
        this.mouse_dragged = false;
        this.dx = 0.0;
        this.dy = 0.0;
    }
    @Override
    public void mouseMoved(MouseEvent e) {
        this.x = e.getX();
        this.y = e.getY();
    }
    @Override
    public void mouseDragged(MouseEvent e) {
        this.mouse_dragged = true;
        this.dx = e.getX() - this.x;
        this.dy = e.getY() - this.y;
    }
    
    
}
