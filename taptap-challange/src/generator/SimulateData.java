package generator;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

public class SimulateData implements Function<Integer, Object> {
    @Override
    public Object apply(Integer tripNumber) {
        String[] customerIds = new String[]{"CID000122", "CID000224", "CID000125", "CID000122", "CID000224", "CID000125"};
        String[] busIds = new String[]{"BUS901", "BUS902", "BUS905", "BUS901", "BUS902", "BUS905"};
        String[] terminalIds = new String[]{"T0001A", "T0002A", "T0003A", "T0001B", "T0002B", "T0003B"};

        Random random = new Random();

        List<TripRow> tripRows = new ArrayList<>();

        for (int i = 0; i < tripNumber; i++) {
            // Tạo giao dịch "IN"
            TripRow in = new TripRow();
            in.setDirection("IN");
            in.setCardId(customerIds[random.nextInt(4)]);
            in.setTimestamp(System.currentTimeMillis() + random.nextInt(24) + 60 * 60 * 1000);
            in.setBusId(busIds[random.nextInt(4)]);
            in.setTerminalId(terminalIds[random.nextInt(4)]);
            tripRows.add(in);

            TripRow out = new TripRow();
            // Tạo giao dịch "OUT"
            out.setDirection("OUT");
            out.setCardId(in.getCardId());
            out.setTimestamp(in.getTimestamp() + random.nextInt(60 * 60 * 1000));
            out.setBusId(in.getBusId());
            out.setTerminalId(terminalIds[random.nextInt(4)]);
            tripRows.add(out);
        }
        try {
            FileUtils.writeLines(new File("data/import/import.txt"), tripRows);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    class TripRow {
        String direction;
        String cardId;
        long timestamp;
        String busId;
        String terminalId;

        public String getDirection() {
            return direction;
        }

        public void setDirection(String direction) {
            this.direction = direction;
        }

        public String getCardId() {
            return cardId;
        }

        public void setCardId(String cardId) {
            this.cardId = cardId;
        }

        public long getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(long timestamp) {
            this.timestamp = timestamp;
        }

        public String getBusId() {
            return busId;
        }

        public void setBusId(String busId) {
            this.busId = busId;
        }

        public String getTerminalId() {
            return terminalId;
        }

        public void setTerminalId(String terminalId) {
            this.terminalId = terminalId;
        }

        @Override
        public String toString() {
            return direction + "," + cardId + "," + timestamp + "," + busId + "," + terminalId;
        }
    }
}