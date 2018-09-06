package client;

import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.rmi.CORBA.Util;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class TrackNumberHandler {

    private static Logger logger = LoggerFactory.getLogger(TrackNumberHandler.class);
    private List<String> trackNumbers = new ArrayList<>();
    private int currentIdx;
    private int trackNumSize;
    private static final int PAGE_SIZE = 900;

    public void setTrackNumbers(List<String> trackNumbers){
        this.trackNumbers = trackNumbers;
        currentIdx = 0;
        trackNumSize = this.trackNumbers.size();
    }

    public void addTrackNumbers(List<String> trackNumbers){
        this.trackNumbers.addAll(trackNumbers);
        currentIdx = 0;
        trackNumSize = this.trackNumbers.size();
    }

    public String getCustomerAndExpressman(Sheet sheet){
        Row rawRow = sheet.getRow(1);
        if (rawRow == null){
            System.err.println("no row 1 in sheet");
            return "";
        }
        Cell c9 = rawRow.getCell(9);
        Cell c10 = rawRow.getCell(10);
        //Cell c5= rawRow.getCell(5);
        if (c9 == null || c10 == null){
            System.err.println("cell 9 or 10 or 4 is null in row 1 in sheet");
            return "";
        }

        StringJoiner sb = new StringJoiner("-");
        sb.add(Utils.getCellContent(c9));
        sb.add(Utils.getCellContent(c10));
        String ret = sb.toString();
        System.out.println("getCustomerAndExpressman ret := " + ret);
        return ret;
    }

    public static void main(String[] args) throws Exception {
        final String fileName = "D:\\wfgp_util\\data\\杭州女装网.XLS";
        TrackNumberHandler fh = new TrackNumberHandler();
        System.out.println(fh.parseForOutputFileName(fileName));
    }

    public String parseForOutputFileName(final String fileName) throws Exception{
        FileInputStream file = new FileInputStream(new File(fileName));
        Workbook workbook = new HSSFWorkbook(file);
        //WorkbookFactory.create();
        Sheet sheet = workbook.getSheetAt(0);
        return getCustomerAndExpressman(sheet);
    }

    public List<String> getTrackNumbers(Sheet sheet){
        List<String> rets = new ArrayList<>();
        for(Row row : sheet){
            Cell c0 = row.getCell(0);
            if (c0 == null){
                System.err.println("no cell 0");
                continue;
            }
            final String content = Utils.getCellContent(c0);
            if (Utils.isTrackNumber(content))
                rets.add(content);
            else
                System.err.println("not track number " + content);
        }
        return rets;
    }

    public void parseFile(String fileLocation) throws Exception {
        FileInputStream file = new FileInputStream(new File(fileLocation));
        Workbook workbook = new HSSFWorkbook(file);
        //WorkbookFactory.create();
        Sheet sheet = workbook.getSheetAt(0);
        setTrackNumbers(getTrackNumbers(sheet));
    }

    public boolean hasElement(){
        return trackNumSize > currentIdx;
    }

    public String getElement(){
        if (trackNumSize <= PAGE_SIZE) {
            logger.info("track number size:  total size: "+ trackNumSize + " actual size:" + trackNumbers.size());
            currentIdx = trackNumSize;
            return Utils.joinElement(trackNumbers);
        }
        int endIdx = currentIdx + PAGE_SIZE;
        if (endIdx > trackNumSize)
            endIdx = trackNumSize;
        List<String> subList = trackNumbers.subList(currentIdx, endIdx);
        logger.info("export index: " + endIdx+ " total size: "+ trackNumSize);

        currentIdx = endIdx;
        return Utils.joinElement(subList);
    }
}
