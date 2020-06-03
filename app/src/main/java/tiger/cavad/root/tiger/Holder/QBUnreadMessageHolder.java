package tiger.cavad.root.tiger.Holder;

import android.os.Bundle;

/**
 * Created by root on 7/25/18.
 */

public class QBUnreadMessageHolder {

    private static  QBUnreadMessageHolder instance ;
    private Bundle bundle;

    public static synchronized  QBUnreadMessageHolder getInstance(){
        QBUnreadMessageHolder qbChatDialogHolder ;
        synchronized (QBChatDialogHolder.class){
            if (instance == null )
                instance = new QBUnreadMessageHolder();
        }
        qbChatDialogHolder = instance;

        return qbChatDialogHolder;
    }


    private  QBUnreadMessageHolder(){
         bundle = new Bundle();

    }

    public void setBundle(Bundle bundle){

        this.bundle = bundle;

    }

    public Bundle getBundle(){
        return  this.bundle;

    }

    public  int getUnreadMessageByDialogId(String id){

        return this.bundle.getInt(id);

    }


}
