package tiger.cavad.root.tiger.Holder;

import com.quickblox.chat.model.QBChatMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by root on 7/17/18.
 */

public class QBChatMessagesHolder {

     private  static QBChatMessagesHolder instance ;
     private static HashMap<String,ArrayList<QBChatMessage>> qbChatMessageArray ;

    public static synchronized QBChatMessagesHolder getInstance()
    {
        QBChatMessagesHolder qbChatMessagesHolder ;

        synchronized (QBChatMessagesHolder.class){

            if(instance == null)
            instance = new QBChatMessagesHolder();

            qbChatMessagesHolder = instance ;
        }

        return qbChatMessagesHolder;
    }


    private  QBChatMessagesHolder(){
        this.qbChatMessageArray = new HashMap<>();

    }

    public void puMessages(String dialogId,ArrayList<QBChatMessage> qbChatMessages){


        this.qbChatMessageArray.put(dialogId,qbChatMessages);

    }

    public  void  putMessage(String dialogId, QBChatMessage qbChatMessage){

        List<QBChatMessage> lstResult =  (List)this.qbChatMessageArray.get(dialogId);

        lstResult.add(qbChatMessage);

        ArrayList<QBChatMessage> lstAdded = new ArrayList(lstResult.size());
        lstAdded.addAll(lstResult);
        puMessages(dialogId,lstAdded);

    }


    public ArrayList<QBChatMessage> getChatMessagesByDialogId(String Dialogid){
    return (ArrayList<QBChatMessage>)this.qbChatMessageArray.get(Dialogid);

    }
}
