#pragma config(Sensor, S4, sonarSensor, sensorSONAR)

task main(){
	int grado, valor, min = 255;
	nMotorEncoder[motorB] = 0;

	for(int i = 1; i <= 180; i++){
		while(nMotorEncoder[motorB] < i){
			motor[motorB] = 30;
		}
		motor[motorB] = 0;
		valor = SensorValue[sonarSensor];
		if (valor < min){
			min = valor;
			grado = i;
		}
		wait1Msec(50);
	}

	while(nMotorEncoder[motorB] > 0){
		motor[motorB] = -30;
	}
	motor[motorB] = 0;

	nxtDisplayCenteredTextLine(3, "Grado: %d", grado);
	nxtDisplayCenteredTextLine(5, "Distancia: %d cm", min);
	wait1Msec(5000);

}
