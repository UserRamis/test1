package net.GaripovRamis.Test.storage;

import net.GaripovRamis.Test.exception.AppException;
import net.GaripovRamis.Test.model.KeyPosition;
import net.GaripovRamis.Test.model.Position;
import net.GaripovRamis.Test.sql.Sql;
import org.apache.log4j.Logger;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;

import static org.apache.log4j.Logger.getLogger;

public class SqlStorageImpl implements Storage {

    private static final Logger LOG = getLogger(SqlStorageImpl.class);

    private Sql sql;

    public SqlStorageImpl(String dbUrl, String dbUser, String dbPassword) {
        sql = new Sql(() -> DriverManager.getConnection(dbUrl, dbUser, dbPassword));
    }

    @Override
    public List<Position> getAll() {
        return sql.execute("SELECT * FROM positions",
                st -> {
                    ResultSet rs = st.executeQuery();
                    List<Position> positions = new ArrayList<>();
                    while (rs.next()) {
                        positions.add(new Position(rs.getInt("id"), rs.getString("depcode"),
                                rs.getString("depjob"), rs.getString("description")));
                    }
                    return positions;
                });
    }

    @Override
    public boolean syncDbXml(Map<KeyPosition, Position> mapPositions) {

        List<Position> list = getAll();
        sql.execute(conn -> {
            for (Position item : list) {
                KeyPosition key = new KeyPosition(item.getDepCode(), item.getDepJob());
                if (mapPositions.containsKey(key)) {
                    try (PreparedStatement st = conn.prepareStatement("UPDATE positions SET description=? WHERE id=?")) {
                        st.setString(1, mapPositions.get(key).getDescription());
                        st.setInt(2, item.getId());
                        if (st.executeUpdate() == 0) {
                            throw new AppException("Позиция для обновления не найдена <" + item.getDepCode() + "> + <" + item.getDepJob() + ">");
                        }
                        LOG.info("Обновление записи с ключом - DepCode: <" + item.getDepCode() + "> <" + item.getDepJob() + ">");
                        mapPositions.remove(key);
                    }
                } else {
                    mapPositions.remove(key);
                    try (PreparedStatement st = conn.prepareStatement("DELETE FROM positions WHERE id=?")) {
                        st.setInt(1, item.getId());
                        if (st.executeUpdate() == 0) {
                            throw new AppException("Позиция для удаления не найдена <" + item.getDepCode() + "> + <" + item.getDepJob() + ">");
                        }
                        LOG.info("Удаление записи с ключом - DepCode: <" + item.getDepCode() + "> <" + item.getDepJob() + ">");
                    }
                }
            }
            try (PreparedStatement st = conn.prepareStatement("INSERT INTO positions (depcode, depjob, description) VALUES (?, ?, ?)")) {
                for (Position item : mapPositions.values()) {
                    st.setString(1, item.getDepCode());
                    st.setString(2, item.getDepJob());
                    st.setString(3, item.getDescription());
                    LOG.info("Попытка добавления новой записи с ключом - DepCode: <" + item.getDepCode() + "> <" + item.getDepJob() + ">");
                    st.addBatch();
                }
                st.executeBatch();
            }
            return true;
        });
        return false;
    }
}
