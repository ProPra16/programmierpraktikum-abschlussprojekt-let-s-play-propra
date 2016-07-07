package de.hhu.propra;

/**
 * Created by Viktor on 07.07.2016.
 */
public class Test {
  private boolean bestanden=false;
  private String Inhalt;
  private String TestName;

  public String getTestName(){
    return  this.TestName;
  }
  public void setTestName(String NeuerName){
    this.TestName=NeuerName;
  }
  public Test(String Inhalt,String NeuerName){
    this.Inhalt=Inhalt;
    this.TestName=NeuerName;
  }
  public String getInhalt(){
    return this.Inhalt;
  }
  public void setInhalt(String neuerInhalt){
    this.Inhalt=neuerInhalt;
  }
  public boolean istBestanden(){
    return this.bestanden;
  }
  public void setBestanden(boolean YesNo){
    this.bestanden=YesNo;
  }
}
