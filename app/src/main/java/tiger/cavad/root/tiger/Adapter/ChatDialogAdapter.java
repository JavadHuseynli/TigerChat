package tiger.cavad.root.tiger.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.quickblox.chat.model.QBChatDialog;

import java.util.ArrayList;

import tiger.cavad.root.tiger.R;

public class ChatDialogAdapter extends BaseAdapter {

    private Context context ;
    private ArrayList<QBChatDialog> qbChatDialogs ;

    public ChatDialogAdapter(Context context, ArrayList<QBChatDialog> qbChatDialogs) {
        this.context = context;
        this.qbChatDialogs = qbChatDialogs;
    }

    @Override
    public int getCount() {
        return qbChatDialogs.size();
    }

    @Override
    public Object getItem(int position) {
        return qbChatDialogs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView ;

        if(view == null ){

            LayoutInflater inflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_chat_dia,null);

            TextView txtTitle,txtMessage ;
            ImageView imageView ,  unreadImageView;

            txtMessage = (TextView)view.findViewById(R.id.list_chat_dialog_message);
            txtTitle = (TextView)view.findViewById(R.id.list_chat_dialog_title);
            imageView =(ImageView) view.findViewById(R.id.image_chatDialog);
            unreadImageView = (ImageView)  view.findViewById(R.id.image_unread);

            txtMessage.setText(qbChatDialogs.get(position).getLastMessage());
            txtTitle.setText(qbChatDialogs.get(position).getName());

            ColorGenerator generator = ColorGenerator.MATERIAL;
            int randomcolor = generator.getRandomColor();

            TextDrawable.IBuilder builder = TextDrawable.builder().beginConfig()
                    .withBorder(4)
                    .endConfig()
                    .round();


            TextDrawable drawable = builder.build(txtTitle.getText().toString().substring(0,1).toUpperCase(),randomcolor);

            imageView.setImageDrawable(drawable);
        }



        return view;
    }
}
