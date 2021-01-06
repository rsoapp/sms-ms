package rsoapp.smsms.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rsoapp.smsms.model.SendingDataDto;
import rsoapp.smsms.service.SmsService;

@RestController
@RequestMapping("/v1/sms/")
public class SmsController {

    private final SmsService smsService;

    public SmsController(SmsService smsService) {
        this.smsService = smsService;
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
}
