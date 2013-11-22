task ShameEyes();

task main(){
	StartTask(ShameEyes);
	wait1Msec(5000);
	StopTask(ShameEyes);
}

task ShameEyes(){
	while(true){
		nxtDisplayRICFile(0, 0, "ShameEyes.ric");
	}
}
