package tiger.cavad.root.tiger.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.quickblox.users.model.QBUser;

import java.util.ArrayList;

import tiger.cavad.root.tiger.R;

import static tiger.cavad.root.tiger.R.layout.support_simple_spinner_dropdown_item;

public class ListUserAdapter extends BaseAdapter {

    private Context context ;
    private ArrayList<QBUser> qbUsers;


    public ListUserAdapter(Context context, ArrayList<QBUser> qbUsers) {
        this.context = context;
        this.qbUsers = qbUsers;
    }

    @Override
    public int getCount() {
        return qbUsers.size();
    }

    @Override
    public Object getItem(int position) {
        return  qbUsers.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView ;
        if(convertView == null ){

            LayoutInflater inflater  = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            view = inflater.inflate(android.R.layout.simple_list_item_multiple_choice,null);
            TextView textView  = view.findViewById(android.R.id.text1);
            textView.setText(qbUsers.get(position).getLogin());

        }

        return view;
    }
}
