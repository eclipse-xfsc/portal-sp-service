package eu.gaia_x.spservice.rest;

import eu.gaia_x.spservice.model.ErrorDto;
import eu.gaia_x.spservice.model.SaveRequest;
import eu.gaia_x.spservice.util.ProxyCall;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/sp-service")
@Api(tags = "Solution Packaging Service")
public class SaveController {

  @Autowired
  @Qualifier("demoSrv")
  WebClient srv;

  @ApiOperation("Save Solution Packaging")
  @PostMapping(value = "/save")
  public ResponseEntity save(HttpServletRequest request, @RequestBody SaveRequest rq) {
    log.info("Request received: {}", rq);
    if (!"save".equals(rq.getAction()) && !"build".equals(rq.getAction())) {
      return ResponseEntity.badRequest().body(
              new ErrorDto(
                      "/api/sp-service/save",
                      "Incorrect action type"
              )
      );
    }

    if (rq.getSelectedItems() == null || rq.getSelectedItems().isEmpty()) {
      return ResponseEntity.badRequest().body(
              new ErrorDto(
                      "/api/sp-service/save",
                      "Incorrect selected items specification"
              )
      );
    }

    try {
      final ResponseEntity<Map<String, ?>> re = ProxyCall.doPost(srv, request, rq);
      if (re == null) {
        return ResponseEntity.badRequest().build();
      }

      return ResponseEntity.ok().build();
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(
              new ErrorDto(
                      "/api/sp-service/save",
                      e.getMessage()
              )
      );
    }
  }
}
