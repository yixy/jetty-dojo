package me.youzhilane.dojo;

import org.eclipse.jetty.client.api.Response;
import org.eclipse.jetty.proxy.ProxyServlet;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URI;

public class HelloProxyServlet extends ProxyServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try{
            HelloResponseWrapper helloResponseWrapper=new HelloResponseWrapper(request,response);
            super.service(request, helloResponseWrapper);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected URI rewriteURI(HttpServletRequest request) {
        URI uri=null;
        try {
            uri=new URI("http://192.168.0.109:7777");
        }catch (Exception e){
            e.printStackTrace();
        }
        return uri;
    }

    @Override
    protected void onProxyResponseSuccess(HttpServletRequest request,HttpServletResponse response,Response proxyResponse){
        try {
            if (response instanceof HelloResponseWrapper){
                ((HelloResponseWrapper) response).writeOutput();
            }
            super.onProxyResponseSuccess(request,response,proxyResponse);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    class HelloResponseWrapper extends HttpServletResponseWrapper{
        private HelloProxyOutputStream helloProxyOutputStream;
        private HttpServletRequest request;
        private HttpServletResponse response;

        public HelloResponseWrapper(HttpServletRequest request,HttpServletResponse response){
            super(response);
            helloProxyOutputStream=new HelloProxyOutputStream();
            this.response=response;
            this.request=request;
        }

        @Override
        public ServletOutputStream getOutputStream() throws IOException {
            return helloProxyOutputStream;
        }

        public void writeOutput()throws IOException{
            try {
                String query=request.getQueryString();
                if("1".equals(query)){
                    Thread.sleep(5000);
                }
                response.addIntHeader("Content-Length",-1);
                String result = helloProxyOutputStream.getUtf8OutputStr();
                PrintWriter out=response.getWriter();
                out.write(query+" This is a proxy msg: \n"+result);
                out.flush();
                out.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    class HelloProxyOutputStream extends ServletOutputStream{
        ByteArrayOutputStream outputStream=new ByteArrayOutputStream();

        @Override
        public boolean isReady() {
            throw  new UnsupportedOperationException("has mock servlet output stream, this method is not be supported.");
        }

        @Override
        public void setWriteListener(WriteListener writeListener) {
            throw  new UnsupportedOperationException("has mock servlet output stream, this method is not be supported.");
        }

        @Override
        public void write(int b) {
            outputStream.write(b);
        }

        @Override
        public void write(byte[] b, int off, int len) throws IOException {
            outputStream.write(b,off,len);
        }

        public String getUtf8OutputStr() throws UnsupportedEncodingException{
            String outputStr=outputStream.toString("UTF-8");
            try{
                outputStream.close();
                outputStream=null;
            }catch (IOException e){
                e.printStackTrace();
            }
            return outputStr;
        }
    }


}
