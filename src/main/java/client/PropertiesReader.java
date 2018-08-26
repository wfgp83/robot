package client;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesReader {

    private Properties prop = new Properties();

    public PropertiesReader(final String dir) throws IOException {
        InputStream input = new FileInputStream(Utils.getMouseLocationFileName(dir));
        prop.load(input);
    }

    public MyPoint getReceiveTabPoint(){
        return  new MyPoint(Integer.valueOf(prop.getProperty(Receive_Tab_X_KEY)),
                Integer.valueOf(prop.getProperty(Receive_Tab_Y_KEY)));
    }

    public MyPoint getReceiveTabCustomerNamePoint(){
        return  new MyPoint(Integer.valueOf(prop.getProperty(Receive_Tab_Customer_Name_X_KEY)),
                Integer.valueOf(prop.getProperty(Receive_Tab_Customer_Name_Y_KEY)));
    }

    public MyPoint getReceiveTabQueryPoint(){
        return  new MyPoint(Integer.valueOf(prop.getProperty(Receive_Tab_Query_X_KEY)),
                Integer.valueOf(prop.getProperty(Receive_Tab_Query_Y_KEY)));
    }

    public MyPoint getReceiveTabExportPoint(){
        return  new MyPoint(Integer.valueOf(prop.getProperty(Receive_Tab_Export_X_KEY)),
                Integer.valueOf(prop.getProperty(Receive_Tab_Export_Y_KEY)));
    }

    public MyPoint getReceiveTabExportExccelStylePoint(){
        return  new MyPoint(Integer.valueOf(prop.getProperty(Receive_Tab_Export_Exccel_Style_X_KEY)),
                Integer.valueOf(prop.getProperty(Receive_Tab_Export_Exccel_Style_Y_KEY)));
    }

    public MyPoint getReceiveTabExportExccelButtonSavePoint(){
        return  new MyPoint(Integer.valueOf(prop.getProperty(Receive_Tab_Export_Excel_Button_Save_X_KEY)),
                Integer.valueOf(prop.getProperty(Receive_Tab_Export_Excel_Button_Save_Y_KEY)));
    }

    public MyPoint getReceiveTabExportExccelButtonCompletePoint(){
        return  new MyPoint(Integer.valueOf(prop.getProperty(Receive_Tab_Export_Excel_Button_Complete_X_KEY)),
                Integer.valueOf(prop.getProperty(Receive_Tab_Export_Excel_Button_Complete_Y_KEY)));
    }

    public MyPoint getReceiveTabExportExccelButtonOverridePoint(){
        return  new MyPoint(Integer.valueOf(prop.getProperty(Receive_Tab_Export_Excel_Button_Override_X_KEY)),
                Integer.valueOf(prop.getProperty(Receive_Tab_Export_Excel_Button_Override_Y_KEY)));
    }

    public MyPoint getSendTabPoint(){
        return  new MyPoint(Integer.valueOf(prop.getProperty(Send_Tab_X_KEY)),
                Integer.valueOf(prop.getProperty(Send_Tab_Y_KEY)));
    }

    public MyPoint getSendTabCheckboxTrackNumberPoint(){
        return  new MyPoint(Integer.valueOf(prop.getProperty(Send_Tab_Checkbox_Track_Number_X_KEY)),
                Integer.valueOf(prop.getProperty(Send_Tab_Checkbox_Track_Number_Y_KEY)));
    }

    public MyPoint getSendTabTrackNumbersPoint(){
        return  new MyPoint(Integer.valueOf(prop.getProperty(Send_Tab_Track_Numbers_X_KEY)),
                Integer.valueOf(prop.getProperty(Send_Tab_Track_Numbers__Y_KEY)));
    }

    public MyPoint getSendTabTrackNumbersClearPoint(){
        return  new MyPoint(Integer.valueOf(prop.getProperty(Send_Tab_Track_Numbers_Clear_X_KEY)),
                Integer.valueOf(prop.getProperty(Send_Tab_Track_Numbers__Clear_Y_KEY)));
    }

    public MyPoint getSendTabQueryPoint(){
        return  new MyPoint(Integer.valueOf(prop.getProperty(Send_Tab_Query_X_KEY)),
                Integer.valueOf(prop.getProperty(Send_Tab_Query_Y_KEY)));
    }

    public MyPoint getSendTabExportPoint(){
        return  new MyPoint(Integer.valueOf(prop.getProperty(Send_Tab_Export_X_KEY)),
                Integer.valueOf(prop.getProperty(Send_Tab_Export_Y_KEY)));
    }

    public MyPoint getSendTabExportExccelStylePoint(){
        return  new MyPoint(Integer.valueOf(prop.getProperty(Send_Tab_Export_Exccel_Style_X_KEY)),
                Integer.valueOf(prop.getProperty(Send_Tab_Export_Exccel_Style_Y_KEY)));
    }

    public MyPoint getSendTabExportExccelButtonSavePoint(){
        return  new MyPoint(Integer.valueOf(prop.getProperty(Send_Tab_Export_Excel_Button_Save_X_KEY)),
                Integer.valueOf(prop.getProperty(Send_Tab_Export_Excel_Button_Save_Y_KEY)));
    }

    public MyPoint getSendTabExportExccelButtonCompletePoint(){
        return  new MyPoint(Integer.valueOf(prop.getProperty(Send_Tab_Export_Excel_Button_Complete_X_KEY)),
                Integer.valueOf(prop.getProperty(Send_Tab_Export_Excel_Button_Complete_Y_KEY)));
    }

    public MyPoint getSendTabExportExccelButtonOverridePoint(){
        return  new MyPoint(Integer.valueOf(prop.getProperty(Send_Tab_Export_Excel_Button_Override_X_KEY)),
                Integer.valueOf(prop.getProperty(Send_Tab_Export_Excel_Button_Override_Y_KEY)));
    }

    public static String Receive_Tab_X_KEY = "receive.tab.x";
    public static String Receive_Tab_Y_KEY = "receive.tab.y";
    public static String Receive_Tab_Customer_Name_X_KEY = "receive.tab.customer.name.x";
    public static String Receive_Tab_Customer_Name_Y_KEY = "receive.tab.customer.name.y";
    public static String Receive_Tab_Query_X_KEY = "receive.tab.query.x";
    public static String Receive_Tab_Query_Y_KEY = "receive.tab.query.y";
    public static String Receive_Tab_Export_X_KEY = "receive.tab.export.x";
    public static String Receive_Tab_Export_Y_KEY = "receive.tab.export.y";
    public static String Receive_Tab_Export_Exccel_Style_X_KEY = "receive.tab.export.excel.style.x";
    public static String Receive_Tab_Export_Exccel_Style_Y_KEY = "receive.tab.export.excel.style.y";
    public static String Receive_Tab_Export_Excel_Button_Save_X_KEY = "receive.tab.export.excel.button.save.x";
    public static String Receive_Tab_Export_Excel_Button_Save_Y_KEY = "receive.tab.export.excel.button.save.y";
    public static String Receive_Tab_Export_Excel_Button_Complete_X_KEY = "receive.tab.export.excel.button.complete.x";
    public static String Receive_Tab_Export_Excel_Button_Complete_Y_KEY = "receive.tab.export.excel.button.complete.y";
    public static String Receive_Tab_Export_Excel_Button_Override_X_KEY = "receive.tab.export.excel.button.override.x";
    public static String Receive_Tab_Export_Excel_Button_Override_Y_KEY = "receive.tab.export.excel.button.override.y";

    public static String Send_Tab_X_KEY = "send.tab.x";
    public static String Send_Tab_Y_KEY = "send.tab.y";
    public static String Send_Tab_Checkbox_Track_Number_X_KEY = "send.tab.checkbox.track.number.x";
    public static String Send_Tab_Checkbox_Track_Number_Y_KEY = "send.tab.checkbox.track.number.y";
    public static String Send_Tab_Track_Numbers_Clear_X_KEY = "send.tab.track.numbers.clear.x";
    public static String Send_Tab_Track_Numbers__Clear_Y_KEY = "send.tab.track.numbers.clear.y";
    public static String Send_Tab_Track_Numbers_X_KEY = "send.tab.track.numbers.x";
    public static String Send_Tab_Track_Numbers__Y_KEY = "send.tab.track.numbers.y";
    public static String Send_Tab_Query_X_KEY = "send.tab.query.x";
    public static String Send_Tab_Query_Y_KEY = "send.tab.query.y";
    public static String Send_Tab_Export_X_KEY = "send.tab.export.x";
    public static String Send_Tab_Export_Y_KEY = "send.tab.export.y";
    public static String Send_Tab_Export_Exccel_Style_X_KEY = "send.tab.export.excel.style.x";
    public static String Send_Tab_Export_Exccel_Style_Y_KEY = "send.tab.export.excel.style.y";
    public static String Send_Tab_Export_Excel_Button_Save_X_KEY = "send.tab.export.excel.button.save.x";
    public static String Send_Tab_Export_Excel_Button_Save_Y_KEY = "send.tab.export.excel.button.save.y";
    public static String Send_Tab_Export_Excel_Button_Complete_X_KEY = "send.tab.export.excel.button.complete.x";
    public static String Send_Tab_Export_Excel_Button_Complete_Y_KEY = "send.tab.export.excel.button.complete.y";
    public static String Send_Tab_Export_Excel_Button_Override_X_KEY = "send.tab.export.excel.button.override.x";
    public static String Send_Tab_Export_Excel_Button_Override_Y_KEY = "send.tab.export.excel.button.override.y";
}
