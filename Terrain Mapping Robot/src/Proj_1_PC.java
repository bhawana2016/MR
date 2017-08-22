import lejos.nxt.Button;
import lejos.nxt.Motor;
import lejos.nxt.MotorPort;
import lejos.nxt.NXTMotor;
import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;
import lejos.nxt.addon.CompassHTSensor;
import lejos.nxt.addon.SensorSelector;
import lejos.nxt.addon.SensorSelectorException;
import lejos.robotics.Accelerometer;
import lejos.robotics.navigation.DifferentialPilot;
import util.*;

public class Proj_1_PC {

    public static boolean isInside(double X, double Y) {


        if(X>18  && X<430 &&  Y>22 && Y<365) {
                return true;
        } else
        		return false;
    }//isInside function
    
	
	public static void main(String[] args){
		
/*		NXTMotor m1=new NXTMotor(MotorPort.A); //right	
		NXTMotor m2=new NXTMotor(MotorPort.C); //left
*/		Get_Coord_Class get_coord=new Get_Coord_Class();
		int MAINTAIN_LENGTH=25;
		Coordinate coord=new Coordinate();
		Coordinate init_coord=new Coordinate();
		Coordinate final_coord=new Coordinate();
		Coordinate prev=new Coordinate();
		Coordinate curr=new Coordinate();
		Coordinate turn_prev=new Coordinate();
		Coordinate turn_curr=new Coordinate();
		boolean leftturn=false,rightturn=false;
		boolean backFlag = true;
		double curr_compass,prev_compass;
		 int dir_flag1 = 0,dir_flag2=0;
		int z=0;
		DifferentialPilot pilot = new DifferentialPilot(5.5f, 16f, Motor.A, Motor.C);
		CompassHTSensor compass=new CompassHTSensor(SensorPort.S3);

		boolean use_me=true,flag=true;
		 UltrasonicSensor MidSonic = new UltrasonicSensor(SensorPort.S2);
		 UltrasonicSensor LeftSonic = new UltrasonicSensor(SensorPort.S1);
		 UltrasonicSensor RightSonic = new UltrasonicSensor(SensorPort.S4);
//		 Accelerometer accelerometer = SensorSelector.createAccelerometer(SensorPort.S3);

		int diff;
		
		long startTime = System.currentTimeMillis();
		
		while(false||(System.currentTimeMillis()-startTime)< 5000)
		{
			coord=get_coord.getCoord();
			init_coord=coord;
		}
		coord=get_coord.getCoord();
		init_coord=coord;
		turn_prev=coord;

		
	//	System.out.println(coord.x+" "+coord.y);
		while(!(Button.ESCAPE.isDown())) {
			
			startTime = System.currentTimeMillis();
			while(((System.currentTimeMillis()-startTime) < 500) && use_me)
			{
        	 Motor.A.stop();
        	 Motor.C.stop();
        	 coord=get_coord.getCoord();
        	 
			}//While for the stop
			use_me=false;
			if(isInside(coord.x,coord.y))
			{	
				prev=get_coord.getCoord();
				System.out.println(coord.x+" "+coord.y+" "+z+" "+LeftSonic.getDistance()+" "+RightSonic.getDistance());
				startTime = System.currentTimeMillis();
				while((System.currentTimeMillis()-startTime)< 1000){
					coord=get_coord.getCoord();
					
					if(MidSonic.getDistance()<=15 && flag){
						pilot.rotate(90);
						flag=false;
					}
					else if(LeftSonic.getDistance()>30 && RightSonic.getDistance()>30){
						if(leftturn){
							turn_curr=get_coord.getCoord();
							diff=turn_curr.x-turn_prev.x;
							if (diff>0){
								dir_flag1=345;
								dir_flag2=355;
							}else if(diff<0){
								dir_flag1=180;
								dir_flag2=205;
							}
							
							Motor.B.rotate(-50);
							leftturn=false;
							Motor.A.setSpeed(50);
							Motor.C.setSpeed(50);
							//curr_compass=compass.getDegrees();
							
							if(compass.getDegrees()>dir_flag1 && compass.getDegrees()<dir_flag2){
								Motor.A.stop();
								Motor.C.stop();
							}//end of if
							else{
								Motor.A.forward();
								Motor.C.backward();
							
								while(!(compass.getDegrees()>dir_flag1 && compass.getDegrees()<dir_flag2)){
								//	 System.out.println(compass.getDegrees());
									 }//end of while
								Motor.A.stop();
								Motor.C.stop();
							}//end of else
							//curr_compass=compass.getDegrees();
							//System.out.println("Current Compass "+curr_compass);
						}//end of if left turn
						
						else if(rightturn){
							turn_curr=get_coord.getCoord();
							diff=turn_curr.x-turn_prev.x;
							if (diff>0){
								dir_flag1=345;
								dir_flag2=355;
							}else if(diff<0){
								dir_flag1=180;
								dir_flag2=205;
							}
							Motor.B.rotate(50);
							rightturn=false;
							//curr_compass=compass.getDegrees();
							Motor.A.setSpeed(50);
							Motor.C.setSpeed(50);
							//curr_compass=compass.getDegrees();
							
							if(compass.getDegrees()>dir_flag1 && compass.getDegrees()<dir_flag2){
								Motor.A.stop();
								Motor.C.stop();
							}
							else{
								Motor.A.forward();
								Motor.C.backward();
							
								while(!(compass.getDegrees()>dir_flag1 && compass.getDegrees()<dir_flag2)){
								//	 System.out.println(compass.getDegrees());
									 }
								Motor.A.stop();
								Motor.C.stop();
							}
							
							//curr_compass=compass.getDegrees();
							//System.out.println("Current Compass "+curr_compass);
							
						}//end of right turn else if
						
						Motor.A.setSpeed(150);
						Motor.C.setSpeed(150);
						Motor.A.forward();
						Motor.C.forward();
						use_me=true;
					}//end of straight  if
					
					//left wallfollow
					else if(LeftSonic.getDistance() <=30){
					//	System.out.println("Left Wall follow condition");
						if(!leftturn){
						//	prev_compass=compass.getDegrees();
						Motor.B.rotate(50);
						//System.out.println("Prev Compass "+prev_compass);
						}
						
						leftturn=true;
						
						if(rightturn){
							Motor.B.rotate(50);
							rightturn=false;
						}
						if (MidSonic.getDistance()-MAINTAIN_LENGTH ==0){
							Motor.A.setSpeed(150);
							Motor.C.setSpeed(150);
							Motor.A.forward();
							Motor.C.forward();
						}
						else if(MidSonic.getDistance()-MAINTAIN_LENGTH < 0){
							Motor.A.setSpeed(150);
							Motor.C.setSpeed(100);
							Motor.A.forward();
							Motor.C.forward();
						}
						else if(MidSonic.getDistance() - MAINTAIN_LENGTH >0){
							Motor.C.setSpeed(150);
							Motor.A.setSpeed(100);
							Motor.A.forward();
							Motor.C.forward();
						}
						
							Motor.A.forward();
							Motor.C.forward();
							use_me=true;
					}
					
					//Right wall follow
					else if(RightSonic.getDistance() <=30){
						if(!rightturn){
							Motor.B.rotate(-50);
							//prev_compass=compass.getDegrees();
							//System.out.println("Prev Compass "+prev_compass);
						}
						
						rightturn=true;
						
						if(leftturn){
							Motor.B.rotate(-50);
							leftturn=false;
						}
						if (MidSonic.getDistance()-MAINTAIN_LENGTH ==0){
							Motor.A.setSpeed(150);
							Motor.C.setSpeed(150);
							Motor.A.forward();
							Motor.C.forward();
						}
						else if(MidSonic.getDistance()-MAINTAIN_LENGTH < 0){
							Motor.A.setSpeed(100);
							Motor.C.setSpeed(150);
							Motor.A.forward();
							Motor.C.forward();
						}
						else if(MidSonic.getDistance() - MAINTAIN_LENGTH >0){
							Motor.C.setSpeed(100);
							Motor.A.setSpeed(150);
							Motor.A.forward();
							Motor.C.forward();
						}
						Motor.A.forward();
						Motor.C.forward();
						use_me=true;
					}//end of right wall follow
					
					
			}	//While for the forward movement
				
 			
             coord=get_coord.getCoord();
             curr=coord;
            //System.out.println("Distance "+ dist+ " Accel "+accelerometer.getXAccel()+" Angle "+ angle);
  
 			//System.out.println(coord.x+" "+coord.y+" "+z+" "+LeftSonic.getDistance()+" "+RightSonic.getDistance());
			}	//If isInside
			else if(!isInside(coord.x,coord.y))
			{
				backFlag = true;
				Motor.A.stop();
				Motor.C.stop();
				Motor.A.setSpeed(150);
           	 	Motor.C.setSpeed(150);
				startTime = System.currentTimeMillis();
				while(backFlag && (System.currentTimeMillis()-startTime)< 2000)
				{
				 Motor.A.backward();
            	 Motor.C.backward();
            	 coord=get_coord.getCoord();
				}	//Move backward
				backFlag = false;
				Motor.A.stop();		
				Motor.C.stop();
				final_coord=get_coord.getCoord();
				if(isInside(coord.x,coord.y))
				{
					diff=final_coord.x-init_coord.x;
					if(final_coord.x > 11 && final_coord.x <21 && final_coord.y > 15 && final_coord.y <21 ){
						Motor.A.stop();
						Motor.C.stop();
						System.exit(0);
					}
					
					if(diff>0){
						pilot.setTravelSpeed(50);
						pilot.rotate(-90);
						pilot.travel(20);
						pilot.rotate(-90);
					}
					else if(diff<20){
						pilot.setTravelSpeed(50);
						pilot.rotate(90);
						pilot.travel(20);
						pilot.rotate(90);
					}
					
					turn_prev=get_coord.getCoord();
					//System.out.println(1);
				}		//After the backward travel, turn condition
					
					
				coord=get_coord.getCoord();
			}	//elseIf of not of isInside()

	} // While Button Main While loop

}
}
