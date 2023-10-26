package net.GaripovRamis.Test.storage;

import net.GaripovRamis.Test.model.KeyPosition;
import net.GaripovRamis.Test.model.Position;

import java.util.List;
import java.util.Map;

public interface Storage {

    List<Position> getAll();

    boolean syncDbXml(Map<KeyPosition, Position> mapPositions);
}
