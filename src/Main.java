
import java.io.FileNotFoundException;
import java.util.HashMap;


public class Main {
    public static void main(String[] args) throws FileNotFoundException
    {
        //this is for Bayes
       TestBayes testBayes = new TestBayes();
      testBayes.WriteTxt();

        //this is for ID3
     /*   PrepereID3Data id=new PrepereID3Data();
        id.initializeData();
        Id3Data id3Data =new Id3Data();
        id3Data.Initializer(id.getMailCounter(),id.TrainingDataNumber,id.path);*/

        //this is for LogisticRegression
      /*  LogisticRegression logistic = new LogisticRegression();
        logistic.initializeData();*/



    }
}
