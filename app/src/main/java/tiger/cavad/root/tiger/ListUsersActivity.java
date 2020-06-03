package tiger.cavad.root.tiger;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.quickblox.chat.QBChatService;
import com.quickblox.chat.QBRestChatService;
import com.quickblox.chat.QBSystemMessagesManager;
import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.chat.model.QBChatMessage;
import com.quickblox.chat.model.QBDialogType;
import com.quickblox.chat.utils.DialogUtils;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;

import org.jivesoftware.smack.SmackException;

import java.util.ArrayList;

import tiger.cavad.root.tiger.Adapter.ListUserAdapter;
import tiger.cavad.root.tiger.Common.Common;

public class ListUsersActivity extends AppCompatActivity {

    ListView lsUsers ;
    Button createChat ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_users);


        retrieveAllUser();
        lsUsers = (ListView) findViewById(R.id.ListUsers);
        lsUsers.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        createChat = (Button) findViewById(R.id.btn_create_chat);
        createChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int countChoice = lsUsers.getCount();
                if(lsUsers.getCheckedItemPositions().size()==1)
                    cratePrivateChat(lsUsers.getCheckedItemPositions());
                else if (lsUsers.getCheckedItemPositions().size()> 1)
                    createGroupChat(lsUsers.getCheckedItemPositions());
                else
                    Toast.makeText(ListUsersActivity.this,"Please selecet friend to chat", Toast.LENGTH_SHORT).show();
            }

        });
    }


    private void createGroupChat(SparseBooleanArray checkedItemPositions) {

        final ProgressDialog mDialog = new ProgressDialog(ListUsersActivity.this);
        mDialog.setMessage("Please waiting ...");
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.show();

        int countChoice  = lsUsers.getCount();
        ArrayList<Integer> occupanIdsList = new ArrayList<>();
        for(int i =0 ; i<countChoice;i++){
            if(checkedItemPositions.get(i)){
                QBUser user = (QBUser) lsUsers.getItemAtPosition(i);
                occupanIdsList.add(user.getId());

            }
        }
        // Chat Dialog
        QBChatDialog dialog =  new QBChatDialog();
        dialog.setName(Common.createChatDialogName(occupanIdsList));
        dialog.setType(QBDialogType.GROUP);
        dialog.setOccupantsIds(occupanIdsList);

        QBRestChatService.createChatDialog(dialog).performAsync(new QBEntityCallback<QBChatDialog>() {
            @Override
            public void onSuccess(QBChatDialog qbChatDialog, Bundle bundle) {
                mDialog.dismiss();
                Toast.makeText(getBaseContext(), "Create chat dialog successfully", Toast.LENGTH_SHORT).show();

                QBSystemMessagesManager qbSystemMessagesManager = QBChatService.getInstance().getSystemMessagesManager();
                QBChatMessage chatMessage = new QBChatMessage();
                chatMessage.setRecipientId(qbChatDialog.getId());

                for(int i = 0 ; i< qbChatDialog.getOccupants().size();i++){
                    chatMessage.setRecipientId(qbChatDialog.getOccupants().get(i));
                    chatMessage.setBody(qbChatDialog.getDialogId());

                    try {
                        qbSystemMessagesManager.sendSystemMessage(chatMessage);

                    } catch (SmackException.NotConnectedException e) {
                        e.printStackTrace();
                    }

                }

                finish();
            }

            @Override
            public void onError(QBResponseException e) {
                Log.e("ERROR", e.getMessage());
            }
        });

    }

    private void cratePrivateChat(SparseBooleanArray checkedItemPositions) {
        final ProgressDialog mDialog = new ProgressDialog(ListUsersActivity.this);
        mDialog.setMessage("Please waiting ...");
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.show();

        int countChoice  = lsUsers .getCount();
        for(int i =0 ; i<countChoice;i++){
            if(checkedItemPositions.get(i)){
                final QBUser user = (QBUser) lsUsers.getItemAtPosition(i);
                QBChatDialog dialog = DialogUtils.buildPrivateDialog(user.getId());

                QBRestChatService.createChatDialog(dialog).performAsync(new QBEntityCallback<QBChatDialog>() {
                    @Override
                    public void onSuccess(QBChatDialog qbChatDialog, Bundle bundle) {
                        mDialog.dismiss();
                        Toast.makeText(getBaseContext(), "Create chat dialog successfully", Toast.LENGTH_SHORT).show();

                        QBSystemMessagesManager qbSystemMessagesManager = QBChatService.getInstance().getSystemMessagesManager();
                        QBChatMessage chatMessage = new QBChatMessage();
                        chatMessage.setRecipientId(user.getId());
                        chatMessage.setBody(qbChatDialog.getDialogId());

                        try {
                            qbSystemMessagesManager.sendSystemMessage(chatMessage);

                        } catch (SmackException.NotConnectedException e) {
                            e.printStackTrace();
                        }

                        finish();
                    }

                    @Override
                    public void onError(QBResponseException e) {
                        Log.e("ERROR", e.getMessage());
                    }
                });

            }
        }
    }



    private void retrieveAllUser()
    {

        QBUsers.getUsers(null).performAsync(new QBEntityCallback<ArrayList<QBUser>>() {
            @Override
            public void onSuccess(ArrayList<QBUser> qbUsers, Bundle bundle) {

                ArrayList<QBUser> qbUserWithoutCurrent = new ArrayList<QBUser>();
                for(QBUser user : qbUsers){

                    if(!user.getLogin().equals(QBChatService.getInstance().getUser().getLogin()))
                    {
                        qbUserWithoutCurrent.add(user);
                    }

                    ListUserAdapter adapter = new ListUserAdapter(getBaseContext(),qbUserWithoutCurrent);

                    lsUsers.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onError(QBResponseException e) {
                Log.e("ERROR",e.getMessage());
            }
        });
    }
}
