#pragma config(Sensor, S1, sonarSensor, sensorSONAR)
#pragma config(Sensor, S2, lightSensor, sensorLightInactive)
#pragma config(Motor, motorA, rightMotor, tmotorNXT, PIDControl)
#pragma config(Motor, motorB, leftMotor, tmotorNXT, PIDControl)
#define MAX_DIST 10 //distancia maxima de 10cm
#define MIN_DIST 5  //distancia minima de 5cm

//variables globales
int SonarValue;
int LightValue;


task MonitorLight(){
	while(true){
		LightValue = SensorValue[lightSensor];
	}
}

task MonitorSonar(){
	while(true){
		SonarValue = SensorValue[sonarSensor];
	}
}

task main(){
	int count = 1;
	//Start Monitor Sonar & Monitor Light
	StartTask(MonitorSonar);
	StartTask(MonitorLight);

	//Synch motors
	nSyncedMotors = synchAB;	//Left motor slaved to Right motor
	nSyncedTurnRatio = -100;	//Left motor turns -100% of right motor

	nxtDisplayTextLine(2, "Para iniciar,");
	nxtDisplayTextLine(3, "coloque su mano");
	nxtDisplayTextLine(4, "entre 10 y 5cm");
	nxtDisplayTextLine(5, "del sonar.");
	//Detect distance range
	while(true){
		if( (SonarValue<=MAX_DIST)&&(SonarValue>=MIN_DIST) ){
			//Stop if distance is in [5,10] range
//			do{
				eraseDisplay();
				motor[rightMotor] = 20;				//Right motor moves at 50% power
																			//Left motor automatically moves at -50%
																			//because of synch and synch ratio.
//			wait1Msec(1000);
				nxtDisplayCenteredTextLine(3, "Calibrando...");
				nxtDisplayTextLine(4, "Espere unos");
				nxtDisplayTextLine(5, "momentos");
/*				if(count==1){
					wait1Msec(1000);
					count = 0;
				}
				else{
					wait1Msec(100);
				}
			}while(SonarValue>MAX_DIST);

*/			for(int i = 0;i<10; i++){
					nxtDisplayString(3, "Medicion %2d", i);
					nxtDisplayString(4, "Sonar: %3d", SonarValue);
					wait1Msec(500);
				}
				motor[rightMotor] = 0;
			break;
		}
	}
	eraseDisplay();
	nxtDisplayCenteredTextLine(2, "Calibracion");
	nxtDisplayCenteredTextLine(3, "Terminada");
	wait10Msec(100);
	eraseDisplay();
	StopAllTasks();

}
