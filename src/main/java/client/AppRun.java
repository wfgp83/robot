package client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.List;

public class AppRun {

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
        if (customer.isEmpty())
            return expressman + "-" + dateInfo;
        return customer + "-" + expressman + "-" + dateInfo;
    }

    public static void main(String... args) {
        long currTime = System.currentTimeMillis();
        String appConfDir;
        if (args.length <1) {
            appConfDir = AppConfReader.getConfDir();
        } else {
            appConfDir = args[0]+ "\\wfgp_util";
        }

        if (!Utils.isFileExist(appConfDir)){
            logger.error("App configuration not exist :", appConfDir);
            return;
        }

        System.out.println("press any keyboard key to start ...");
        try {
            System.in.read();
            Thread.sleep(TWO_SECONDS);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return;
        }

        AppRun exportData = new AppRun();
        try {
            exportData.initApp(appConfDir);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return;
        }

        List<String> customers = null;
        try {
            customers = CustomerConfigReader.getDatas(appConfDir);
        } catch (IOException e) {
            logger.error(e.getMessage());
            return;
        }
        int totalItem = 0;
        for (String customer: customers) {
            CsvHandler tnh = new CsvHandler();
            logger.info("Do customer ", customer);
            logger.info("Receive tab ", customer);
            exportData.receiveTab();
            logger.info("Fill customer ", customer);
            exportData.fillCustomer(customer);

            logger.info("Query receive tab ", customer);
            exportData.queryForReceiveTab();

            String exportDatafileName = Utils.getExportDataFileName(appConfDir, customer);
            logger.info("Export data receive tab ", customer);
            exportData.exportDataForReceiveTab(Utils.isFileExist(exportDatafileName));
            logger.info("Parse data receive tab ", customer);


            tnh.parseFile(exportDatafileName);
            while (tnh.hasNext()) {
                ExpressmanTrackNumberData numDatas = tnh.next();
                totalItem += numDatas.trackNumSize;
                int fileNameSuffix = 0;
                while (numDatas.hasElement()) {
                    fileNameSuffix++;
                    String dataStr = numDatas.getElement();
                    logger.info("Send tab ", customer);
                    exportData.sentTab();
                    logger.info("Delete and copy track numbers send tab ", customer);
                    exportData.delAndCpForTrackNumbers(dataStr);
                    logger.info("Query send tab " + customer);
                    exportData.queryForSendTab();

                    logger.info("Export data send tab ", customer);
                    final String sendFileName = Utils.getFileNameWithPrefixIndex(numDatas.expressman,
                            fileNameSuffix);
                    String exportSendDatafileName = Utils.getExportDataFileName(appConfDir,
                            sendFileName);
                    exportData.nameToClipboard(sendFileName);
                    exportData.exportDataForSendTab(Utils.isFileExist(exportSendDatafileName));
                    try {
                        Thread.sleep(TWO_SECONDS);
                    } catch (InterruptedException e) {
                        logger.error(e.getMessage());
                    }
                }

                try {
                    List<ExcelData> excelDatas = CsvOutputHandler.parseFile(appConfDir,
                            numDatas.expressman, fileNameSuffix);
                    final String outputFilePrefix = exportData.getOutputFileNamePrefix(customer,
                            numDatas.expressman, tnh.getDateStr());
                    final String outputFileName = Utils.getResultFileName(appConfDir, outputFilePrefix);
                    Utils.deleteFile(outputFileName);
                    CsvOutputHandler.writeFile(outputFileName, excelDatas);
                } catch (Exception e) {
                    logger.error(e.getMessage());
                }
            }
        }
        final long spendTime = (System.currentTimeMillis() - currTime) /1000;
        final String msg = "Successful Total " + totalItem +",time " +
                spendTime + " second";
        JOptionPane.showMessageDialog(null, msg);
    }

    public void findByExpressNo(){
        MyPoint point = propertiesReader.getSendTabCheckboxTrackNumberPoint();
        robot.mouseMove(point.x, point.y);
        clickLeftMouse();
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

    public void exportDataForReceiveTab(boolean exportDataFileExist) {
        MyPoint export = propertiesReader.getReceiveTabExportPoint();
        MyPoint excel = propertiesReader.getReceiveTabExportExccelStylePoint();
        MyPoint save = propertiesReader.getReceiveTabExportExccelButtonSavePoint();
        MyPoint complete = propertiesReader.getReceiveTabExportExccelButtonCompletePoint();
        MyPoint override = propertiesReader.getReceiveTabExportExccelButtonOverridePoint();
        exportData(export, excel, save, complete, override, exportDataFileExist);
    }

    public void nameToClipboard(String customerName) {
        StringSelection dataStr = new StringSelection(customerName);
        systemClipboard.setContents(dataStr, dataStr);
    }

    public void fillCustomer(String customerName){
        nameToClipboard(customerName);
        delAndCpForCusomter();
    }

    public void query(MyPoint point){
        robot.mouseMove(point.x, point.y);
        clickLeftMouse();
    }

    public void queryForSendTab(){
       MyPoint point = propertiesReader.getSendTabQueryPoint();
       query(point);
    }

    public void queryForReceiveTab(){
        MyPoint point = propertiesReader.getReceiveTabQueryPoint();
        query(point);
    }

    public void delAndCp(MyPoint point){
        robot.delay(40);
        robot.mouseMove(point.x,point.y);
        clickLeftMouse();
        ctrlA();
        del();

        robot.mouseMove(point.x,point.y);
        clickLeftMouse();
        ctrlV();
    }

    public void delAndCpForCusomter(){
        MyPoint point = propertiesReader.getReceiveTabCustomerNamePoint();
        delAndCp(point);
    }

    public void delAndCpForTrackNumbers(String datas){
        StringSelection dataStr = new StringSelection(datas);
        systemClipboard.setContents(dataStr, dataStr);
        MyPoint point = propertiesReader.getSendTabTrackNumbersPoint();
        delAndCp(point);
    }

    public void receiveTab() {
        robot.delay(40);

        MyPoint point = propertiesReader.getReceiveTabPoint();
        robot.mouseMove(point.x, point.y);
        clickLeftMouse();
    }

    public void sentTab() {
        robot.delay(40);

        MyPoint point = propertiesReader.getSendTabPoint();
        robot.mouseMove(point.x, point.y);
        clickLeftMouse();
    }

    public void clickLeftMouse()
    {
        robot.delay(40);
        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        robot.delay(200);
        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
        robot.delay(200);
    }

    public void ctrlA(){
        robot.delay(40);
        robot.keyPress(KeyEvent.VK_CONTROL);
        robot.delay(40);
        robot.keyPress(KeyEvent.VK_A);
        robot.delay(40);
        robot.keyRelease(KeyEvent.VK_A);
        robot.delay(40);
        robot.keyRelease(KeyEvent.VK_CONTROL);
        robot.delay(100);
    }

    public void ctrlV(){
        robot.delay(40);
        robot.keyPress(KeyEvent.VK_CONTROL);
        robot.delay(40);
        robot.keyPress(KeyEvent.VK_V);
        robot.delay(40);
        robot.keyRelease(KeyEvent.VK_V);
        robot.delay(40);
        robot.keyRelease(KeyEvent.VK_CONTROL);
        robot.delay(100);
    }

    public void del(){
        robot.delay(40);
        robot.keyPress(KeyEvent.VK_DELETE);
        robot.delay(100);
    }
}
