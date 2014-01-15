package com.hyn.xtask.example;

import java.util.concurrent.CancellationException;

import com.hyn.xtask.XLog;
import com.hyn.xtask.XTask;

public class TestTask extends XTask<String, String>{
	private static int sIndex = 0;
	@Override
	public String runInBackground() throws InterruptedException,
			CancellationException {
		
		Thread.sleep(500);
		
		XLog.d("Test", "test");
		
		Thread.sleep(500);
		XLog.d("Test", ""+sIndex);
		return ""+sIndex++;
	}

}
