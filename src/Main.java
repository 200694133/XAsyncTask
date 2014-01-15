import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import com.hyn.xtask.XAsyncTask;
import com.hyn.xtask.example.TestTask;

public class Main {

	public static void main(String[] args) {
		XAsyncTask<String, String> taskm = new XAsyncTask<String, String>(10);
		for(int i=0;i<200;++i){
			TestTask task = new TestTask();
			taskm.post(task);
		}
		
		try {
			Thread.sleep(1000000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
