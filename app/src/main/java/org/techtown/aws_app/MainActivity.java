package org.techtown.aws_app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.amplifyframework.AmplifyException;
import com.amplifyframework.api.aws.AWSApiPlugin;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.AWSDataStorePlugin;
import com.amplifyframework.datastore.DataStoreChannelEventName;
import com.amplifyframework.datastore.appsync.ModelWithMetadata;
import com.amplifyframework.datastore.generated.model.Priority;
import com.amplifyframework.datastore.generated.model.Todo;
import com.amplifyframework.hub.HubChannel;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            Amplify.addPlugin(new AWSDataStorePlugin());
            Amplify.addPlugin(new AWSApiPlugin());
            Amplify.configure(getApplicationContext());

            Log.i("Tutorial", "Initialized Amplify");
        } catch (AmplifyException e) {
            Log.e("Tutorial", "Could not initialize Amplify", e);
        }

        Amplify.Hub.subscribe(
                HubChannel.DATASTORE,
                event -> DataStoreChannelEventName.RECEIVED_FROM_CLOUD.toString().equals(event.getName()),
                event -> {
                    ModelWithMetadata<?> modelWithMetadata = (ModelWithMetadata<?>) event.getData();
                    Todo todo = (Todo) modelWithMetadata.getModel();

                    Log.i("Tutorial", "==== Todo ====");
                    Log.i("Tutorial", "Name: " + todo.getName());

                    if (todo.getPriority() != null) {
                        Log.i("Tutorial", "Priority: " + todo.getPriority().toString());
                    }

                    if (todo.getDescription() != null) {
                        Log.i("Tutorial", "Description: " + todo.getDescription());
                    }
                }
        );

    }
}
