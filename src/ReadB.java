import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;

import static jdk.nashorn.internal.objects.NativeMath.round;

public class ReadB {
    private HashMap<Integer, Double[]> MailHash = new HashMap<Integer, Double[]>();
    private HashSet<Integer> tempSet;
    private int SpamCounter = 0;
    private int HammCounter = 0;

    private void inputToHashMap(String path) throws FileNotFoundException {

        for (int i = 1; i <=9; i++) {
            String localPath = path + Integer.toString(i);
            File dir = new File(localPath);
            for (File file : dir.listFiles()) {
                if (checkSpam(file)) {
                    SpamCounter++;
                } else {
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

                        } else {
                            MailHash.put(k, new Double[]{0.0, 0.0});
                            MailHash.get(k)[0] += 1;
                        }
                    }//end for
                } else {
                    for (int k : tempSet) {
                        if (MailHash.containsKey(k)) {
                            MailHash.get(k)[1] += 1;
                        } else {
                            MailHash.put(k, new Double[]{0.0, 0.0});
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
        SpamCounter += 2;
        HammCounter += 2;
        Propability();
        System.out.println("SpamCounter: " + SpamCounter + " HammCounter: " + HammCounter);
    }

    private boolean checkSpam(File dir) throws FileNotFoundException {
        if (dir.getName().contains("spmsg")) {
            return true;
        }
        return false;
    }

    private void Propability() {
        for (int lexi : MailHash.keySet()) {
            MailHash.get(lexi)[0] = Math.pow(Math.log(MailHash.get(lexi)[0] / (double)SpamCounter),2);
            MailHash.get(lexi)[1] = Math.pow(Math.log(MailHash.get(lexi)[1] / (double)HammCounter),2);
        }
    }

    public void ReadMail() throws FileNotFoundException {
        String path = "src\\pu_corpora_public\\pu1\\part";
        inputToHashMap(path);
        /*for (int word : MailHash.keySet()) {
            System.out.println(word + " " + "spam is: " + MailHash.get(word)[0] + " ham is: " + MailHash.get(word)[1]);
        }*/
        CalculateTest(path);
    }

    public double getSpamPropability() {
        return (double) SpamCounter / ((double)SpamCounter + (double)HammCounter);
    }

    public double getHamPropability() {
        return 1.0 - getSpamPropability();
    }

    public void CalculateTest(String path) throws FileNotFoundException {
        String localPath = path + Integer.toString(10);
        File dir = new File(localPath);
        int counterMessage=0;
        int correctCounterMessage=0;
        for (File file : dir.listFiles()) {
         counterMessage++;
            tempSet = new HashSet<Integer>();
            Scanner scanner = new Scanner(file);
            while (scanner.hasNext()) {
                String word = scanner.next();
                if (!word.equals("Subject:")) {
                    int cell = Integer.parseInt(word);
                    tempSet.add(cell);
                }
            }//end while
            double SpamPropability = 1.0;
            double HamPropability = 1.0;


            for (int word : tempSet) {
                if (MailHash.containsKey(word)) {

                    SpamPropability = SpamPropability + MailHash.get(word)[0];
                    HamPropability = HamPropability + MailHash.get(word)[1];


                } else {
                    SpamPropability = SpamPropability + Math.pow(Math.log(1.0/(double)SpamCounter),2);
                    HamPropability = HamPropability + Math.pow(Math.log(1.0/(double)HammCounter),2);

                }
              //  System.out.println("Word: " + word + " spam " + SpamPropability + " ham " + HamPropability);

            }

            if (SpamPropability * getSpamPropability() > HamPropability * getHamPropability()) {
                System.out.println("Its a Ham eimai sto file " + file.toString());
                if(!checkSpam(file)){
                   correctCounterMessage++;
                }
            } else {
                System.out.println("Its a Spam eimai sto file " + file.toString());
                if(checkSpam(file)){
                    correctCounterMessage++;
                }
            }

        }//end for

         System.out.println(correctCounterMessage);
         System.out.println(counterMessage);
         double d =(double) correctCounterMessage/(double) counterMessage;
         System.out.println(d*100);
        System.out.println(getSpamPropability());
        System.out.println(getHamPropability());
    }
}

