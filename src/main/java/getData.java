import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class getData {

    public static void main(String[] args) throws Exception{
        //FileInputStream input = new FileInputStream("D:\\song\\data.txt");
        BufferedReader reader=new BufferedReader(
                new FileReader("D:\\song\\data.txt"));
        List<String> datas = new ArrayList<>();
        while(reader.ready()){
            String line = reader.readLine();
            if(line.contains("元")) {
                datas.add(line);
            }
        }
        Collections.sort(datas, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return  getMoney(o2) - getMoney(o1);
            }
        });
        int total = 0;
        for (String data: datas) {
            //System.out.println(data);
            total += getMoney(data);
        }
        System.out.println("total = " + total);

        for (String data: datas) {
            if (data.contains("魏") || data.contains("尚") ||  data.contains("仪"))
            System.out.println(data);
            //total += getMoney(data);
        }

    }

    private static int getMoney(String line){
        int startIdx = line.indexOf("帮助了 ");
        int endIdx = line.indexOf(" 元");
        return Integer.parseInt(line.substring(startIdx+4, endIdx));
    }
}
