package tiger.cavad.root.tiger.Common;

import com.quickblox.users.model.QBUser;

import java.util.List;

import tiger.cavad.root.tiger.Holder.QBUsersHolder;

public class Common {

    public  static  final String DIALOG_EXTRA="Dialogs";
    public static String createChatDialogName(List<Integer> qbUsers){

        List<QBUser> qbUsers1 = QBUsersHolder.getInstance().getUsersByIds(qbUsers);
        StringBuilder name = new StringBuilder();
        for (QBUser user : qbUsers1) {
            name.append(user.getFullName()).append(" ");
        }

        if(name.length()> 10 ) {
            name = name.replace(10, name.length() - 1, "....");
        }

        return name.toString();


    }

}
