import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import Jama.Matrix;

public class GraphKmeans extends Clustering {

  public GraphKmeans(String path) throws IOException {
    Graph graph = new Graph(path);
    Matrix m = graph.getMatrix();
    normalize(m);
    m = mulMatrixK(6, m);
    this.matrix = m.getArray();
    ids = graph.getIds();
  }

  private Matrix mulMatrixK(int k, Matrix matrix) {
    while (k-- > 0) {
      matrix = matrix.times(matrix);
    }
    return matrix;
  }

  public static void main(String[] args) throws IOException {
    String id = "698";
    long a = System.currentTimeMillis();
    GraphKmeans gk = new GraphKmeans("/Users/huanchen/Desktop/lab4/facebook/" + id + ".edges");
    Evaluation eva = new Evaluation("/Users/huanchen/Desktop/lab4/facebook/" + id + ".circles");
    ClusterTagging cTag = new ClusterTagging("/Users/huanchen/Desktop/lab4/facebook/" + id
            + ".featnames", "/Users/huanchen/Desktop/lab4/facebook/" + id + ".feat");
    List<List<String>> clusters = gk.kmeans(3);
    eva.evaluate(clusters);
    cTag.tagCluster(clusters);
    System.out.println("\r<br>time : " + (System.currentTimeMillis() - a) / 1000f + " seconds");
  }
}
