package client;

public class ExcelData {
    public String trackNumber;
    public String province;
    public String sender;

    public ExcelData(String trackNumber, String province, String sender) {
        this.trackNumber = trackNumber;
        this.province = province;
        this.sender = sender;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ExcelData excelData = (ExcelData) o;

        return trackNumber.equals(excelData.trackNumber);
    }

    @Override
    public int hashCode() {
        return trackNumber.hashCode();
    }
}
