package rsoapp.smsms.service;

import com.twilio.rest.api.v2010.account.Message;
import com.twilio.rest.api.v2010.account.MessageCreator;
import com.twilio.type.PhoneNumber;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import rsoapp.smsms.config.ApplicationVariables;
import rsoapp.smsms.model.SendingDataDto;
import rsoapp.smsms.model.UserDto;

@Service
public class SmsService {

    private String userMsUrl;
    private final String twilioTrialNumber = "+17864606545";
    private final RestTemplate restTemplate;
    private ApplicationVariables applicationVariables;

    public SmsService(RestTemplate restTemplate, ApplicationVariables applicationVariables) {
        this.restTemplate = restTemplate;
        this.applicationVariables = applicationVariables;

        if (applicationVariables.getEnvironmentType().equals("prod")) {
            userMsUrl = "http://user-ms:8080/v1/user/";
        }
        else {
            userMsUrl = "http://localhost:8083/v1/user/";
        }
    }

    public ResponseEntity<Void> sendSms(SendingDataDto sendingDataDto) {
        try {
            UserDto fromUser = getUserData(sendingDataDto.getFromId());
            UserDto toUser = getUserData(sendingDataDto.getToId());

            if (fromUser != null && toUser != null) {
                PhoneNumber to = new PhoneNumber(toUser.getPhoneNumber());
                PhoneNumber from = new PhoneNumber(twilioTrialNumber);
                String message = "Dobili ste novo sporočilo s spletne strani adapp:\n" +
                        sendingDataDto.getMessage() + "\n\n" +
                        "Kontakt pošiljatelja:\n" +
                        fromUser.getPhoneNumber() + "\n" +
                        fromUser.getEmail() + "\n";

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
