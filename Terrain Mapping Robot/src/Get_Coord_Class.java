import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import util.*;

public class Get_Coord_Class {

 public static final String MUL_CAST_ADDR_SND = "224.7.6.3";
 public static final int PORT = 8888;
 public int x,y;
 byte[] outBuf;
 String msg;
 Coordinate coord;
	int id;
	int count=0;
	DatagramSocket socket = null;
    DatagramPacket outPacket = null;
    InetAddress address;
    CameraReceiver cam;
 
	public Get_Coord_Class() {
		try {
			cam = new CameraReceiver();
		    cam.establishConnection();
			socket = new DatagramSocket();
			address = InetAddress.getByName(MUL_CAST_ADDR_SND);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
	
	public Coordinate getCoord() {
		cam.readPosition();
		
		Coordinate coord = new Coordinate();
		
		coord.x=(int) cam.getX();
		coord.y=(int) cam.getY();
		
		msg =String.valueOf(coord.x)+" "+String.valueOf(coord.y) ;
		outBuf = msg.getBytes();
		outPacket = new DatagramPacket(outBuf, outBuf.length, address, PORT);
				
	    try {
			socket.send(outPacket);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	  //  System.out.println("The co-ordinates received are "+coord.x+","+coord.y);	
		return coord;
	}

}
