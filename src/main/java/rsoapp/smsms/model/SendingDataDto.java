package rsoapp.smsms.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SendingDataDto {

    private String fromId;
    private String toId;
    private String message;
}
