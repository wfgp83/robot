package client;

import java.awt.*;

public class MouseLocation {
    public static void main(String ... args) throws InterruptedException {
        while(true) {
            PointerInfo a = MouseInfo.getPointerInfo();
            Point b = a.getLocation();
            int x = (int) b.getX();
            int y = (int) b.getY();
            System.out.println("(x,y) := (" + Integer.toString(x) + "," + Integer.toString(y) + ")");
            Thread.sleep(5000);
        }
    }
}
