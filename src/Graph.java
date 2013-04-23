import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Jama.Matrix;

public class Graph {
  private Matrix matrix;

  private String[] ids;

  public Graph(String path) throws IOException {
    readEdgeToMatrix(path);
  }

  private void readEdgeToMatrix(String path) throws IOException {
    /* matrix using starting vertices (row index) as key */
    Map<String, List<String>> edges = new HashMap<String, List<String>>();
    /* the String id to index mapping */
    Map<String, Integer> ind = new HashMap<String, Integer>();
    BufferedReader br = new BufferedReader(new FileReader(path));
    String line = null;
    while ((line = br.readLine()) != null) {
      String[] tmp = line.split(" ");
      String out = tmp[0];
      String in = tmp[1];
      /* add to ind */
      if (!ind.containsKey(out))
        ind.put(out, ind.size());
      if (!ind.containsKey(in))
        ind.put(in, ind.size());
      /* add to edges */
      if (!edges.containsKey(out)) {
        ArrayList<String> list = new ArrayList<String>();
        list.add(in);
        edges.put(out, list);
      } else {
        edges.get(out).add(in);
      }
    }
    br.close();
    /* transform edges to adjacency matrix */
    int dim = ind.size();
    this.matrix = new Matrix(dim, dim);
    for (String out : ind.keySet()) {
      int indout = ind.get(out);
      if (edges.containsKey(out)) {
        List<String> list = edges.get(out);
        for (String in : list) {
          int indin = ind.get(in);
          matrix.set(indout, indin, 1);
        }
      }
    }
    /* transform ind to index to string id mapping */
    this.ids = new String[ind.size()];
    for (String id : ind.keySet()) {
      ids[ind.get(id)] = id;
    }
    System.out.println();
  }

  public Matrix getMatrix() {
    return this.matrix;
  }

  public String[] getIds() {
    return this.ids;
  }
}
