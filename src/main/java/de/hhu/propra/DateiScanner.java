package de.hhu.propra;

        import java.io.*;

/**
 * Created by Viktor on 04.07.2016.
 */
public class DateiScanner {
  public DateiScanner(String DieDatei){
    try {
      File TempDatei = new File("DieDatei");
      FileReader leseRatte= new FileReader(TempDatei);
      BufferedReader buffLeser= new BufferedReader(leseRatte);
      StringBuffer strBuff= new StringBuffer();
      String line;
      while ((line = buffLeser.readLine()) != null) {
        strBuff.append(line);
        strBuff.append("\n");
      }
      leseRatte.close();
      System.out.println(strBuff.toString());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}

// Code für den Scanner aus
// http://www.avajava.com/tutorials/lessons/how-do-i-read-a-string-from-a-file-line-by-line.html
// entnommen