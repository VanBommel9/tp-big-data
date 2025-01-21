package org.epf.hadoop.colfil2;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;
import java.util.Arrays;

public class UserRelationMapper extends Mapper<Object, Text, UserPair, Text> {
    private UserPair userPair = new UserPair();
    private Text relationValue = new Text();

    @Override
    protected void map(Object key, Text value, Context context) throws IOException, InterruptedException {
        String[] line = value.toString().split("\\t");
        if (line.length != 2) return;

        String user = line[0];
        String[] friends = line[1].split(",");

        // Émettre chaque paire d'amis potentiels
        for (int i = 0; i < friends.length; i++) {
            for (int j = i + 1; j < friends.length; j++) {
                userPair = new UserPair(friends[i].trim(), friends[j].trim());
                relationValue.set(user);
                context.write(userPair, relationValue);
            }

            // Émettre la relation directe
            userPair = new UserPair(user, friends[i].trim());
            relationValue.set("-1");
            context.write(userPair, relationValue);
        }
    }
}