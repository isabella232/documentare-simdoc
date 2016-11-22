package com.orange.documentare.core.comp.clustering.graph.jgrapht;
/*
 * Copyright (c) 2016 Orange
 *
 * Authors: Christophe Maldivi & Joel Gardes
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 */

import com.orange.documentare.core.model.ref.clustering.graph.ClusteringGraph;
import com.orange.documentare.core.model.ref.clustering.graph.GraphItem;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.util.List;

@Log4j2
@RequiredArgsConstructor(suppressConstructorProperties = true)
public class TriangulationGraphBuilder extends GraphBuilder {

  @Override
  protected void addEdges(ClusteringGraph clusteringGraph) {
    for (GraphItem graphItem : getGraphItems()) {
      if (graphItem.isTriangleSingleton()) {
        log.debug("Do not add edges for item {}", graphItem.getVertexName());
      } else {
        addEdgesFor(graphItem);
      }
    }
  }

  private void addEdgesFor(GraphItem graphItem) {
    int indexVertex1 = graphItem.getVertex1Index();
    int indexVertex2 = graphItem.getVertex2Index();
    int indexVertex3 = graphItem.getVertex3Index();
    int[] edgesLength = graphItem.getEdgesLength();
    tryToConnect(indexVertex1, indexVertex2, edgesLength[0]);
    tryToConnect(indexVertex2, indexVertex3, edgesLength[1]);
    tryToConnect(indexVertex3, indexVertex1, edgesLength[2]);
  }

  private void tryToConnect(int indexVertex1, int indexVertex2, int edgeLength) {
    if (noSingletons(indexVertex1, indexVertex2)) {
      JGraphEdge e = getGraph().addEdge(getGraphItems().get(indexVertex1), getGraphItems().get(indexVertex2));
      e.init(indexVertex1, indexVertex2, edgeLength);
      getGraph().setEdgeWeight(e, edgeLength);
    }
  }

  private boolean noSingletons(int indexVertex1, int indexVertex2) {
    List<GraphItem> items = getGraphItems();
    return !items.get(indexVertex1).isTriangleSingleton() && !items.get(indexVertex2).isTriangleSingleton();
  }
}