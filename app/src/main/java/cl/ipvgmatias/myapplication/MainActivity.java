package cl.ipvgmatias.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    Button Btn_registro;
    EditText pass;
    EditText correo;
    FirebaseFirestore mFirestore;
    FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();;

        correo = findViewById(R.id.editTextTextEmailAddress);
        pass = findViewById(R.id.editTextTextPassword);
        Btn_registro = findViewById(R.id.button);

        Btn_registro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String user = correo.getText().toString().trim();
                String passe = pass.getText().toString().trim();

                if(user.isEmpty() && passe.isEmpty()){
                    Toast.makeText(MainActivity.this, "Complete los datos", Toast.LENGTH_SHORT).show();

                }else {
                    registreuser(user, passe);
                    

                }


            }
        });
    }

    private void registreuser(String user, String passe) {
        mAuth.createUserWithEmailAndPassword(user, passe).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                String id = mAuth.getCurrentUser().getUid();
                Map<String, String> map = new HashMap<>();
                map.put("id",id);
                map.put("email",user);
                map.put("password",passe);;

                mFirestore.collection("user").document(id).set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        finish();
                        startActivity(new Intent(MainActivity.this,MainActivity2.class));
                        Toast.makeText(MainActivity.this, "Usuario Registrado con exito", Toast.LENGTH_SHORT).show();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "Error al registrar", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this, "Error al registrar", Toast.LENGTH_SHORT).show();
            }
        });

    }
}