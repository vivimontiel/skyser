package skyser.dao;

import skyser.objects.Message;
import skyser.objects.Passenger;

import java.util.List;

public interface MessageDAO {

    /**
     * @return the list of messages assigned to a specific sender
     */
    public List<Message> getMessageSender(Passenger p);
    /**
     * @return the list of messages assigned to a specific receiver
     */
    public List<Message> getMessageReceiver(Passenger p);
    /**
     * @return true if the message is received
     */
    public boolean putMessage(Message m);
    /**
     * @return true if the user has been delete
     */
    public boolean deleteMessage(int id);

}