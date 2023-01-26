import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.util.concurrent.Executor;

public enum EmulatedHttpServer implements Runnable {
   INSTANCE;

   public void run() {
      try {
         System.out.println("Booting up http emulated server...");
         System.out.println("Creating http server");
         HttpServer server = HttpServer.create(new InetSocketAddress(80), 0);
         server.createContext("/", new EmulatedHttpServer.Handler());
         server.setExecutor((Executor)null);
         System.out.println("Successfully created");
         System.out.println("Starting..");
         server.start();
         System.out.println("Successfully started");
      } catch (Exception var2) {
         var2.printStackTrace();
      }

   }

   public static byte[] readAllBytes(InputStream inputStream) throws IOException {
      int bufLen = 214748;
      byte[] buf = new byte[214748];
      IOException exception = null;

      try {
         ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

         int readLen;
         while((readLen = inputStream.read(buf, 0, 214748)) != -1) {
            outputStream.write(buf, 0, readLen);
         }

         byte[] var6 = outputStream.toByteArray();
         return var6;
      } catch (IOException var15) {
         exception = var15;
         throw var15;
      } finally {
         if (exception == null) {
            inputStream.close();
         } else {
            try {
               inputStream.close();
            } catch (IOException var14) {
               exception.addSuppressed(var14);
            }
         }

      }
   }

   private static class Handler implements HttpHandler {
      private Handler() {
      }

      public void handle(HttpExchange exchange) throws IOException {
         try {
            byte[] b = EmulatedHttpServer.readAllBytes(exchange.getRequestBody());
            System.out.print(exchange.getRequestURI().toString() + String.format("(%s)", exchange.getRequestMethod()) + ": ");
            System.out.println(new String(b));
            this.response("0x3", exchange);
         } catch (Exception var3) {
            var3.printStackTrace();
         }

      }

      private void response(String data, HttpExchange exchange) throws IOException {
         byte[] b = data.getBytes();
         exchange.sendResponseHeaders(200, (long)b.length);
         exchange.getResponseBody().write(b);
         exchange.getResponseBody().flush();
      }

      // $FF: synthetic method
      Handler(Object x0) {
         this();
      }
   }
}
