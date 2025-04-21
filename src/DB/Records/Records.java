package DB.Records;

import DB.DbPaths;
import game.Utils.Menu.Menu;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Records {
    private static String thisName;
    private static Long thisValue;

    private static boolean shouldInsert(RecordType record, String line) {
        String[] lineArr = line.split(" ");
        long value = Long.parseLong(lineArr[lineArr.length - 1]);

        return switch (record.getOrder()) {
            case "ascending" -> thisValue < value;
            case "descending" -> thisValue > value;
            default -> false;
        };
    }

    private static void pasteInFile(File file, RecordType record, List<String> resultLines) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            Menu.println("Ваш рекорд добавлен в таблицу " + record.getTableName() + "!");
            for (int i = 0; i < Math.min(resultLines.size(), 10); i++) {
                String resLine = resultLines.get(i);
                Menu.println(i + 1 + ". " + resLine);
                bw.write(resLine);
                bw.newLine();
            }
        }
    }

    private static void insertRecord(RecordType record, long myValue) throws IOException {
        thisValue = myValue;
        File file = new File(DbPaths.RECORDS.getPath() + record.getFilename());
        List<String> resultLines = new ArrayList<>();

        String line;
        boolean inserted = false;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            int i = 0;
            while ((line = br.readLine()) != null && i < 11) {
                if (!inserted && shouldInsert(record, line)) {
                    resultLines.add(thisName + " " + thisValue);
                    inserted = true;
                }
                i++;
                resultLines.add(line);
            }
        }

        if (!inserted && resultLines.size() < 10) {
            resultLines.add(thisName + " " + thisValue);
            inserted = true;
        }

        if (inserted) pasteInFile(file, record, resultLines);
    }

    public static void insertRecords(String name, Map<String, Long> stats) throws IOException {
        thisName = name;
        insertRecord(RecordType.KILLS, stats.get("kills"));
        insertRecord(RecordType.STEPS, stats.get("steps"));
        insertRecord(RecordType.TIME, stats.get("time"));
    }
}
