#define SONIDO 25

task main(){
	SetSensorType(S1,sensorSoundDBA);	//Activamos el sensor de sonido
	wait1Msec(1000);									//Esperamos un segundo para activa el microfono

	while(true){											//ciclo infinito
		if(SensorValue(S1) < SONIDO){		//Busca girando en su propio eje
			motor[motorB] = -10;					//Un motor gira para enfrente y otro para atras
			motor[motorC] = 20;
		}
		else{														//Encuentra el sonido y avanza hacia el
			motor[motorB] = 50;						//Los dos motores giran hacia el frente
			motor[motorC] = 50;
		}
	}
}

/*
	task funcion_X()
	{

	}
	task funcion_Y()
	{
	}
	task main()
	{
		Precedes(funcion_X, funcion_Y);
	}
	*/

