#pragma config(Sensor, S1, sonar, sensorSONAR)
#pragma config(Sensor, S2, light, sensorLightInactive)

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

task main(){
	StartTask(MonitorLight);
	StartTask(MonitorSonar);
	while(true){
		nxtDisplayString(0, "Light: %3d", LightValue);
		nxtDisplayString(1, "Sonar: %3d", SonarValue);
		wait10Msec(25);
	}
}
