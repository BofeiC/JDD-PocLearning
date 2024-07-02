package hessian;

import com.caucho.hessian.io.Hessian2Input;

import java.io.*;
import java.util.concurrent.Callable;

public class Deserializer implements Callable<Object> {
	private final byte[] bytes;

	public Deserializer(byte[] bytes) { this.bytes = bytes; }

	public Object call() throws Exception {
		return deserialize(bytes);
	}

	public static Object deserialize(final byte[] serialized) throws IOException, ClassNotFoundException {
		final ByteArrayInputStream in = new ByteArrayInputStream(serialized);
		return deserialize(in);
	}

	public static Object deserialize(final InputStream in) throws ClassNotFoundException, IOException {
        Hessian2Input hessian2Input = new Hessian2Input(in);
		return hessian2Input.readObject();
	}

	public static void main(String[] args) throws ClassNotFoundException, IOException {
		final InputStream in = args.length == 0 ? System.in : new FileInputStream(new File(args[0]));
		Object object = deserialize(in);
	}
}
