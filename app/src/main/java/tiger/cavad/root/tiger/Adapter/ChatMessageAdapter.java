package tiger.cavad.root.tiger.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.github.library.bubbleview.BubbleTextView;
import com.quickblox.chat.QBChatService;
import com.quickblox.chat.model.QBChatMessage;

import java.util.ArrayList;

import tiger.cavad.root.tiger.Holder.QBUserHolder;
import tiger.cavad.root.tiger.R;


/**
 * Created by root on 7/17/18.
 */

public class ChatMessageAdapter extends BaseAdapter {

    private Context context ;
    private ArrayList<QBChatMessage> qbChatMessages ;

    public ChatMessageAdapter(Context context, ArrayList<QBChatMessage> qbChatMessages) {

        this.context = context;
        this.qbChatMessages = qbChatMessages;

    }

    @Override
    public int getCount() {
        return qbChatMessages.size();
    }

    @Override
    public Object getItem(int i) {
        return qbChatMessages.get(i) ;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View contentview, ViewGroup viewGroup) {

        View  view  = contentview ;
         if(contentview  == null )
         {

         LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
         if(qbChatMessages.get(i).getSenderId().equals(QBChatService.getInstance().getUser().getId()))
         {
             view = inflater.inflate(R.layout.lst_send_message,null);
             BubbleTextView bubbleTextView = (BubbleTextView) view.findViewById(R.id.message_content);
             bubbleTextView.setText(qbChatMessages.get(i).getBody());
         }else
             {
                 view = inflater.inflate(R.layout.lst_recv_message,null);
                 BubbleTextView bubbleTextView = (BubbleTextView) view.findViewById(R.id.message_content);
                 bubbleTextView.setText(qbChatMessages.get(i).getBody());
                 TextView txtname = view.findViewById(R.id.message_user);
                 txtname.setText(QBUserHolder.getInstance().getUserById(qbChatMessages.get(i).getSenderId()).getFullName());
             }

         }
        return view;
    }
}
