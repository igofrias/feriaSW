#pragma config(Sensor, S3, colourSensor, sensorCOLORFULL)

task main(){
	while (true){
		SetSensorType(colourSensor, sensorCOLORBLUE);
		wait1Msec(300);
		SetSensorType(colourSensor, sensorCOLORRED);
		wait1Msec(300);
		SetSensorType(colourSensor, sensorCOLORGREEN);
		wait1Msec(300);
		SetSensorType(colourSensor, sensorCOLORFULL);
		wait1Msec(300);
	}
}
