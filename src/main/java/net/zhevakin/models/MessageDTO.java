package net.zhevakin.models;

import lombok.Getter;
import lombok.Setter;

import java.text.SimpleDateFormat;
import java.util.Date;

@Getter
@Setter
public class MessageDTO {

    private String user;

    private String text;

    private String dateSend;

    public void setDateSend(Date dateSend) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        this.dateSend = simpleDateFormat.format(dateSend);
    }

}
