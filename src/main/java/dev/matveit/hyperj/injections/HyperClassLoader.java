package dev.matveit.hyperj.injections;

import dev.matveit.hyperj.util.*;

import org.slf4j.Logger;

import java.io.*;

public class HyperClassLoader extends ClassLoader {
    private final Logger logger;

    public HyperClassLoader(ClassLoader parent, String threadName) {
        super(HyperClassLoader.class.getSimpleName(), parent);
        assert registerAsParallelCapable();
        this.logger = LoggerUtil.getLogger(threadName);
    }

    public HyperClassLoader(String threadName) {
        this(getSystemClassLoader(), threadName);
    }

    @SuppressWarnings("RedundantThrows")
    private Class<?> getClass(String name) throws ClassNotFoundException {
        String file = name.replace('.', File.separatorChar) + ".class";
        byte[] b;
        try {
            // This loads the byte code data from the file
            b = this.loadClassFileData(file);
            // defineClass is inherited from the ClassLoader class, it converts byte array into a Class.
            Class<?> c = defineClass(name, b, 0, b.length);
            this.resolveClass(c);
            return c;
        } catch (NullPointerException | IOException e) {
            return null;
        }
    }

    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        logger.trace("Loading '" + name + "'");
        Class<?> clazz = getClass(name);
        if (clazz != null) {
            logger.trace("Successfully loaded '" + name + "'");
            return clazz;
        }
        logger.debug("Failed to load class '" + name + "', falling back to internal loader");
        return super.loadClass(name);
    }

    private byte[] loadClassFileData(String name) throws IOException, NullPointerException {
        InputStream stream = getClass().getClassLoader().getResourceAsStream(name);
        assert stream != null;
        int size = stream.available();
        byte[] buff = new byte[size];
        DataInputStream in = new DataInputStream(stream);
        in.readFully(buff);
        in.close();
        return buff;
    }
}
