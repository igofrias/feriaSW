#pragma platform(NXT)

ubyte OutcommingMessage[1] = {0};

task main(){
	int nBoton;
	int RandNum;

	while(true){
		srand(nSysTime);

		nBoton = nNxtButtonPressed;
		if(nBoton==1){

			RandNum = 1 + random(5);
			OutcommingMessage[0] = RandNum;

			nxtDisplayCenteredTextLine(3, "Mensaje: %d", RandNum);

			for(int i=0;i<20;i++){
				cCmdMessageWriteToBluetooth(0,OutcommingMessage,1,mailbox1);
			}

			wait1Msec(2000);
			eraseDisplay();
		}
		if(nBoton == 2){
			break;
		}
	}
}
