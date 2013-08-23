task main(){
	//Variables definitions
	TFileHandle lightCal;         			// create a file handle variable 'lightCal'
	TFileIOResult IOResult;           	// create an IO result variable 'IOResult'
	string lightFile = "lightCal.dat";	// create and initialize a string variable 'myFileName'
	int myFileSize = 10;              	// create and initialize an integer variable 'myFileSize'
	float Aver, Devia;

	OpenRead(lightCal, IOResult, lightFile, myFileSize);
	ReadFloat(lightCal, IOResult, Aver);  // read a float from the file and store it to 'Aver'
	ReadFloat(lightCal, IOResult, Devia);  // read a float from the file and store it to 'Devia'
	Close(lightCal, IOResult);

	nxtDisplayString(3, "Promedio  : %2d", Aver);
	nxtDisplayString(4, "Desviacion: %2d", Devia);
	wait1Msec(2000);
}
