task HappyEyes();

task main(){
	StartTask(HappyEyes);
	wait1Msec(5000);
	StopTask(HappyEyes);
}

task HappyEyes(){
	while(true){
		nxtDisplayRICFile(0, 0, "HappyEyes.ric");
	}
}
