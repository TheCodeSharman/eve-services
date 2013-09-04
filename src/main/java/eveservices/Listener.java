package eveservices;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.InflaterOutputStream;

import org.zeromq.ZContext;
import org.zeromq.ZFrame;
import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Socket;
import org.zeromq.ZMsg;

import uudi.UUDIFactory;
import uudi.UUDIResult;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Listener {

	public static class EMDRSink extends Thread {
		String uri;
		public EMDRSink(String uri) {
			this.uri = uri;
		}

		@Override
		public void run() {

			ObjectMapper mapper = new ObjectMapper();

			mapper.configure( com.fasterxml.jackson.databind.SerializationFeature.INDENT_OUTPUT, true);
			mapper.configure( com.fasterxml.jackson.core.JsonParser.Feature.AUTO_CLOSE_SOURCE, false);
			mapper.configure( com.fasterxml.jackson.core.JsonGenerator.Feature.AUTO_CLOSE_TARGET, false);

			ZContext ctx = new ZContext();
			Socket emdr = ctx.createSocket(ZMQ.SUB);
			emdr.connect(uri);
			emdr.subscribe(new byte[0]);

			while (!isInterrupted()) {
				try {
					ByteArrayOutputStream buff = new ByteArrayOutputStream();
					InflaterOutputStream inflater = new InflaterOutputStream(buff);
					for (ZFrame z : ZMsg.recvMsg(emdr)) {
						inflater.write(z.getData());
					}

					UUDIResult res = UUDIFactory.parseUUDIResultFromJson( buff.toByteArray(), mapper);

					System.out.println("\n>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
					mapper.writeValue(System.out, res);
					System.out.println("\n<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<\n");

					inflater.close();

				} catch (IOException e) {
					System.err.println("Unable to decompress packet: discarding: " + e);
				}
			}

			emdr.close();
			ctx.close();
		}

	}

	public static void main(String[] args) throws IOException, InterruptedException {
		EMDRSink emdrSink = new EMDRSink("tcp://relay-us-central-1.eve-emdr.com:8050");
		emdrSink.start();
	}

}
