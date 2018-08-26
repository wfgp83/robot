package client;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class CsvOutputHandler {

    public static List<ExcelData>  parseFile(final String dir, final String cutomerName,
                                             int idx) throws Exception {
        List<ExcelData> rets = new ArrayList<>();
        for(int prefix = 1; prefix <= idx; prefix++) {
            final String fileName =  Utils.getExportDataFileName(dir,
                    Utils.getFileNameWithPrefixIndex(cutomerName, prefix));
            File file = new File(fileName);
            CSVParser parser = CSVParser.parse(file, Charset.forName("GB2312"), CSVFormat.EXCEL);
            rets.addAll(getRequiredDatas(parser));
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

    public static List<ExcelData> getRequiredDatas(CSVParser parser){
        List<ExcelData> rets = new ArrayList<>();
        for (CSVRecord csvRecord : parser) {
            // Accessing Values by Column Index
            final String trackNum = Utils.getCellData(csvRecord, 0);
            if (!Utils.isTrackNumber(trackNum))
            {
                System.out.println("not a track number :=" + trackNum);
                continue;
            }

            final String dstAddress = Utils.getCellData(csvRecord, 11);
            final String sender = Utils.getCellData(csvRecord, 17);
            if (Utils.isTrackNumber(trackNum))
                rets.add(new ExcelData(trackNum, dstAddress, sender));
            else
                System.err.println("not track number " + trackNum);
        }
        System.out.println(rets.size());
        return rets;
    }

    public static void main(String[] args) throws Exception {
        final String dir = "D:\\wfgp_util";
        final String fileName = dir + "\\data\\891.CSV";
        List<ExcelData> datas = parseFile(dir, "891", 1);
        writeFile(Utils.getResultFileName(dir,"8888"), datas);
    }

    public static void writeFile(String fileName, List<ExcelData> datas) throws Exception {
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

