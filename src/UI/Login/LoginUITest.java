package UI.Login;

import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.*;

import UI.Login.LoginUI;
import DataInfo.EnumType;

public class LoginUITest {

	private JFrame loginUI;

	public LoginUITest() {
		// add JPlane
		loginUI = new LoginUI();
	}

	public static void main(String[] args) {
		new LoginUI();
	}
}
