import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class Main {

	public static void main(String[] args) {
		Callable<String> callable = new Callable<String>() {
			@Override
			public String call() throws Exception {
				System.out.println("in call ");
				Thread.sleep(10000);
				String time = "dd";
				System.out.println("in call end");
				return time;
			}
		};
		Runnable runnable = new Runnable() {

			@Override
			public void run() {
				System.out.println("in runnable ");
				try {
					int temp = 1;
					int i = 9 / temp;
					System.out.println(i);
					Thread.sleep(10000);
					String time = "dd";
					System.out.println("in runnable end");
				} catch (InterruptedException e) {
					System.out.println("runnable cancelled");
					throw new RuntimeException("ddd");
				}
			}
		};

		FutureTask<String> task = new FutureTask<String>(runnable, "result");
		// task.run();
		Thread thread = new Thread(task);
		thread.setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
			@Override
			public void uncaughtException(Thread t, Throwable e) {
				System.out.println("uncaughtException");
			}
		});
		thread.start();
		System.out.println("the end");
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
//		System.out.println("cancel task");
//		task.cancel(true);
//		System.out.println("cancel task over");
		try {
			System.out.println("result "+task.get());
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		} catch (ExecutionException e1) {
			e1.printStackTrace();
		}

	}

}
