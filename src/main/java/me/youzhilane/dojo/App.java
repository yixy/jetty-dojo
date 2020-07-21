package me.youzhilane.dojo;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.util.log.Log;

public class App {
    public static void main(String[] args)throws Exception{
        Log.setLog(new Jetty2Log4j2Bridge("Jetty-Server"));
        Server server=new Server(7070);
        ServletContextHandler handler=new ServletContextHandler(server,"/");
        handler.addServlet(HelloProxyServlet.class,"/hello");
        handler.addServlet(HelloAsyncMiddleManServlet.class,"/midhello");
        server.start();
    }

}
