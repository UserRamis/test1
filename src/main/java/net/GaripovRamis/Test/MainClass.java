package net.GaripovRamis.Test;

import net.GaripovRamis.Test.exception.AppException;

import javax.xml.transform.TransformerException;
import java.io.IOException;

public class MainClass {
    public static void main(String[] args) throws TransformerException, IOException {
        if (args.length != 2) {
            System.out.println("Используйте два параметра в командной строке!");
            System.out.println("Первый:");
            System.out.println(" save - сохранить базу данных в XML");
            System.out.println(" sync - загрузить из XML в базу данных");
            System.out.println("Второй:");
            System.out.println(" 1.xml - имя XML-файла (может содержать путь к файлу)");
        } else {
            try {
                XmlDbSync xmlDbSync = new XmlDbSync();
                switch (args[0]) {
                    case "save":
                        xmlDbSync.createXml(args[1]);
                        break;
                    case "sync":
                        xmlDbSync.syncXml(args[1]);
                        break;
                    default:
                        throw new AppException("Неверный первый параметр!");
                }
            } catch (Exception e) {
                System.out.printf("Неверные параметры! " + e);
            }
        }
    }
}
