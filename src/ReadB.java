import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;

public class ReadB {

    private HashMap<Integer, int[]> MailHash = new HashMap();
    private int WordTable[] = {1,1};
    private HashSet<Integer> tempSet=new HashSet();


    private void inputToHashMap(String path) throws FileNotFoundException {
        for (int i = 1; i <= 8; i++) {
            String localPath = path + Integer.toString(i);

            File dir = new File(localPath);
            for (File file : dir.listFiles()) {

                Scanner scanner = new Scanner(file);
                while (scanner.hasNext()) {
                    String word = scanner.next();
                    if (!word.equals("Subject:")) {
                        int cell = Integer.parseInt(word);
                        tempSet.add(cell);

                    }
                }//end while

                /*for(int k: tempSet){
                    System.out.println("tempset"+metritis+ "has :"+k);
                }*/

                if(checkSpam(file)){
                    for(int k: tempSet){
                        if(MailHash.containsKey(k)){
                            MailHash.get(k)[0]+=1;
                        }else{
                            MailHash.put(k,WordTable);
                        }
                    }//end for
                } else{
                    for(int k: tempSet){
                        if(MailHash.containsKey(k)){
                            MailHash.get(k)[1]+=1;
                        }else{
                            MailHash.put(k,WordTable);
                        }
                    }//end for
                }
                tempSet.clear();
                scanner.close();
            }
        }
    }



    private boolean checkSpam (File dir) throws FileNotFoundException
    {
        if (dir.getName().contains("spmsg")) {
            return true;
        }
        return false;
    }

    public void ReadMail() throws FileNotFoundException{
        String path = "src\\pu_corpora_public\\pu1\\part";

        inputToHashMap(path);
        for (int word: MailHash.keySet()){

            int key =word;
            System.out.println(key + " " +"spam is: "+MailHash.get(word)[0]+"ham is: "+MailHash.get(word)[1]);
        }


    }

}

