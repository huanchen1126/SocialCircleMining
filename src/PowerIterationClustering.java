import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import Jama.Matrix;

public class PowerIterationClustering extends Clustering {

  private static double THRESHOLD = 0.01;

  public PowerIterationClustering(String path) throws IOException {
    Graph graph = new Graph(path);
    Matrix m = graph.getMatrix();
    normalize(m);
    this.matrix = pic(m);
    this.ids = graph.getIds();
  }

  private double[][] pic(Matrix m) {
    /* initialize v */
    System.out.println(m.getRowDimension());
    Matrix preV = new Matrix(m.getRowDimension(), 1);
    for (int i = 0; i < preV.getRowDimension(); i++)
      preV.set(i, 0, Math.random());
    Matrix curV = null;
    double preDiff = 0;
    double curDiff = Double.MAX_VALUE;
    int iteration = 1;
    /* do power iteration */
    while (Math.abs(curDiff - preDiff) > THRESHOLD) {
      // System.out.println("iteration " + iteration++ + " dist: " + Math.abs(curDiff - preDiff));
      /* update previous difference */
      preDiff = curDiff;
      /* the new v */
      curV = m.times(preV);
      /* compute the diff */
      curDiff = getDiff(preV, curV);
      /* update previous vector */
      preV = curV;
    }
    return curV.getArray();
  }

  private double getDiff(Matrix v1, Matrix v2) {
    double diff = 0.0;
    for (int i = 0; i < v1.getRowDimension(); i++)
      diff += Math.pow(v1.get(i, 0) - v2.get(i, 0), 2);
    return Math.sqrt(diff);
  }

  

  public static void main(String[] args) throws IOException {
    // long a=System.currentTimeMillis();
    PowerIterationClustering pic = new PowerIterationClustering(
            "/Users/huanchen/Desktop/lab4/facebook/698.edges");
    Evaluation eva = new Evaluation("/Users/huanchen/Desktop/lab4/facebook/698.circles");
    long a = System.currentTimeMillis();
    eva.evaluate(pic.kmeans(3));
    System.out.println("\r<br>time : " + (System.currentTimeMillis() - a) / 1000f + " seconds");
  }
}
