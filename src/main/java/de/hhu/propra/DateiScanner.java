package de.hhu.propra;

        import java.io.*;
        import java.net.URISyntaxException;

/**
 * Created by Viktor on 04.07.2016.
 */
public class DateiScanner {
  public static String Scannen(String DieDatei) throws URISyntaxException {
    String RueckgabeString="";
    String path = DateiScanner.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
    path=path.substring(1,path.lastIndexOf("/"));
    path=path+"/";
    try {
      File TempDatei = new File(path+DieDatei+".txt");
      FileReader leseRatte= new FileReader(TempDatei);
      BufferedReader buffLeser= new BufferedReader(leseRatte);
      StringBuffer strBuff= new StringBuffer();
      String line;
      while ((line = buffLeser.readLine()) != null) {
        strBuff.append(line);
        strBuff.append("\n");
      }
      leseRatte.close();
      RueckgabeString=(strBuff.toString());
    } catch (IOException e) {
      e.printStackTrace();
    }
    return RueckgabeString;
  }
}

// Code für den Scanner aus
// http://www.avajava.com/tutorials/lessons/how-do-i-read-a-string-from-a-file-line-by-line.html
// entnommen