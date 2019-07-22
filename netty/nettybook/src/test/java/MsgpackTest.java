import org.msgpack.MessagePack;
import org.msgpack.template.Templates;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MsgpackTest {

    public static void main(String[] args) throws IOException {
        List<String> src = new ArrayList<String>();
        src.add("msgpack");
        src.add("kumofs");
        src.add("viver");
        MessagePack pack = new MessagePack();
        byte[] raw = pack.write(src);
        System.out.println(raw.length);
        List<String> dist1 = pack.read(raw, Templates.tList(Templates.TString));
        for (String c : dist1) {
            System.out.println(c);
        }
    }
}
