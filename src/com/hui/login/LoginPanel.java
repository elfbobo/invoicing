package com.hui.login;

import java.awt.Graphics;
import java.awt.Image;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class LoginPanel extends JPanel {
	private Image img ;

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponents(g);
		g.drawImage(img, 0, 0, this);
	}

	public LoginPanel(){
		super();
		URL url = this.getClass().getResource("/res/login.jpg");
		img = new ImageIcon(url).getImage();
	}

}
