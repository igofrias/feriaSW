#pragma config(Motor, motorA, rightMotor,  tmotorNXT, PIDControl)
#pragma config(Motor, motorC, leftMotor,   tmotorNXT, PIDControl)
#pragma config(Sensor, S1, lightRSensor, sensorLightInactive)
#pragma config(Sensor, S2, lightLSensor, sensorLightInactive)

/*****************************************
 *            Task Statement
 ****************************************/
task MonitorRLight();
task MonitorLLight();

/*****************************************
 *            Function Statement
 ****************************************/
void calibrate();
void Photophobia();

/*****************************************
 *       Global Variable Statement
 ****************************************/
int LightRVal;
int LightLVal;
int RightLimit;
int LeftLimit;
float averRLight = 0;	// float variable 'averLight'
float averLLight = 0; 	// float variable 'averLLight'

/*****************************************
 *            Main Task
 ****************************************/
task main(){
	StartTask(MonitorRLight);
	StartTask(MonitorLLight);
	calibrate();
	Photophobia();
	return;
}

/*****************************************
 *            Task Definition
 ****************************************/
//Light level monitoring (Right)
task MonitorRLight(){
	while(true){
		LightRVal = SensorValue[lightRSensor];
	}
}

//Light level monitoring (Left)
task MonitorLLight(){
	while(true){
		LightLVal = SensorValue[lightLSensor];
	}
}

//Function: calibrate
void calibrate(){
	//Variables definitions
	int LightRData[10];					  				// create an array 'LightData' for data of light sensor
	int LightLData[10];						  			// create an array 'LightLData' for data of light sensor

	//Initial Value
	averLLight = 0;
	averRLight = 0;

	//Light medition
	for(int i = 0;i<10; i++){
		LightRData[i] = LightRVal;
		LightLData[i] = LightLVal;

		//Obtain Average
		averRLight += (LightRData[i]/10.0);
		averLLight += (LightLData[i]/10.0);
	}
}

void Photophobia(){
	while(true){
		calibrate();
		RightLimit = averRLight +10;
		LeftLimit = averLLight+10;
		if(LightRVal > RightLimit && LightLVal < LeftLimit){
			motor[leftMotor] = 50;
			motor[rightMotor] = -50;
			wait1Msec(500);
			motor[leftMotor] = 0;
			motor[rightMotor] = 0;
		}
		if(LightLVal > LeftLimit && LightRVal < RightLimit){
			motor[leftMotor] = -50;
			motor[rightMotor] = 50;
			wait1Msec(500);
			motor[leftMotor] = 0;
			motor[rightMotor] = 0;
		}
		if(LightLVal > LeftLimit && LightRVal > RightLimit){
			motor[leftMotor] = 50;
			motor[rightMotor] = 50;
			wait1Msec(500);
			motor[leftMotor] = 0;
			motor[rightMotor] = 0;
		}
	}
	return;
}
