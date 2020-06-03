package tiger.cavad.root.tiger;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.quickblox.auth.QBAuth;
import com.quickblox.auth.session.BaseService;
import com.quickblox.auth.session.QBSession;
import com.quickblox.chat.QBChat;
import com.quickblox.chat.QBChatService;
import com.quickblox.chat.QBIncomingMessagesManager;
import com.quickblox.chat.QBRestChatService;
import com.quickblox.chat.QBSystemMessagesManager;
import com.quickblox.chat.exception.QBChatException;
import com.quickblox.chat.listeners.QBChatDialogMessageListener;
import com.quickblox.chat.listeners.QBSystemMessageListener;
import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.chat.model.QBChatMessage;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.BaseServiceException;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.core.request.QBRequestGetBuilder;
import com.quickblox.users.model.QBUser;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import tiger.cavad.root.tiger.Adapter.ChatDialogAdapter;
import tiger.cavad.root.tiger.Common.Common;
import tiger.cavad.root.tiger.Holder.QBChatDialogHolder;

public class ChatDialogActivity extends AppCompatActivity  implements QBSystemMessageListener, QBChatDialogMessageListener {

    ListView lstChatDialog;
    Button button ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_dialog);
        lstChatDialog  = findViewById(R.id.lstChatDialogs);
        button = findViewById(R.id.chatdialog_adduser);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChatDialogActivity.this,ListUsersActivity.class);
                startActivity(intent);

            }
        });
        createSessionforChat();
        lstChatDialog= (ListView) findViewById(R.id.lstChatDialogs);
        lstChatDialog.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                QBChatDialog qbChatDialog = (QBChatDialog) lstChatDialog.getAdapter().getItem(i);
                Intent intent = new Intent(ChatDialogActivity.this, ChatMesageActivity.class);
                intent.putExtra(Common.DIALOG_EXTRA,qbChatDialog);
                startActivity(intent);
            }
        });
        loadChatDialog();

    }


    private void loadChatDialog() {
        QBRequestGetBuilder requestBuilder = new QBRequestGetBuilder();
        requestBuilder.setLimit(100);

        QBRestChatService.getChatDialogs(null,requestBuilder).performAsync(new QBEntityCallback<ArrayList<QBChatDialog>>() {
            @Override
            public void onSuccess(ArrayList<QBChatDialog> qbChatDialogs, Bundle bundle) {

                ChatDialogAdapter adapter = new ChatDialogAdapter(getBaseContext(),qbChatDialogs);
                lstChatDialog.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(QBResponseException e) {
                    Log.e("ERROR",e.getMessage());

            }
        });
    }

    private void createSessionforChat() {
        final ProgressDialog mDialog = new ProgressDialog(ChatDialogActivity.this);
        mDialog.setMessage("Please waiting ..... ");
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.show();
        String user ,password;
        user = getIntent().getStringExtra("user");
        password= getIntent().getStringExtra("password");

        final QBUser qbUser = new QBUser(user,password);
        QBAuth.createSession(qbUser).performAsync(new QBEntityCallback<QBSession>() {
            @Override
            public void onSuccess(QBSession qbSession, Bundle bundle) {
                qbUser.setId(qbSession.getUserId());
                try{
                    qbUser.setPassword(BaseService.getBaseService().getToken());
                }catch (BaseServiceException e){
                    e.printStackTrace();
                }
                QBChatService.getInstance().login(qbUser, new QBEntityCallback() {
                    @Override
                    public void onSuccess(Object o, Bundle bundle) {

                        mDialog.dismiss();
                    }

                    @Override
                    public void onError(QBResponseException e) {
                        Log.e("ERROR",""+e.getMessage());
                    }
                });
            }

            @Override
            public void onError(QBResponseException e) {

            }
        });
    }

    @Override
    public void processMessage(String s, QBChatMessage qbChatMessage, Integer ınteger) {
        loadChatDialog();


    }

    @Override
    public void processError(String s, QBChatException e, QBChatMessage qbChatMessage, Integer ınteger) {

    }

    @Override
    public void processMessage(QBChatMessage qbChatMessage) {
        QBRestChatService.getChatDialogById(qbChatMessage.getBody()).performAsync(new QBEntityCallback<QBChatDialog>() {
            @Override
            public void onSuccess(QBChatDialog qbChatDialog, Bundle bundle) {

                QBChatDialogHolder.getInstance().putDialog(qbChatDialog);
                ArrayList<QBChatDialog> adapterSource   = QBChatDialogHolder.getInstance().getAllChatDialogs();
                ChatDialogAdapter adapters = new ChatDialogAdapter(getBaseContext(),adapterSource);
                lstChatDialog.setAdapter(adapters);
                adapters.notifyDataSetChanged();
            }

            @Override
            public void onError(QBResponseException e) {

            }
        });
    }

    @Override
    public void processError(QBChatException e, QBChatMessage qbChatMessage) {

    }
}
