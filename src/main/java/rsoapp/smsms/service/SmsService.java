package rsoapp.smsms.service;

import com.twilio.rest.api.v2010.account.Message;
import com.twilio.rest.api.v2010.account.MessageCreator;
import com.twilio.type.PhoneNumber;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import rsoapp.smsms.config.TwilioConfig;
import rsoapp.smsms.model.SendingDataDto;
import rsoapp.smsms.model.UserDto;

@Service
public class SmsService {

    private final String userMsUrl = "http://localhost:8083/v1/user/";
    private final RestTemplate restTemplate;
    private final TwilioConfig twilioConfig;

    public SmsService(RestTemplate restTemplate, TwilioConfig twilioConfig) {
        this.restTemplate = restTemplate;
        this.twilioConfig = twilioConfig;
    }

    public ResponseEntity<Void> sendSms(SendingDataDto sendingDataDto) {
        try {
            UserDto fromUser = getUserData(sendingDataDto.getFromId());
            UserDto toUser = getUserData(sendingDataDto.getToId());

            if (fromUser != null && toUser != null) {
                PhoneNumber to = new PhoneNumber(toUser.getPhoneNumber());
                PhoneNumber from = new PhoneNumber(twilioConfig.getTrialNumber());
                String message = "Dobili ste novo sporočilo s spletne strani adapp.\nKontakt pošiljatelja:\n" +
                        fromUser.getPhoneNumber() + "\n" +
                        fromUser.getEmail() + "\n\n" + sendingDataDto.getMessage();

                MessageCreator creator = Message.creator(to, from, message);
                creator.create();
                return new ResponseEntity<>(HttpStatus.OK);
            }
            else {
                return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // get user data from user microservice
    public UserDto getUserData(String userId) {
        try {
            return restTemplate.getForObject(userMsUrl + userId, UserDto.class);
        } catch (Exception e) {
            return null;
        }
    }
}
