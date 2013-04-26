import java.util.ArrayList;
import java.util.List;

import Jama.Matrix;

public abstract class Clustering {
  /* adjacency matrix */
  protected double[][] matrix;

  protected String[] ids;

  private static int MAX_ITERATION = 50;

  public Clustering() {
  }

  protected void normalize(Matrix m) {
    int rowDim = m.getRowDimension();
    int[] degree = new int[rowDim];
    /* get degree */
    for (int i = 0; i < rowDim; i++) {
      int colDim = m.getColumnDimension();
      for (int j = 0; j < colDim; j++) {
        degree[i] += m.get(i, j);
      }
    }
    /* normalize */
    for (int i = 0; i < rowDim; i++) {
      int colDim = m.getColumnDimension();
      for (int j = 0; j < colDim; j++) {
        m.set(i, j, m.get(i, j) / degree[i]);
      }
    }
  }

  protected void toLap(Matrix matrix) {
    int rowDim = matrix.getRowDimension();
    int[] degree = new int[rowDim];
    /* get degree */
    for (int i = 0; i < rowDim; i++) {
      int colDim = matrix.getColumnDimension();
      for (int j = 0; j < colDim; j++) {
        degree[i] += matrix.get(i, j);
      }
    }
    /* normalize */
    for (int i = 0; i < rowDim; i++) {
      int colDim = matrix.getColumnDimension();
      for (int j = 0; j < colDim; j++) {
        if (i == j)
          matrix.set(i, j, 1);
        else
          matrix.set(i, j, matrix.get(i, j) / degree[i]);
      }
    }
  }

  public List<List<String>> kmeans(int k) {
    ArrayList<Integer> centroidid = new ArrayList<Integer>();
    /* randomly choose k centroids */
    for (int i = 0; i < k; i++) {
      int cent = (int) (Math.random() * matrix.length);
      while (centroidid.contains(cent)) {
        cent = (int) (Math.random() * matrix.length);
      }
      centroidid.add(cent);
    }
    /* get the initial centroids */
    double[][] centroids = new double[k][matrix[0].length];
    for (int i = 0; i < k; i++) {
      double[] tmp = matrix[centroidid.get(i)];
      for (int j = 0; j < tmp.length; j++) {
        centroids[i][j] = tmp[j];
      }
    }
    /* do kmeans iteration */
    int iteration = 0;
    List<List<Integer>> clusters = null;
    while (iteration++ < MAX_ITERATION) {
      /* initialize the clusters structure */
      clusters = new ArrayList<List<Integer>>();
      for (int i = 0; i < k; i++) {
        clusters.add(new ArrayList());
      }
      /* for each data point */
      for (int i = 0; i < matrix.length; i++) {
        int clusterid = 0;
        double sim = Double.MAX_VALUE;
        /* find the closest cluster */
        for (int j = 0; j < k; j++) {
          double cursim = computeSimilarity(matrix[i], centroids[j]);
          if (cursim < sim) {
            clusterid = j;
            sim = cursim;
          }
        }
        clusters.get(clusterid).add(i);
      }
      /* update the centroids */
      int newk = 0;
      for (int i = 0; i < k; i++) {
        List<Integer> list = clusters.get(i);
        int size = list.size();
        if (size != 0) {
          double[] centroid = new double[matrix[0].length];
          for (int ind : list) {
            for (int j = 0; j < centroid.length; j++) {
              centroid[j] += matrix[ind][j];
            }
          }
          for (int j = 0; j < centroid.length; j++) {
            centroid[j] /= size;
          }
          centroids[newk++] = centroid;
        }
      }
      k = newk;
    }
    List<List<String>> result = new ArrayList<List<String>>();
    System.out.println("cluster num " + clusters.size());
    if (clusters != null) {
      for (List<Integer> list : clusters) {
        List<String> r = new ArrayList<String>();
        for (int ind : list) {
          System.out.print(ids[ind] + '\t');
          r.add(ids[ind]);
        }
        result.add(r);
        System.out.print('\n');
      }
    }
    return result;
  }

  public List<List<String>> kmeans(List<Integer> centroidid) {
    int k = centroidid.size();
    /* get the initial centroids */
    double[][] centroids = new double[k][matrix[0].length];
    for (int i = 0; i < k; i++) {
      double[] tmp = matrix[centroidid.get(i)];
      for (int j = 0; j < tmp.length; j++) {
        centroids[i][j] = tmp[j];
      }
    }
    /* do kmeans iteration */
    int iteration = 0;
    List<List<Integer>> clusters = null;
    while (iteration++ < MAX_ITERATION) {
      /* initialize the clusters structure */
      clusters = new ArrayList<List<Integer>>();
      for (int i = 0; i < k; i++) {
        clusters.add(new ArrayList());
      }
      /* for each data point */
      for (int i = 0; i < matrix.length; i++) {
        int clusterid = 0;
        double sim = Double.MAX_VALUE;
        /* find the closest cluster */
        for (int j = 0; j < k; j++) {
          double cursim = computeSimilarity(matrix[i], centroids[j]);
          if (cursim < sim) {
            clusterid = j;
            sim = cursim;
          }
        }
        clusters.get(clusterid).add(i);
      }
      /* update the centroids */
      int newk = 0;
      for (int i = 0; i < k; i++) {
        List<Integer> list = clusters.get(i);
        int size = list.size();
        if (size != 0) {
          double[] centroid = new double[matrix[0].length];
          for (int ind : list) {
            for (int j = 0; j < centroid.length; j++) {
              centroid[j] += matrix[ind][j];
            }
          }
          for (int j = 0; j < centroid.length; j++) {
            centroid[j] /= size;
          }
          centroids[newk++] = centroid;
        }
      }
      k = newk;
    }
    List<List<String>> result = new ArrayList<List<String>>();
    System.out.println("cluster num " + clusters.size());
    if (clusters != null) {
      for (List<Integer> list : clusters) {
        List<String> r = new ArrayList<String>();
        for (int ind : list) {
          System.out.print(ids[ind] + '\t');
          r.add(ids[ind]);
        }
        result.add(r);
        System.out.print('\n');
      }
    }
    return result;
  }

  private double computeSimilarity(double[] vertex, double[] cent) {
    double sim = 0;
    for (int i = 0; i < vertex.length; i++) {
      sim += Math.pow(vertex[i] - cent[i], 2);
    }
    return Math.sqrt(sim);
  }

}
