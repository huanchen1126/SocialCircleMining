import java.io.IOException;
import java.util.List;

import Jama.EigenvalueDecomposition;
import Jama.Matrix;

public class SpectralClustering extends Clustering {

  public SpectralClustering(String path, int k) throws IOException {
    Graph graph = new Graph(path);
    Matrix m = graph.getMatrix();
    toLap(m);
    /* get the eigen vectors */
    EigenvalueDecomposition ed = m.eig();
    Matrix v = ed.getV();
    matrix = v.getMatrix(0, v.getRowDimension() - 1, 0, k).getArray();
    ids = graph.getIds();
  }

  public static void main(String[] args) throws IOException {
    String id = "698";
    long a = System.currentTimeMillis();
    SpectralClustering sc = new SpectralClustering("/Users/huanchen/Desktop/lab4/facebook/" + id
            + ".edges", 3);
    Evaluation eva = new Evaluation("/Users/huanchen/Desktop/lab4/facebook/" + id + ".circles");
    ClusterTagging cTag = new ClusterTagging("/Users/huanchen/Desktop/lab4/facebook/" + id
            + ".featnames", "/Users/huanchen/Desktop/lab4/facebook/" + id + ".feat");
    List<List<String>> clusters = sc.kmeans(3);
    eva.evaluate(clusters);
    cTag.tagCluster(clusters);
    System.out.println("\r<br>time : " + (System.currentTimeMillis() - a) / 1000f + " seconds");
  }
}
