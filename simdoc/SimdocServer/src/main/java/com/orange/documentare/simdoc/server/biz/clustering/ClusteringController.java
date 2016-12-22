package com.orange.documentare.simdoc.server.biz.clustering;

import com.orange.documentare.simdoc.server.biz.clustering.ClusteringRequest.RequestValidation;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST;

@Slf4j
@RestController
public class ClusteringController implements ClusteringApi {

  @Autowired
  ClusteringService clusteringService;

  public ClusteringResult clustering(
    @ApiParam(value = "Clustering request parameters", required=true)
    @RequestBody
      ClusteringRequest req, HttpServletResponse res) throws IOException {
    log.info("[Clustering request] " + req);
    RequestValidation validation = req.validate();
    if (validation.ok) {
      return clusteringService.build(req.inputDirectory, req.outputDirectory, req.clusteringParameters(), req.debug());
    } else {
      res.sendError(SC_BAD_REQUEST, validation.error);
      return ClusteringResult.error(validation.error);
    }
  }
}
