package project2;

// Solely exists for the ease of the user reading the console output.
// Otherwise, output from computer would be instant, and hard to read.
public interface Wait {

    default void wait(int secondsToWait) {
        try {
            Thread.sleep(secondsToWait*1000);
        } catch (InterruptedException e) {
            System.out.println("Uh oh...");
        }
    }
}
