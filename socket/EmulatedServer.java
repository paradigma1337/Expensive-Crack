import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public enum EmulatedServer implements Runnable {
   INSTANCE;

   public void run() {
      try {
         long startTime = System.currentTimeMillis();
         System.out.println("Booting up server emulator...");
         System.out.println("Reading response");
         InputStream responseStream = ClassLoader.getSystemResourceAsStream("response");
         if (responseStream != null) {
            byte[] response = readAllBytes(responseStream);
            System.out.println("Successfully read the response");
            System.out.println("Creating server...");
            ServerSocket serverSocket = new ServerSocket(2817);
            System.out.printf("Successfully created server in %dms\n", System.currentTimeMillis() - startTime);
            System.out.println("Listening...");

            while(true) {
               Socket socket = serverSocket.accept();
               System.out.println(socket.getInetAddress().toString() + " requested data");
               DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
               outputStream.write(response);
               outputStream.flush();
               socket.shutdownOutput();
               System.out.println(socket.getInetAddress().toString() + " received response");
            }
         }

         throw new RuntimeException("Unable to read response, be sure you ve put it into the classpath");
      } catch (Exception var8) {
         var8.printStackTrace();
      }
   }

   public static byte[] readAllBytes(InputStream inputStream) throws IOException {
      int bufLen = 10737418;
      byte[] buf = new byte[10737418];
      IOException exception = null;

      try {
         ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

         int readLen;
         while((readLen = inputStream.read(buf, 0, 10737418)) != -1) {
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
}
