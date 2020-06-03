package tiger.cavad.root.tiger;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.quickblox.chat.QBChatService;
import com.quickblox.chat.QBIncomingMessagesManager;
import com.quickblox.chat.QBRestChatService;
import com.quickblox.chat.exception.QBChatException;
import com.quickblox.chat.listeners.QBChatDialogMessageListener;
import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.chat.model.QBChatMessage;
import com.quickblox.chat.model.QBDialogType;
import com.quickblox.chat.request.QBMessageGetBuilder;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smackx.muc.DiscussionHistory;

import java.util.ArrayList;

import tiger.cavad.root.tiger.Adapter.ChatMessageAdapter;
import tiger.cavad.root.tiger.Common.Common;
import tiger.cavad.root.tiger.Holder.QBChatMessagesHolder;

public class ChatMesageActivity extends AppCompatActivity  implements QBChatDialogMessageListener {

    QBChatDialog qbChatDialog ;
    ListView lstChatMessages ;
    ImageButton submitButtons ;
    EditText edtContent ;
    ChatMessageAdapter adapter;


    @Override
    protected void onDestroy() {
        super.onDestroy();
        qbChatDialog.removeMessageListrener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        qbChatDialog.removeMessageListrener(this);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_mesage);
        initViews();

        initChatDialog();

        retrieveMessage();


        submitButtons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                QBChatMessage chatMessage  = new QBChatMessage();
                chatMessage.setBody(edtContent.getText().toString());
                chatMessage.setSenderId(QBChatService.getInstance().getUser().getId());
                chatMessage.setSaveToHistory(true);

                try {
                    qbChatDialog.sendMessage(chatMessage);
                } catch (SmackException.NotConnectedException e) {
                    e.printStackTrace();
                }

//                //Put Message to cache
//
                if (qbChatDialog.getType()== QBDialogType.PRIVATE){

                    QBChatMessagesHolder.getInstance().putMessage(qbChatDialog.getDialogId(),chatMessage);
                    ArrayList<QBChatMessage> message = QBChatMessagesHolder.getInstance()
                            .getChatMessagesByDialogId(qbChatDialog.getDialogId());
                    adapter = new ChatMessageAdapter(getBaseContext(),message);
                    lstChatMessages.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
                //Remove text from edit

                edtContent.setText("");
                edtContent.setFocusable(true);
            }
        });

    }
    private void retrieveMessage() {
        QBMessageGetBuilder qbMessageGetBuilder  =  new QBMessageGetBuilder();
        qbMessageGetBuilder.setLimit(1000);
        if(qbChatDialog != null){
            QBRestChatService.getDialogMessages(qbChatDialog,qbMessageGetBuilder).performAsync(new QBEntityCallback<ArrayList<QBChatMessage>>() {
                @Override
                public void onSuccess(ArrayList<QBChatMessage> qbChatMessage, Bundle bundle) {
                    QBChatMessagesHolder.getInstance().puMessages(qbChatDialog.getDialogId(),qbChatMessage);

                    adapter = new ChatMessageAdapter(getBaseContext(),qbChatMessage);
                    lstChatMessages.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                }

                @Override
                public void onError(QBResponseException e) {

                }
            });
        }
    }

    private void initChatDialog() {

        qbChatDialog = (QBChatDialog) getIntent().getSerializableExtra(Common.DIALOG_EXTRA);
        qbChatDialog.initForChat(QBChatService.getInstance());

        //REGISTER LISTENER INCOMING MESSAGE
        QBIncomingMessagesManager incomingMessagesManager = QBChatService.getInstance().getIncomingMessagesManager();
        incomingMessagesManager.addDialogMessageListener(new QBChatDialogMessageListener() {
            @Override
            public void processMessage(String s, QBChatMessage qbChatMessage, Integer ınteger) {

            }

            @Override
            public void processError(String s, QBChatException e, QBChatMessage qbChatMessage, Integer ınteger) {

            }
        });

        // Add Join group to enable group chat
        if(qbChatDialog.getType() == QBDialogType.PUBLIC_GROUP || qbChatDialog.getType()==QBDialogType.GROUP){
            DiscussionHistory discussionHistory = new DiscussionHistory();
            discussionHistory.setMaxStanzas(0);

            qbChatDialog.join(discussionHistory, new QBEntityCallback() {
                @Override
                public void onSuccess(Object o, Bundle bundle) {

                }

                @Override
                public void onError(QBResponseException e) {

                }
            });
        }

        qbChatDialog.addMessageListener(this);
    }

    private void initViews() {

        lstChatMessages = (ListView) findViewById(R.id.list_of_message);
        submitButtons = (ImageButton) findViewById(R.id.send_button);
        edtContent= (EditText) findViewById(R.id.edt_content);
    }
    @Override
    public void processMessage(String s, QBChatMessage qbChatMessage, Integer ınteger) {
        QBChatMessagesHolder.getInstance().putMessage(qbChatMessage.getDialogId(),qbChatMessage);
        ArrayList<QBChatMessage> message = QBChatMessagesHolder.getInstance().getChatMessagesByDialogId(qbChatMessage.getDialogId());
        adapter = new ChatMessageAdapter(getBaseContext(),message);
        lstChatMessages.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }


    @Override
    public void processError(String s, QBChatException e, QBChatMessage qbChatMessage, Integer ınteger) {

    }
}
