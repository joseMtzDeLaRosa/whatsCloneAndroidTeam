package teclag.c17130049.whatsappclone.providers;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import teclag.c17130049.whatsappclone.models.Message;

public class MessagesProvider {

    CollectionReference mCollection;


    public  MessagesProvider (){
         mCollection= FirebaseFirestore.getInstance().collection("Messages");
    }


    public Task<Void> create(Message message){

        DocumentReference document =  mCollection.document();
        message.setId(document.getId());
        return document.set(message);


    }

    public Query getMessageByChat(String idchat){
        return mCollection.whereEqualTo("idChat",idchat).orderBy("timestamp",Query.Direction.ASCENDING);

    }


}
