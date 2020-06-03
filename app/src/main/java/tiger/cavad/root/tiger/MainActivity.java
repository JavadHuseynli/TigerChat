package tiger.cavad.root.tiger;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.quickblox.auth.QBAuth;
import com.quickblox.auth.session.QBSession;
import com.quickblox.auth.session.QBSettings;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;
import com.rengwuxian.materialedittext.MaterialEditText;

public class MainActivity extends AppCompatActivity {
    static  final String APP_ID="73648";
    static  final String AUTH_KEY="Wc6eksZvcuTapgy";
    static  final String AUTH_SECRET="5T6kSRHCMJCEaO2";
    static  final String ACCOUNT_KEY="pH9E8fy-LqmijXaBZZRK";
    Button btnregister , btnsigup;
    EditText edituser , editpass ;
    RelativeLayout rootLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnregister = (Button) findViewById(R.id.btnRegister);
        btnsigup = (Button)findViewById(R.id.btnSigİn);

        rootLayout =(RelativeLayout)findViewById(R.id.root_layout);

        btnregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showRegisterDialog();
            }
        });

        btnsigup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSignUp();
            }
        });

        intilizeFrameWork();
        registersesion();
    }


    private void registersesion() {
        QBAuth.createSession().performAsync(new QBEntityCallback<QBSession>() {
            @Override
            public void onSuccess(QBSession qbSession, Bundle bundle) {

            }

            @Override
            public void onError(QBResponseException e) {
                Log.e("Error",e.getMessage());
            }
        });
    }

    private void showSignUp() {

        AlertDialog.Builder dialog = new AlertDialog.Builder(this);

        dialog.setTitle("Sign İN") ;

        LayoutInflater inflater  = LayoutInflater.from(this);
        View register_layout  = inflater.inflate(R.layout.layout_signup, null);

        final MaterialEditText Phone= register_layout.findViewById(R.id.edtName);
        final MaterialEditText Password = register_layout.findViewById(R.id.edtPassword);


        dialog.setView(register_layout);


        dialog.setPositiveButton("SIGN İN", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, final int i) {

                dialogInterface.dismiss();

                if(TextUtils.isEmpty(Phone.getText().toString())){
                    Snackbar.make(rootLayout,"Please enter Phone or Password" , Snackbar.LENGTH_SHORT).show();
                    return;

                }
                final String phone = Phone.getText().toString();
                final String Pass = Password.getText().toString();

                QBUser qbUser = new QBUser(phone,Pass);
                QBUsers.signIn(qbUser).performAsync(new QBEntityCallback<QBUser>() {

                    @Override
                    public void onSuccess(QBUser qbUser, Bundle bundle) {

                        Toast.makeText(getBaseContext(),"Əməliyyat uğurlu aparılıb",Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(MainActivity.this,ChatDialogActivity.class);
                        intent.putExtra("user",phone);
                        intent.putExtra("password",Pass);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onError(QBResponseException e) {
                        Toast.makeText(getBaseContext(),""+e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        dialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        dialog.show();

    }

    private void showRegisterDialog() {

        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);

        dialog.setTitle("REGISTER") ;

        LayoutInflater inflater  = LayoutInflater.from(this);
        View register_layout  = inflater.inflate(R.layout.layout_register, null);

        final MaterialEditText Name = register_layout.findViewById(R.id.edtFullName);
//        final MaterialEditText Age = register_layout.findViewById(R.id.edtAge);
//        final MaterialEditText Location = register_layout.findViewById(R.id.edtLocation);
        final MaterialEditText Phone = register_layout.findViewById(R.id.edtPhone);
        final MaterialEditText Password = register_layout.findViewById(R.id.edtPassword);


        dialog.setView(register_layout);

        dialog.setPositiveButton("REGISTER", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {


                String name = Name.getText().toString();
                String phone = Phone.getText().toString();
                String Pass = Password.getText().toString();

                QBUser qbUser = new QBUser(phone,Pass);
                qbUser.setFullName(name);

                QBUsers.signUp(qbUser).performAsync(new QBEntityCallback<QBUser>() {

                    @Override
                    public void onSuccess(QBUser qbUser, Bundle bundle) {

                        Toast.makeText(getBaseContext(),"Əməliyyat uğurlu aparılıb",Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onError(QBResponseException e) {
                        Toast.makeText(getBaseContext(),""+e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
                dialogInterface.dismiss();

            }
        });

        dialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        dialog.show();
    }


    private void intilizeFrameWork() {
        QBSettings.getInstance().init(getApplicationContext(),APP_ID,AUTH_KEY,AUTH_SECRET);
        QBSettings.getInstance().setAccountKey(ACCOUNT_KEY);

    }
}
