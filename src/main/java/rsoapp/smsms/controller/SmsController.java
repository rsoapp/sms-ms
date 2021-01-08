package rsoapp.smsms.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rsoapp.smsms.health.CustomHealthCheck;
import rsoapp.smsms.model.SendingDataDto;
import rsoapp.smsms.service.SmsService;

@RestController
@RequestMapping("/v1/sms/")
public class SmsController {

    private final SmsService smsService;
    private final CustomHealthCheck customHealthCheck;

    public SmsController(SmsService smsService, CustomHealthCheck customHealthCheck) {
        this.smsService = smsService;
        this.customHealthCheck = customHealthCheck;
    }

    @PostMapping("send")
    public ResponseEntity<Void> sendSms(@RequestParam("fromId") String fromId,
                                        @RequestParam("toId") String toId,
                                        @RequestParam("message") String message) {
        try {
            return smsService.sendSms(new SendingDataDto(fromId, toId, message));
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("break")
    public ResponseEntity<Void> makeUnhealthy() {
        customHealthCheck.setState("DOWN");
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
