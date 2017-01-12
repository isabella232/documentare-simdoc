package com.orange.documentare.core.image.linedetection;
/*
 * Copyright (c) 2016 Orange
 *
 * Authors: Christophe Maldivi & Joel Gardes
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 */

import com.googlecode.zohhak.api.TestWith;
import com.googlecode.zohhak.api.runners.ZohhakRunner;
import com.orange.documentare.core.image.connectedcomponents.ConnectedComponents;
import com.orange.documentare.core.image.opencv.OpencvLoader;
import com.orange.documentare.core.image.test.TestDrawer;
import com.orange.documentare.core.model.io.Gzip;
import com.orange.documentare.core.model.json.JsonGenericHandler;
import org.fest.assertions.Assertions;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.IOException;

@RunWith(ZohhakRunner.class)
public class RawLinesBuilderTest {

  static {
    OpencvLoader.load();
  }

  @TestWith({
          "/latin_connected_components_ref.json.gz, /latin_raw_lines_ref.json.gz, latin_raw_lines.json.gz, /latin.png, latin_raw_lines.png"
  })
  public void buildRawLines(String connectedComponentsFileResource, String refFileResource, String outputLines, String inputImage, String outputImage) throws IOException {
    // given
    JsonGenericHandler jsonGenericHandler = new JsonGenericHandler(true);
    File connectedComponentsFile = new File(getClass().getResource(connectedComponentsFileResource).getFile());
    ConnectedComponents connectedComponents = (ConnectedComponents) jsonGenericHandler.getObjectFromJsonGzipFile(ConnectedComponents.class, connectedComponentsFile);

    File ccRefFile = new File(getClass().getResource(refFileResource).getFile());
    String ccRefJsonString = Gzip.getStringFromGzipFile(ccRefFile);

    RawLinesBuilder rawLinesBuilder = new RawLinesBuilder(Integer.MAX_VALUE);
    File outputFile = new File(outputLines);

    // when
    Lines rawlines = rawLinesBuilder.build(connectedComponents);
    TestDrawer.drawLines(new File(getClass().getResource(inputImage).getFile()), new File(outputImage), rawlines);

    jsonGenericHandler.writeObjectToJsonGzipFile(rawlines, outputFile);
    String ccTestJsonString = Gzip.getStringFromGzipFile(outputFile);

    // then
    Assertions.assertThat(ccTestJsonString).isEqualTo(ccRefJsonString);
  }
}