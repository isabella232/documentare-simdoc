package com.orange.documentare.simdoc.server.biz.distances;

import com.orange.documentare.simdoc.server.biz.CachesStats;
import com.orange.documentare.simdoc.server.biz.clustering.RequestValidation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST;

@Slf4j
@RestController
public class DistancesController implements DistancesApi {

  @Autowired
  DistancesService distancesService;

  @Override
  public DistancesRequestResult distances(
    @RequestBody DistancesRequest req, HttpServletResponse res) throws IOException {
    log.info("[Distances request] " + req);

    RequestValidation validation = req.validate();
    if (!validation.ok) {
      return error(res, validation.error);
    }

    try {
      DistancesRequestResult result = compute(req);
      CachesStats.log();
      return result;
    } catch (IOException e) {
      return error(res, e.getMessage());
    }
  }

  private DistancesRequestResult compute(DistancesRequest req) throws IOException {
    return distancesService.compute(req);
  }

  private DistancesRequestResult error(HttpServletResponse res, String error) throws IOException {
    res.sendError(SC_BAD_REQUEST, error);
    return DistancesRequestResult.error(error);
  }
}
