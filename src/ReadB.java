import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;

public class ReadB {
    private HashMap<Integer, Double[]> MailHash = new HashMap<Integer, Double[]>();
    private HashSet<Integer> tempSet;
    private int SpamCounter=0;
    private int HammCounter=0;
    private void inputToHashMap(String path) throws FileNotFoundException {

        for (int i = 1; i <= 9; i++) {
            String localPath = path + Integer.toString(i);
            File dir = new File(localPath);
            for (File file : dir.listFiles()) {
                if(checkSpam(file))
                {
                    SpamCounter++;
                }else
                {
                        HammCounter++;
                }
                tempSet = new HashSet<Integer>();
                Scanner scanner = new Scanner(file);
                while (scanner.hasNext()) {
                    String word = scanner.next();
                    if (!word.equals("Subject:")) {
                        int cell = Integer.parseInt(word);
                        tempSet.add(cell);
                    }
                }//end while
                if (checkSpam(file)) {
                    for (int k : tempSet) {
                        if (MailHash.containsKey(k)) {

                            MailHash.get(k)[0] += 1;
                           // System.out.println("test: " + k + "getk0: " + MailHash.get(k)[0]);
                        } else {
                            MailHash.put(k, new Double[]{0.0,0.0});
                            MailHash.get(k)[0] += 1;
                        }
                    }//end for
                } else {
                    for (int k : tempSet) {
                        if (MailHash.containsKey(k)) {
                            MailHash.get(k)[1] += 1;
                        } else {
                            MailHash.put(k, new Double[]{0.0,0.0});
                            MailHash.get(k)[1] += 1;
                        }
                    }//end for
                }
                scanner.close();
                // tempSet.clear();
            }
        }
        for (int lexi : MailHash.keySet()) {
            MailHash.get(lexi)[0]++;
            MailHash.get(lexi)[1]++;
        }
        SpamCounter+=2;
        HammCounter+=2;
        Propability();
        System.out.println("SpamCounter: "+ SpamCounter+" HammCounter: " + HammCounter);
    }
    private boolean checkSpam (File dir) throws FileNotFoundException
    {
        if (dir.getName().contains("spmsg")) {
            return true;
        }
        return false;
    }
    private  void Propability()
    {
        for(int lexi: MailHash.keySet())
        {
            MailHash.get(lexi)[0]= Math.log(MailHash.get(lexi)[0]/SpamCounter);
            MailHash.get(lexi)[1]= Math.log(MailHash.get(lexi)[1]/HammCounter);
        }
    }

    public void ReadMail() throws FileNotFoundException{
        String path = "src\\pu_corpora_public\\pu1\\part";

        inputToHashMap(path);
//        for (int word: MailHash.keySet()){
//            System.out.println(word + " " +"spam is: "+MailHash.get(word)[0]+" ham is: "+MailHash.get(word)[1]);
//        }
        CalculateTest(path);
    }
    public double getSpamPropability()
    {
        return SpamCounter/(SpamCounter+HammCounter);
    }
    public double getHamPropability()
    {
        return 1-getSpamPropability();
    }
    public void CalculateTest(String path) throws FileNotFoundException {
        String localPath = path + Integer.toString(10);
        File dir = new File(localPath);

        for (File file : dir.listFiles())
        {
            double SpamPropability=1.0;
            double HamPropability=1.0;
            tempSet = new HashSet<Integer>();
            Scanner scanner = new Scanner(file);
            while (scanner.hasNext()) {
                String word = scanner.next();
                if (!word.equals("Subject:")) {
                    int cell = Integer.parseInt(word);
                    tempSet.add(cell);
                }
            }//end while
            for(int word : tempSet)
            {
                if(MailHash.containsKey(word))
                {
                    SpamPropability=SpamPropability*MailHash.get(word)[0];
                    HamPropability=HamPropability*MailHash.get(word)[1];
                }
                else
                {
                    SpamPropability=SpamPropability*(1/SpamCounter);
                    HamPropability=HamPropability*(1/HammCounter);
                }
                System.out.println("word: " +word +" spam Propability: "+ SpamPropability + " hamPropability: " + HamPropability );
            }
            if(SpamPropability*getSpamPropability()<= HamPropability*getHamPropability())
            {
                System.out.println("Hamm " + file.toString() + " hameprp: " + HamPropability*getHamPropability() + " spam: " + SpamPropability*getSpamPropability());
            }
            else
                {
                    System.out.println("Spam" + file.toString() + " hameprp: " + HamPropability*getHamPropability() + " spam: " + SpamPropability*getSpamPropability());
                }
        }//end for

    }
}

