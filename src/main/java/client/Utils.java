package client;

import org.apache.commons.csv.CSVRecord;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Utils {

    private static Logger logger = LoggerFactory.getLogger(Utils.class);

    public static String getExportDataFileName(final String dir, final String customerName) {
        return getFileName(dir, customerName, "rawData", ".CSV");
    }

    public static String getTrackNumberForExportDataFileName(final String dir, final String customerName) {
        return getFileName(dir, customerName, "rawData", ".txt");
    }

    public static String getResultFileName(final String dir, final String customerName) {
        return getFileName(dir, customerName, "output", ".XLS");
    }

    public static String getNoMatchFileName(final String dir, final String customerName) {
        return getFileName(dir, customerName, "noMatch", ".txt");
    }

    public static String getTrackNumberFileName(final String dir, final String customerName) {
        return getFileName(dir, customerName, "trackNumber", ".txt");
    }

    private static String getFileName(final String dir, final String customerName,
                                      final String subDir, final String suffix) {
        StringJoiner sb = new StringJoiner("", dir + "\\" + subDir +"\\", suffix);
        sb.add(customerName);
        return sb.toString();
    }

    public static String joinElement(List<String> elements){
        StringJoiner sj = new StringJoiner("\r\n");
        for (String trackNumber : elements) {
            sj.add(trackNumber);
        }
        return sj.toString();
    }

    public static boolean isFileExist(final String fileName) {
        if (fileName == null || fileName.isEmpty())
            return false;
        File checkFile = new File(fileName);
        return checkFile.exists();
    }

    public static boolean isDirExist(final String fileName) {
        if (fileName == null || fileName.isEmpty())
            return false;
        File checkFile = new File(fileName);
        return checkFile.isDirectory();
    }

    public static boolean createDir(final String fileName) {
        if (fileName == null || fileName.isEmpty())
            return false;
        File checkFile = new File(fileName);
        return checkFile.mkdir();
    }

    public static void deleteFile(final String fileName) {
        if (fileName == null || fileName.isEmpty())
            return;
        File checkFile = new File(fileName);
        checkFile.delete();
    }

    public static String getMouseLocationFileName(final String dir) {
        return dir + "\\conf\\mouse_location.txt";
    }

    public static String getCustomerConfiFileName(final String dir) {
        return dir +"\\conf\\customer.txt";
    }

    public static String getCellContent(Cell cell){
        String ret = "";
        switch (cell.getCellTypeEnum()) {
            case STRING:
                ret = cell.getRichStringCellValue().getString();
                break;
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    ret = cell.getDateCellValue() + "";
                } else {
                    ret = cell.getNumericCellValue() + "";
                }
                break;
            case BOOLEAN:
                ret = cell.getBooleanCellValue() + "";
                break;
            case FORMULA:
                ret = cell.getCellFormula();
                break;
            default:
        }
        return ret;
    }

    public static String getMouthAndDay(final String dateStr) {
        if (dateStr == null || dateStr.isEmpty()){
            return "";
        }
        String pattern = "yyyy-MM-dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        Date date = null;
        try {
            date = simpleDateFormat.parse(dateStr);
        } catch (ParseException e) {
            logger.error("Invalid date " + dateStr);
        }
        if (date == null)
            return "";
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        int mouth = calendar.get(Calendar.MONTH)+1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        return mouth + "-" + day;
    }

    public static String getFileNameWithPrefixIndex(final String name, int indx){
        return indx + name;
    }

    public static boolean isTrackNumber(final String content){
        final String regex = "\\d+";
        return  (!content.isEmpty() && content.matches(regex));
    }

    public static boolean trackNumberIsTitle(final String trackNum) {
        return (trackNum.isEmpty() ||
                trackNum.startsWith("记录数") ||
                trackNum.startsWith("运单编号") ||
                trackNum.startsWith("基本信息") ||
                trackNum.startsWith("订单编号") ||
                trackNum.startsWith("百世单号"));
    }

    public static String getCellData(CSVRecord csvRecord, int idx){
        String data = "";
        try {
            data = csvRecord.get(idx);
            data = data.trim();
        } catch (ArrayIndexOutOfBoundsException e){
            logger.error("no csv record at cell " + idx);
        }
        return data;
    }
}
