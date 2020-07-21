package me.youzhilane.dojo;

import org.eclipse.jetty.client.api.Response;
import org.eclipse.jetty.proxy.AsyncMiddleManServlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.ByteBuffer;
import java.util.List;

public class HelloAsyncMiddleManServlet extends AsyncMiddleManServlet {

    @Override
    protected String rewriteTarget(HttpServletRequest clientRequest){
        return "http://localhost:7777";
    }

    @Override
    protected ContentTransformer newServerResponseContentTransformer(HttpServletRequest clientRequest, HttpServletResponse proxyResponse, Response serverResponse){
        return new MyContentTransformer(clientRequest);
    }

    private static class MyContentTransformer implements ContentTransformer
    {
        private HttpServletRequest clientRequest;
        public MyContentTransformer(HttpServletRequest clientRequest){
            this.clientRequest=clientRequest;
        }
        public void transform(ByteBuffer input, boolean finished, List<ByteBuffer> output)
        {
            System.out.println("**********************");
//            Charset charset = Charset.forName("utf-8");
//            CharBuffer charBuffer = charset.decode(input);
//            String s = charBuffer.toString()+ "  additional message.";
//            System.out.println(s);
//            try {
//                ByteBuffer buffer = ByteBuffer.wrap(s.getBytes("utf-8"));
//                output.add(buffer);
//            } catch (UnsupportedEncodingException e) {
//                e.printStackTrace();
//            }
            String query=clientRequest.getQueryString();
            if("1".equals(query)){
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            output.add(input);
            System.out.println("**********************");
//            try {
//                Thread.sleep(10000);
//
//            }catch (InterruptedException e){
//
//            }
        }
    }
}
