import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.File;

public class AddString {
    public static void main(String[] args) {
        String line;
        try (
                BufferedReader br = new BufferedReader(new FileReader(new File(args[0])))
            ) 
                {
                  String[] content = {"a"};
                
                  line = br.readLine();
                  content = line.split(",");
                  

                  for (int i = 0; i < content.length; i++) {
                    StringBuffer sb = new StringBuffer(content[i]);
                    sb.insert(1, '"');
                    sb.append('"');
                    content[i] = sb.toString();
                  }

                  try ( BufferedWriter bw = new BufferedWriter(new FileWriter(new File(args[1]))) ) {
                    
                    for (int i = 0; i < content.length; i++) {
                        if (i == 0) {
                            bw.write("{" + content[i] + ",\n");
                        }
                        else if (i == content.length-1) {
                            bw.write("\t" + content[i] + "}");
                        } else {
                            bw.write('\t'  + content[i] + ",\n");
                        }
                    }
                    System.out.println("\n\u001B[32mFiles changed succsefully\u001B[0m");
                  } catch (IOException e) {
                    System.out.println(e.getLocalizedMessage() + " Something wrong writing");
                  }
                } 

            catch (IOException e) {
                System.out.println(e.getMessage() + " Something wrong reading");
            }

            

    }
}
