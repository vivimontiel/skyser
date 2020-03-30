package skyser.objects;

import java.util.Date;

public class Message{
    
    private int id_mess;
    private Date dateOfSent;
    private String message, receiver,sender;
    private double note;

    public Message(int id_mess,String receiver,String sender,Date dateOfSent,String message){
        this.id_mess = id_mess;
        this.receiver = receiver;
        this.sender = sender;
        this.dateOfSent = dateOfSent;
        this.message = message;
    }

    public Message(){}
    public int getIdMess(){
        return id_mess;
    }
    public String getReceiver(){
        return receiver;
    }
    public String getSender(){return sender;}
    public Date getDateOfMessage(){
        return dateOfSent;
    }
    public String getMessage(){
        return message;
    }
} 