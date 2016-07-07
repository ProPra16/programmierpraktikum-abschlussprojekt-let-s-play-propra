package de.hhu.propra;

import java.io.File;
import java.io.FileWriter;
import java.net.URISyntaxException;

/**
 * Created by Viktor on 07.07.2016.
 */
public class TestWriter {
  public TestWriter(String Dateiname, String Dateiinhalt) throws URISyntaxException {
    String path = TestWriter.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
    path=path.substring(1,path.lastIndexOf("/"));
    path=path+"/";
    try {
      FileWriter writer = new FileWriter(path+Dateiname + ".txt");
      writer.write("test\n"+Dateiinhalt);
      writer.close();
    } catch (Exception e) {
      System.err.println("Schreiben der Testdatei fehlgeschlagen" + e);
    }
  }
}
