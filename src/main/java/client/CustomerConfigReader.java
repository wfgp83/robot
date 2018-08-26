package client;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerConfigReader {

    public static List<String> getDatas(final String dir) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(
                new FileInputStream(Utils.getCustomerConfiFileName(dir)), "UTF-8"));
        List<String> datas = new ArrayList<>();
        boolean isFirst = true;
        while (true) {
            final String line = reader.readLine();
            if (line == null) break;
            if (isFirst){
                isFirst = false;
                continue;
            }
            datas.add(line);
        }
        return datas;
    }
}
