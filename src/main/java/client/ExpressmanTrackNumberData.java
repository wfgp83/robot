package client;

import java.util.List;
import java.util.StringJoiner;

public class ExpressmanTrackNumberData {
    private int currentIdx;
    public int trackNumSize;
    private static final int PAGE_SIZE = 1000;

    public String expressman;
    public List<String> trackNumbers;

    public ExpressmanTrackNumberData(String expressman, List<String> trackNumbers) {
        this.expressman = expressman;
        this.trackNumbers = trackNumbers;
        currentIdx = 0;
        trackNumSize = trackNumbers.size();
    }

    public List<String> getTrackTNumbers() {
        return trackNumbers;
    }

    public String getElement(){
        if (trackNumSize <= PAGE_SIZE) {
            currentIdx = trackNumSize;
            return Utils.joinElement(trackNumbers);
        }
        int endIdx = currentIdx + PAGE_SIZE;
        if (endIdx > trackNumSize)
            endIdx = trackNumSize;
        List<String> subList = trackNumbers.subList(currentIdx, endIdx);
        System.out.println("track number size: " + (endIdx-currentIdx) + " total size: "+ trackNumSize);

        currentIdx = endIdx;
        return Utils.joinElement(subList);
    }

    public boolean hasElement(){
        return trackNumSize > currentIdx;
    }
}
