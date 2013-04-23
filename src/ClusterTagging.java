import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClusterTagging {
  /* dictionary, tag index to name mapping */
  Map<Integer, String> dict;

  /* user features */
  Map<String, int[]> frdFeatures;

  /* num of features */
  int numFeatures;

  public ClusterTagging(String dictPath, String uFeaturePath) throws IOException {
    /* read the dict */
    dict = new HashMap<Integer, String>();
    BufferedReader br = new BufferedReader(new FileReader(dictPath));
    String line = null;
    while ((line = br.readLine()) != null) {
      numFeatures++;
      int ind = line.indexOf(' ');
      String id = line.substring(0, ind);
      String feature = line.substring(ind + 1);
      dict.put(Integer.parseInt(id), feature);
    }
    br.close();
    /* read the features */
    frdFeatures = new HashMap<String, int[]>();
    br = new BufferedReader(new FileReader(uFeaturePath));
    line = null;
    while ((line = br.readLine()) != null) {
      int ind = line.indexOf(' ');
      String frd = line.substring(0, ind);
      String[] features = line.substring(ind + 1).split(" ");
      int[] iFeatures = new int[features.length];
      for (int i = 0; i < features.length; i++)
        iFeatures[i] = Integer.parseInt(features[i]);
      frdFeatures.put(frd, iFeatures);
    }
    br.close();
  }

  public void tagCluster(List<List<String>> clusters) {
    /* cluster features */
    List<int[]> clusterFeatures = new ArrayList<int[]>();
    for (List<String> cluster : clusters) {
      int[] feature = new int[numFeatures];
      for (String frd : cluster) {
        int[] frdFeature = frdFeatures.get(frd);
        for (int i = 0; i < numFeatures; i++) {
          feature[i] += frdFeature[i];
        }
      }
      clusterFeatures.add(feature);
    }
    /* print result */
    for (int[] feature : clusterFeatures) {
      for (int i = 0; i < numFeatures; i++) {
        System.out.print("feature:" + dict.get(i) + ",count:" + feature[i]+"\t");
      }
      System.out.print("\n");
    }
  }
}
