package org.epf.hadoop.colfil2;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.HashSet;

public class UserRelationReducer extends Reducer<UserPair, Text, UserPair, Text> {
    private Text result = new Text();

    @Override
    protected void reduce(UserPair key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
        HashSet<String> commonRelations = new HashSet<>();
        boolean isDirectlyConnected = false;

        for (Text value : values) {
            if (value.toString().equals("-1")) {
                isDirectlyConnected = true;
            } else {
                commonRelations.add(value.toString());
            }
        }

        // Ignorer les paires directement connectées
        if (isDirectlyConnected) return;

        // Écrire la paire et le nombre de relations communes
        result.set(String.valueOf(commonRelations.size()));
        context.write(key, result);
    }
}