package client;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.*;

public class CsvHandler {
    // expressman, list of track number

    private static Logger logger = LoggerFactory.getLogger(CsvHandler.class);
    List<ExpressmanTrackNumberData> trackNumbers;
    Iterator<ExpressmanTrackNumberData> customerItr;

    TrackNumberHandler trackNumberHandler = new TrackNumberHandler();

    public TrackNumberHandler getTrackNumberHandler() {
        return trackNumberHandler;
    }

    List<ExpressTrackCustomerData> noExpressmanTrackNumbers;
    Optional<String> dateString = Optional.empty();

    public String getDateStr() {
        if (dateString.isPresent())
            return dateString.get();
        return "";
    }

    public CsvHandler(){
        noExpressmanTrackNumbers = new ArrayList<>();
        trackNumbers = new ArrayList<>();
    }

    public List<ExpressTrackCustomerData> getNoExpressmanTrackNumbers(){
        return noExpressmanTrackNumbers;
    }

    public void resetNoExpressmanTrackNumbers(){
        noExpressmanTrackNumbers.clear();
    }

    public void setTrackNumbers(List<ExpressmanTrackNumberData> trackNumbers){
        this.trackNumbers.addAll(trackNumbers);
    }

    public void parseFile(String fileLocation) {
        File file = new File(fileLocation);
        CSVParser parser = null;
        try {
            parser = CSVParser.parse(file, Charset.forName("GB2312"), CSVFormat.EXCEL);
        } catch (IOException e) {
            logger.error(e.getMessage());
            return;
        }
        setTrackNumbers(getTrackNumbers(parser));
    }

    public void parseFile(List<String> files) throws IOException {
        for (String f: files) {
            logger.info("parse " + f);
            parseFile(f);
        }
    }

    public void putTrackNum(Map<String, List<String>> datas, final String expressman,
                            final String trackNum, final String customer){
        final String keyStr = expressman + "_" + customer;
        List<String> tracks = datas.get(keyStr);
        if (tracks == null) {
            tracks = new ArrayList<>();
            datas.put(keyStr, tracks);
        }
        tracks.add(trackNum);
    }

    public List<ExpressmanTrackNumberData> getTrackNumbers(CSVParser parser){
        Map<String, List<String>> datas = new HashMap<>();
        for (CSVRecord csvRecord : parser) {
            // Accessing Values by Column Index
            final String trackNum = Utils.getCellData(csvRecord, 0);
            if (Utils.trackNumberIsTitle(trackNum)) {
                continue;
            }

            if (!Utils.isTrackNumber(trackNum)) {
                logger.warn("not a track number :=" + trackNum);
                continue;
            }

            if (!dateString.isPresent()) {
                final String dateStr = Utils.getCellData(csvRecord, 5);
                if (!dateStr.isEmpty()) {
                    dateString = Optional.of(Utils.getMouthAndDay(dateStr));
                }
            }
            String expressman = Utils.getCellData(csvRecord, 9);
            String customer = Utils.getCellData(csvRecord, 10);
            if (expressman.isEmpty() || customer.isEmpty()){
                noExpressmanTrackNumbers.add(new ExpressTrackCustomerData(
                        expressman, trackNum, customer));
                continue;
            }
            putTrackNum(datas, expressman, trackNum, customer);
        }
        List<ExpressmanTrackNumberData> retDatas = new ArrayList<>();
        datas.forEach((k,v)->{
            ExpressmanTrackNumberData numData = new ExpressmanTrackNumberData(k, v);
            retDatas.add(numData);
        });

        return retDatas;
    }

    public void merge(){
        Map<String, List<String>> datas = new HashMap<>();
        trackNumbers.forEach((v)->{
            List<String> tracks = datas.get(v.expressman);
            trackNumberHandler.addTrackNumbers(v.trackNumbers);
            if (tracks == null) {
                datas.put(v.expressman, v.trackNumbers);
            } else {
                tracks.addAll(v.trackNumbers);
            }
        });

        trackNumbers.clear();
        datas.forEach((k,v)->{
            ExpressmanTrackNumberData numData = new ExpressmanTrackNumberData(k, v);
            trackNumbers.add(numData);
        });
        customerItr = trackNumbers.iterator();
    }

    public static void main(String[] args) throws IOException {
        final String fileName = "D:\\wfgp_util\\data\\杭州女装网.CSV";
        CsvHandler handler = new CsvHandler();
        handler.parseFile(fileName);
        int itemCount = 0;
        while (handler.hasNext()){
            ExpressmanTrackNumberData numData = handler.next();
            itemCount += numData.trackNumSize;
            System.out.println("key: " + numData.expressman + " size: " + numData.trackNumSize);
            //for (String num : numData.trackNumbers) {
                //System.out.println("      " + num);
            //}
            while(numData.hasElement()){
                final String tracknums = numData.getElement();
                System.out.println("key :" + numData.expressman + "\n" + tracknums);
            }
        }
        System.out.println(handler.getDateStr());
        System.out.println("total count : " + itemCount);
    }

    public ExpressmanTrackNumberData next(){
        return customerItr.next();
    }

    public boolean hasNext(){
        return customerItr.hasNext();
    }
}
