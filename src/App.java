import connection.ConnectionDB;

public class App {
    public static void main(String[] args) throws Exception {
        ConnectionDB conn = new ConnectionDB();
        conn.getConnection();
        conn.closeConnection();
    }
}
