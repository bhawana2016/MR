package util;
//this is our project code


import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;


public class CameraReceiver {

	public static int noOfRobots;
	public Socket socket;
	public DataInputStream dis;
	public DataOutputStream dout;
	public BufferedReader br;
	public double X, Y;
	
	public CameraReceiver() {
		socket = null;
		dis = null;
		dout = null;
		br = null;
		X = 0;
		Y = 0;
	}
	
	public void setPosition(double x, double y) {
		this.X = x;
		this.Y = y;
	}
	
	public double getX() {
		return X;
	}

	public double getY() {
		return Y;
	}

	public void checkColorChoice() {
		if(Const.ROBOT_COLOR >= noOfRobots) {
			System.out.println("Invalid Robot Color set");
			System.exit(0);
		}
	}
	
	public void establishConnection() {
		System.out.println("Connecting to "+Const.SERVER_IP+" on port "+Const.SERVER_PORT);
		System.out.println("We shall detect only one color");
		try 
		{
			socket = new Socket(Const.SERVER_IP, Const.SERVER_PORT);
			dis = new DataInputStream(socket.getInputStream());
			dout = new DataOutputStream(socket.getOutputStream());
			br = new BufferedReader(new InputStreamReader(System.in));
			
			noOfRobots = Integer.parseInt(dis.readLine());
			System.out.println("noOfRobots: " + noOfRobots);
			checkColorChoice();
			
			System.out.println(Const.ROBOT_COLOR);
			dout.writeUTF(Integer.toString(Const.ROBOT_COLOR));
			dout.flush();
		} 
		catch (UnknownHostException e) {
			e.printStackTrace();
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String readPosition() {
		String message="";
		try {
			message = dis.readLine();
		//	String [] position = message.split(" ");
			//   x=Double.parseDouble( msg.substring(0,msg.indexOf(" ")));
		    //   y=Double.parseDouble(msg.substring(msg.indexOf(" ")+1,msg.length()));
		       
			//setPosition(Double.parseDouble(position[0]), Double.parseDouble(position[1]));
			setPosition(Double.parseDouble( message.substring(0,message.indexOf(" "))), Double.parseDouble(message.substring(message.indexOf(" ")+1,message.length())));
			//dis.reset();
		}
		catch(IOException e) {
			e.printStackTrace();
		}
		return message;
	}
	
	
}
