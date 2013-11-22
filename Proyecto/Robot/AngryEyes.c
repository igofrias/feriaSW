task AngryEyes();

task main(){
	StartTask(AngryEyes);
	wait1Msec(5000);
	StopTask(AngryEyes);
}

task AngryEyes(){
	while(true){
		nxtDisplayRICFile(0, 0, "AngryEyes.ric");
	}
}
