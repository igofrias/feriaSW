task OpenCloseEyes();

task main(){
	StartTask(OpenCloseEyes);
	wait1Msec(10000);
	StopTask(OpenCloseEyes);
	return;
}

task OpenCloseEyes(){
	while(true){
		nxtDisplayRICFile(0, 0, "OpenEyes.ric");
		wait1Msec(1500);
		nxtDisplayRICFile(0, 0, "CloseEyes.ric");
		wait1Msec(200);
	}
}
