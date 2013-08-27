package com.Phyrex.demo;

import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

class Hilitos extends Thread {


		/** Message handler used by thread to interact with TextView */
		private Handler mHandler;
		private boolean mRun = false;
		
		public Hilitos(Handler handler) {
			mHandler = handler;
		}


		/**
		 * Restores game state from the indicated Bundle. Typically called when
		 * the Activity is being restored after having been previously
		 * destroyed.
		 * 
		 * @param savedState
		 *            Bundle containing the game state
		 */
		public synchronized void restoreState(Bundle savedState) {

		}

		@Override
		public void run() {
		    
		//	Log.d(TAG, "--run--");
			while (mRun) {
				
				// sleep some time
				try {
				    Thread.sleep(30);
				}
				catch (InterruptedException e) {
				}

			}
		}
		
		public void setRunning(boolean b){
			mRun=b;
		}
		
		

}

