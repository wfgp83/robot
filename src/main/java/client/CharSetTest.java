package client;

import javax.swing.*;
import java.io.IOException;
import java.nio.charset.Charset;

public class CharSetTest {
    public static void main(String[] args) {
        Charset charset = Charset.forName("UTF-8");
        System.out.println(charset);

        //JOptionPane.showInputDialog(null, "This is the message",
                //"This is the default text");
        int total = 1000;
        final String msg = "Successful Total " + total;
        //JOptionPane.showMessageDialog(null, msg);
        String input = "wdi/st*niu\"tian<may>good:ok_fine|that?hi";
        String normal = input.replaceAll("[/<>:\"|?*]", "_");
        System.out.println(normal);
        int[] a = new int[2];
        increaseInt(a);
        System.out.println(a[0] + " " + a[1]);
    }

    public static void increaseInt(int[] a ) {
        a[0] = a[0]  +9;
    }
}
