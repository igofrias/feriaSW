#define MAX_DIST 10 //distancia maxima de 10cm
#define MIN_DIST 5  //distancia minima de 5cm
#pragma config(Sensor, S1, sonar, sensorSONAR)
#pragma config(Sensor, S2, light, sensorLightInactive)
#pragma config(Motor, motorA, Right,tmotorNormal,PIDControl)
#pragma config(Motor, motorB, Left,tmotorNormal,PIDControl)

//variables globales
int SonarValue;
int LightValue;

task MonitorLight(){
	while(true){
		LightValue = SensorValue[light];
	}
}

task MonitorSonar(){
	while(true){
		SonarValue = SensorValue[sonar];
	}
}

task main()
{
	//Start Monitor Sonar & Monitor Light
	StartTask(MonitorSonar);
	StartTask(MonitorLight);

	//Synch motors
	nSyncedMotors = synchAB;	//Left motor slaved to Right motor
	nSyncedTurnRatio = -100;	//Left motor turns -100% of right motor

	//Detect distance range
	if((SonarValue<=MAX_DIST)&&(SonarValue>=MIN_DIST){
		//Stop if distance is in [5,10] range
		while((SonarValue>MAX_DIST)||(SonarValue<MIN_DIST){
			motor[Right] = 50;				//Right motor moves at 50% power
																//Left motor automatically moves at -50%
																//because of synch and synch ratio.
			wait1Msec(1000);

		}
	}


}
