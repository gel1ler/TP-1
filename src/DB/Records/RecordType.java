package DB.Records;


//Формат файлов
//player value
public enum RecordType {
    KILLS("kills.txt", "descending", "Убийца"),
    STEPS("steps.txt", "ascending", "Лентяй"),
    TIME("time.txt", "ascending", "Скороход");

    private final String filename, order, tableName;

    RecordType(String filename, String order, String tableName) {
        this.filename = filename;
        this.order = order;
        this.tableName = tableName;
    }

    public String getFilename() {
        return filename;
    }
    public String getOrder() {
        return order;
    }

    public String getTableName() {
        return tableName;
    }
}
