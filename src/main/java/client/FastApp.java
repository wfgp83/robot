package client;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FastApp {

    private static Logger logger = LoggerFactory.getLogger(AppRun.class);

    public static final int TWO_SECONDS = 2000;
    private PropertiesReader propertiesReader;
    private Robot robot;
    private Clipboard systemClipboard;

    public void initApp(String appConfDir) throws Exception {
        propertiesReader = new PropertiesReader(appConfDir);
        systemClipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        robot = new Robot();
        robot.setAutoDelay(1000);
        robot.setAutoWaitForIdle(true);
    }

    public String getOutputFileNamePrefix(String customer, String expressman, String dateInfo){
        return expressman + "-" + dateInfo;
    }

    public static void main(String... args) {
        long currTime = System.currentTimeMillis();
        final String appConfDir = "d:\\wfgp_util";
        final List<String> dirs = new ArrayList<>();
        //dirs.add(appConfDir);
        dirs.add(appConfDir +"\\rawData");
        //dirs.add(appConfDir +"\\exportData");
        dirs.add(appConfDir +"\\log");
        dirs.add(appConfDir +"\\output");
        dirs.add(appConfDir +"\\unknown");

        for (String dir : dirs) {
            try {
                FileUtils.deleteDirectory(new File(dir));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        for (String dir : dirs) {
            Utils.createDir(dir);
        }

        System.out.println("Confirm export data to " + appConfDir +
                "\\rawData directory,\nThen press any keyboard key to start ...");
        try {
            System.in.read();
            Thread.sleep(TWO_SECONDS);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return;
        }

        FastApp exportData = new FastApp();
        try {
            exportData.initApp(appConfDir);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return;
        }

        final String rawDir = appConfDir +"\\rawData";
        File folder = new File(rawDir);
        File[] listOfFiles = folder.listFiles();
        List<String> files = new ArrayList<>();
        for (File f: listOfFiles) {
            if (!f.isFile()) {
                continue;
            }
            files.add(rawDir + "\\"+ f.getName());
        }

        if(files.isEmpty()){
            logger.info("no files under " + rawDir);
            return;
        }

        CsvHandler tnh = new CsvHandler();
        try {
            tnh.parseFile(files);
            tnh.merge();
        } catch (IOException e) {
            logger.error(e.getMessage());
            return;
        }

        final String unknownDir = appConfDir +"\\unknown";
        try {
            CsvOutputHandler.writeUnkownToFile(unknownDir + "\\unknon" + tnh.getDateStr() + ".XLS",
                    tnh.getNoExpressmanTrackNumbers());
        } catch (Exception e) {
            e.printStackTrace();
        }

        final  int unknownTtem = tnh.getNoExpressmanTrackNumbers().size();
        tnh.resetNoExpressmanTrackNumbers();

        exportData.sentTab();

        int fileNameSuffix = 0;
        final String tabFileNamePrefix = "sendTab";
        while (tnh.getTrackNumberHandler().hasElement()) {
            fileNameSuffix ++;
            final String  trackNums = tnh.getTrackNumberHandler().getElement();
            exportData.delAndCpForTrackNumbers(trackNums);
            exportData.queryForSendTab();

            final String sendFileName = Utils.getFileNameWithPrefixIndex(tabFileNamePrefix,
                    fileNameSuffix);
            String exportSendDatafileName = Utils.getExportDataFileName(appConfDir,
                    sendFileName);
            exportData.nameToClipboard(sendFileName);
            exportData.exportDataForSendTab(Utils.isFileExist(exportSendDatafileName));
        }

        List<ExcelData> excelDatas = new ArrayList<>();
        try {
            excelDatas = CsvOutputHandler.parseFile(appConfDir,
                    tabFileNamePrefix, fileNameSuffix);
            logger.info("export data size := " + excelDatas.size());
        } catch (Exception e) {
            logger.error(e.getMessage());
        }

        if (excelDatas.isEmpty()){
            return;
        }

        int[] retValue = new int[2];
        while (tnh.hasNext()) {
            ExpressmanTrackNumberData numDatas = tnh.next();
            exportData.execute(numDatas, retValue, appConfDir, tnh, excelDatas);
        }

        final long spendTime = (System.currentTimeMillis() - currTime) /1000;
        int totalItem = retValue[0];
        final  int total = totalItem + unknownTtem;
        final String msg = "Successful Total " + total +", unknown "+ unknownTtem  + ", success "
                + totalItem + ",time " + spendTime + " second";
        logger.info(msg);
        JOptionPane.showMessageDialog(null, msg);
    }

    public void execute(ExpressmanTrackNumberData numDatas, int[] retValue,
                        String appConfDir, CsvHandler tnh, List<ExcelData> excelDatas){
        final int Create_NEW_DIR_Threshold = 60;
        final String customer = numDatas.expressman.replaceAll(
                "[/<>:\"|?*]", "_");
        retValue[0] += numDatas.trackNumSize;

        List<ExcelData> requiredData = new ArrayList<>();
        List<String> trackNums = numDatas.getTrackTNumbers();
        if (trackNums.isEmpty()) {
            logger.info("no data for " + customer);
            return;
        }
        long currTime = System.currentTimeMillis();
        trackNums.forEach((v)->{
            //logger.info("Begin try to find ==========" + v);
            excelDatas.forEach((k)->{
                //logger.info("export " + k.trackNumber);
                if (k.trackNumber.equals(v)) {
                    requiredData.add(k);
                }
                //else {
                  //  logger.warn("no match track number:" + v);
                //}
            });
            //logger.info("END=============");
        });
        logger.info("spend time " + (System.currentTimeMillis() - currTime));
        if (requiredData.isEmpty()){
            logger.info("empty match  for " + customer + " " + trackNums.get(0));
            return;
        }
        final String outputFilePrefix = getOutputFileNamePrefix("",
                customer, tnh.getDateStr());
        final String outputFileName = Utils.getResultFileName(appConfDir, outputFilePrefix);
        Utils.deleteFile(outputFileName);
        try {
            CsvOutputHandler.writeFile(outputFileName, requiredData);
        } catch (Exception e) {
           logger.error(e.getMessage());
        }
    }

    public void exportData(MyPoint export, MyPoint excel, MyPoint save, MyPoint complete,
                           MyPoint override, boolean fileExist) {

        // export button
        robot.mouseMove(export.x, export.y);
        clickLeftMouse();

        // excel button
        robot.mouseMove(excel.x,excel.y);
        clickLeftMouse();

        // file name
        ctrlV();

        // ok button
        robot.mouseMove(save.x,save.y);
        clickLeftMouse();

        if (fileExist) {
            // export success
            robot.mouseMove(override.x,override.y);
            clickLeftMouse();
        }

        robot.delay(100);

        // export success
        robot.mouseMove(complete.x,complete.y);
        clickLeftMouse();
    }

    public void exportDataForSendTab(boolean fileExist) {
        MyPoint export = propertiesReader.getSendTabExportPoint();
        MyPoint excel = propertiesReader.getSendTabExportExccelStylePoint();
        MyPoint save = propertiesReader.getSendTabExportExccelButtonSavePoint();
        MyPoint complete = propertiesReader.getSendTabExportExccelButtonCompletePoint();
        MyPoint override = propertiesReader.getSendTabExportExccelButtonOverridePoint();
        exportData(export, excel, save, complete, override, fileExist);
    }

    public void nameToClipboard(String customerName) {
        StringSelection dataStr = new StringSelection(customerName);
        systemClipboard.setContents(dataStr, dataStr);
    }

    public void query(MyPoint point){
        robot.mouseMove(point.x, point.y);
        clickLeftMouse();
        robot.delay(1000);
    }

    public void queryForSendTab(){
        MyPoint point = propertiesReader.getSendTabQueryPoint();
        query(point);
    }

    public void delAndCp(MyPoint clearP, MyPoint point){
        robot.delay(40);
        robot.mouseMove(clearP.x, clearP.y);
        clickLeftMouse();

        robot.mouseMove(point.x,point.y);
        clickLeftMouse();
        ctrlV();
    }

    public void delAndCpForTrackNumbers(String datas){
        StringSelection dataStr = new StringSelection(datas);
        systemClipboard.setContents(dataStr, dataStr);
        MyPoint point = propertiesReader.getSendTabTrackNumbersPoint();
        MyPoint clearP = propertiesReader.getSendTabTrackNumbersClearPoint();
        delAndCp(clearP, point);
    }

    public void sentTab() {
        robot.delay(40);

        MyPoint point = propertiesReader.getSendTabPoint();
        robot.mouseMove(point.x, point.y);
        clickLeftMouse();
    }

    public void selectCheckboxQueryByTrackNumber(){
        robot.delay(40);

        MyPoint point = propertiesReader.getSendTabCheckboxTrackNumberPoint();
        robot.mouseMove(point.x, point.y);
        clickLeftMouse();
    }

    public void clickLeftMouse()
    {
        robot.delay(40);
        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        robot.delay(40);
        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
        robot.delay(40);
    }

    public void ctrlV(){
        robot.delay(20);
        robot.keyPress(KeyEvent.VK_CONTROL);
        robot.delay(20);
        robot.keyPress(KeyEvent.VK_V);
        robot.delay(20);
        robot.keyRelease(KeyEvent.VK_V);
        robot.delay(20);
        robot.keyRelease(KeyEvent.VK_CONTROL);
        robot.delay(40);
    }
}

