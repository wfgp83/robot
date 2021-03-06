package client;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CsvOutputHandler{

    private static Logger logger = LoggerFactory.getLogger(CsvOutputHandler.class);
    private static int invalidItemCount;

    public static int getInvalidItemCount() {
        return invalidItemCount;
    }

    public static void increaseInvalidItemCount() {
        invalidItemCount++;
    }

    public static void resetInvalidItemCount() {
        invalidItemCount = 0;
    }

    public static boolean isFileEmpty(final String fullName, int waitTime) {
        // first time for a file
        if (waitTime == 1) {
            return true;
        }

        File file = new File(fullName);
        if (!file.exists()) {
            return true;
        }

        CSVParser parser = null;
        try {
            parser = CSVParser.parse(file, Charset.forName("GB2312"), CSVFormat.EXCEL);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        Set<ExcelData> datas = getRequiredDatas(parser, true);
        boolean isEm = datas.isEmpty();
        if (isEm) {
            logger.info("retry export file " + fullName);
        }
        return isEm;
    }

    public static List<ExcelData>  parseFile(final String dir, final String cutomerName,
                                             int idx) throws Exception {
        List<ExcelData> rets = new ArrayList<>();
        resetInvalidItemCount();
        for(int prefix = 1; prefix <= idx; prefix++) {
            final String fileName =  Utils.getExportDataFileName(dir,
                    Utils.getFileNameWithPrefixIndex(cutomerName, prefix));
            logger.info("parse " + fileName);
            File file = new File(fileName);
            CSVParser parser = CSVParser.parse(file, Charset.forName("GB2312"), CSVFormat.EXCEL);
            rets.addAll(getRequiredDatas(parser, false));
        }
        processProvince(rets);
        return rets;
    }

    public static void processProvince(List<ExcelData> rets){
        for (ExcelData data: rets) {
            if (data.province.length() > 4)
                data.province = data.province.substring(0, 2);
        }
    }

    public static Set<ExcelData> getRequiredDatas(CSVParser parser, boolean isCheckFileEmpty){
        Set<ExcelData> rets = new HashSet<>();
        if (parser == null) {
            return rets;
        }
        for (CSVRecord csvRecord : parser) {
            // Accessing Values by Column Index
            final String trackNum = Utils.getCellData(csvRecord, 3);
            if (Utils.trackNumberIsTitle(trackNum)) {
                continue;
            }

            if (isCheckFileEmpty) {
                rets.add(new ExcelData(trackNum, "", ""));
                break;
            }

            if (!Utils.isTrackNumber(trackNum)) {
                logger.warn("not a track number :=" + trackNum);
                increaseInvalidItemCount();
                continue;
            }

            final String dstAddress = Utils.getCellData(csvRecord, 11);
            final String sender = Utils.getCellData(csvRecord, 17);
            rets.add(new ExcelData(trackNum, dstAddress, sender));
        }

        return rets;
    }

    public static void writeFile(String fileName, List<ExcelData> datas) throws Exception {
        logger.info("write output file " + fileName);
        Workbook workbook = new HSSFWorkbook();
        Sheet sheet = workbook.createSheet("sheet");

        Row header = sheet.createRow(0);
        Cell headerCell = header.createCell(0);
        headerCell.setCellValue("运单号");

        headerCell = header.createCell(1);
        headerCell.setCellValue("目的地");

        headerCell = header.createCell(2);
        headerCell.setCellValue("发件人");

        int rows = 0;
        for (ExcelData data: datas) {
            rows++;
            Row row = sheet.createRow(rows);
            Cell cell0 = row.createCell(0);
            cell0.setCellValue(data.trackNumber);
            Cell cell1 = row.createCell(1);
            cell1.setCellValue(data.province);
            Cell cell2 = row.createCell(2);
            cell2.setCellValue(data.sender);
        }
        FileOutputStream outputStream = new FileOutputStream(fileName);
        workbook.write(outputStream);
        workbook.close();
    }


    public static void writeUnkownToFile(String fileName, List<ExpressTrackCustomerData> datas) throws Exception {
        Workbook workbook = new HSSFWorkbook();
        Sheet sheet = workbook.createSheet("sheet");

        Row header = sheet.createRow(0);
        Cell headerCell = header.createCell(0);
        headerCell.setCellValue("运单号");

        headerCell = header.createCell(1);
        headerCell.setCellValue("客户名称");

        headerCell = header.createCell(2);
        headerCell.setCellValue("收/派件员");

        int rows = 0;
        for (ExpressTrackCustomerData data: datas) {
            rows++;
            Row row = sheet.createRow(rows);
            Cell cell0 = row.createCell(0);
            cell0.setCellValue(data.trackNumber);
            Cell cell1 = row.createCell(1);
            cell1.setCellValue(data.customer);
            Cell cell2 = row.createCell(2);
            cell2.setCellValue(data.expressman);
        }
        FileOutputStream outputStream = new FileOutputStream(fileName);
        workbook.write(outputStream);
        workbook.close();
    }
}

