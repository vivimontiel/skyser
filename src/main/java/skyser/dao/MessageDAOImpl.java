package skyser.dao;

import skyser.objects.Message;
import skyser.objects.Passenger;

import java.util.*;

public class MessageDAOImpl implements MessageDAO{
    
    List<Message> listMess;
    
    public MessageDAOImpl(List m){
        listMess = new ArrayList<Message>();
    }

    @Override
    public List<Message> getMessageSender(Passenger p){
        List<Message> list = new ArrayList<Message>();
        for(Message m : listMess){
            /*if(m.getSender() == p.getId() ){
                list.add(m);
            }*/
        }
        return list;
    }

    @Override
    public List<Message> getMessageReceiver(Passenger p){
        List<Message> list = new ArrayList<Message>();
        for(Message m : listMess){
            /*if(m.getReceiver() == p.getId() ){
                list.add(m);
            }*/
        }
        return list;
    }

    @Override
    public boolean putMessage(Message m){
        this.listMess.add(m);
        return true;
    }

    @Override
    public boolean deleteMessage(int id){
        listMess.remove(listMess.get(id));
		return true;
    }

    
}