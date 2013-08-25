task main(){
	//Variables definitions
	TFileHandle calFile;         					// create a file handle variable 'lightCal'
	TFileIOResult IOResult;           		// create an IO result variable 'IOResult'
	string lightFile = "lightCal.dat";		// create and initialize a string variable 'lightFile'
//	string soundFile = "soundCal.dat";	// create and initialize a string variable 'soundFile'
	int myFileSize = 10;              		// create and initialize an integer variable 'myFileSize'
	float aveLight, devLight;
//	float aveSound, devSound;

	//Read Light Calibration
	OpenRead(calFile, IOResult, lightFile, myFileSize);
	ReadFloat(calFile, IOResult, aveLight);  // read a float from the file and store it to 'Aver'
	ReadFloat(calFile, IOResult, devLight);  // read a float from the file and store it to 'Devia'
	Close(calFile, IOResult);
/*
	//Read Sound Calibration
	OpenRead(calFile, IOResult, soundFile, myFileSize);
	ReadFloat(calFile, IOResult, aveSound);  // read a float from the file and store it to 'Aver'
	ReadFloat(calFile, IOResult, devSound);  // read a float from the file and store it to 'Devia'
	Close(calFile, IOResult);
*/
	nxtDisplayString(0, "Luz");
	nxtDisplayString(1, ">Promedio  : %2d", aveLight);
	nxtDisplayString(2, ">Desviacion: %2d", devLight);
/*
	nxtDisplayString(4, "Sonido");
	nxtDisplayString(5, ">Promedio  : %2d", aveSound);
	nxtDisplayString(6, ">Desviacion: %2d", devSound);
*/	wait1Msec(2000);
}
