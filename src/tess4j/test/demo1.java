package tess4j.test;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

/**
 * @version 1.32 2007-04-14
 * @author Cay Horstmann
 */
public class demo1 {
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				SizedFrame frame = new SizedFrame();
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setVisible(true);
			}
		});
	}
}

class SizedFrame extends JFrame {
	public SizedFrame() {
		// get screen dimensions

		Toolkit kit = Toolkit.getDefaultToolkit();
		Dimension screenSize = kit.getScreenSize();
		int screenHeight = screenSize.height;
		int screenWidth = screenSize.width;

		// set frame width, height and let platform pick screen location

		setSize(screenWidth / 2, screenHeight / 2);
		setLocationByPlatform(true);

		// set frame icon and title
		URL url = SizedFrame.class.getResource("/ship.jpg")  ; 
//		System.out.println(url.toString());
		
//		Image img = kit.getImage(url.toString());
//		this.setIconImage(img);
		ImageIcon imgIcon=new ImageIcon(url.toString());
		this.setIconImage(imgIcon.getImage());
		setTitle("SizedFrame");
	}
}