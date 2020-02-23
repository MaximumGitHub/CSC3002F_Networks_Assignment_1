import java.io.IOException;
import java.net.ServerSocket;

public class server
{

    public static void main(String[] args)
    {

    ServerSocket ourServer;
    try {
        ourServer = new ServerSocket(1024);
        }
    catch(IOException e)
    {
        System.out.println(e);
    }
    }
}
