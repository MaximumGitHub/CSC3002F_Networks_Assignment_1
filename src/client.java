
import java.io.IOException;
import java.net.*;

    public class client
    {

        public static void main(String[] args) throws IOException
        {
            Foo();
        }
        static void Foo() throws IOException {

            Socket client = new Socket(InetAddress.getLocalHost(),1024); //Initialize server socket here.

            String hostName = client.getInetAddress().getHostName();

            System.out.println(hostName);
        }
    }
