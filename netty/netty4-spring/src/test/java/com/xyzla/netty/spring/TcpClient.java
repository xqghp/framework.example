package com.xyzla.netty.spring;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

public class TcpClient {

    private static String serverIp = "127.0.0.1";
    private static int serverPort = 9090;

    public static void main(String[] args) throws IOException {
        byte[] b = "2013年5月，习近平在天津调研时提出，要谱写新时期社会主义现代化的京津“双城记”。2013年8月，习近平在北戴河主持研究河北发展问题时，又提出要推动京津冀协同发展。此后，习近平多次就京津冀协同发展作出重要指示。三地推进协同发展的共识加快形成，方向日渐清晰。尤其是2017年4月1日，设立雄安新区的消息一经公布，举世瞩目。".getBytes();
        send(b);
    }


    public static byte[] send(byte[] sendData) throws UnknownHostException, IOException {
        Socket socket = new Socket(serverIp, serverPort);
        OutputStream os = socket.getOutputStream();
        InputStream is = socket.getInputStream();
        byte resultArray[] = new byte[1];
        try {
//            BufferedReader in = new BufferedReader(new InputStreamReader(is));

            // 定义一个发送消息协议格式：|--header:4 byte--|--content:10MB--|
            // 获取一个4字节长度的协议体头
            byte[] dataLength = intToByteArray(4, sendData.length);
            // 和请求的数据组成一个请求数据包
            byte[] requestMessage = combineByteArray(dataLength, sendData);
            //发送数据-------------------------------
            os.write(requestMessage);
            os.flush();
            //接收数据-------------------------------
//            resultArray = IOUtils.toByteArray(is);
            System.out.println("11--in:" + is + "  --out:" + os);
            byte ibuf[] = new byte[1024];
            int len = is.read(ibuf);
            String s = new String(ibuf, 0, len);
            System.out.print(len + s);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            os.close();
            is.close();
            socket.close();
        }
        return resultArray;
    }

    private static byte[] intToByteArray(int byteLength, int intValue) {
        return ByteBuffer.allocate(byteLength).putInt(intValue).array();
    }

    private static byte[] combineByteArray(byte[] array1, byte[] array2) {
        byte[] combined = new byte[array1.length + array2.length];
        System.arraycopy(array1, 0, combined, 0, array1.length);
        System.arraycopy(array2, 0, combined, array1.length, array2.length);
        return combined;
    }

}
