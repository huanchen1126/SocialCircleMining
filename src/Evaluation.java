import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class Evaluation {
  HashMap<String, HashSet<String>> circles;

  public Evaluation(String path) throws IOException {
    circles = new HashMap<String, HashSet<String>>();
    BufferedReader br = new BufferedReader(new FileReader(path));
    String line = null;
    while ((line = br.readLine()) != null) {
      String tmp[] = line.split("\t");
      for (int i = 1; i < tmp.length; i++)
        for (int j = i + 1; j < tmp.length; j++) {
          String v1 = tmp[i];
          String v2 = tmp[2];
          /* add v2 to v1's circle */
          if (!circles.containsKey(v1)) {
            HashSet<String> set = new HashSet<String>();
            set.add(v2);
            circles.put(v1, set);
          } else {
            circles.get(v1).add(v2);
          }
          /* add v1 to v2's circle */
          if (!circles.containsKey(v2)) {
            HashSet<String> set = new HashSet<String>();
            set.add(v1);
            circles.put(v2, set);
          } else {
            circles.get(v2).add(v1);
          }
        }
    }
    br.close();
  }

  public void evaluate(List<List<String>> clusters) {
    /* build an index */
    HashMap<String, Integer> index = new HashMap<String, Integer>();
    for (int i = 0; i < clusters.size(); i++) {
      List<String> cluster = clusters.get(i);
      for (String v : cluster) {
        index.put(v, i);
      }
    }
    /* evaluate */
    int correct = 0;
    int wrong = 0;
    for (String v1 : circles.keySet()) {
      HashSet<String> circle = circles.get(v1);
      for (String v2 : circle) {
        if (index.containsKey(v1)) {
          if (clusters.get(index.get(v1)).contains(v2))
            correct++;
          else
            wrong++;
        }
      }
    }
    System.out.println("Precision: " + (double) correct / (correct + wrong));
  }

  public void evaluate1(List<List<String>> clusters) {
    int correct = 0;
    int wrong = 0;
    for (List<String> cluster : clusters) {
      int size = cluster.size();
      for (int i = 0; i < size; i++) {
        for (int j = i; j < size; j++) {
          String v1 = cluster.get(i);
          String v2 = cluster.get(j);
          if (circles.containsKey(v1)) {
            if (circles.get(v1).contains(v2))
              correct++;
            else
              wrong++;
          }
          if (circles.containsKey(v2)) {
            if (circles.get(v2).contains(v1))
              correct++;
            else
              wrong++;
          }
        }
      }
    }
    System.out.println("Precision: " + (double) correct / (correct + wrong));
  }
}
