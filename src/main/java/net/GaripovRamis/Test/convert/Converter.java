package net.GaripovRamis.Test.convert;

import net.GaripovRamis.Test.model.KeyPosition;
import net.GaripovRamis.Test.model.Position;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface Converter {

    Document createXML(List<Position> positions);

    Map<KeyPosition, Position> syncXML(File xmlFile) throws IOException, SAXException;
}
