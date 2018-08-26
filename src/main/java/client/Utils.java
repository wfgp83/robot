package client;

import org.apache.commons.csv.CSVRecord;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Utils {

    public static String getExportDataFileName(final String dir, final String customerName) {
        StringJoiner sb = new StringJoiner("", dir + "\\rawData\\", ".CSV");
        sb.add(customerName);
        return sb.toString();
    }

    public static String getResultFileName(final String dir, final String customerName) {
        StringJoiner sb = new StringJoiner("", dir + "\\output\\", ".XLS");
        sb.add(customerName);
        return sb.toString();
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
            return null;
        }
        String pattern = "yyyy-MM-dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        Date date = null;
        try {
            date = simpleDateFormat.parse(dateStr);
        } catch (ParseException e) {
            System.out.println("Invalid date " + dateStr);
        }
        if (date == null)
            return null;
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

    public static String getCellData(CSVRecord csvRecord, int idx){
        String data = "";
        try {
            data = csvRecord.get(idx);
            data = data.trim();
        } catch (ArrayIndexOutOfBoundsException e){
            System.err.println("no csv record at cell " + idx);
        }
        return data;
    }
}
