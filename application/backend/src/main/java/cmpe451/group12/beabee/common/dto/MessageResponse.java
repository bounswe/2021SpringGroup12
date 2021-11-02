package cmpe451.group12.beabee.common.dto;

import cmpe451.group12.beabee.common.enums.MessageType;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class MessageResponse {
    public final String message;
    public final MessageType messageType;

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }

        if (!(o instanceof MessageResponse)) {
            return false;
        }
        MessageResponse other = (MessageResponse) o;
        return other.messageType.equals(this.messageType) && other.message.equals(this.message);
    }

    @Override
    public String toString() {
        return String.format(this.message + " " + this.messageType);
    }
}