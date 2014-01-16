import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import com.hyn.xtask.IXFutureTask;
import com.hyn.xtask.XAsyncTask;
import com.hyn.xtask.example.TestTask;

public class Main {
	private static final int MAX_COUNT = 200;

	public static void main(String[] args) {
		XAsyncTask taskm = new XAsyncTask(10);
		List<IXFutureTask> tl = new ArrayList<IXFutureTask>();
		for(int i=0;i<MAX_COUNT;++i){
			TestTask task = new TestTask(i);
			tl.add(task);
			taskm.post(task);
		}
		
		int count = 0;
		for(int i=0;count<MAX_COUNT;i+=3){
			int index = i%MAX_COUNT;
			taskm.cancel(tl.get(index));
			++count;
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		try {
			Thread.sleep(1000000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
