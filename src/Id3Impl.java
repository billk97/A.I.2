import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Scanner;

public class Id3Impl {
    public HashMap<Integer, Integer[]> wordHash;
    public HashSet<Integer> tempSet;
    public String path= "src\\pu_corpora_public\\pu3\\part";
    private int spamCounter=0;
    private int hamCounter=0;


    public void fillWordHash() throws FileNotFoundException {
        wordHash=new HashMap<Integer, Integer[]>();
        int MailCounter=0;
        /**loops all the directories until  8**/
        for (int i = 1; i <= 8; i++) {
            String localPath = path + Integer.toString(i);
            File dir = new File(localPath);
            /**lops all the files**/
            for (File file : dir.listFiles()) {
                MailCounter++;
                tempSet = new HashSet<Integer>();
                Scanner scanner = new Scanner(file);
                while (scanner.hasNext()) {
                    String word = scanner.next();
                    if (!word.equals("Subject:")) {
                        tempSet.add(Integer.parseInt(word));
                    }//end if
                }//end while tempset filled
                if(!checkSpam(file)){
                    hamCounter++;
                    for(int k: tempSet){
                        if(wordHash.containsKey(k)){
                            wordHash.get(k)[0]+=1;
                        }else{
                            wordHash.put(k,new Integer[]{1,0,0,0});
                        }//word found in spam mail
                    }//end for
                }else{
                    spamCounter++;
                    for(int k: tempSet){
                        if(wordHash.containsKey(k)){
                            wordHash.get(k)[1]+=1;
                        }else{
                            wordHash.put(k,new Integer[]{0,1,0,0});
                        }//word found in ham mail
                    }
                }
                scanner.close();
            }//end for
        }//end for
    }//end fillWordHash

    public void fillNonFoundWords(){
        for(int k: wordHash.keySet()){
            wordHash.get(k)[2]=hamCounter-wordHash.get(k)[0];
            wordHash.get(k)[3]=spamCounter-wordHash.get(k)[1];
        }

    }



    private boolean checkSpam(File dir) throws FileNotFoundException {
        if (dir.getName().contains("spmsg")) {
            return true;
        }
        return false;
    }

    public void printHash(){
        for(int k: wordHash.keySet()){
            System.out.println("word possibilities are ="+wordHash.get(k)[0]+" "+wordHash.get(k)[1]+" "+wordHash.get(k)[2]+" "+wordHash.get(k)[3]);
        }
    }



}
