package example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

@SuppressWarnings("unused")
public class App {
    private static final Logger logger = LoggerFactory.getLogger("App");
    public static void main$(String[] args) {
        logger.info("Hello World !");
        logger.info(Arrays.toString(args));
        logger.info("Thread: " + Thread.currentThread().getContextClassLoader().getName());
        logger.info("Class: " + App.class.getClassLoader().getName());
    }
}
