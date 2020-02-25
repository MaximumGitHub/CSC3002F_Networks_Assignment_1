import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

/**
 * A simple TCP server. When a client connects, it sends the client the current
 * datetime, then closes the connection. This is arguably the simplest server
 * you can write. Beware though that a client has to be completely served its
 * date before the server will be able to handle another client.
 */
public class server
{
    public final static String fileIN = "c:/temp/source-downloaded.jpg";
    public static void main(String[] args) throws IOException
    {
        int bytesRead;
        int current = 0;
        try (var listener = new ServerSocket(59090))
        {
            System.out.println("The date server is running...");
            while (true)
            {
                try (var socket = listener.accept()) {
                    byte[] byteArr = new byte[6022386];
                    DataOutputStream output = null;
                    BufferedOutputStream buffer = null;
                    try {
                        output = new DataOutputStream(socket.getOutputStream());
                    } catch (IOException e) {
                        System.out.println(e);
                    }
                    DataInputStream input = null;
                    try {
                        input = new DataInputStream(socket.getInputStream());
                    } catch (IOException e) {
                        System.out.println(e);
                    }
                    //OutputStream temp = new OutputStream(fileIN);                    }
                    output = new DataOutputStream(new FileOutputStream(fileIN));
                    buffer = new BufferedOutputStream(output);
                    bytesRead = input.read(byteArr, 0, byteArr.length);
                    current = bytesRead;

                    do {
                        bytesRead = input.read(byteArr, current, (byteArr.length - current));
                        if (bytesRead >= 0) {
                            current += bytesRead;
                        }
                    }
                        while (bytesRead > -1) ;
                        buffer.write(byteArr, 0, current);
                        buffer.flush();
                        System.out.println("File Downloaded");
                }
            }
        }
    }
}