package client;

import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class ExcelHandler {
    public static List<ExcelData>  parseFile(final String dir, final String cutomerName, int idx) throws Exception {
        List<ExcelData> rets = new ArrayList<>();
        for(int prefix = 1; prefix <= idx; prefix++) {
           final String fileName =  Utils.getExportDataFileName(dir,
                   Utils.getFileNameWithPrefixIndex(cutomerName, prefix));
           System.out.println(fileName);
            FileInputStream file = new FileInputStream(new File(fileName));
            Workbook workbook = new HSSFWorkbook(file);
            Sheet sheet = workbook.getSheetAt(0);
            rets.addAll(getRequiredDatas(sheet));
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

    public static List<ExcelData> getRequiredDatas(Sheet sheet){
        List<ExcelData> rets = new ArrayList<>();
        for(Row row : sheet){
            Cell c3 = row.getCell(3);
            Cell c11 = row.getCell(11);
            Cell c17 = row.getCell(17);
            if (c3 == null){
                System.err.println("no cell 3");
                continue;
            }
            final String cell3Content = Utils.getCellContent(c3);
            final String cell11Content = Utils.getCellContent(c11);
            final String cell17Content = Utils.getCellContent(c17);

            if (Utils.isTrackNumber(cell3Content))
                rets.add(new ExcelData(cell3Content, cell11Content, cell17Content));
            else
                System.err.println("not track number " + cell3Content);
        }
        System.out.println(rets.size());
        return rets;
    }

    public static void main(String[] args) throws Exception{

        //List<ExcelData> datas = parseFile("d:\\wfgp_util", "杭州女装网", 1);
        TrackNumberHandler handler = new TrackNumberHandler();
        final String fileName = "D:\\wfgp_util\\data\\5678.CSV";
        String outputFilePrefix = handler.parseForOutputFileName(fileName);
        String outputFileName = Utils.getResultFileName("D:\\wfgp_util", outputFilePrefix);
        System.out.println(outputFileName);
        //writeFile(outputFileName, datas);
    }

    public static void writeFile(String fileName, List<ExcelData> datas) throws Exception {
        Workbook workbook = new HSSFWorkbook();
        Sheet sheet = workbook.createSheet("sheet");

        //sheet.setColumnWidth(0, 6000);
        //sheet.setColumnWidth(1, 4000);

        Row header = sheet.createRow(0);

        /*CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);*/

        /*HSSFFont font = ((HSSFWorkbook) workbook).createFont();
        font.setFontName("Tahoma");
        font.setFontHeightInPoints((short) 16);
        font.setBold(true);
        headerStyle.setFont(font);*/

        Cell headerCell = header.createCell(0);
        headerCell.setCellValue("运单号");
        //headerCell.setCellStyle(headerStyle);

        headerCell = header.createCell(1);
        headerCell.setCellValue("目的地");
        //headerCell.setCellStyle(headerStyle);

        headerCell = header.createCell(2);
        headerCell.setCellValue("发件人");
        //headerCell.setCellStyle(headerStyle);

        //CellStyle style = workbook.createCellStyle();
        //style.setWrapText(true);

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
        //Row row = sheet.createRow(2);
        //Cell cell = row.createCell(0);
        //cell.setCellValue("John Smith");
        //cell.setCellStyle(style);
        FileOutputStream outputStream = new FileOutputStream(fileName);
        workbook.write(outputStream);
        workbook.close();
    }
}
